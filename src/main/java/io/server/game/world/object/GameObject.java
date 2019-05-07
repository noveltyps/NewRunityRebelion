package io.server.game.world.object;

import io.server.game.world.Interactable;
import io.server.game.world.entity.Entity;
import io.server.game.world.position.Position;
import io.server.util.generic.GenericAttributes;

/**
 * Represents a game object.
 *
 * @author Michael | Chex
 */
public interface GameObject extends Interactable {

	default int getInstancedHeight() {
		return Entity.DEFAULT_INSTANCE_HEIGHT;
	}

	/** Gets the generic attributes. */
	GenericAttributes getGenericAttributes();

	/** Gets the object definition. */
	ObjectDefinition getDefinition();

	/** Gets the regional location. */
	Position getPosition();

	/** Gets the width. */
	int width();

	/** Gets the length. */
	int length();

	/** Gets the object type. */
	ObjectType getObjectType();

	/** Gets the rotation. */
	ObjectDirection getDirection();

	/** Registers the game object. */
	void register();

	/** Unregisters the game object. */
	void unregister();

	/** Determines if this object is active on the world. */
	boolean active();

	/** Gets the object id. */
	default int getId() {
		return getDefinition().getId();
	}

	void transform(int id);

	void rotate(ObjectDirection direction);

}
