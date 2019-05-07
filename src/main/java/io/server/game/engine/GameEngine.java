package io.server.game.engine;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.util.concurrent.AbstractScheduledService;

import io.server.Config;
import io.server.game.engine.sync.ClientSynchronizer;
import io.server.game.engine.sync.ParallelClientSynchronizer;
import io.server.game.engine.sync.SequentialClientSynchronizer;
import io.server.game.world.World;
import io.server.game.world.entity.MobList;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.session.GameSession;
import io.server.util.Stopwatch;

public final class GameEngine extends AbstractScheduledService {

	private static final Logger logger = LogManager.getLogger();

	private final ClientSynchronizer synchronizer;

	@Override
	protected String serviceName() {
		return "GameThread";
	}

	public GameEngine() {
		synchronizer = (Config.PARALLEL_GAME_ENGINE ? new ParallelClientSynchronizer()
				: new SequentialClientSynchronizer());
	}

	@Override
	protected void runOneIteration() throws Exception {
		final Stopwatch stopwatch = Stopwatch.start();
		final Stopwatch stopwatch2 = Stopwatch.start();

		World world = World.get();
		MobList<Player> players = World.getPlayers();
		MobList<Npc> npcs = World.getNpcs();

		world.dequeLogins();

		long elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);

		if (elapsed > 10 && Config.SERVER_DEBUG) {
			System.out.println(String.format("world.dequeLogins(): %d ms", elapsed));
		}
		stopwatch.reset();

		world.dequeLogouts();

		elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);

		if (elapsed > 10 && Config.SERVER_DEBUG) {
			System.out.println(String.format("world.dequeLogouts(): %d ms", elapsed));
		}
		stopwatch.reset();

		world.process();

		elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);

		if (elapsed > 10 && Config.SERVER_DEBUG) {
			System.out.println(String.format("world.process(): %d ms", elapsed));
		}
		stopwatch.reset();

		players.forEach(player -> {
			player.getSession().ifPresent(GameSession::processClientPackets);
			try {
				player.sequence();
			} catch (Exception ex) {
				logger.error(String.format("error player.sequence(): %s ms", player), ex);
			}
		});

		elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);

		if (elapsed > 10 && Config.SERVER_DEBUG) {
			System.out.println(String.format("player.sequence(): %d ms", elapsed));
		}
		stopwatch.reset();

		npcs.forEach(Npc::sequence);

		elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);

		if (elapsed > 10 && Config.SERVER_DEBUG) {
			System.out.println(String.format("npc.sequence(): %d ms", elapsed));
		}
		stopwatch.reset();

		try {
			synchronizer.synchronize(players, npcs);
		} catch (Exception ex) {
			logger.fatal("Error in the main game sequencer.", ex);
		}

		elapsed = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);

		if (elapsed > 10 && Config.SERVER_DEBUG) {
			System.out.println(String.format("synchronizer.synchronize(): %d ms", elapsed));
		}

		stopwatch.reset();

		if (stopwatch2.elapsedTime(TimeUnit.MILLISECONDS) > 60 && Config.SERVER_DEBUG) {
			System.out.println(String.format("CYCLE END: %d ms", stopwatch2.elapsedTime(TimeUnit.MILLISECONDS)));
		}

	}

	@Override
	protected Scheduler scheduler() {
		return Scheduler.newFixedRateSchedule(600, 600, TimeUnit.MILLISECONDS);
	}

}
