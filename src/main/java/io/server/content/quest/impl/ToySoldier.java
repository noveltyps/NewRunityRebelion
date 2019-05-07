package io.server.content.quest.impl;

import java.util.Arrays;

import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.content.event.impl.NpcInteractionEvent;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.quest.Quest;
import io.server.content.quest.QuestManager;
import io.server.game.Animation;
import io.server.game.world.World;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendEntityHintArrow;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendMinimapState;
import io.server.net.packet.out.SendMinimapState.MinimapState;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

/**
 * Handles the toy soldier quest.
 *
 * @author Daniel
 */
public class ToySoldier extends Quest {

	@Override
	public String name() {
		return "Toy Soldier";
	}

	@Override
	public String created() {
		return "2016/11/03";
	}

	@Override
	public int questPoint() {
		return 2;
	}

	@Override
	public int index() {
		return QuestManager.TOY_SOLDIER;
	}

	@Override
	public void update(Player player) {
		clean(player);
		switch (getStage(player)) {
		case 0:
			player.send(new SendString("To start this quest speak to the boy near the", 37112));
			player.send(new SendString("Grand exchange tunnel shortcut.", 37113));
			player.send(new SendString("Having a high combat and 65 thieving is recommended.", 37115));
			break;
		case 1:
			player.send(new SendString("The boy said the last time he was playing with Billy", 37112));
			player.send(new SendString("was at Barbarian village.", 37113));
			break;
		case 2:
			player.send(new SendString("Brundt the Chieftain wants a toy for his daughter in exchange for", 37114));
			player.send(new SendString("Billy. He told me I should seek Zeke whom is located at Al-kahrid", 37115));
			player.send(new SendString("in a Scimitar shop.", 37116));
			break;
		case 3:
		case 4:
			player.send(new SendString("Zeke needs a white pearl in order to complete the toy.", 37113));
			player.send(new SendString("The pearl is hidden at 55 wilderness and is locked up tight.", 37114));
			player.send(new SendString("He also told me to bring some lock picks.", 37115));
			player.send(new SendString("Oh, and apparently it's haunted!", 37116));
			break;
		case 5:
			player.send(new SendString("Zeke has given me the doll. I should take it back to", 37113));
			player.send(new SendString("Brundt the Chieftain in exchange for Billy.", 37114));
			break;
		case 6:
			if (player.inventory.contains(7759)) {
				player.send(new SendString("I found Billy. I should return him to the boy.", 37113));
			} else {
				player.send(new SendString("Brundt the Chieftain lied to me. That idiot. Well I guess", 37113));
				player.send(new SendString("I will have to search around Barbarian village for Billy.", 37114));
			}
			break;
		case 7:
			player.send(new SendString("QUEST COMPLETED", 37111));
			player.send(new SendString("<col=000080>You have completed this quest!", 37112));
			player.send(new SendString("<col=000080>You were awarded:", 37113));
			player.send(new SendString("2 quest point", 37115));
			player.send(new SendString("Ring of wealth scroll", 37116));
			player.send(new SendString("Magic shortbow scrolls", 37117));
			player.send(new SendString("500,000 coins", 37118));
			break;
		}
	}

	@Override
	public void sendDialogues(DialogueFactory factory) {
		Player player = factory.getPlayer();
		int npc = player.attributes.get("QUEST_ENTITY_KEY", Integer.class);
		switch (getStage(player)) {
		case 0:
			factory.sendStatement("Sorry, but this quest has been temporarily disabled!").execute();
//                if (npc == 1919) {
//                    factory.sendNpcChat(npc, Expression.ALMOST_CRYING, "*sniff* *sniff*").sendPlayerChat("What's wrong little boy?").sendNpcChat(log, Expression.ALMOST_CRYING, "*sniff*", "nothing...").sendPlayerChat(Expression.ANGRY, "Okay, obviously something is wrong you're fucking", "crying like a god damn baby. I will only ask once more,", "What's wrong little boy?").sendNpcChat(log, Expression.NEARLY_CRYING, "I lost billy....", "*sniff*").sendPlayerChat("Billy?", "Is that your dog?").sendNpcChat(log, Expression.NEARLY_CRYING, "No.. Billy is my toy...", "He was my best friend and I can't find him...").sendOption("Help find Billy", () -> {
//                        setStage(player, 1);
//                        refresh(player);
//                        player.dialogueFactory.sendDialogue(this);
//
//                    }, "Walk away", () -> {
//                        factory.sendPlayerChat("Well that sucks kid.", "See ya.");
//                    }).execute();
//                }
			break;
		case 1:
			if (npc == 1919) {
				factory.sendPlayerChat("Okay, I will help you find Billy.", " Where was the last place you saw him?")
						.sendNpcChat(npc, Expression.CALM_TALK, "The last time I was playing with ",
								"Billy was at Barbarian village.")
						.sendPlayerChat("What does Billy look like?")
						.sendNpcChat(npc, Expression.CALM_TALK, "He has a black hat and a red jacket.")
						.sendPlayerChat("I will be right back.")
						.sendNpcChat(npc, Expression.HAPPY, "Thanks so much mister!").execute();
			} else if (npc == 3926) {
				factory.sendNpcChat(npc, Expression.CALM_TALK, "Welcome adventurer,", "I am Brundt the Chieftain.",
						"How may I help you?")
						.sendPlayerChat("I was wondering if you happened to find a toy,",
								"it had a black hat and a red jacket.")
						.sendNpcChat(npc, Expression.CALM_TALK, "Oh yes! It's quite a lovely toy. ",
								"I can't wait to give it to my new born daughter.")
						.sendPlayerChat(Expression.ANGRY, "That's not yours! A little boy lost his toy and he",
								"is very saddened about it.")
						.sendNpcChat(npc, Expression.HAPPY, "Oh that's terrible.",
								"However unless you have a replacement toy, this is mine.")
						.sendPlayerChat("Do you think I carry around a replacement toy?")
						.sendNpcChat(npc, Expression.HAPPY, "Mmmm..", "Well then go get one for me.")
						.sendPlayerChat("From where exactly?").sendNpcChat(npc, Expression.HAPPY,
								"My good friend Zeke can help you.", "He runs a Scimitar shop in Al-kahrid.")
						.execute().onAction(() -> {
							setStage(player, 2);
							player.interfaceManager.close();
						});
			}
			break;
		case 2:
			if (npc == 527) {
				factory.sendNpcChat(npc, Expression.HAPPY, "Greetings warrior, ",
						"would you like to purchase a fine crafted Scimitar? ")
						.sendOption("Talk about Toy Soldier", () -> {
							setStage(player, 3);
							player.dialogueFactory.sendDialogue(this);
						}, "View shop", () -> {

						}, "Nothing", player.interfaceManager::close).execute();
			}
			break;
		case 3:
			if (npc == 527) {
				factory.sendPlayerChat("I was sent by Brundt the Chieftain, he is seeking", "a toy for his daughter,")
						.sendNpcChat(npc, Expression.CALM_TALK, "Brundt had his daughter! That's wonderful news. ",
								"Yes wonderful... A toy he seeks? ", " Yes I know of a great toy!")
						.sendNpcChat(npc, Expression.CALM_TALK, "Unfortunately the toy is missing the most ",
								"important piece. If you can bring me it ", "I can build the toy.")
						.sendPlayerChat("What do you need and where can I get it?")
						.sendNpcChat(npc, Expression.CALM_TALK,
								"I need a white pearl. Finding it may be hard though...",
								"If my memory serves, there is a pearl hidden inside a",
								"chest at 55 wilderness. It's locked up tight so bring",
								"some lock picks. Oh and it's haunted by a ghost. Good luck!")
						.execute().onAction(() -> {
							setStage(player, 4);
							player.interfaceManager.close();
						});
			}
			break;
		case 4:
			if (npc == 527) {
				if (player.inventory.contains(4485)) {
					factory.sendPlayerChat("I Killed the barbarian spirit and retrieved the pearl!")
							.sendNpcChat(npc, Expression.CALM_TALK, "Wonderful news! Give it here.")
							.sendStatement("You have over the white pearl to Zeke").execute().onAction(() -> {
								player.send(new SendString("A few moments later..", 12285));
								player.send(new SendMinimapState(MinimapState.HIDDEN));
								player.interfaceManager.open(12283);

								World.schedule(3, () -> {
									player.send(new SendMinimapState(MinimapState.NORMAL));
									player.interfaceManager.close();
									factory.sendNpcChat(npc, "Here take this.").execute();
									player.inventory.replace(4485, 7763, true);
									setStage(player, 5);
								});
							});
				} else {
					factory.sendNpcChat(npc, "Speak to me when you have the pearl, I am very busy.").execute();
				}
			}
			break;
		case 5:
			if (npc == 3926) {
				if (player.inventory.contains(7763)) {
					factory.sendNpcChat(npc, Expression.CALM_TALK, "Have you talked to Zeke?")
							.sendPlayerChat("Yes and he gave me a doll. Can I get Billy back now?")
							.sendNpcChat(npc, Expression.CALM_TALK, "Yes, a deal is a deal. Unfortunately I do not",
									"have Billy.")
							.sendPlayerChat(Expression.ANGRY, "WHAT!?", "Why did you make me go get you a doll then!?")
							.sendNpcChat(npc, Expression.CALM_TALK,
									"I needed a toy for my daughter and I can't leave this",
									"place. We are all held hostige by the demon spiri-")
							.sendPlayerChat(Expression.ANGRY, "I don't have time for your bullshit.",
									"Take your stupid doll, I am going to find Billy myself.")
							.execute().onAction(() -> {
								player.inventory.remove(new Item(7763));
								setStage(player, 6);
								player.interfaceManager.close();
							});
				}
			}
			break;
		case 6:
			if (npc == 1919 && player.inventory.contains(7759)) {
				factory.sendNpcChat(npc, Expression.CALM_TALK, "Did you find Billy mister?")
						.sendPlayerChat("Yeah kid, I did. It was a lot of work but I found him.")
						.sendNpcChat(npc, Expression.CALM_TALK, "Oh thank you so much!",
								"I found these scrolls when I was playing and I don't",
								"know what to do with them. Here, you have them.")
						.execute().onAction(() -> {
							player.inventory.remove(new Item(7759));
							complete(player);
						});
			}
			break;
		case 7:
			if (npc == 1919) {
				factory.sendStatement("You have completed the quest and the",
						"boy no longer is interested in speaking to you.").execute();
			} else if (npc == 3926) {
				factory.sendStatement("There is no point in speaking to this idiot anymore.").execute();
			} else if (npc == 527) {
				factory.sendNpcChat(npc, Expression.HAPPY, "Greetings warrior, ",
						"would you like to purchase a fine crafted Scimitar? ").sendOption("Yes", () -> {
						}, "No", () -> {
							player.interfaceManager.close();
						}).execute();
			}
			break;
		}
	}

	@Override
	public void reward(Player player) {
		player.inventory.add(new Item(12783, 1));
		player.inventory.add(new Item(12786, 1));
		player.inventory.add(new Item(20527, 500_000));
		player.send(new SendString("Ring of wealth scroll", 12150));
		player.send(new SendString("Magic shortbow scroll", 12151));
		player.send(new SendString("500,000 Royale tokens", 12152));
		player.send(new SendString("2 Quest point", 12153));
		player.send(new SendString("", 12154));
		player.send(new SendString("", 12155));
	}

	@Override
	protected boolean clickNpc(Player player, NpcInteractionEvent event) {
		int[] npc = { 1919, 3926, 527 };
		if (event.getOpcode() == 0 && Arrays.stream(npc).anyMatch($it -> event.getNpc().id == $it)) {
			player.attributes.set("QUEST_ENTITY_KEY", event.getNpc().id);
			player.dialogueFactory.sendDialogue(this);
			return true;
		}
		return false;
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		int id = -1;
		if (event.getOpcode() == 0) {
			id = event.getObject().getId();
			if (id == 11726) {
				clickGate(player, event.getObject());
			} else if (id == 11735) {
				clickChest(player, event.getObject());
			} else if (id == 11600) {
				clickCrate(player, event.getObject());
			} else {
				id = -1;
			}
		}
		return id != -1;
	}

	private void clickGate(Player player, GameObject object) {
		int thieving = player.skills.getLevel(Skill.THIEVING);

		if (thieving < 65) {
			player.send(new SendMessage("You need a thieving level of 65 to lock pick this."));
			return;
		}

		int locks = player.inventory.computeAmountForId(1523);

		if (locks == 0) {
			player.send(new SendMessage("You need a lock pick to enter this."));
			return;
		}

		int chance = Utility.random(1, 15);
		player.send(new SendMessage("You attempt to pick the lock..."));
		player.locking.lock();
		player.animate(new Animation(2246));
		World.schedule(2, () -> {
			if (chance > 10) {
				player.skills.addExperience(Skill.THIEVING, 500);
				player.send(new SendMessage("You successfully pick the lock."));
//                player.action.execute(new DoorAction(player, object, true), true);
			} else {
				player.inventory.remove(new Item(1523, 1));
				player.damage(new Hit(Utility.random(1, 5)));
				player.send(new SendMessage("You have failed to pick the lock and were injured in the attempt."));
			}
			player.locking.unlock();
		});
	}

	private void clickChest(Player player, GameObject object) {
		if (player.quest.getStage()[index()] == 4 && !player.inventory.contains(4485)) {
			if (player.inventory.contains(2404)) {
				player.inventory.replace(2404, 4485, true);
				player.dialogueFactory.sendStatement("You found a pearl hidden inside the chest.").execute();
				return;
			}

			if (!player.followers.isEmpty()) {
				return;
			}

			Npc spirit = new Npc(player, 5562, new Position(3193, 3961));
			spirit.register();
			spirit.animate(new Animation(6725));
			player.send(new SendEntityHintArrow(spirit));
			player.followers.add(spirit);

			World.schedule(3, () -> {
				spirit.speak("Your time has come warrior!");
				spirit.getCombat().attack(player);
			});
		} else {
			player.dialogueFactory.sendStatement("Nothing interesting happens...").execute();
		}
	}

	private void clickCrate(Player player, GameObject object) {
		if (player.quest.getStage()[index()] == 6 && !player.inventory.contains(7759)) {
			player.dialogueFactory.sendStatement("You search the crate...")
					.sendPlayerChat(Expression.ANGRY, "Wow Billy was in the crate this whole time!").execute()
					.onAction(() -> {
						player.inventory.add(new Item(7759));
						player.interfaceManager.close();
					});
		}
	}
}
