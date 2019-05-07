package io.server.content.skill.impl.magic.enchant;

import java.util.HashMap;
import java.util.Map;

public enum SpellEnchantData {
	SAPPHIRERING(1637, 2550, 7, 18, 719, 114, 1), SAPPHIREAMULET(1694, 1727, 7, 18, 719, 114, 1),
	SAPPHIRENECKLACE(1656, 3853, 7, 18, 719, 114, 1), EMERALDRING(1639, 2552, 27, 37, 719, 114, 2),
	EMERALDAMULET(1696, 1729, 27, 37, 719, 114, 2), EMERALDNECKLACE(1658, 5521, 27, 37, 719, 114, 2),
	RUBYRING(1641, 2568, 47, 59, 720, 115, 3), RUBYAMULET(1698, 1725, 47, 59, 720, 115, 3),
	RUBYNECKLACE(1660, 11194, 47, 59, 720, 115, 3), DIAMONDRING(1643, 2570, 57, 67, 720, 115, 4),
	DIAMONDAMULET(1700, 1731, 57, 67, 720, 115, 4), DIAMONDNECKLACE(1662, 11090, 57, 67, 720, 115, 4),
	DRAGONSTONERING(1645, 2572, 68, 78, 721, 116, 5), DRAGONSTONEAMULET(1702, 1712, 68, 78, 721, 116, 5),
	DRAGONSTONENECKLACE(1664, 11105, 68, 78, 721, 116, 5), ONYXRING(6575, 6583, 87, 97, 721, 452, 6),
	ONYXAMULET(6581, 6585, 87, 97, 721, 452, 6), ONYXNECKLACE(6577, 11128, 87, 97, 721, 452, 6);

	private final int unenchanted, enchanted, levelReq, xpGiven, anim, gfx, reqEnchantmentLevel;

	SpellEnchantData(int unenchanted, int enchanted, int levelReq, int xpGiven, int anim, int gfx,
			int reqEnchantmentLevel) {
		this.unenchanted = unenchanted;
		this.enchanted = enchanted;
		this.levelReq = levelReq;
		this.xpGiven = xpGiven;
		this.anim = anim;
		this.gfx = gfx;
		this.reqEnchantmentLevel = reqEnchantmentLevel;
	}

	public int getUnenchanted() {
		return unenchanted;
	}

	public int getEnchanted() {
		return enchanted;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public int getXp() {
		return xpGiven;
	}

	public int getAnim() {
		return anim;
	}

	public int getGFX() {
		return gfx;
	}

	public int getLevel() {
		return reqEnchantmentLevel;
	}

	private static final Map<Integer, SpellEnchantData> enc = new HashMap<>();

	public static SpellEnchantData forId(int itemID) {
		return enc.get(itemID);
	}

	static {
		for (SpellEnchantData en : SpellEnchantData.values()) {
			enc.put(en.getUnenchanted(), en);
		}
	}
}