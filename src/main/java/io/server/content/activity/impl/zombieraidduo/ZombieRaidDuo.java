package io.server.content.activity.impl.zombieraidduo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import io.server.content.ActivityLog;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityListener;
import io.server.content.activity.ActivityType;
import io.server.content.activity.panel.ActivityPanel;
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
 *  Handles the Double Threat Minigame!
 */

public class ZombieRaidDuo extends Activity {

	/** The player in the All Vs One. */
	private  Player player;

	private  Player other;
	
	/** The activity completed flag. */
	private boolean completed;

	/** The time it took to complete the activity. */
	private long time;

	/** The amount of tokkuls the player has acquired. */
	private int rewards;

	/** A set of npcs in this activity. */
	public final Set<Npc> npcs = new HashSet<>();

	/** The current wave of this activity. */
	private ZombieRaidDuoData.WaveData wave = ZombieRaidDuoData.WaveData.WAVE_1;

	/** The combat listener to add for all mobs. */
	private final ZombieRaidDuoCavesListener listener = new ZombieRaidDuoCavesListener(this);

	/**
	 * Constructs a new {@code AllVsOne} object for a {@code player} and an
	 * {@code instance}.
	 */
	public ZombieRaidDuo(Player player, int instance, Player other) {
		super(10, instance);
		this.player = player;
		this.other = other;
	}
	


	public static ZombieRaidDuo create(Player player, Player other) {
		ZombieRaidDuo minigame = new ZombieRaidDuo(player, player.playerAssistant.instance(), other);

		player.move(new Position(2765, 5132, player.getHeight()));
		other.move(player.getPosition());
		
		ActivityPanel.update(player, -1, "Zombie Raid Duo ", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the Zombie Raid Duo, #name.",
				"There are a total of 29 waves",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();
		
		ActivityPanel.update(other, -1, "Zombie Raid Duo", "Activity Completion:", "Good Luck, " + other.getName() + "!");
		other.dialogueFactory.sendNpcChat(5567, "Welcome to the Zombie Raid Duo, #name.",
				"There are a total of 29 waves.",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();
		
		minigame.time = System.currentTimeMillis();
		minigame.add(player);
		minigame.add(other);
		minigame.resetCooldown();
		return minigame;
	}
	
	public static int[] GENERATED_LOOT = { 22090, 22089, 22088, 22091 };

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
			rewards += Utility.random(500, 2700);
			if (npcs.isEmpty()) {
				wave = ZombieRaidDuoData.WaveData.getNext(wave.ordinal());
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

		Position spawn = new Position(2765, 5134, player.getHeight());

		for (int id : wave.getMonster()) {
			Npc npc = new Npc(id, spawn);
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
		pause();
	}

	@Override
	public void finish() {
		cleanup();
		remove(player);
		player.move(new Position(3086, 3501, 0));
		remove(other);
		other.move(new Position(3086, 3501, 0));

		if (completed) {
			player.dialogueFactory.sendNpcChat(5567, "You have defeated Zombie Raid Duo, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			
			other.dialogueFactory.sendNpcChat(5567, "You have defeated Zombie Raid Duo, I am most impressed!",
					"Please accept this gift, young thug.").execute();
			rewards += 20000;

            player.setzombiePoints(player.getzombiePoints() + 1500);
            other.setzombiePoints(player.getzombiePoints() + 1500);

            player.dialogueFactory.sendNpcChat(1756, "You have beaten the zombie raid!",
					"You were rewarded with " + player.zombiePoints + " zombie raid points.",
					"You now have: " + player.zombiePoints + ".").execute();
            
            
    		player.message("<img=8>You now have @red@" + rewards + " Zombie Raid Tickets!");
    		other.message("<img=8>You now have @red@" + rewards + " Zombie Raid Tickets!");

			player.inventory.addOrDrop(new Item(10909, 1));
			other.inventory.addOrDrop(new Item(10909, 1));

			player.send(new SendMessage("You have completed the Zombie Raid Duo activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			
			other.send(new SendMessage("You have completed the Zombie Raid Duo activity. Final time: @red@"
					+ Utility.getTime(time) + "</col>."));
			player.activityLogger.add(ActivityLog.ZOMBIERAID);
			other.activityLogger.add(ActivityLog.ZOMBIERAID);
			player.setAllForOnePartner(null);
			other.setAllForOnePartner(null);
			return;
		}

		if (rewards <= 0)
			rewards = 6;
        player.inventory.addOrDrop(new Item(621, rewards / 2));
		player.message("<img=9>You now have @red@" + rewards + " Zombie Raid Duo Tickets!");
		player.message("bare in mind, your rewards have been halved since you died whilst in the minigame!");
		player.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
		
        other.inventory.addOrDrop(new Item(621, rewards / 2));
        other.message("<img=9>You now have @red@" + rewards + " Zombie Raid Duo Tickets!");
        other.message("bare in mind, your rewards have been halved since you died whilst in the minigame!");
        other.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
		player.setAllForOnePartner(null);
		other.setAllForOnePartner(null);
	}

	@Override
	public void cleanup() {
		ActivityPanel.clear(player);
		ActivityPanel.clear(other);
		if (!npcs.isEmpty())
			npcs.forEach(this::remove);
	}

	@Override
	public void update() {
		if (wave == null) {
			ActivityPanel.update(player, 100, "Zombie Raid Duo", new Item(11642), "Congratulations, you have",
					"completed the Zombie Raid Duo", "activity!");
			
			ActivityPanel.update(other, 100, "Zombie Raid Duo", new Item(11642), "Congratulations, you have",
					"completed the Zombie Raid Duo", "activity!");
			return;
		}
		int progress = (int) Utility.getPercentageAmount(wave.ordinal() + 1, ZombieRaidDuoData.WaveData.values().length);
		if (progress >= 100 && !completed)
			progress = 99;
		ActivityPanel.update(player, progress, "Zombie Raid Duo", new Item(11642),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (ZombieRaidDuoData.WaveData.values().length),
				"</col>Monsters Left: <col=FF5500>" + npcs.size(),
				"</col>Points Gained: <col=FF5500>" + Utility.formatDigits(rewards),
				"</col>Time: <col=FF5500>" + Utility.getTime());
		
		ActivityPanel.update(other, progress, "Zombie Raid Duo", new Item(11642),
				"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (ZombieRaidDuoData.WaveData.values().length),
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
			remove(other);
			player.send(new SendMessage("You have lost your current progress as you have teleported."));
			other.send(new SendMessage("You have lost your current progress as you have teleported."));
			other.setAllForOnePartner(null);
			player.setAllForOnePartner(null);
		}
	}

	@Override
	public void onLogout(Player player) {
		finish();
		remove(player);
		remove(other);
		player.setAllForOnePartner(null);
		other.setAllForOnePartner(null);
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
