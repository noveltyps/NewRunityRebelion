package io.server.game.world;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.server.Config;
import io.server.content.activity.impl.battlerealm.BattleRealm;
import io.server.content.activity.impl.pestcontrol.PestControl;
import io.server.content.activity.record.GlobalRecords;
import io.server.content.bot.PlayerBot;
import io.server.content.clanchannel.ClanRepository;
import io.server.content.eventboss.EventBossHandler;
import io.server.content.referral.ReferralHandler;
import io.server.content.worldevent.WorldEventHandler;
import io.server.game.Graphic;
import io.server.game.Projectile;
import io.server.game.event.bus.DataBus;
import io.server.game.event.listener.WorldEventListener;
import io.server.game.task.Task;
import io.server.game.task.TaskManager;
import io.server.game.task.impl.PlayerRemovalTask;
import io.server.game.task.impl.SystemUpdateEvent;
import io.server.game.world.entity.MobList;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.NpcAssistant;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.game.world.region.RegionManager;
import io.server.net.packet.out.SendGameMessage;
import io.server.net.packet.out.SendGraphic;
import io.server.net.packet.out.SendKillFeed;
import io.server.net.packet.out.SendLogout;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendObjectAnimation;
import io.server.net.packet.out.SendProjectile;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendWidget;
import io.server.util.GameSaver;
import io.server.util.Utility;

/**
 * Represents the game world.
 *
 * @author Daniel
 * @author Michael
 */
public final class World {

	private static final Logger logger = LogManager.getLogger();

	public static ReferralHandler referralHandler = new ReferralHandler();
	public static WorldEventHandler eventHandler = new WorldEventHandler();

	/** The players registered in this world. */
	public final MobList<Player> players = new MobList<>(Config.MAX_PLAYERS);

	/** The npcs registered in this world. */
	private final MobList<Npc> npcs = new MobList<>(Config.MAX_NPCS);

	/** The {@link Player}s waiting to login. */
	private final Queue<Player> logins = new ConcurrentLinkedQueue<>();

	/** The {@link Player}s waiting to onLogout. */
	private final Queue<Player> logouts = new ConcurrentLinkedQueue<>();

	/** The task manager. */
	private final TaskManager taskManager = new TaskManager();

	/** The region manager. */
	private final RegionManager regionManager = new RegionManager();

	public static final AtomicBoolean update = new AtomicBoolean(false);

	private static final DataBus dataBus = DataBus.getInstance();

	/** The world instance. */
	private static final World WORLD = new World();

	private World() {
		dataBus.subscribe(new WorldEventListener());
		eventHandler.execute();
	}

	/** Handles the world sequencing. */
	public void sequence() {

	}

	/** Saves all the game data. */
	public static void save() {
		System.out.println("Saving RebelionXOS...");
		get().players.forEach(PlayerSerializer::save);
		logger.info("All players were successfully saved.");
		GlobalRecords.save();
		logger.info("All global records were successfully saved.");
		ClanRepository.saveAllActiveClans();
		logger.info("All clans were successfully saved.");
		GameSaver.save();
		logger.info("All game data were successfully saved.");
	}

	/** Updates the server. */
	public static void update(int miliseconds, boolean restart) {
		if (!update.get()) {
			update.set(true);
			schedule(new SystemUpdateEvent(miliseconds * 60, restart));
			get().players.stream().forEach(
					it -> it.send(new SendGameMessage(0, miliseconds, "System " + (restart ? "restart" : "update") + " in:")));
		}
	}

	/** Shuts down the server. */
	public static void shutdown() {
		save();
		get().players.forEach(p -> p.logout(true));
		System.out.println("shutting down...");
		World.schedule(15, () -> System.exit(0));
	}

	/** Restarts the server. */
	public static void restart() {
		System.out.println("restarting server...");
		save();
		try {
			Runtime.getRuntime().exec("cmd /c start Run.bat");
		} catch (IOException e) {
			e.printStackTrace();
		}
		get().players.forEach(p -> p.logout(true));
		World.schedule(15, () -> System.exit(0));
	}


	/** Handles queueing the player logins. */
	public static void queueLogin(Player player) {
		if (player.isBot) {
			get().logins.add(player);
		} else {
			player.getSession().ifPresent(it -> World.get().logins.add(player));
		}
	}

	/** Handles queueing the player logouts. */
	public static void queueLogout(Player player) {
		if (player == null || get().logouts.contains(player)) 
			return;
		get().logouts.add(player);
	}

	/** Gets a player by name. */
	public static Optional<Player> search(String name) {
		for (Player player : get().players) {
			if (player == null) {
				continue;
			}

			if (player.getUsername().equalsIgnoreCase(name)) {
				return Optional.of(player);
			}
		}
		return Optional.empty();
	}

	public static Optional<Player> searchAll(String name) {
		for (Player player : get().players) {
			if (player == null) {
				continue;
			}

			if (player.getUsername().equalsIgnoreCase(name)) {
				return Optional.of(player);
			}
		}

		for (Player player : World.get().getLogins()) {
			if (player == null) {
				continue;
			}

			if (player.getUsername().equalsIgnoreCase(name)) {
				return Optional.of(player);
			}
		}
		return Optional.empty();
	}

	/**
	 * Returns a player within an optional whose name hash is equal to {@code
	 * username}.
	 */
	public static Optional<Player> getPlayerByHash(long usernameHash) {
		return get().players.findFirst(it -> Objects.equals(it.usernameLong, usernameHash));
	}

	/** Gets a player by index. */
	public static Optional<Player> getPlayerBySlot(int index) {
		return get().players.stream().filter($it -> $it.getIndex() == index).findFirst();
	}

	/** Gets an npc by index. */
	public static Optional<Npc> getNpcBySlot(int index) {
		return get().npcs.stream().filter($it -> $it.getIndex() == index).findFirst();
	}

	/** Kicks all valid players that meet a certain condition */
	public static void kickPlayer(String name) {
		search(name).ifPresent(it -> it.logout(true));
	}

	public static void kickPlayer(Predicate<Player> condition) {
		for (Player player : get().players) {
			if (player.isBot)
				continue;
			if (!condition.test(player))
				continue;
			player.send(new SendLogout());
			queueLogout(player);
		}
	}

	public void process() {
		synchronized (World.class) {
			try {
				taskManager.processTasks();
				PestControl.sequenceMinigame();
				BattleRealm.sequenceMinigame();
			} catch (Exception ex) {
				logger.error("Error sequencing pest control or BattleRealm", ex);
			}
		}
	}

	/** Submits a new event. */
	public static void schedule(Task task) {
		TaskManager.schedule(task);
	}

	/** Executes a runnable on a delay. */
	public static void schedule(int delay, Runnable runnable) {
		schedule(new Task(delay) {
			@Override
			public void execute() {
				runnable.run();
				cancel();
			}
		});
	}

	/** Sends a graphic to the world. */
	public static void sendGraphic(Graphic graphic, Position position, int instance) {
		for (Player player : getPlayers()) {
			if (player.instance != instance)
				continue;
			if (!Utility.withinDistance(player, position, Region.VIEW_DISTANCE))
				continue;
			player.send(new SendGraphic(graphic, position));
		}
	}

	/** Sends a graphic to the world. */
	public static void sendGraphic(Graphic graphic, Position position) {
		get().players.stream().filter(player -> Utility.withinDistance(player, position, Region.VIEW_DISTANCE))
				.forEach(player -> {
					player.send(new SendGraphic(graphic, position));
				});
	}

	/** Sends a world object animation. */
	public static void sendObjectAnimation(int animation, GameObject object) {
		get().players.stream().filter($it -> Utility.withinDistance(object, $it, Region.VIEW_DISTANCE)).forEach($it -> {
			$it.send(new SendObjectAnimation(animation, object));
		});
	}

	/** Sends a world projectile. */
	public static void sendProjectile(Projectile projectile, Position position, int lock, byte offsetX, byte offsetY) {
		get().players.stream().filter($it -> Utility.withinDistance($it, position, Region.VIEW_DISTANCE))
				.forEach($it -> $it.send(new SendProjectile(projectile, position, lock, offsetX, offsetY)));
	}

	/** Sends a world projectile. */
	public static void sendProjectile(Mob source, Mob target, Projectile projectile) {
		int lockon = target.isNpc() ? target.getIndex() + 1 : -target.getIndex() - 1;
		int sourceX = source.getX() + source.width() / 2, sourceY = source.getY() + source.length() / 2;
		int targetX = target.getX() + target.width() / 2, targetY = target.getY() + target.length() / 2;
		byte offsetX = (byte) (targetX - sourceX);
		byte offsetY = (byte) (targetY - sourceY);
		Position center = new Position(sourceX, sourceY);
		get().players.stream().filter(
				$it -> source.instance == $it.instance && Utility.withinDistance(target, $it, Region.VIEW_DISTANCE))
				.forEach($it -> $it.send(new SendProjectile(projectile, center, lockon, offsetX, offsetY)));
	}

	/** Sends a world projectile. */
	public static void sendProjectile(Mob source, Position target, Projectile projectile) {
		int sourceX = source.getX() + source.width() / 2, sourceY = source.getY() + source.length() / 2;
		Position center = new Position(sourceX, sourceY);
		byte offsetX = (byte) (target.getX() - sourceX);
		byte offsetY = (byte) (target.getY() - sourceY);
		get().players.stream().filter($it -> source.instance == $it.instance && Utility.withinDistance($it, target, 15))
				.forEach($it -> $it.send(new SendProjectile(projectile, center, -1, offsetX, offsetY)));
	}

	/** Sends a global message. */
	public static void sendMessage(String... messages) {
		for (Player player : getPlayers()) {
			if (player != null)
				player.message(messages);
		}
	}

	/** Sends a global message with an exception. */
	public static void sendMessage(String message, Predicate<Player> filter) {
		for (Player player : getPlayers()) {
			if (player != null && filter.test(player))
				player.message(message);
		}
	}
	
	public static void sendTeleportButtonNpc(int npcId) {
		for (Player player : getPlayers()) {
			player.send(new SendString("" + npcId, 45615));
		}
	}

	public static void sendTeleportButton() {
		for (Player player : getPlayers()) {
			player.send(new SendString("1", 45600));
		}
	}

	public static void hideTeleportButton() {
		for (Player player : getPlayers()) {
			player.send(new SendString("0", 45600));

		}
	}

	public static void sendTeleportButtonNpc1(int npcId) {
		for (Player player : getPlayers()) {
			player.send(new SendString("" + npcId, 46615));
		}
	}

	public static void sendTeleportButton1() {
		for (Player player : getPlayers()) {
			player.send(new SendString("1", 46600));
		}
	}

	public static void hideTeleportButton1() {
		for (Player player : getPlayers()) {
			player.send(new SendString("0", 46600));

		}
	}

	public static void sendTeleportButtonNpc2(int npcId) {
		for (Player player : getPlayers()) {
			player.send(new SendString("" + npcId, 47615));
		}
	}

	public static void sendTeleportButton2() {
		for (Player player : getPlayers()) {
			player.send(new SendString("1", 47600));
		}
	}

	public static void hideTeleportButton2() {
		for (Player player : getPlayers()) {
			player.send(new SendString("0", 47600));

		}
	}

	public static void sendTeleportButtonNpc3(int npcId) {
		for (Player player : getPlayers()) {
			player.send(new SendString("" + npcId, 48615));
		}
	}

	public static void sendTeleportButton3() {
		for (Player player : getPlayers()) {
			player.send(new SendString("1", 48600));
		}
	}

	public static void hideTeleportButton3() {
		for (Player player : getPlayers()) {
			player.send(new SendString("0", 48600));
		}
	}

	public static void sendEXPWidgetforwell() {
		for (Player player : getPlayers()) {
			player.send(new SendWidget(SendWidget.WidgetType.DOUBLEXP, 1800));
		}
	}

	public static void sendEXPWidgetforday() {
		for (Player player : getPlayers()) {
			player.send(new SendWidget(SendWidget.WidgetType.DOUBLEXP, 86400));
		}
	}

	public static void sendGalvekInformation() {
		for (Player player : getPlayers()) {
			EventBossHandler.openGalvek(player);
		}
	}


	public static void sendArenaInformation() {
		for (Player player : getPlayers()) {
			EventBossHandler.openArena(player);
		}
	}


	public static void sendSkotizoInformation() {
		for (Player player : getPlayers()) {
			EventBossHandler.openSkotizo(player);
		}
	}
	
	public static void sendPorazdirInformation() {
		for (Player player : getPlayers()) {
			EventBossHandler.openPorazdir(player);
		}
	}

	public static void sendStaffMessage(String... messages) {
		for (Player player : getStaff()) {
			if (player == null)
				continue;
			 player.message(messages);
		}
	}
	
	public static void sendStaffMessageWithoutHelper(String... messages1) {
		for (Player player1 : getStaff2()) {
			 if (player1 == null)
				 continue;
			player1.message(messages1);
		}
	}
	/** Gets the staff players currently online. */
	public static List<Player> getStaff2() {
		List<Player> staff1 = new ArrayList<>();

		for (Player player1 : getPlayers()) {
			if (player1 != null && (PlayerRight.isManagement(player1)))
				staff1.add(player1);
		}

		return staff1;
	}


	/** Sends a game message. */
	public static void sendBroadcast(int time, String message, boolean countdown) {
		get().players.stream().forEach($it -> {
			$it.send(new SendGameMessage(countdown ? 0 : 1, time, Utility.capitalizeSentence(message)));
			$it.send(new SendMessage("[<col=2C7526>RebelionXOS</col>]" + Utility.capitalizeSentence(message)));
		});
	}

	public void dequeLogins() {
		if (logins.isEmpty()) {
			return;
		}

		for (int index = 0; index < Config.LOGIN_THESHOLD; index++) {
			Player player = logins.poll();

			if (player == null) {
				break;
			}

			try {
				player.register();
			} catch (Exception ex) {
				logger.error(String.format("error registering %s", player), ex);
			}

		}
	}

	public void dequeLogouts() {
		if (logouts.isEmpty()) {
			return;
		}

		for (int index = 0; index < Config.LOGOUT_THESHOLD; index++) {
			Player player = logouts.poll();

			if (player == null) {
				break;
			}

			World.schedule(new PlayerRemovalTask(player/*, false*/));
		}
	}

	public int getWildernessResourcePlayers() {
		int count = 0;
		for (Player player : getPlayers()) {
			if (player != null && Area.inWildernessResource(player))
				count++;
		}
		return count;
	}

	/** Sends a kill feed notification. */
	public static void sendKillFeed(String killer, Mob victim) {
		if (victim.isNpc() && (Area.inWilderness(victim) || !NpcAssistant.STRATEGIES.containsKey(victim.id)))
			return;
		for (Player everyone : getPlayers())
			everyone.send(new SendKillFeed(killer, victim.getName()));
	}
	
	public static WorldEventHandler getWorldEventHandler() { return eventHandler; }

	/**
	 * Gets the amount of valid players online.
	 *
	 * @return The amount of players online.
	 */

	/**
	 * Includes the bot count to the player count, with a additional +3
	 * 
	 * @return
	 */

	   public static int getPlayerCount() {
	        return Math.toIntExact(getPlayers().size() - getBotCount());
	    }

	public static int getBotCount() {
		return PlayerBot.BOT_COUNT.get();
	}

	public static int getStaffCount() {
		int count = 0;

		for (Player player : getPlayers()) {
			if (player != null && (PlayerRight.isManagement(player) || player.right == PlayerRight.HELPER))
				count++;
		}

		return count;
	}

	public static int getWildernessCount() {
		int count = 0;

		for (Player player : getPlayers()) {
			if (player != null && player.isBot && Area.inWilderness(player))
				count++;
		}

		return count;
	}

	public static Player getPlayerByName(String name) {
		for (Player p : getPlayers()) {
			 if (name != null && p.getUsername().toLowerCase().equalsIgnoreCase(name.toLowerCase()))
				 return p;
		}
		return null;
	}
	
	/** Gets the staff players currently online. */
	public static List<Player> getStaff() {
		List<Player> staff = new ArrayList<>();
      
		
		for (Player player : getPlayers()) {
			if(!player.isVisible()) {
				return staff;
			}
			
			if (player != null && (PlayerRight.isManagement(player) || player.right == PlayerRight.HELPER))
				staff.add(player);
		}
		return staff;
	}

	public static World get() {
		return WORLD;
	}

	public static void cancelTask(Object attachment) {
		cancelTask(attachment, false);
	}

	public static void cancelTask(Object attachment, boolean logout) {
		get().taskManager.cancel(attachment, logout);
	}

	public static MobList<Player> getPlayers() {
		return get().players;
	}

	public static MobList<Npc> getNpcs() {
		return get().npcs;
	}

	public static RegionManager getRegions() {
		return get().regionManager;
	}

	public TaskManager getTaskManager() {
		return taskManager;
	}

	public Queue<Player> getLogins() {
		return logins;
	}

	public Queue<Player> getLogouts() {
		return logouts;
	}

	public static DataBus getDataBus() {
		return dataBus;
	}

	public static void schedule(Object events) {
		// TODO Auto-generated method stub
		
	}

	public static void sendDropMessage(Player player, Item item, String npc) {
		String playerName = Utility.getColoredName(player);
		String itemName = Utility.getAOrAn(item.getName()) + " " + Utility.highlightText(item.getName());
		String itemAmount = Utility.highlightText(item.getAmount());
		String npcName = Utility.highlightText(npc);
		String header = Utility.formatHeader("[Rare Drop]");
		if (item.getAmount() == 1)
			sendMessage(header + " " + playerName + " has received " + itemName + " from " + npcName);
		else
			sendMessage(header + " " + playerName + " has received " + itemAmount + "x " + Utility.removeAOrAn(itemName) + " from " + npcName);
	}

	public static void sendPetMessage(Player player, Npc pet) {
		String playerName = Utility.getColoredName(player);
		String petName = Utility.getAOrAn(pet.getName()) + " " + Utility.highlightText(pet.getName());
		String header = Utility.formatHeader("[Follower]");
		sendMessage(header + " " + playerName + " has received " + petName + "!");
	}

	public static void sendClueMessage(Player player, Item item, String name) {
		String playerName = Utility.getColoredName(player);
		String itemName = Utility.getAOrAn(item.getName()) + " " + Utility.highlightText(item.getName());
		String itemAmount = Utility.highlightText(item.getAmount());
		String header = Utility.formatHeader("[Clue Scroll]");
		
		sendMessage(header + " " + playerName + " has received " + itemName);
		if (item.getAmount() == 1)
			sendMessage(header + " " + playerName + " has received " + itemName + " from a Clue scroll!");
		else
			sendMessage(header + " " + playerName + " has received " + itemAmount + "x " + Utility.removeAOrAn(itemName) + " from a Clue scroll!");
	}

}
