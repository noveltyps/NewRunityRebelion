package io.server.game.world.items.containers.equipment;

import java.util.Arrays;

/**
 * The enumerated types of a players equipped item slots.
 * 
 * @author Daniel
 */
public enum EquipmentType {
	NOT_WIELDABLE(-1), HAT(0), HELM(0), MASK(0), FACE(0), CAPE(1), SHIELD(5), GLOVES(9), BOOTS(10), AMULET(2), RING(12),
	ARROWS(13), BODY(4), TORSO(4), LEGS(7), WEAPON(3);

	private final int slot;

	EquipmentType(final int slot) {
		this.slot = slot;
	}

	public static EquipmentType lookup(int slot) {
		return Arrays.stream(values()).filter(it -> it.slot == slot).findFirst().orElse(EquipmentType.NOT_WIELDABLE);
	}

	public int getSlot() {
		return slot;
	}

}
