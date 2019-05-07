package io.server.content.upgrading;

import io.server.game.world.items.Item;

/**
 * Upgrade Data Storage
 * @author Nerik#8690
 * @edited Adam_#6723
 *
 */
public enum UpgradeData {

	LIME_WHIP(new Item(21225, 1), new Item(21292, 1), new Item(21820, 500), 0.20),
	ICE_KATANA(new Item(3273, 1), new Item(21294, 1), new Item(21820, 200), 0.30),
	SAMURAI_HELM(new Item(20035, 1), new Item(20049, 1), new Item(21820, 150), 0.30),
	SAMURAI_PLATE(new Item(20038, 1), new Item(13284, 1), new Item(21820, 150), 0.30),
	SAMURAI_LEGS(new Item(20044, 1), new Item(20058, 1), new Item(21820, 150), 0.30),
	SAMURAI_GLOVES(new Item(20041, 1), new Item(20055, 1), new Item(21820, 150), 0.30),
	SAMURAI_BOOTS(new Item(20047, 1), new Item(20061, 1), new Item(21820, 150), 0.30),
	ELITE_SS(new Item(13719, 1), new Item(13722, 1), new Item(21820, 250), 0.35),
	RAPTOR_PLATEBODY(new Item(17164, 1), new Item(17167, 1), new Item(21820, 250), 0.20),
	RAPTOR_HELM(new Item(17163, 1), new Item(17166, 1), new Item(21820, 250), 0.20),
	RAPTOR_PLATELEGS(new Item(17165, 1), new Item(17168, 1), new Item(21820, 250), 0.20),
	MAGMA_AXE(new Item(13687, 1), new Item(13833, 1), new Item(21820, 300), 0.25),
	GLAIVE(new Item(11063, 1), new Item(17160, 1), new Item(21820, 500), 0.20),
	PHOENIX_BOW(new Item(3274, 1), new Item(13831, 1), new Item(21820, 500), 0.15),
	LAVABOW(new Item(13831, 1), new Item(13749, 1), new Item(21820, 500), 0.10),
	SARADOMIN_BOW(new Item(20997, 1), new Item(13831, 1), new Item(21820, 500), 0.20),
	
	ELITETORVA_HELM(new Item(6564, 1), new Item(16648, 1), new Item(21820, 1000), 0.20),
	ELITETORVA_BODY(new Item(6565, 1), new Item(16647, 1), new Item(21820, 1000), 0.20),
	ELITETORVA_LEGS(new Item(6566, 1), new Item(16649, 1), new Item(21820, 1000), 0.20),
	LAVA_SCYTHE(new Item(13739, 1), new Item(22325, 1), new Item(21820, 450), 0.30),
	GHRAZY_RAPIER(new Item(7029, 1), new Item(22324, 1), new Item(21820, 600), 0.15),
	;

	
	private Item item_input, item_reward, etharRequirement;
	private double chance;

	UpgradeData(Item item_input, Item item_reward, Item etharRequirement, double chance) {
		this.item_input = item_input;
		this.item_reward = item_reward;
		this.etharRequirement = etharRequirement;
		this.chance = chance;
	}

	public Item getItemInput() {
		return item_input;
	}

	public Item getItemReward() {
		return item_reward;
	}

	public Item getEtharRequirement() {
		return etharRequirement;
	}

	public double getChance() {
		return chance;
	}
	
	public static Item[] ITEMS = new Item[] {
			LIME_WHIP.getItemReward(), 
			ICE_KATANA.getItemReward(),
			SAMURAI_HELM.getItemReward(),
			SAMURAI_PLATE.getItemReward(),
			SAMURAI_LEGS.getItemReward(),
			SAMURAI_GLOVES.getItemReward(),
			SAMURAI_BOOTS.getItemReward(),
			MAGMA_AXE.getItemReward(),
			GLAIVE.getItemReward(),
			ELITE_SS.getItemReward(),
			PHOENIX_BOW.getItemReward(),
			RAPTOR_PLATEBODY.getItemReward(),
			RAPTOR_HELM.getItemReward(),
			RAPTOR_PLATELEGS.getItemReward(),
			LAVABOW.getItemReward(),
			ELITETORVA_HELM.getItemReward(),
			ELITETORVA_BODY.getItemReward(),
			ELITETORVA_LEGS.getItemReward(),
			LAVA_SCYTHE.getItemReward(),
			GHRAZY_RAPIER.getItemReward(),
	};
	

}
