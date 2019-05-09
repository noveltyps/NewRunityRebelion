package plugin.click.item;
 
import io.server.content.Gambling.RollType;
import io.server.content.activity.impl.zulrah.ZulrahActivity;
import io.server.content.combat.cannon.Cannon;
import io.server.content.combat.cannon.CannonManager;
import io.server.content.consume.Anglerfish;
import io.server.content.mysterybox.impl.LegendaryMysteryBox;
import io.server.content.mysterybox.impl.Raids1MysteryBox;
import io.server.content.mysterybox.impl.Raids2MysteryBox;
import io.server.content.mysterybox.impl.Raids3MysteryBox;
import io.server.content.mysterybox.impl.Raids4MysteryBox;
import io.server.content.mysterybox.impl.Raids5MysteryBox;
import io.server.content.mysterybox.impl.SuperMysteryBox;
import io.server.content.mysterybox.impl.UltraMysteryBox;
import io.server.content.mysterybox.impl.ZombieBox;
import io.server.content.playerguide.PlayerGuideHandler;
import io.server.content.scratchcard.ScratchCard;
import io.server.content.skill.impl.hunter.net.HunterNetting;
import io.server.content.skill.impl.hunter.trap.impl.BirdSnare;
import io.server.content.skill.impl.hunter.trap.impl.BoxTrap;
import io.server.content.skill.impl.slayer.Slayer;
import io.server.content.skill.impl.slayer.SlayerTask;
import io.server.content.skill.impl.woodcutting.BirdsNest;
import io.server.content.votingRewardInterface.VotingRewardHandler;
import io.server.game.action.impl.SpadeAction;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendFadeScreen;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

public class ItemFirstClickPlugin extends PluginContext { // etest

	
	@Override
	protected boolean firstClickItem(Player player, ItemClickEvent event) {
		if ((event.getItem().getId() >= 11238 && event.getItem().getId() <= 11256) || event.getItem().getId() == 19732)
			HunterNetting.lootJar(player, event.getItem());
		switch (event.getItem().getId()) {
		case 10020:
		case 10018:
		case 10014:
		case 10016:
			player.inventory.remove(event.getItem().getId());
			player.inventory.add(10012);
			break;

		case 10008:
			for (int i = 0; i < player.boxes.length; i++) {
				if (player.skills.getLevel(Skill.HUNTER) / 20 < i) {
					player.sendMessage("You must have a Hunter level of " + 20 * i + " to lay another trap.");
					break;
				} else if (player.boxes[i] == null || player.boxes[i].inactive) {
					player.boxes[i] = new BoxTrap(player, 
							new CustomGameObject(BoxTrap.State.SET.objectId, player.getPosition().copy()));
					player.boxes[i].setTrap();
					break;
				}
			}
			break;
		case 10006:
			for (int i = 0; i < player.snares.length; i++) {
				if (player.skills.getLevel(Skill.HUNTER) / 20 < i) {
					player.sendMessage("You must have a Hunter level of " + 20 * i + " to lay another trap.");
					break;
				} else if (player.snares[i] == null || player.snares[i].inactive) {
					player.snares[i] = new BirdSnare(player, 
							new CustomGameObject(BirdSnare.State.SET.objectId, player.getPosition().copy()));
					player.snares[i].setTrap();
					break;
				}
			}
			break;
        /* Dice bag */
        case 15098:
        	player.gambling.rollDice(100, RollType.PUBLIC);
            //DiceBag.roll(player, false);
            break;
        case 7478:
        	new VotingRewardHandler(player).Open();
        	break;
        case 15099:
        	player.gambling.rollDice(0, RollType.BLACKJACK_START);
        	break;
        case 15100:
        	player.gambling.rollDice(0, RollType.BLACKJACK_HIT);
        	break;
		case 6:
			CannonManager.drop(player, new Cannon(player.getName(), player.getPosition()));
			break;
		case 22092:
			if(player.inventory.getFreeSlots() <= 5) {
				player.message("You need more inventory space!");
				return false;
			}
			player.inventory.add(7775, 125000);
			player.message("@red@You've recieved 125,000 Tickets Extra for using the dragon key!");
			player.message("@red@ You've been lucky and recieved a AvO 3 box!");
			player.inventory.addOrDrop(new Item(6833, 1));
		    player.inventory.remove(22092, 1);
			break;
		case 21813:
			player.pkPoints += 25;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.add(995, 50000000);
			player.inventory.remove(21813, 1);
			break;
		case 7775:
			player.message("To use these points, speak to Davey the thot");
			break;
		case 21810:
			player.pkPoints += 20;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.add(995, 35000000);
			player.inventory.remove(21810, 1);
			break;
		case 21807:
			player.pkPoints += 15;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.add(995, 25000000);
			player.inventory.remove(21807, 1);
			break;
			
		case 620:
			player.inventory.remove(620, 1);
			player.setRight(PlayerRight.DONATOR);
			player.donation.setCredits(100);
			player.donation.setSpent(10);
            player.message("@red@ Relog for your affects to take place & rank to update!");
			break;
			
		case 455:
			new ScratchCard(player).display();
			break;
		case 12746:
			player.pkPoints += 23;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12746, 1);
			break;
		case 10028:
			if (player.inventory.getFreeSlots() <= 3) {
				player.message("You do not have enough inventory space to open this box!");
				return false;
			}
			player.inventory.remove(10028, 1);

			if (Utility.random(1, 5) <= 4) {
				player.inventory.add(995, 5000000);
				player.inventory.add(17163, 1);
				player.inventory.add(17164, 1);
				player.inventory.add(17165, 1);
				player.inventory.add(3273, 1);
				player.message("@gre@You were lucky and received 5 Million GP! & RebelionX Set!");
				return false;
			}
			if (Utility.random(1, 10) <= 2) {
				player.inventory.add(995, 15000000);
				player.inventory.add(3273, 1);
				player.message("@blu@You were lucky and received 15 Million GP! & Ice Katana!");
				return false;
			}
			if (Utility.random(1, 500) <= 2) {
				player.inventory.add(995, 20000000);
				player.inventory.add(17160, 1);
				player.inventory.add(15300, 1);
				player.inventory.add(13686, 1);
				player.inventory.add(15308, 1);
				player.inventory.add(15301, 1);
				player.message("@red@You were EXTREMELY lucky and received 20 Million GP!");
				return false;
			}
			player.message("you were unfortunate and did not recieve anything.");
			break;
			
			
		case 12789:
			if (player.inventory.getFreeSlots() <= 5) {
				player.message("You need 5 inventory space to open this box!");
				return false;
			}
			player.inventory.remove(12789, 1);
			
			if (Utility.random(1, 8) <= 2) {
				player.inventory.add(17166, 1);
				player.inventory.add(17167, 1);
				player.inventory.add(17168, 1);
				player.message("@red@A Flame RebelionX Set!");
				return false;
			}
			player.message("you were unfortunate and did not recieve anything.");
			break;

		case 299:
			player.gambling.plantSeed();
			break;

		case 5020:
			if (player.inventory.contains(995, 1147000000)) {
				player.message("You can't claim this ticket, make some room!");
				return false;
			}

			player.inventory.add(995, 1000000000);
			player.message("You have just claimed 1 1Bil Ticket!");
			player.inventory.remove(5020, 1);

			break;
		case 5021:
			if (player.inventory.contains(995, 1647000000)) {
				player.message("You can't claim this ticket, make some room!");
				return false;
			}
			player.inventory.add(995, 500000000);
			player.message("You have just claimed 1 500M Ticket!");
			player.inventory.remove(5021, 1);

			break;
		case 12748:
			player.pkPoints += 5;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12748, 1);
			break;
		case 4079:
			player.animate(1460);
			break;
		
		
		case 10909:
			Raids1MysteryBox box1 = new Raids1MysteryBox();
			box1.execute(player);
			break;
		case 10993:
			Raids2MysteryBox box2 = new Raids2MysteryBox();
			box2.execute(player);
			break;
		case 22126:
			Raids3MysteryBox box3 = new Raids3MysteryBox();
			box3.execute(player);
			break;
		case 22127:
			Raids4MysteryBox box4 = new Raids4MysteryBox();
			box4.execute(player);
			break;
		case 10911:
			Raids5MysteryBox box5 = new Raids5MysteryBox();
			box5.execute(player);
			break;
		case 6199:
			SuperMysteryBox box6 = new SuperMysteryBox();
			box6.execute(player);
			break;
		case 12955:
			UltraMysteryBox box7 = new UltraMysteryBox();
			box7.execute(player);
			break;
		case 11739:
			LegendaryMysteryBox box8 = new LegendaryMysteryBox();
			box8.execute(player);
			break;
		case 11915:
			ZombieBox box9 = new ZombieBox();
			box9.execute(player);
			break;
	
		case 12749:
			player.pkPoints += 7;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12749, 1);
			break;
		case 12750:
			player.pkPoints += 9;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12750, 1);
			break;
		case 12751:
			player.pkPoints += 11;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12751, 1);
			break;
		case 12752:
			player.pkPoints += 15;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12752, 1);
			break;
		case 12753:
			player.pkPoints += 18;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12753, 1);
			break;
		case 12754:
			player.pkPoints += 21;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12754, 1);
			break;
		case 12755:
			player.pkPoints += 23;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12755, 1);
			break;
		case 12756:
			player.pkPoints += 25;
			player.message("<img=2>You now have @red@" + player.getpkPoints() + " PVP Points!");
			player.inventory.remove(12756, 1);
			break;

		case 13441:
			Anglerfish.onAnglerEffect(player, event.getSlot());
			break;

		case 12791:
			player.runePouch.open();
			break;
		case 4155: {
			Slayer slayer = player.slayer;
			SlayerTask task = slayer.getTask();
			player.send(new SendMessage(
					task == null ? "You currently don't have a task, visit Nieve in edgeville to be assigned one."
							: String.format(
									player.slayer.partner == null ? "You're assigned to kill %s; only %d more to go."
											: "You and " + player.slayer.partner.getName()
													+ " are assigned to kill %s; only %d more to go.",
									task.getName(), slayer.getAmount())));
		}
			break;

		case 995:
			player.send(new SendInputAmount("Enter the amount of coins you want to deposit:", 10, input -> player.bankVault.deposit(Integer.parseInt(input))));
			break;

		case 405: {
			int coins = Utility.random(50000, 75000);
			player.inventory.remove(405, 1);
			player.inventory.add(995, coins);
			player.message("You found " + Utility.formatDigits(coins) + " coins inside of the casket!");
			break;
		}
		case 12938:
			if (player.isTeleblocked()) {
				player.message("You are currently under the affects of a teleblock spell and can not teleport!");
				break;
			}

			player.locking.lock();
			player.send(new SendFadeScreen("You are teleporting to Zulrah's shrine...", 1, 3));
			World.schedule(5, () -> {
				player.move(new Position(2268, 3069, 0));
				ZulrahActivity.create(player);
				player.locking.unlock();
			});
			player.inventory.remove(12938);
			break;

		case 10834:

			int coins = 100000000;

			if (player.inventory.contains(995, 2100000000)) {
				player.message("you have max cash in your inventory, please bank your cash before claiming more.");
			} else {
				player.inventory.remove(10834, 1);
				player.inventory.add(995, coins);
				player.message("You have claimed the @gre@100 Mill " + "Coin Bag");
				player.animate(2109);
				player.graphic(1177);
			}
			break;

		case 10835:
			int coins1 = 500000000;
			if (player.inventory.contains(995, 1500000000)) {
				player.message("you have max cash in your inventory, please bank your cash before claiming more.");
			} else {
				player.inventory.remove(10835, 1);
				player.inventory.add(995, coins1);
				player.message("You have claimed the @gre@500 Mill " + "Coin Bag");
				player.animate(2109);
				player.graphic(1177);
			}
			break;

		/*
		 * case 11865: { player.inventory.remove(11865, 1); player.inventory.add(11864,
		 * 1); player.message("You have disassembled the slayer helmet."); break; } case
		 * 19639: { player.inventory.remove(19639, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; } case
		 * 19641: { player.inventory.remove(19641, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; } case
		 * 19643: { player.inventory.remove(19643, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; } case
		 * 19645: { player.inventory.remove(19645, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; } case
		 * 19647: { player.inventory.remove(19647, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; } case
		 * 19649: { player.inventory.remove(19649, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; } case
		 * 21264: { player.inventory.remove(21264, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; } case
		 * 21266: { player.inventory.remove(19639, 1); player.inventory.add(11864, 1);
		 * player.message("You have disassembled the slayer helmet."); break; }
		 */
		case 21034:
			if (player.unlockedPrayers.contains(Prayer.RIGOUR)) {
				player.dialogueFactory.sendStatement("You already have this prayer unlocked!").execute();
				return true;
			}

			player.inventory.remove(event.getItem());
			player.unlockedPrayers.add(Prayer.RIGOUR);
			player.dialogueFactory.sendStatement("You have learned the Rigour prayer!").execute();
			break;

		case 21079:
			if (player.unlockedPrayers.contains(Prayer.AUGURY)) {
				player.dialogueFactory.sendStatement("You already have this prayer unlocked!").execute();
				return true;
			}

			player.inventory.remove(event.getItem());
			player.unlockedPrayers.add(Prayer.AUGURY);
			player.dialogueFactory.sendStatement("You have learned the Augury prayer!").execute();
			break;

		case 2528:
			player.send(new SendString("Genie's Experience Lamp", 2810));
			player.send(new SendString("", 2831));
			player.interfaceManager.open(2808);
			break;

		/* Spade */
		case 952:
			player.action.execute(new SpadeAction(player), true);
			break;

		/* Looting bag. */
		case 11941:
			player.lootingBag.open();
			break;
			
		case 1856:
			PlayerGuideHandler guide = new PlayerGuideHandler();
			guide.open(player);
			break;

		/* Birds nest. */
		case 5070:
		case 5071:
		case 5072:
		case 5073:
		case 5074:
			BirdsNest.search(player, event.getItem().getId());
			break;

		default:
			return false;

		}
		return true;
	}

}
