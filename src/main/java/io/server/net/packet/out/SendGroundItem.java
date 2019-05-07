package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.ground.GroundItem;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

public class SendGroundItem extends OutgoingPacket {

	private final GroundItem groundItem;
	private GroundItemType type;

	private SendGroundItem(GroundItem groundItem, GroundItemType type) {
		super(44, 12);
		this.groundItem = groundItem;
		this.type = type;
	}

	public SendGroundItem(GroundItem groundItem) {
		this(groundItem, GroundItemType.NORMAL);
	}

	@Override
	public boolean encode(Player player) {
		if (groundItem.instance != player.instance) {
			return false;
		}

		if (!groundItem.item.isTradeable()) {
			this.type = GroundItemType.UNTRADEABLE;
		} else if (groundItem.item.getValue() > 1_000_000) {
			this.type = GroundItemType.RARE;
		} else {
			this.type = GroundItemType.NORMAL;
		}

		player.send(new SendCoordinate(groundItem.getPosition()));
		builder.writeShort(groundItem.item.getId(), ByteModification.ADD, ByteOrder.LE)
				.writeLong(groundItem.item.getAmount()).writeByte(type.ordinal()).writeByte(0);
		return true;
	}

	public enum GroundItemType {
		NORMAL, RARE, UNTRADEABLE
	}
}
