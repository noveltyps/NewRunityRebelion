package io.server.game.action.impl;

import io.server.content.skill.impl.magic.spell.Spell;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;

/**
 * Handles the spell casting action.
 * 
 * @author Daniel
 */
public final class SpellAction extends Action<Player> {
	private final Spell spell;
	private final Item item;

	/** Creates the <code>SpellAction</code>. */
	public SpellAction(Player player, Spell spell, Item item) {
		super(player, 3);
		this.spell = spell;
		this.item = item;
	}

	@Override
	protected void onSchedule() {
		spell.execute(getMob(), item);
	}

	@Override
	public void execute() {
		cancel();
	}

	@Override
	public String getName() {
		return "spell-action";
	}

	@Override
	public boolean prioritized() {
		return true;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.WALKABLE;
	}

}
