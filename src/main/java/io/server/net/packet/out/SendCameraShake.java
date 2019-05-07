package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public class SendCameraShake extends OutgoingPacket {
	private final int verticalAmount;
	private final int verticalSpeed;
	private final int horizontalAmount;
	private final int horizontalSpeed;

	public SendCameraShake(int verticalAmount, int verticalSpeed, int horizontalAmount, int horizontalSpeed) {
		super(35, 4);
		this.verticalAmount = verticalAmount;
		this.verticalSpeed = verticalSpeed;
		this.horizontalAmount = horizontalAmount;
		this.horizontalSpeed = horizontalSpeed;
	}

	@Override
	protected boolean encode(Player player) {
		builder.writeByte(verticalAmount);
		builder.writeByte(verticalSpeed);
		builder.writeByte(horizontalAmount);
		builder.writeByte(horizontalSpeed);
		return true;
	}
}
