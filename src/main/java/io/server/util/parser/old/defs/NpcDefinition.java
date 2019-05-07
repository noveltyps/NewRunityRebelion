package io.server.util.parser.old.defs;

import static io.server.game.world.entity.combat.CombatConstants.EMPTY_BONUSES;
import static io.server.game.world.entity.combat.CombatConstants.EMPTY_SKILLS;

import java.util.Arrays;

import io.server.Config;

public class NpcDefinition {

	/* The array of npc definitions. */
	public static final NpcDefinition[] DEFINITIONS = new NpcDefinition[Config.NPC_DEFINITION_LIMIT + 1];

	public final int id;
	public final String name;
	public int size;
	public int combatLevel;
	public boolean attackable;
	public int stand;
	public int walk;
	public int turn180;
	public int turn90CW;
	public int turn90CCW;

	/* NEWER SHIT */

	public int attackAnim;
	public int blockAnim;
	public int deathAnim;
	public boolean aggressive;
	public boolean attackNpc;
	public boolean poisonous;
	public int[] skills;
	public int[] bonuses;
	public int attackDelay;

	public NpcDefinition(int id, String name) {
		this.id = id;
		this.name = name;
		this.size = 1;
		this.combatLevel = 0;
		this.attackable = false;
		this.stand = -1;
		this.walk = -1;
		this.turn180 = -1;
		this.turn90CW = -1;
		this.turn90CCW = -1;
		this.attackAnim = -1;
		this.blockAnim = -1;
		this.deathAnim = -1;
		this.aggressive = false;
		this.attackNpc = false;
		this.poisonous = false;
		this.skills = EMPTY_SKILLS;
		this.bonuses = EMPTY_BONUSES;
		this.attackDelay = 4;
	}

	public static NpcDefinition get(int id) {
		if (id > Config.NPC_DEFINITION_LIMIT)
			return DEFINITIONS[Config.NPC_DEFINITION_LIMIT];

		if (id < 0)
			id = 0;

		if (DEFINITIONS[id] == null)
			return new NpcDefinition(id, "null");

		return DEFINITIONS[id];
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof NpcDefinition))
			return false;

		NpcDefinition other = (NpcDefinition) obj;

		if (other.id != id)
			return false;

		if (!name.equals(other.name))
			return false;

		if (other.size != size)
			return false;

		if (other.combatLevel != combatLevel)
			return false;

		if (other.attackable != attackable)
			return false;

		if (other.stand != stand)
			return false;

		if (other.walk != walk)
			return false;

		if (other.turn180 != turn180)
			return false;

		if (other.turn90CW != turn90CW)
			return false;

		if (other.turn90CCW != turn90CCW)
			return false;

		if (other.attackAnim != attackAnim)
			return false;

		if (other.blockAnim != blockAnim)
			return false;

		if (other.deathAnim != deathAnim)
			return false;

		if (other.aggressive != aggressive)
			return false;

		if (other.attackNpc != attackNpc)
			return false;

		if (other.poisonous != poisonous)
			return false;

		if (!Arrays.equals(other.skills, skills))
			return false;

		if (!Arrays.equals(other.bonuses, bonuses))
			return false;

		return other.attackDelay == attackDelay;
	}

}
