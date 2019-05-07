package io.server.game.world.entity.mob.npc.drop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.server.game.world.entity.mob.npc.dropchance.DropChanceHandler;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Ran;
import io.server.util.RandomUtils;

/**
 * The class which represents a npc drop table.
 *
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 29-1-2017.
 */
public final class NpcDropTable {

	/** The npc ids that share this drop table. */
	public final int[] npcIds;

	/** Determines if this table has access to the rare drop table. */
	private final boolean rareDropTable;

	/** The cached array of {@link NpcDrop}s. */
	public final NpcDrop[] drops;

	/** Constructs a new {@link NpcDropTable}. */
	public NpcDropTable(int[] npcIds, boolean rareDropTable, NpcDrop[] npcDrops) {
		this.npcIds = npcIds;
		this.rareDropTable = rareDropTable;
		this.drops = npcDrops;
	}

	public void generate(Player player, Map<Integer, Long> items, int rate) {
		
		if (drops == null)
			return;
		
		int temp = rate + 100;
		int numFullDrops = temp / 100;
		int extraDropChance = temp % 100;
		if (Ran.hitPercent(extraDropChance))
			numFullDrops++;
		
		int index = 0;
		while (index < drops.length && drops[index].chance == 0) { // add the always drops
			items.merge(index++, 1L, Long::sum); 
		}
		for (int i = 0; i < numFullDrops; i++) {
			int drop = getRandomWeightItem(index);
			if (drop < 0)
				break;
			items.merge(drop, 1L, Long::sum);
		}
		
		return;
	}
	
	private int getRandomWeightItem(int index) {
		if (index >= drops.length)
			return -1;
		int low = index;
		int high = drops.length - 1;
		int roll = Ran.gen.nextInt(drops[high].cumulativeWeight);
		int mid = -1;
		NpcDrop reward = null;
		while (high != low) {
			mid = (low + high) / 2;
			reward = drops[mid];
			if (roll < reward.cumulativeWeight) {
				high = mid;
			} else {
				low = mid + 1;
			}
		}
		return low;
	}

	public boolean isRareDropTable() {
		return rareDropTable;
	}

}
