package io.server.content.mysterybox2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.server.content.mysterybox.impl.AllVsOneV3Box;
import io.server.content.mysterybox.impl.DoubleThreat;
import io.server.content.mysterybox.impl.InfernoBox;
import io.server.content.mysterybox2.impl.PetMysteryBox;
import io.server.game.world.items.Item;

/**
 * Mystery Box
 * @author Adam_#6723
 */

public abstract class MysteryBox {

    /** The map containing all the mystery boxes. */
    private static Map<Integer, MysteryBox> MYSTERY_BOXES = new HashMap<>();

    /** Handles loading the mystery boxes. */
    public static void load() {
        MysteryBox INFERNO_BOX = new InfernoBox();
        MysteryBox PET_BOX = new PetMysteryBox();

        MysteryBox ALL_VS_V3 = new AllVsOneV3Box();
        MysteryBox DOUBLE_THREAT = new DoubleThreat();

        MYSTERY_BOXES.put(INFERNO_BOX.item(), INFERNO_BOX);
        MYSTERY_BOXES.put(PET_BOX.item(), PET_BOX);
        MYSTERY_BOXES.put(ALL_VS_V3.item(), ALL_VS_V3);
        MYSTERY_BOXES.put(DOUBLE_THREAT.item(), DOUBLE_THREAT);

    }

    /** Handles getting the mystery box. */
    static Optional<MysteryBox> getMysteryBox(int item) {
        return MYSTERY_BOXES.containsKey(item) ? Optional.of(MYSTERY_BOXES.get(item)) : Optional.empty();
    }

    /** The name of the mystery box. */
    protected abstract String name();

    /** The item identification of the mystery box. */
    protected abstract int item();

    /** The amount considered for the item to be a rare item. */
    protected abstract int rareValue();

    /** The rewards for the mystery box. */
    protected abstract Item[] rewards();
}
