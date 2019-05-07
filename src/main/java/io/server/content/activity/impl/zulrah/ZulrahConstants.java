package io.server.content.activity.impl.zulrah;

import io.server.game.world.position.Position;

/**
 * Holds all the zulrah constants.
 *
 * @author Daniel.
 * @author Adam Temporarily removed zulrah ID 2043 Due to it causing LOADS of
 *         issues.
 */
public class ZulrahConstants {

	/** The maximum amount of clouds that zulrah can spawn at a time. */
	public static final int MAXIMUM_CLOUDS = 4;

	/** The maximum amount of snakelings that zulrah can spawn at a time. */
	public static final int MAXIMUM_SNAKELINGS = 4;

	/** The zulrah identifications. */
	public static final Integer[] ZULRAH_IDS = new Integer[] { 2042, 2044 };

	/** All the possible positions zulrah can move to. */
	public static final Position[] LOCATIONS = { new Position(2266, 3072), new Position(2276, 3074),
			new Position(2273, 3064) };

	/** All the possible positions zulrah can spawn his clouds for. */
	public static final Position[] CLOUD_POSITIONS = { new Position(2272, 3076), new Position(2272, 3072),
			new Position(2270, 3069), new Position(2267, 3069), new Position(2264, 3069), new Position(2262, 3072),
			new Position(2262, 3076) };

}
