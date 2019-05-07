package io.server.game.world.entity.mob.npc.drop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.server.content.CrystalChest;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.dropchance.DropChanceHandler;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomGen;
import io.server.util.Utility;

/**
 * The manager class which holds the static entries of drop tables and has a
 * method to execute a drop table from the specified npc.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-1-2017.
 */
public final class NpcDropManager {

	/** The collection of npc ids by their representative drop tables. */
	public static final Map<Integer, NpcDropTable> NPC_DROPS = new HashMap<>();

	/** Alternative drop positions for mobs (i.e kraken, zulrah) */
	private static final int[] ALTERNATIVE_POSITION = { 2044, 2043, 2042, 494 };

	/** Attempts to drop the drop table which belongs to {@code npc#id}. */
	public static void drop(Player killer, Npc npc) {
		if (killer == null) {
			return;
		}

		if (npc == null) {
			return;
		}

		NpcDropTable table = NPC_DROPS.get(npc.id);

		if (table == null) {
			return;
		}

		RandomGen gen = new RandomGen();
		int rate = DropChanceHandler.getRate(killer);
		Map<Integer, Long> npc_drops = new HashMap<Integer, Long>();
		table.generate(killer, npc_drops, rate);
		Position dropPosition = npc.getPosition().copy();
		/*
		 * if(killer.equipment.contains(1995)) {
		 * killer.message("Your loot is auto picked up"); } else {
		 * killer.message("I'm a moron and did something stupid. FK"); }
		 */
		// special drop positions
		if (checkAlternativePosition(npc.id)) {
			dropPosition = killer.getPosition().copy();
		}

		// crystal key drop
		if (npc.getMaximumHealth() > 50 && Utility.random(150) <= 5) {
			Item crystal_key = Utility.randomElement(CrystalChest.KEY_HALVES);
			GroundItem.create(killer, crystal_key, dropPosition);
			killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + crystal_key.getName()));
		}

		// casket drop
		if (npc.getMaximumHealth() > 60 && Utility.random(1, 200) <= 2) {
			Item casket = new Item(405);
			GroundItem.create(killer, casket, dropPosition);
			killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + casket.getName()));
		}

		// starter box drop
		if (npc.getMaximumHealth() > 60 && Utility.random(1, 1000) <= 5) {
			Item starterbox = new Item(10028);
			GroundItem.create(killer, starterbox, dropPosition);
			killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + starterbox.getName()));
		}
		
		// starter box drop
		if (npc.getMaximumHealth() > 5 && Utility.random(1, 1200) <= 5) {
			Item raptor = new Item(12789);
			GroundItem.create(killer, raptor, dropPosition);
			killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + raptor.getName()));
		}

		// drop table
		for (Entry<Integer, Long> entry : npc_drops.entrySet()) {
			NpcDrop drop = table.drops[entry.getKey()];
			Item item = drop.toItem(entry.getValue());

			/**
			 * sell this on the store
			 */
			if (killer.equipment.contains(10557)) {
				killer.message("@or2@[AUTO LOOT] Your Loot has been added to your inventory.");
				killer.inventory.add(new Item(item.getId(), item.getAmount()));
				if (drop.type.equals(NpcDropChance.RARE) || drop.type.equals(NpcDropChance.ULTRA_RARE)) {
					killer.message("@or2@[AUTO LOOT] ["+drop.type+"] Your Loot Was: " + ItemDefinition.get(item.getId()).getName());
				}
				continue;
			}

			if (item.getId() == 11941 && killer.playerAssistant.contains(item)) { // looting bag
				killer.message("You have missed out on " + Utility.getAOrAn(item.getName()) + " " + item.getName()
						+ " since you already have on your account.");
				continue;
			}
			if (item.getId() == 2677 && killer.playerAssistant.contains(item)) { // easy clue
				killer.message("You have missed out on " + Utility.getAOrAn(item.getName()) + " " + item.getName()
						+ " since you already have on your account.");
				continue;
			}
			if (item.getId() == 2801 && killer.playerAssistant.contains(item)) { // medium clue
				killer.message("You have missed out on " + Utility.getAOrAn(item.getName()) + " " + item.getName()
						+ " since you already have on your account.");
				continue;
			}
			if (item.getId() == 2722 && killer.playerAssistant.contains(item)) {// hard clue
				killer.message("You have missed out on " + Utility.getAOrAn(item.getName()) + " " + item.getName()
						+ " since you already have on your account.");
				continue;
			}
			if (item.getId() == 12073 && killer.playerAssistant.contains(item)) {// elite clue
				killer.message("You have missed out on " + Utility.getAOrAn(item.getName()) + " " + item.getName()
						+ " since you already have on your account.");
				continue;
			}
//
//            if (killer.clanChannel != null && killer.clanChannel.lootshareEnabled()) {
//                killer.forClan(channel -> channel.splitLoot(killer, npc, item));
//                return;
//            }

			if (killer.settings.dropNotification && item.getValue() > 250000) {
				World.sendDropMessage(killer, item, npc.getName());
				
				/*
				String name = item.getName();
				killer.send(new SendMessage("<col=BA383E>Rare Drop Notification: </col>" + name + " ("
						+ Utility.formatDigits(item.getValue()) + " coins)"));*/
			} else if (killer.settings.untradeableNotification && !item.isTradeable()) {
				killer.send(new SendMessage("<col=F5424B>Untradeable Drop Notification: </col>" + item.getName()));
			}
			
			if (!item.isStackable()) {
				Item single = item.createWithAmount(1);
				for (int i = 0; i < item.getAmount(); i++)
					GroundItem.create(killer, single, dropPosition);
			} else {
				GroundItem.create(killer, item, dropPosition);
			}
		}
	}

	/*
	 *  if(Config.DOUBLE_DROPS == true) {
				  GroundItem.create(killer, item, dropPosition);
				  GroundItem.create(killer, item, dropPosition);
	              killer.getPlayer().message("[DAILY EVENTS] @red@You've recieved double drops.");
	              return;
				}
	 */
	private static boolean checkAlternativePosition(int npc) {
		for (int alternative : ALTERNATIVE_POSITION) {
			if (alternative == npc)
				return true;
		}
		return false;
	}
}