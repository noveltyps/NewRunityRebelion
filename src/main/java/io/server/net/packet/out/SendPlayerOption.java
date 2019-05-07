package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerOption;
import io.server.net.codec.ByteModification;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketType;

/**
 * Shows a player options such as right clicking a player.
 * 
 * @author Daniel
 */
public final class SendPlayerOption extends OutgoingPacket {

	private PlayerOption option;
	private boolean top;
	private boolean disable;

	public SendPlayerOption(PlayerOption option, boolean top) {
		this(option, top, false);
	}

	public SendPlayerOption(PlayerOption option, boolean top, boolean disable) {
		super(104, PacketType.VAR_BYTE);
		this.option = option;
		this.top = top;
		this.disable = disable;
	}

	@Override
	public boolean encode(Player player) {
		if (player.contextMenus.contains(option) && !disable) {
			return false;
		}

		builder.writeByte(option.getIndex(), ByteModification.NEG).writeByte(top ? 1 : 0, ByteModification.ADD)
				.writeString(disable ? "null" : option.getName());

		if (disable) {
			player.contextMenus.remove(option);
		} else {
			player.contextMenus.add(option);
		}
		return true;
	}

}
