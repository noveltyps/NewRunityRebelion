package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.ground.GroundItem;
import io.server.net.codec.ByteModification;
import io.server.net.packet.OutgoingPacket;

public class SendRemoveGroundItem extends OutgoingPacket {

	private final GroundItem groundItem;

	public SendRemoveGroundItem(GroundItem groundItem) {
		super(156, 3);
		this.groundItem = groundItem;
	}

	@Override
	public boolean encode(Player player) {
		player.send(new SendCoordinate(groundItem.getPosition()));
		builder.writeByte(0, ByteModification.ADD).writeShort(groundItem.item.getId());
		return true;
	}

}
