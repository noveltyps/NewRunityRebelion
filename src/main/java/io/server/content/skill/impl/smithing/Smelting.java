package io.server.content.skill.impl.smithing;

import java.util.Optional;

import io.server.Config;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.object.GameObject;
import io.server.net.packet.out.SendChatBoxInterface;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendItemModelOnInterface;

public final class Smelting extends Action<Player> {
	/**
	 * The array which holds all the possible furnace ids a player can smelt his
	 * bars in.
	 */
	private static final int[] FURNACE_IDS = { 16469, 3994 };

	/** The array which holds all the frames you can draw an item on. */
	private static final int[] SMELT_FRAME = { 2405, 2406, 2407, 2409, 2410, 2411, 2412, 2413 };

	/** The bar identification to be drawn on each frame. */
	private static final int[] SMELT_BARS = { 2349, 2351, 2355, 2353, 2357, 2359, 2361, 2363 };

	/** The definition of the bar we're creating. */
	private final SmeltingData definition;

	/** Determines if we're smelting dependant on the superheat spell. */
	private final boolean spell;

	/** The amount we're producing. */
	private int amount;

	/** Constructs a new {@link Smelting}. */
	private Smelting(Player player, SmeltingData definition, int amount, boolean spell) {
		super(player, 2, false);
		this.definition = definition;
		this.amount = amount;
		this.spell = spell;
	}

	/** Attempts to start smelting for the specified {@code player}. */
	static boolean smelt(Player player, int buttonId) {
		Optional<SmeltingData> data = SmeltingData.getDefinition(buttonId);

		if (!data.isPresent()) {
			return false;
		}

		if (data.get().amount == -1) {
			player.send(new SendInputAmount("How many you would like to melt?", 2,
					s -> Smelting.smelt(player, data.get(), Integer.parseInt(s))));
			return true;
		}

		smelt(player, data.get(), data.get().amount);
		return true;
	}

	/**
	 * Smelts the {@code data} for the specified {@code player} and produces the
	 * exact amount the player inputed if he has the requirements.
	 */
	private static void smelt(Player player, SmeltingData data, int amount) {
		player.interfaceManager.close();
		player.action.execute(new Smelting(player, data, amount, false));
		player.skills.get(Skill.SMITHING).setDoingSkill(true);
	}

	/** Opens the smelting itemcontainer for the {@code player}. */
	static boolean openInterface(Player player, GameObject object) {
		boolean accessible = false;

		for (int id : FURNACE_IDS) {
			if (object.getId() == id) {
				accessible = true;
				break;
			}
		}

		if (!accessible) {
			return false;
		}

		player.send(new SendChatBoxInterface(2400));
		return true;
	}

	/** Sends the items on the smelting itemcontainer. */
	public static void clearInterfaces(Player player) {
		for (int j = 0; j < SMELT_FRAME.length; j++) {
			player.send(new SendItemModelOnInterface(SMELT_FRAME[j], 150, SMELT_BARS[j]));
		}
	}

	/** Checks if the player has the requirements to smelt. */
	private boolean smelt() {
		if (getMob().skills.get(Skill.SMITHING).getLevel() < definition.requirement) {
			getMob().message("You need a smithing level of " + definition.requirement + " to smelt this bar.");
			return false;
		}

		if (!getMob().inventory.containsAll(definition.required)) {
			getMob().message("You don't have the required items to smelt this bar.");
			return false;
		}

		if (!spell) {
			getMob().animate(new Animation(899));
		}

//        System.out.println(amount);
		getMob().inventory.removeAll(definition.required);
		getMob().inventory.addAll(definition.produced);
		getMob().skills.addExperience(Skill.SMITHING, (definition.experience * Config.SMITHING_MODIFICATION * 1.05)
				* new ExperienceModifier(getMob()).getModifier());
		amount--;

		if (amount < 1) {
			this.cancel();
		}

		return true;
	}

	@Override
	protected boolean canSchedule() {
		return !getMob().skills.get(Skill.SMITHING).isDoingSkill();
	}

	@Override
	protected void onSchedule() {
	}

	@Override
	public void execute() {
		if (!getMob().skills.get(Skill.SMITHING).isDoingSkill()) {
			cancel();
			return;
		}

		if (!smelt()) {
			cancel();
		}
	}

	@Override
	protected void onCancel(boolean logout) {
		getMob().resetFace();
		getMob().skills.get(Skill.SMITHING).setDoingSkill(false);
	}

	@Override
	public boolean prioritized() {
		return false;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

	@Override
	public String getName() {
		return "smelting-action";
	}
}
