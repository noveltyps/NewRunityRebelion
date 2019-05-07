package io.server.content.worldevent;

import java.util.ArrayList;

/**
 * @author Austin X (Rune-Server)
 */

public enum WorldEventType {
	PKP("Double PK Points"),
	VOTE("Vote Rewards"),
	DONATOR_POINTS("Donator Points"),
	EXP("Double Experience"),
	DROP_RATE("Double Drop Rate"),
	DROP_RATE_BOOST("Drop Rate Boost"),
	PEST_CONTROL("Pest Control"),
	AVO("Double AVO Tickets"),
	BLACK_FRIDAY("Black Friday", DONATOR_POINTS),
	XMAS("X-Mas", new WorldEventType[] {DONATOR_POINTS, EXP, PEST_CONTROL})
	;

	WorldEventType[] types;

	String name;

	WorldEventType(String name) {
		this.name = name;
	}
	
	WorldEventType(String name, WorldEventType[] types) {
		this.name = name;
		this.types = types;
	}
	
	WorldEventType(String name, WorldEventType type) {
		this.name = name;
		this.types = new WorldEventType[] {type};
	}
	
	public String getName() {
		return name;
	}
	
	public static ArrayList<WorldEventType> getTypes(WorldEventType type) {
		ArrayList<WorldEventType> types = new ArrayList<WorldEventType>();
		types.add(type);
		if (type.types != null && type.types.length > 0)
			for (WorldEventType t : type.types)
				types.add(t);
		return types;
	}
	
}