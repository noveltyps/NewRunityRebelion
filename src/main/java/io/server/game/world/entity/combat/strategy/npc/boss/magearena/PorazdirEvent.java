package io.server.game.world.entity.combat.strategy.npc.boss.magearena;

import java.util.concurrent.TimeUnit;

import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.util.Stopwatch;

public class PorazdirEvent extends Task {
	private Npc Porazdir;
	private boolean initial;
	private final Stopwatch stopwatch = Stopwatch.start();

	public PorazdirEvent() {
		super(false, 100);
		this.Porazdir = null;
		this.initial = true;
	}

	@Override
	public void execute() {
		System.err.println("Stopwatch: 1 " + stopwatch);
		if ((Porazdir == null || !Porazdir.isRegistered()) && !initial) {
			initial = true;
			stopwatch.reset();
		}

		if (initial) {
			if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 30) {
				Porazdir = PorazdirUtility.generatePorazdirSpawn();
				initial = false;
				stopwatch.reset();
			}
			return;
		}

		if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 30) {// 15 minutes
			initial = true;
			stopwatch.reset();
			if (Porazdir != null) {
				Porazdir.speak("Pathetic humans could not kill me! Muhahaha");
				World.schedule(2, () -> Porazdir.unregister());
			}
			World.sendMessage("<col=ff0000> Porazdir has disappeared! He will return in 30 minutes.");
		} else if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 30) {// 10 minutes
			World.sendMessage("<col=ff0000> Porazdir will disappear in 30 minutes!");
		} else if (stopwatch.elapsedTime(TimeUnit.MINUTES) == 30) {// 5 minutes
			World.sendMessage("<col=ff0000> Porazdir will disappear in 30 minutes!");
		}
		System.err.println("Stopwatch: " + stopwatch);
	}
}
