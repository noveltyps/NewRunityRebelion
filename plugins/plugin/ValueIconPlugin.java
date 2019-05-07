package plugin;

import io.server.game.event.impl.DropItemEvent;
import io.server.game.event.impl.MovementEvent;
import io.server.game.event.impl.PickupItemEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.data.ValueIcon;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;

public class ValueIconPlugin extends PluginContext {

	@Override
	protected boolean onMovement(Player player, MovementEvent event) {
		if (player.valueIcon != -1 && !Area.inWilderness(player)) {
			player.valueIcon = -1;
			player.updateFlags.add(UpdateFlag.APPEARANCE);
			return false; // if this is true any other plugins that use this method wont work
		}

		if (player.valueIcon == -1 && Area.inWilderness(player)) {
			player.valueIcon = player.playerAssistant.getValueIcon(player).getCode();
			player.updateFlags.add(UpdateFlag.APPEARANCE);
			return false; // if this is true any other plugins that use this method wont work
		}
		return false;
	}

	@Override
	protected boolean onPickupItem(Player player, PickupItemEvent event) {
		final Item item = event.getItem();

		if (item.getValue() * item.getAmount() <= 10_000) {
			return false; // if this is true any other plugins that use this method wont work
		}

		final Position position = event.getPosition();

		if (Area.inWilderness(position)) {
			ValueIcon icon = player.playerAssistant.getValueIcon(player);

			final int currentIcon = player.valueIcon;

			if (icon.getCode() == currentIcon) {
				return false; // if this is true any other plugins that use this method wont work
			}

			player.valueIcon = icon.getCode();
			player.updateFlags.add(UpdateFlag.APPEARANCE);
		}

		return false;
	}

	@Override
	protected boolean onDropItem(Player player, DropItemEvent event) {
		final Item item = event.getItem();

		if (item.getValue() * item.getAmount() <= 10_000) {
			return false; // if this is true any other plugins that use this method wont work
		}

		final Position position = event.getPosition();

		if (Area.inWilderness(position)) {
			ValueIcon icon = player.playerAssistant.getValueIcon(player);

			final int currentIcon = player.valueIcon;

			if (icon.getCode() == currentIcon) {
				return false; // if this is true any other plugins that use this method wont work
			}

			player.valueIcon = icon.getCode();
			player.updateFlags.add(UpdateFlag.APPEARANCE);
		}
		return false;
	}

}
