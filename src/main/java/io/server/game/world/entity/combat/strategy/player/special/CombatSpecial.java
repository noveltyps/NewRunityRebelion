package io.server.game.world.entity.combat.strategy.player.special;

import java.util.Arrays;
import java.util.Optional;

import io.server.content.activity.Activity;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.Combat;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.combat.strategy.player.special.melee.AbyssalBludgen;
import io.server.game.world.entity.combat.strategy.player.special.melee.AbyssalDagger;
import io.server.game.world.entity.combat.strategy.player.special.melee.AbyssalTentacleWhip;
import io.server.game.world.entity.combat.strategy.player.special.melee.AbyssalWhip;
import io.server.game.world.entity.combat.strategy.player.special.melee.AnicentMace;
import io.server.game.world.entity.combat.strategy.player.special.melee.ArmadylGodsword;
import io.server.game.world.entity.combat.strategy.player.special.melee.BandosGodsword;
import io.server.game.world.entity.combat.strategy.player.special.melee.DefaultMelee;
import io.server.game.world.entity.combat.strategy.player.special.melee.DinhsBulwark;
import io.server.game.world.entity.combat.strategy.player.special.melee.Dragon2h;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonClaws;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonDagger;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonHaldberd;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonLongsword;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonMace;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonScimitar;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonSpear;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonSword;
import io.server.game.world.entity.combat.strategy.player.special.melee.DragonWarhammer;
import io.server.game.world.entity.combat.strategy.player.special.melee.GraniteMaul;
import io.server.game.world.entity.combat.strategy.player.special.melee.SaradominGodsword;
import io.server.game.world.entity.combat.strategy.player.special.melee.SaradominSword;
import io.server.game.world.entity.combat.strategy.player.special.melee.ScytheOfViturStrategy;
import io.server.game.world.entity.combat.strategy.player.special.melee.ToxicGlaiveSpecialAttack;
import io.server.game.world.entity.combat.strategy.player.special.melee.ZamorakGodsword;
import io.server.game.world.entity.combat.strategy.player.special.range.ArmadylCrossbow;
import io.server.game.world.entity.combat.strategy.player.special.range.Ballista;
import io.server.game.world.entity.combat.strategy.player.special.range.Blowpipe;
import io.server.game.world.entity.combat.strategy.player.special.range.DarkBow;
import io.server.game.world.entity.combat.strategy.player.special.range.DragonThrownaxe;
import io.server.game.world.entity.combat.strategy.player.special.range.MagicShortbow;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendInterfaceLayer;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendMoveComponent;
import io.server.net.packet.out.SendSpecialAmount;
import io.server.net.packet.out.SendString;

/**
 * The enumerated type whose elements represent the combat special attacks.
 *
 * @author Chex
 * @author Daniel
 */
public enum CombatSpecial {
	/* Abyssal **/
	ABYSSAL_WHIP(new int[] { 4151, 21371, 15441, 15442, 15443, 15444 }, 50, AbyssalWhip.get()),
	ABYSSAL_TENTACLE_WHIP(new int[] { 12006 }, 50, AbyssalTentacleWhip.get()),
	ABYSSAL_DAGGER(new int[] { 13265, 13267, 13271, 13269 }, 50, AbyssalDagger.get()),
	ABYSSAL_BLUDGEN(new int[] { 13263 }, 50, AbyssalBludgen.get()),

	/* Dragon **/
	DRAGON_DAGGER(new int[] { 1215, 1231, 5680, 5698 }, 25, DragonDagger.get()),
	DRAGON_WARHAMMER(new int[] { 13576 }, 50, DragonWarhammer.get()),
	DRAGON_LONGSWORD(new int[] { 1305 }, 25, DragonLongsword.get()),
	DRAGON_SCIMITAR(new int[] { 4587 }, 50, DragonScimitar.get()),
	DRAGON_SWORD(new int[] { 21206 }, 40, DragonSword.get()),
	DRAGON_HALBERD(new int[] { 3204 }, 30, DragonHaldberd.get()),
	DRAGON_CLAWS(new int[] { 13652, 20784 }, 50, DragonClaws.get()),
	DRAGON_SPEAR(new int[] { 1249, 1263, 5730, 3176, 5716, 11824, 11889 }, 25, DragonSpear.get()),
	DRAGON_MACE(new int[] { 1434 }, 25, DragonMace.get()), DRAGON_2H(new int[] { 7158 }, 60, Dragon2h.get()),
	ANICENT_MACE(new int[] { 11061 }, 100, AnicentMace.get()),

	DRAGON_BATTLEAXE(new int[] { 1377 }, 100, DefaultMelee.get()) {
		@Override
		public void enable(Player player) {
			if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.send(new SendMessage("You do not have enough special energy left!"));
				return;
			}
			player.speak("Raarrrrrgggggghhhhhhh!");
			player.animate(new Animation(1056, UpdatePriority.HIGH));
			player.graphic(new Graphic(246, UpdatePriority.HIGH));
			player.skills.modifyLevel(level -> level - level / 10, Skill.ATTACK);
			player.skills.modifyLevel(level -> level - level / 10, Skill.DEFENCE);
			player.skills.modifyLevel(level -> level - level / 10, Skill.RANGED);
			player.skills.modifyLevel(level -> level - level / 10, Skill.MAGIC);
			player.skills.modifyLevel(level -> level + 10 + (player.skills.getMaxLevel(Skill.STRENGTH) - level) / 4,
					Skill.STRENGTH, 0, 120);
			player.skills.refresh(Skill.ATTACK);
			player.skills.refresh(Skill.DEFENCE);
			player.skills.refresh(Skill.STRENGTH);
			player.skills.refresh(Skill.RANGED);
			player.skills.refresh(Skill.MAGIC);
			drain(player, 100);
		}
	},

	/* Godsword **/
	SARADOMIN_GODSWORD(new int[] { 11806, 20372 }, 50, SaradominGodsword.get()),
	ARMADYL_GODSWORD(new int[] { 11802, 20368 }, 50, ArmadylGodsword.get()),
	ZAMORAK_GODSWORD(new int[] { 11808, 20374 }, 50, ZamorakGodsword.get()),
	BANDOS_GODSWORD(new int[] { 11804, 20370 }, 50, BandosGodsword.get()),
	SCYTHE_OF_VITUR(new int[] { 22325 }, 30, ScytheOfViturStrategy.get()),

	/* Ranged **/
	BLOW_PIPE(new int[] { 12926 }, 50, Blowpipe.get()),
	DARK_BOW(new int[] { 11235, 12765, 12766, 12767, 12768, 13749, 22162, 13831, 13713}, 55, DarkBow.get()),
	MAGIC_SHORTBOW(new int[] { 861, 12788 }, 55, MagicShortbow.get()),
	BALLISTA(new int[] { 19478, 19481 }, 55, Ballista.get()),
	ARMADYL_CROSSBOW(new int[] { 11785 }, 40, ArmadylCrossbow.get()),
	DRAGON_THROWNAXE(new int[] { 20849 }, 25, DragonThrownaxe.get()) {
		@Override
		public void enable(Player player) {
			super.enable(player);
			if (player.isSpecialActivated()) {
				player.getCombat().setCooldown(0);
			}
		}
	},

	TOXIC_GLAIVE(new int[] { 11063 }, 50, ToxicGlaiveSpecialAttack.get()),

	/* Other **/
	SARADOMIN_SWORD(new int[] { 11838, 12809 }, 100, SaradominSword.get()),
	STAFF_OF_THE_DEAD(new int[] { 11791, 12904 }, 100, DefaultMelee.get()) {
		@Override
		public void enable(Player player) {
			if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.send(new SendMessage("You do not have enough special energy left!"));
				return;
			}
			player.animate(new Animation(7083, UpdatePriority.HIGH));
			player.graphic(new Graphic(1228, 0, 16));
			player.skills.setLevel(1, 125);
			player.send(new SendMessage("Your defence has been boosted!"));
			drain(player, 100);

		}
	},

	INFERNAL_HARPOON(new int[] { 21031, 21028 }, 100, DefaultMelee.get()) {
		@Override
		public void enable(Player player) {
			if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.send(new SendMessage("You do not have enough special energy left!"));
				return;
			}
			player.animate(new Animation(439, UpdatePriority.HIGH));
			player.graphic(new Graphic(255, true, UpdatePriority.HIGH));
			player.send(new SendMessage("Your chances of catching fishes has increased by x2!"));
			player.speak("You plebs ain't got x2 Fishing rate like me ;)");
			drain(player, 100);
		}
	},
	DRAGON_PICKAXE(new int[] { 11920, 20014, 13243 }, 100, DefaultMelee.get()) {
		@Override
		public void enable(Player player) {
			if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.send(new SendMessage("You do not have enough special energy left!"));
				return;
			}
			player.animate(new Animation(7138, UpdatePriority.HIGH));
			player.graphic(new Graphic(246, true, UpdatePriority.HIGH));
			player.send(new SendMessage("Your mining speed has increased by x2!"));
			player.speak("Aye kids, I'm getting x2 Mining speed then you plebs!");
			drain(player, 100);
		}
	},
	DRAGON_AXE(new int[] { 6739, 13241, 20011 }, 100, DefaultMelee.get()) {
		@Override
		public void enable(Player player) {
			if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.send(new SendMessage("You do not have enough special energy left!"));
				return;
			}
			player.animate(new Animation(2876, UpdatePriority.HIGH));
			player.graphic(new Graphic(479, true, UpdatePriority.HIGH));
			player.send(new SendMessage("Your Woodcutting rate has increased by x2!"));
			player.speak("Aye kids, I'm getting x2 Woodcutting speed then you plebs!");
			drain(player, 100);
		}
	},
	GRANITE_MAUL(new int[] { 4153 }, 50, GraniteMaul.get()) {
		@Override
		public void enable(Player player) {
			if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.send(new SendMessage("You do not have enough special energy left!"));
				return;
			}

			player.setSpecialActivated(true);
			player.send(new SendConfig(301, 1));

			Combat<Player> combat = player.getCombat();
			Mob defender = combat.getLastVictim();

			if (combat.isAttacking(defender)) {
				combat.submitStrategy(defender, GraniteMaul.get());
			}
		}

		@Override
		public void disable(Player player, boolean clicked) {
			// TODO: potential bug when specials are disabled other
			// TODO: than in the spec bar, example: switching weapons

			boolean doubleSpec = !player.attributes.has("granite-maul-spec");
			int current = player.getSpecialPercentage().intValue();

			if (clicked && doubleSpec && current >= getAmount()) {
				player.attributes.set("granite-maul-spec", true);
			} else {
				player.attributes.remove("granite-maul-spec");
				super.disable(player, clicked);
			}
		}
	},

	DINHS_BULWARK(new int[] {  }, 50, DinhsBulwark.get());

	/**
	 * The identifiers for the weapons that perform this special.
	 */
	private final int[] ids;

	/**
	 * The amount of special energy drained by this attack.
	 */
	private final int amount;

	/**
	 * The strength bonus added when performing this special attack.
	 */
	private final CombatStrategy<Player> strategy;

	/**
	 * Creates a new {@link CombatSpecial}.
	 *
	 * @param ids    the identifiers for the weapons that perform this special.
	 * @param amount the amount of special energy drained by this attack.
	 */
	CombatSpecial(int[] ids, int amount, CombatStrategy<Player> strategy) {
		this.ids = ids;
		this.amount = amount;
		this.strategy = strategy;
	}

	public static Optional<CombatSpecial> forId(int weapon) {
		Optional<CombatSpecial> special = Arrays.stream(CombatSpecial.values())
				.filter(c -> Arrays.stream(c.getIds()).anyMatch(id -> weapon == id)).findFirst();
		return special;
	}

	/**
	 * Executes exactly when {@code player} activates the special bar.
	 *
	 * @return the combat strategy
	 */
	public CombatStrategy<Player> getStrategy() {
		return strategy;
	}

	/**
	 * Drains the special bar for {@code player}.
	 *
	 * @param player the player who's special bar will be drained.
	 */
	public void drain(Player player) {
		player.getSpecialPercentage().decrementAndGet(amount, 0);
		updateSpecialAmount(player);
		disable(player, false);
	}

	public static void drain(Player player, int toDrain) {
		player.getSpecialPercentage().decrementAndGet(toDrain, 0);
		updateSpecialAmount(player);
	}

	/**
	 * Restores the special bar for {@code player}.
	 *
	 * @param player the player who's special bar will be restored.
	 * @param amount the amount of energy to restore to the special bar.
	 */
	public static void restore(Player player, int amount) {
		player.getSpecialPercentage().incrementAndGet(amount, 100);
		updateSpecialAmount(player);
	}

	/**
	 * Updates the special bar with the amount of special energy {@code player} has.
	 *
	 * @param player the player who's special bar will be updated.
	 */
	public static void updateSpecialAmount(Player player) {
		if (player.getWeapon().getSpecialBar() == -1 || player.getWeapon().getSpecialMeter() == -1) {
			return;
		}

		int specialBar = player.getWeapon().getSpecialMeter();
		int specialAmount = player.getSpecialPercentage().get() / 10;
		player.send(new SendSpecialAmount());

		// System.out.println(specialBar + " " + specialAmount);

		for (int i = 10; i > 0; i--) {
			player.send(new SendMoveComponent(specialAmount >= i ? 500 : 0, 0, --specialBar));
		}
	}

	/**
	 * Updates the weapon itemcontainer with a special bar if needed.
	 *
	 * @param player the player to update the itemcontainer for.
	 */
	public static void assign(Player player) {
		if (player.getCombatSpecial() != null) {
			player.getCombatSpecial().disable(player, false);
		}

		if (player.getWeapon().getSpecialBar() == -1) {
			player.setCombatSpecial(null);
			return;
		}

		Item item = player.equipment.get(Equipment.WEAPON_SLOT);
		if (item == null) {
			player.setCombatSpecial(null);
			return;
		}

		Optional<CombatSpecial> special = Arrays.stream(CombatSpecial.values())
				.filter(c -> Arrays.stream(c.getIds()).anyMatch(id -> item.getId() == id)).findFirst();
		if (special.isPresent()) {
			player.send(new SendInterfaceLayer(player.getWeapon().getSpecialBar(), false));
			player.setCombatSpecial(special.get());
			return;
		}

		player.send(new SendInterfaceLayer(player.getWeapon().getSpecialBar(), true));
		player.setCombatSpecial(null);
	}

	/**
	 * Gets the identifiers for the weapons that perform this special.
	 *
	 * @return the identifiers for the weapons.
	 */
	public final int[] getIds() {
		return ids;
	}

	/**
	 * Gets the amount of special energy drained by this attack.
	 *
	 * @return the amount of special energy drained.
	 */
	public final int getAmount() {
		return amount;
	}

	public void enable(Player player) {
		if (Activity.evaluate(player, it -> !it.canUseSpecial(player))) {
			return;
		}

		if (!player.isSpecialActivated()) {
			if (player.getSpecialPercentage().intValue() < player.getCombatSpecial().getAmount()) {
				player.send(new SendMessage("You do not have enough special energy left!"));
				return;
			}

			player.send(new SendConfig(301, 1));
			player.setSpecialActivated(true);
			player.send(new SendString("on", 1998));
		}
	}

	public void disable(Player player, boolean clicked) {
		if (player.isSpecialActivated()) {
			player.send(new SendConfig(301, 0));
			player.setSpecialActivated(false);
			player.send(new SendString("off", 1998));
		}
	}

}
