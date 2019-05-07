package io.server.game.action.impl;

import io.server.content.skill.SkillRepository;
import io.server.content.skill.impl.hunter.Hunter;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.Utility;

/**
 * Teleports an entity to another part of the world.
 * 
 * @author Daniel
 */
public final class ImplingTeleportAction extends Action<Npc> {

	public ImplingTeleportAction(Npc entity) {
		super(entity, 180);
	}

	@Override
	protected void onSchedule() {
		getMob().transform(Utility.randomElement(SkillRepository.HUNTER_SPAWNS));
	}

	@Override
	public void execute() {
		getMob().move(getMob().spawnPosition);
		getMob().locking.unlock();
		Hunter.SPAWNS.put(getMob().id, getMob().spawnPosition);
		cancel();
	}

	@Override
	public String getName() {
		return "Impling teleport";
	}

	@Override
	public boolean prioritized() {
		return false;
	}

	@Override
	public WalkablePolicy getWalkablePolicy() {
		return WalkablePolicy.NON_WALKABLE;
	}

}
