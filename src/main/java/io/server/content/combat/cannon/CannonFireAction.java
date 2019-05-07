package io.server.content.combat.cannon;

import io.server.Config;
import io.server.content.combat.cannon.CannonManager.Rotation;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.HitIcon;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * 
 * @author Adam_#6723
 *
 */

public final class CannonFireAction extends Task {
	private final Player player;
	private final Cannon cannon;

	public CannonFireAction(Player player, Cannon cannon) {
		super(true, 2);
		this.player = player;
		this.cannon = cannon;
	}

	@Override
	public void execute() {
		if (CannonManager.getCannon(player) == null) {
			cannon.setFiring(false);
			cannon.setAmmunition(0);
			cancel();
			return;
		}

		if (cannon.getAmmunition() <= 0) {
			player.send(new SendMessage("Your cannon has run out of ammunition!"));
			cannon.setFiring(false);
			cannon.setAmmunition(0);
			cancel();
			return;
		}

		switch (cannon.getRotation()) {
		case NORTH:
			cannon.setRotation(Rotation.NORTH_EAST);
			break;
		case NORTH_EAST:
			cannon.setRotation(Rotation.EAST);
			break;
		case EAST:
			cannon.setRotation(Rotation.SOUTH_EAST);
			break;
		case SOUTH_EAST:
			cannon.setRotation(Rotation.SOUTH);
			break;
		case SOUTH:
			cannon.setRotation(Rotation.SOUTH_WEST);
			break;
		case SOUTH_WEST:
			cannon.setRotation(Rotation.WEST);
			break;
		case WEST:
			cannon.setRotation(Rotation.NORTH_WEST);
			break;
		case NORTH_WEST:
			cannon.setRotation(Rotation.NORTH);
			break;
		}

		CannonManager.rotate(cannon);

		Npc npc = targetMob();

		if (npc != null) {
			int lockon = npc.getIndex() + 1;
			byte offsetX = (byte) ((npc.getPosition().getY() - npc.getPosition().getY()) * -1);
			byte offsetY = (byte) ((npc.getPosition().getX() - npc.getPosition().getX()) * -1);
			Position cannonPosition = new Position(cannon.getPosition().getX() + 1, cannon.getPosition().getY() + 1,
					cannon.getPosition().getHeight());
			World.sendProjectile(CannonManager.getCannonFire(), cannonPosition, lockon, offsetX, offsetY);

			Hit hit = new Hit(Utility.random(0, 30), HitIcon.CANON);
			double experience = hit.getDamage() * Config.COMBAT_MODIFICATION;
			player.skills.addExperience(Skill.RANGED, experience);

			npc.damage(hit);
			cannon.setAmmunition(cannon.getAmmunition() - 1);
		}
	}

	private Npc targetMob() {
		Npc[] npcs = CannonManager.getNpc(cannon);

		if (npcs != null) {
			for (Npc npc : npcs) {
				if (npc != null) {

					int cannonX = cannon.getPosition().getX();
					int cannonY = cannon.getPosition().getY();
					int npcX = npc.getX();
					int npcY = npc.getY();

					if (!npc.isDead() && npc.getHeight() == cannon.getPosition().getHeight()
							&& npc.getCurrentHealth() != 0) {

						if (cannon.getRotation() == null) {
							cannon.setRotation(Rotation.NORTH);
						}

						switch (cannon.getRotation()) {
						case NORTH:
							if (npcY < cannonY && npcX >= cannonX - 1 && npcX <= cannonX + 1)
								return npc;
							break;
						case NORTH_EAST:
							if (npcX <= cannonX - 1 && npcY <= cannonY - 1)
								return npc;
							break;
						case EAST:
							if (npcX < cannonX && npcY >= cannonY - 1 && npcY <= cannonY + 1)
								return npc;
							break;
						case SOUTH_EAST:
							if (npcX <= cannonX - 1 && npcY >= cannonY + 1)
								return npc;
							break;
						case SOUTH:
							if (npcY > cannonY && npcX >= cannonX - 1 && npcX <= cannonX + 1)
								return npc;
							break;
						case SOUTH_WEST:
							if (npcX >= cannonX + 1 && npcY >= cannonY + 1)
								return npc;
							break;
						case WEST:
							if (npcX > cannonX && npcY >= cannonY - 1 && npcY <= cannonY + 1)
								return npc;
							break;
						case NORTH_WEST:
							if (npcY <= cannonY - 1 && npcX >= cannonX + 1)
								return npc;
							break;
						}
					}
				}
			}
		}
		return null;
	}
}
