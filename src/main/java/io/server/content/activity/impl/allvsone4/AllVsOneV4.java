package io.server.content.activity.impl.allvsone4;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.server.Config;
import io.server.content.ActivityLog;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityListener;
import io.server.content.activity.ActivityType;
import io.server.content.activity.panel.ActivityPanel;
import io.server.content.worldevent.WorldEventType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/** @author Adam_#6723 
 *  Handles the All Vs One Minigame.
 */

public class AllVsOneV4 extends Activity {

	/** The player in the All Vs One. */
	private final Player player;

	/** The activity completed flag. */
	private boolean completed;

	/** The time it took to complete the activity. */
	private long time;

	/** The amount of tokkuls the player has acquired. */
	private int rewards;

	/** A set of npcs in this activity. */
	public final Set<Npc> npcs = new HashSet<>();

	/** The current wave of this activity. */
	private AllVsOneData4.WaveData wave = AllVsOneData4.WaveData.WAVE_1;

	/** The combat listener to add for all mobs. */
	private final AllVsOneCavesListener4 listener = new AllVsOneCavesListener4(this);

	/**
	 * Constructs a new {@code AllVsOne} object for a {@code player} and an
	 * {@code instance}.
	 */
	private AllVsOneV4(Player player, int instance) {
		super(10, instance);
		this.player = player;
	}

	public static AllVsOneV4 create(Player player) {
		AllVsOneV4 minigame = new AllVsOneV4(player, player.playerAssistant.instance());
		player.move(new Position(3027, 5227, player.getHeight()));
		ActivityPanel.update(player, -1, "All vs One V4", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the All Vs One V4, #name.",
				"There are a total of 20 waves.",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();
		minigame.time = System.currentTimeMillis();
		minigame.add(player);
		minigame.resetCooldown();
		return minigame;
	}
	
	public static int[] GENERATED_LOOT = { 22090, 22089, 22088, 22091 };

	public static void createRandomGroundItems(Player player) {
		if (Utility.random(1, 2) <= 1) {
			GroundItem.createGlobal(player, new Item(GENERATED_LOOT[Utility.random(GENERATED_LOOT.length)], 1),
					new Position(2558 + Utility.random(1, 15), 4960 + Utility.random(1, 15), player.getPosition().getHeight()));
			player.message("@red@ A Dragon piece key has been dropped in a random location!");
			player.message("@red@ Find all four pieces to make the key to complete this minigame!");
		}
	}

	/** Handles what happens to a mob when they die in the activity. */
	void handleDeath(Mob dead) {
		if (dead.isPlayer() && dead.equals(player)) {
			finish();
			return;
		}
		if (dead.isNpc() && npcs.contains(dead)) {
			
			if (dead.id == 3162) {
				remove(dead);
				npcs.remove(dead);
				for (int index = 0; index < 2; index++) {
					Position position = new Position(dead.getX() + (index == 0 ? -1 : +1), dead.getY(), dead.getHeight());
					Npc roc = new Npc(763, position);
					add(roc);
					npcs.add(roc);
					roc.getCombat().attack(player);
				}
				return;
			}

			npcs.remove(dead);
			remove(dead);
			rewards += Utility.random(3250, 5500);
			if (npcs.isEmpty()) {
				wave = AllVsOneData4.WaveData.getNext(wave.ordinal());
				if (wave == null) {
					completed = true;
					player.send(new SendMessage("You have finished the activity!"));
				} else {
					player.send(new SendMessage("The next wave will commence soon."));
				}
				resetCooldown();
				return;
			}
		}
	}

	@Override
	protected void start() {
		if (wave == null) {
			finish();
			return;
		}
		if (player.locking.locked()) {
			return;
		}

		Position spawn = new Position(3024, 5236, player.getHeight());
		Position[] boundaries = Utility.getInnerBoundaries(spawn, Utility.random(4, 6), Utility.random(4, 6));

		for (int id : wave.getMonster()) {
			Npc npc = new Npc(id, RandomUtils.random(boundaries));
			npc.owner = player;
			add(npc);
			npcs.add(npc);
			npc.getCombat().attack(player);
			npc.face(player);
			player.face(npc.getPosition());
			npc.locking.unlock(); 
			//pause();
		}
		pause();
	}

	@Override
	public void finish() {
		cleanup();
		remove(player);
		player.move(new Position(3086, 3501, 0));

		if (completed) {
			player.dialogueFactory.sendNpcChat(5567, "You have defeated All Vs One V4, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			rewards += 20000;
			
			if(World.getWorldEventHandler().getEvent(WorldEventType.AVO) != null) {
	            player.inventory.addOrDrop(new Item(7775, rewards));
	            player.message("<img=8>@red@You've recieved double Tickets because of the daily server events!");
			}
			
			if(Utility.random(1, 14) <= 1) {
				player.inventory.addOrDrop(new Item(6833, 2));
			}
            player.inventory.addOrDrop(new Item(7775, rewards / 2));
    		player.message("<img=8>You now have @red@" + rewards + " All Vs One V4 Tickets!");
			player.inventory.addOrDrop(new Item(6833, 2));
			player.inventory.addOrDrop(new Item(290, 2));
			player.send(new SendMessage("You have completed the All Vs One V4 activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			player.activityLogger.add(ActivityLog.ALLVSONE3);
			return;
		}

		if (rewards <= 0)
			rewards = 6;
        player.inventory.addOrDrop(new Item(7775, rewards / 2));
		player.message("<img=9>You now have @red@" + rewards + " All Vs One Tickets!");
		player.message("bare in mind, your rewards have been halved since you died whilst in the minigame!");
		player.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
	}

	@Override
	public void cleanup() {
		ActivityPanel.clear(player);
		if (!npcs.isEmpty())
			npcs.forEach(this::remove);
	}

	@Override
	public void update() {
		if (wave == null) {
			ActivityPanel.update(player, 100, "All Vs One V4", new Item(11642), "Congratulations, you have",
					"completed the All Vs One V4", "activity!");
			return;
		}
		int progress = (int) Utility.getPercentageAmount(wave.ordinal() + 1, AllVsOneData4.WaveData.values().length);
		if (progress >= 100 && !completed)
			progress = 99;
		ActivityPanel.update(player, progress, "All Vs One V4", new Item(11642),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (AllVsOneData4.WaveData.values().length),
				"</col>Monsters Left: <col=FF5500>" + npcs.size(),
				"</col>Points Gained: <col=FF5500>" + Utility.formatDigits(rewards),
				"</col>Time: <col=FF5500>" + Utility.getTime());
	}

	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inAllVsOne4(player)) {
			cleanup();
			remove(player);
			player.send(new SendMessage("You have lost your current progress as you have teleported."));
		}
	}

	@Override
	public void onLogout(Player player) {
		finish();
		remove(player);
	}

	@Override
	public ActivityType getType() {
		return ActivityType.ALLVSONE4;
	}

	@Override
	public Optional<? extends ActivityListener<? extends Activity>> getListener() {
		return Optional.of(listener);
	}
}
