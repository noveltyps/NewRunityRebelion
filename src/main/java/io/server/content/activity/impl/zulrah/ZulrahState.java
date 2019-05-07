package io.server.content.activity.impl.zulrah;

import io.server.game.Animation;
import io.server.game.Projectile;
import io.server.game.UpdatePriority;
import io.server.game.task.impl.ObjectPlacementEvent;
import io.server.game.world.World;
import io.server.game.world.entity.combat.strategy.npc.boss.Zulrah;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * Handles all the states for Zulrah.
 *
 * @author Daniel
 */
public enum ZulrahState implements ZulrahInterface<ZulrahActivity> {
	
	ATTACKING() {
		@Override
		public void execute(ZulrahActivity activity) {
			boolean zulrahValid = !activity.zulrah.isDead();
			boolean stateValid = activity.count >= 15 && Utility.random(3) == 0;
			activity.attackable = true;
			if (zulrahValid && stateValid) {
				activity.count = 0;
				activity.state = ZulrahState.DIVE;
				activity.cooldown(2);
				activity.attackable = false;
				return;
			}
			if (!activity.zulrah.getCombat().isAttacking(activity.player)) {
				activity.zulrah.getCombat().attack(activity.player);
			}
			activity.cooldown(2);
			activity.count++;
		}
	},
	INITIALIZE() {
		@Override
		public void execute(ZulrahActivity activity) {
			Player player = activity.player;
			if (activity.count == 1) {
				activity.zulrah = new Npc(2042, new Position(2266, 3073));
				activity.attackable = false;
				activity.zulrah.locking.lock();
				activity.zulrah.owner = player;
				activity.add(activity.zulrah);
				activity.zulrah.definition.setRetaliate(false);
				activity.zulrah.animate(new Animation(5071));
			} else if (activity.count == 2) {
				activity.zulrah.face(new Position(2268, 3069));
				player.face(Direction.NORTH);
			} else if (activity.count == 3) {
				activity.count = 0;
				activity.state = INITIAL_CLOUDS;
				activity.zulrah.getCombat().reset();
				activity.cooldown(3);
				return;
			}
			activity.count++;
		}
	},
	INITIAL_CLOUDS() {
		@Override
		public void execute(ZulrahActivity activity) {
			Npc zulrah = activity.zulrah;
			int index = activity.count / 3;
			int stage = activity.count % 3;
			if (index >= ZulrahConstants.CLOUD_POSITIONS.length) {
				activity.count = 0;
				activity.state = DIVE;
				activity.zulrah.getCombat().reset();
				activity.cooldown(2);
				return;
			}
			if (stage == 0) {
				zulrah.face(ZulrahConstants.CLOUD_POSITIONS[index]);
			} else if (stage == 1) {
				zulrah.animate(new Animation(5069, UpdatePriority.VERY_HIGH));
				World.sendProjectile(zulrah, ZulrahConstants.CLOUD_POSITIONS[index],
						new Projectile(1045, 1, 35, 85, 40));
			} else if (stage == 2) {
				CustomGameObject cloud = new CustomGameObject(11700, activity.getInstance(),
						ZulrahConstants.CLOUD_POSITIONS[index]);
				World.schedule(new ObjectPlacementEvent(cloud, 35, () -> activity.clouds.remove(cloud)));
				activity.clouds.add(cloud);
			}
			activity.count++;
		}
	},
	POISONOUS_CLOUD() {
		@Override
		public void execute(ZulrahActivity activity) {
			Npc zulrah = activity.zulrah;
			if (activity.target == null) {
				activity.target = activity.getCloudPosition();
			}
			if (activity.clouds.size() >= ZulrahConstants.MAXIMUM_CLOUDS) {
				activity.count = 0;
				activity.state = DIVE;
				activity.cooldown(2);
				return;
			}
			int stage = activity.count % 3;
			if (stage == 0) {
				zulrah.face(activity.target);
			} else if (stage == 1) {
				zulrah.animate(new Animation(5069, UpdatePriority.VERY_HIGH));
				World.sendProjectile(zulrah, activity.target, new Projectile(1045, 1, 35, 85, 40));
			} else if (stage == 2) {
				CustomGameObject cloud = new CustomGameObject(11700, activity.getInstance(), activity.target);
				activity.clouds.add(cloud);
				World.schedule(new ObjectPlacementEvent(cloud, 150, () -> activity.clouds.remove(cloud)));
				activity.target = null;
			}
			activity.count++;
			activity.cooldown(1);
		}
	},
	SNAKELINGS() {
		@Override
		public void execute(ZulrahActivity activity) {
			Npc zulrah = activity.zulrah;
			if (activity.target == null) {
				activity.target = activity.getSnakelingPosition();
			}
			if (activity.snakes.size() >= ZulrahConstants.MAXIMUM_SNAKELINGS) {
				activity.count = 0;
				activity.state = DIVE;
				activity.cooldown(2);
				return;
			}
			int stage = activity.count % 3;
			if (stage == 0) {
				zulrah.face(activity.target);
			} else if (stage == 1) {
				zulrah.animate(new Animation(5068, UpdatePriority.VERY_HIGH));
				World.sendProjectile(zulrah, activity.target, new Projectile(1047, 1, 35, 85, 40));
			} else if (stage == 2 && Utility.random(1, 3) == 2) {
				Npc snake = new Npc(2045, activity.target);
				activity.add(snake);
				snake.getCombat().attack(activity.player);
				activity.snakes.add(snake);
				activity.target = null;
			}
			activity.count++;
			activity.cooldown(1);
		}
	},
	DIVE() {
		@Override
		public void execute(ZulrahActivity activity) {
			Npc zulrah = activity.zulrah;
			zulrah.resetFace();
			int count = activity.count % 6;
			if (count == 0) {
				zulrah.locking.lock();
			} else if (count == 2) {
				zulrah.animate(new Animation(5072, UpdatePriority.VERY_HIGH), true);
				activity.player.send(new SendMessage("Zulrah dives into the swamp..."));
			} else if (count == 4) {
				zulrah.setVisible(false);
				int next = RandomUtils.randomExclude(ZulrahConstants.ZULRAH_IDS, zulrah.id);
				if (next == 2042)
					((Zulrah) zulrah.getStrategy()).setRanged();
				else if (next == 2043)
					((Zulrah) zulrah.getStrategy()).setMelee();
				else if (next == 2044)
					((Zulrah) zulrah.getStrategy()).setMagic();
				zulrah.transform(next);
				zulrah.move(RandomUtils.randomExclude(ZulrahConstants.LOCATIONS, zulrah.getPosition()));
			} else if (count == 5) {
				zulrah.locking.unlock();
				zulrah.setVisible(true);
				zulrah.animate(new Animation(5071, UpdatePriority.VERY_HIGH));
				activity.zulrah.interact(activity.player);
				activity.count = 0;
				activity.state = activity.getNextState();
				activity.zulrah.definition.setRetaliate(activity.state == ATTACKING);
				activity.cooldown(2);
				return;
			}
			activity.count++;
		}
	},;
}

interface ZulrahInterface<T> {
	void execute(final T activity);
}