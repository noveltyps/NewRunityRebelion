package io.server.content.quest.impl;

import io.server.content.activity.impl.magearena.MageArena;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.content.event.impl.NpcInteractionEvent;
import io.server.content.quest.Quest;
import io.server.content.quest.QuestManager;
import io.server.content.quest.QuestState;
import io.server.content.store.Store;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendString;

/**
 * The quest for Kolodion's Arena
 *
 * @author Daniel
 */
public class KolodionsArena extends Quest {

	@Override
	public void sendDialogues(DialogueFactory factory) {
		Player player = factory.getPlayer();
		switch (getStage(player)) {
		case 0:
			factory.sendNpcChat(1603, Expression.HAPPY, "My my, what a beautiful looking warrior", "you are!");
			factory.sendPlayerChat("Umm yeah I know.", "I'm a head turner that's for sure.");
			factory.sendNpcChat(1603, Expression.AFFLICTED, "But are you worthy to beat my arena?",
					"Only the strongest of them all can do it.",
					"And quite frankly, I don't think you have what it takes!");
			factory.sendPlayerChat("First off, I don't care what you think about me.",
					"You are an NPC, you are scripted.", "Secondly, I can do anything! So bring it.");
			factory.sendNpcChat(1603, Expression.LAUGH, "Hahaha! We will see who is right in the end.",
					"If you beat my arena I will reward you handsomely.",
					"If you fail! My curse will be placed upon you.");
			factory.onAction(() -> {
				setStage(player, 1);
				refresh(player);
				factory.sendNpcChat(1603, Expression.EVIL, "Good luck!");
			});
			factory.execute();
			break;
		case 1:
			factory.sendNpcChat(1603, Expression.HAPPY, "Are you ready?");
			factory.sendOption("Yes", () -> MageArena.create(player), "No", factory::clear);
			factory.execute();
			break;
		case 2:
			factory.sendNpcChat(1602, Expression.HAPPY, "I heard the news but I didn't think it was true,",
					"You've actually beaten Kolodion's arena!", "Congratulations, here is your reward.").execute()
					.onAction(() -> {
						complete(player);
					});
			break;
		}
	}

	@Override
	public String name() {
		return "Kolodion's Arena";
	}

	@Override
	public String created() {
		return "2017-11-17";
	}

	@Override
	public int questPoint() {
		return 3;
	}

	@Override
	public int index() {
		return QuestManager.KOLODIONS_ARENA;
	}

	@Override
	public void update(Player player) {
		clean(player);
		switch (getStage(player)) {
		case 0:
			player.send(new SendString("I can start this quest by speaking to", 37115));
			player.send(new SendString("Kolodion who is located at the mage arena.", 37116));
			break;
		case 1:
			player.send(new SendString("Kolodion has challenged me to beat his arena.", 37113));
			player.send(new SendString("I can enter his arena by speaking to him.", 37114));
			player.send(new SendString("I should bring all fight styles, food and potions.", 37115));
			break;
		case 2:
			player.send(new SendString("I can complete the quest by speaking to the chamber guardian.", 37115));
			break;
		case 3:
			player.send(new SendString("Quest Completed!", 37115));
			break;
		}
	}

	@Override
	public void reward(Player player) {
		player.inventory.addOrDrop(new Item(995, 5000_000));
		player.send(new SendString("5 million coins", 12150));
		player.send(new SendString("God capes", 12151));
		player.send(new SendString("3 Quest points", 12152));
		player.send(new SendString("", 12153));
		player.send(new SendString("", 12154));
		player.send(new SendString("", 12155));
	}

	@Override
	protected boolean clickNpc(Player player, NpcInteractionEvent event) {
		if (event.getOpcode() == 0 && event.getNpc().id == 1603) {
			if (getState(player) == QuestState.COMPLETED) {
				DialogueFactory factory = player.dialogueFactory;
				factory.sendNpcChat(1603, Expression.HAPPY, "Would you like to play my arena again?");
				factory.sendOption("Yes", () -> MageArena.create(player), "No", factory::clear);
				factory.execute();
				return true;
			} else if (getStage(player) == 2) {
				DialogueFactory factory = player.dialogueFactory;
				factory.sendNpcChat(1603, Expression.HAPPY, "You proved me wrong #name!",
						"Jump into that sparkling pool for a reward.");
				factory.execute();
				return true;
			}
			player.dialogueFactory.sendDialogue(this);
			return true;
		}
		if (event.getOpcode() == 0 && event.getNpc().id == 1602) {
			if (getState(player) == QuestState.COMPLETED) {
				DialogueFactory factory = player.dialogueFactory;
				factory.sendNpcChat(1602, Expression.HAPPY, "Hello adventurer.", "Would you like to view my shop?");
				factory.sendOption("Yes", () -> Store.STORES.get("Kolodion's Arena Store").open(player), "No",
						factory::clear);
				factory.execute();
				return true;
			} else if (getStage(player) != 2) {
				DialogueFactory factory = player.dialogueFactory;
				factory.sendNpcChat(1602, Expression.HAPPY, "Please go away. I'm busy walking around pointlessly.");
				factory.execute();
			}
			player.dialogueFactory.sendDialogue(this);
			return true;
		}
		return false;
	}
}
