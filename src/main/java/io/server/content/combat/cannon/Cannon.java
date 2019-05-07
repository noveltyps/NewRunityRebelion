package io.server.content.combat.cannon;

import io.server.content.combat.cannon.CannonManager.Rotation;
import io.server.content.combat.cannon.CannonManager.Setup;
import io.server.game.world.entity.Entity;
import io.server.game.world.entity.EntityType;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.position.Position;
import io.server.game.world.region.Region;
import io.server.net.packet.out.SendAddObject;
import io.server.net.packet.out.SendRemoveObject;

/**
 * Handles the dwarf cannon.
 * 
 * @Adam_#6723
 */
public class Cannon extends Entity {

	private final String owner;

	private final Position position;

	private CustomGameObject object;

	private int ammunition;

	private boolean firing;

	private Setup stage;

	private Rotation rotation;

	public Cannon(String owner, Position position) {
		super(position);
		this.owner = owner;
		this.position = position;
		this.ammunition = 0;
		this.firing = false;
		this.stage = Setup.NO_CANNON;
		this.rotation = Rotation.NORTH;
		this.object = new CustomGameObject(8, position);
	}

	public String getOwner() {
		return owner;
	}

	public Position getPosition() {
		return position;
	}

	public int getAmmunition() {
		return ammunition;
	}

	public void setAmmunition(int ammunition) {
		this.ammunition = ammunition;
	}

	public boolean isFiring() {
		return firing;
	}

	public void setFiring(boolean firing) {
		this.firing = firing;
	}

	public Setup getStage() {
		return stage;
	}

	public void setStage(Setup stage) {
		this.stage = stage;
	}

	public CustomGameObject getObject() {
		return object;
	}

	public void setObject(CustomGameObject object) {
		this.object = object;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}

	@Override
	public void register() {
		if (!isRegistered()) {
			Region region = getRegion();
			setRegistered(true);

			if (region == null) {
				setPosition(getPosition());
			} else if (!region.containsObject(getHeight(), object)) {
				addToRegion(region);
			}
		}
	}

	@Override
	public void unregister() {
		if (isRegistered()) {
			destroy();
		}
	}

	@Override
	public void addToRegion(Region region) {
		region.getPlayers(getHeight()).stream().filter(Player::isValid)
				.forEach(player -> player.send(new SendAddObject(object)));
		region.addObject(object);
	}

	@Override
	public void removeFromRegion(Region region) {
		region.getPlayers(getHeight()).stream().filter(Player::isValid)
				.forEach(player -> player.send(new SendRemoveObject(object)));
		region.removeObject(object);
	}

	@Override
	public String getName() {
		return "Dwarf cannon";
	}

	@Override
	public EntityType getType() {
		return EntityType.DWARF_CANNON;
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
