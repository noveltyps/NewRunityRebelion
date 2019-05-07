package io.server.content.combat.cannon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.server.game.Animation;
import io.server.game.Projectile;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.position.Area;
import io.server.game.world.region.Region;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * 
 * @author Adam_#6723
 *
 */

public class CannonManager {

	static Map<String, Cannon> ACTIVE_CANNONS = new HashMap<>();

	public enum Setup {
		NO_CANNON, BASE, STAND, BARRELS, FURNACE, COMPLETE_CANNON
	}

	public enum Rotation {
		NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
	}

	public static Cannon getCannon(Player player) {
		return ACTIVE_CANNONS.get(player.getName());
	}

	public static void drop(Player player, Cannon cannon) {
		if (Area.inVorkath(player)) {
			player.message("Cannon cannot be used in vorkath!");
			return;
		}
		if(Area.inCorp(player)) {
			player.message("Cannon cannot be used in vorkath!");
			return;
		}
		if (Area.inCerberus(player)) {
			player.message("Cannon cannot be used in cerberus!");
			return;
		}
		if (Area.inZulrah(player)) {
			player.message("Cannon cannot be used in Zulrah!");
			return;
		}
		if (Area.inKraken(player)) {
			player.message("Cannon cannot be used in Kraken!");
			return;
		}
		if (ACTIVE_CANNONS.containsKey(player.getName())) {
			player.send(new SendMessage("You already have a cannon active!"));
			return;
		}

		if (cannon.getStage().ordinal() != 0) {
			player.send(new SendMessage("You have already started setting up a cannon!"));
			return;
		}

		for (Cannon other : ACTIVE_CANNONS.values()) {
			if (other.getPosition().isWithinDistance(player.getPosition(), 5)) {
				player.send(new SendMessage("You are trying to build too close to another cannon!"));
				return;
			}
		}

		if (!playerHasCannon(player)) {
			player.send(new SendMessage("You do not have a full cannon in your inventory!"));
			return;
		}

		World.schedule(new CannonBuild(player, cannon));
	}

	public static void pickup(Player player) {
		Cannon cannon = ACTIVE_CANNONS.get(player.getName());

		if (cannon == null) {
			player.send(new SendMessage("This is not your cannon!"));
			return;
		}

		if (!cannon.getOwner().equalsIgnoreCase(player.getName())) {
			player.send(new SendMessage("This is not your cannon!"));
			return;
		}
		player.inventory.add(new Item(6));
		player.inventory.add(new Item(8));
		player.inventory.add(new Item(10));
		player.inventory.add(new Item(12));
		player.inventory.add(new Item(2, cannon.getAmmunition()));
		ACTIVE_CANNONS.remove(player.getName());
		player.animate(new Animation(827));
		cannon.getObject().unregister();
	}

	public static void load(Player player) {
		Cannon cannon = ACTIVE_CANNONS.get(player.getName());

		if (cannon == null) {
			player.send(new SendMessage("This is not your cannon!"));
			return;
		}

		if (!cannon.getOwner().equalsIgnoreCase(player.getName())) {
			player.send(new SendMessage("This is not your cannon!"));
			return;
		}

		if (!player.inventory.contains(2)) {
			player.send(new SendMessage("You do not have any Cannon balls."));
			return;
		}

		int needed = 30 - cannon.getAmmunition();

		if (needed == 0) {
			player.send(new SendMessage("Your cannon is full."));
			return;
		}

		int cannon_balls = player.inventory.computeAmountForId(2);

		if (cannon_balls <= needed) {
			player.inventory.remove(2, cannon_balls);
			player.send(new SendMessage("You load the last of your cannon balls"));
			cannon.setAmmunition(cannon.getAmmunition() + cannon_balls);
		} else {
			player.inventory.remove(2, needed);
			player.send(new SendMessage("You load " + needed + " balls into the cannon."));
			cannon.setAmmunition(cannon.getAmmunition() + needed);
		}

		if (cannon.isFiring()) {
			player.send(new SendMessage("The cannon is already firing!"));
			return;
		}

		cannon.setFiring(true);
		World.schedule(new CannonFireAction(player, cannon));
	}

	public static Projectile getCannonFire() {
		Projectile p = new Projectile(53);
		p.setStartHeight(50);
		p.setEndHeight(50);
		p.setDelay(2);
		return p;
	}

	public static boolean playerHasCannon(Player player) {
		return player.inventory.contains(6) && player.inventory.contains(8) && player.inventory.contains(10)
				&& player.inventory.contains(12);
	}

	public static Npc[] getNpc(Cannon cannon) {
		ArrayList<Npc> attack = new ArrayList<>();

		for (Npc npc : World.getNpcs()) {
			if (npc == null) {
				continue;
			}

			if (!Utility.withinDistance(npc, cannon, Region.VIEW_DISTANCE)) {
				continue;
			}

			if (!npc.definition.isAttackable()) {
				continue;
			}

			attack.add(npc);
		}

		Npc[] npc = new Npc[attack.size()];

		for (int i = 0; i < npc.length; i++) {
			npc[i] = attack.get(i);
		}

		return npc;
	}

	public static void rotate(Cannon cannon) {
		switch (cannon.getRotation()) {
		case NORTH:
			World.sendObjectAnimation(516, cannon.getObject());
			break;
		case NORTH_EAST:
			World.sendObjectAnimation(517, cannon.getObject());
			break;
		case EAST:
			World.sendObjectAnimation(518, cannon.getObject());
			break;
		case SOUTH_EAST:
			World.sendObjectAnimation(519, cannon.getObject());
			break;
		case SOUTH:
			World.sendObjectAnimation(520, cannon.getObject());
			break;
		case SOUTH_WEST:
			World.sendObjectAnimation(521, cannon.getObject());
			break;
		case WEST:
			World.sendObjectAnimation(514, cannon.getObject());
			break;
		case NORTH_WEST:
			World.sendObjectAnimation(515, cannon.getObject());
			break;
		}
	}

}
