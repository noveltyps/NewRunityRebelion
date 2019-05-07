package io.server.content.activity.impl.giantmole;

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

public class GiantMoleActivity extends Activity {

	private final Player player;
	public Npc mole = null;
	private final GiantMoleActivityListener listener = new GiantMoleActivityListener(this);

	private static final int MOLE = 6499;
	private static final Position MOLE_POS = new Position(1761, 5186, 0);

	private GiantMoleActivity(Player player, int instance) {
		super(1, instance);
		this.player = player;
	}

	public static GiantMoleActivity create(Player player) {
		GiantMoleActivity minigame = new GiantMoleActivity(player, player.playerAssistant.instance());
		minigame.add(player);
		return minigame;
	}

	public static void CreatePaidInstance(Player player) {
		if (!player.bank.contains(995, 1000000)) {
			player.message("You need to have 1,000,000 coins inside your bank to pay for the instance!");
			return;
		} else {
			player.bank.remove(995, 1000000);
			Teleportation.teleport(player, new Position(1765, 5185, 0), 20, () -> create(player));
			player.send(new SendMessage("You have teleported to the Instanced Version of Giant Mole"));
			player.send(new SendMessage("1,000,000 coins has been taken out of your bank, as a fee."));

		}
	}

	public static void CreateUnPaidInstance(Player player) {
		player.send(new SendMessage("You have teleported to the Non-Instanced Version of Giant Mole"));
		Teleportation.teleport(player, new Position(1765, 5185, 0));
	}

	@Override
	public void add(Mob mob) {
		super.add(mob);
		if (mob.isNpc()) {
			if (mob.getNpc().id == MOLE) {
				mole = mob.getNpc();
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
		if (id == MOLE) {
			mole = null;
			Teleportation.teleport(player, Config.DEFAULT_POSITION, 20, () -> {
				player.send(new SendMessage("Get yo ass back home boi, " + player.getName() + "!"));
			});
		}
		super.remove(mob);
	}

	@Override
	protected void start() {
		Npc npc = new Npc(MOLE, MOLE_POS);
		npc.face(player);
		npc.owner = player;
		add(npc);
		player.face(mole.getPosition());
		npc.locking.unlock(); //this caused it ye, this is what i did for lizard shaman
		pause();
	}

	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	@Override
	public void finish() {
		boolean successfull = mole.isDead();
		cleanup();
		remove(player);
		if (successfull) {
			player.activityLogger.add(ActivityLog.GIANT_MOLE);
			player.message("Congratulations, you have killed the Giant Mole.");
			restart(1, () -> {
				if (Area.inMole(player)) {
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
		if (mole != null && mole.isRegistered())
			mole.unregister();
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		return true;
	}

	@Override
	protected Optional<GiantMoleActivityListener> getListener() {
		return Optional.of(listener);
	}

	@Override
	public void onLogout(Player player) {
		cleanup();
		remove(player);
	}

	@Override
	public void onDeath(Mob mob) {
		if (mob.isNpc() && mob.getNpc().equals(mole)) {
			World.schedule(new NpcDeath(mob.getNpc(), this::finish));
			return;
		}
		super.onDeath(mob);
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inMole(player)) {
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
		return ActivityType.MOLE_INSTANCE;
	}
}
