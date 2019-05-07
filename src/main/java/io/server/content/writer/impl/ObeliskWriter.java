package io.server.content.writer.impl;

import io.server.content.writer.InterfaceWriter;
import io.server.game.world.entity.mob.player.Player;

/**
 * Class handles writing on the obelisk itemcontainer.
 *
 * @author Daniel
 */
public class ObeliskWriter extends InterfaceWriter {
	private String[] text = { "", "Level 44 Wilderness", "Level 27 Wilderness", "Level 35 Wilderness",
			"Level 13 Wilderness", "Level 19 Wilderness", "Level 50 Wilderness", "", "", "", "", "", "", "", "", "",
			"" };

	public ObeliskWriter(Player player) {
		super(player);
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
