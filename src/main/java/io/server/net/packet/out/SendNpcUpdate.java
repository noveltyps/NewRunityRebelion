package io.server.net.packet.out;

import java.util.Iterator;

import io.server.Config;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.world.World;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.Viewport;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.Position;
import io.server.net.codec.AccessType;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.OutgoingPacket;
import io.server.net.packet.PacketBuilder;
import io.server.net.packet.PacketType;

/**
 * The packet that's responsible for updating npcs.
 *
 * @author nshusa
 */
public final class SendNpcUpdate extends OutgoingPacket {

	public SendNpcUpdate() {
		super(65, PacketType.VAR_SHORT);
	}

	@Override
	public boolean encode(Player player) {
		PacketBuilder maskBuf = PacketBuilder.alloc();
		try {
			builder.initializeAccess(AccessType.BIT);
			builder.writeBits(8, player.viewport.getNpcsInViewport().size());

			for (Iterator<Npc> itr = player.viewport.getNpcsInViewport().iterator(); itr.hasNext();) {

				Npc npc = itr.next();

				if (player.viewport.shouldRemove(npc)) {
					if (npc.atomicPlayerCount.decrementAndGet() < 0) {
						npc.atomicPlayerCount.set(0);
					}
					itr.remove();
					builder.writeBits(1, 1);
					builder.writeBits(2, 3);
				} else {
					updateMovement(builder, npc);

					if (npc.isUpdateRequired()) {
						updateNpc(maskBuf, npc);
					}
				}

			}

			int npcsAdded = 0;

			for (Npc localNpc : World.getRegions().getLocalNpcs(player)) {

				if (player.viewport.getNpcsInViewport().size() >= Viewport.CAPACITY
						|| npcsAdded == Viewport.ADD_THRESHOLD) {
					break;
				}

				if (player.viewport.add(localNpc)) {
					npcsAdded++;
					addNewNpc(builder, player, localNpc);
					updateNpc(maskBuf, localNpc);

					if (localNpc.atomicPlayerCount.incrementAndGet() < 0) {
						localNpc.atomicPlayerCount.set(0);
					}
				}

			}

			if (maskBuf.buffer().readableBytes() > 0) {
				builder.writeBits(14, 16383);
				builder.initializeAccess(AccessType.BYTE);
				builder.writeBytes(maskBuf.buffer());
			} else {
				builder.initializeAccess(AccessType.BYTE);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	private static void addNewNpc(PacketBuilder packet, Player player, Npc npc) {
		packet.writeBits(14, npc.getIndex());
		packet.writeBits(5, npc.getPosition().getY() - player.getPosition().getY());
		packet.writeBits(5, npc.getPosition().getX() - player.getPosition().getX());
		packet.writeBits(1, 0);
		packet.writeBits(Config.NPC_BITS, npc.id);
		packet.writeBits(1, npc.isUpdateRequired() ? 1 : 0);
	}

	private static void updateMovement(PacketBuilder packet, Npc npc) {
		final boolean updateRequired = npc.isUpdateRequired();
		if (npc.movement.getRunningDirection() != -1) {
			packet.writeBit(true).writeBits(2, 2).writeBits(3, npc.movement.getWalkingDirection())
					.writeBits(3, npc.movement.getRunningDirection()).writeBit(npc.isUpdateRequired());
		} else if (npc.movement.getWalkingDirection() != -1) {
			packet.writeBit(true).writeBits(2, 1).writeBits(3, npc.movement.getWalkingDirection())
					.writeBit(npc.isUpdateRequired());
		} else {
			packet.writeBit(updateRequired);
			if (updateRequired) {
				packet.writeBits(2, 0);
			}
		}
	}

	private static void updateNpc(PacketBuilder maskBuf, Npc npc) {
		if (!npc.isUpdateRequired()) {
			return;
		}

		int mask = 0;

		if (npc.updateFlags.contains(UpdateFlag.ANIMATION) && npc.getAnimation().isPresent()) {
			mask |= 0x10;
		}

		if (npc.updateFlags.contains(UpdateFlag.GRAPHICS) && npc.getGraphic().isPresent()) {
			mask |= 0x80;
		}

		if (npc.updateFlags.contains(UpdateFlag.INTERACT)) {
			mask |= 0x20;
		}

		if (npc.updateFlags.contains(UpdateFlag.FORCED_CHAT)) {
			mask |= 0x1;
		}

		if (npc.updateFlags.contains(UpdateFlag.FIRST_HIT)) {
			mask |= 0x40;
		}

		if (npc.updateFlags.contains(UpdateFlag.SECOND_HIT)) {
			mask |= 0x8;
		}

		if (npc.updateFlags.contains(UpdateFlag.TRANSFORM)) {
			mask |= 0x2;
		}

		if (npc.updateFlags.contains(UpdateFlag.FACE_COORDINATE)) {
			mask |= 0x4;
		}

		maskBuf.writeByte(mask);

		if (npc.updateFlags.contains(UpdateFlag.ANIMATION) && npc.getAnimation().isPresent()) {
			appendAnimationMask(npc, maskBuf);
		}

		if (npc.updateFlags.contains(UpdateFlag.GRAPHICS) && npc.getGraphic().isPresent()) {
			appendGfxMask(npc, maskBuf);
		}

		if (npc.updateFlags.contains(UpdateFlag.INTERACT)) {
			appendFaceEntityMask(npc, maskBuf);
		}

		if (npc.updateFlags.contains(UpdateFlag.FORCED_CHAT)) {
			appendForceChatMask(npc, maskBuf);
		}

		if (npc.updateFlags.contains(UpdateFlag.FIRST_HIT)) {
			appendFirstHitMask(npc, maskBuf);
		}

		if (npc.updateFlags.contains(UpdateFlag.SECOND_HIT)) {
			appendSecondHitMask(npc, maskBuf);
		}

		if (npc.updateFlags.contains(UpdateFlag.TRANSFORM)) {
			appendTransformationMask(npc, maskBuf);
		}

		if (npc.updateFlags.contains(UpdateFlag.FACE_COORDINATE)) {
			appendFaceCoordinateMask(npc, maskBuf);
		}

	}

	private static void appendAnimationMask(Npc npc, PacketBuilder maskBuf) {
		Animation anim = npc.getAnimation().orElse(Animation.RESET);
		maskBuf.writeShort(anim.getId(), ByteOrder.LE).writeByte(anim.getDelay());
	}

	private static void appendGfxMask(Npc npc, PacketBuilder maskBuf) {
		Graphic gfx = npc.getGraphic().orElse(Graphic.RESET);
		maskBuf.writeShort(gfx.getId()).writeInt(gfx.getDelay() | gfx.getHeight());
	}

	private static void appendFaceEntityMask(Npc npc, PacketBuilder maskBuf) {
		Mob mob = npc.interactingWith;
		int index = 65535;
		if (mob != null) {
			index = mob.getIndex();
			if (mob.isPlayer()) {
				index += 32768;
			}
			maskBuf.writeShort(index);
		} else {
			maskBuf.writeShort(index);
		}
	}

	private static void appendForceChatMask(Npc npc, PacketBuilder maskBuf) {
		maskBuf.writeString(npc.forceChat);
	}

	private static void appendFaceCoordinateMask(Npc npc, PacketBuilder maskBuf) {
		Position loc = npc.facePosition;
		if (loc == null) {
			Position currentPos = npc.getPosition();
			Direction currentDir = npc.movement.lastDirection;
			maskBuf.writeShort(((currentPos.getX() + currentDir.getDirectionX()) << 1) + 1, ByteOrder.LE)
					.writeShort(((currentPos.getY() + currentDir.getDirectionY()) << 1) + 1, ByteOrder.LE);
		} else {
			maskBuf.writeShort((loc.getX() << 1) + 1, ByteOrder.LE).writeShort((loc.getY() << 1) + 1, ByteOrder.LE);
		}
	}

	private static void appendTransformationMask(Npc npc, PacketBuilder maskBuf) {
		maskBuf.writeShort(npc.id, ByteModification.ADD, ByteOrder.LE);
	}

	private static void appendFirstHitMask(final Npc npc, final PacketBuilder updateBlock) {
		Hit hit = npc.firstHit;
		int id = hit.getHitsplat().getId();
		int max = npc.getMaximumHealth() >= 500 ? 200 : 100;
		int health = max * npc.getCurrentHealth() / npc.getMaximumHealth();
		if (health > max)
			health = max;

		if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
			id++;
		}

		updateBlock.writeByte(hit.getDamage());
		updateBlock.writeByte(id, ByteModification.ADD);
		updateBlock.writeByte(hit.getHitIcon().getId());
		updateBlock.writeByte(health);
		updateBlock.writeByte(max, ByteModification.NEG);
	}

	private static void appendSecondHitMask(final Npc npc, final PacketBuilder updateBlock) {
		Hit hit = npc.secondHit;
		int id = hit.getHitsplat().getId();
		int max = npc.getMaximumHealth() >= 500 ? 200 : 100;
		int health = npc.getCurrentHealth() * max / npc.getMaximumHealth();
		if (health > max)
			health = max;

		if (hit.getHitsplat() == Hitsplat.NORMAL && hit.getDamage() > 0) {
			id++;
		}

		updateBlock.writeByte(hit.getDamage());
		updateBlock.writeByte(id, ByteModification.SUB);
		updateBlock.writeByte(hit.getHitIcon().getId());
		updateBlock.writeByte(health);
		updateBlock.writeByte(max, ByteModification.NEG);
	}

}