package io.server.content.mysterybox;

import java.util.HashMap;
import java.util.Map;

import io.server.content.mysterybox.impl.AllVsOneBox;
import io.server.content.mysterybox.impl.LegendaryMysteryBox;
import io.server.content.mysterybox.impl.Raids1MysteryBox;
import io.server.content.mysterybox.impl.Raids2MysteryBox;
import io.server.content.mysterybox.impl.Raids3MysteryBox;
import io.server.content.mysterybox.impl.Raids4MysteryBox;
import io.server.content.mysterybox.impl.Raids5MysteryBox;
import io.server.content.mysterybox.impl.SuperMysteryBox;
import io.server.content.mysterybox.impl.UltraMysteryBox;
import io.server.content.mysterybox.impl.ZombieBox;

/**
 * Mystery Box Event Listener
 * 
 * @author Nerik#8690	
 *
 */
public class MysteryBoxEvent {

	public static final Map<MysteryBoxType, MysteryBoxListener> MYSTERY_BOX = new HashMap<>();

	static {

		MYSTERY_BOX.putIfAbsent(MysteryBoxType.ALLVSONEBOX, new AllVsOneBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.RAIDS1MYSTERYBOX, new Raids1MysteryBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.RAIDS2MYSTERYBOX, new Raids2MysteryBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.RAIDS3MYSTERYBOX, new Raids3MysteryBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.RAIDS4MYSTERYBOX, new Raids4MysteryBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.RAIDS5MYSTERYBOX, new Raids5MysteryBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.ZOMBIEBOX, new ZombieBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.LEGENDARY_MBOX, new LegendaryMysteryBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.ULTRA_MBOX, new UltraMysteryBox());
		MYSTERY_BOX.putIfAbsent(MysteryBoxType.SUPER_MBOX, new SuperMysteryBox());
		
	}
}
