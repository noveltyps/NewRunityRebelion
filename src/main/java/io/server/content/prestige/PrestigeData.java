package io.server.content.prestige;

import java.util.Arrays;
import java.util.Optional;

import io.server.game.world.entity.skill.Skill;

/**
 * Holds the prestige data.
 *
 * @author Daniel.
 */
public enum PrestigeData {
	ATTACK("Attack", Skill.ATTACK, -13504, 52010), DEFENCE("Defence", Skill.DEFENCE, -13498, 52012),
	STRENGTH("Strength", Skill.STRENGTH, -13501, 52011), HITPOINTS("Hitpoints", Skill.HITPOINTS, -13483, 52017),
	RANGE("Ranged", Skill.RANGED, -13495, 52013), PRAYER("Prayer", Skill.PRAYER, -13492, 52014),
	MAGIC("Magic", Skill.MAGIC, -13489, 52015), COOKING("Cooking", Skill.COOKING, -13453, 52027),
	WOODCUTTING("Woodcutting", Skill.WOODCUTTING, -13447, 52029),
	FLETCHING("Fletching", Skill.FLETCHING, -13468, 52022), FISHING("Fishing", Skill.FISHING, -13456, 52026),
	FIREMAKING("Firemaking", Skill.FIREMAKING, -13450, 52028), CRAFTING("Crafting", Skill.CRAFTING, -13471, 52021),
	SMITHING("Smithing", Skill.SMITHING, -13459, 52025), MINING("Mining", Skill.MINING, -13462, 52024),
	HERBLORE("Herblore", Skill.HERBLORE, -13477, 52019), AGILITY("Agility", Skill.AGILITY, -13480, 52018),
	THIEVING("Thieving", Skill.THIEVING, -13474, 52020), SLAYER("Slayer", Skill.SLAYER, -13465, 52023),
	FARMING("Farming", Skill.FARMING, -13444, 52030), RUNECRAFTING("Runecraft", Skill.RUNECRAFTING, -13486, 52016),
	HUNTER("Hunter", Skill.HUNTER, -13441, 52031);

	/** The name of the prestige. */
	public final String name;

	/** The skill identification of the prestige. */
	public final int skill;

	/** The button identification of the prestige. */
	public final int button;

	/** The string identification of the prestige. */
	public final int string;

	/**
	 * Constructs a new <code>Prestige</code>.
	 *
	 * @param name   The name of the prestige.
	 * @param skill  The skill of the prestige.
	 * @param button The button of the prestige.
	 * @param string The string of the prestige.
	 */
	PrestigeData(String name, int skill, int button, int string) {
		this.name = name;
		this.skill = skill;
		this.button = button;
		this.string = string;
	}

	/**
	 * Grabs the prestige data based on the button identification.
	 *
	 * @param button The button identification.
	 * @return The prestige data.
	 */
	public static Optional<PrestigeData> forButton(int button) {
		return Arrays.stream(values()).filter(prestige -> prestige.button == button).findFirst();
	}
}
