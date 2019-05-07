package io.server.game.world.entity.mob.npc.definition;

import static io.server.game.world.entity.combat.CombatConstants.BONUS_CONFIG_FIELD_NAMES;
import static io.server.game.world.entity.combat.CombatConstants.EMPTY_BONUSES;
import static io.server.game.world.entity.combat.CombatConstants.EMPTY_SKILLS;
import static io.server.game.world.entity.combat.CombatConstants.SKILL_FIELD_NAMES;

import java.util.Objects;
import java.util.Optional;

import com.google.gson.JsonObject;

import io.server.Config;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.projectile.CombatProjectile;
import io.server.util.parser.GsonParser;

/**
 * Contains the npc definitions.
 *
 * @author Michael | Chex
 */
public class NpcDefinition {

	/** The array of npc definitions. */
	public static final NpcDefinition[] DEFINITIONS = new NpcDefinition[Config.NPC_DEFINITION_LIMIT + 1];

	private final int id;
	private final String name;
	private int combatLevel;
	private int size;
	private int stand;
	private int walk;
	private int turn180;
	private int turn90CW;
	private int turn90CCW;
	private int attackAnim;
	private int blockAnim;
	private int deathAnim;
	private int deathTimer;
	private boolean attackable;
	private boolean aggressive;
	private boolean retaliate;
	private boolean poisonous;
	private boolean poisonImmunity;
	private boolean venomImmunity;
	private boolean flying;
	private int[] skills;
	private int[] bonuses;
	private int attackDelay;
	private int attackRadius;
	private int respawnTime;
	private CombatAttackData combatAttackData;

	public NpcDefinition(int id, String name) {
		this.id = id;
		this.name = name;
		this.combatLevel = 0;
		this.size = 1;
		this.stand = -1;
		this.walk = -1;
		this.turn180 = -1;
		this.turn90CW = -1;
		this.turn90CCW = -1;
		this.attackAnim = -1;
		this.blockAnim = -1;
		this.deathAnim = -1;
		this.deathTimer = 3;
		this.flying = false;
		this.attackable = false;
		this.aggressive = false;
		this.retaliate = false;
		this.poisonous = false;
		this.poisonImmunity = false;
		this.venomImmunity = false;
		this.skills = EMPTY_SKILLS;
		this.bonuses = EMPTY_BONUSES;
		this.attackDelay = 4;
		this.attackRadius = 1;
		this.respawnTime = 30;
	}

	public static GsonParser createParser() {
		return new GsonParser("def/npc/npc_definitions", false) {

			@Override
			protected void parse(JsonObject data) {
				int id = data.get("id").getAsInt();
				String name = data.get("name").getAsString();

				NpcDefinition definition = new NpcDefinition(id, name);

				if (data.has("size")) {
					definition.size = data.get("size").getAsInt();
				}

				if (data.has("combat-level")) {
					definition.combatLevel = data.get("combat-level").getAsInt();
				}

				if (data.has("stand")) {
					definition.stand = data.get("stand").getAsInt();
				}

				if (data.has("walk")) {
					definition.walk = data.get("walk").getAsInt();
				}

				definition.turn180 = definition.walk;
				if (data.has("turn180")) {
					definition.turn180 = data.get("turn180").getAsInt();
				}

				definition.turn90CW = definition.walk;
				if (data.has("turn90CW")) {
					definition.turn90CW = data.get("turn90CW").getAsInt();
				}

				definition.deathTimer = 3;
				if (data.has("death-timer")) {
					definition.deathTimer = data.get("death-timer").getAsInt();
				}

				definition.turn90CCW = definition.walk;
				if (data.has("turn90CCW")) {
					definition.turn90CCW = data.get("turn90CCW").getAsInt();
				}

				if (data.has("attack-animation")) {
					definition.attackAnim = data.get("attack-animation").getAsInt();
				}

				if (data.has("block-animation")) {
					definition.blockAnim = data.get("block-animation").getAsInt();
				}

				if (data.has("death-animation")) {
					definition.deathAnim = data.get("death-animation").getAsInt();
				}

				if (data.has("attackable")) {
					definition.attackable = data.get("attackable").getAsBoolean();
				}

				if (data.has("aggressive")) {
					definition.aggressive = data.get("aggressive").getAsBoolean();
				}

				definition.retaliate = definition.isAttackable();
				if (data.has("retaliate")) {
					definition.retaliate = data.get("retaliate").getAsBoolean();
				}

				if (data.has("poisonous")) {
					definition.poisonous = data.get("poisonous").getAsBoolean();
				}

				if (data.has("flying")) {
					definition.flying = data.get("flying").getAsBoolean();
				}

				if (data.has("poison-immunity")) {
					definition.poisonImmunity = data.get("poison-immunity").getAsBoolean();
				}

				if (data.has("venom-immunity")) {
					definition.venomImmunity = data.get("venom-immunity").getAsBoolean();
				}

				if (data.has("attack-cooldown")) {
					definition.attackDelay = data.get("attack-cooldown").getAsInt();
				}

				if (data.has("attack-radius")) {
					definition.attackRadius = data.get("attack-radius").getAsInt();
				}

				if (data.has("combat-type") && data.has("projectile-key")) {
					CombatType type = CombatType.valueOf(data.get("combat-type").getAsString());
					String key = data.get("projectile-key").getAsString();
					definition.combatAttackData = new CombatAttackData(type, key);
				}

				if (data.has("respawn")) {
					definition.respawnTime = data.get("respawn").getAsInt();
				}

				for (int index = 0; index < SKILL_FIELD_NAMES.length; index++) {
					String skillName = SKILL_FIELD_NAMES[index];
					if (data.has(skillName)) {
						if (definition.skills == EMPTY_SKILLS) {
							definition.skills = new int[EMPTY_SKILLS.length];
						}
						definition.skills[index] = data.get(skillName).getAsInt();
					}
				}

				for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
					String bonusName = BONUS_CONFIG_FIELD_NAMES[index];
					if (data.has(bonusName)) {
						if (definition.bonuses == EMPTY_BONUSES) {
							definition.bonuses = new int[EMPTY_BONUSES.length];
						}
						definition.bonuses[index] = data.get(bonusName).getAsInt();
					}
				}

				DEFINITIONS[id] = definition;
			}
		};
	}

	/**
	 * Gets a npc definition from the definition array.
	 *
	 * @param id the id of the npc
	 * @return the npc definition
	 */
	public static NpcDefinition get(int id) {
		if (id > Config.NPC_DEFINITION_LIMIT)
			return DEFINITIONS[Config.NPC_DEFINITION_LIMIT];

		if (id < 0)
			id = 0;

		if (DEFINITIONS[id] == null)
			return new NpcDefinition(id, "null");

		return DEFINITIONS[id];
	}

	/** @return the id. */
	public int getId() {
		return id;
	}

	/** @return the name. */
	public String getName() {
		return name;
	}

	/** @return the sze. */
	public int getSize() {
		return size;
	}

	/** @return the combat level. */
	public int getCombatLevel() {
		return combatLevel;
	}

	/** @return the stand anim. */
	public int getStand() {
		return stand;
	}

	/** @return the walk anim. */
	public int getWalk() {
		return walk;
	}

	/** @return the turn 180 anim. */
	public int getTurn180() {
		return turn180;
	}

	/** @return the turn 90 CW anim. */
	public int getTurn90CW() {
		return turn90CW;
	}

	/** @return the turn 90 CCW anim. */
	public int getTurn90CCW() {
		return turn90CCW;
	}

	/** @return the attack anim. */
	public int getAttackAnimation() {
		return attackAnim;
	}

	/** @return the block anim. */
	public int getBlockAnimation() {
		return blockAnim;
	}

	/** @return the death anim. */
	public int getDeathAnimation() {
		return deathAnim;
	}

	public int getDeathTimer() {
		return deathTimer;
	}

	/** @return the attackable flag. */
	public boolean isAttackable() {
		return attackable;
	}

	/** @return the aggressive flag. */
	public boolean isAggressive() {
		return aggressive;
	}

	/** @return the retaliate flag. */
	public boolean isRetaliate() {
		return retaliate;
	}

	/** @return the poisonous flag. */
	public boolean isPoisonous() {
		return poisonous;
	}

	/** @return the poison immunity flag. */
	public boolean hasPoisonImmunity() {
		return poisonImmunity;
	}

	/** @return the venom immunity flag. */
	public boolean hasVenomImmunity() {
		return venomImmunity;
	}

	public boolean isFlying() {
		return flying;
	}

	/** @return the attack delay. */
	public int getAttackDelay() {
		return attackDelay;
	}

	/** @return the attack radius. */
	public int getAttackRadius() {
		return attackRadius;
	}

	/** @return the respawn time in ticks. */
	public int getRespawnTime() {
		return respawnTime;
	}

	/** @return the skill level array. */
	public int[] getSkills() {
		return skills;
	}

	/** @return the equipment bonus array. */
	public int[] getBonuses() {
		return bonuses;
	}

	public void setAggressive(boolean aggressive) {
		this.aggressive = aggressive;
	}

	public void setRetaliate(boolean retaliate) {
		this.retaliate = retaliate;
	}

	public void setRespawnTime(int respawnTime) {
		this.respawnTime = respawnTime;
	}

	public Optional<CombatAttackData> getCombatAttackData() {
		return Optional.ofNullable(combatAttackData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, size);
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof NpcDefinition && ((NpcDefinition) obj).id == id);
	}

	public static final class CombatAttackData {

		public final CombatType type;

		public final String key;

		public CombatAttackData(CombatType type, String key) {
			this.type = type;
			this.key = key;
		}

		public CombatProjectile getDefinition() {
			return CombatProjectile.getDefinition(key);
		}
	}
}
