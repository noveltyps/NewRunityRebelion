package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public class SendConstruction extends OutgoingPacket {

	public SendConstruction() {
		super(241, -1);
		// super(241);
	}

	@Override
	public boolean encode(Player player) {
//		if (player.getHouse() == null) {
//			System.out.println("House is null");
//			return;
//		}
//
//		player.setLastLocation(player.getPosition().copy());
//		builder.writeShort(player.getPosition().getChunkY() + 6, ByteModification.ADDITION);
//		builder.initializeAccess(AccessType.BIT);
//
//		for (int z = 0; z < 4; z++) {
//			for (int x = 0; x < 13; x++) {
//				for (int y = 0; y < 13; y++) {
//					Room room = player.getHouse().rooms[x][y][z];
//					builder.writeBits(1, room != null ? 1 : 0);
//					
//					if (room == null) {
//						continue;
//					}
//
//					builder.writeBits(26, room.getX() / 8 << 14 | room.getY() / 8 << 3 | 0 % 4 << 24 | room.getRotation() % 4 << 1);
//				}
//			}
//		}
//
//		builder.initializeAccess(AccessType.BYTE);
//		builder.writeShort(player.getPosition().getChunkX() + 6);
//		player.getSession().ifPresent(s -> s.queue(builder));
		return false;
	}

}