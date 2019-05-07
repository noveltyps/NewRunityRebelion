package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

public class SendWidget extends OutgoingPacket {

	private final WidgetType type;
	private final int seconds;

	public SendWidget(WidgetType type, int seconds) {
		super(178, 3);
		this.type = type;
		this.seconds = seconds;
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(type.icon).writeShort(seconds * 50);
		return true;
	}

	public enum WidgetType {
		ANTI_FIRE(1), VENGEANCE(2), FROZEN(3), TELEBLOCK(4), SKULL(5), CLAN(6), STUN(7), OVERLOAD(8), STRENGTH(9),
		ATTACK(10), DEFENCE(11), RANGE(12), MAGIC(13), DOUBLEXP(14), POISON(15), FISHING(16), STAMINA(17), VENOM(18),
		SUPERVENOM(19), AGILITY(20);

		private final int icon;

		WidgetType(int icon) {
			this.icon = icon;
		}
	}

}
