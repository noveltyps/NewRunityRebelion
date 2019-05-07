package io.server.content.mysterybox.impl;

import io.server.content.mysterybox2.MysteryBox;
import io.server.game.world.items.Item;

/**
 *  All Vs One Box
 * @author Adam_#6723
 */
public class AllVsOneV3Box extends MysteryBox {
    @Override
    protected String name() {
        return "All Vs One V3 Box";
    }

    @Override
    protected int item() {
        return 6833;
    }

    @Override
    protected int rareValue() {
        return 10_000_000;
    }

    @Override
    protected Item[] rewards() {
        return new Item[]{
                new Item(995, 2000000), // COINS
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(11824, 1), // ZAMORAKIAN_SPEAR
                new Item(11832, 1), // BANDOS_CHESTPLATE
                new Item(11834, 1), // BANDOS_TASSETS
                new Item(11826, 1), // ARMADYL_HELMET
                new Item(11828, 1), // ARMADYL_CHESTPLATE
                new Item(11830, 1), // ARMADYL_CHAINSKIRT
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(11824, 1), // ZAMORAKIAN_SPEAR
                new Item(11832, 1), // BANDOS_CHESTPLATE
                new Item(11834, 1), // BANDOS_TASSETS
                new Item(11826, 1), // ARMADYL_HELMET
                new Item(11828, 1), // ARMADYL_CHESTPLATE
                new Item(11830, 1), // ARMADYL_CHAINSKIRT
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(11824, 1), // ZAMORAKIAN_SPEAR
                new Item(11832, 1), // BANDOS_CHESTPLATE
                new Item(11834, 1), // BANDOS_TASSETS
                new Item(11826, 1), // ARMADYL_HELMET
                new Item(11828, 1), // ARMADYL_CHESTPLATE
                new Item(11830, 1), // ARMADYL_CHAINSKIRT
                new Item(11785, 1), // ARMADYL_CROSSBOW
                new Item(11838, 1), // SARADOMIN_SWORD
                new Item(12924, 1), // TOXIC_BLOWPIPE
                new Item(6585, 1), // AMULET_OF_FURY
                new Item(11802, 1), // ARMADYL_GODSWORD
                new Item(11804, 1), // BANDOS_GODSWORD
                new Item(11806, 1), // SARADOMIN_GODSWORD
				new Item(10557, 1), // collector icon
				new Item(21024, 1), // ancestral robe bottom
				new Item(21021, 1), // ancestral top
				new Item(21018, 1), // ancestral hat
                new Item(11808, 1), // ZAMORAK_GODSWORD
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
				new Item(21225, 1),
				new Item(13576, 1), // Dragon warhammer
				new Item(9923, 1), // skeleton leggings
				new Item(1050, 1), // Santa hat
				new Item(12890, 1), // santa gloves
				new Item(13652, 1), // dragon claws
				new Item(12888, 1), // Santa jacket
				new Item(12891, 1), // Santa boots
				new Item(12821, 1), // spectral ss
				new Item(12825, 1), // arcane ss
				new Item(13343, 1), // black santa hat
				new Item(10556, 1), // attacker icon
				new Item(10557, 1), // collector icon
				new Item(10558, 1), // defender icon
				new Item(10559, 1), // healer icon
				new Item(11810, 1), // Arma hilt
				new Item(11812, 1), // Bandos hilt
				new Item(11814, 1), // Sara hilt
				new Item(11816, 1), // Zam hilt
				new Item(13235, 1), // eternal boots
				new Item(13237, 1), // peg boots
				new Item(13239, 1), // prim boots
				new Item(13652, 1), // Dragon Claws
				new Item(11785, 1), // Arma crossbow
				new Item(12926, 1), // Toxic blowpipe
				new Item(13235, 1), // eternal boots
				new Item(13237, 1), // peg boots
				new Item(13239, 1), // prim boots
				new Item(13652, 1), // Dragon Claws
				new Item(11785, 1), // Arma crossbow
				new Item(12926, 1), // Toxic blowpipe
				new Item(21079, 1), // arcane prayer scroll
				new Item(21034, 1), // dexterous prayer scroll
				new Item(1055, 1), // blue h'ween
				new Item(1959, 1), // pumpkin
				new Item(1961, 1), // easter egg
				new Item(10507, 1), // reindeer hat
				new Item(21012, 1), // Dragon hunter cbow
				new Item(19553, 1), // Amulet of torture
				new Item(11283, 1), // DFS
				new Item(21021, 1), // ancestral top
				new Item(21018, 1), // ancestral hat
				new Item(12002, 1), // occult necklace
				new Item(21024, 1), // ancestral robe bottom
				new Item(11785, 1), // Arma crossbow
				new Item(11926, 1), // odium ward
				new Item(12691, 1), // tyrannical ring (i)
				new Item(12692, 1), // treasonousnring (i)
				new Item(13202, 1), // ring of gods (i)
				new Item(12397, 1), // crown
				new Item(11889, 1), // zam hasta
				new Item(11791, 1), // SOTD
				new Item(19710, 1), // ring of suffering (i)
				new Item(12831, 1), // blessed ss
		        new Item(2581, 1), // Robin hood hat
		        new Item(299, 50), // 50x Flower Poker Seeds
				new Item(12926, 1), // Toxic blowpipe
				new Item(11283, 1), // DFS
		        new Item(811, 500), // 500x Rune Dart
		        new Item(11230, 200), // 200x Dragon Dart
		        new Item(7668, 1), // Gadderhammer
		        new Item(6818, 1), // Bow-Sword (Cool skiller weapon)
		        new Item(6199, 1), // Mummy's leg
				new Item(12357, 1), // katana
		        new Item(299, 50), // 50x Flower Poker Seeds
				new Item(12926, 1), // Toxic blowpipe
				new Item(11283, 1), // DFS
		        new Item(811, 500), // 500x Rune Dart
		        new Item(11230, 200), // 200x Dragon Dart
		        new Item(7668, 1), // Gadderhammer
		        new Item(6818, 1), // Bow-Sword (Cool skiller weapon)
		        new Item(6199, 1), // Mummy's leg
				new Item(12357, 1), // katana
				new Item(21021, 1), // ancestral top
				new Item(21018, 1), // ancestral hat
				new Item(12002, 1), // occult necklace
				new Item(6739, 1), // Dragon Axe
				new Item(12797, 1), // Dragon Pickaxe
				new Item(21028, 1), // Dragon Harpoon
				new Item(11771, 1), // Archers ring (i)
				new Item(11773, 1), // Berserker ring (i)
				new Item(11772, 1), // warrior ring (i)
				new Item(4151, 1), // Abyssal Whip
				new Item(6585, 1), // Fury
				new Item(11840, 1), // Dragon boots
				new Item(12881, 1), // Ahrim set
				new Item(12883, 1), // Kail's set
				new Item(12873, 1), // Guthen set
				new Item(12879, 1), // Torag set
				new Item(19484, 500), // 500 Dragon Jav
				new Item(11212, 500), // 500 Dragon Arrows
				new Item(12875, 1), // Verac set
				new Item(4151, 1), // Abyssal Whip
				new Item(6585, 1), // Fury
				new Item(11840, 1), // Dragon boots
				new Item(12881, 1), // Ahrim set
				new Item(12883, 1), // Kail's set
				new Item(12873, 1), // Guthen set
				new Item(12879, 1), // Torag set
				new Item(19484, 500), // 500 Dragon Jav
				new Item(11212, 500), // 500 Dragon Arrows
				new Item(12875, 1), // Verac set

        };
    }
}
