package io.server.content.activity.impl.raids4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

public class Raids4 extends Activity {

	/** The player in the All Vs One. */
	private  Player player;

	private  Player other;
	
	private List<Player> partners = new ArrayList<>();
	
	/** The activity completed flag. */
	private boolean completed;

	/** The time it took to complete the activity. */
	private long time;

	/** The amount of tokkuls the player has acquired. */
	private int rewards;

	/** A set of npcs in this activity. */
	public final Set<Npc> npcs = new HashSet<>();

	/** The current wave of this activity. */
	private Raids4Data.WaveData wave = Raids4Data.WaveData.WAVE_1;

	/** The combat listener to add for all mobs. */
	private final Raids4CavesListener listener = new Raids4CavesListener(this);

	/**
	 * Constructs a new {@code AllVsOne} object for a {@code player} and an
	 * {@code instance}.
	 */
	public Raids4(Player player, int instance, Player other) {
		super(10, instance);
		this.player = player;
		this.other = other;
	}
	
	public Raids4(Player player, int instance, List<Player> partners) {
		super(10, instance);
		this.player = player;
		this.partners = partners;
	}
	
	public static Raids4 create(Player player, List<Player> partners) {

		Raids4 minigame = new Raids4(player, player.playerAssistant.instance(), partners);

		player.move(new Position(2761, 9336, player.getHeight()));
		
		ActivityPanel.update(player, -1, "Raids 4", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the Raids 4, #name.",
				"There are a total of 6 waves",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();	
		
		minigame.add(player);
		
		for (Player p : partners)
			p.move(player.getPosition());	
		
		for (Player p : partners) {
			
			ActivityPanel.update(p, -1, "Raids 4", "Activity Completion:", "Good Luck, " + p.getName() + "!");
			
			p.dialogueFactory.sendNpcChat(5567, "Welcome to the Raids 4, #name.",
					"There are a total of 6 waves",
					"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();	
			
			minigame.add(p);
			
		}
		
		minigame.time = System.currentTimeMillis();
		minigame.resetCooldown();
		
		return  minigame;
		
	}

	public static Raids4 create(Player player, Player other) {
		Raids4 minigame = new Raids4(player, player.playerAssistant.instance(), other);

		player.move(new Position(3168, 4321, player.getHeight()));
		other.move(player.getPosition());
		
		ActivityPanel.update(player, -1, "Raids 4", "Activity Completion:", "Good Luck, " + player.getName() + "!");
		player.dialogueFactory.sendNpcChat(5567, "Welcome to the Raids 4, #name.",
				"There are a total of 6 waves",
				"Use your activity panel (bottom left tab) for wave information.", "Good luck!").execute();
		
		ActivityPanel.update(other, -1, "Raids 4", "Activity Completion:", "Good Luck, " + other.getName() + "!");
		other.dialogueFactory.sendNpcChat(5567, "Welcome to the Raids 4, #name.",
				"There are a total of 6 waves",
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
		if (partners.contains(dead)) {
			dead.getPlayer().inventory.addOrDrop(new Item(621, rewards / 2));
			dead.getPlayer().message("<img=9>You now have @red@" + rewards + " Raids Tickets!");
			dead.getPlayer().message("bare in mind, your rewards have been halved since you died whilst in the minigame!");
			dead.getPlayer().dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
			dead.getPlayer().partyLeader = null;
			partners.remove(dead.getPlayer());
		}
		if (dead.isPlayer() && dead.equals(player)) {
			player.inventory.addOrDrop(new Item(621, rewards / 2));
			player.message("<img=9>You now have @red@" + rewards + " Raids Tickets!");
			player.message("bare in mind, your rewards have been halved since you died whilst in the minigame!");
			player.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
			player.partyLeader = null;
			player = null;
		}
		if (player == null && partners.size() <= 0) {
			finish();
			return;
		}
//		if (dead.isPlayer() && (dead.equals(player) || dead.equals(other) && other.isPlayer())) {
//			finish();
//			return;
//		}
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
					if (player != null)
						roc.getCombat().attack(player);
					for (Player p : partners)
						roc.getCombat().attack(p);
//					roc.getCombat().attack(other);
				}
				return;
			}

			npcs.remove(dead);
			remove(dead);
			rewards += Utility.random(500, 2700);
			if (npcs.isEmpty()) {
				wave = Raids4Data.WaveData.getNext(wave.ordinal());
				if (wave == null) {
					completed = true;
					if (player != null)
						player.send(new SendMessage("You have finished the activity!"));
//					other.send(new SendMessage("You have finished the activity!"));
					for (Player p : partners)
						p.send(new SendMessage("You have finished the activity!"));
				} else {
					if (player != null)
						player.send(new SendMessage("The next wave will commence soon."));
//					other.send(new SendMessage("The next wave will commence soon."));
					for (Player p : partners)
						p.send(new SendMessage("The next wave will commence soon."));
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
		
		for (Player p : partners)
			if (p.locking.locked())
				return;
		
//		if (other.locking.locked()) {
//			return;
//		}

		/**
		 * Accesses the position for each attribute within the enum
		 */
		
		Position spawn =  new Position(3168, 4310, player.getHeight());
		Position[] boundaries = Utility.getInnerBoundaries(spawn, Utility.random(1, 2), Utility.random(1, 2));

		for (int id : wave.getMonster()) {
			Npc npc = new Npc(id, RandomUtils.random(boundaries));
			//npc.owner = player;
			add(npc);
			npcs.add(npc);
			
			if (player != null) {
				npc.getCombat().attack(player);
				npc.face(player);
				player.face(npc.getPosition());
				
			}
			
			for (Player p : partners) {
				npc.getCombat().attack(p);
				npc.face(p);
				p.face(npc.getPosition());
			}
			
			
			npc.locking.unlock();
			//pause();
		}
		pause();
	}

	@Override
	public void finish() {
		
		if (player != null || partners.size() > 0)
			return;
		
		cleanup();
		
		if (player != null) {
			remove(player);
			player.partyLeader = null;
			player.move(new Position(3086, 3501, 0));
		}
		
		for (Player p : partners) {
			remove(p);
			p.partyLeader = null;
			p.move(new Position(3086, 3501, 0));
		}

		if (completed) {
			
			if (player != null)
				player.dialogueFactory.sendNpcChat(5567, "You have defeated Raids 4, I am most impressed!",
						"Please accept this gift, young thug.").execute();
			
			for (Player p : partners)
				p.dialogueFactory.sendNpcChat(5567, "You have defeated Raids 4, I am most impressed!",
						"Please accept this gift, young thug.").execute();
				
//			other.dialogueFactory.sendNpcChat(5567, "You have defeated Raids 4, I am most impressed!",
//					"Please accept this gift, young thug.").execute();
			
			rewards += 20000;
			
			if (player != null) {
				player.inventory.addOrDrop(new Item(621, rewards));
	    		player.message("<img=8>You now have @red@" + rewards + " Raids 4 Tickets!");
				player.inventory.addOrDrop(new Item(22127, 1));
				player.send(new SendMessage("You have completed the Raids 4 activity. Final time: @red@"
						+ Utility.getTime(time) + "</col>."));
			}
			
			for (Player p : partners) {
				p.inventory.addOrDrop(new Item(621, rewards));
				p.message("<img=8>You now have @red@" + rewards + " Raids 4 Tickets!");
				p.inventory.addOrDrop(new Item(22127, 1));
				p.send(new SendMessage("You have completed the Raids 4 activity. Final time: @red@"
						+ Utility.getTime(time) + "</col>."));
			}
				
//            other.inventory.addOrDrop(new Item(621, rewards));

//    		other.message("<img=8>You now have @red@" + rewards + " Raids 4 Tickets!");

//			other.inventory.addOrDrop(new Item(10993, 1));
			
//			other.send(new SendMessage("You have completed the Raids 4 activity. Final time: @red@"
//					+ Utility.getTime(time) + "</col>."));
			
			player.activityLogger.add(ActivityLog.RAIDS4);
			player.getRaidPartners().clear();
			
			
			for (Player p : partners) {
				p.activityLogger.add(ActivityLog.RAIDS4);
				p.getRaidPartners().clear();
			}
			
//			other.activityLogger.add(ActivityLog.Raids4);
//			player.setAllForOnePartner(null);
//			other.setAllForOnePartner(null);
			
			return;
		}

		if (rewards <= 0)
			rewards = 6;
		
//		if (player != null) {
//	        player.inventory.addOrDrop(new Item(621, rewards / 2));
//			player.message("<img=9>You now have @red@" + rewards + " Raids Tickets!");
//			player.message("bare in mind, your rewards have been halved since you died whilst in the minigame!");
//			player.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
//		}
		
		if (player != null)
			player.getRaidPartners().clear();
		
		for (Player p : partners)
			p.getRaidPartners().clear();
		
//        other.inventory.addOrDrop(new Item(621, rewards / 2));
//        other.message("<img=9>You now have @red@" + rewards + " Raids Tickets!");
//        other.message("bare in mind, your rewards have been halved since you died whilst in the minigame!");
//        other.dialogueFactory.sendNpcChat(5567, "Better luck next time!", "Take these points as a reward.").execute();
//		player.setAllForOnePartner(null);
//		other.setAllForOnePartner(null);
		
	}

	@Override
	public void cleanup() {
		
		if (player != null || partners.size() > 0)
			return;
		
		if (player != null)
			ActivityPanel.clear(player);
		for (Player p : partners)
			ActivityPanel.clear(p);
		if (!npcs.isEmpty())
			npcs.forEach(this::remove);
	}

	@Override
	public void update() {
		if (wave == null) {
			if (player != null)
				ActivityPanel.update(player, 100, "Raids 4", new Item(11642), "Congratulations, you have",
						"completed the Raids 4", "activity!");
			for (Player p : partners)
				ActivityPanel.update(p, 100, "Raids 4", new Item(11642), "Congratulations, you have",
						"completed the Raids 4", "activity!");
			return;
		}
		int progress = (int) Utility.getPercentageAmount(wave.ordinal() + 1, Raids4Data.WaveData.values().length);
		if (progress >= 100 && !completed)
			progress = 99;
		if (player != null)
			ActivityPanel.update(player, progress, "Raids 4", new Item(11642),
					"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (Raids4Data.WaveData.values().length),
					"</col>Monsters Left: <col=FF5500>" + npcs.size(),
					"</col>Points Gained: <col=FF5500>" + Utility.formatDigits(rewards),
					"</col>Time: <col=FF5500>" + Utility.getTime());
		for (Player p : partners)
			ActivityPanel.update(p, progress, "Raids 4", new Item(11642),
					"</col>Wave: <col=FF5500>" + (wave.ordinal() + 1) + "/" + (Raids4Data.WaveData.values().length),
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
		if (!Area.inRaids4(player)) {
			
			if (player.equals(this.player))
				this.player = null;
			
			if (partners.contains(player))
				partners.remove(player);
			
			if (this.player == null && partners.size() <= 0)
				cleanup();
			
			remove(player);
			player.partyLeader = null;
			if (this.player != null  && this.player.partyLeader == player)  {
				for (Player p : partners)
					p.partyLeader = null;
			}
			player.send(new SendMessage("You have lost your current progress as you have teleported."));
			player.getRaidPartners().clear();
			
//			remove(other);
//			other.send(new SendMessage("You have lost your current progress as you have teleported."));
//			other.setAllForOnePartner(null);
//			player.setAllForOnePartner(null);
		}
	}

	@Override
	public void onLogout(Player player) {
		if (this.player == null && partners.size() <= 0)
			finish();
		remove(player);
//		remove(other);
		player.partyLeader = null;
		if (this.player != null  && this.player.partyLeader == player)  {
			for (Player p : partners)
				p.partyLeader = null;
		}
		player.getRaidPartners().clear();
//		player.setAllForOnePartner(null);
//		other.setAllForOnePartner(null);
	}

	@Override
	public ActivityType getType() {
		return ActivityType.RAIDS4;
	}

	@Override
	public Optional<? extends ActivityListener<? extends Activity>> getListener() {
		return Optional.of(listener);
	}
}
