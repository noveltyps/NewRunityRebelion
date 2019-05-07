package io.server.content.skill.impl.magic.enchant;

public enum SpellEnchant {
	SAPPHIRE(1155, 555, 1, 564, 1, -1, 0), EMERALD(1165, 556, 3, 564, 1, -1, 0), RUBY(1176, 554, 5, 564, 1, -1, 0),
	DIAMOND(1180, 557, 10, 564, 1, -1, 0), DRAGONSTONE(1187, 555, 15, 557, 15, 564, 1),
	ONYX(6003, 557, 20, 554, 20, 564, 1);

	private final int spell, reqRune1, reqAmtRune1, reqRune2, reqAmtRune2, reqRune3, reqAmtRune3;

	SpellEnchant(int spell, int reqRune1, int reqAmtRune1, int reqRune2, int reqAmtRune2, int reqRune3,
			int reqAmtRune3) {
		this.spell = spell;
		this.reqRune1 = reqRune1;
		this.reqAmtRune1 = reqAmtRune1;
		this.reqRune2 = reqRune2;
		this.reqAmtRune2 = reqAmtRune2;
		this.reqRune3 = reqRune3;
		this.reqAmtRune3 = reqAmtRune3;
	}

	public int getSpell() {
		return spell;
	}

	public int getReq1() {
		return reqRune1;
	}

	public int getReqAmt1() {
		return reqAmtRune1;
	}

	public int getReq2() {
		return reqRune2;
	}

	public int getReqAmt2() {
		return reqAmtRune2;
	}

	public int getReq3() {
		return reqRune3;
	}

	public int getReqAmt3() {
		return reqAmtRune3;
	}

	public static SpellEnchant forSpell(int id) {
		for (SpellEnchant spellEnchant : values()) {
			if (spellEnchant.getSpell() == id)
				return spellEnchant;
		}
		return null;
	}

}
