package io.server.game.world.entity.combat.strategy.npc.boss.arena;

import java.util.concurrent.TimeUnit;

import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.Stopwatch;

public class ArenaEvent extends Task {
	private Npc arena;
	private boolean initial;
	private final Stopwatch stopwatch = Stopwatch.start();

	public ArenaEvent() {
		super(false, 100);
		this.arena = null;
		this.initial = true;
	}

	@Override
	public void execute() {
		if ((arena == null || !arena.isRegistered()) && !initial) {
			initial = true;
			stopwatch.reset();
		}

		if (initial) {
			if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 60) {
				arena = ArenaUtility.generateSpawn();
				initial = false;
				stopwatch.reset();
			}
			return;
		}

		if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 15) {// 15 minutes
			initial = true;
			stopwatch.reset();
			if (arena != null) {
				arena.speak("Pathetic humans could not kill me! Muhahaha");
				World.schedule(2, () -> arena.unregister());
			}
			World.sendMessage("<col=ff0000> arena has disappeared! He will return in 30 minutes.");
		} else if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 10) {// 10 minutes
			World.sendMessage("<col=ff0000> arena will disappear in 5 minutes!");
		} else if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 5) {// 5 minutes
			World.sendMessage("<col=ff0000> arena will disappear in 10 minutes!");
		}
	}
}
