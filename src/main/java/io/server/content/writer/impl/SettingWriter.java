package io.server.content.writer.impl;

import io.server.content.writer.InterfaceWriter;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendString;

/**
 * Class handles writing on the server settings itemcontainer.
 *
 * @author Daniel
 */
public class SettingWriter extends InterfaceWriter {
	private String[] text = { "", "</col>Welcome screen: " + format(player.settings.welcomeScreen),
			"</col>TriviaBot: " + format(player.settings.triviaBot),
			"</col>Global yell: " + format(player.settings.yell),
			"</col>Drop notification: " + format(player.settings.dropNotification),
			"</col>Untradeables notification: " + format(player.settings.untradeableNotification),
			"</col>Prestige colors: " + format(player.settings.prestigeColors), "", };

	public SettingWriter(Player player) {
		super(player);
	}
	
	public static void open(Player player) {
		InterfaceWriter.write(new SettingWriter(player));
		player.send(new SendString("Misc Server Settings", 51002));
		player.send(new SendString("Click on the settings you'd like to change.", 51003));
		player.interfaceManager.open(51000);
	}

	private String format(boolean parameter) {
		return parameter ? "<col=47781F>Enabled" : "<col=F02E2E>Disabled";
	}

	@Override
	protected int startingIndex() {
		return 51011;
	}

	@Override
	protected String[] text() {
		return text;
	}

	@Override
	protected int[][] color() {
		return null;
	}

	@Override
	protected int[][] font() {
		return null;
	}
}
