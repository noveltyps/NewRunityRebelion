package plugin.click.button;

import io.server.content.emote.Emote;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.util.Utility;

public class EmoteButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button == 18714) {
			Emote.skillcape(player);
			return true;
		}

		if (!Emote.forId(button).isPresent()) {
			return false;
		}

		Emote emote = Emote.forId(button).get();

		if (!emote.activated(player)) {
			player.dialogueFactory
					.sendStatement("You have not unlocked the " + Utility.formatEnum(emote.name()) + " emote yet!")
					.execute();
			return true;
		}

		Emote.execute(player, emote.getAnimation(), emote.getGraphic());
		return true;
	}
}
