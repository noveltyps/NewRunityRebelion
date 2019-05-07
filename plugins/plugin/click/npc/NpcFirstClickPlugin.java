package plugin.click.npc;

import static io.server.content.pet.PetData.JAD;

import io.server.Config;
import io.server.content.activity.impl.TutorialActivity;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.Expression;
import io.server.content.dialogue.impl.ClanmasterDialogue;
import io.server.content.dialogue.impl.ConstructionDialogue;
import io.server.content.dialogue.impl.DailyTaskDialogue;
import io.server.content.dialogue.impl.GamblerDialogue;
import io.server.content.dialogue.impl.NieveDialogue;
import io.server.content.dialogue.impl.RealmLordDialogue;
import io.server.content.dialogue.impl.RoyalKingDialogue;
import io.server.content.dialogue.impl.SailorKingDialouge;
import io.server.content.dialogue.impl.VoteDialogue;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.pet.Pets;
import io.server.content.rejuvenation.RejuvenationFountain;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.skill.impl.thieving.Thieving;
import io.server.content.store.Store;
import io.server.content.store.impl.PersonalStore;
import io.server.content.store.impl.SkillcapeStore;
import io.server.content.teleport.TeleportHandler;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;

public class NpcFirstClickPlugin extends PluginContext {

	@Override
	protected boolean firstClickNpc(Player player, NpcClickEvent event) {
		final int id = event.getNpc().id;
		switch (id) {
		case 523: // Temp Daily Tasks Dialogue
			player.dialogueFactory.sendDialogue(new DailyTaskDialogue());
			break;
		case 3893:
			player.dialogueFactory.sendNpcChat(3893, "Ahoy there!", "I'll note your Rune Essence!").execute();
			break;
		case 4925:
			player.dialogueFactory.sendNpcChat(4925, "Oh you saved me! Take this as a reward!")
					.sendItem("Reward", "Dusty Key", 1590).execute();
			player.inventory.add(1590, 1);
			break;
		case 1635:
			if (player.skills.getLevel(Skill.HUNTER) >= 1) {
				player.skills.addExperience(Skill.HUNTER,
						(Config.HUNTER_MODIFICATION) * new ExperienceModifier(player).getModifier());
	
				break;
			}
		case 1389:
			player.dialogueFactory
			.sendNpcChat(id, "Hey, im the refferal Manager", "Did you know you can get rewarded by inviting players?")
			.sendNpcChat(id, "Simply invite your friends", "and tell them to place you as the refferal", "You'll recieve the following.")
			.sendNpcChat(id, "1x Silver Mbox")
			.sendNpcChat(id, "Your friend will recieve the following.", "1x Silver Mbox")
			.sendNpcChat(id, "Trade me to view my store!")
			.execute();
			break;
		case 3220:
			player.dialogueFactory.sendOption("Air Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, Config.AIR_ZONE, 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Air Altar!, " + player.getName() + "!"));
					});
				}
			}, "Nature Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, Config.NATURE_ZONE, 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Nature altar, " + player.getName() + "!"));
					});

				}
			}, "Blood Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, Config.BLOOD_ZONE, 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Blood altar, " + player.getName() + "!"));
					});
				}
			}, "Law Altar", () -> {
				if (player.wilderness > 30 && !PlayerRight.isPriviledged(player)) {
					player.message("@or2@you can't teleport above 30 wilderness");
				} else {
					Teleportation.teleport(player, Config.LAW_ALTAR, 20, () -> {
						player.send(new SendMessage("@or2@Welcome to the Law Altar, " + player.getName() + "!"));
					});
				}
			}, "Nowhere", player.interfaceManager::close).execute();
			break;
			
		case 605:
			DialogueFactory factory1 = player.dialogueFactory;
			factory1.sendOption("Open Raid Store",
					() -> Store.STORES.get("Raids Store").open(factory1.getPlayer()),
					"Open AVO Store",
					() -> Store.STORES.get("AVO Store").open(factory1.getPlayer()), "Nevermind",
					factory1::clear);
			factory1.execute();
			break;

		case 7481:
			player.dialogueFactory.sendDialogue(new VoteDialogue());
			break;
			
		case 4399:	
			TeleportHandler.open(player);
			break;

		case 2152:
			Store.STORES.get("Bob's Brilliant Axes").open(player);
			break;

		case 5450:
			Store.STORES.get("Zaff's Superior Staffs").open(player);
			break;
			
		case 899:
			Store.STORES.get("Blood Money Store").open(player);
			break;
			
		case 1570:
			Store.STORES.get("Blood Point Store").open(player);
			break;

		case 3115:
			Store.STORES.get("Zeke�s Superior Scimitars").open(player);
			break;

		case 5452:
			Store.STORES.get("Varrock Swordshop").open(player);
			break;

		case 5451:
			Store.STORES.get("Dommik's Crafting Store").open(player);
			break;

		case 5449:
			Store.STORES.get("Skiller Shop").open(player);
			break;

		case 5789:
			player.send(new SendString("Agility Ticket Exchange", 8383));
			player.interfaceManager.open(8292);
			break;

		case 2186:
			Store.STORES.get("The Tzhaar Tokkul Store").open(player);
			break;
			
		case 521:
			Store.STORES.get("Skilling Point Store").open(player);
			break;

		case 305:
			DialogueFactory factory11 = player.dialogueFactory;
			factory11.sendOption("Open Culinaromancer's Food Store",
					() -> Store.STORES.get("Culinaromancer's Food Store").open(factory11.getPlayer()),
					"Open Culinaromancer's Items Store",
					() -> Store.STORES.get("Culinaromancer's Items Store").open(factory11.getPlayer()), "Nevermind",
					factory11::clear);
			factory11.execute();

			break;

		case 2180: {
			DialogueFactory factory = player.dialogueFactory;
			factory.sendNpcChat(2180, "Would you like to sacrifice a firecape for a chance",
					"of getting the pet? Chances are 1/200.");
			factory.sendOption("Yes", () -> {
				factory.onAction(() -> {
					if (!player.inventory.contains(6570, 1)) {
						factory.sendNpcChat(2180, "You must have a firecape to sacrifice first!").execute();
						return;
					}

					player.inventory.remove(6570, 1);
					if (!Pets.onReward(player, JAD)) {
						factory.sendNpcChat(2180, "I'm sorry. The RNG was not on your side today!").execute();
					} else {
						factory.clear();
					}
				});
			}, "No", factory::clear);
			factory.execute();
			break;
		}

		/* Banker */
		case 394:
		case 395:
		case 2897:
		case 2898:
			player.bankPin.open();
			break;

		/* Mage bank */
		case 1600:
			player.bank.open();
			break;

		/* Nurse sarah */
		case 1152:
			new RejuvenationFountain(player).execute();
			break;

		case 506:
		case 507:
		case 513: // falador female
		case 512: // falador male
		case 1032:
			Store.STORES.get("The General Store").open(player);
			break;

		case 1199:
			Store.STORES.get("Chef's Choodle Oodle Store").open(player);
			break;

		/* Gnome trainer */
		case 6080:
			player.dialogueFactory.sendPlayerChat("Hello there.")
					.sendNpcChat(id, "This isn't a grannies' tea party, let's see some sweat", "human. Go! Go! Go!")
					.execute();
			break;

		/* Barbarian trainer */
		case 2153:
			player.dialogueFactory
					.sendNpcChat(id, "Haha welcome to my obstacle course. Have fun, but",
							"remember this isn't a child's playground you hobo.", "People have died here.")
					.sendNpcChat(id, "The best way to train, is to go round the course in a", "clockwise direction.")
					.execute();
			break;

		/* Potion decanting */
		case 1146: {
//                DialogueFactory factory = player.dialogueFactory;
//                factory.sendNpcChat(id, "Hello, I am Joe the chemist.", "How may I help you?");
//                factory.sendOption(
//                        "Decant all potions (50,000 RT)",
//                        () -> factory.onAction(() -> Decanting.decant(player)),
//
//                        "Nevermind",
//                        () -> factory.onAction(player.interfaceManager::close));
//                factory.execute();
			break;
		}

		/* Ironman shop */
		case 311: {
			DialogueFactory factory = player.dialogueFactory;
			if (PlayerRight.isIronman(player) || PlayerRight.isDeveloper(player)) {
				factory.sendNpcChat(id, "Welcome my friend.", "How may I help you?")
						.sendOption("Claim armour", player.playerAssistant::claimIronmanArmour, "View shop",
								() -> factory.onAction(() -> Store.STORES.get("Ironman Store").open(player)),
								"Nevermind", factory::clear)
						.execute();
			} else {
				factory.sendNpcChat(id, "I have no buisness with you plebs, taking the easy route",
						"You're not an Ironman, you wimp.");

			}

		}
			break;

		/* Melee shop */
		case 3216: {
			DialogueFactory factory = player.dialogueFactory;
			factory.sendOption("Weapons", () -> Store.STORES.get("Melee Weapons").open(player), "Armour",
					() -> Store.STORES.get("Melee Armour").open(player)).execute();
		}
			break;

		/* Ranged shop */
		case 3217:
			Store.STORES.get("Range Store").open(player);
			break;

		/* Magic shop */
		case 3218:
			Store.STORES.get("Magic Store").open(player);
			break;
		/* PK Reward Shop 1 */
		case 841:
			Store.STORES.get("Pk Rewards Shop 1").open(player);
			break;
		/* Pure shop */
		case 5440:
			Store.STORES.get("Pure Store").open(player);
			break;

		/* Wise old man */
		case 4306:
			new SkillcapeStore().open(player);
			break;

		case 6773:
			player.lostUntradeables.open();
			break;

		/* Gamble shop */
		case 1011:
			Store.STORES.get("The Gamble Store").open(player);
			break;
		/* Gambler */
		case 1012:
			player.dialogueFactory.sendDialogue(new GamblerDialogue(event.getNpc()));
			break;
		/* Note Trader Shop */
		case 3189:
			break;
		/* Skilling shop */
		case 505:
			player.dialogueFactory.sendOption("Equipment", () -> {
				player.dialogueFactory.onAction(() -> Store.STORES.get("Skilling Store Equipment").open(player));
			}, "Farming", () -> {
				player.dialogueFactory.onAction(() -> Store.STORES.get("Farming Supplies").open(player));
			}, "Herblore", () -> {
				player.dialogueFactory.onAction(() -> Store.STORES.get("Herblore Supplies").open(player));
			}, "Miscellaneous", () -> {
				player.dialogueFactory.onAction(() -> Store.STORES.get("Miscellaneous Supplies").open(player));
			}).execute();
			break;

		/* Mac */
		case 6481: {
			DialogueFactory factory = player.dialogueFactory;
			if (!player.skills.isMaxed()) {
				factory.sendNpcChat(id, "Please speak to me once you are maxed in all 20 skills.").execute();
				return true;
			}

			factory.sendNpcChat(id, "Hello adventurer!", "Are you interested in purchasing a Max cape and",
					"hood for 2,500,000 gp?").sendOption("Yes", () -> {
						factory.onAction(() -> {
							if (!player.inventory.contains(new Item(995, 2_500_000))) {
								factory.sendNpcChat(id, "You need 2,500,000 gp to purchase this cape!").execute();
								return;
							}

							final Item[] ITEMS = { new Item(13281), new Item(13280) };
							if (!player.inventory.hasCapacityFor(ITEMS)) {
								factory.sendNpcChat(id, "You do not have enough inventory spaces for this!").execute();
								return;
							}

							player.inventory.remove(995, 2_500_000);
							player.inventory.addAll(ITEMS);
							factory.sendNpcChat(id, "Enjoy your new cape!").execute();
						});
					}, "No", () -> {
						factory.onAction(player.interfaceManager::close);
					}).execute();
		}
			break;

		/* Construction dialogue */
		case 5419:
			player.dialogueFactory.sendDialogue(new ConstructionDialogue());
			break;

		/* King Royal dialogue */
		case 5523:
			player.dialogueFactory.sendDialogue(new RoyalKingDialogue(0));
			break;

		/* Merchant King */
		case 5608:
			player.dialogueFactory.sendDialogue(new SailorKingDialouge(0));
			break;
		/* Clanmaster Dialogue. */
		case 1143:
			player.dialogueFactory.sendDialogue(new ClanmasterDialogue());
			break;

		/* Nieve Dialogue. */
		case 490:
		case 6797:
			player.dialogueFactory.sendDialogue(new NieveDialogue());
			break;

		/* Realm Lord */
		case 15:
			player.dialogueFactory.sendDialogue(new RealmLordDialogue());
			break;

		/* Makeover mage */
		case 1307:
			player.interfaceManager.open(3559);
			break;
		case 1615:
			if (PlayerRight.isIronman(player)) {
				player.send(new SendMessage("As an iron man you may not access player owned stores!"));
				return true;
			}
			PersonalStore.openMenu(player);

			break;
			
		/* Thieving goods merchant */
		case 3439:
			Thieving.exchange(player);
			break;
		case 1755:
			Store.STORES.get("The Pest Control Store").open(player);
			break;
			
		case 3438:
			Store.STORES.get("Exclusive Donator Store").open(player);
			break;

		case 5919:
			if (PlayerRight.isDonator(player) || PlayerRight.isPriviledged(player)) {
				Store.STORES.get("Grace's Graceful Store").open(player);
			} else {
				player.message("You need to be a donator or higher to access this store!");
			}
			break;

		case 306:
			player.dialogueFactory.sendNpcChat(306, "Hello #name, would you like to do the tutorial?");
			player.dialogueFactory.sendOption("Yes", () -> TutorialActivity.create(player), "No",
					() -> player.dialogueFactory.clear());
			player.dialogueFactory.execute();
			break;

		default:
			player.dialogueFactory.sendNpcChat(id, Expression.ANNOYED, "Please go away I'm busy.").execute();
		}
		return false;
	}

}
