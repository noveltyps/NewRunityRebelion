package io.server.net.packet.in;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.items.containers.equipment.EquipmentType;
import io.server.game.world.items.containers.pricechecker.PriceType;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.MessageColor;
import io.server.util.Utility;

/**
 * Handles the in examine packet
 * 
 * @author Nerik / Harryl
 */
@PacketListenerMeta({ 150 })
public class ExaminePacketListener implements PacketListener {

	@Override
	public void handlePacket(Player player, GamePacket packet) {
		int slot = packet.readShort();
		int interfaceId = packet.readShort();
		int itemId = packet.readShort();

		if (PlayerRight.isDeveloper(player) && player.debug) {
			player.send(new SendMessage(
					"[Examine] - slot: " + slot + " -- interfaceId: " + interfaceId + " -- itemId: " + itemId,
					MessageColor.DEVELOPER));
		}

		if (slot == -1) {
			player.settings.clientWidth = interfaceId;
			player.settings.clientHeight = itemId;
			return;
		}

		ItemDefinition itemDef = ItemDefinition.get(itemId);

		Item item = player.inventory.get(slot);
		if (item == null || item.getId() != itemId)
			return;
		if (item.isTradeable()) {
			String message = "";
			message += "Item: <col=A52929>" + item.getName() + "</col> ";
			message += "Value: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.VALUE)) + " ("
					+ Utility.formatDigits(item.getValue(PriceType.VALUE) * item.getAmount()) + ")</col> ";
			message += "High alch: <col=A52929>" + Utility.formatDigits(item.getValue(PriceType.HIGH_ALCH_VALUE))
					+ "</col> ";
			player.send(new SendMessage(message));
		}

		if (itemDef != null) {
			player.interfaceManager.close();
			if (itemDef.getEquipmentType() == EquipmentType.NOT_WIELDABLE
					|| itemDef.getEquipmentType() == EquipmentType.ARROWS) {
				player.message("We couldn't load the statistics for this item!");
				return;
			}
			int count = 52103;
			player.interfaceManager.open(52100);
			player.send(new SendItemOnInterface(52102, new Item(itemId)));
			player.send(new SendString(itemDef.getName(), 52113));
			player.send(new SendString("Equipement: " + itemDef.getEquipmentType(), count++));
			player.send(new SendString("Stackable: " + itemDef.isStackable(), count++));
			player.send(new SendString("Noted: " + itemDef.isNoted(), count++));
			player.send(new SendString("High Alch: " + formatCoins(itemDef.getHighAlch()), count++));
			player.send(new SendString("Low Alch: " + formatCoins(itemDef.getLowAlch()), count++));
			player.send(new SendString("Tradeable: " + itemDef.isTradeable(), count++));
			player.send(new SendString("Weight: " + itemDef.getWeight(), count++));

			player.send(new SendString("Value: " + formatCoins(itemDef.getValue()), 52143));

			String[] text = { "Stab", "Slash", "Crush", "Magic", "Range" };
			for (int i = 0; i < 5; i++) {
				player.send(new SendString(text[i] + ": " + String.valueOf(itemDef.getBonuses()[0 + i]), 52131 + i));
				player.send(new SendString(text[i] + ": " + String.valueOf(itemDef.getBonuses()[5 + i]), 52138 + i));
			}

			player.send(new SendString("Strength: " + String.valueOf(itemDef.getBonuses()[10]), 52144));
			player.send(new SendString("Prayer: " + String.valueOf(itemDef.getBonuses()[11]), 52145));

		}
	}

	public static String formatCoins(int amount) {
		if (amount > 9999 && amount <= 9999999) {
			return (amount / 1000) + "K";
		} else if (amount > 9999999 && amount <= 999999999) {
			return (amount / 1000000) + "M";
		} else if (amount > 999999999) {
			return (amount / 1000000000) + "B";
		}
		return String.valueOf(amount);
	}

}
