package io.server.content.staff;

import java.util.ArrayList;
import java.util.List;

import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendTooltip;
import io.server.util.MessageColor;
import io.server.util.Utility;

/**
 * Handles the staff panel. FIXME THIS CLASS NEEDS TO BE REWRITTEN DANIEL GOD
 * DAMN IT
 * 
 * @author Daniel
 */
public class StaffPanel {
	public static void open(Player player, PanelType panel) {
		if (!PlayerRight.isPriviledged(player) && panel == PanelType.DEVELOPER_PANEL) {
			panel = PanelType.INFORMATION_PANEL;
			player.send(new SendMessage("You do not have permission to access this panel."));
		}

		update(player, panel);
		player.interfaceManager.open(panel.getIdentification());
	}

	/** Updates the active member list on the itemcontainer. */
	public static void members(Player player) {
		List<String> user_list = new ArrayList<>();

		for (Player p : World.getPlayers()) {
			if (!p.isBot)
				user_list.add(p.getName());
		}

		Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
		int size = user_list.size() < 16 ? 16 : user_list.size();

		for (int i = 0; i < size; i++) {
			boolean invalid = i >= user_list.size();
			boolean selected = other != null && (!invalid && other.getName().equalsIgnoreCase(user_list.get(i)));
			String name = "";

			if (!invalid && World.search(user_list.get(i)).isPresent()) {
				Player member = World.search(user_list.get(i)).get();

				name = PlayerRight.getCrown(member) + " " + member.getName();
			}
			player.send(new SendString(invalid ? "" : (selected ? "<col=ffffff>" : "") + name, 36731 + i));
			player.send(new SendTooltip(invalid ? "" : "View information of <col=FFB83F>" + name, 36731 + i));
		}

		player.attributes.set("STAFF_PANEL_KEY", user_list);
		player.send(new SendScrollbar(36730, size * 16));
		player.send(new SendString("</col>Players online: <col=FFB83F>" + (World.getPlayerCount()), 36713));
	}

	/** Handles searching for a player on the staff itemcontainer. */
	public static void search(Player player, String context) {
		if (!World.search(context).isPresent()) {
			player.send(new SendMessage("There was no active player found by the name of " + context + ".",
					MessageColor.DARK_BLUE));
			return;
		}

		player.attributes.set("PLAYER_PANEL_KEY", World.search(context).get());
		open(player, PanelType.INFORMATION_PANEL);
	}

	/** Handles clicking buttons on the staff panel. */
	@SuppressWarnings("unchecked")
	public static boolean click(Player player, int button) {
		int base_button = -28805;
		int modified_button = (base_button - button);
		int index = Math.abs(modified_button);
		List<String> user_list = player.attributes.get("STAFF_PANEL_KEY", List.class);

		if (user_list == null || index >= user_list.size()) {
			return false;
		}

		if (!World.search(user_list.get(index)).isPresent()) {
			members(player);
			player.send(new SendMessage("This player was not valid. List was refreshed with active gameMembers.",
					MessageColor.DARK_BLUE));
			return true;
		}

		player.attributes.set("PLAYER_PANEL_KEY", World.search(user_list.get(index)).get());
		update(player, PanelType.INFORMATION_PANEL);
		return true;
	}

	/** Updates the itemcontainer based on the panel type. */
	public static void update(Player player, PanelType panel) {
		members(player);
		switch (panel) {
		case INFORMATION_PANEL:
			player.send(new SendConfig(374, panel.ordinal()));
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			boolean nulled = other == null;
			boolean valid = !nulled && (player.right.equals(PlayerRight.OWNER)
					|| (PlayerRight.isPriviledged(player) && !other.right.equals(PlayerRight.OWNER)));
			player.send(new SendString(nulled ? "" : "Username: <col=FFB83F>" + Utility.formatName(other.getName()),
					37001));
			player.send(
					new SendString(
							nulled ? ""
									: "Password: <col=FFB83F>"
											+ (valid ? other.getPassword() : "You do not have sufficient permission"),
							37002));
			player.send(
					new SendString(
							nulled ? ""
									: "IP Address: <col=FFB83F>"
											+ (valid ? other.lastHost : "You do not have sufficient permission"),
							37003));
			player.send(new SendString(
					nulled ? "" : "Rank: <col=FFB83F>" + PlayerRight.getCrown(other) + " " + other.right.getName(),
					37004));
			player.send(new SendString(
					nulled ? ""
							: "Networth: <col=FFB83F>" + Utility.formatDigits(other.playerAssistant.networth()) + "gp",
					37005));
			player.send(new SendString(nulled ? "" : "Donation Points :" + Utility.formatDigits(other.donation.getCredits()), 37006));
			player.send(new SendString(
					nulled ? "" : "Refferals Points: <col=FFB83F>" + Utility.formatDigits(other.getReferralPoints()),
					37007));
			player.send(new SendString(
					nulled ? "" : "Total Refferals: <col=FFB83F>" + Utility.formatDigits(other.getTotalRefferals()),
					37008));
			player.send(new SendString(nulled ? "" : other.created, 36716));
			return;
		case ACTION_PANEL:
			player.send(new SendConfig(374, panel.ordinal()));
			int action_string = 36514;
			for (StaffAction action : StaffAction.values()) {
				player.send(new SendTooltip("Select <col=FFB83F>" + action.getName(), action_string - 3));
				player.send(new SendString(action.getName(), action_string));
				action_string += 4;
			}
			return;
		case DEVELOPER_PANEL:
			player.send(new SendConfig(374, panel.ordinal()));
			int magic_string = 36314;
			for (DeveloperAction action : DeveloperAction.values()) {
				player.send(new SendTooltip("Select <col=FFB83F>" + action.getName(), magic_string - 3));
				player.send(new SendString(action.getName(), magic_string));
				magic_string += 4;
			}
			return;
		}
	}
}
