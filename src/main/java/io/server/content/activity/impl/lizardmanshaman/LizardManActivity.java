package io.server.content.activity.impl.lizardmanshaman;

import java.util.Optional;

import io.server.Config;
import io.server.content.ActivityLog;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityDeathType;
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

public class LizardManActivity extends Activity {

	private final Player player;
	public Npc shaman = null;
	private final LizardManActivityListener listener = new LizardManActivityListener(this);

	private static final int SHAMAN = 6766;
	private static final Position SHAMAN_POS = new Position(1436, 3709, 0);

	private LizardManActivity(Player player, int instance) {
		super(1, instance);
		this.player = player;
	}

	public static LizardManActivity create(Player player) {
		LizardManActivity minigame = new LizardManActivity(player, player.playerAssistant.instance());
		minigame.add(player);
		return minigame;
	}

	public static void CreatePaidInstance(Player player) {
		if (!player.bank.contains(995, 1000000)) {
			player.message("You need to have 1,000,000 coins inside your bank to pay for the instance!");
			return;
		} else {
			player.bank.remove(995, 1000000);
			Teleportation.teleport(player, new Position(1436, 3709, 0), 20, () -> create(player));
			player.send(new SendMessage("You have teleported to the Instanced Version of Lizard man"));
			player.send(new SendMessage("1,000,000 coins has been taken out of your bank, as a fee."));

		}
	}

	public static void CreateUnPaidInstance(Player player) {
		player.send(new SendMessage("You have teleported to the Non-Instanced Version of Lizard man Shaman"));
		Teleportation.teleport(player, new Position(1436, 3709, 0));
	}

	@Override
	public void add(Mob mob) {
		super.add(mob);
		if (mob.isNpc()) {
			if (mob.getNpc().id == SHAMAN) {
				shaman = mob.getNpc();
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
		if (id == SHAMAN) {
			shaman = null;
			Teleportation.teleport(player, Config.DEFAULT_POSITION, 20, () -> {
				player.send(new SendMessage("Get yo ass back home boi, " + player.getName() + "!"));
			});
		}
		super.remove(mob);
	}

	@Override
	protected void start() {
		Npc npc = new Npc(SHAMAN, SHAMAN_POS);
		npc.face(player);
		npc.owner = player;
		add(npc);
		player.face(shaman.getPosition());
		npc.locking.unlock();
		pause();
	}

	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	@Override
	public void finish() {
		boolean successfull = shaman.isDead();
		cleanup();
		remove(player);
		if (successfull) {
			player.activityLogger.add(ActivityLog.LIZARDMAN_SHAMAN);
			player.message("Congratulations, you have killed the Lizardman Shaman.");
			restart(1, () -> {
				if (Area.inShaman(player)) {
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

	@Override
	public void cleanup() {
		if (shaman != null && shaman.isRegistered())
			shaman.unregister();
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		return true;
	}

	@Override
	protected Optional<LizardManActivityListener> getListener() {
		return Optional.of(listener);
	}

	@Override
	public void onLogout(Player player) {
		cleanup();
		remove(player);
	}

	@Override
	public void onDeath(Mob mob) {
		if (mob.isNpc() && mob.getNpc().equals(shaman)) {
			World.schedule(new NpcDeath(mob.getNpc(), this::finish));
			return;
		}
		super.onDeath(mob);
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inShaman(player)) {
			cleanup();
			remove(player);
		}
	}

	@Override
	public ActivityDeathType deathType() {
		return ActivityDeathType.PURCHASE;
	}

	@Override
	public ActivityType getType() {
		return ActivityType.LIZARD_INSTANCE;
	}
}
