package io.server.game.world.entity.mob.npc.drop;

import io.server.util.Utility;

/**
 * The enumerated type whose elements represent a set of constants used to
 * differ between {@link NpcDrop} rarities.
 * 
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-1-2017.
 */
public enum NpcDropChance {
	ALWAYS(0), 
	COMMON(10), 
	UNCOMMON(30), 
	RARE(600), 
	VERY_RARE(1200), 
	ULTRA_RARE(1800);

	/** The denominator which is the divisor of a nominator of 1. */
	public final int denominator;

	/** Constructs a new {@link NpcDropChance}. */
	NpcDropChance(int denominator) {
		this.denominator = denominator;
	}

	@Override
	public String toString() {
		return Utility.formatEnum(name());
	}
	
	public static final NpcDropChance[] values = NpcDropChance.values();
}
