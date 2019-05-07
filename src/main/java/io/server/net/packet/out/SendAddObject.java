package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;

public class SendAddObject extends OutgoingPacket {

	private final GameObject object;

	public SendAddObject(GameObject object) {
		super(151, 4);
		this.object = object;
	}

	@Override
	public boolean encode(Player player) {
		if (object.getInstancedHeight() != player.instance) {
			return false;
		}
		player.send(new SendCoordinate(object.getPosition()));
		builder.writeByte(0, ByteModification.ADD);
		builder.writeShort(object.getId(), ByteOrder.LE);
		builder.writeByte((object.getObjectType().getId() << 2) + (object.getDirection().getId() & 3),
				ByteModification.SUB);
		return true;
	}

}