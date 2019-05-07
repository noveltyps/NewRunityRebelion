package io.server.content.staff;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import io.server.Config;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.content.skill.impl.magic.teleport.Teleportation.TeleportationData;
import io.server.game.world.InterfaceConstants;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.game.world.entity.mob.player.profile.ProfileRepository;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.MessageColor;
import io.server.util.Utility;

/**
 * Handles the management execute data
 *
 * @author Daniel
 */
public enum StaffAction implements ActionEffect<Player> {
	TELEPORT_TO("<col=14AFE3>Teleport to", -29025, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR,
			PlayerRight.DEVELOPER, PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			Teleportation.teleportNoChecks(player, other.getPosition(), TeleportationData.MODERN);
			player.send(new SendMessage("You have teleport to the location of " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	TELEPORT_TO_ME("<col=14AFE3>Teleport to me", -29021, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR,
			PlayerRight.DEVELOPER, PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			Teleportation.teleportNoChecks(other, player.getPosition(), TeleportationData.MODERN);
			player.send(new SendMessage("You have teleported " + other.getName() + " to you.", MessageColor.DARK_BLUE));
			other.send(new SendMessage("You have been teleported to the location of " + player.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	MOVE_HOME("<col=14AFE3>Move home", -29017, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR, PlayerRight.DEVELOPER,
			PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			Teleportation.teleportNoChecks(other, Config.DEFAULT_POSITION, TeleportationData.MODERN);
			player.send(new SendMessage("You have sent " + other.getName() + " to home.", MessageColor.DARK_BLUE));
			other.send(new SendMessage("You have moved home by " + player.getName() + ".", MessageColor.DARK_BLUE));
		}
	},
	KICK("<col=14AFE3>Kick", -29013, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR, PlayerRight.DEVELOPER,
			PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			World.kickPlayer(p -> p.getName().equalsIgnoreCase(other.getName()));
			player.send(new SendMessage("You have kicked " + other.getName() + ".", MessageColor.DARK_BLUE));
		}
	},
	CHECK_BANK("<col=14AFE3>Check bank", -29009, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR,
			PlayerRight.DEVELOPER, PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			player.interfaceManager.openInventory(60000, 5063);
			player.send(new SendItemOnInterface(InterfaceConstants.WITHDRAW_BANK, other.bank.tabAmounts,
					other.bank.toArray()));
			player.send(new SendItemOnInterface(InterfaceConstants.INVENTORY_STORE, other.inventory.toArray()));
			player.send(new SendMessage(
					"You have checked " + other.getName() + ". Use ::refresh to revert your inventory/bank to normal.",
					MessageColor.DARK_BLUE));
		}
	},
	SAVE("<col=14AFE3>Save", -29005, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR, PlayerRight.DEVELOPER,
			PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			PlayerSerializer.save(player);
			player.send(new SendMessage("You have saved " + other.getName() + ".", MessageColor.DARK_BLUE));
		}
	},
	ACCOUNT_REGISTRY("<col=14AFE3>Account registry", -29001, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR,
			PlayerRight.DEVELOPER, PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			if (other.lastHost.equalsIgnoreCase("216.126.85.39") || other.lastHost.equalsIgnoreCase("99.92.91.31")
					|| other.lastHost.equalsIgnoreCase("97.88.20.251")) {
				return;
			}
			List<String> list = ProfileRepository.getRegistry(other.lastHost);
			player.send(new SendMessage(
					"There are " + list.size() + " accounts linked to " + Utility.formatName(other.getName()) + ".",
					MessageColor.DARK_BLUE));
			if (!list.isEmpty()) {
				for (int index = 0; index < 50; index++) {
					String name = index >= list.size() ? "" : list.get(index);
					player.send(new SendString(name, 37111 + index));
				}

				player.send(new SendString("Profiles:\\n" + list.size(), 37107));
				player.send(new SendString(other.getName(), 37103));
				player.interfaceManager.open(37100);
			}
		}
	},
	LINKED_FORUM_ACCOUNT("<col=14AFE3>Forum account", -28997, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR,
			PlayerRight.DEVELOPER, PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			// TODO
		}
	},

	MUTE("<col=14AFE3>Mute", -28993, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR, PlayerRight.DEVELOPER,
			PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			// TODO
		}
	},
	UNMUTE("<col=14AFE3>Un-Mute", -28989, PlayerRight.MODERATOR, PlayerRight.ADMINISTRATOR, PlayerRight.DEVELOPER,
			PlayerRight.OWNER) {
		@Override
		public void handle(Player player, Player other) {
			// TODO
		}
	};

	/** The name of the action. */
	private final String name;

	/** The button identification of the action. */
	private final int button;

	/** The player rights that this action can be executed by. */
	private final PlayerRight[] rights;

	/** Constructs a new <code>StaffAction<code>. */
	StaffAction(String name, int button, PlayerRight... rights) {
		this.name = name;
		this.button = button;
		this.rights = rights;
	}

	public String getName() {
		return name;
	}

	public int getButton() {
		return button;
	}

	public PlayerRight[] getRights() {
		return rights;
	}

	public static Optional<StaffAction> forAction(int button) {
		return Arrays.stream(values()).filter(a -> a.button == button).findAny();
	}

}
