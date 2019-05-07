package io.server.game.world.entity.mob.npc.drop;

import io.server.game.world.items.Item;
import io.server.util.Ran;
import io.server.util.RandomGen;

/**
 * The class which represents a single npc drop.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-1-2017. hi
 */
public final class NpcDrop implements Comparable<NpcDrop> {
	/** The item id of this drop. */
	public int item;

	/** The chance this item is dropped. */
	public NpcDropChance type;

	/** The alternate chance modifier to use over the {@code chance} rarity. */
	public int chance;

	/** The minimum amount of this drop. */
	public final int minimum;

	/** The maximum amount of this drop. */
	public final int maximum;
	
	public int cumulativeWeight;
	
	/** Constructs a new {@link NpcDrop}. */
	public NpcDrop(int item, NpcDropChance type, int chance, int minimum, int maximum) {
		this.item = item;
		this.type = type;
		this.chance = chance;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	/** Converts this {@link NpcDrop} to an item. */
	public Item toItem() {
		return new Item(item, minimum + Ran.gen.nextInt(maximum + 1));
	}

	public void setItem(int item) {
		this.item = item;
	}

	public void setType(NpcDropChance type) {
		this.type = type;
	}

	/** Determines if this chance will be successful or not. */
	public boolean successful(RandomGen random) {
		int numerator = 1;
		int denominator = chance == 0 ? type.denominator : chance;
		return (random.inclusive(numerator, denominator)) % numerator == 0;
	}

	@Override
	public int compareTo(NpcDrop other) {
		return chance - other.chance;
	}

	public Item toItem(Long rolls) {
		long amount = minimum * rolls;
		int temp = maximum - minimum + 1;
		for (long i = 0; i < rolls; i++) {
			amount += Ran.gen.nextInt(temp);
		}
		if (amount > Integer.MAX_VALUE) {
			return new Item(item, Integer.MAX_VALUE);
		} else {
			return new Item(item, (int) amount);
		}
	}
}
