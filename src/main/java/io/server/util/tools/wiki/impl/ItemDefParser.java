package io.server.util.tools.wiki.impl;

import static io.server.game.world.entity.combat.CombatConstants.BONUS_CONFIG_FIELD_NAMES;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.items.containers.equipment.EquipmentType;
import io.server.util.Utility;
import io.server.util.parser.GsonParser;
import io.server.util.tools.wiki.parser.WikiTable;
import io.server.util.tools.wiki.parser.WikiTableParser;

public class ItemDefParser extends WikiTableParser {

	private static final Logger logger = LogManager.getLogger();

	private static final String[] BAD = { "Notes", "Lathas'_amulet", "Bronze_pick_head", "Iron_pick_head",
			"Steel_pick_head", "Adamant_pick_head", "Mithril_pick_head", "Bronze_axe_head", "Rune_pick_head",
			"Iron_axe_head", "Steel_axe_head", "Black_axe_head", "Mithril_axe_head", "Adamant_axe_head",
			"Rune_axe_head", "Phoenix_hq_key", "Poisoned_dart(p)", "Worm", "Throwing_rope", "Cooking_pot",
			"Poisoned_dagger(p)", "Farmer's_fork", "Shantay_disclaimer", "Unfinished_cocktail", "Odd_cocktail",
			"Equa_toad's_legs", "Spicy_toad's_legs", "Seasoned_legs", "Spicy_worm", "Odd_gnomebowl", "Odd_crunchies",
			"Odd_batta", "Dead_orb", "Sliding_piece", "Firework", "Left-handed_banana", "Knight_of_ardougne", "Shoe",
			"Shoe.", "Board_game_piece", "Stool", "Sliding_button", "Monkey_magic", "Monkey_wrench", "Mouth_grip",
			"Puddle_of_slime", "Female_h.A.M.", "Male_h.A.M.", "500k_experience_lamp", "Castlewars_hood",
			"Swamp_wallbeast", "Swamp_cave_slime", "Swamp_cave_bug", "Empty_oil_lamp", "Empty_candle_lantern",
			"Empty_oil_lantern", "Blue_sweets", "Deep_blue_sweets", "White_sweets", "Green_sweets", "Red_sweets",
			"Pink_sweets", "Black_spear(kp)", "Broken_plate", "Book_of_'h.A.M'", "Ahrim's_hood_100", "Ahrim's_hood_75",
			"Ahrim's_hood_50", "Ahrim's_staff_75", "Ahrim's_staff_50", "Ahrim's_staff_25", "Ahrim's_robetop_100",
			"Ahrim's_robetop_75", "Ahrim's_robetop_50", "Ahrim's_robeskirt_100", "Ahrim's_robetop_25",
			"Ahrim's_robeskirt_75", "Dharok's_helm_100", "Dharok's_helm_75", "Dharok's_helm_50", "Dharok's_helm_25",
			"Dharok's_platebody_100", "Dharok's_platebody_50", "Dharok's_platebody_25", "Dharok's_platelegs_25",
			"Guthan's_helm_100", "Guthan's_helm_75", "Guthan's_helm_50", "Guthan's_helm_25", "Guthan's_warspear_100",
			"Guthan's_warspear_75", "Guthan's_platebody_75", "Guthan's_platebody_50", "Guthan's_chainskirt_75",
			"Karil's_coif_100", "Karil's_coif_75", "Karil's_crossbow_100", "Karil's_crossbow_75", "Karil's_crossbow_50",
			"Karil's_crossbow_25", "Karil's_leathertop_75", "Karil's_leathertop_50", "Karil's_leatherskirt_75",
			"Torag's_helm_100", "Torag's_hammers_100", "Torag's_hammers_75", "Torag's_hammers_25", "Torag's_hammers_50",
			"Torag's_platebody_25", "Torag's_platelegs_75", "Torag's_platelegs_50", "Torag's_platelegs_25",
			"Verac's_helm_100", "Verac's_helm_25", "Verac's_flail_100", "Verac's_flail_75", "Verac's_flail_50",
			"Verac's_flail_25", "Verac's_brassard_100", "Verac's_brassard_25", "Verac's_plateskirt_100", "Apples(1)",
			"Apples(3)", "Apples(2)", "Oranges(3)", "Oranges(1)", "Oranges(4)", "Strawberries(2)", "Strawberries(1)",
			"Strawberries(3)", "Strawberries(4)", "Bananas(3)", "Bananas(4)", "Potatoes(3)", "Potatoes(6)",
			"Potatoes(7)", "Potatoes(9)", "Potatoes(8)", "Onions(2)", "Onions(1)", "Onions(3)", "Onions(4)",
			"Onions(8)", "Onions(9)", "Cabbages(2)", "Cabbages(3)", "Cabbages(4)", "Cabbages(5)", "Cabbages(6)",
			"Old_man's_message", "Cabbages(7)", "Dial", "Acetic_acid", "Nitrous_oxide", "Sodium_chloride",
			"Metal_spade", "Fox", "Poison_dagger(p+)", "Poison_dagger(p++)", "Asgarnian_ale(m)", "Greenman's_ale(m)",
			"Dragon_bitter(m)", "Greenmans_ale(1)", "Greenmans_ale(2)", "Greenmans_ale(3)", "Dwarven_stout(m2)",
			"Dwarven_stout(m1)", "Dwarven_stout(m3)", "Dwarven_stout(m4)", "Asgarnian_ale(m1)", "Asgarnian_ale(m2)",
			"Asgarnian_ale(m3)", "Asgarnian_ale(m4)", "Greenmans_ale(m1)", "Greenmans_ale(m2)", "Greenmans_ale(m3)",
			"Greenmans_ale(m4)", "Mind_bomb(m1)", "Mind_bomb(m3)", "Mind_bomb(m2)", "Mind_bomb(m4)",
			"Dragon_bitter(m1)", "Dragon_bitter(m2)", "Dragon_bitter(m3)", "Dragon_bitter(m4)", "Moonlight_mead(m1)",
			"Moonlight_mead(m2)", "Moonlight_mead(m3)", "Moonlight_mead(m4)", "Axeman's_folly(m1)",
			"Axeman's_folly(m2)", "Axeman's_folly(m3)", "Chef's_delight(m3)", "Chef's_delight(m1)",
			"Chef's_delight(m2)", "Axeman's_folly(m4)", "Slayer_respite(m3)", "Slayer_respite(m1)",
			"Slayer_respite(m2)", "Slayer_respite(m4)", "Cider(m1)", "Cider(m2)", "Cider(m3)", "Tomatoes(1)",
			"Tomatoes(4)", "Spirit_roots", "Worn_key", "Teleport_crystal_(4)", "Teleport_crystal_(3)",
			"Teleport_crystal_(2)", "Enchanted_lyre(2)", "Oak_blackjack(o)", "Oak_blackjack(d)", "Willow_blackjack(d)",
			"Willow_blackjack(o)", "Maple_blackjack(o)", "Maple_blackjack(d)", "Mage_arena_cape", "Orchid_seed",
			"White_tree_shoot_(w)", "Agility_jump", "Agility_climb", "Agility_contortion", "Agility_balance",
			"Drop_rate_increase_(1.5x)", "Catspeak_amulet(e)", "Empty_fishbowl", "Guam_in_a_box", "Guam_in_a_box?",
			"Seaweed_in_a_box?", "Seaweed_in_a_box", "Dragon_axe_head", "Menaphite_thug", "Mime_Box", "Drill_Demon_Box",
			"Clan_Resource_Box", "Marionette_handle", "Magical_orb_(a)", "Part_mud_pie", "Part_garden_pie",
			"Part_fish_pie", "Part_admiral_pie", "Part_wild_pie", "Part_summer_pie", "Burnt_chompy", "Paddle", "Brulee",
			"Red_spice_(4)", "Red_spice_(3)", "Red_spice_(2)", "Red_spice_(1)", "Orange_spice_(4)", "Orange_spice_(2)",
			"Orange_spice_(3)", "Orange_spice_(1)", "Brown_spice_(3)", "Brown_spice_(2)", "Brown_spice_(1)",
			"Yellow_spice_(3)", "Yellow_spice_(2)", "Yellow_spice_(1)", "Drop_rate_increase_(2x)", "Evil_dave",
			"Lumbridge_guide", "Pirate_pete", "Sir_amik_varze", "Skrach", "Overgrown_hellcat", "Lazy_hell_cat",
			"Rod_of_ivandis(10)", "Rod_of_ivandis(8)", "Rod_of_ivandis(9)", "Rod_of_ivandis(4)", "Rod_of_ivandis(5)",
			"Rod_of_ivandis(3)", "Rod_of_ivandis(2)", "Hoop", "Bow_and_arrow", "Pot_of_tea_(4)", "Pot_of_tea_(3)",
			"Pot_of_tea_(2)", "Pot_of_tea_(1)", "Paintbrush", "Toy_soldier_(wound)", "Toy_doll_(wound)",
			"Toy_mouse_(wound)", "Zaros_mjolnir", "Servant_bell", "Icon_of_bob", "Cloth-covered_altar", "Small_statues",
			"Medium_statues", "Large_statues", "Small_portrait", "Medium_head", "Minor_head", "Major_head",
			"Mounted_sword", "Small_landscape", "Large_portrait", "Mid-level_plants", "Large_landscape",
			"Rune_display_case", "Low-level_plants", "High-level_plants", "Candles", "Torches", "Skull_torches",
			"Skeleton_guard", "Hobgoblin_guard", "Tok-xil", "Marble_att._Stone", "Magical_balance_1",
			"Magical_balance_2", "Magical_balance_3", "Hangman_game", "Large-leaf_plant", "Adamantite_armour",
			"Runite_armour", "King_arthur", "Greater_focus", "Mahogany_eagle", "Mahogany_demon", "Combat_room",
			"Dungeon_cross", "Treasure_room", "Dungeon_stairs", "Formal_garden", "Shot", "Zanik_(ham)",
			"Zanik_(showdown)", "Magic_essence(4)", "Magic_essence(3)", "Magic_essence(2)", "Magic_essence(1)",
			"Pharaoh's_sceptre_(1)", "Pharaoh's_sceptre_(2)", "Black_goblin_mail", "Yellow_goblin_mail",
			"Green_goblin_mail", "Purple_goblin_mail", "Suqah_monster", "Astral_tiara", "Blurite_bolts(p)",
			"Blurite_bolts(p+)", "Blurite_bolts(p++)", "Mixed_special", "Broken_cauldron", "A_red_circle",
			"A_red_triangle", "A_red_square", "A_red_pentagon", "An_orange_circle", "An_orange_triangle",
			"A_yellow_circle", "An_orange_square", "A_yellow_triangle", "A_yellow_square", "A_yellow_pentagon",
			"A_green_triangle", "A_green_square", "A_green_circle", "A_green_pentagon", "A_blue_circle",
			"A_blue_triangle", "A_blue_square", "A_blue_pentagon", "An_indigo_circle", "An_indigo_triangle",
			"A_violet_circle", "An_indigo_square", "An_indigo_pentagon", "A_violet_triangle", "A_violet_square",
			"A_violet_pentagon", "Attack_cape(t)", "Strength_cape(t)", "Defence_cape(t)", "Prayer_cape(t)",
			"Hitpoints_cape(t)", "Agility_cape(t)", "Thieving_cape(t)", "Fletching_cape(t)", "Slayer_cape(t)",
			"Construct._Cape(t)", "Cooking_cape(t)", "Firemaking_cape(t)", "Woodcut._Cape(t)", "Farming_cape(t)",
			"Oak_costume_box", "Teak_costume_box", "Mahogany_cos_box", "Carved_oak_wardrobe", "Marble_wardrobe",
			"Carved_teak_wardrobe", "Magic_cape_rack", "M._Treasure_chest", "Red_destabiliser", "Blue_destabiliser",
			"Green_destabiliser", "Yellow_destabiliser", "Black_destabiliser", "Yellow_balloon", "Blue_balloon",
			"Red_balloon", "Orange_balloon", "Black_balloon", "Purple_balloon", "Pink_balloon", "Footprint",
			"Imp-in-a-box(2)", "Imp-in-a-box(1)", "More...", "Back...", "Spiked/pois._Egg", "Healing_vial(2)",
			"Healing_vial(1)", "No_eggs", "Keris(p)", "Keris(p+)", "Keris(p++)", "D'hide_body_(g)", "Wizard_robe_(t)",
			"D'hide_body_(t)", "Documents", "Bulging_taxbag", "A_jester_stick", "Apricot_cream_pie", "Summer_garden",
			"Spring_garden", "Autumn_garden", "Winter_garden", "Mixture_-_step_1(4)", "Mixture_-_step_1(3)",
			"Mixture_-_step_1(1)", "Mixture_-_step_1(2)", "Mixture_-_step_2(3)", "Mixture_-_step_2(2)",
			"Mixture_-_step_2(1)", "Mixture_-_step_2(4)", "Pushup", "Situp", "Skull_staples", "Starjump",
			"Grubs_?_la_mode", "Powerbox", "Magic_egg", "Chocolate_chunks", "Chocolate_kebbit", "Zanik_(slice)",
			"Castle_wars_bracelet(1)", "Castle_wars_bracelet(2)", "Abyssal_bracelet(4)", "Abyssal_bracelet(3)",
			"Abyssal_bracelet(2)", "Abyssal_bracelet(1)", "Drop_rate_increase_(2.5x)", "Dream_vial_(empty)",
			"Dream_vial_(water)", "Dream_vial_(herb)", "Restored_shield", "250k_experience_lamp", "2x_experience_(1hr)",
			"2x_experience_(3hr)", "2x_resource_(15min)", "Bronze_hasta(kp)", "Iron_hasta(kp)", "Steel_hasta(kp)",
			"Mithril_hasta(kp)", "Adamant_hasta(kp)", "Fish_vial", "Super_str._Mix(2)", "Super_str._Mix(1)",
			"Super_def._Mix(2)", "Super_def._Mix(1)", "Wimpy_feather", "Void_seal(7)", "Void_seal(6)", "Void_seal(3)",
			"Void_seal(2)", "Void_seal(1)", "2x_experience_(5hr)", "Black_pick_head", "Junk", "Clan_wars_cape",
			"Community_pumpkin", "Clan_Showcase_Box", "Old_school_bond_(untradeable)", "Music_cape(t)",
			"Infernal_axe_(uncharged)", "Infernal_pickaxe_(uncharged)", "Key_master's_key", "Credits",
			"Vial_of_tears_(empty)", "Vial_of_tears_(1)", "Vial_of_tears_(2)", "Vial_of_tears_(3)",
			"Vial_of_tears_(full)", "Deadman_teleport_tablet", "Dice_(up_to_100)", "Dragon_javelin(p++)",
			"Dragon_javelin(p+)", "Combat_scarred_key", "Key_(elite)", "Puzzle_box_(master)", "Fire_cape_(broken)",
			"Fire_max_cape_(broken)", "Iron_defender_(broken)", "Steel_defender_(broken)", "Bronze_defender_(broken)",
			"Black_defender_(broken)", "Mithril_defender_(broken)", "Adamant_defender_(broken)",
			"Rune_defender_(broken)", "Dragon_defender_(broken)", "Void_knight_top_(broken)", "Elite_void_top_(broken)",
			"Void_knight_robe_(broken)", "Elite_void_robe_(broken)", "Void_knight_mace_(broken)",
			"Void_mage_helm_(broken)", "Void_knight_gloves_(broken)", "Void_ranger_helm_(broken)",
			"Void_melee_helm_(broken)", "Decorative_armour_(broken)", "Decorative_sword_(broken)",
			"Decorative_helm_(broken)", "Decorative_shield_(broken)", "Fighter_hat_(broken)", "Ranger_hat_(broken)",
			"Penance_skirt_(broken)", "Healer_hat_(broken)", "Fighter_torso_(broken)", "Blurite_tokens",
			"Stash_units_(easy)", "Stash_units_(medium)", "Stash_units_(hard)", "Saradomin_halo_(broken)",
			"Zamorak_halo_(broken)", "Stash_units_(elite)", "Stash_units_(master)", "Guthix_halo_(broken)",
			"Rejuvenation_potion_(4)", "Rejuvenation_potion_(3)", "Rejuvenation_potion_(2)", "Rejuvenation_potion_(1)",
			"Elder_(-)(1)", "Elder_(-)(2)", "Elder_(-)(3)", "Elder_potion_(1)", "Elder_(-)(4)", "Elder_potion_(3)",
			"Elder_potion_(2)", "Elder_potion_(4)", "Elder_(+)(1)", "Elder_(+)(2)", "Elder_(+)(3)", "Elder_(+)(4)",
			"Twisted_(-)(1)", "Twisted_(-)(2)", "Twisted_(-)(3)", "Twisted_(-)(4)", "Twisted_potion_(1)",
			"Twisted_potion_(3)", "Twisted_potion_(2)", "Twisted_potion_(4)", "Twisted_(+)(1)", "Twisted_(+)(2)",
			"Kodai_(-)(2)", "Twisted_(+)(3)", "Twisted_(+)(4)", "Kodai_(-)(1)", "Kodai_(-)(3)", "Kodai_(-)(4)",
			"Kodai_potion_(2)", "Kodai_potion_(1)", "Kodai_potion_(3)", "Kodai_potion_(4)", "Kodai_(+)(1)",
			"Kodai_(+)(2)", "Kodai_(+)(4)", "Kodai_(+)(3)", "Revitalisation_(-)(1)", "Revitalisation_(-)(2)",
			"Revitalisation_(-)(3)", "Revitalisation_(-)(4)", "Revitalisation_potion_(1)", "Revitalisation_potion_(2)",
			"Revitalisation_potion_(3)", "Revitalisation_potion_(4)", "Revitalisation_(+)(1)", "Revitalisation_(+)(2)",
			"Revitalisation_(+)(3)", "Revitalisation_(+)(4)", "Prayer_enhance_(-)(1)", "Prayer_enhance_(-)(2)",
			"Prayer_enhance_(-)(3)", "Prayer_enhance_(-)(4)", "Prayer_enhance_(1)", "Prayer_enhance_(2)",
			"Prayer_enhance_(3)", "Prayer_enhance_(4)", "Prayer_enhance_(+)(1)", "Prayer_enhance_(+)(2)",
			"Prayer_enhance_(+)(4)", "Prayer_enhance_(+)(3)", "Xeric's_aid_(-)(1)", "Xeric's_aid_(-)(2)",
			"Xeric's_aid_(-)(3)", "Xeric's_aid_(-)(4)", "Xeric's_aid_(1)", "Xeric's_aid_(2)", "Xeric's_aid_(3)",
			"Xeric's_aid_(+)(1)", "Xeric's_aid_(4)", "Xeric's_aid_(+)(2)", "Xeric's_aid_(+)(3)", "Xeric's_aid_(+)(4)",
			"Overload_(-)(1)", "Overload_(-)(2)", "Overload_(-)(3)", "Overload_(-)(4)", "Overload_(+)(1)",
			"Overload_(+)(2)", "Overload_(+)(3)", "Overload_(+)(4)", "Infernal_harpoon_(uncharged)",
			"Ring_of_returning(4)", "Ring_of_returning(3)", "Ring_of_returning(2)", "Ring_of_returning(1)",
			"Burning_amulet(4)", "Burning_amulet(3)", "Burning_amulet(2)", "Burning_amulet(1)", "Salted_chocolate_mix",
			"Copper's_crimson_collar", "Tzhaar-hur", "Infernal_cape_(broken)", "Infernal_max_cape_(broken)",
			"Wilderness_champion_amulet", "Fossil_island_wyvern" };

	private ItemDefParser() {
		super(generateTables());
	}

	public static void main(String[] args) throws InterruptedException {
		ItemDefParser parser = new ItemDefParser();
		parser.begin();
	}

	private static LinkedList<WikiTable> generateTables() {
		List<Definition> definitions = parseDefinitions();
		LinkedList<WikiTable> tables = new LinkedList<>();
		String link = "http://oldschoolrunescape.wikia.com/wiki/";
		Iterator<Definition> iterator = definitions.iterator();

		while (iterator.hasNext()) {
			Definition definition = iterator.next();
			iterator.remove();
			tables.add(new WikiTable(link + format(definition.name)) {
				@Override
				protected void parseDocument(Document document) {
					Elements infobox = document.select(".wikitable.infobox");
					Elements equipment = document.select(".wikitable.smallpadding");
					int idx = 0;

					EquipmentType type = EquipmentType.NOT_WIELDABLE;
					boolean twoHanded = false;
					int[] bonuses = null;

					for (Element info : infobox) {
						JsonObject object = new JsonObject();
						Elements attributes = infobox.select("a");

						String destroyMessage = "Drop";
						double weight = 0;
						boolean tradeable = false;

						for (Element attribute : attributes) {
							String attr = attribute.attr("title");
							try {
								switch (attr) {
								case "Tradeable": {
									tradeable = attribute.parent().nextElementSibling().text().equalsIgnoreCase("Yes");
									break;
								}
								case "Destroy": {
									destroyMessage = attribute.parent().nextElementSibling().text();
									break;
								}
								case "Weight": {
									Element element = attribute.parent().nextElementSibling();
									String alchemy = element.text().replaceAll("[^-\\d.]", "");
									if (!alchemy.matches("-?[0-9]*\\.?[0-9]*")) {
										alchemy = element.children().last().nextSibling().toString()
												.replaceAll("[^-\\d.]", "");
									}
									if (!alchemy.isEmpty())
										weight = Double.parseDouble(alchemy);
									break;
								}
								}
							} catch (Exception ignored) {
								logger.warn("Exception while parsing item [id=" + definition.id + ", name="
										+ definition.name + "]", ignored);
							}
						}

						try {
							if (!equipment.isEmpty()) {
								bonuses = new int[Equipment.SIZE];

								Elements equipmentInfo = equipment.select("td");
								int ind = 0;
								if (equipmentInfo.size() > idx) {
									do {
										Element attribute = equipmentInfo.get(idx++);
										if (attribute.attr("style").equals("text-align: center; width: 35px;")
												|| attribute.attr("style").equals("text-align: center; width: 30px;")) {
											bonuses[ind++ % Equipment.SIZE] = Integer
													.parseInt(attribute.text().replace("%", ""));
										}
									} while (ind < Equipment.SIZE);
								}

								for (Element attribute : equipment.select("img")) {
									switch (attribute.attr("data-image-key")) {
									case "Head_slot.png":
										type = EquipmentType.HELM; // TODO
										break;
									case "Cape_slot.png":
										type = EquipmentType.CAPE;
										break;
									case "Weapon_slot.png":
										type = EquipmentType.WEAPON;
										break;
									case "Neck_slot.png":
										type = EquipmentType.AMULET;
										break;
									case "Ammo_slot.png":
										type = EquipmentType.ARROWS;
										break;
									case "Shield_slot.png":
										type = EquipmentType.SHIELD;
										break;
									case "Torso_slot.png":
										type = EquipmentType.BODY; // TODO
										break;
									case "Legs_slot.png":
										type = EquipmentType.LEGS;
										break;
									case "Gloves_slot.png":
										type = EquipmentType.GLOVES;
										break;
									case "Boots_slot.png":
										type = EquipmentType.BOOTS;
										break;
									case "Ring_slot.png":
										type = EquipmentType.RING;
										break;
									case "2h_slot.png":
										type = EquipmentType.WEAPON;
										twoHanded = true;
										break;
									}
								}
							}
						} catch (Exception ignored) {
							bonuses = null;
							type = EquipmentType.NOT_WIELDABLE;
							logger.warn("Exception while parsing item equipment data [id=" + definition.id + ", name="
									+ definition.name + "]", ignored);
						}

						object.addProperty("name", info.select("caption").text());

						if (!destroyMessage.equals("Drop"))
							object.addProperty("destroy-message", destroyMessage);

						if (tradeable)
							object.addProperty("tradable", true);

						if (twoHanded)
							object.addProperty("two-handed", true);

						if (weight != 0)
							object.addProperty("weight", weight);

						if (type != EquipmentType.NOT_WIELDABLE)
							object.addProperty("equipment-type", type.name());

						if (bonuses != null) {
							for (int index = 0; index < BONUS_CONFIG_FIELD_NAMES.length; index++) {
								String bonusName = BONUS_CONFIG_FIELD_NAMES[index];
								if (bonuses[index] != 0) {
									object.addProperty(bonusName, bonuses[index]);
								}
							}
						}

						if (!table.contains(object)) {
							table.add(object);
						}
					}
				}
			});
		}
		return tables;
	}

	@Override
	protected void finish() {
		JsonArray array = new JsonArray();
		for (WikiTable table : tables) {
			array.addAll(table.getTable());
		}
		writeToJson("wiki_item_dump", array);
	}

	private static String format(String name) {
		name = Utility.capitalizeSentence(name);
		name = name.replace(" ", "_");
		return name;
	}

	private static List<Definition> parseDefinitions() {
		Set<String> added = new HashSet<>();
		List<Definition> definitions = new LinkedList<>();
		Set<String> skip = new HashSet<>();
		Collections.addAll(skip, BAD);
		new GsonParser("wiki/client_item_dump") {
			@Override
			protected void parse(JsonObject data) {
				Definition definition = new Definition();

				definition.id = data.get("id").getAsInt();
				definition.name = data.get("name").getAsString();

				if (added.contains(definition.name))
					return;

				if (skip.contains(format(definition.name)))
					return;

				added.add(definition.name);
				definitions.add(definition);
			}
		}.run();
		skip.clear();
		added.clear();
		return definitions;
	}

	public static final class Definition {
		public int id;
		public String name;
	}

}
