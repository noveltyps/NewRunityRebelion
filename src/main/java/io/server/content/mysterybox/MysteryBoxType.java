package io.server.content.mysterybox;

/**
 * Type of mystery box
 * 
 * @author Nerik#8690
 *
 */
public enum MysteryBoxType {

	ZOMBIEBOX(11915), RAIDS1MYSTERYBOX(10909), RAIDS2MYSTERYBOX(10933), RAIDS3MYSTERYBOX(22126), RAIDS4MYSTERYBOX(22127), RAIDS5MYSTERYBOX(0000), SUPER_MBOX(6199), LEGENDARY_MBOX(12955), ULTRA_MBOX(11739), ALLVSONEBOX(290), MEGA_MBOX(6508), PETMBOX(8038), ALLVSONEV3BOX(6833);

	private int id;

	MysteryBoxType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
