package io.server.net.packet.in;

import static com.google.common.base.Preconditions.checkState;

import io.server.content.event.EventDispatcher;
import io.server.content.event.impl.FirstObjectClick;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.event.impl.SecondObjectClick;
import io.server.content.event.impl.ThirdObjectClick;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginManager;
import io.server.game.world.World;
import io.server.game.world.entity.mob.data.PacketType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.game.world.object.ObjectDefinition;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.game.world.region.RegionManager;
import io.server.net.codec.ByteModification;
import io.server.net.codec.ByteOrder;
import io.server.net.packet.ClientPackets;
import io.server.net.packet.GamePacket;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;

/**
 * The {@code GamePacket} responsible for clicking various options of an in-game
 * object.
 *
 * @author Daniel | Obey
 */
@PacketListenerMeta({ ClientPackets.FIRST_CLICK_OBJECT, ClientPackets.SECOND_CLICK_OBJECT,
		ClientPackets.THIRD_CLICK_OBJECT })
public class ObjectInteractionPacketListener implements PacketListener {

	@Override
	public void handlePacket(final Player player, GamePacket packet) {
		checkState(player != null, "[@ObjectInteraction] Player is null");
		
		if (player.isTeleporting() || player.isDead() ||  player.locking.locked(PacketType.CLICK_OBJECT))
			return;

		switch (packet.getOpcode()) {
		case ClientPackets.FIRST_CLICK_OBJECT:
			handleFirstClickObject(player, packet);
			break;
		case ClientPackets.SECOND_CLICK_OBJECT:
			handleSecondClickObject(player, packet);
			break;
		case ClientPackets.THIRD_CLICK_OBJECT:
			handleThirdClickObject(player, packet);
			break;
		}
	}

	private static final int[] CHEAP_CODE = { 5959, 1814, 5959, 5960, 9706 };

	private static void handleFirstClickObject(Player player, GamePacket packet) {
		int x = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		int id = packet.readShort(false);
		int y = packet.readShort(false, ByteModification.ADD);
		ObjectDefinition objectDefinition = ObjectDefinition.lookup(id);

		if (objectDefinition == null)
			return;

		final Position position = Position.create(x, y, player.getHeight());
		Region region = RegionManager.getRegion(x, y);
		GameObject object = region.getGameObject(id, position);

		if (object == null) {
			return;
		}

		for (int cheap : CHEAP_CODE) {
			if (object.getId() == cheap) {
				player.walkExactlyTo(object.getPosition(), () -> onAction(player, object, 1));
				return;
			}
		}

		player.walkTo(object, () -> onAction(player, object, 1));
		
		switch(object.getId()) {
		
		case 823: // Max hit dummy
//			player.getCombat().
			break;
		
		}
		
	}

	private static void handleSecondClickObject(Player player, GamePacket packet) {
		final int id = packet.readShort(ByteOrder.LE, ByteModification.ADD);
		final int y = packet.readShort(ByteOrder.LE);
		final int x = packet.readShort(false, ByteModification.ADD);

		final ObjectDefinition objectDefinition = ObjectDefinition.lookup(id);

		if (objectDefinition == null) {
			return;
		}

		final Position position = Position.create(x, y, player.getHeight());
		final Region region = World.getRegions().getRegion(position);

		final GameObject object = region.getGameObject(id, position);

		if (object == null) {
			return;
		}

		player.walkTo(object, () -> onAction(player, object, 2));
	}

	private static void handleThirdClickObject(Player player, GamePacket packet) {
		final int x = packet.readShort(ByteOrder.LE);
		final int y = packet.readShort(false);
		final int id = packet.readShort(false, ByteOrder.LE, ByteModification.ADD);

		final ObjectDefinition objectDefinition = ObjectDefinition.lookup(id);

		if (objectDefinition == null) {
			return;
		}

		final Position position = Position.create(x, y, player.getHeight());
		final Region region = World.getRegions().getRegion(position);
		final GameObject object = region.getGameObject(id, position);

		if (object == null) {
			return;
		}

		player.walkTo(object, () -> onAction(player, object, 3));
	}

	private static void onAction(Player player, GameObject object, int type) {
		player.face(object);
		ObjectInteractionEvent event;

		if (player.getDynamicRegion() != null && player.getDynamicRegion().getHandler() != null) {
			player.getDynamicRegion().getHandler().handleObjectInteraction(type, object);
			return;
		}
		
		if (type == 2) {
			event = new SecondObjectClick(object);
		} else if (type == 3) {
			event = new ThirdObjectClick(object);
		} else {
			event = new FirstObjectClick(object);
		}

		if (EventDispatcher.execute(player, event)) {
			return;
		}

		PluginManager.getDataBus().publish(player, new ObjectClickEvent(type, object));
	}
}
