package io.server.game.world.object;

import static io.server.game.world.object.ObjectDirection.NORTH;
import static io.server.game.world.object.ObjectDirection.SOUTH;

import java.util.Objects;

import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.net.packet.out.SendAddObject;
import io.server.net.packet.out.SendRemoveObject;
import io.server.util.Utility;
import io.server.util.generic.GenericAttributes;

/**
 * Represents a static game object loaded from the map fs.
 *
 * @author Michael | Chex
 */
public class StaticGameObject implements GameObject {

	/** The object definition. */
	public ObjectDefinition definition;

	/** The generic attributes. */
	public GenericAttributes genericAttributes;

	/** The object position coordinates. */
	public final Position position;

	/** The object type. */
	public ObjectType type;

	/** A byte holding the object rotation. */
	public ObjectDirection direction;

	/** Creates the game object. */
	public StaticGameObject(ObjectDefinition definition, Position position, ObjectType type,
			ObjectDirection direction) {
		this.definition = definition;
		this.position = position;
		this.type = type;
		this.direction = direction;
	}

	@Override
	public GenericAttributes getGenericAttributes() {
		if (genericAttributes == null) {
			genericAttributes = new GenericAttributes();
		}
		return genericAttributes;
	}

	@Override
	public ObjectDefinition getDefinition() {
		return definition;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public int width() {
		if (direction == NORTH || direction == SOUTH) {
			return definition.length;
		}
		return definition.width;
	}

	@Override
	public int length() {
		if (direction == NORTH || direction == SOUTH) {
			return definition.width;
		}
		return definition.length;
	}

	@Override
	public ObjectType getObjectType() {
		return type;
	}

	@Override
	public ObjectDirection getDirection() {
		return direction;
	}

	@SuppressWarnings("static-access")
	@Override
	public void register() {
		Region objectRegion = getPosition().getRegion();
		if (!objectRegion.containsObject(getHeight(), this)) {
			TraversalMap.markObject(objectRegion, this, true, true);
			for (Region region : World.getRegions().getSurroundingRegions(getPosition())) {
				for (Player other : region.getPlayers(getHeight())) {
					if (other.instance != getInstancedHeight())
						continue;
					if (Utility.withinViewingDistance(this, other, Region.VIEW_DISTANCE)) {
						other.send(new SendAddObject(this));
					}
				}
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void unregister() {
		Region objectRegion = getPosition().getRegion();
		if (objectRegion.containsObject(getHeight(), this)) {
			TraversalMap.markObject(objectRegion, this, false, true);
			for (Region region : World.getRegions().getSurroundingRegions(getPosition())) {
				for (Player other : region.getPlayers(getHeight())) {
					if (other.instance != getInstancedHeight())
						continue;
					if (Utility.withinViewingDistance(this, other, Region.VIEW_DISTANCE)) {
						other.send(new SendRemoveObject(this));
					}
				}
			}
		}
	}

	@Override
	public void transform(int id) {
		unregister();
		definition = ObjectDefinition.lookup(id);
		register();
	}

	@Override
	public void rotate(ObjectDirection direction) {
		unregister();
		this.direction = direction;
		register();
	}

	@Override
	public boolean active() {
		return getPosition().getRegion().containsObject(getHeight(), this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(definition.getId(), position);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof StaticGameObject) {
			StaticGameObject other = (StaticGameObject) obj;
			return definition == other.definition && position.equals(other.position);
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("StaticGameObject[id=%s, loc=%s, width=%s, len=%s, rot=%s, type=%s]", getId(),
				getPosition(), width(), length(), getDirection(), getObjectType());
	}
}
