package io.server.content.skill.impl.cooking;

import java.util.Arrays;

import io.server.Config;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.dialogue.ChatBoxItemDialogue;
import io.server.content.event.impl.ItemOnObjectInteractionEvent;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.object.GameObject;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles the cooking skill.
 * 
 * @author Daniel
 */
public class Cooking extends Skill {

	private transient final String[] objects = { "range", "fire", "oven", "stove", "cooking range", "fireplace" };

	public Cooking(int level, double experience) {
		super(Skill.COOKING, level, experience);
	}

	private boolean cookableObject(GameObject object) {
		String name = object.getDefinition().name.toLowerCase();
		return Arrays.stream(objects).anyMatch(name::contains);
	}

	private boolean success(Player player, int level, int noBurn) {
		if (player.skills.getLevel(Skill.COOKING) >= noBurn) {
			return true;
		}

		int burn_bonus = 3;
		double burn_chance = (45.0 - burn_bonus);
		double cook_level = player.skills.getLevel(Skill.COOKING);
		double level_needed = (double) level;
		double burn_stop = (double) noBurn;
		double multi_a = (burn_stop - level_needed);
		double burn_dec = (burn_chance / multi_a);
		double multi_b = (cook_level - level_needed);
		burn_chance -= (multi_b * burn_dec);
		double random_number = Utility.random(100);
		return burn_chance <= random_number;
	}

	@Override
	protected double modifier() {
		return Config.COOKING_MODIFICATION;
	}

	@Override
	protected boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
		Item item = event.getItem();
		GameObject object = event.getObject();

		if (!cookableObject(object)) {
			return false;
		}

		if (!CookData.forId(item.getId()).isPresent()) {
			return false;
		}

		if (player.skills.get(Skill.COOKING).isDoingSkill()) {
			return true;
		}

		CookData cook = CookData.forId(item.getId()).get();

		if (getLevel() < cook.getLevel()) {
			player.dialogueFactory.sendStatement("You need a cooking level of " + cook.getLevel() + " to cook this!")
					.execute();
			return true;
		}

		if (player.inventory.computeAmountForId(item.getId()) == 1) {
			player.action.execute(cook(player, cook, 1), true);
		} else {
			ChatBoxItemDialogue.sendInterface(player, 1746, cook.getCooked(), 0, -5, 170);
			player.chatBoxItemDialogue = new ChatBoxItemDialogue(player) {
				@Override
				public void firstOption(Player player) {
					player.action.execute(cook(player, cook, 1), true);
				}

				@Override
				public void secondOption(Player player) {
					player.action.execute(cook(player, cook, 5), true);
				}

				@Override
				public void thirdOption(Player player) {
					player.send(new SendInputAmount("Enter amount of fish you would like to cook", 10,
							input -> player.action.execute(cook(player, cook, Integer.parseInt(input)), true)));
				}

				@Override
				public void fourthOption(Player player) {
					player.action.execute(cook(player, cook, 28), true);
				}
			};
		}
		return true;
	}

	private Action<Player> cook(Player player, CookData cook, int amount) {
		return new Action<Player>(player, 3, true) {
			int ticks = 0;

			@Override
			protected void onSchedule() {
				player.skills.get(Skill.COOKING).setDoingSkill(true);
			}

			@Override
			public void execute() {
				if (!player.skills.get(Skill.COOKING).isDoingSkill()) {
					cancel();
					return;
				}

				if (!player.inventory.contains(cook.getItem())) {
					cancel();
					return;
				}

				player.animate(new Animation(883));
				String name = ItemDefinition.get(cook.getCooked()).getName();
				player.inventory.remove(cook.getItem(), 1);

				if (success(player, cook.getLevel(), cook.getNoBurn())) {
					player.inventory.add(cook.getCooked(), 1);
					player.inventory.add(995, cook.getMoney());
					player.skills.addExperience(Skill.COOKING,
							(cook.getExp() * modifier()) * new ExperienceModifier(player).getModifier());
					player.send(new SendMessage("You successfully cook the " + name + "."));
					RandomEventHandler.trigger(player);
				} else {
					player.inventory.add(cook.getBurnt(), 1);
					player.send(new SendMessage("You accidently burn the " + name + "."));
				}

				if (++ticks == amount) {
					cancel();
				}
			}

			@Override
			protected void onCancel(boolean logout) {
				player.resetFace();
				player.skills.get(Skill.COOKING).setDoingSkill(false);
			}

			@Override
			public String getName() {
				return "Cooking";
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
