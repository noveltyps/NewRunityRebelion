package plugin.click.item;

import io.server.content.ActivityLog;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.util.Utility;
import io.server.util.chance.Chance;

/**
 * Handles opening the clue scroll
 *
 * @author Daniel
 */
public class ClueScrollPlugin extends PluginContext {

	private static final String[] ITEMS_ANNOUNCED = { "3rd", "ranger", "gilded", "robin", "sandals", "wizard boots",
			"monk", "golden", "wooden", "team cape", "ornamnet", "royal" };

	private enum ClueLevel {
		EASY, MEDIUM, HARD, ELITE
	}

	private static final Item EASY_CLUE = new Item(2677);
	private static final Item MEDIUM_CLUE = new Item(2801);
	private static final Item HARD_CLUE = new Item(2722);
	private static final Item ELITE_CLUE = new Item(12073);
	private static final Chance<Item> EASY = new Chance<>();
	private static final Chance<Item> MEDIUM = new Chance<>();
	private static final Chance<Item> HARD = new Chance<>();
	private static final Chance<Item> ELITE = new Chance<>();

	private void handle(Player player, Item item, ClueLevel level) {
		if (player.inventory.getFreeSlots() < 6) {
			player.message("You need at least 6 free inventory space!");
			return;
		}

		Chance<Item> clue_rewards = getReward(level);

		if (clue_rewards == null)
			return;

		int length = 3 + Utility.random(2);
		Item[] rewards = new Item[length];

		for (int index = 0, count = 0; index < length; index++, count++) {
			Item clue_item = clue_rewards.next();

			if (!clue_item.isStackable() && clue_item.getAmount() > 1) {
				clue_item.setId(clue_item.getNotedId());
			}

			String name = clue_item.getName().toLowerCase();
			rewards[count] = clue_item;

			for (String announcement : ITEMS_ANNOUNCED)
				if (name.contains(announcement))
					World.sendClueMessage(player, item, name);
		}

		player.inventory.remove(item);
		player.inventory.addAll(rewards);
		player.send(new SendItemOnInterface(6963, rewards));
		player.message("<col=EB2A3D>Your " + item.getName().toLowerCase() + " reward value is "
				+ Utility.formatDigits(getValue(rewards)) + " coins!");
		player.interfaceManager.open(6960);

		if (level == ClueLevel.EASY) {
			player.activityLogger.add(ActivityLog.EASY_CLUE);
		} else if (level == ClueLevel.MEDIUM) {
			player.activityLogger.add(ActivityLog.MEDIUM_CLUE);
		} else if (level == ClueLevel.HARD) {
			player.activityLogger.add(ActivityLog.HARD_CLUE);
		} else if (level == ClueLevel.ELITE) {
			player.activityLogger.add(ActivityLog.ELITE_CLUE);
		}
	}

	@Override
	protected boolean firstClickItem(Player player, ItemClickEvent event) {
		Item item = event.getItem();

		if (item.getId() == EASY_CLUE.getId()) {
			handle(player, item, ClueLevel.EASY);
			return true;
		}
		if (item.getId() == MEDIUM_CLUE.getId()) {
			handle(player, item, ClueLevel.MEDIUM);
			return true;
		}
		if (item.getId() == HARD_CLUE.getId()) {
			handle(player, item, ClueLevel.HARD);
			return true;
		}
		if (item.getId() == ELITE_CLUE.getId()) {
			handle(player, item, ClueLevel.ELITE);
			return true;
		}

		return false;
	}

	private int getValue(Item... items) {
		int value = 0;
		for (Item item : items) {
			value += item.getValue();
		}
		return value;
	}

	private Chance<Item> getReward(ClueLevel level) {
		switch (level) {
		case EASY:
			return EASY;
		case MEDIUM:
			return MEDIUM;
		case HARD:
			return HARD;
		case ELITE:
			return ELITE;
		}
		return null;
	}

	public static void declare() {
		EASY.add(15, new Item(12205)); // Bronze platebody (g)
		EASY.add(15, new Item(12207)); // Bronze platelegs (g)
		EASY.add(15, new Item(12209)); // Bronze plateskirt (g)
		EASY.add(15, new Item(12211)); // Bronze full helm (g)
		EASY.add(15, new Item(12213)); // Bronze kiteshield (g)
		EASY.add(15, new Item(12215)); // Bronze platebody (t)
		EASY.add(15, new Item(12217)); // Bronze platelegs (t)
		EASY.add(15, new Item(12219)); // Bronze plateskirt (t)
		EASY.add(15, new Item(12221)); // Bronze full helm (t)
		EASY.add(15, new Item(12223)); // Bronze kiteshield (t)
		EASY.add(15, new Item(12225)); // Iron platebody (t)
		EASY.add(15, new Item(12227)); // Iron platelegs (t)
		EASY.add(15, new Item(12229)); // Iron plateskirt (t)
		EASY.add(15, new Item(12231)); // Iron full helm (t)
		EASY.add(15, new Item(12233)); // Iron kiteshield (t)
		EASY.add(15, new Item(12235)); // Iron platebody (g)
		EASY.add(15, new Item(12237)); // Iron platelegs (g)
		EASY.add(15, new Item(12239)); // Iron plateskirt (g)
		EASY.add(15, new Item(12241)); // Iron full helm (g)
		EASY.add(15, new Item(12243)); // Iron kiteshield (g)
		EASY.add(15, new Item(2583)); // Black platebody (t)
		EASY.add(15, new Item(2585)); // Black platelegs (t)
		EASY.add(15, new Item(2587)); // Black full helm (t)
		EASY.add(15, new Item(2589)); // Black kiteshield (t)
		EASY.add(15, new Item(2591)); // Black platebody (g)
		EASY.add(15, new Item(2593)); // Black platelegs (g)
		EASY.add(15, new Item(2595)); // Black full helm (g)
		EASY.add(15, new Item(2597)); // Black kiteshield (g)
		EASY.add(15, new Item(3472)); // Black plateskirt (t)
		EASY.add(15, new Item(3473)); // Black plateskirt (g)
		EASY.add(45, new Item(2635)); // Black beret
		EASY.add(45, new Item(2637)); // White beret
		EASY.add(45, new Item(12247)); // Red beret
		EASY.add(45, new Item(2633)); // Blue beret
		EASY.add(200, new Item(2631)); // Highwayman mask
		EASY.add(25, new Item(12245)); // Beanie
		EASY.add(15, new Item(7386)); // Blue skirt (g)
		EASY.add(15, new Item(7390)); // Blue wizard robe (g)
		EASY.add(15, new Item(7394)); // Blue wizard hat (g)
		EASY.add(15, new Item(7396)); // Blue wizard hat (t)
		EASY.add(15, new Item(7388)); // Blue skirt (t)
		EASY.add(15, new Item(7392)); // Blue wizard robe (t)
		EASY.add(15, new Item(12449)); // Black wizard robe (g)
		EASY.add(15, new Item(12453)); // Black wizard hat (g)
		EASY.add(15, new Item(12445)); // Black skirt (g)
		EASY.add(15, new Item(12447)); // Black skirt (t)
		EASY.add(15, new Item(12451)); // Black wizard robe (t)
		EASY.add(15, new Item(12455)); // Black wizard hat (t)
		EASY.add(25, new Item(10404)); // Red elegant shirt
		EASY.add(25, new Item(10406)); // Red elegant legs
		EASY.add(25, new Item(10424)); // Red elegant blouse
		EASY.add(25, new Item(10426)); // Red elegant skirt
		EASY.add(25, new Item(10412)); // Green elegant shirt
		EASY.add(25, new Item(10414)); // Green elegant legs
		EASY.add(25, new Item(10432)); // Green elegant blouse
		EASY.add(25, new Item(10434)); // Green elegant skirt
		EASY.add(25, new Item(10408)); // Blue elegant shirt
		EASY.add(25, new Item(10410)); // Blue elegant legs
		EASY.add(25, new Item(10428)); // Blue elegant blouse
		EASY.add(25, new Item(10430)); // Blue elegant skirt
		EASY.add(10, new Item(12375)); // Black cane
		EASY.add(.99, new Item(20217)); // Team Cape i
		EASY.add(.99, new Item(20211)); // Team Cape zero
		EASY.add(.99, new Item(20214)); // Team Cape x



		MEDIUM.add(15, new Item(12293)); // Mithril full helm (t)
		MEDIUM.add(15, new Item(12287)); // Mithril platebody (t)
		MEDIUM.add(15, new Item(12289)); // Mithril platelegs (t)
		MEDIUM.add(15, new Item(12291)); // Mithril kiteshield (t)
		MEDIUM.add(15, new Item(12295)); // Mithril plateskirt (t)
		MEDIUM.add(15, new Item(12283)); // Mithril full helm (g)
		MEDIUM.add(15, new Item(12277)); // Mithril platebody (g)
		MEDIUM.add(15, new Item(12285)); // Mithril plateskirt (g)
		MEDIUM.add(15, new Item(12279)); // Mithril platelegs (g)
		MEDIUM.add(15, new Item(12281)); // Mithril kiteshield (g)
		MEDIUM.add(15, new Item(2605)); // Adamant full helm (t)
		MEDIUM.add(15, new Item(3474)); // Adamant plateskirt (t)
		MEDIUM.add(15, new Item(2603)); // Adamant kiteshield (t)
		MEDIUM.add(15, new Item(2599)); // Adamant platebody (t)
		MEDIUM.add(15, new Item(2601)); // Adamant platelegs (t)
		MEDIUM.add(15, new Item(2607)); // Adamant platebody (g)
		MEDIUM.add(15, new Item(2609)); // Adamant platelegs (g)
		MEDIUM.add(15, new Item(2611)); // Adamant kiteshield (g)
		MEDIUM.add(15, new Item(2613)); // Adamant full helm (g)
		MEDIUM.add(15, new Item(3475)); // Adamant plateskirt (g)
		MEDIUM.add(.99, new Item(2577)); // Ranger boots
		MEDIUM.add(.99, new Item(12598)); // Holy sandals
		MEDIUM.add(25, new Item(10400)); // Black elegant shirt
		MEDIUM.add(25, new Item(10402)); // Black elegant legs
		MEDIUM.add(25, new Item(10416)); // Purple elegant shirt
		MEDIUM.add(25, new Item(10418)); // Purple elegant legs
		MEDIUM.add(25, new Item(12315)); // Pink elegant shirt
		MEDIUM.add(25, new Item(12317)); // Pink elegant legs
		MEDIUM.add(25, new Item(12339)); // Pink elegant blouse
		MEDIUM.add(25, new Item(12341)); // Pink elegant skirt
		MEDIUM.add(25, new Item(12343)); // Gold elegant blouse
		MEDIUM.add(25, new Item(12345)); // Gold elegant skirt
		MEDIUM.add(25, new Item(12347)); // Gold elegant shirt
		MEDIUM.add(25, new Item(12349)); // Gold elegant legs
		MEDIUM.add(25, new Item(10436)); // Purple elegant blouse
		MEDIUM.add(25, new Item(10438)); // Purple elegant skirt
		MEDIUM.add(25, new Item(10420)); // White elegant blouse
		MEDIUM.add(25, new Item(10422)); // White elegant skirt
		MEDIUM.add(10, new Item(12377)); // Adamant cane
		MEDIUM.add(35, new Item(10364)); // Strength amulet (t)
		MEDIUM.add(20, new Item(10446)); // Saradomin cloak
		MEDIUM.add(35, new Item(12361)); // Cat mask
		MEDIUM.add(35, new Item(12428)); // Penguin mask
		MEDIUM.add(35, new Item(12359)); // Leprechaun hat
		MEDIUM.add(75, new Item(20260)); // Piscarilius Banner
		MEDIUM.add(75, new Item(20257)); // Lovakengj Banner
		MEDIUM.add(75, new Item(20251)); // Arceuus Banner
		MEDIUM.add(75, new Item(20263)); // Shayzien Banner
		MEDIUM.add(75, new Item(20254)); // Hosidius Banner



		HARD.add(5, new Item(12526)); // Fury ornament kit
		HARD.add(5, new Item(12532)); // Dragon sq shield ornament kit
		HARD.add(5, new Item(12534)); // Dragon chainbody ornament kit
		HARD.add(5, new Item(12536)); // Dragon plate/skirt ornament kit
		HARD.add(5, new Item(12538)); // Dragon full helm ornament kit
		HARD.add(5, new Item(12528)); // Dark infinity colour kit
		HARD.add(5, new Item(12530)); // Light infinity colour kit
		HARD.add(.99, new Item(3481)); // Gilded platebody
		HARD.add(.99, new Item(3483)); // Gilded platelegs
		HARD.add(.99, new Item(3485)); // Gilded plateskirt
		HARD.add(.99, new Item(3486)); // Gilded full helm
		HARD.add(.99, new Item(3488)); // Gilded kiteshield
		HARD.add(.99, new Item(12389)); // Gilded scimitar
		HARD.add(.99, new Item(12391)); // Gilded boots
		HARD.add(65, new Item(7398)); // Enchanted robe
		HARD.add(65, new Item(7399)); // Enchanted top
		HARD.add(65, new Item(7400)); // Enchanted hat
		HARD.add(1, new Item(2581)); // Robin hood hat
		HARD.add(45, new Item(2651)); // Pirate's hat
		HARD.add(.1, new Item(10346)); // 3rd age platelegs
		HARD.add(.1, new Item(10348)); // 3rd age platebody
		HARD.add(.1, new Item(10350)); // 3rd age full helmet
		HARD.add(.1, new Item(10352)); // 3rd age kiteshield
		HARD.add(.1, new Item(10330)); // 3rd age range top
		HARD.add(.1, new Item(10332)); // 3rd age range legs
		HARD.add(.1, new Item(10334)); // 3rd age range coif
		HARD.add(.1, new Item(10336)); // 3rd age vambraces
		HARD.add(.1, new Item(10338)); // 3rd age robe top
		HARD.add(.1, new Item(10340)); // 3rd age robe
		HARD.add(.1, new Item(10342)); // 3rd age mage hat
		HARD.add(.1, new Item(10344)); // 3rd age amulet

		

	
		ELITE.add(50, new Item(9194, 50)); // Onyx Bolt Tips
		ELITE.add(125, new Item(5317)); // Spirit Seed
		ELITE.add(125, new Item(5316)); // Magic Seed
		ELITE.add(125, new Item(5315)); // Yew Seed
		ELITE.add(25, new Item(12526)); // Fury ornament kit
		ELITE.add(25, new Item(12528)); // Dark infinity colour kit
		ELITE.add(25, new Item(12530)); // Light infinity colour kit
		ELITE.add(25, new Item(12532)); // Dragon sq shield ornament kit
		ELITE.add(25, new Item(12534)); // Dragon chainbody ornament kit
		ELITE.add(25, new Item(12536)); // Dragon legs/skirt ornament kit
		ELITE.add(25, new Item(12538)); // Dragon full helm ornament kit
		ELITE.add(25, new Item(12335)); // Briefcase
		ELITE.add(25, new Item(12337)); // Sagacious spectacles
		ELITE.add(25, new Item(12339)); // Pink elegant blouse
		ELITE.add(25, new Item(12341)); // Pink elegant skirt
		ELITE.add(25, new Item(12343)); // Gold elegant blouse
		ELITE.add(25, new Item(12345)); // Gold elegant skirt
		ELITE.add(25, new Item(12347)); // Gold elegant shirt
		ELITE.add(25, new Item(12349)); // Gold elegant legs
		ELITE.add(25, new Item(12351)); // Musketeer hat
		ELITE.add(25, new Item(12371)); // Lava dragon mask
		ELITE.add(25, new Item(12373)); // Dragon cane
		ELITE.add(4, new Item(12393)); // Royal gown top
		ELITE.add(4, new Item(12395)); // Royal gown bottom
		ELITE.add(4, new Item(12397)); // Royal crown
		ELITE.add(1, new Item(12389)); // Ranger's tunic
		ELITE.add(8, new Item(19958)); // Dark tuxedo jacket
		ELITE.add(8, new Item(19961)); // Dark tuxedo cuffs
		ELITE.add(8, new Item(19964)); // Dark trousers
		ELITE.add(8, new Item(19967)); // Dark tuxedo shoes
		ELITE.add(8, new Item(19970)); // Dark bow tie
		ELITE.add(8, new Item(19973)); // Light tuxedo jacket
		ELITE.add(8, new Item(19976)); // Light tuxedo cuffs
		ELITE.add(8, new Item(19979)); // Light trousers
		ELITE.add(8, new Item(19982)); // Light tuxedo shoes
		ELITE.add(8, new Item(19985)); // Light bow tie
		ELITE.add(5, new Item(19988)); // Blacksmith's helm
		ELITE.add(5, new Item(19991)); // Bucket helm
		ELITE.add(5, new Item(19994)); // Ranger gloves
		ELITE.add(5, new Item(19997)); // Holy wraps
		ELITE.add(5, new Item(19941)); // Heavy casket
		ELITE.add(.01, new Item(12422)); // 3rd age wand
		ELITE.add(.01, new Item(12424)); // 3rd age bow
		ELITE.add(.01, new Item(12426)); // 3rd age longsword
		ELITE.add(.01, new Item(12437)); // 3rd age cloak
		ELITE.add(.001, new Item(20011)); // 3rd age axe
		ELITE.add(.001, new Item(20014)); // 3rd age pickaxe
	}
}
