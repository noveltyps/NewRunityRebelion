package io.server.content.mysterybox.impl;

import io.server.content.mysterybox2.MysteryBox;
import io.server.game.world.items.Item;

/**
 *  All Vs One Box
 * @author Adam_#6723
 */
public class DoubleThreat extends MysteryBox {
    @Override
    protected String name() {
        return "Double Threat Box";
    }

    @Override
    protected int item() {
        return 6855;
    }

    @Override
    protected int rareValue() {
        return 20_000_000;
    }

    @Override
    protected Item[] rewards() {
        return new Item[]{
                new Item(995, 1000000), // COINS
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(995, 1000000), // COINS
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(11907, 1), // TRIDENT_OF_THE_SEAS
                new Item(13265, 1), // ABYSSAL_DAGGER
                new Item(11791, 1), // STAFF_OF_THE_DEAD
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
                new Item(11826, 1), // ARMADYL_HELMET
                new Item(11828, 1), // ARMADYL_CHESTPLATE
                new Item(11830, 1), // ARMADYL_CHAINSKIRT
                new Item(11785, 1), // ARMADYL_CROSSBOW
                new Item(11791, 1), // STAFF_OF_THE_DEAD
                new Item(11824, 1), // ZAMORAKIAN_SPEAR
                new Item(11832, 1), // BANDOS_CHESTPLATE
                new Item(11834, 1), // BANDOS_TASSETS
                new Item(11826, 1), // ARMADYL_HELMET
                new Item(11828, 1), // ARMADYL_CHESTPLATE
                new Item(11830, 1), // ARMADYL_CHAINSKIRT
                new Item(11785, 1), // ARMADYL_CROSSBOW
                new Item(11826, 1), // ARMADYL_HELMET
                new Item(11828, 1), // ARMADYL_CHESTPLATE
                new Item(11830, 1), // ARMADYL_CHAINSKIRT
                new Item(11785, 1), // ARMADYL_CROSSBOW
                new Item(11838, 1), // SARADOMIN_SWORD
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
				new Item(20035, 1), // Samurai kasa
				new Item(20038, 1), // Samurai shirt
				new Item(20044, 1), // Samurai greaves
				new Item(20041, 1), // Samurai gloves
				new Item(20047, 1), // Samurai boots
				new Item(13576, 1), // Dragon warhammer
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
				new Item(21012, 1), // Dragon hunter cbow
				new Item(11283, 1), // DFS
				new Item(21021, 1), // ancestral top
				new Item(21018, 1), // ancestral hat
				new Item(12002, 1), // occult necklace
				new Item(21024, 1), // ancestral robe bottom
				new Item(11785, 1), // Arma crossbow
				new Item(13202, 1), // ring of gods (i)
				new Item(11889, 1), // zam hasta
				new Item(11791, 1), // SOTD
				new Item(19710, 1), // ring of suffering (i)
				new Item(12926, 1), // Toxic blowpipe
				new Item(11283, 1), // DFS
				new Item(12357, 1), // katana
				new Item(13202, 1), // ring of gods (i)
				new Item(11889, 1), // zam hasta
				new Item(11791, 1), // SOTD
				new Item(19710, 1), // ring of suffering (i)
				new Item(12926, 1), // Toxic blowpipe
				new Item(11283, 1), // DFS
				new Item(12357, 1), // katana
				new Item(21021, 1), // ancestral top
				new Item(21018, 1), // ancestral hat
        };
    }
}
