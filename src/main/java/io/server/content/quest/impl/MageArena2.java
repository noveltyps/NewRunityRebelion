package io.server.content.quest.impl;

import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.content.event.impl.NpcInteractionEvent;
import io.server.content.quest.Quest;
import io.server.content.quest.QuestManager;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendString;

/**
 * Handles the MageArena 2 quest.
 *
 * @author Adam_#6723
 */
public class MageArena2 extends Quest {

	@Override
	public String name() {
		return "Mage Arena II";
	}

	@Override
	public String created() {
		return "2018/06/04";
	}

	@Override
	public int questPoint() {
		return 3;
	}

	@Override
	public int index() {
		return QuestManager.MageArena2;
	}

	@Override
	public void update(Player player) {
		clean(player);
		switch (getStage(player)) {
		case 0:
			player.send(new SendString("To start this quest please speak to the Wizard located at mage bank", 37116));
			break;
		case 1:
			player.send(new SendString("I have spoken to NPC and they want me to bring the following Items.", 37112));
			player.send(new SendString("I can achieve this by killing the three demi-gods.", 37113));
			player.send(new SendString("1x Saradomin Light", 37115));
			player.send(new SendString("1x Zamorak Symbol", 37116));
			player.send(new SendString("1x Guthix Icon", 37117));
			break;
		case 2:
			player.send(new SendString("I have given the priest what he needed.", 37115));
			player.send(new SendString("I should speak to him again to claim my onReward.", 37116));
			break;
		case 3:
			player.send(new SendString("QUEST COMPLETED", 37111));
			player.send(new SendString("<col=000080>You have completed this quest!", 37112));
			player.send(new SendString("<col=000080>You were awarded:", 37113));
			player.send(new SendString("2 quest point", 37115));
			player.send(new SendString("250,000 Combat experience", 37116));
			player.send(new SendString("400,000,0 gold coins", 37117));
			break;
		}
	}

	@Override
	public void sendDialogues(DialogueFactory factory) {
		Player player = factory.getPlayer();
		switch (getStage(player)) {
		case 0:
			factory.sendPlayerChat("Hey there...").sendNpcChat(4398, Expression.ALMOST_CRYING,
					"Oh #name, I need your help!", "The three Demi gods have escaped their prison!",
					"I need your help to slay them, they're causing havoc!", "Brave warrior, Could you please help me?")
					.sendOption("Sure", () -> {
						setStage(player, 1);
						player.interfaceManager.close();
						player.dialogueFactory.sendDialogue(this);
					}, "No, I'm too busy right now", () -> {
						factory.sendPlayerChat("No, I'm too busy right now").sendNpcChat(4398, Expression.ALMOST_CRYING,
								"*sigh*", "If you change your mind I will be here.");
					}).execute();
			break;
		case 1:
			Item[] needed = { new Item(13256), new Item(8056), new Item(8060) };
			if (player.inventory.containsAny(needed) && !player.inventory.containsAll(needed)) {
				factory.sendPlayerChat("So what do you need me to do?")
						.sendNpcChat(4398, Expression.HAPPY, "I need the following items to banish the evil spirits:",
								"1x Saradomin Light, 1x Zamorak Symbol and 1x Guthix symbol.",
								"You can gather these items by killing the demi gods physical forms!")
						.sendNpcChat(4398, Expression.ANXIOUS, "You can gather these items from the bosses.",
								"they spawn every 60-90 Minutes At Random Locations!")
						.sendPlayerChat("Alright, I will be right back.").execute();
				return;
			}
			if (player.inventory.containsAll(needed)) {
				setStage(player, 2);
				player.inventory.removeAll(needed);
				player.dialogueFactory.sendStatement("You have handed over all your items.").execute();
				return;
			}
			factory.sendPlayerChat("So what do you need me to do?")
					.sendNpcChat(4398, Expression.HAPPY, "I need the following items to banish their spirits:",
							"1x Saradomin Light, 1x Zamorak Symbol and 1x Guthix symbol.")
					.sendPlayerChat("Alright, I will be right back.").execute();
			break;
		case 2:
			factory.sendNpcChat(4398, Expression.HAPPY, "Thank you so much #name!", "Please take this as a reward")
					.onAction(() -> {
						complete(player);
					}).execute();
			break;
		default:
			factory.sendStatement("You have completed the quest and the", "priest no longer has anything to say.")
					.execute();
			break;
		}
	}

	@Override
	public void reward(Player player) {
		player.skills.addExperience(Skill.HITPOINTS, 500000, false);
		player.skills.addExperience(Skill.STRENGTH, 500000, false);
		player.skills.addExperience(Skill.DEFENCE, 500000, false);
		player.skills.addExperience(Skill.ATTACK, 500000, false);

		player.inventory.add(new Item(3062, 1), -1, true);

		player.message("The items have been sent to your inventory!");

		player.send(new SendString("500,000 Combat experience", 12150));
		player.send(new SendString("4,000,000 Coins", 12151));
		player.send(new SendString("2 Quest Points", 12152));
		player.send(new SendString("The Three Imbued God Capes!", 12153));
		player.send(new SendString("The Three God Halos", 12154));
		player.send(new SendString("The Three God Mjolnir", 12155));
		player.inventory.refresh();
	}

	public final static Item[] REWARD_ITEMS = { new Item(21795, 1), new Item(21793, 1), new Item(21791, 1),
			new Item(12638, 1), new Item(12637, 1), new Item(12639, 1), new Item(6760, 1), new Item(6762, 1),
			new Item(6764, 1) };

	@Override
	protected boolean clickNpc(Player player, NpcInteractionEvent event) {
		if (event.getOpcode() == 0 && event.getNpc().id == 4398) {
			player.dialogueFactory.sendDialogue(this);
			return true;
		}
		return false;
	}
}
