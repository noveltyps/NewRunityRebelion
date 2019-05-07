package io.server.content.skill.impl.magic.spell;

import io.server.Config;
import io.server.content.activity.Activity;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.skill.impl.magic.enchant.BoltEchantData;
import io.server.content.skill.impl.magic.enchant.SpellEnchant;
import io.server.content.skill.impl.magic.enchant.SpellEnchantData;
import io.server.game.Graphic;
import io.server.game.action.impl.SpellAction;
import io.server.game.world.entity.combat.magic.MagicRune;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.net.packet.out.SendForceTab;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.Stopwatch;

/**
 * Handles a player casting a non-combat spell (mostly on items).
 *
 * @author Daniel
 */
public class SpellCasting {
	private final static Item[] ITEMS = { new Item(9236), new Item(9240), new Item(9237), new Item(9238),
			new Item(9241), new Item(9239), new Item(9242), new Item(9243), new Item(9244), new Item(9245) };
	private final static int[][] RUNES = { { 564, 1 }, { 556, 2 }, { 564, 1 }, { 555, 1 }, { 558, 1 }, { 564, 1 },
			{ 557, 2 }, { 564, 1 }, { 555, 2 }, { 564, 1 }, { 556, 3 }, { 561, 1 }, { 564, 1 }, { 554, 2 }, { 564, 1 },
			{ 554, 5 }, { 565, 1 }, { 564, 1 }, { 557, 10 }, { 563, 2 }, { 564, 1 }, { 557, 15 }, { 566, 1 },
			{ 564, 1 }, { 554, 20 }, { 560, 1 } };
	private final static int[] LEVELS = { 4, 7, 14, 24, 27, 29, 49, 57, 68, 87 };

	/** The casting spell */
	public Spell spell;
	/** The casting stopwatch. */
	public Stopwatch castingDelay = Stopwatch.start();
	/** The vengeance stopwatch. */
	public Stopwatch vengeanceDelay = Stopwatch.start();
	/** The player spell casting. */
	private Player player;

	/** Creates the <code>SpellCasting</code>. */
	public SpellCasting(Player player) {
		this.player = player;
		this.spell = null;
	}

	/** Handles casting a combat spell. */
	public boolean cast(Spell s) {
		if (player.skills.getLevel(Skill.MAGIC) < s.getLevel()) {
			player.send(new SendMessage("You need a magic level " + s.getLevel() + " of to cast " + s.getName() + "."));
			return false;
		}

		if (!player.isBot && !MagicRune.hasRunes(player, s.getRunes())) {
			player.send(new SendMessage("You do not have the required runes to cast this spell."));
			return false;
		}

		if (Activity.evaluate(player, it -> !it.canSpellCast(player))) {
			return false;
		}

		spell = s;
		player.inventory.removeAll(s.getRunes());
		return true;
	}

	/** Handles casting the spell. */
	public void cast(Spell spell, Item item) {
		if (player.skills.getLevel(Skill.MAGIC) < spell.getLevel()) {
			player.send(new SendMessage("You need a Magic level of " + spell.getLevel() + " to do this!"));
			return;
		}
		if (spell.getRunes() != null) {
			if (!PlayerRight.isDeveloper(player) && !player.isBot && !MagicRune.hasRunes(player, spell.getRunes())) {
				player.send(new SendMessage("You do not have the required runes to do this!"));
				return;
			}
		}
		
		if (Activity.evaluate(player, it -> !it.canSpellCast(player))) 
			return;
		
		player.action.execute(new SpellAction(player, spell, item), true);
	}

	private boolean hasEnchantRunes(int spellID) {
		SpellEnchant spell = SpellEnchant.forSpell(spellID);

		if (spell == null)
			return false;

		if (spell.getReq3() == 0) {
			return player.inventory.contains(spell.getReq1(), spell.getReqAmt1())
					&& player.inventory.contains(spell.getReq2(), spell.getReqAmt2())
					&& player.inventory.contains(spell.getReq3(), spell.getReqAmt3());
		} else {
			return player.inventory.contains(spell.getReq1(), spell.getReqAmt1())
					&& player.inventory.contains(spell.getReq2(), spell.getReqAmt2());
		}
	}

	private int getEnchantmentLevel(int spellID) {
		switch (spellID) {
		case 1155: // Lvl-1 enchant sapphire
			return 1;
		case 1165: // Lvl-2 enchant emerald
			return 2;
		case 1176: // Lvl-3 enchant ruby
			return 3;
		case 1180: // Lvl-4 enchant diamond
			return 4;
		case 1187: // Lvl-5 enchant dragonstone
			return 5;
		case 6003: // Lvl-6 enchant onyx
			return 6;
		}
		return 0;
	}

	public void enchantItem(int itemID, int spellID) {
		SpellEnchant ens = SpellEnchant.forSpell(spellID);

		if (ens == null) {
			return;
		}

		SpellEnchantData enc = SpellEnchantData.forId(itemID);

		if (enc == null) {
			player.message("You can't enchant this item!");
			return;
		}

		if (player.skills.getMaxLevel(Skill.MAGIC) < enc.getLevel()) {
			player.message("You need a magic level of at least " + enc.getLevelReq() + " to cast this spell.");
			return;
		}

		if (!player.inventory.contains(enc.getUnenchanted(), 1)) {
			return;
		}

		if (!hasEnchantRunes(spellID)) {
			player.message("You do not have enough runes to cast this spell.");
			return;
		}

		if (getEnchantmentLevel(spellID) != enc.getLevel()) {
			player.message("You can only enchant this jewelry using a level-" + enc.getLevel() + " enchantment spell.");
			return;
		}

		player.inventory.remove(enc.getUnenchanted(), 1);
		player.inventory.add(enc.getEnchanted(), 1);
		player.skills.addExperience(Skill.MAGIC,
				(enc.getXp() * Config.MAGIC_MODIFICATION) * new ExperienceModifier(player).getModifier());
		player.inventory.remove(ens.getReq1(), ens.getReqAmt1());
		player.inventory.remove(ens.getReq2(), ens.getReqAmt2());
		player.animate(enc.getAnim());
		player.graphic(new Graphic(enc.getGFX(), true));

		if (ens.getReq3() != -1) {
			player.inventory.remove(ens.getReq3(), ens.getReqAmt3());
		}

		player.send(new SendForceTab(Config.MAGIC_TAB));
	}

	public void openBoltEnchant() {
		for (int index = 0; index < LEVELS.length; index++) {
			String color = player.skills.get(Skill.MAGIC).getLevel() < LEVELS[index] ? "@red@" : "@gre@";
			player.send(new SendString(color + "Magic " + LEVELS[index], 42756 + index));
		}
		for (int i = 0; i < RUNES.length; i++) {
			String color = player.inventory.contains(new Item(RUNES[i][0], RUNES[i][1])) ? "@gre@" : "@red@";
			int amount = player.inventory.computeAmountForId(RUNES[i][0]);
			player.send(new SendString(color + (amount >= RUNES[i][1] ? RUNES[i][1] + "" : amount) + "/" + RUNES[i][1],
					42766 + i));
		}
		player.send(new SendItemOnInterface(42752, ITEMS));
		player.interfaceManager.open(42750);
	}

	public void enchant(int itemId) {
		if (!player.interfaceManager.isInterfaceOpen(42750))
			return;
		if (!BoltEchantData.forItem(itemId).isPresent())
			return;

		BoltEchantData enchant = BoltEchantData.forItem(itemId).get();
		String boltName = ItemDefinition.get(enchant.bolt).getName();

		if (player.skills.get(Skill.MAGIC).getLevel() < enchant.levelRequired) {
			player.send(new SendMessage(
					"You need a Magic level of " + enchant.levelRequired + " to enchant " + boltName + "."));
			return;
		}
		if (!player.inventory.contains(new Item(enchant.bolt, 10))) {
			player.send(new SendMessage("You need 10 " + boltName + " to do this!"));
			return;
		}
		if (!player.inventory.containsAll(enchant.runesRequired)) {
			player.send(new SendMessage("You do not have the required runes to do this!"));
			return;
		}
		player.inventory.removeAll(enchant.runesRequired);
		player.inventory.remove(enchant.bolt, 10);
		player.inventory.add(enchant.enchantedBolt, 10);
		player.skills.addExperience(Skill.MAGIC,
				(250 * Config.MAGIC_MODIFICATION) * new ExperienceModifier(player).getModifier());
		player.send(new SendMessage(
				"@red@You have enchanted 10 " + boltName + (boltName.endsWith("bolts") ? "." : " bolts.")));
		openBoltEnchant();
	}
}
