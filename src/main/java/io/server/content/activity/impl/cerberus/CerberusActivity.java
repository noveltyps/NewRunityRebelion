package io.server.content.activity.impl.cerberus;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import io.server.Config;
import io.server.content.ActivityLog;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.World;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.NpcDeath;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;

/**
 * 
 * @author Adam_#6723
 *
 */

public class CerberusActivity extends Activity {

	private final Player player;
	public Npc cerberus = null;
	//private boolean completed;
	public Set<Npc> ghosts = new HashSet<>();
	private final CerberusActivityListener listener = new CerberusActivityListener(this);

	private static final int CERBERUS = 5862;
	private static final Position CERBERUS_POS = new Position(1238, 1250);

	private CerberusActivity(Player player, int instance) {
		super(1, instance);
		this.player = player;
	}

	public static CerberusActivity create(Player player) {
		CerberusActivity minigame = new CerberusActivity(player, player.playerAssistant.instance());
		minigame.add(player);
		// player.gameRecord.start();
		return minigame;
	}

	public static void CreatePaidInstance(Player player) {
		if (!player.bank.contains(995, 1000000)) {
			player.message("You need to have 1,000,000 coins inside your bank to pay for the instance!");
			return;
		} else {
			player.bank.remove(995, 1000000);
			Teleportation.teleport(player, new Position(1240, 1226, 0), 20, () -> CerberusActivity.create(player));
			player.send(new SendMessage("You have teleported to the Instanced Version of Cerberus"));
			player.send(new SendMessage("1,000,000 coins has been taken out of your bank, as a fee."));

		}
	}

	public static void CreateUnPaidInstance(Player player) {
		player.send(new SendMessage("You have teleported to the Non-Instanced Version of Cerberus"));
		Teleportation.teleport(player, new Position(1240, 1226, 0));
	}

	@Override
	public void onDeath(Mob mob) {
		if (mob.isPlayer() && mob.equals(player)) {
			player.send(new SendMessage("Better luck next time!"));
			cleanup();
			remove(player);
			return;
		}
		if (mob.isNpc() && mob.getNpc().id == CERBERUS) {
			World.schedule(new NpcDeath(mob.getNpc(), () -> {
				//completed = true;
				finish();
			}));
			return;
		}
		super.onDeath(mob);
	}

	@Override
	public void add(Mob mob) {
		super.add(mob);
		if (mob.isNpc()) {
			if (mob.getNpc().id == CERBERUS) {
				cerberus = mob.getNpc();
			} else {
				ghosts.add(mob.getNpc());
			}
			mob.locking.lock();
		}
	}

	@Override
	public void remove(Mob mob) {
		if (!mob.isNpc()) {
			super.remove(mob);
			return;
		}
		int id = mob.getNpc().id;
		if (id == CERBERUS) {
			cerberus = null;
			Teleportation.teleport(player, Config.DEFAULT_POSITION, 20, () -> {
			});
		} else {
			ghosts.remove(mob.getNpc());
		}
		super.remove(mob);
	}

	@Override
	protected void start() {
		Npc npc = new Npc(CERBERUS, CERBERUS_POS);
		npc.face(player);
		npc.owner = player;
		add(npc);
		player.face(cerberus.getPosition());
		npc.locking.unlock();
		pause();
	}

	@Override
	public void onLogout(Player player) {
		player.move(Config.DEFAULT_POSITION);
		cleanup();
		finish();
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inCerberus(player)) {
			cleanup();
			finish();
		}
	}

	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	// new method = good code.
	@Override
	public void finish() {
		boolean successfull = cerberus.isDead();
		cleanup();
		remove(player);
		if (successfull) {
			player.activityLogger.add(ActivityLog.CERBERUS);
			player.message("Congratulations, you have killed the Cerberus. ");
			restart(10, () -> {
				if (Area.inCerberus(player)) {
					create(player);
				} else {
					remove(player);
					if (PlayerRight.isDonator(player) || PlayerRight.isSuper(player)) {
						player.setBossPoints(player.getBossPoints() + 2);
						player.message("<img=2>You now have @red@" + player.getBossPoints() + " Boss Points!");

					}

					if (PlayerRight.isExtreme(player) || PlayerRight.isElite(player)) {
						player.setBossPoints(player.getBossPoints() + 3);
						player.message("<img=2>You now have @red@" + player.getBossPoints() + " Boss Points!");

					}
					if (PlayerRight.isKing(player)) {
						player.setBossPoints(player.getBossPoints() + 4);
						player.message("<img=2>You now have @red@" + player.getBossPoints() + " Boss Points!");

					} else {
						player.setBossPoints(player.getBossPoints() + 1);
						player.message("<img=2>You now have @red@" + player.getBossPoints() + " Boss Points!");

					}
				}
			});
		}
	}

	/*
	 * @Override public void finish() { //old method, shit code cleanup();
	 * 
	 * if (completed) { player.send(new
	 * SendMessage("Congratulations, you have killed the Cerberus.")); //+
	 * Utility.getTime(player.gameRecord.end(ActivityType.CERBERUS)) + "</col>."));
	 * } else { player.gameRecord.end(ActivityType.CERBERUS, false); }
	 * 
	 * remove(player);
	 * player.message("Please teleport back to Cerberus to fight him again!"); }
	 */

	@Override
	public void cleanup() {
		if (cerberus != null && cerberus.isRegistered())
			cerberus.unregister();
		Iterator<Npc> it = ghosts.iterator();
		while (it.hasNext()) {
			Npc npc = it.next();
			npc.animate(npc.definition.getDeathAnimation());
			World.schedule(3, npc::unregister);
			it.remove();
		}
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		return true;
	}

	@Override
	public ActivityType getType() {
		return ActivityType.CERBERUS;
	}

	@Override
	protected Optional<CerberusActivityListener> getListener() {
		return Optional.of(listener);
	}
}
