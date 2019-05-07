package io.server.game.world.entity.combat.attack.listener.item;

import io.server.game.world.entity.combat.attack.listener.ItemCombatListenerSignature;
import io.server.game.world.entity.combat.attack.listener.SimplifiedListener;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles the proselyte armour effect, which if equipped reduces drain rate by
 * 10%
 *
 * @author Adam_#6723
 */
@ItemCombatListenerSignature(requireAll = false, items = { 9672, 9674, 9676 })
public class ProstateArmourSet extends SimplifiedListener<Player> {


}
