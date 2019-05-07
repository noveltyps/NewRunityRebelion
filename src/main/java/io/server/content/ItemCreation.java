package io.server.content;

import java.util.Optional;

import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;

/**
 * Handles creating items with the use item packet listener.
 *
 * @author Daniel
 * @Edited Adam / Adam_#6723
 */
public class ItemCreation {

	/** Holds the creation data. */
	public enum CreationData {
	SLAYER_HELM(new Item[] { new Item(11864) }, null, new Skill[] { new Skill(Skill.CRAFTING, 55, 0) }, new Item(8921),
			new Item(4166), new Item(4168), new Item(4164), new Item(4551), new Item(4155)),
	SLAYER_HELM_I(new Item[] { new Item(11864) }, null, new Skill[] { new Skill(Skill.CRAFTING, 55, 0) },
			new Item(8901), new Item(4166), new Item(4168), new Item(4164), new Item(4551), new Item(4155)),

	BLACK_SLAYER_HELM(new Item[] { new Item(19639) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11864), new Item(7980)),
	BLACK_SLAYER_HELM_I(new Item[] { new Item(19641) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11865), new Item(7980)),

	GREEN_SLAYER_HELM(new Item[] { new Item(19643) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11864), new Item(7981)),
	GREEN_SLAYER_HELMI(new Item[] { new Item(19645) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11865), new Item(7981)),
	RED_SLAYER_HELM(new Item[] { new Item(19647) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11864), new Item(7979)),
	RED_SLAYER_HELMI(new Item[] { new Item(19649) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11865), new Item(7979)),
	PURPLE_SLAYER_HELM(new Item[] { new Item(21264) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11864), new Item(21275)),
	PURPLE_SLAYER_HELMI(new Item[] { new Item(21266) }, null, new Skill[] { new Skill(Skill.CRAFTING, 99, 0) },
			new Item(11865), new Item(21275)),
	ARMADYL_GODSWORD(new Item[] { new Item(11802) }, null, null, new Item(11798), new Item(11810)),
	BANDOS_GODSWORD(new Item[] { new Item(11804) }, null, null, new Item(11798), new Item(11812)),
	SARADOMIN_GODSWORD(new Item[] { new Item(11806) }, null, null, new Item(11798), new Item(11814)),
	ZAMORAK_GODSWORD(new Item[] { new Item(11808) }, null, null, new Item(11798), new Item(11816)),

	PRIMORDIAL_BOOTS(new Item[] { new Item(13239) }, null,
			new Skill[] { new Skill(Skill.RUNECRAFTING, 60, 0), new Skill(Skill.MAGIC, 60, 0) }, new Item(11840),
			new Item(13231)),
	ETERNAL_BOOTS(new Item[] { new Item(13235) }, null,
			new Skill[] { new Skill(Skill.RUNECRAFTING, 60, 0), new Skill(Skill.MAGIC, 60, 0) }, new Item(6920),
			new Item(13227)),
	PEGASIAN_BOOTS(new Item[] { new Item(13237) }, null,
			new Skill[] { new Skill(Skill.RUNECRAFTING, 60, 0), new Skill(Skill.MAGIC, 60, 0) }, new Item(2577),
			new Item(13229)),

	INFERNAL_AXE(new Item[] { new Item(13241) }, null,
			new Skill[] { new Skill(Skill.WOODCUTTING, 61, 0), new Skill(Skill.FIREMAKING, 85, 0) }, new Item(6739),
			new Item(13233)),
	INFERNAL_PICKAXE(new Item[] { new Item(13243) }, null, new Skill[] { new Skill(Skill.SMITHING, 85, 0) },
			new Item(11920), new Item(13233)),
	INFERNAL_HARPOON(new Item[] { new Item(21031) }, null, new Skill[] { new Skill(Skill.COOKING, 85, 0) },
			new Item(21028), new Item(13233)),

	GODSWORD_SHARDS_1_AND_2(new Item[] { new Item(11794) }, null, null, new Item(11818), new Item(11820)),
	GODSWORD_SHARDS_2_AND_3(new Item[] { new Item(11800) }, null, null, new Item(11822), new Item(11820)),
	GODSWORD_SHARDS_1_AND_3(new Item[] { new Item(11796) }, null, null, new Item(11818), new Item(11822)),

	GODSWORD_BLADE(new Item[] { new Item(11798) }, null, new Skill[] { new Skill(Skill.SMITHING, 80, 0) },
			new Item(11822), new Item(11794)),
	GODSWORD_BLADE_I(new Item[] { new Item(11798) }, null, new Skill[] { new Skill(Skill.SMITHING, 80, 0) },
			new Item(11818), new Item(11800)),
	GODSWORD_BLADE_II(new Item[] { new Item(11798) }, null, new Skill[] { new Skill(Skill.SMITHING, 80, 0) },
			new Item(11820), new Item(11796)),
	TOXIC_BLOWPIPE(new Item(12924), null, new Skill[] { new Skill(Skill.FLETCHING, 53, 0) }, new Item(12922, 1),
			new Item(1755, 1)),
	FIRE_MAX_CAPE(new Item[] { new Item(13329), new Item(13330) }, null, null, new Item(6570), new Item(13280),
			new Item(13281)),
	SARADOMIN_MAX_CAPE(new Item[] { new Item(13331), new Item(13332) }, null, null, new Item(2412), new Item(13280),
			new Item(13281)),
	ZAMORAK_MAX_CAPE(new Item[] { new Item(13333), new Item(13334) }, null, null, new Item(2414), new Item(13280),
			new Item(13281)),
	GUTHIX_MAX_CAPE(new Item[] { new Item(13335), new Item(13336) }, null, null, new Item(2413), new Item(13280),
			new Item(13281)),
	AVA_MAX_CAPE(new Item[] { new Item(13337), new Item(13338) }, null, null, new Item(10498), new Item(13280),
			new Item(13281)),
	AVA2_MAX_CAPE(new Item[] { new Item(13337), new Item(13338) }, null, null, new Item(10499), new Item(13280),
			new Item(13281)),
	TOXIC_STAFF_OF_THE_DEAD(new Item(12904), null, new Skill[] { new Skill(Skill.CRAFTING, 59, 0) }, new Item(11791, 1),
			new Item(12932, 1)),
	AMULET_OF_TORTURE(new Item(19553), null, new Skill[] { new Skill(Skill.CRAFTING, 59, 0) }, new Item(6581, 1),
			new Item(19529, 1)),
	TRIDENT_OF_SWAMP(new Item(12899), null, null, new Item(12932, 1), new Item(11907, 1)),

	DRAGON_PICKAXE(new Item(12797), null, null, new Item(11920, 1), new Item(12800, 1)),
	MAGIC_SHORTBOW_INFUSED(new Item(12788), new Item(861), null, new Item(12786, 1), new Item(861, 1)),
	ABYSSAL_TENTACLE_WHIP(new Item(12006, 1), new Item(12004, 1), null, new Item(12004, 1), new Item(4151, 1)),
	ABYSSAL_VOLCANIC_WHIP(new Item(12773), new Item(4151, 1), null, new Item(12771, 1), new Item(4151, 1)),
	ABYSSAL_FROZEN_WHIP(new Item(12774), new Item(4151, 1), null, new Item(12769, 1), new Item(4151, 1)),
	SERPENTINE_VISAGE(new Item(12929), new Item(12927, 1), new Skill[] { new Skill(Skill.CRAFTING, 52, 0) },
			new Item(12927, 1), new Item(1755, 1)),
	FURY_AMULET_KIT(new Item(12436), null, null, new Item(6585, 1), new Item(12526, 1)),
	DFS_CREATION(new Item(11283), null, new Skill[] { new Skill(Skill.CRAFTING, 85, 0) }, new Item(1540, 1),
			new Item(11286, 1)),
	DRAGON_SQ_SHIELD_KIT(new Item(12418), null, null, new Item(1187, 1), new Item(12532, 1)),
	DRAGON_CHAINBODY_KIT(new Item(12414), null, null, new Item(3140, 1), new Item(12534, 1)),
	DRAGON_PLATELEGS_KIT(new Item(12415), null, null, new Item(4087, 1), new Item(12536, 1)),
	DRAGON_FULLHELM_KIT(new Item(12417), null, null, new Item(11335, 1), new Item(12538, 1)),
	DRAGON_PLATESKIRT_KIT(new Item(12416), null, null, new Item(4585, 1), new Item(12536, 1)),
	DRAGON_SCIM_KIT(new Item(20000), null, null, new Item(20002, 1), new Item(4587, 1)),
	DRAGON_DEF_KIT(new Item(19722), null, null, new Item(20143, 1), new Item(12954, 1)),

	AMULET_OF_TORTURE_KIT(new Item(20366), null, null, new Item(20062, 1), new Item(19553, 1)),
	AMULET_OF_OCCULT_KIT(new Item(19720), null, null, new Item(20065, 1), new Item(12002, 1)),

	
	ARMADYLGOD_OR(new Item(20368), null, null, new Item(20068, 1), new Item(11802, 1)),
	BANDOSGOD_OR(new Item(20370), null, null, new Item(20071, 1), new Item(11804, 1)),
	ZAMORAKGOD_OR(new Item(20372), null, null, new Item(20074, 1), new Item(11806, 1)),
	SARADOMINGOD_OR(new Item(20374), null, null, new Item(20077, 1), new Item(11808, 1)),
	TRIDENT_OF_SWAMPS(new Item(12899), null, null, new Item(11907, 1), new Item(12932, 1)),
	INFERNAL_MAXCAPE(new Item[] { new Item(21285), new Item(21282) }, null, null, new Item(21295), new Item(13280),
			new Item(13281)),
	AVAS_ASSEMBLER_CAPE(new Item[] { new Item(21898), new Item(21900) }, null, null, new Item(22109), new Item(13280),
			new Item(13281)),
	AVAS_ASSEMBLER(new Item(22109), null, null, new Item(21907, 1), new Item(10499, 1)),
	ZENYTE_NECK(new Item(19535), null, null, new Item(6577, 1), new Item(19529, 1)),
	DRAGON_KEY(new Item(22092), null, new Skill[] { new Skill(Skill.CRAFTING, 59, 0) }, new Item(22090, 1), new Item(22091, 1), new Item(22089, 1), new Item(22088, 1)),
    
	BLESSED_SS(new Item(12831), null, null, new Item(12833, 1), new Item(12829, 1)),

	
	CAP_AND_GOGGLES(new Item(9946), null, null, new Item(9945, 1), new Item(9472, 1));

		/** The product of the creation. */
		public final Item[] product;

		/** The dropped item. */
		public final Item dropped;

		/** The skill required. */
		public final Skill[] level;

		/** The items required. */
		public final Item[] required;

		/** Constructs a new <code>CreationData</code>. */
		CreationData(Item product, Item dropped, Skill[] level, Item... required) {
			this.product = new Item[] { product };
			this.dropped = dropped;
			this.level = level;
			this.required = required;
		}

		/** Constructs a new <code>CreationData</code>. */
		CreationData(Item[] product, Item dropped, Skill[] level, Item... required) {
			this.product = product;
			this.dropped = dropped;
			this.level = level;
			this.required = required;
		}

		/** Handles finding the creation data based off the items used. */
		public static Optional<CreationData> forItems(Item first, Item second) {
			for (CreationData data : CreationData.values()) {
				int found = 0;
				for (Item items : data.required) {
					if (items.getId() == first.getId() || items.getId() == second.getId())
						found++;
				}
				if (found >= 2)
					return Optional.of(data);
			}
			return Optional.empty();
		}

		/** Finds the creation data based off the product item. */
		public static Optional<CreationData> forItem(Item item) {
			for (CreationData data : CreationData.values()) {
				for (Item items : data.product) {
					if (item.getId() == items.getId())
						return Optional.of(data);
				}
			}
			return Optional.empty();
		}
	}

	/** Handles creation items on death. */
	public static Item onDeath(Item item) {
		Optional<CreationData> data = CreationData.forItem(item);
		return data.isPresent() && data.get().dropped != null ? data.get().dropped : item;
	}
}
