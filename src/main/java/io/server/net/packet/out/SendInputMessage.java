package io.server.net.packet.out;

import java.util.Optional;
import java.util.function.Consumer;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.ByteModification;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

/**
 * Sends an input dialogue.
 * 
 * @author Daniel
 * @author Michael
 */
public class SendInputMessage extends OutgoingPacket {

	private final Consumer<String> action;
	private final String inputMessage;
	private final int inputLength;

	public SendInputMessage(String message, int length, Consumer<String> action) {
		super(187, PacketType.VAR_SHORT);
		this.action = action;
		this.inputMessage = message;
		this.inputLength = length;
	}

	@Override
	public boolean encode(Player player) {
		player.enterInputListener = Optional.of(action);//sec lookin onto it on my side
		builder.writeString(inputMessage).writeShort(inputLength, ByteModification.ADD);
		return true;
	}

}
