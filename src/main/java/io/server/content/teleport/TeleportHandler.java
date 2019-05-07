package io.server.content.teleport;

import java.util.ArrayList;
import java.util.List;

import io.server.Config;
import io.server.content.activity.impl.barrows.Barrows;
import io.server.content.activity.impl.kraken.KrakenActivity;
import io.server.content.activity.impl.warriorguild.WarriorGuild;
import io.server.content.activity.impl.zulrah.ZulrahActivity;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.skill.impl.magic.teleport.TeleportType;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendFadeScreen;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

/**
 * Handles teleporting to various locations around RebelionX.
 *
 * @author Daniel
 * @edit Adam/adameternal123
 */
public class TeleportHandler {

	/** Holds the teleport titles names. */
	private static final String[] TITLES = { "Favorites", "Minigames", "Skilling", "Monster Killing", "Player Killing",
			"Bosses" };

	/** Opens the teleport itemcontainer. */
	public static void open(Player player) {
		open(player, TeleportType.FAVORITES, 0);
	}

	/** Opens the teleport itemcontainer. */
	public static void open(Player player, TeleportType type) {
		open(player, type, 0);
	}

	/** Opens the teleport itemcontainer to a certain teleportNoChecks type. */
	public static void open(Player player, TeleportType type, int teleportIndex) {
		List<Teleport> teleports = type == TeleportType.FAVORITES ? player.favoriteTeleport : getTeleports(type);
		player.attributes.set("TELEPORT_TYPE_KEY", type);
		if (player.attributes.get("TELEPORT_INDEX_KEY", Integer.class) == null)
			player.attributes.set("TELEPORT_INDEX_KEY", 0);
		int size = teleports.size();
		for (int index = 0, string = 58009; index < 6; index++) {
			String color = "<col=" + (type.ordinal() == index ? "ffffff" : "ff9933") + ">";
			player.send(new SendString(color + TITLES[index], string));
			string += 4;
		}
		for (int index = 0, string = 58052; index < (size < 9 ? 9 : size); index++, string += 2) {
			if (index >= size) {
				player.send(new SendString("", string));
				continue;
			}
			Teleport teleport = teleports.get(index);
			String prefex = teleportIndex == index ? "<col=ffffff>" : "</col>";
			String favorite = player.favoriteTeleport.contains(teleport) ? "<clan=6> " : "";
			try {
				player.send(new SendString(favorite + prefex + teleport.getName(), string));
			} catch (Exception e) {
				System.out
						.println("Something is null and keeps breaking when you open Favorites in teleport interface");
			}
		}
		if (type == TeleportType.FAVORITES && player.favoriteTeleport.isEmpty()) {
			display(player, null);
		} else {
			display(player, teleports.get(teleportIndex));
		}
		player.send(new SendScrollbar(58050, size <= 9 ? 225 : (size * 25)));
		player.interfaceManager.open(58000);
	}

	/**
	 * Displays all the teleport text on the itemcontainer.
	 *
	 * @param player   The player viewing the teleport text.
	 * @param teleport The teleport type.
	 */
	public static void display(Player player, Teleport teleport) {
		player.attributes.set("TELEPORT", teleport);
		player.send(new SendConfig(348, player.favoriteTeleport.contains(teleport) ? 0 : 1));

		if (teleport != null) {
			Item items[] = new Item[3];
			for (int index = 0, count = 0; index < teleport.getDisplay().length; index++, count++) {
				if (teleport.getDisplay()[index] == -1) {
					items[count] = null;
					continue;
				}
				Item item = new Item(teleport.getDisplay()[index]);
				if (item.isStackable())
					item.setAmount(50000);
				items[count] = item;
			}
			player.send(new SendItemOnInterface(58041, items));
		}

		if (teleport != null && teleport.getStrings()[0].length() == 0 && teleport.getStrings()[1].length() == 0) {
			player.send(new SendString(teleport.getName(), 58031));
			player.send(new SendString("", 58032));
			player.send(new SendString("", 58033));
			return;
		} else if (teleport == null) {
			player.send(new SendString("", 58031));
			player.send(new SendString("You do not have any teleport selected", 58032));
			player.send(new SendString("", 58033));
			player.send(new SendItemOnInterface(58041));
			return;
		}

		player.send(new SendString(teleport.getName(), 58031));
		player.send(new SendString("<col=ff7000>" + teleport.getStrings()[0], 58032));
		player.send(new SendString("<col=ff7000>" + teleport.getStrings()[1], 58033));
	}

	/**
	 * Handles clicking teleport buttons on the itemcontainer.
	 *
	 * @param player The player clicking the buttons.
	 * @param button The button identification.
	 */
	public static void click(Player player, int button) {
		TeleportType type = player.attributes.get("TELEPORT_TYPE_KEY", TeleportType.class);
		List<Teleport> teleports = type == TeleportType.FAVORITES ? player.favoriteTeleport : getTeleports(type);
		int index = getOrdinal(button);
		player.attributes.set("TELEPORT_INDEX_KEY", index);
		open(player, type, index);
		display(player, teleports.get(index));
	}

	/**
	 * Handles teleporting to the destination.
	 *
	 * @param player The player teleporting.
	 */
	public static void teleport(Player player) {
		if (player.wilderness > 20 && !PlayerRight.isPriviledged(player)) {
			player.send(new SendMessage("You can't teleport past 20 wilderness!"));
			return;
		}

		Teleport teleport = player.attributes.get("TELEPORT", Teleport.class);
		if (teleport == null) {
			player.send(new SendMessage("You have not selected a destination to teleport to!"));
			return;
		}
		if (teleport.isSpecial()) {
			special(player, teleport);
			return;
		}

		if (!teleport.getcustomsAllowed() == true && player.inventory.containsAny(Config.CUSTOM_ITEMS)) {
			player.message("@red@You are not allowed to bring in custom items " + player.getName() + "!");
			return;
		}
		if (!teleport.getcustomsAllowed() == true && player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
			player.message(
					"@red@You are not allowed to bring in custom items into this area " + player.getName() + "!");
			return;
		}

		Teleportation.teleport(player, teleport.getPosition());
		player.send(new SendMessage("You have teleported to " + teleport.getName() + "."));
	}

	/** Handles favorite a teleport. */
	public static void favorite(Player player) {
		Teleport teleport = player.attributes.get("TELEPORT", Teleport.class);
		if (teleport == null) {
			player.send(new SendMessage("You have not selected a teleport to favorite!"));
			player.send(new SendConfig(348, 1));
			return;
		}
		boolean isFavorite = player.favoriteTeleport.contains(teleport);
		int index = player.attributes.get("TELEPORT_INDEX_KEY", Integer.class);
		if (index == -1) {
			index = 0;
		}

		if (isFavorite) {
			player.favoriteTeleport.remove(teleport);
			index = 0;
		} else {
			player.favoriteTeleport.add(teleport);
		}

		isFavorite = player.favoriteTeleport.contains(teleport);
		player.send(new SendConfig(348, isFavorite ? 0 : 1));
		player.send(new SendMessage(
				"You have " + (isFavorite ? "" : "un-") + "favorited the " + teleport.getName() + " teleport."));
		TeleportType type = player.attributes.get("TELEPORT_TYPE_KEY", TeleportType.class);
		open(player, type, index);
	}

	/** Handles special case TELEPORT. */
	public static void special(Player player, Teleport teleport) {
		DialogueFactory factory = player.dialogueFactory;

		switch (teleport) {
		case AGILITY:
			factory.sendOption("Gnome agility course", () -> {
				Teleportation.teleport(player, new Position(2480, 3437, 0));
				player.send(new SendMessage("You have teleported to the Gnome agility course."));
			}, "Barbarian agility course", () -> {
				Teleportation.teleport(player, new Position(2546, 3551, 0));
				player.send(new SendMessage("You have teleported to the Barbarian agility course."));
			}, "Wilderness agility course", () -> {
					Teleportation.teleport(player, new Position(2998, 3915, 0));
					player.send(new SendMessage("You have teleported to the Wilderness agility course."));
			}, "Rooftop courses", () -> {
				factory.sendStatement("Loading").sendOption("Seer's Village rooftop course", () -> {
					Teleportation.teleport(player, new Position(2729, 3488, 0));
					player.send(new SendMessage("You have teleported to the Seer's Village rooftop agility course."));
				}, "Ardougne rooftop course", () -> {
					Teleportation.teleport(player, new Position(2674, 3297, 0));
					player.send(new SendMessage("You have teleported to the Ardougne rooftop agility course."));
				}).execute();
			}, "Nevermind", factory::clear).execute();
			break;
		case MINING:
			factory.sendOption("Varrock", () -> {
				Teleportation.teleport(player, new Position(3285, 3365, 0));
				player.send(new SendMessage("You have teleported to the varrock mining area."));
			}, "Falador", () -> {
				Teleportation.teleport(player, new Position(3044, 9785, 0));
				player.send(new SendMessage("You have teleported to the falador mining area."));
			}, "Rune Essence", () -> {
				Teleportation.teleport(player, new Position(2910, 4832, 0));
				player.send(new SendMessage("You have teleported to the rune essence mining area."));
			}, "Shilo Village", () -> {
				Teleportation.teleport(player, new Position(2826, 2997, 0));
				player.send(new SendMessage("You have teleported to the shilo village mining area."));
			}, "Nevermind", factory::clear).execute();
			break;
		case WOODCUTTING:
			factory.sendOption("Camelot", () -> {
				Teleportation.teleport(player, new Position(2724, 3475, 0));
				player.send(new SendMessage("You have teleported to the camelot woodcutting area."));
			}, "Woodcutting Guild", () -> {
				Teleportation.teleport(player, new Position(1587, 3488, 0));
				player.send(new SendMessage("You have teleported to the woodcutting guild."));
			}, "Nevermind", factory::clear).execute();
			break;
		case HUNTER:
			factory.sendOption("Puro Puro", () -> {
				Position[] teleports = { new Position(2619, 4292), new Position(2564, 4292), new Position(2564, 4347),
						new Position(2619, 4347) };
				Teleportation.teleport(player, Utility.randomElement(teleports));
				player.send(new SendMessage("You have teleported to the puro puro hunter area."));
			}, "Boxed Butterflys", () -> {
				 Teleportation.teleport(player, new Position(3166, 3307, 0));
				player.send(new SendMessage("You have teleported to the Boxed Butterflys area."));
			}, "Nevermind", factory::clear).execute();
			break;

        case KRAKEN:
            Teleportation.teleport(player, new Position(2280, 10031, 0), 20, () -> KrakenActivity.create(player));
            break;
        case WARRIOR_GUILD:
            Teleportation.teleport(player, new Position(2846, 3541, 0), 20, () -> WarriorGuild.create(player));
            break;
        case BARROWS:
            Teleportation.teleport(player, new Position(3565, 3315, 0), 20, () -> Barrows.create(player));
            break;
        case KURASK:
        	if(player.prestige.totalPrestige <= 79) {
        		player.message("You need a total of 80 prestige's to enter this boss!");
        		break;
        	} else {
        		Teleportation.teleport(player, (new Position(3061, 5485, 0)));
        		player.message("@red@ Welcome to Kurask! I'd recommend a group to take this beast down!");
        	}
        	break;
        case ZULRAH:
            if (player.isTeleblocked()) {
                player.message("You are currently under the affects of a teleblock spell and can not teleport!");
                break;
            }

            player.locking.lock();
            player.send(new SendFadeScreen("You are teleporting to Zulrah's shrine...", 1, 3));
            World.schedule(5, () -> {
                player.move(new Position(2268, 3069, 0));
                ZulrahActivity.create(player);
                player.locking.unlock();
            });
            break;
		default:
			break;

		}
	}

	/** Gets a list of teleports based off the teleport type. */
	private static List<Teleport> getTeleports(TeleportType type) {
		List<Teleport> list = new ArrayList<>();
		for (Teleport t : Teleport.values()) {
			if (t.getType() == type)
				list.add(t);
		}
		return list;
	}

	/** Gets the ordinal of a teleport based on the list ordinal. */
	private static int getOrdinal(int button) {
		int base_button = -7484;
		int ordinal = Math.abs((base_button - button) / 2);
		return ordinal;
	}
}
