package io.server.net.packet.out;

import io.server.game.Projectile;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;
import io.server.net.packet.OutgoingPacket;

public class SendProjectile extends OutgoingPacket {

	private final Projectile projectile;
	private final Position position;
	private final int lock;
	private final byte offsetX;
	private final byte offsetY;

	public SendProjectile(Projectile projectile, Position position, int lock, byte offsetX, byte offsetY) {
		super(117, 15);
		this.projectile = projectile;
		this.lock = lock;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.position = position;
	}

	@Override
	public boolean encode(Player player) {
		player.send(new SendCoordinate(position));
		builder.writeByte(0).writeByte(offsetX).writeByte(offsetY).writeShort(lock).writeShort(projectile.getId())
				.writeByte(projectile.getStartHeight()).writeByte(projectile.getEndHeight())
				.writeShort(projectile.getDelay()).writeShort(projectile.getDuration()).writeByte(projectile.getCurve())
				.writeByte(64);
		return true;
	}

}