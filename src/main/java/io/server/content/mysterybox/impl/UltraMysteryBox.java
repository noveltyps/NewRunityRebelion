package io.server.content.mysterybox.impl;

import java.util.Random;

import io.server.content.mysterybox.MysteryBoxListener;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.util.Utility;

public class UltraMysteryBox implements MysteryBoxListener {

	@Override
	public Item[] getCommon() {
		return new Item[] { new Item(995, Utility.random(2500000, 3500000)), 
				new Item(12926, 1), // Toxic blowpipe
				new Item(11926, 1), // odium ward
				new Item(12691, 1), // tyrannical ring (i)
				new Item(12692, 1), // treasonousnring (i)
				new Item(13202, 1), // ring of gods (i)
				new Item(11791, 1), // SOTD
				new Item(19710, 1), // ring of suffering (i)
				new Item(20595, 1), // elder chaos hood
				new Item(20517, 1), // elder chaos top
				new Item(20520, 1), // elder chaos top
				new Item(21298, 1), // obsidian helm
				new Item(21301, 1), // obsid body
				new Item(21304, 1), // obsid legs
				new Item(20520, 1) // elder chaos robe

			};
		}


	@Override
	public Item[] getUncommon() {
		return new Item[] { new Item(995, Utility.random(2500000, 3500000)),
				new Item(19553, 1), // Amulet of torture
				new Item(11785, 1), // Arma crossbow
		};
	}

	@Override
	public Item[] getRare() {
		return new Item[] { new Item(995, Utility.random(3500000, 5000000)), new Item(12889, 1), // santa pantaloons
				new Item(13175, 1), // h'ween set
				new Item(21000, 1), // twisted buckler
				new Item(21006, 1), // kodai wand
				new Item(21003, 1), // elder maul
				new Item(21015, 1), // Dinh bulwark
				new Item(20997, 1), // twisted bow
				new Item(12817, 1), // ely ss
				new Item(12821, 1), // spectral ss
				new Item(12825, 1), // arcane ss
				new Item(12819, 1), // ely sigil
				new Item(12823, 1), // spectral sigil
				new Item(12827, 1), // arcane sigil
				new Item(11847, 1), // black h'ween mask
				new Item(10556, 1), // attacker icon
				new Item(10557, 1), // collector icon
				new Item(10558, 1), // defender icon
		};
	}

	@Override
	public Item[] getUltra() {
		return new Item[] { new Item(995, Utility.random(5000000, 50000000)), new Item(13173, 1), // phat set
				new Item(13175, 1), // h'ween set
				new Item(21000, 1), // twisted buckler
				new Item(21006, 1), // kodai wand
				new Item(21003, 1), // elder maul
				new Item(21015, 1), // Dinh bulwark
				new Item(20997, 1), // twisted bow
				new Item(12817, 1), // ely ss
				new Item(12821, 1), // spectral ss
				new Item(12825, 1), // arcane ss
				new Item(12819, 1), // ely sigil
				new Item(12823, 1), // spectral sigil
				new Item(12827, 1), // arcane sigil
				new Item(1057, 1), // red h'ween
				new Item(1053, 1), // green h'ween
				new Item(13235, 1), // eternal boots
				new Item(13237, 1), // peg boots
				new Item(13239, 1), // prim boots
				new Item(13652, 1), // Dragon Claws
				new Item(11785, 1), // Arma crossbow
				new Item(12926, 1), // Toxic blowpipe
				new Item(21079, 1), // arcane prayer scroll
				new Item(21034, 1), // dexterous prayer scroll
				
				new Item(21225, 1), // black h'ween mask
				new Item(21292, 1), // black h'ween mask

				new Item(11847, 1), // black h'ween mask
				new Item(1048, 1), // white phat
				new Item(1040, 1), // yellow phat
				new Item(1042, 1), // blue phat
				new Item(1044, 1), // green phat
				new Item(1046, 1), // purple phat

				new Item(1038, 1), // red phat
				new Item(11862, 1), // black phat
				new Item(11863, 1), // rainbow phat
				new Item(12399, 1), // phat and specs

		};
	}

	@Override
	public void execute(Player player) {
		Random random = new Random();
		player.inventory.remove(11739, 1);
		/**
		 * Utility.random(1, 150) <-- This generates a RANDOM number between 1 and 150.
		 * Utility.random(1, 250) <= 10 <---- This generates a RANDOM NUMBER between 1
		 * and 150 and if the RANDOM NUMBER is equal to 10 then it will execute.
		 */
		if (Utility.random(1, 50) <= 12) {
			player.inventory.add(getUncommon()[random.nextInt(getUncommon().length)]);
			player.message("You have recieved a Uncommon loot!");
		}
		if (Utility.random(1, 350) <= 4) {
			player.inventory.add(getRare()[random.nextInt(getRare().length)]);
			player.message("@gre@You have recieved a Rare loot!");
			World.sendMessage(player.getName() + " @red@Has received RARE LOOT!");
		}
		//TODO Make Supreme rank have beenfits
		if (Utility.random(1, 550) <= 6) {
			player.inventory.add(getUltra()[random.nextInt(getUltra().length)]);
			player.message("You have recieved a ULTRA RARE LOOT!");
			World.sendMessage(player.getName() + " @red@Has received ULTRA RARE LOOT!");
		} else {
			player.inventory.add(getCommon()[random.nextInt(getCommon().length)]);
		}
		player.setBossPoints(player.getBossPoints() + 10);
        player.message("You have recieved 10 Boss points!" + " <img=2>You now have @red@ " + player.getBossPoints() + " boss points");
	}

}