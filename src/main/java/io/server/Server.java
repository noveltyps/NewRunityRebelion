package io.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.server.content.WellOfGoodwill;
import io.server.content.activity.annoucements.Announcement;
import io.server.content.activity.record.GlobalRecords;
import io.server.content.clanchannel.ClanRepository;
import io.server.content.discord.Discord;
import io.server.content.itemaction.ItemActionRepository;
import io.server.content.mysterybox2.MysteryBox;
import io.server.content.skill.SkillRepository;
import io.server.content.store.PersonalStoreSaver;
import io.server.content.triviabot.TriviaBot;
import io.server.fs.cache.FileSystem;
import io.server.fs.cache.decoder.MapDefinitionDecoder;
import io.server.fs.cache.decoder.ObjectDefinitionDecoder;
import io.server.fs.cache.decoder.RegionDecoder;
import io.server.game.engine.GameEngine;
import io.server.game.plugin.PluginManager;
import io.server.game.service.NetworkService;
import io.server.game.service.StartupService;
import io.server.game.task.impl.BotStartupEvent;
import io.server.game.task.impl.ClanUpdateEvent;
import io.server.game.task.impl.DoubleExperienceEvent;
import io.server.game.task.impl.MessageEvent;
import io.server.game.task.impl.PlayerSaveEvent;
import io.server.game.task.impl.RoyaltyEvent;
import io.server.game.world.World;
import io.server.game.world.entity.combat.attack.listener.CombatListenerManager;
import io.server.game.world.entity.combat.strategy.npc.boss.arena.ArenaEvent;
import io.server.game.world.entity.combat.strategy.npc.boss.galvek.GalvekEvent;
import io.server.game.world.entity.combat.strategy.npc.boss.magearena.PorazdirEvent;
import io.server.game.world.entity.combat.strategy.npc.boss.skotizo.SkotizoEvent;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.profile.ProfileRepository;
import io.server.game.world.entity.mob.player.punishments.PunishmentExecuter;
import io.server.game.world.items.ItemDefinition;
import io.server.io.PacketListenerLoader;
import io.server.omen.endpoints.LoadComputerEndpoint;
import io.server.omen.endpoints.RegisterComputerEndpoint;
import io.server.util.GameSaver;
import io.server.util.Logger;
import io.server.util.Referals;
import io.server.util.Stopwatch;
import io.server.util.parser.impl.CombatProjectileParser;
import io.server.util.parser.impl.GlobalObjectParser;
import io.server.util.parser.impl.NpcDropParser;
import io.server.util.parser.impl.NpcForceChatParser;
import io.server.util.parser.impl.NpcSpawnParser;
import io.server.util.parser.impl.ObjectRemovalParser;
import io.server.util.parser.impl.PacketSizeParser;
import io.server.util.parser.impl.StoreParser;
import io.server.util.sql.MySqlConnector;
import io.server.util.tools.ItemStatsDumper;
import neytorokx.Omen;
import plugin.click.item.ClueScrollPlugin;


/**
 * 
 * @author Adam_#6723
 *
 */

public final class Server {

	public static final AtomicBoolean serverStarted = new AtomicBoolean(false);

	
	public static final Stopwatch UPTIME = Stopwatch.start();

	// services
	private static final StartupService startupService = new StartupService();
	private static final GameEngine gameService = new GameEngine();
	private static final NetworkService networkService = new NetworkService();

	private static final Server INSTANCE = new Server();
	
	private static final Omen OMEN = new Omen(2080);

	private Server() {

	}

	private void processSequentialStatupTasks() {
		Logger.log("Preparing Sequential Start Up Tasks...");
		if (Config.MY_SQL) {
			try {
				MySqlConnector.run();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			Logger.log("Preparing Object/Region Decoding... ");
			new ObjectRemovalParser().run();
			FileSystem fs = FileSystem.create(System.getProperty("user.home") + "/RunityCache/");
			new ObjectDefinitionDecoder(fs).run();
			new MapDefinitionDecoder(fs).run();
			new RegionDecoder(fs).run();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ItemDefinition.createParser().run();
		NpcDefinition.createParser().run();
		new CombatProjectileParser().run();
		CombatListenerManager.load();
		new NpcSpawnParser().run();
		new NpcDropParser().run();
		new NpcForceChatParser().run();
		new StoreParser().run();
		new GlobalObjectParser().run();
		PunishmentExecuter.init();
		new Discord().start();
		try {
			Referals.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	//	new NpcDefinitionParser().run();
	//	new NpcDefParser().run();
		//5 seconds of work, stfu shit amazon i get paid by 3 different servers 4ner
		
		ItemStatsDumper.printStats();
	}

	/**
	 * Called after the sequential startup tasks, use this for faster startup. Try
	 * not to use this method for tasks that rely on other tasks or you'll run into
	 * issues.
	 */
	private void processParallelStatupTasks() {
		startupService.submit(new PacketSizeParser());
		startupService.submit(new PacketListenerLoader());
		startupService.submit(TriviaBot::declare);
		startupService.submit(ClanRepository::loadChannels);
		startupService.submit(GlobalRecords::load);
		startupService.submit(SkillRepository::load);
		startupService.submit(ProfileRepository::load);
        startupService.submit(MysteryBox::load);
		startupService.submit(PersonalStoreSaver::loadPayments);
		startupService.submit(ItemActionRepository::declare);
		startupService.submit(ClueScrollPlugin::declare);
		startupService.submit(GameSaver::load);
		startupService.shutdown();
	}

	/**
	 * Called when the game engine is running and all the startup tasks have
	 * finished loading
	 */
	private void onStart() {
		if (WellOfGoodwill.isActive()) {
			World.schedule(new DoubleExperienceEvent());
		}
		
		World.schedule(new Announcement());
		World.schedule(new MessageEvent());
		World.schedule(new ClanUpdateEvent());
		World.schedule(new PlayerSaveEvent());
		World.schedule(new BotStartupEvent());
		World.schedule(new SkotizoEvent());
		World.schedule(new PorazdirEvent());
		World.schedule(new ArenaEvent());
		World.schedule(new GalvekEvent());
		World.schedule(new RoyaltyEvent());
	   //	World.schedule(new DailyServerBonuses());
	   // World.schedule(new FreeForAll());
		Logger.log("Events have been scheduled");
		Logger.log("World Schdule Events have loaded adam.");
	}

	public void start() throws Exception {

		Logger.tag();

		Logger.log(String.format("Game Engine=%s", Config.PARALLEL_GAME_ENGINE ? "Parallel" : "Sequential"));
		processSequentialStatupTasks();
		processParallelStatupTasks();
		
		OMEN.registerEndpoint("/register", new RegisterComputerEndpoint());
		OMEN.registerEndpoint("/loadCompID", new LoadComputerEndpoint());
		OMEN.startServer();

		startupService.awaitUntilFinished(5, TimeUnit.MINUTES);
		Logger.log("Startup service finished");

		PluginManager.load();

		gameService.startAsync().awaitRunning();
		Logger.log("Game service started");

		onStart();

		networkService.start(Config.SERVER_PORT);
		
	}

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Logger.log("Shutting down server, initializing shutdown hook");
			World.save();
		}));

		try {
			INSTANCE.start();
		} catch (Throwable t) {
			Logger.error("A problem has been encountered while starting the server.");
			Logger.parent(t.getMessage());
		}
	

	}
	

	public static Server getInstance() {
		return INSTANCE;
	}

}
