package io.server.content.freeforall;

import io.server.content.skill.impl.magic.Spellbook;
import io.server.game.world.items.Item;

/**
 * Free For All Data Storage
 * 
 * @author hamza
 *
 */
public enum FreeForAllData implements FreeForAllListener {

	DHAROK() {

		@Override
		public String getName() {
			return "Dharok";
		}

		@Override
		public Item[] getEquipment() {
			return new Item[] { new Item(10828), new Item(1712), new Item(4355), new Item(4587), new Item(1127),
					new Item(8850), new Item(1079), new Item(3105), new Item(2250), new Item(7461) };
		}

		@Override
		public Item[] getInventory() {
			return new Item[] { new Item(1127), new Item(4587), new Item(2503), new Item(9185), new Item(1079),
					new Item(8850), new Item(2497), new Item(10498), new Item(391), new Item(1215), new Item(6685),
					new Item(2440), new Item(391, 2), new Item(6685), new Item(2436), new Item(391, 2), new Item(3024),
					new Item(2444), new Item(391, 2), new Item(3024), new Item(555, 800), new Item(391, 2),
					new Item(560, 600), new Item(565, 300), };
		}

		@Override
		public Spellbook getSpellBook() {
			return Spellbook.LUNAR;
		}

	};
}
