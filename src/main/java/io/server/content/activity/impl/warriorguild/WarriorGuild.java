package io.server.content.activity.impl.warriorguild;

import java.util.Optional;

import io.server.Config;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.content.activity.panel.ActivityPanel;
import io.server.content.dialogue.impl.KamfreenaDialogue;
import io.server.content.event.impl.ItemOnObjectInteractionEvent;
import io.server.content.event.impl.NpcInteractionEvent;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.event.impl.PickupItemInteractionEvent;
import io.server.content.store.Store;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.task.Task;
import io.server.game.task.impl.WarriorGuildCyclopEvent;
import io.server.game.world.World;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.NpcDeath;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendEntityHintArrow;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * This class handles the warrior's guild activity.
 *
 * @author Daniel
 * @author Nerik#8690 / Hamza
 */
public class WarriorGuild extends Activity {

	/** The player in the warrior guild activity. */
	private final Player player;

	/** The animated npc. */
	private Npc animated;

	/** The warrior guild state for this activity. */
	public WarriorGuildState state;

	/** The warrior guild activity listener for this activity. */
	private final WarriorGuildActivityListener listener = new WarriorGuildActivityListener(this);

	/** Contructs a new <code>WarriorGuild</code>. */
	private WarriorGuild(Player player) {
		super(3, Mob.DEFAULT_INSTANCE_HEIGHT);
		this.player = player;
	}

	/** Handes creating a new warrior guild activity for the player. */
	public static WarriorGuild create(Player player) {
		WarriorGuild minigame = new WarriorGuild(player);
		minigame.state = WarriorGuildState.ANIMATOR;
		minigame.add(player);
		return minigame;
	}

	/** Handles ending the activity. */
	public void end() {
		cleanup();
		finish();
	}

	/** Handles summoning the animated npc. */
	private void summon(int index, AnimatorData animator) {
		boolean walk_state = player.movement.isRunningToggled();

		animated = new Npc(WarriorGuildUtility.ANIMATED[index], animator.objectPosition);
		animated.owner = player;
		animated.setVisible(false);
		add(animated);

		player.movement.setRunningToggled(false);
		player.locking.lock(LockType.MASTER_WITH_MOVEMENT);

		for (int i = 0; i < WarriorGuildUtility.ARMOUR[index].length; i++) {
			player.inventory.remove(WarriorGuildUtility.ARMOUR[index][i]);
		}

		World.schedule(new Task(1) {
			int count = 0;

			@Override
			protected void execute() {
				switch (count) {

				case 0:
					player.animate(new Animation(827, UpdatePriority.HIGH));
					player.send(new SendMessage("You place the armour pieces inside the animator..."));
					break;
				case 2:
					player.send(new SendMessage("The animator begins to hum, something appears to be working..."));
					player.walkExactlyTo(animator.walkToPosition);
					break;
				case 7:
					animated.setVisible(true);
					animated.animate(4410);
					animated.speak("I'M ALIVE!");
					animated.getCombat().attack(player);
					player.face(animated);
					player.send(new SendEntityHintArrow(animated));
					player.send(new SendMessage("The animated armour comes to life!"));
					cancel();
					break;
				}
				count++;
			}

			@Override
			protected void onCancel(boolean logout) {
				player.blockFace = false;
				player.locking.unlock();
				player.movement.reset();
				player.movement.setRunningToggled(walk_state);

			}
		});
	}

	@Override
	protected void start() {

	}

	@Override
	public void finish() {
		remove(player);
		ActivityPanel.clear(player);
	}

	@Override
	public void cleanup() {
		if (animated != null) {
			remove(animated);
		}
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		int object = event.getObject().getId();
		if (object == 16671) {
			player.move(new Position(2841, 3538, 2));
			return true;
		}
		if (object == 24303) {
			player.move(new Position(2841, 3538, 0));
			return true;
		}
		if (object == 24306 || object == 24309) {
			if (state == WarriorGuildState.ANIMATOR) {
				if (!player.inventory.contains(new Item(8851, 25))) {
					player.dialogueFactory.sendStatement("You need at least 25 warrior guild tokens to enter.")
							.execute();
					return true;
				}
				player.inventory.remove(new Item(8851, 25));
				state = WarriorGuildState.CYCLOPS;
				player.move(new Position(player.getX() + 1, player.getY(), 2));
				if (!player.warriorGuidTask) {
					World.schedule(new WarriorGuildCyclopEvent(player));
				}
			} else if (state == WarriorGuildState.CYCLOPS) {
				player.move(new Position(player.getX() - 2, player.getY(), 2));
				state = WarriorGuildState.ANIMATOR;
			}
			return true;
		}
		return false;
	}

	@Override
	protected boolean clickNpc(Player player, NpcInteractionEvent event) {
		int npc = event.getNpc().id;
		if (npc == 2461) {
			player.dialogueFactory.sendDialogue(new KamfreenaDialogue());
			return true;
		}
		if (npc == 2462) {
			Store.STORES.get("Shanomi's Armour Store").open(player);
			return true;
		}
		return false;
	}

	@Override
	protected boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
		if (animated != null) {
			player.dialogueFactory.sendStatement("You already have an animated armour spawned!").execute();
			return false;
		}

		int object = event.getObject().getId();

		if (object != 23955) {
			return false;
		}

		Item used = event.getItem();
		int armourIndex = WarriorGuildUtility.contains(player, used.getId());

		if (armourIndex == -1) {
			return true;
		}

		Optional<AnimatorData> animator = AnimatorData.getAnimator(event.getObject().getPosition());

		if (!animator.isPresent()) {
			return true;
		}

		if (!player.getPosition().equals(animator.get().standPosition)) {
			player.dialogueFactory.sendStatement("Please stand in front of the animator!").execute();
			return true;
		}

		summon(armourIndex, animator.get());
		return true;
	}

	@Override
	protected boolean pickupItem(Player player, PickupItemInteractionEvent event) {
		if (WarriorGuildUtility.getDefender(player) == event.getItem().getId()) {
			player.pickup(event.getItem(), event.getPosition());
			return true;
		}
		return false;
	}

	@Override
	public void onLogout(Player player) {
		if (state == WarriorGuildState.CYCLOPS)
			player.move(new Position(2846, 3540, 2));
		cleanup();
		finish();
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inWarriorGuild(player)) {
			if (animated != null) {
				player.send(new SendMessage(
						"The animated armour vanishes as you have leave the region, taking your armour"));
				player.send(new SendMessage("with him."));
			}
			cleanup();
			finish();
		}
	}

	@Override
	public void onDeath(Mob mob) {
		if (mob.isNpc()) {
			World.schedule(new NpcDeath(mob.getNpc()));
			player.send(new SendEntityHintArrow(player, true));
			animated = null;
			return;
		}
		remove(mob);
		mob.move(Config.DEFAULT_POSITION);
		end();
	}

	@Override
	public void update() {
		if (state == WarriorGuildState.ANIMATOR)
			return;
		if (state == WarriorGuildState.CYCLOPS) {
			int current = WarriorGuildUtility.getDefenderIndex(player);
			int progress = (int) Utility.getPercentageAmount(current + 1, 8);
			Item defender = new Item(WarriorGuildUtility.getDefender(player), 1);
			ActivityPanel.update(player, progress, "Warrior Guild", defender, "Cyclops will be dropping:",
					"<col=FF5500>" + defender.getName(), "Chance: <col=FF5500>1/20</col>");
		}
	}

	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	@Override
	public void sequence() {
	}

	@Override
	public boolean safe() {
		return false;
	}

	@Override
	protected Optional<WarriorGuildActivityListener> getListener() {
		return Optional.of(listener);
	}

	@Override
	public ActivityType getType() {
		return ActivityType.WARRIOR_GUILD;
	}
}
