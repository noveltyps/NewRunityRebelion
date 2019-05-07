package io.server.game.world.entity.combat.magic;

import io.server.Config;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.MessageColor;
import io.server.util.StringUtils;

public class Autocast {
	private final static int[] ANCIENT_STAFFS = { 21006, 4675, 4710, 11791, 12904, 6914, 12422, 1409, 6562, 3053 };
	private final static int[] NO_AUTOCAST = { 12899, 11907, 11908 };

	public static boolean clickButton(Player player, int button) {
		switch (button) {
		case 2004:
		case 6161:
			player.interfaceManager.setSidebar(Config.ATTACK_TAB, 328);
			return true;
		case 24111:
		case 349:
			if (!player.isAutocast()) {
				Item weapon = player.equipment.getWeapon();

				if (weapon != null) {
					sendSelectionInterface(player, weapon.getId());
				}
			} else {
				reset(player);
			}
			return true;

		case 1830:
			setAutocast(player, 1152);
			return true;
		case 1834:
			setAutocast(player, 1160);
			return true;
		case 1838:
			setAutocast(player, 1172);
			return true;
		case 1842:
			setAutocast(player, 1183);
			return true;

		case 1831:
			setAutocast(player, 1154);
			return true;
		case 1835:
			setAutocast(player, 1163);
			return true;
		case 1839:
			setAutocast(player, 1175);
			return true;
		case 1843:
			setAutocast(player, 1185);
			return true;

		case 1832:
			setAutocast(player, 1156);
			return true;
		case 1836:
			setAutocast(player, 1166);
			return true;
		case 1840:
			setAutocast(player, 1177);
			return true;
		case 1844:
			setAutocast(player, 1188);
			return true;

		case 1833:
			setAutocast(player, 1158);
			return true;
		case 1837:
			setAutocast(player, 1169);
			return true;
		case 1841:
			setAutocast(player, 1181);
			return true;
		case 1845:
			setAutocast(player, 1189);
			return true;

		case 13189:
			setAutocast(player, 12939);
			return true;
		case 13215:
			setAutocast(player, 12963);
			return true;
		case 13202:
			setAutocast(player, 12951);
			return true;
		case 13228:
			setAutocast(player, 12975);
			return true;

		case 13241:
			setAutocast(player, 12987);
			return true;
		case 13267:
			setAutocast(player, 13011);
			return true;
		case 13254:
			setAutocast(player, 12999);
			return true;
		case 13280:
			setAutocast(player, 13023);
			return true;

		case 13147:
			setAutocast(player, 12901);
			return true;
		case 13167:
			setAutocast(player, 12919);
			return true;
		case 13158:
			setAutocast(player, 12911);
			return true;
		case 13178:
			setAutocast(player, 12929);
			return true;

		case 6162:
			setAutocast(player, 12861);
			return true;
		case 13125:
			setAutocast(player, 12881);
			return true;
		case 13114:
			setAutocast(player, 12871);
			return true;
		case 13136:
			setAutocast(player, 12891);
			return true;
		}
		return false;
	}

	public static void reset(Player player) {
		if (player.isAutocast()) {
			player.setAutocast(null);
			player.send(new SendConfig(108, 0));
			player.send(new SendString("Spell", 18584));
		}
	}

	private static void sendSelectionInterface(Player player, int weaponId) {
		for (int id : NO_AUTOCAST) {
			if (weaponId == id) {
				player.send(new SendMessage("You can't autocast with this weapon!"));
				return;
			}
		}

		if (player.spellbook == null) {
			player.spellbook = Spellbook.MODERN;
		}

		switch (player.spellbook) {
		case ANCIENT:
			if (!player.equipment.containsAny(ANCIENT_STAFFS)) {
				if (!player.equipment.hasWeapon()) {
					return;
				}

				String def = player.equipment.getWeapon().getName().toLowerCase();
				player.send(new SendMessage(
						"You can't autocast ancient magic with " + StringUtils.getAOrAn(def) + " " + def + "."));
				return;
			}
			player.interfaceManager.setSidebar(Config.ATTACK_TAB, 1689);
			break;
		case LUNAR:
			player.send(new SendMessage("You can't autocast lunar magic!"));
			break;
		case MODERN:
			if (player.equipment.contains(4675)) {
				player.send(new SendMessage("You can't autocast normal magic with this staff!"));
				return;
			}
			player.interfaceManager.setSidebar(Config.ATTACK_TAB, 1829);
			break;
		}
	}

	private static void setAutocast(Player player, int id) {
		CombatSpell spell = CombatSpell.get(id);

		if (spell == null) {
			player.send(new SendMessage("[ERROR] No spell definition found.", MessageColor.LIGHT_RED));
			return;
		}

		if (!PlayerRight.isPriviledged(player) && !MagicRune.hasRunes(player, spell.getRunes())) {
			player.send(new SendMessage("You don't have the runes required for this spell!"));
			return;
		}

		player.setAutocast(spell);
		player.send(new SendConfig(43, 3));
		player.send(new SendConfig(108, 1));
		player.interfaceManager.setSidebar(Config.ATTACK_TAB, 328);
	}
}
