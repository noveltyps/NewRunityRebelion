
package io.server.content.activity.impl.soloraids3;

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

public class Raids3 extends Activity {

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
	private Raids3Data.WaveData wave = Raids3Data.WaveData.WAVE_1;

	/** The combat listener to add for all mobs. */
	private final Raids3CavesListener listener = new Raids3CavesListener(this);

	/**
	 * Constructs a new {@code Raids1} object for a {@code player} and an
	 * {@code instance}.
	 */
	private Raids3(Player player, int instance) {
		super(10, instance);
		this.player = player;
	}

	public static Raids3 create(Player player) {
		Raids3 minigame = new Raids3(player, player.playerAssistant.instance());
		player.move(new Position(3168, 4321, player.getHeight()));
		ActivityPanel.update(player, -1, "Solo Raids 3", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the Solo Raids 1, #name.",
				"There are a total of 3 waves, we use Challenge Mode order.",
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
				wave = Raids3Data.WaveData.getNext(wave.ordinal());
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

		Position spawn = new Position(3168, 4310, player.getHeight());
		Position[] boundaries = Utility.getInnerBoundaries(spawn, Utility.random(1, 8), Utility.random(1, 8));

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
			player.dialogueFactory.sendNpcChat(5567, "You have defeated Solo Raids 3, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			rewards += 10000;
			//player.setRaids1Points(player.getRaids1Points() + rewards);
			
			
            player.inventory.addOrDrop(new Item(621, rewards));
    		player.message("<img=9>You now have @red@" + rewards + " Solo Raids Tickets!");
			if(Utility.random(1, 3) == 3) {
			player.inventory.addOrDrop(new Item(22126));
			}
			player.inventory.addOrDrop(new Item(22126));
			Pets.onReward(player, PetData.PIRATE_PETE);
			player.send(new SendMessage("You have completed the Solo Raids 3 activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			player.activityLogger.add(ActivityLog.SOLORAIDS1);
			return;
		}

		if (rewards <= 0)
			rewards = 1;
        player.inventory.addOrDrop(new Item(621, rewards));
		player.message("<img=9>You now have @red@" + rewards + " Solo Raids Tickets!");
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
			ActivityPanel.update(player, 100, "Solo Raids 3", new Item(22325), "Congratulations, you have",
					"completed the Solo Raids 1", "activity!");
			return;
		}
		int progress = (int) Utility.getPercentageAmount(wave.ordinal() + 1, Raids3Data.WaveData.values().length);
		if (progress >= 100 && !completed)
			progress = 99;
		ActivityPanel.update(player, progress, "Solo Raids 3", new Item(22325),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (Raids3Data.WaveData.values().length),
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
		if (!Area.inSoloRaids2(player)) {
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
		return ActivityType.SOLORAIDS1;
	}

	@Override
	public Optional<? extends ActivityListener<? extends Activity>> getListener() {
		return Optional.of(listener);
	}
}
