package io.server.content.skill.impl.firemaking;

import java.util.Optional;

import io.server.Config;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.clanchannel.content.ClanTaskKey;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.prestige.PrestigePerk;
import io.server.content.skill.impl.DestructionSkillAction;
import io.server.game.Animation;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;

/**
 * Handles the firemaking action.
 *
 * @author Daniel
 */
public final class FiremakingAction extends DestructionSkillAction {
	private final Item item;
	private final FiremakingData firemaking;

	FiremakingAction(Player player, Item item, FiremakingData firemaking) {
		super(player, Optional.empty(), false);
		this.item = item;
		this.firemaking = firemaking;
	}

	@Override
	public boolean canInit() {
		if (getMob().skills.getMaxLevel(Skill.FIREMAKING) < firemaking.getLevel()) {
			getMob().getPlayer()
					.message("You need a firemaking level of " + firemaking.getLevel() + " to light this log!");
			return false;
		}
		if (Area.inEdgeville(getMob()) || Area.inDonatorZone(getMob()) || Area.inWilderness(getMob())) {
			getMob().getPlayer().message("You can not burn a fire here!");
			return false;
		}
		Region region = getMob().getRegion();
		if (region.containsObject(getMob().getPosition())) {
			getMob().getPlayer().send(new SendMessage("You can't light a fire here!"));
			return false;
		}
		return true;
	}

	@Override
	public Optional<SkillAnimation> animation() {
		return Optional.empty();
	}

	@Override
	public double experience() {
		return firemaking.getExperience() * Config.FIREMAKING_MODIFICATION * 5;
	}

	@Override
	public int skill() {
		return Skill.FIREMAKING;
	}

	@Override
	public void init() {
		if (getMob().isPlayer()) {
			getMob().getPlayer().send(new SendMessage("You attempt to light the log."));
		}
		getMob().animate(new Animation(733));
	}

	@Override
	public double successFactor() {
		return 100 - firemaking.getLevel();
	}

	@Override
	public void onDestruct(boolean success) {
		if (success) {
			Player player = (Player) getMob();
			player.inventory.remove(item);
			Position walkTo = player.getPosition();

			if (TraversalMap.isTraversable(player.getPosition(), Direction.WEST, player.width())) {
				walkTo = walkTo.west();
			} else if (TraversalMap.isTraversable(player.getPosition(), Direction.EAST, player.width())) {
				walkTo = walkTo.east();
			} else if (TraversalMap.isTraversable(player.getPosition(), Direction.SOUTH, player.width())) {
				walkTo = walkTo.north();
			} else if (TraversalMap.isTraversable(player.getPosition(), Direction.NORTH, player.width())) {
				walkTo = walkTo.south();
			}

			if (!player.getPosition().equals(walkTo)) {
				player.movement.walkTo(walkTo);
			}

			if (player.prestige.hasPerk(PrestigePerk.FLAME_ON) && RandomUtils.success(.25)) {
				player.inventory.remove(firemaking.getLog(), 1);
				player.skills.addExperience(Skill.FIREMAKING,
						(experience() * new ExperienceModifier(player).getModifier()));
			}

			RandomEventHandler.trigger(player);
			CustomGameObject object = new CustomGameObject(26185, player.getPosition());
			object.register();

			if (firemaking == FiremakingData.WILLOW_LOG) {
				player.forClan(channel -> channel.activateTask(ClanTaskKey.BURN_WILLOW_LOG, getMob().getName()));
			}

			World.schedule(new Task(180) {
				@Override
				protected void execute() {
					GroundItem.createGlobal(player, new Item(592), object.getPosition());
					object.unregister();
					cancel();
				}
			});
		}
	}

	@Override
	public void onCancel(boolean logout) {
		Player player = (Player) getMob();
		player.animate(new Animation(65535));
	}

	@Override
	public String getName() {
		return "Firemaking";
	}

	@Override
	public Item destructItem() {
		return item;
	}
}
