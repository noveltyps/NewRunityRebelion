
package io.server.content.activity.impl.zombieraid;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.server.content.ActivityLog;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityListener;
import io.server.content.activity.ActivityType;
import io.server.content.activity.panel.ActivityPanel;
import io.server.content.pet.PetData;
import io.server.content.pet.Pets;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/** @author Adam_#6723 
 *  Handles the All Vs One Minigame.
 */

public class ZombieRaid extends Activity {

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
	private ZombieRaidData.WaveData wave = ZombieRaidData.WaveData.WAVE_1;

	/** The combat listener to add for all mobs. */
	private final ZombieRaidCavesListener listener = new ZombieRaidCavesListener(this);

	/**
	 * Constructs a new {@code ZombieRaid} object for a {@code player} and an
	 * {@code instance}.
	 */
	private ZombieRaid(Player player, int instance) {
		super(10, instance);
		this.player = player;
	}

	public static ZombieRaid create(Player player) {
		ZombieRaid minigame = new ZombieRaid(player, player.playerAssistant.instance());
		player.move(new Position(2765, 5132, player.getHeight()));
		ActivityPanel.update(player, -1, "Zombie Raid", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the Zombie Raid, #name.",
				"There are a total of 29 waves, we use Challenge Mode order.",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();
		minigame.time = System.currentTimeMillis();
		minigame.add(player);
		minigame.resetCooldown();
		return minigame;
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
					Position position = new Position(dead.getX() + (index == 0 ? -1 : +1), dead.getY(),
							dead.getHeight());
					Npc roc = new Npc(763, position);
					add(roc);
					npcs.add(roc);
					roc.getCombat().attack(player);
				}
				return;
			}

			npcs.remove(dead);
			remove(dead);
			rewards += Utility.random(500, 3500);
			if (npcs.isEmpty()) {
				wave = ZombieRaidData.WaveData.getNext(wave.ordinal());
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

		Position spawn = new Position(2765, 5134, player.getHeight());

		for (int id : wave.getMonster()) {
			Npc npc = new Npc(id, spawn);
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
			player.dialogueFactory.sendNpcChat(5567, "You have defeated Zombie Raid, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			rewards += 10000;
			//player.add(player.getRaidPoints() + rewards);
			
			
    		player.message("<img=9>You now have @red@" + rewards + " Zombie Raid Tickets!");
			if(Utility.random(1, 3) == 3) {
			player.inventory.addOrDrop(new Item(20211));
			}
			player.inventory.addOrDrop(new Item(10909));
			Pets.onReward(player, PetData.PIRATE_PETE);
			player.send(new SendMessage("You have completed the Zombie Raid activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			player.activityLogger.add(ActivityLog.ZOMBIERAID);
			return;
		}

		if (rewards <= 0)
			rewards = 1;
		player.setzombiePoints(player.getzombiePoints() + 1500);
		player.message("<img=9>You now have @red@" + rewards + " Zombie Raid Tickets!");
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
			ActivityPanel.update(player, 100, "Zombie Raid", new Item(22325), "Congratulations, you have",
					"completed the Zombie Raid", "activity!");
			return;
		}
		int progress = (int) Utility.getPercentageAmount(wave.ordinal() + 1, ZombieRaidData.WaveData.values().length);
		if (progress >= 100 && !completed)
			progress = 99;
		ActivityPanel.update(player, progress, "Zombie Raid", new Item(22325),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (ZombieRaidData.WaveData.values().length),
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
		if (!Area.inZombieRaid(player)) {
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
		return ActivityType.ZOMBIERAID;
	}

	@Override
	public Optional<? extends ActivityListener<? extends Activity>> getListener() {
		return Optional.of(listener);
	}
}
