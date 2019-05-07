
package io.server.game.event.impl;

import io.server.game.event.Event;
import io.server.game.world.object.GameObject;

public class ObjectClickEvent implements Event {

	private final int type;

	private final GameObject object;

	public ObjectClickEvent(int type, GameObject object) {
		this.type = type;
		this.object = object;
	}

	public int getType() {
		return type;
	}

	public GameObject getObject() {
		return object;
	}

}
