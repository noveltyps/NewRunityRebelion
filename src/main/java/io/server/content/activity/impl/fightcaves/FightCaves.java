package io.server.content.activity.impl.fightcaves;

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

/** @author Daniel */
public class FightCaves extends Activity {

	/** The player in the cave. */
	private final Player player;

	/** The activity completed flag. */
	private boolean completed;

	/** The time it took to complete the activity. */
	private long time;

	/** The amount of tokkuls the player has acquired. */
	private int tokkuls;

	/** A set of npcs in this activity. */
	public final Set<Npc> npcs = new HashSet<>();

	/** The current wave of this activity. */
	private TzhaarData.WaveData wave = TzhaarData.WaveData.WAVE_1;

	/** The combat listener to add for all mobs. */
	private final FightCavesListener listener = new FightCavesListener(this);

	/**
	 * Constructs a new {@code FightCaves} object for a {@code player} and an
	 * {@code instance}.
	 */
	private FightCaves(Player player, int instance) {
		super(10, instance);
		this.player = player;
	}

	public static FightCaves create(Player player) {
		FightCaves minigame = new FightCaves(player, player.playerAssistant.instance());
		player.move(new Position(2398, 5083, player.getHeight()));
		ActivityPanel.update(player, -1, "Fight Caves", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(2180, "Welcome to the Fight Caves, #name.",
				"There are a total of 15 waves, Tz-Tok Jad being the last.",
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
			if (dead.id == 2191) {
				remove(dead);
				npcs.remove(dead);
				for (int index = 0; index < 2; index++) {
					Position position = new Position(dead.getX() + (index == 0 ? -1 : +1), dead.getY(),
							dead.getHeight());
					Npc TzKet = new Npc(2192, position);
					add(TzKet);
					npcs.add(TzKet);
					TzKet.getCombat().attack(player);
				}
				return;
			}

			npcs.remove(dead);
			remove(dead);
			tokkuls += Utility.random(500, 1250);
			if (npcs.isEmpty()) {
				wave = TzhaarData.WaveData.getNext(wave.ordinal());
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

		Position spawn = new Position(2398, 5083, player.getHeight());
		Position[] boundaries = Utility.getInnerBoundaries(spawn, 8, 8);

		for (int id : wave.getMonster()) {
			Npc npc = new Npc(id, RandomUtils.random(boundaries));
			npc.owner = player;
			add(npc);
			npcs.add(npc);
			npc.getCombat().attack(player);
		}
		pause();
	}

	@Override
	public void finish() {
		cleanup();
		remove(player);
		player.move(new Position(2438, 5169, 0));

		if (completed) {
			player.dialogueFactory.sendNpcChat(2180, "You have defeated Tztok-Jad, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			tokkuls += 10000;
			player.inventory.add(6529, tokkuls);
			player.inventory.addOrDrop(new Item(6570));
			Pets.onReward(player, PetData.JAD);
			player.send(new SendMessage("You have completed the Fight Caves activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			player.activityLogger.add(ActivityLog.FIGHT_CAVES);
			return;
		}

		if (tokkuls <= 0)
			tokkuls = 1;

		player.inventory.addOrDrop(new Item(6529, tokkuls));
		player.dialogueFactory.sendNpcChat(2180, "Better luck next time!", "Take these tokkuls as a reward.").execute();
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
			ActivityPanel.update(player, 100, "Fight Caves", new Item(6570), "Congratulations, you have",
					"completed the Fight Caves", "activity!");
			return;
		}
		int progress = (int) Utility.getPercentageAmount(wave.ordinal() + 1, TzhaarData.WaveData.values().length);
		if (progress >= 100 && !completed)
			progress = 99;
		ActivityPanel.update(player, progress, "Fight Caves", new Item(6570),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (TzhaarData.WaveData.values().length),
				"</col>Monsters Left: <col=FF5500>" + npcs.size(),
				"</col>Tokkuls Gained: <col=FF5500>" + Utility.formatDigits(tokkuls),
				"</col>Time: <col=FF5500>" + Utility.getTime());
	}

	@Override
	public boolean canTeleport(Player player) {
		return false;
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inFightCaves(player)) {
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
		return ActivityType.FIGHT_CAVES;
	}

	@Override
	public Optional<? extends ActivityListener<? extends Activity>> getListener() {
		return Optional.of(listener);
	}
}
