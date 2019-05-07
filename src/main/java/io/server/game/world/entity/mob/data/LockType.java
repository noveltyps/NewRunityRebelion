package io.server.game.world.entity.mob.data;

import io.server.game.Graphic;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendWidget;

/**
 * Holds all the lock types.
 *
 * @author Daniel
 */
public enum LockType {
	MASTER(PacketType.values()) {
		@Override
		public void execute(Mob mob, int time) {
			if (mob.isPlayer())
				mob.getPlayer().action.reset();
		}
	},
	MASTER_WITH_MOVEMENT(PacketType.MASTER_WITH_MOVEMENT) {
		@Override
		public void execute(Mob mob, int time) {
			if (mob.isPlayer())
				mob.getPlayer().action.reset();
		}
	},
	MASTER_WITH_COMMANDS(PacketType.MASTER_WITH_COMMANDS) {
		@Override
		public void execute(Mob mob, int time) {
			if (mob.isPlayer())
				mob.getPlayer().action.reset();
		}
	},
	MORPH(PacketType.CLICK_ITEM, PacketType.CLICK_NPC, PacketType.CLICK_OBJECT, PacketType.COMBAT, PacketType.DROP_ITEM,
			PacketType.MOVEMENT, PacketType.USE_MAGIC, PacketType.WIELD_ITEM, PacketType.WALKING, PacketType.PICKUP_ITEM,
			PacketType.USE_ITEM, PacketType.INTERACT) {
		@Override
		public void execute(Mob mob, int time) {
			if (mob.isPlayer())
				mob.getPlayer().action.reset();
		}
	},
	OBJECT(PacketType.CLICK_OBJECT) {
		@Override
		public void execute(Mob mob, int time) {
			mob.movement.reset();
		}
	},
	WALKING(PacketType.WALKING, PacketType.MOVEMENT) {
		@Override
		public void execute(Mob mob, int time) {
		}
	},
	STUN(PacketType.WALKING, PacketType.COMBAT, PacketType.PICKUP_ITEM, PacketType.WIELD_ITEM, PacketType.COMMANDS,
			PacketType.CLICK_BUTTON, PacketType.CLICK_NPC, PacketType.CLICK_OBJECT, PacketType.USE_ITEM,
			PacketType.INTERACT) {
		@Override
		public void execute(Mob mob, int time) {
			if (mob.locking.locked() && mob.locking.getLock() == STUN)
				return;
			if (mob.isPlayer()) {
				Player player = mob.getPlayer();
				player.resetFace();
				player.graphic(new Graphic(80, true));
				player.send(new SendMessage("You have been stunned!"));
				player.send(new SendWidget(SendWidget.WidgetType.STUN, time));
			}
		}
	},
	FREEZE(PacketType.WALKING, PacketType.MOVEMENT) {
		@Override
		public void execute(Mob mob, int time) {
			if (mob.locking.locked() && mob.locking.getLock() == FREEZE)
				return;
			if (mob.isPlayer()) {
				Player player = mob.getPlayer();
				player.resetFace();
				player.send(new SendMessage("You've been frozen!"));
				player.send(new SendWidget(SendWidget.WidgetType.FROZEN, time));
			}
		}
	};

	/** The lock packet flag. */
	public final PacketType[] packets;

	/** Handles the execution of the lock. */
	public abstract void execute(Mob mob, int time);

	/** Constructs a new <code>LockType</code>. */
	LockType(PacketType... packets) {
		this.packets = packets;
	}

	public boolean isLocked(PacketType type) {
		return isLocked(type, null, null);
	}

	public boolean isLocked(PacketType type, Mob mob, Object object) {
		for (PacketType packet : packets) {
			if (type == packet)
				return !packet.exception(mob, object);
		}
		return false;
	}
}
