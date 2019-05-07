package io.server.game.world.region;

import static io.server.game.world.position.Position.HEIGHT_LEVELS;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.server.game.world.Interactable;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.object.GameObject;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Position;
import io.server.util.Utility;

/**
 * Represents a single region.
 *
 * @author Graham Edgecombe
 */
public class Region {
	
	private static final int CHUNK_SIZE = 8;
	public static final int SIZE = CHUNK_SIZE * 8;
	public static final int VIEW_DISTANCE = SIZE / 4 - 1;
	public static final Map<Position, GameObject> ACTIVE_OBJECT = new HashMap<>();
	public static final Set<Position> SKIPPED_OBJECTS = new HashSet<>();

	/** The region id. */
	private int id;

	/** The region coordinates. */
	private int x, y;

	/** The region blocks that handle different heights. */
	private final RegionBlock[] blocks = new RegionBlock[Short.MAX_VALUE];

	/** The surrounded regions. */
	private Optional<Region[]> surroundingRegions = Optional.empty();

	/** Creates a region. */
	public Region(int x, int y) {
		this.x = x;
		this.y = y;
		this.id = ((x >> 6) << 8) + (y >> 6);
	}

	/** @return region x-coordinate */
	public int getX() {
		return x;
	}

	/** @return region y-coordinate */
	public int getY() {
		return y;
	}

	/**
	 * Gets a single tile in this region from the specified height, x and y
	 * coordinates.
	 *
	 * @param height The height.
	 * @param x      The x coordinate.
	 * @param y      The y coordinate.
	 * @return The tile in this region for the specified attributes.
	 */
	public RegionTile getTile(int height, int x, int y) {
		return getBlock(height).getTile(x, y);
	}

	public Optional<Region[]> getSurroundingRegions() {
		return surroundingRegions;
	}

	public void setSurroundingRegions(Optional<Region[]> surroundingRegions) {
		this.surroundingRegions = surroundingRegions;
	}

	/** @return the players in this region */
	public Collection<Player> getPlayers(int height) {
		return getBlock(height).getPlayers();
	}

	/** @return the npcs in this region */
	public Collection<Npc> getNpcs(int height) {
		return getBlock(height).getNpcs();
	}

	/** @return the game objects with the given position */
	public List<GameObject> getGameObjects(Position position) {
		return getBlock(position.getHeight()).getGameObjects(position);
	}

	/** @return the ground item with the given id at the given position */
	public GroundItem getGroundItem(int id, Position position) {
		return getBlock(position.getHeight()).getGroundItem(id, position);
	}

	/** Adds a player to this region. */
	public void addPlayer(Player player) {
		getBlock(player.getHeight()).addPlayer(player);
	}

	/** Removes a player from this region. */
	public void removePlayer(Player player) {
		getBlock(player.getHeight()).removePlayer(player);
	}

	/** Adds an npc to this region. */
	public void addNpc(Npc npc) {
		getBlock(npc.getHeight()).addNpc(npc);
	}

	/** Removes an npc from this region. */
	public void removeNpc(Npc npc) {
		getBlock(npc.getHeight()).removeNpc(npc);
	}

	/** Adds an object to this region. */
	public void addObject(GameObject object) {
		getBlock(object.getHeight()).addObject(object);
	}

	/** Removes an object from this region. */
	public void removeObject(GameObject object) {
		getBlock(object.getHeight()).removeObject(object);
	}

	/** Adds a ground item to this region. */
	public void addGroundItem(GroundItem item) {
		getBlock(item.getHeight()).addGroundItem(item);
	}

	/** Adds a ground item to this region. */
	public void removeGroundItem(GroundItem item) {
		getBlock(item.getHeight()).removeGroundItem(item);
	}

	public boolean containsNpc(int height, Npc npc) {
		return getBlock(height).containsNpc(npc);
	}

	public boolean containsPlayer(int height, Player player) {
		return getBlock(height).containsPlayer(player);
	}

	/** @return {@code true} if object is in region */
	public boolean containsObject(int height, GameObject object) {
		return getBlock(height).containsObject(object);
	}

	/** @return {@code true} if object occupies position */
	public boolean containsObject(Position position) {
		return getBlock(position.getHeight()).containsObject(position);
	}

	/** @return a set of ground items on the position */
	public Set<GroundItem> getGroundItems(Position position) {
		return getBlock(position.getHeight()).getGroundItems(position);
	}

	/** @return {@code true} if object with given id occupies position */
	public GameObject getGameObject(int id, Position position) {
		return getBlock(position.getHeight()).getGameObject(id, position);
	}

	public List<GameObject> getObjects(Position position) {
		return getBlock(position.getHeight()).getGameObjects(position);
	}

	/** Sends ground items in this region to the {@code player}. */
	public void sendGroundItems(Player player) {
		getBlock(player.getHeight()).sendGroundItems(player);
	}

	/** Sends game objects in this region to the {@code player}. */
	public void sendGameObjects(Player player) {
		getBlock(player.getHeight()).sendGameObjects(player);
	}

	/** @return {@code true} if the region contains the position */
	public boolean contains(Position position) {
		return getX() == position.getRegionX() && getY() == position.getRegionY();
	}

	/** @return {@code true} if the region contains the position */
	public boolean contains(int x, int y) {
		return getX() == ((x >> 3) - 6) >> 3 && getY() == ((y >> 3) - 6) >> 3;
	}

	private RegionBlock getBlock(int height) {
		if (height < 0) {
			height = 0;
			
			//throw new IllegalArgumentException(
				//	"Height is out of bounds. Received (" + height + "), expected range [0, " + HEIGHT_LEVELS + "].");
		}

		if (blocks[height] == null) {
			blocks[height] = new RegionBlock();
		}

		return blocks[height];
	}

	public static boolean reachable(Interactable source, Interactable target) {
		return reachable(source.getPosition(), source.width(), source.length(), target.getPosition(), target.width(),
				target.length());
	}

	public static boolean reachable(Position source, int sourceWidth, int sourceLength, Position target,
			int targetWidth, int targetLength) {
		if (Utility.inside(source, sourceWidth, sourceLength, target, targetWidth, targetLength)) {
			return targetWidth == 0 || targetLength == 0;
		}

		int x, y;
		int sourceTopX = source.getX() + sourceWidth - 1;
		int sourceTopY = source.getY() + sourceLength - 1;
		int targetTopX = target.getX() + targetWidth - 1;
		int targetTopY = target.getY() + targetLength - 1;

		if (sourceTopY == target.getY() - 1 && source.getX() >= target.getX() && source.getX() <= targetTopX) {
			for (x = 0, y = sourceLength - 1; x < sourceWidth; x++)
				if (TraversalMap.blockedNorth(source.transform(x, y)))
					return false;
			return true;
		}

		if (sourceTopX == target.getX() - 1 && source.getY() >= target.getY() && source.getY() <= targetTopY) {
			for (x = 0, y = 0; y < sourceLength; y++)
				if (TraversalMap.blockedEast(source.transform(x, y)))
					return false;
			return true;
		}

		if (source.getY() == targetTopY + 1 && source.getX() >= target.getX() && source.getX() <= targetTopX) {
			for (x = 0, y = 0; x < sourceWidth; x++)
				if (TraversalMap.blockedSouth(source.transform(x, y)))
					return false;
			return true;
		}

		if (source.getX() == targetTopX + 1 && source.getY() >= target.getY() && source.getY() <= targetTopY) {
			for (x = sourceWidth - 1, y = 0; y < sourceLength; y++)
				if (TraversalMap.blockedWest(source.transform(x, y)))
					return false;
			return true;
		}

		return false;
	}

	public void skip(GameObject gameObject) {
		getBlock(gameObject.getHeight()).skip(gameObject);
	}

	@Override
	public String toString() {
		return "Region[" + x + "_" + y + "]";
	}

	public int getId() {
		return id;
	}

	public static void setMobOnTile(int size, Position position) {
		int len = size - 1;
		if (len < 1)
			len = 1;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				int xx = x % len;
				int yy = y % len;
				if (xx == 0 && yy == 0 || xx != 0 && yy != 0)
					continue;
				Position next = position.transform(x, y);
				next.getRegion().getTile(next.getHeight(), next.getX() & 0x3F, next.getY() & 0x3F).setMobOnTile(true);
			}
		}
	}

	public static void removeMobOnTile(int size, Position position) {
		int len = size - 1;
		if (len < 1)
			len = 1;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				int xx = x % len;
				int yy = y % len;
				if (xx == 0 && yy == 0 || xx != 0 && yy != 0)
					continue;
				Position next = position.transform(x, y);
				next.getRegion().getTile(next.getHeight(), next.getX() & 0x3F, next.getY() & 0x3F).setMobOnTile(false);
			}
		}
	}

	public static boolean isMobOnTile(int size, Position position) {
		int len = size - 1;
		if (len < 1)
			len = 1;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				int xx = x % len;
				int yy = y % len;
				if (xx == 0 && yy == 0 || xx != 0 && yy != 0)
					continue;
				Position next = position.transform(x, y);
				if (next.getRegion().getTile(next.getHeight(), next.getX() & 0x3F, next.getY() & 0x3F).isMobOnTile())
					return true;
			}
		}
		return false;
	}
}
