package plugin.click.object;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.server.Config;
import io.server.content.activity.Activity;
import io.server.content.activity.impl.bosscontrol.BossControl;
import io.server.content.activity.impl.fightcaves.FightCaves;
import io.server.content.activity.impl.pestcontrol.PestControl;
import io.server.content.activity.impl.warriorguild.WarriorGuild;
import io.server.content.combat.cannon.CannonManager;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.dialogue.impl.WellOfGoodwillDialogue;
import io.server.content.freeforall.impl.FreeForAllLobbyTask;
import io.server.content.masterminer.Util;
import io.server.content.quest.QuestManager;
import io.server.content.skill.impl.crafting.impl.Tanning;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.staff.PanelType;
import io.server.content.staff.StaffPanel;
import io.server.content.store.Store;
import io.server.content.store.impl.PersonalStore;
import io.server.content.teleport.TeleportHandler;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.action.impl.DoorAction;
import io.server.game.action.impl.LadderAction;
import io.server.game.action.impl.LeverAction;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.task.impl.ChopVineTask;
import io.server.game.task.impl.ObjectReplacementEvent;
import io.server.game.task.impl.SteppingStoneTask;
import io.server.game.task.impl.SuperAntipoisonTask;
import io.server.game.world.InterfaceConstants;
import io.server.game.world.World;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.effect.CombatEffectType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.HitIcon;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.combat.magic.Autocast;
import io.server.game.world.entity.combat.strategy.npc.boss.chimera.ChimeraUtility;
import io.server.game.world.entity.combat.strategy.player.special.CombatSpecial;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Position;
import io.server.game.world.region.dynamic.minigames.DuoInferno4Session;
import io.server.game.world.region.dynamic.minigames.Raids14Session;
import io.server.game.world.region.dynamic.minigames.Raids24Session;
import io.server.game.world.region.dynamic.minigames.Raids34Session;
import io.server.game.world.region.dynamic.minigames.Raids44Session;
import io.server.game.world.region.dynamic.minigames.Raids54Session;
import io.server.game.world.region.dynamic.minigames.ZombieRaidDuo4Session;
import io.server.net.packet.out.SendFadeScreen;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendURL;
import io.server.util.RandomUtils;
import io.server.util.Utility;

public class ObjectFirstClickPlugin extends PluginContext {

	@SuppressWarnings("static-access")
	@Override
	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		final GameObject object = event.getObject();
		final int id = object.getId();
		// final int ANIM = 0;

		switch (id) {

		case 9385:
		case 9382:
		case 194:
			if (!player.hasAllForOnePartner()) {
				player.sendMessage("You need to have an Zombie Raid Duo partner to enter this portal!");
				player.sendMessage("Right click this portal > Set Options and send a partner request!");
				break;
			}
			if (player.partyLeader != player) {
				player.sendMessage("Only your party leader can start Zombie Raid Duo!");
				break;
			}
			if (!player.getPosition().isWithinDistance(player.allForOnePartner.getPosition(), 15)) {
				player.sendMessage("Your partner needs to be with you in order to start this session!");
				break;
			}
			ZombieRaidDuo4Session session16 = new ZombieRaidDuo4Session();
			session16.onStart(player);
			break;
		case 9380:
			for (int i = 0; i < player.boxes.length; i++) {
				if (player.boxes[i] != null && object == player.boxes[i].object) {
					player.boxes[i].pickUp();
					break;
				}
			}
			break;
		case 9344:
		case 9345:
		case 9379:
		case 9377:
		case 9373:
		case 9375:
		case 9348:
			for (int i = 0; i < player.snares.length; i++) {
				if (player.snares[i] != null && object == player.snares[i].object) {
					player.snares[i].pickUp();
					break;
				}
			}
			break;
		case 19039:
			if (player.getRaidPartners().size() <= 0) {
				player.sendMessage("You need to a Raids 4 party to enter this portal!");
				player.sendMessage("Right click this portal > Set Options and send a partner request!");
				break;
			}
//			if (!player.hasAllForOnePartner()) {
//				player.sendMessage("You need to an Raids 2 partner to enter this portal!");
//				player.sendMessage("Right click this portal > Set Options and send a partner request!");
//				break;
//			}
			if (player.partyLeader != player) {
				player.sendMessage("Only your party leader can start Raids 2!");
				break;
			}
			
			boolean partyPresent14 = true;
			
			for (Player p : player.getRaidPartners())
				if (!player.getPosition().isWithinDistance(p.getPosition(), 15))
					partyPresent14 = false;
			
//			if (!player.getPosition().isWithinDistance(player.allForOnePartner.getPosition(), 15)) {
//				player.sendMessage("Your partner needs to be with you in order to start this session!");
//				break;
//			}
			
			if (!partyPresent14) {
				player.sendMessage("Your entire party needs to be with you in order to start this session!");
				break;
			}
			
			Raids44Session session14 = new Raids44Session();
			session14.onStart(player);
		//	player.setDynamicRegion(new DynamicRegion(player, RegionType.All_FOR_ONE_4));
		//	player.getAllForOnePartner().setDynamicRegion(player.getDynamicRegion());
			break;
		case 4031:
			if (player.getRaidPartners().size() <= 0) {
				player.sendMessage("You need to a Raids 3 party to enter this portal!");
				player.sendMessage("Right click this portal > Set Options and send a partner request!");
				break;
			}
//			if (!player.hasAllForOnePartner()) {
//				player.sendMessage("You need to an Raids 2 partner to enter this portal!");
//				player.sendMessage("Right click this portal > Set Options and send a partner request!");
//				break;
//			}
			if (player.partyLeader != player) {
				player.sendMessage("Only your party leader can start Raids 3!");
				break;
			}
			
			boolean partyPresent13 = true;
			
			for (Player p : player.getRaidPartners())
				if (!player.getPosition().isWithinDistance(p.getPosition(), 15))
					partyPresent13 = false;
			
//			if (!player.getPosition().isWithinDistance(player.allForOnePartner.getPosition(), 15)) {
//				player.sendMessage("Your partner needs to be with you in order to start this session!");
//				break;
//			}
			
			if (!partyPresent13) {
				player.sendMessage("Your entire party needs to be with you in order to start this session!");
				break;
			}
			
			Raids34Session session13 = new Raids34Session();
			session13.onStart(player);
		//	player.setDynamicRegion(new DynamicRegion(player, RegionType.All_FOR_ONE_4));
		//	player.getAllForOnePartner().setDynamicRegion(player.getDynamicRegion());
			break;
		
		/* Player owned shops. */
		case 3029:
			if (PlayerRight.isIronman(player)) {
				player.send(new SendMessage("As an iron man you may not access player owned stores!"));
				return true;
			}
			PersonalStore.openMenu(player);

			break;
			
		case 4006: // GLOBAL-BM-2
			if (player.getRaidPartners().size() <= 0) {
				player.sendMessage("You need to a Raids 5 party to enter this portal!");
				player.sendMessage("Right click this portal > Set Options and send a partner request!");
				break;
			}
//			if (!player.hasAllForOnePartner()) {
//				player.sendMessage("You need to an Raids 2 partner to enter this portal!");
//				player.sendMessage("Right click this portal > Set Options and send a partner request!");
//				break;
//			}
			if (player.partyLeader != player) {
				player.sendMessage("Only your party leader can start Raids 5!");
				break;
			}
			
			boolean partyPresent15 = true;
			
			for (Player p : player.getRaidPartners())
				if (!player.getPosition().isWithinDistance(p.getPosition(), 15))
					partyPresent15 = false;
			
//			if (!player.getPosition().isWithinDistance(player.allForOnePartner.getPosition(), 15)) {
//				player.sendMessage("Your partner needs to be with you in order to start this session!");
//				break;
//			}
			
			if (!partyPresent15) {
				player.sendMessage("Your entire party needs to be with you in order to start this session!");
				break;
			}
			
			Raids54Session session15 = new Raids54Session();
			session15.onStart(player);
		//	player.setDynamicRegion(new DynamicRegion(player, RegionType.All_FOR_ONE_4));
		//	player.getAllForOnePartner().setDynamicRegion(player.getDynamicRegion());
			break;
		
		// donor rev zone //
		case 23673:
			if (PlayerRight.isElite(player) || PlayerRight.isExtreme(player) || PlayerRight.isSupreme(player) || PlayerRight.isKing(player)) {
				player.message("Welcome to our Donator Rev Zone");
				player.move(new Position(2350, 3862, player.getHeight()));
			}
			else player.message("Only Extreme+ can enter our Donator Rev Zone!");
			break;
			/* Grand exchange. */
		case 26044:

			if (PlayerRight.isIronman(player)) {
				player.send(new SendMessage("As an iron man you may not access player owned stores!"));
				return true;
			}
			PersonalStore.openMenu(player); 

			break;

		
		case 16539:
			player.move(new Position(2730, 10008, player.getHeight()));
			break;
			
		case 8720:
			player.send(new SendURL("https://www.brutalos.org/vote"));
			break;
				

		  case 26727: 
			new FreeForAllLobbyTask(player).execute();
			break; 
			
		case 27215: {
			if (!player.itemDelay.elapsed(2, TimeUnit.SECONDS)) {
				return true;
			}

			CombatType type = player.getStrategy().getCombatType();
			int maxHit = player.playerAssistant.getMaxHit(player, type);

			HitIcon hitIcon = HitIcon.MELEE;
			if (type == CombatType.MAGIC) {
				hitIcon = HitIcon.MAGIC;
			} else if (type == CombatType.RANGED) {
				hitIcon = HitIcon.RANGED;
			}

			player.animate(player.getStrategy().getAttackAnimation(player, player));
			player.writeFakeDamage(new Hit(maxHit, Hitsplat.CRITICAL, hitIcon));
			player.itemDelay.reset();
			break;
		}
		
		case 13291: // Magic Chest
			if (!player.inventory.contains(1547)) {
				player.sendMessage("You don't have a magic chest key to use!");
				break;
			}
			
			Item[] rewards = new Item[]{ new Item(995, 5000) };
			
			Item reward = rewards[ThreadLocalRandom.current().nextInt(0, rewards.length - 1)];
			
			player.inventory.remove(new Item(1547, 1));
			player.inventory.add(reward);
			
			break;

		case 31990:
			player.animate(839);
			player.move(new Position(2272, 4054));
			break;

		case 12356: // ROD Portal
			Teleportation.teleport(player, Config.DEFAULT_POSITION);
			player.message("You've teleported Home, and all your data in RFD Has been lost.e");
			break;

		/**
		 * Raids First Wave out of 3 on the first floor. This wave has three different
		 * rooms that players potientally land in. The bosses will be easier due to the
		 * fact that Raids will be possible to solo.
		 */
		case 29789:
			if (player.getY() <= object.getY()) {

				if (Utility.random(1, 3) == 1) {
					player.move(new Position(3307, 5208, 0));
					player.message("You have entered the first boss wave, there's no turning back now! MUHAHAH");
					player.message("[RAIDS] NPC W.E WaveeE");

				}
				if (Utility.random(1, 3) == 2) {
					player.move(new Position(3343, 5258, 0));
					player.message("You have entered the first boss wave, there's no turning back now! MUHAHAH");
					player.message("[RAIDS] NPC W.E Wave");

				}
				if (Utility.random(1, 3) == 3) {
					player.move(new Position(3280, 5249, 0));
					player.message("You have entered the first boss wave, there's no turning back now! MUHAHAH");
					player.message("[RAIDS] Vespula Wave");
				}
			}
			break;

		case 23555:
			if (player.skills.getLevel(Skill.AGILITY) <= 52) {
				player.message("You cannot complete this course");
			} else {
				player.move(new Position(2998, 3931, 0));
			}
			break;

		case 2971:
			if (player.getBossPoints() <= 250) {
				player.message("You need to offer a sacrifice of 250 Boss Points to enter Chimera's lair!");
				return false;
			}
		      ChimeraUtility.generateSpawn();
			 World.sendMessage(player.getName() + "Has initiated the Chimera Boss! Wish him luck!!");
			player.move(new Position(2420, 4689, 0));
			break;

		case 1558:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;
		case 23728:
		case 23727:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;
		case 27264:
			player.bank.open();
			break;
		case 25824:
			Tanning.open(player);
			break;

		/* Bandos godwars. */
		case 27485:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 27486:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;
		case 16529:
			player.move(new Position(3142, 3513));

			break;

		case 16530:
			player.move(new Position(3137, 3516));

			break;

		case 25016:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 1, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 1, player.getY(), player.getHeight()));
			}
			player.message("Go past the gaps to capture the implings!");
			break;
		case 6944:
			player.bank.open();
			break;

		case 16510:
			if (player.getPosition().equals(new Position(2880, 9813))) {
				player.move(new Position(2878, 9813));
				player.message("you have avoided the spikes");

			} else if (player.getPosition().equals(new Position(2878, 9813))) {
				player.move(new Position(2880, 9813));
				player.message("you have avoided the spikes!");
			}
			break;

		/*
		 * case 29789: // player.animate(ANIM); if (player.getX() == 3311 &&
		 * player.getY() == 5368) { player.walk(new Position(3310, player.getX()),
		 * true); player.walk(new Position(5373, player.getY()), true);
		 * player.face(Direction.NORTH); } break;
		 */

		// Barrows Teleport
		case 23674:
			if (!player.right.isDonator(player)) {
				player.send(new SendMessage("You cannot enter this portal."));
			} else {
				player.move(new Position(2894, 2727, 0));
				player.send(new SendFadeScreen("@or2@Welcome To The Secret Barrows Location", 1, 3));
			}
			break;
		// Lava Dragon Teleport
		case 23675:
			if (!player.right.isDonator(player)) {
				player.send(new SendMessage("You cannot enter this portal."));
			} else {
				player.move(new Position(2910, 9903, 0));
				player.send(new SendFadeScreen("@or2@Welcome To Lava Dragons", 1, 3));
			}
			break;
		// Giant roc
		case 23676:
			if (!player.right.isDonator(player)) {
				player.send(new SendMessage("You cannot enter this portal."));
			} else {
				player.move(new Position(2594, 3910, 0));
				player.send(new SendFadeScreen("@or2@Welcome To Giant Roc's Cave", 1, 3));
			}
			break;
		/**
		 * Basically, copy the code. change the case ID So it matches the Object ID. so
		 * if i wanted to do lava dragon portals which is 23676. yeah so this is the
		 * only file you gotta edit for the portals.
		 */

		case 29042:
			player.send(
					new SendMessage("@or2@You have completed @lre@ Barrows Minigame @red@" + player.getName() + "!"));
			Teleportation.teleport(player, Config.DEFAULT_POSITION, 20, () -> {
				player.send(new SendFadeScreen("@or2@Thanks for completing barrows!", 1, 3));
				player.send(new SendMessage("@red@Congratulations"));
			});
			break;

		case 31556: 
			player.move(new Position(3223, 10184, 0));
			break;

		case 24064:
			player.move(new Position(3046, 3372, 0));
			player.message("Welcome to Brutal's Gambling zone!");
			break;

		case 4151:
			if(player.getCombat().inCombat()) {
				player.message("You cannot teleport via the portal whilst in combat.");
				return true; 
			}
			if(player.isTeleblocked()) {
				player.message("You are currently teleblocked.");
				return true;
			}
			if (!player.revstele.elapsed(10, TimeUnit.SECONDS)) {
				player.dialogueFactory.sendNpcChat(1152, "You can only do this once every " + "10 Seconds!",
						"Time Passed: " + Utility.getTime(player.revstele.elapsedTime())).execute();

				return true;
			}
			player.revstele.reset();
            player.move(new Position(3086, 3501, 0));
			break;
		case 3399:
			if (player.inventory.getFreeSlots() <= 2) {
				player.dialogueFactory.sendNpcChat(1152, "You need atleast 2 free slots to take the free supplies.")
						.execute();

				return true;
			}
			if (!player.takeobj.elapsed(1, TimeUnit.MINUTES)) {
				player.dialogueFactory.sendNpcChat(1152, "You can only do this once every " + 1 + " minutes!",
						"Time Passed: " + Utility.getTime(player.takeobj.elapsedTime())).execute();

				return true;
			}
			player.dialogueFactory.sendNpcChat(1152, "This should allow you to enter the animated room!").execute();
			player.animate(881);

			player.takeobj.reset();
			player.inventory.add(8851, 250);
			break;

		case 29771:
			if (player.inventory.getFreeSlots() <= 13) {
				player.dialogueFactory.sendNpcChat(1152, "You need atleast 13 free slots to take the free supplies.")
						.execute();

				return true;
			}
			if (!player.takeobj.elapsed(2, TimeUnit.MINUTES)) {
				player.dialogueFactory.sendNpcChat(1152, "You can only do this once every " + 2 + " minutes!",
						"Time Passed: " + Utility.getTime(player.takeobj.elapsedTime())).execute();

				return true;
			}
			player.dialogueFactory.sendNpcChat(1152, "You've stolen the supplies before anyone noticed!").execute();
			player.animate(881);

			player.takeobj.reset();
			player.inventory.add(952, 1);
			player.inventory.add(1755, 1);
			player.inventory.add(1269, 1);
			player.inventory.add(1353, 1);
			player.inventory.add(2347, 1);
			player.inventory.add(946, 1);
			player.inventory.add(5331, 1);
			player.inventory.add(5329, 1);
			player.inventory.add(5341, 1);
			player.inventory.add(10010, 1);
			player.inventory.add(590, 1);
			player.inventory.add(303, 1);

			break;

		case 26281:
			if (player.inventory.getFreeSlots() <= 2) {
				player.dialogueFactory.sendNpcChat(345, "You need atleast 2 free slots to take the free supplies.")
						.execute();

				return true;
			}
			if (!player.takeAntiFireshieldDelay.elapsed(2, TimeUnit.MINUTES)) {
				player.dialogueFactory
						.sendNpcChat(345, "You can only do this once every " + 2 + " minutes!",
								"Time Passed: " + Utility.getTime(player.takeAntiFireshieldDelay.elapsedTime()))
						.execute();

				return true;
			}
			player.dialogueFactory.sendNpcChat(345, "You've stolen the shield before anyone could see you!").execute();
			player.animate(881);
			player.takeAntiFireshieldDelay.reset();
			player.inventory.add(1540, 1);

			World.schedule(new ObjectReplacementEvent(object, 40));

			break;

		case 32293:
			if (player.inventory.getFreeSlots() <= 2) {
				player.dialogueFactory.sendNpcChat(7481, "You need atleast 2 free slots to take the free supplies.")
						.execute();

				return true;
			}
			if (!player.takeHammersDelay.elapsed(2, TimeUnit.MINUTES)) {
				player.dialogueFactory.sendNpcChat(7481, "You can only do this once every " + 2 + " minutes!",
						"Time Passed: " + Utility.getTime(player.takeHammersDelay.elapsedTime())).execute();

				return true;
			}
			player.dialogueFactory.sendNpcChat(7481, "You've stolen the hammers before anyone could see you!")
					.execute();
			player.takeHammersDelay.reset();
			player.animate(881);
			player.inventory.add(2347, 1);

			World.schedule(new ObjectReplacementEvent(object, 40));

			break;

		case 15931:
			if (player.inventory.getFreeSlots() <= 2) {
				player.dialogueFactory.sendNpcChat(7481, "You need atleast 2 free slots to take the free supplies.")
						.execute();

				return true;
			}
			if (!player.takeLogsDelay.elapsed(2, TimeUnit.MINUTES)) {
				player.dialogueFactory.sendNpcChat(7481, "You can only do this once every " + 2 + " minutes!",
						"Time Passed: " + Utility.getTime(player.takeLogsDelay.elapsedTime())).execute();

				return true;
			}
			player.dialogueFactory.sendNpcChat(7481, "You've stolen the Logs before anyone could see you!").execute();
			player.takeLogsDelay.reset();
			player.animate(881);

			int random = Utility.random(1, 5);

			if (random == 1) {
				player.inventory.add(1511, 10);
				player.inventory.add(1513, 3);
			}
			if (random == 2) {
				player.inventory.add(1515, 2);
				player.inventory.add(1517, 5);
			}
			if (random == 3) {
				player.inventory.add(1519, 3);
				player.inventory.add(1511, 6);
			}
			if (random == 4) {
				player.inventory.add(2862, 40);
				player.inventory.add(1511, 4);
			}
			if (random == 5) {
				player.inventory.add(1521, 6);
				player.inventory.add(1511, 3);
			}

			World.schedule(new ObjectReplacementEvent(object, 40));
			break;

		case 2873:
			player.dialogueFactory.sendStatement("Would you like to purchase a god cape for 25,000 coins?")
					.sendOption("Yes", () -> {
						if (!player.quest.completed(QuestManager.KOLODIONS_ARENA)) {
							player.send(new SendMessage(
									"You must complete the quest 'Kolodion's Arena' before doing this!"));
							return;

						}
						if (player.inventory.contains(new Item(995, 25000))) {
							player.inventory.addOrDrop(new Item(2412));
							return;
						}
						player.send(new SendMessage("You do not have enough coins to do this!"));
					}, "No", () -> player.dialogueFactory.clear()).execute();
			return true;
		case 2874:
			player.dialogueFactory.sendStatement("Would you like to purchase a god cape for 25,000 coins?")
					.sendOption("Yes", () -> {
						if (!player.quest.completed(QuestManager.KOLODIONS_ARENA)) {
							player.send(new SendMessage(
									"You must complete the quest 'Kolodion's Arena' before doing this!"));
							return;

						}
						if (player.inventory.contains(new Item(995, 25000))) {
							player.inventory.addOrDrop(new Item(2414));
							return;
						}
						player.send(new SendMessage("You do not have enough coins to do this!"));
					}, "No", () -> player.dialogueFactory.clear()).execute();
			return true;
		case 2875:
			player.dialogueFactory.sendStatement("Would you like to purchase a god cape for 25,000 coins?")
					.sendOption("Yes", () -> {
						if (!player.quest.completed(QuestManager.KOLODIONS_ARENA)) {
							player.send(new SendMessage(
									"You must complete the quest 'Kolodion's Arena' before doing this!"));
							return;

						}
						if (player.inventory.contains(new Item(995, 25000))) {
							player.inventory.addOrDrop(new Item(2413));
							return;
						}
						player.send(new SendMessage("You do not have enough coins to do this!"));
					}, "No", () -> player.dialogueFactory.clear()).execute();
			return true;

		case 15516:
			player.move(new Position(1831, 3556));
			player.face(Direction.EAST);
			break;
		case 2878:
			player.move(new Position(2509, 4689));
			break;
		case 2879:
			player.move(new Position(2542, 4718));
			break;

		case 16254:
			TeleportHandler.open(player);
			break;

		case 29749:
			// Skill Area:
			if (player.getY() == 3483) {
				if (player.skills.getTotalLevel() < 1250 && !PlayerRight.isDonator(player)) {
					player.dialogueFactory.sendStatement("You need a total skill level of 1,250",
							"to enter this skilling area!");
					player.dialogueFactory.execute();
					return true;
				}
				player.walk(new Position(player.getX(), 3481), true);
				player.face(Direction.NORTH);
				return true;
			} else if (player.getY() == 3481) {
				player.walk(new Position(player.getX(), 3483), true);
				player.face(Direction.SOUTH);
				return true;
			}

			if (player.pet != null) {
				player.dialogueFactory
						.sendNpcChat(player.pet.id, "I'm sorry #name,", "but I can not enter this arena with you!")
						.execute();
				return true;
			}

			if (player.getCombat().isUnderAttack()) {
				player.message("You can't do this until you are fully out of combat!");
				return true;
			}

			player.locking.lock(1);
			player.prayer.resetProtection();

			// Event Area:
			if (player.getX() == 3084) {
				player.walk(new Position(3082, player.getY()), true);
				player.face(Direction.WEST);
			} else if (player.getX() == 3082) {
				player.walk(new Position(3084, player.getY()), true);
				player.face(Direction.EAST);
			} else if (player.getY() == 3515) {
				player.walk(new Position(player.getX(), 3513), true);
				player.face(Direction.SOUTH);
			} else if (player.getY() == 3513) {
				player.walk(new Position(player.getX(), 3515), true);
				player.face(Direction.NORTH);
			} else if (player.getX() == 3073) {
				player.walk(new Position(3075, player.getY()), true);
				player.face(Direction.WEST);
			} else if (player.getX() == 3075) {
				player.walk(new Position(3073, player.getY()), true);
				player.face(Direction.EAST);
			} else if (player.getY() == 3504) {
				player.walk(new Position(player.getX(), 3506), true);
				player.face(Direction.SOUTH);
			} else if (player.getY() == 3506) {
				player.walk(new Position(player.getX(), 3504), true);
				player.face(Direction.NORTH);
			}
			break;

		case 26762: {
			Position end = null;
			player.face(new Position(object.getX(), object.getY()));
			player.animate(844);
			if (player.getPosition().equals(new Position(3233, 3938))
					|| player.getPosition().equals(new Position(3232, 3938))) {
				end = new Position(3233, 10332);
			} else if (player.getPosition().equals(new Position(3233, 3950))
					|| player.getPosition().equals(new Position(3232, 3950))) {
				end = new Position(3232, 10351);
			} else if (player.getPosition().equals(new Position(3242, 3948))
					|| player.getPosition().equals(new Position(3243, 3948))) {
				end = new Position(3243, 10351);
			}
			Position finalEnd = end;
			if (finalEnd != null) {
				player.move(finalEnd);
				player.getCombat().reset();
				player.getCombat().clearIncoming();
			}
			break;
		}
		case 26763: {
			Position end = null;
			player.face(new Position(object.getX(), object.getY()));
			player.animate(844);
			if (player.getPosition().equals(new Position(3233, 10332))) {
				end = new Position(3233, 3938);
			} else if (player.getPosition().equals(new Position(3232, 10351))) {
				end = new Position(3233, 3950);
			} else if (player.getPosition().equals(new Position(3243, 10351))) {
				end = new Position(3242, 3948);
			}
			Position finalEnd = end;
			if (finalEnd != null) {
				player.move(finalEnd);
				player.getCombat().reset();
				player.getCombat().clearIncoming();
			}
			break;
		}

		case 26760: {
			if (player.skills.getTotalLevel() < 1250 && !PlayerRight.isDonator(player)) {
				player.dialogueFactory.sendStatement("You need a total skill level of 1,250",
						"to enter this skilling area!");
				player.dialogueFactory.execute();
				return false;
			}

			Position destination = null;
			Direction direction = null;

			if (player.getY() < 3480) {
				player.movement.walkTo(new Position(player.getX(), 3480));
			} else if (player.getY() > 3481) {
				player.movement.walkTo(new Position(player.getX(), 3481));
			}

			if (player.getY() == 3481) {
				direction = Direction.SOUTH;
				destination = new Position(player.getX(), 3480);
			} else if (player.getY() == 3480) {
				direction = Direction.NORTH;
				destination = new Position(player.getX(), 3481);
			}

			if (player.getY() < 3944) {
				player.movement.walkTo(new Position(player.getX(), 3944));
			} else if (player.getY() > 3945) {
				player.movement.walkTo(new Position(player.getX(), 3945));
			}

			if (player.getY() == 3945) {
				direction = Direction.SOUTH;
				destination = new Position(player.getX(), 3944);
			} else if (player.getY() == 3944) {
				direction = Direction.NORTH;
				destination = new Position(player.getX(), 3945);
			}

			if (direction != null) {
				player.getCombat().reset();
				player.face(direction);
				player.locking.lock(1, LockType.MASTER_WITH_MOVEMENT);
				player.inventory.remove(995, 5000);
				player.movement.walkTo(destination);
			}
			break;
		}

		case 3192:
			player.interfaceManager.open(InterfaceConstants.DUEL_SCOREBOARD);
			break;

		case 6948:
			player.interfaceManager.open(InterfaceConstants.DEPOSIT_BOX);
			break;

		case 2631:
			if (!player.inventory.contains(1591)) {
				player.dialogueFactory.sendStatement("Looks like I need a key to unlock this.",
						"Maybe if I kill that guard I can get the key.").execute();
				break;
			}
			if (player.getPosition().matches(2931, 9690)) {
				player.move(new Position(2931, 9689));
			} else if (player.getPosition().matches(2931, 9689)) {
				player.move(new Position(2931, 9690));
			}
			break;

		case 1524:
			if (player.getPosition().matches(2907, 9698)) {
				player.move(new Position(2907, 9697));
			} else if (player.getPosition().matches(2907, 9697)) {
				player.move(new Position(2907, 9698));
			}

			if (player.getPosition().matches(2958, 3821) || player.getPosition().matches(2959, 3821)) {
				player.move(new Position(2957, 3821));
			} else if (player.getPosition().matches(2957, 3821) || player.getPosition().matches(2956, 3821)) {
				player.move(new Position(2958, 3821));
			}
			break;

		case 1521:
			if (player.getPosition().matches(2908, 9698)) {
				player.move(new Position(2908, 9697));
			} else if (player.getPosition().matches(2908, 9697)) {
				player.move(new Position(2908, 9698));
			}

			if (player.getPosition().matches(2958, 3820) || player.getPosition().matches(2959, 3820)) {
				player.move(new Position(2957, 3820));
			} else if (player.getPosition().matches(2957, 3820) || player.getPosition().matches(2956, 3820)) {
				player.move(new Position(2958, 3820));
			}

			break;

		case 2623: {
			boolean hasKey = player.inventory.contains(1590);
			if (player.getPosition().matches(2923, 9803)) {
				player.action.execute(new DoorAction(player, object, new Position(2924, 9803), Direction.EAST,
						player1 -> hasKey, "You need a dusty key to unlock this door."));
			} else if (player.getPosition().matches(2924, 9803)) {
				player.action.execute(new DoorAction(player, object, new Position(2923, 9803), Direction.WEST,
						player1 -> hasKey, "You need a dusty key to unlock this door."));
			}
			break;
		}

		case 1815:// WILDERNESS ARDY LEVER
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			player.action.execute(new LeverAction(player, object, new Position(2561, 3311, 0), Direction.WEST));
			break;

		case 1814:// ARDY WILDERNESS LEVER
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			player.action.execute(new LeverAction(player, object, new Position(3153, 3923, 0), Direction.WEST));
			break;

		case 5959:// MAGE BANK LEVER
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			player.action.execute(new LeverAction(player, object, new Position(2539, 4712, 0), Direction.WEST));
			break;

		case 9706:// MAGE BANK LEVER
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			player.action.execute(new LeverAction(player, object, new Position(3105, 3951, 0), Direction.WEST));
			break;

		case 9707:// MAGE BANK LEVER
			player.action.execute(new LeverAction(player, object, new Position(3105, 3956, 0), Direction.NORTH));
			break;

		case 5960:// MAGE BANK LEVER
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			player.action.execute(new LeverAction(player, object, new Position(3090, 3956, 0), Direction.SOUTH));
			break;

		case 18987:// KING BLACK DRAGON LADDER
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			player.action.execute(new LadderAction(player, object, new Position(3069, 10257, 0)));
			break;

		case 18988:// KING BLACK DRAGON LADDER
			
			player.action.execute(new LadderAction(player, object, new Position(3017, 3850, 0)));
			break;

		case 1816:// KING BLACK DRAGON LEVER
			
			player.action.execute(new LeverAction(player, object, new Position(2271, 4680, 0), Direction.SOUTH));
			break;

		case 1817:// KING BLACK DRAGON LEVER
		
			player.action.execute(new LeverAction(player, object, new Position(3067, 10253, 0), Direction.SOUTH));
			break;

		case 16537:// SLAYER CHAIN
			if (player.getHeight() == 0) {
				player.action.execute(new LadderAction(player, object, new Position(player.getX(), player.getY(), 1),
						PlayerRight::isDonator, "You need to be a donator to use this shortcut!"));
			} else {
				player.action.execute(new LadderAction(player, object, new Position(player.getX(), player.getY(), 2),
						PlayerRight::isDonator, "You need to be a donator to use this shortcut!"));
			}
			break;

		case 16538:// SLAYER CHAIN
			if (player.getHeight() == 1) {
				player.action.execute(new LadderAction(player, object, new Position(player.getX(), player.getY(), 0),
						PlayerRight::isDonator, "You need to be a donator to use this shortcut!"));
			} else {
				player.action.execute(new LadderAction(player, object, new Position(player.getX(), player.getY(), 1),
						PlayerRight::isDonator, "You need to be a donator to use this shortcut!"));
			}
			break;

		/**
		 * START OF DOOR SYSTEM, BECAUSE HARRYL THE FUCKER WILL NOT WRITE A NEW ONE SO I
		 * HAVE TO USE THIS BULLSHIT !!!!?!?!?!?!
		 **/

		case 30388:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 30387:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 25814:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 1723:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;
		case 24309:
		case 24306:
			if (player.getPosition().getX() == 2855 && player.getPosition().getY() == 3546
					&& player.getPosition().getHeight() == 0) {
				return true;
			}
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;
		case 1733:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 11773:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 25813:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 14917:// SEERS DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3091, 3882, 0), Direction.SOUTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3093, 3879, 0), Direction.NORTH));
			}
			break;

		case 1727:// RIGHT DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3337, 3896, 0), Direction.NORTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3337, 3895, 0), Direction.SOUTH));
			}
			break;

		case 1728:// LEFT DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3336, 3896, 0), Direction.NORTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3336, 3895, 0), Direction.SOUTH));
			}
			break;

		case 2111:// SLAYER DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3429, 3536, 0), Direction.NORTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3429, 3535, 0), Direction.SOUTH));
			}
			break;

		case 2108:// SLAYER DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3428, 3536, 0), Direction.NORTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3428, 3535, 0), Direction.SOUTH));
			}
			break;

		case 2102:// SLAYER DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3426, 3556, 1), Direction.NORTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3426, 3555, 1), Direction.SOUTH));
			}
			break;

		case 2104:// SLAYER DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3427, 3556, 1), Direction.NORTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3427, 3555, 1), Direction.SOUTH));
			}
			break;

		case 2100:// SLAYER DOOR
			if (player.getY() <= object.getY()) {
				player.action.execute(new DoorAction(player, object, new Position(3445, 3555, 2), Direction.NORTH));
			} else {
				player.action.execute(new DoorAction(player, object, new Position(3445, 3554, 2), Direction.SOUTH));
			}
			break;

		/** END OF DOORS.CFG KAPPA HONESTLY THIS IS JUST AS BAD FUCK ME UP THE ASS! **/

		case 21738:
			if (player.skills.getLevel(Skill.AGILITY) < 12) {
				player.send(new SendMessage("You need level 12 agility or more to use this."));
				break;
			}

			if (player.getPosition().getX() == 2649 && player.getPosition().getY() == 9562) {
				World.schedule(new SteppingStoneTask(player, object) {
					@Override
					public void onExecute() {
						if (tick == 0) {
							player.face(object.getPosition());
						} else if (tick == 1) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
						} else if (tick == 3) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
						} else if (tick == 5) {
							player.forceMove(0, 1, 741, 15, 30, new Position(-1, 0), Direction.WEST);
						} else if (tick == 7) {
							player.forceMove(0, 1, 741, 15, 30, new Position(-1, 0), Direction.WEST);
						} else if (tick == 9) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
						} else if (tick == 11) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
						} else if (tick == 13) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, -1), Direction.SOUTH);
						} else if (tick >= 15) {
							cancel();
						}
					}
				});
			}

			break;

		// brimhaven log balance
		case 20882:
			if (player.skills.getLevel(Skill.AGILITY) < 30) {
				player.send(new SendMessage("You need at least level 30 agility to use this."));
				break;
			}
			break;
		// brimhaven go upstairs
		case 21725:
			player.move(new Position(2636, 9510, 2));
			break;

		// brimhaven go downstairs
		case 21726:
			player.move(new Position(2636, 9517));
			break;

		case 21739:
			if (player.skills.getLevel(Skill.AGILITY) < 12) {
				player.send(new SendMessage("You need level 12 agility or more to use this."));
				break;
			}

			if (player.getPosition().getX() == 2647 && player.getPosition().getY() == 9557) {
				World.schedule(new SteppingStoneTask(player, object) {
					@Override
					public void onExecute() {
						if (tick == 0) {
							player.face(object.getPosition());
						} else if (tick == 1) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
						} else if (tick == 3) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
						} else if (tick == 5) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
						} else if (tick == 7) {
							player.forceMove(0, 1, 741, 15, 30, new Position(1, 0), Direction.EAST);
						} else if (tick == 9) {
							player.forceMove(0, 1, 741, 15, 30, new Position(1, 0), Direction.EAST);
						} else if (tick == 11) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
						} else if (tick == 13) {
							player.forceMove(0, 1, 741, 15, 30, new Position(0, 1), Direction.NORTH);
						} else if (tick >= 15) {
							cancel();
						}
					}
				});
			}
			break;

		case 29993: // fremmy slayer duneon steps
			if (player.getPosition().matches(2703, 9989)) {
				player.move(new Position(2703, 9991));
			} else if (player.getPosition().matches(2703, 9991)) {
				player.move(new Position(2703, 9989));
			}
			break;

		case 30198:
			if (player.getPosition().getX() == 2684 && player.getPosition().getY() == 9436) {
				player.move(new Position(2697, 9436));
			} else if (player.getPosition().getX() == 2697 && player.getPosition().getY() == 9436) {
				player.move(new Position(2684, 9436));
			}
			break;

		case 21731:
		case 21732:
		case 21733:
		case 21734:
		case 21735:
			World.schedule(new ChopVineTask(player, object, 5));
			break;

		/* Slayer staircase */
		case 2114:
			player.move(new Position(player.getX(), player.getY(), 1));
			player.send(new SendMessage("You climb up the staircase."));
			break;
		case 2118:
			player.move(new Position(3438, 3538, 0));
			player.send(new SendMessage("You climb down the staircase."));
			break;
		case 2120:
			player.move(new Position(3412, 3540, 1));
			player.send(new SendMessage("You climb down the staircase."));
			break;
		case 2119:
			player.move(new Position(3417, 3540, 2));
			player.send(new SendMessage("You climb up the staircase."));
			break;

		/* Webs */
		case 733: {
			Item weapon = player.equipment.getWeapon();
			int disarmattack = 1;
			int disaramattackrandom = Utility.random(disarmattack, 3);

			if (weapon == null) {
				player.message("You need a weapon to cut this web!");
				return true;
			}

			boolean hasKnife = player.inventory.contains(946);

			if (!hasKnife && !weapon.getName().contains("claw") && !weapon.getName().contains("whip")
					&& !weapon.getName().contains("sword") && !weapon.getName().contains("dagger")
					&& !weapon.getName().contains("scimitar") && !weapon.getName().contains("axe")) {
				player.message("You need a sharp weapon or knife to cut this web!");
				return true;
			}

			player.animate(new Animation(451, UpdatePriority.HIGH));
			if (RandomUtils.success(1 / 3.0)) {
				break;
			}
			if (disaramattackrandom == disarmattack) {
				World.schedule(new ObjectReplacementEvent(object, 40));
				player.send(new SendMessage("You have cut the web apart!"));

			} else {
				player.send(new SendMessage("You have failed to slash the web apart."));
			}
			break;
		}
		case 1:
			object.transform(11833);
			break;

		/* Pest control */
		case 14315:
			
			DialogueFactory factory1 = player.dialogueFactory;
			factory1.sendOption("Enter Pest Control",
					() -> PestControl.enter(player),
					"Enter Boss Control",
					() -> BossControl.enter(player), "Nevermind",
					factory1::clear);
			factory1.execute();
			break;
			// player.message("Pest control currently disabled");
			//player.message("Just so you now, the void magers trap everyone who join their lobby.");
			//PestControl.enter(player);
			//break;

		/* Warrior Guild */
		case 24318:
			int attack_strength = (player.skills.getMaxLevel(Skill.ATTACK) + player.skills.getMaxLevel(Skill.STRENGTH));
			if (player.getPosition().equals(new Position(2876, 3546, 0))) {
				player.move(new Position(2877, 3546));
				Activity.forActivity(player, it -> it.onRegionChange(player));
				player.face(Direction.EAST);
				break;
			}
			if (attack_strength >= 130) {
				player.move(new Position(2876, 3546));
				WarriorGuild.create(player);
				player.face(Direction.WEST);
				break;
			}
			player.dialogueFactory.sendStatement("You need a combined level of 130 in both your attack",
					"and strength level to enter the Warrior's guild.").execute();
			break;

		/* Fight cave */
		case 11833:
			player.locking.lock();
			player.send(new SendFadeScreen("Welcome to the Fight Caves!", 1, 3));
			World.schedule(5, () -> {
				FightCaves.create(player);
				player.locking.unlock();
			});
			break;
		
			//DUOINFERNO
		case 13617:
			if (!player.hasAllForOnePartner()) {
				player.sendMessage("You need to an All For One partner to enter this portal!");
				player.sendMessage("Right click this portal > Set Options and send a partner request!");
				break;
			}
			if (player.partyLeader != player) {
				player.sendMessage("Only your party leader can start All vs one v4!");
				break;
			}
			if (!player.getPosition().isWithinDistance(player.allForOnePartner.getPosition(), 15)) {
				player.sendMessage("Your partner needs to be with you in order to start this session!");
				break;
			}
			DuoInferno4Session session17 = new DuoInferno4Session();
			session17.onStart(player);
		//	player.setDynamicRegion(new DynamicRegion(player, RegionType.All_FOR_ONE_4));
		//	player.getAllForOnePartner().setDynamicRegion(player.getDynamicRegion());
			break;
			
			
			//raids1
		case 13619: // GLOBAL-BM-1
			if (player.getRaidPartners().size() <= 0) {
				player.sendMessage("You need to a Raids 1 party to enter this portal!");
				player.sendMessage("Right click this portal > Set Options and send a partner request!");
				break;
			}
//			if (!player.hasAllForOnePartner()) {
//				player.sendMessage("You need to an Raids 2 partner to enter this portal!");
//				player.sendMessage("Right click this portal > Set Options and send a partner request!");
//				break;
//			}
			if (player.partyLeader != player) {
				player.sendMessage("Only your party leader can start Raids 1!");
				break;
			}
			
			boolean partyPresent11 = true;
			
			for (Player p : player.getRaidPartners())
				if (!player.getPosition().isWithinDistance(p.getPosition(), 15))
					partyPresent11 = false;
			
//			if (!player.getPosition().isWithinDistance(player.allForOnePartner.getPosition(), 15)) {
//				player.sendMessage("Your partner needs to be with you in order to start this session!");
//				break;
//			}
			
			if (!partyPresent11) {
				player.sendMessage("Your entire party needs to be with you in order to start this session!");
				break;
			}
			
			Raids14Session session11 = new Raids14Session();
			session11.onStart(player);
		//	player.setDynamicRegion(new DynamicRegion(player, RegionType.All_FOR_ONE_4));
		//	player.getAllForOnePartner().setDynamicRegion(player.getDynamicRegion());
			break;
			//
		case 14894:
			//
			break;
		case 13620:
			//
				break;
		
			
				//RAIDS 2
		case 13618: // GLOBAL-BM-5
			if (player.getRaidPartners().size() <= 0) {
				player.sendMessage("You need to a Raids 2 party to enter this portal!");
				player.sendMessage("Right click this portal > Set Options and send a partner request!");
				break;
			}
//			if (!player.hasAllForOnePartner()) {
//				player.sendMessage("You need to an Raids 2 partner to enter this portal!");
//				player.sendMessage("Right click this portal > Set Options and send a partner request!");
//				break;
//			}
			if (player.partyLeader != player) {
				player.sendMessage("Only your party leader can start Raids 2!");
				break;
			}
			
			boolean partyPresent12 = true;
			
			for (Player p : player.getRaidPartners())
				if (!player.getPosition().isWithinDistance(p.getPosition(), 15))
					partyPresent12 = false;
			
//			if (!player.getPosition().isWithinDistance(player.allForOnePartner.getPosition(), 15)) {
//				player.sendMessage("Your partner needs to be with you in order to start this session!");
//				break;
//			}
			
			if (!partyPresent12) {
				player.sendMessage("Your entire party needs to be with you in order to start this session!");
				break;
			}
			
			Raids24Session session12 = new Raids24Session();
			session12.onStart(player);
		//	player.setDynamicRegion(new DynamicRegion(player, RegionType.All_FOR_ONE_4));
		//	player.getAllForOnePartner().setDynamicRegion(player.getDynamicRegion());
			break;
			//
		case 4389:
			//
			break;
		
			
		case 12260:
				player.move(new Position(3169, 4958, 0));			
			break;

		/* Regular Bank */
		
		case 10060:
			System.out.println("Here wtf");
			player.face(object);
			player.bank.open();
			break;

		/* Clan Bank */
		case 11338:
			player.dialogueFactory
					.sendStatement("Clan bank is currently not accessible!", "This is intended for future content!")
					.execute();
			break;

		/* Well */
		case 4004:
			player.dialogueFactory.sendDialogue(new WellOfGoodwillDialogue());
			break;

		/* Management machine */
		case 11546:
			if (PlayerRight.isManagement(player)) {
				StaffPanel.open(player, PanelType.INFORMATION_PANEL);
			}
			break;
			
		case 31561:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		case 153:
			if (!player.RejuvDelay.elapsed(2, TimeUnit.SECONDS)) {
				return true;
			}
			
			if (!player.RejuvDelay.elapsed(2, TimeUnit.MINUTES)) {
				player.dialogueFactory.sendNpcChat(7481, "You can only do this once every 2" + " minutes!",
						"Time Passed: " + Utility.getTime(player.RejuvDelay.elapsedTime())).execute();

				return true;
			}
			
			for (int skill = 0; skill < Skill.SKILL_COUNT; skill++) {
				player.skills.setLevel(skill, player.skills.getMaxLevel(skill));
			}
			player.RejuvDelay.reset();
			player.runEnergy = 100;
			player.skulling.unskull();
			player.skills.restoreAll();
			player.inventory.refresh();
			player.action.reset();
			player.playerAssistant.reset();
			player.interfaceManager.close();
			player.setSpecialActivated(false);
			player.getCombat().getDamageCache().clear();
			CombatSpecial.restore(player, 100);
			player.unpoison();
			CombatUtil.cancelEffect(player, CombatEffectType.POISON);
			CombatUtil.cancelEffect(player, CombatEffectType.VENOM);
			player.movement.reset();
			player.teleblockTimer.set(0);
			player.unvenom();
			player.unpoison();
			World.schedule(new SuperAntipoisonTask(player).attach(player));
			player.equipment.updateAnimation();
			player.animate(1327);
			player.send(
					new SendMessage("You take a sip from the juice fountain and feel your body pulsing with ecstasy."));
			break;

		/* Altar of the occult */
		case 409:
			player.dialogueFactory.sendOption("Modern", () -> {
				Autocast.reset(player);
				player.animate(new Animation(645));
				player.spellbook = Spellbook.MODERN;
				player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
				player.send(new SendMessage(
						"You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			}, "Ancient", () -> {
				Autocast.reset(player);
				player.animate(new Animation(645));
				player.spellbook = Spellbook.ANCIENT;
				player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
				player.send(new SendMessage(
						"You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			}, 
			  "Lunar", () -> {
				Autocast.reset(player);
				player.animate(new Animation(645));
				player.spellbook = Spellbook.LUNAR;
				player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
				player.send(new SendMessage(
						"You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			},
			  "Recharge Prayer", () -> {
				player.animate(new Animation(645));
				player.skills.setLevel(Skill.PRAYER, player.skills.getMaxLevel(Skill.PRAYER));
			}).execute();
			break;

		case 29150:
			player.dialogueFactory.sendOption("Modern", () -> {
				Autocast.reset(player);
				player.animate(new Animation(645));
				player.spellbook = Spellbook.MODERN;
				player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
				player.send(new SendMessage(
						"You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			}, "Ancient", () -> {
				Autocast.reset(player);
				player.animate(new Animation(645));
				player.spellbook = Spellbook.ANCIENT;
				player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
				player.send(new SendMessage(
						"You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			}, 
			  "Lunar", () -> {
				Autocast.reset(player);
				player.animate(new Animation(645));
				player.spellbook = Spellbook.LUNAR;
				player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
				player.send(new SendMessage(
						"You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			},
			  "Recharge Prayer", () -> {
				player.animate(new Animation(645));
				player.skills.setLevel(Skill.PRAYER, player.skills.getMaxLevel(Skill.PRAYER));
			}).execute();
			break;
		/* Ornate Restoration Pool */
		/*
		 * case 29241: if (PlayerRight.isManagement(player)) { for (int skill = 0; skill
		 * < Skill.SKILL_COUNT; skill++) { player.skills.setLevel(skill,
		 * player.skills.getMaxLevel(skill)); } player.send(new SendMessage(
		 * "You take a sip from the juice fountain and feel your body pulsing with ecstasy."
		 * )); } break;
		 */

		/* Donator deposit. */
		case 26254:
			player.donatorDeposit.open();
			break;

		/* Construction. */
		case 15478:
			player.dialogueFactory.sendOption("Enter your house", () -> {
				player.dialogueFactory.onAction(player.house::enter);
			}, "Enter friend's house", () -> {
				player.dialogueFactory.onAction(() -> {
					player.send(new SendInputMessage("Enter your friend's name", 10, player.house::enter));
				});
			}, "Enter clan house", () -> {

			}, "Nevermind", player.interfaceManager::close).execute();
			break;
		case 4525:
			player.house.leave();
			break;

		/* Dwarf cannon. */
		case 6:
			CannonManager.load(player);
			break;

		/* Bandos godwars. */
		case 26503:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), player.getHeight()));
			}
			break;

		/* Saradomin godwars. */
		case 26504:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 2, player.getY(), 0));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 2, player.getY(), 0));
			}
			break;

		/* Zamorak godwars. */
		case 26505:
			if (player.getPosition().getY() < object.getPosition().getY()) {
				player.move(new Position(player.getX(), player.getY() + 2, 2));
			} else if (player.getPosition().getY() > object.getPosition().getY()) {
				player.move(new Position(player.getX(), player.getY() - 2, 2));
			}
			break;

		/* Armadyl godwars. */
		case 26502:
			if (player.getPosition().getY() < object.getPosition().getY()) {
				player.move(new Position(player.getX(), player.getY() + 2, 2));
			} else if (player.getPosition().getY() > object.getPosition().getY()) {
				player.move(new Position(player.getX(), player.getY() - 2, 2));
			}
			break;

		/* Wilderness ditch. */
		case 23271: {
			
			  if (player.playTime < 3000) { 
				  player.message("You cannot enter the wilderness until you have 30 minutes of playtime. "
			  + Utility.getTime((3000 - player.playTime) * 3 / 5) + " minutes remaining.");
			  return true;
			  }
				if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
					player.message("@red@You can no longer take custom's into the wilderness!");
					return true;
				}
			 
			if (player.pet != null) {
				player.dialogueFactory
						.sendNpcChat(player.pet.id, "I'm sorry #name,", "but I can not enter the wilderness with you!")
						.execute();
				return true;
			}
			Position offSet = new Position(0, player.getY() < 3522 ? 3 : -3);
			Direction direction = player.getY() < 3522 ? Direction.NORTH : Direction.SOUTH;
			player.forceMove(3, 6132, 33, 60, offSet, direction);
			break;
		}
		case 19209:
			
        		Teleportation.teleport(player, (new Position(1358, 2715, 0)));
        		player.message("Welcome to Secrets Zone 2!");
        	
			break;
		case 19207:
			
    		Teleportation.teleport(player, (new Position(1373, 2727, 0)));
    		player.message("Welcome to Secrets Zone 3!");
    	
		break;
case 19206:
			
    		Teleportation.teleport(player, (new Position(1385, 2712, 0)));
    		player.message("Welcome to Secrets Zone 4!");
    	
		break;
		case 31949:
		case 7478:
			player.bank.open();
			break;

		/* Skeletal wyvern. */
		case 10596:
			if (player.getY() == 9562) {
				player.move(new Position(player.getX(), 9555));
			} else if (player.getY() == 9555) {
				player.move(new Position(player.getX(), 9562));
			}
			break;

		/* Dagganoths */
		case 10229:
			player.action.execute(new LadderAction(player, object, new Position(1912, 4367, 0)));
			break;

		case 10230:
			player.action.execute(new LadderAction(player, object, new Position(2899, 4449, 0)));
			break;

		/* Corporeal beast. */
		case 677: {
			player.move(new Position(player.getX() < 2974 ? 2974 : 2970, player.getY(), player.getHeight()));
			break;
		}

		case 411:
			if (player.skills.getLevel(Skill.PRAYER) < player.skills.getMaxLevel(Skill.PRAYER)) {
				player.animate(new Animation(645));
				player.skills.setLevel(Skill.PRAYER, player.skills.getMaxLevel(Skill.PRAYER));
				player.send(new SendMessage("You recharge your prayer points."));
			} else {
				player.send(new SendMessage("Your prayer is already full."));
			}
			break;

		case 26366:
		case 26365:
		case 26364:
		case 26363:

			int length = PlayerRight.isDonator(player) ? 5 : 10;
			if (!player.godwarsDelay.elapsed(length, TimeUnit.MINUTES)) {
				player.dialogueFactory.sendStatement("You can only do this once every " + length + " minutes!",
						"Time Passed: " + Utility.getTime(player.godwarsDelay.elapsedTime())).execute();
				return true;
			}

			if (player.skills.getLevel(Skill.PRAYER) < player.skills.getMaxLevel(Skill.PRAYER)) {
				player.animate(new Animation(645));
				player.skills.setLevel(Skill.PRAYER, player.skills.getMaxLevel(Skill.PRAYER));
				player.send(new SendMessage("You recharge your prayer points."));
				player.godwarsDelay.reset();
			} else {
				player.send(new SendMessage("Your prayer is already full."));
			}
			break;

		/* Prayer altar. */
		case 7812:
			if (player.skills.getLevel(Skill.PRAYER) < player.skills.getMaxLevel(Skill.PRAYER)) {
				player.animate(new Animation(645));
				player.skills.setLevel(Skill.PRAYER, player.skills.getMaxLevel(Skill.PRAYER));
				player.send(new SendMessage("You recharge your prayer points."));
			} else {
				player.send(new SendMessage("Your prayer is already full."));
			}
			break;

		/* Ancient altar. */
		case 6552: {
			player.animate(new Animation(645));
			Spellbook book = player.spellbook == Spellbook.MODERN || player.spellbook == Spellbook.LUNAR
					? Spellbook.ANCIENT
					: Spellbook.MODERN;
			Autocast.reset(player);
			player.spellbook = book;
			player.interfaceManager.setSidebar(Config.MAGIC_TAB, book.getInterfaceId());
			player.send(new SendMessage("You are now using the " + book.name().toLowerCase() + " spellbook."));
		}
			break;

		/* Lunar altar. */
		case 410: {
			player.animate(new Animation(645));
			Spellbook book = player.spellbook == Spellbook.MODERN || player.spellbook == Spellbook.ANCIENT
					? Spellbook.LUNAR
					: Spellbook.MODERN;
			Autocast.reset(player);
			player.spellbook = book;
			player.interfaceManager.setSidebar(Config.MAGIC_TAB, book.getInterfaceId());
			player.send(new SendMessage("You are now using the " + book.name().toLowerCase() + " spellbook."));
		}
			break;

		/* AFK Tree */
		case 2092:
			System.out.println("Oh");
			break;

		/* LMS Coffer */
		case 29087:
			player.dialogueFactory
					.sendStatement("You have " + Util.toNiceString(player.getLmsCoffer()) + " coins in your coffer")
					.sendOption("Deposit", () -> {
						player.dialogueFactory.onAction(() -> World.schedule(1, () -> player
								.send(new SendInputAmount("How much would you like to deposit?", 10, input -> {
									int amount = Integer.parseInt(input);
									if (player.inventory.contains(new Item(995, amount))) {
										player.inventory.remove(new Item(995, amount));
										player.lmsCoffer += amount;
										player.send(new SendMessage("You have successfully entered "
												+ Util.toNiceString(amount) + " coins into the coffer."));
									} else {
										player.send(new SendMessage("Sorry, you do not have enough coins for that."));
									}
								})))).execute();
					}, "Withdraw", () -> {
						player.dialogueFactory.onAction(() -> World.schedule(1, () -> player
								.send(new SendInputAmount("How much would you like to withdraw?", 10, input -> {
									int amount = Integer.parseInt(input);
									if (player.lmsCoffer >= amount) {
										if (player.inventory.hasCapacityFor(new Item(995, amount))) {
											player.inventory.add(new Item(995, amount));
											player.lmsCoffer -= amount;
											player.send(new SendMessage("You have successfully withdrawn "
													+ Util.toNiceString(amount) + " coins from the coffer."));
										} else {
											player.send(new SendMessage("You do not have room for this!"));
										}
									} else {
										player.send(new SendMessage("Sorry, you only have "
												+ Util.toNiceString(player.lmsCoffer) + " coins in your coffer."));
									}
								})))).execute();
					}).execute();
			break;

		default:
			return false;

		}
		return true;
	}

}
