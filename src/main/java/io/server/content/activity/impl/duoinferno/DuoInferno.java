package io.server.content.activity.impl.duoinferno;

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
 *  Handles inferno wave.
 */
public class DuoInferno extends Activity {

	/** The player in the Inferno */
	private  Player player;
	private  Player other;

	/** The activity completed flag. */
	private boolean completed;

	/** The time it took to complete the activity. */
	private long time;

	/** The amount of rewards the player has acquired. */
	private int rewards;

	/** A set of npcs in this activity. */
	public final Set<Npc> npcs = new HashSet<>();

	/** The current wave of this activity. */
	private DuoInfernoWaveData.WaveData wave = DuoInfernoWaveData.WaveData.DUOWAVE_1;

	/** The combat listener to add for all mobs. */
	private final DuoInfernoCavesListener listener = new DuoInfernoCavesListener(this);

	/**
	 * Constructs a new {@code Inferno} object for a {@code player} and an
	 * {@code instance}.
	 */
	public DuoInferno(Player player, int instance, Player other) {
		super(10, instance);
		this.player = player;
		this.other = other;
	}

	public static DuoInferno create(Player player, Player other) {
		DuoInferno minigame = new DuoInferno(player, player.playerAssistant.instance(), other);
		player.move(new Position(2273, 5341, player.getHeight()));
		other.move(player.getPosition());
		ActivityPanel.update(player, -1, "Duo Inferno", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the Duo Inferno, #name.",
				"There are a total of 69 waves, TzKal-Zuk being the last.",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();
		ActivityPanel.update(other, -1, "Duo Inferno", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the Duo Inferno, #name.",
				"There are a total of 69 waves, TzKal-Zuk being the last.",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();
		
		minigame.time = System.currentTimeMillis();
		minigame.add(player);
		minigame.add(other);
		minigame.resetCooldown();
		return minigame;
	}

	/** Handles what happens to a mob when they die in the activity. */
	void handleDeath(Mob dead) {
		if (dead.isPlayer() && (dead.equals(player) || dead.equals(other) && other.isPlayer())) {
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
					roc.getCombat().attack(other);
					
				}
				return;
			}

			npcs.remove(dead);
			remove(dead);
			rewards += Utility.random(500, 1250);
			if (npcs.isEmpty()) {
				wave = DuoInfernoWaveData.WaveData.getNext(wave.ordinal());
				if (wave == null) {
					completed = true;
					player.send(new SendMessage("You have finished the activity!"));
					other.send(new SendMessage("You have finished the activity!"));
				} else {
					player.send(new SendMessage("The next wave will commence soon."));
					other.send(new SendMessage("The next wave will commence soon."));
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
		if (other.locking.locked()) {
			return;
		}

		Position spawn = new Position(2273, 5337, player.getHeight());
		Position[] boundaries = Utility.getInnerBoundaries(spawn, Utility.random(1, 8), Utility.random(1, 8));

		for (int id : wave.getMonster()) {
			Npc npc = new Npc(id, RandomUtils.random(boundaries));
			//npc.owner = player;
			add(npc);
			npcs.add(npc);
			npc.getCombat().attack(player);
			npc.face(player);
			player.face(npc.getPosition());
			
			npc.getCombat().attack(other);
			npc.face(other);
			other.face(npc.getPosition());
			npc.locking.unlock();
			//pause();

		}
		if(wave == DuoInfernoWaveData.DUOWAVE_2 || wave == DuoInfernoWaveData.DUOWAVE_1) {
			player.message("WAVE 69!! HURAHH");
			other.message("WAVE 69!! HURAHH");
		}
		pause();
	}
	
	public static void finalWave() {
		//final int BOSS_ID; 
		//WaveData wavee = WaveData.WAVE_69;
	}

	@Override
	public void finish() {
		cleanup();
		remove(player);
		player.move(new Position(3086, 3501, 0));
		remove(other);
		other.move(new Position(3086, 3501, 0));

		if (completed) {
			player.dialogueFactory.sendNpcChat(5567, "You have defeated Inferno, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			other.dialogueFactory.sendNpcChat(5567, "You have defeated Inferno, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			rewards += 10000;
            player.inventory.addOrDrop(new Item(7775, rewards));
            player.inventory.addOrDrop(new Item(21295, rewards));
            other.inventory.addOrDrop(new Item(7775, rewards));
            other.inventory.addOrDrop(new Item(21295, rewards));
    		player.message("<img=9>You now have @red@" + rewards + " Inferno Tickets!");
    		other.message("<img=9>You now have @red@" + rewards + " Inferno Tickets!");
			if(Utility.random(1, 3) == 3) {
			player.inventory.addOrDrop(new Item(20211));
			other.inventory.addOrDrop(new Item(20211));
			}
			player.inventory.addOrDrop(new Item(290));
			other.inventory.addOrDrop(new Item(290));
			Pets.onReward(player, PetData.PIRATE_PETE);
			Pets.onReward(other, PetData.PIRATE_PETE);
			player.send(new SendMessage("You have completed the Inferno activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			other.send(new SendMessage("You have completed the Inferno activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			player.activityLogger.add(ActivityLog.INFERNO);
			other.activityLogger.add(ActivityLog.INFERNO);
			return;
		}

		if (rewards <= 0)
			rewards = 1;
        player.inventory.addOrDrop(new Item(7775, rewards));
		player.message("<img=9>You now have @red@" + rewards + " Inferno Tickets!");
		player.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
		other.inventory.addOrDrop(new Item(7775, rewards));
		other.message("<img=9>You now have @red@" + rewards + " Inferno Tickets!");
		other.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
	}

	@Override
	public void cleanup() {
		ActivityPanel.clear(player);
		if (!npcs.isEmpty())
			npcs.forEach(this::remove);
		ActivityPanel.clear(other);
		if (!npcs.isEmpty())
			npcs.forEach(this::remove);
	}

	@Override
	public void update() {
		if (wave == null) {
			ActivityPanel.update(player, 100, "Inferno", new Item(22325), "Congratulations, you have",
					"completed the Inferno", "activity!");
			ActivityPanel.update(other, 100, "Inferno", new Item(22325), "Congratulations, you have",
							"completed the Inferno", "activity!");
			return;
	
		}
		int progress = (int) Utility.getPercentageAmount(wave.ordinal() + 1, DuoInfernoWaveData.WaveData.values().length);
		if (progress >= 100 && !completed)
			progress = 99;
		ActivityPanel.update(player, progress, "Duo Inferno", new Item(22325),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (DuoInfernoWaveData.WaveData.values().length),
				"</col>Monsters Left: <col=FF5500>" + npcs.size(),
				"</col>Points Gained: <col=FF5500>" + Utility.formatDigits(rewards),
				"</col>Time: <col=FF5500>" + Utility.getTime());
		ActivityPanel.update(other, progress, "Duo Inferno", new Item(22325),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (DuoInfernoWaveData.WaveData.values().length),
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
		if (!Area.inInferno(player)) {
			cleanup();
			remove(player);
			remove(other);
			player.send(new SendMessage("You have lost your current progress as you have teleported."));
			other.send(new SendMessage("You have lost your current progress as you have teleported."));
		}
	}

	@Override
	public void onLogout(Player player) {
		finish();
		remove(player);
		remove(other);
	}

	@Override
	public ActivityType getType() {
		return ActivityType.INFERNO;
	}

	@Override
	public Optional<? extends ActivityListener<? extends Activity>> getListener() {
		return Optional.of(listener);
	}
}
