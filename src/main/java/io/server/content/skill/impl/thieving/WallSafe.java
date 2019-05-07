package io.server.content.skill.impl.thieving;

import io.server.Config;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles the wall safe thieving.
 *
 * @author Daniel
 */
public class WallSafe {

	/** Holds all the rewards. */
	private static final Item REWARD[] = { new Item(1617), new Item(1619), new Item(1621), new Item(1623),
			new Item(1623), new Item(995, 20), new Item(995, 40) };

	/** Handles thieving from the wall safe. */
	static void thieve(Player player) {
		if (player.skills.getMaxLevel(Skill.THIEVING) < 50) {
			player.dialogueFactory.sendStatement("You need a thieving level of 50 to do this!").execute();
			return;
		}
		if (player.inventory.remaining() == 0) {
			player.dialogueFactory.sendStatement("You do not have inventory spaces to do this!").execute();
			return;
		}
		if (player.skills.get(Skill.THIEVING).isDoingSkill()) {
			return;
		}
		player.action.execute(crack(player), true);
	}

	/** Gets the thieving rate. */
	private static int rate(Player player) {
		boolean stethoscope = player.inventory.contains(new Item(5560));
		int calculation = stethoscope ? Utility.random(5) : (Utility.random(11) + 20);
		int calculated = (10 - (int) Math.floor(player.skills.getMaxLevel(Skill.THIEVING) / 10) + calculation) / 2;
		return calculated <= 0 ? 1 : (calculated > 10 ? 10 : calculated);
	}

	/** The wallsafe fail chance. */
	public static int chance(Player player) {
		return (Utility.random((int) Math.floor(player.skills.getMaxLevel(Skill.THIEVING / 10) + 1)));
	}

	/** The wallsafe crack action. */
	private static Action<Player> crack(Player player) {
		return new Action<Player>(player, 3, true) {
			int rate = rate(player);
			int tick = 0;

			@Override
			public void execute() {
				if (tick++ != rate) {
					player.animate(new Animation(881));
					return;
				}

				if (chance(player) == 0) {
					player.animate(new Animation(404));
					player.send(new SendMessage("You slip and trigger a trap!"));
					player.damage(new Hit(Utility.random(1, 5)));
					cancel();
					return;
				}

				player.send(new SendMessage("You get some loot."));
				player.inventory.add(Utility.randomElement(REWARD));
				player.skills.addExperience(Skill.THIEVING,
						(100 * Config.THIEVING_MODIFICATION) * new ExperienceModifier(player).getModifier());
				cancel();
			}

			@Override
			protected void onSchedule() {
				player.skills.get(Skill.THIEVING).setDoingSkill(true);
				player.send(new SendMessage("You attempt to crack the safe... "));
			}

			@Override
			protected void onCancel(boolean logout) {
				player.skills.get(Skill.THIEVING).setDoingSkill(false);
			}

			@Override
			public String getName() {
				return "Wallsafe crack";
			}

			@Override
			public boolean prioritized() {
				return false;
			}

			@Override
			public WalkablePolicy getWalkablePolicy() {
				return WalkablePolicy.NON_WALKABLE;
			}
		};
	}
}
