package io.server.net.packet.out;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.OutgoingPacket;

/**
 * Sends experience to the client's experience counter.
 * 
 * @author Michael | Chex
 */
public class SendExpCounter extends OutgoingPacket {

	private final int skill;
	private final int experience;
	private final boolean counter;

	public SendExpCounter(int skill, int experience, boolean counter) {
		super(127, 6);
		this.skill = skill;
		this.experience = experience;
		this.counter = counter;
	}

	public SendExpCounter(int experience) {
		this(99, experience, true);
	}

	@Override
	public boolean encode(Player player) {
		builder.writeByte(skill).writeInt(experience).writeByte(counter ? 1 : 0);
		return true;
	}

}
