package io.server.game.world.entity.mob.npc.dropchance;

/**
 * 
 * @author Nerik
 *
 */

public enum DropChanceData {

	
	// TODO ROW I & ELITE TORVA
	RING_OF_WEALTH(2572, 5), 
	SOULSETPIECE(13693, 5), 
	SOULSETPIECE1(13696, 5), 
	SOULSETPIECE2(13695, 5),
	SOULSETPIECE4(13692, 5), 
	ROWI(12785, 12),
	DEATH_KATANA(11642, 18), 
	DEATH_KATANAU(22161, 24),
	MAGMA_AXE(13687, 7), 
	RING_15(21752, 15),
	CAPE_50(9074, 5), 
	KARTH_RING_15(13814, 9), 
	NECK_14(21143, 9), 
	RING_14(21081, 9), 
	Dark1(13710, 5), 
	dark2(13714, 5), 
	dark3(13715, 5), 
	dark4(13805, 5), 
	dark5(13816, 5),
	dark6(13711, 5), 
	CHI_HELM(13703, 4), 
	CHI_BODY(13704, 4), 
	CHI_LEGS(13705, 4), 
	owl_head(34, 3),
	owl_plate(79, 3), 
	owl_legs(80, 3), 
	LAVABOW(13749, 11), 
	LAVABOWU(22162, 15),
	LIME_WHIP_U(21292, 15), 
	LIME_WHIP(21225, 8),
    PET1(2182, 5), 
    PET2(20665, 5), 
    PET3(13322, 5),  
    PET4(20663, 5), 
    PET5(20661, 5),
	SWORD_HEARTS(13708, 2), 
	PET6(20659, 5),  
	PET7(13323, 5), 
	PET8(13320, 5), 
	PET9(13325, 5), 
	PET10(12816, 5),
	TORVA_1(16648, 2), 
	PET11(13181, 5), 
	PET12(12648, 5), 
	PET13(13177, 5),  
	PET14(13179, 5),  
	PET15(12654, 5),
	TORVA_2(16647, 2), 
	PET16(12921, 5), 
	PET17(12939, 5), 
	PET18(12940, 5), 
	PET19(12946, 5), 
	PET20(13178, 5),
	TORVA_3(16649, 2), 
	PET21(12655, 5), 
	PET22(11995, 5), 
	PET23(12643, 5), 
	PET24(12650, 5), 
	PET25(12652, 5),
	PERNIX_1(16650, 2),  
	PET26(12651, 5),  
	PET27(12693, 5),  
	PET28(12653, 5),  
	PET29(21273, 5),
	PERNIX_2(16651, 2), 
	PET30 (20851, 5), 
	PET31(13247, 5), 
	PET32(13247, 5), 
	PET33(20693, 5),
	PERNIX_3(16653, 2), 
	PET34(21992, 5), 
	PET35(21992, 5), 
	PET36(21511, 5), 
	PET37(7420, 5),
	VIRTUS_1(16654, 2), 
	DEATHSETPIECE(17157, 5), 
	DEATHSETPIECE1(17162, 5), 
	DEATHSETPIECE2(17159, 5),
	DEATHSETPIECE4(17158, 2),
	VIRTUS_2(16655, 2),
	VIRTUS_3(16656, 2),    	
	ELDER_1(13824, 3),
	ELDER_2(13825, 3),
	ELDER_3(13826, 3),

	ZAROS_1(22301, 2),
	ZAROS_2(22304, 2), 
	ZAROS_3(22307, 2),
	ZAROS_4(15011, 2),
	;

	private int itemId;
	private int modifier;

	DropChanceData(int itemId, int modifier) {
		this.itemId = itemId;
		this.modifier = modifier;
	}

	public int getItemId() {
		return itemId;
	}

	public int getModifier() {
		return modifier;
	}
	
	public static DropChanceData[] values = DropChanceData.values();
}
