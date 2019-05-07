package io.server.content.skill.impl.hunter.trap.impl;

import java.util.Arrays;
import java.util.Objects;

import io.server.Config;
import io.server.game.Animation;
import io.server.game.task.TickableTask;
import io.server.game.world.World;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.object.GameObject;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Position;
import io.server.util.Utility;

public class BoxTrap {
	
	private Player owner;
	public GameObject object;
	private State state = State.SET;
	private Chinchompa chinchompa;
	public boolean inactive;
	
	public BoxTrap(Player owner, GameObject object) {
		this.owner = owner;
		this.object = object;
	}
	public BoxTrap(Player owner, GameObject object, State state, Chinchompa bird) {
		this.owner = owner;
		this.object = object;
		this.state = state;
		this.chinchompa = bird;
	}
	public enum Chinchompa {//same here
		GREY(2910, 10033, 53, 198),
		RED(2911, 10034, 63, 265),
		BLACK(2912, 11959, 73, 315);
		
		int npcId, itemId, level, exp;
		
		Chinchompa(int npcId, int itemId, int level, int exp) { 
			this.npcId = npcId; 
			this.itemId = itemId;
			this.level = level;
			this.exp = exp;
		}
	}
	public enum State {
		BROKEN(9385), 
		SHAKING(9382),
		SET(9380);
		public int objectId;
		State(int objectId) { 
			this.objectId = objectId; 
		}
	}
	
	public void setTrap() {
		for (BoxTrap box : owner.boxes) {
			if (box != null && box != this && !box.inactive && box.object.getPosition().equals(owner.getPosition())) {
				this.inactive = true;
				return;
			}
		}
		for (BirdSnare snare : owner.snares) {
			if (snare != null && !snare.inactive && snare.object.getPosition().equals(owner.getPosition())) {
				this.inactive = true;
				return;
			}
		}
		Position walkTo = owner.getPosition();

		if (TraversalMap.isTraversable(owner.getPosition(), Direction.WEST, owner.width())) {
			walkTo = walkTo.west();
		} else if (TraversalMap.isTraversable(owner.getPosition(), Direction.EAST, owner.width())) {
			walkTo = walkTo.east();
		} else if (TraversalMap.isTraversable(owner.getPosition(), Direction.SOUTH, owner.width())) {
			walkTo = walkTo.north();
		} else if (TraversalMap.isTraversable(owner.getPosition(), Direction.NORTH, owner.width())) {
			walkTo = walkTo.south();
		}

		if (!owner.getPosition().equals(walkTo)) {
			owner.movement.walkTo(walkTo);
		}
		
		owner.animate(new Animation(827));
		World.schedule(boxTask());
		owner.inventory.remove(10008);
		object.register();
	}
	
	public void pickUp() {
		if (state == null || state.equals(State.BROKEN) || chinchompa == null)
			owner.inventory.addOrDrop(new Item(10008));
		else {
			owner.inventory.addOrDrop(new Item(chinchompa.itemId));
			owner.inventory.addOrDrop(new Item(526));
			owner.inventory.addOrDrop(new Item(10008));
			owner.skills.addExperience(Skill.HUNTER, (chinchompa.exp * Config.HUNTER_MODIFICATION));
		}
		inactive = true;
		object.unregister();
	}
	
	private TickableTask boxTask() {
		return new TickableTask(false, 6) {
			@Override
			protected void tick() {
				if (owner == null) {
					cancel();
					inactive = true;
					return;
				}
				if (inactive) {
					object.unregister();
					cancel();
					return;
				}
				int lvl = owner.skills.getLevel(Skill.HUNTER);
				if (!state.equals(State.BROKEN) && chinchompa == null)
					World.getNpcs().stream().filter(Objects::nonNull).forEach(npc -> {
						Arrays.asList(Chinchompa.values()).stream().filter(b -> b.npcId == npc.id).forEach(chin -> {
							int chance = (99 - (lvl - chin.level)) / 20;
							if ((chance <= 0 || Utility.random(chance) <= 1) && Utility.random(0, 10, true) > 3) {
								if (npc.getPosition().equals(object.getPosition()) && !npc.getCombat().inCombat()) {
									chinchompa = chin;
									npc.locking.lock();
									npc.appendDeath();
									state = State.SHAKING;
									World.schedule(6, () -> object.transform(state.objectId-1));
									World.schedule(7, () -> {
										Position moveTo = object.getPosition();
										for (int i = 0; i < 3; i++) {
											int random = Utility.random(1, 4, true);
											if (random == 1 && TraversalMap.isTraversable(object.getPosition(), Direction.WEST, object.width())) {
												moveTo = moveTo.west();
											} else if (random == 2 && TraversalMap.isTraversable(object.getPosition(), Direction.EAST, object.width())) {
												moveTo = moveTo.east();
											} else if (random == 3 && TraversalMap.isTraversable(object.getPosition(), Direction.SOUTH, object.width())) {
												moveTo = moveTo.north();
											} else if (random == 4 && TraversalMap.isTraversable(object.getPosition(), Direction.NORTH, object.width())) {
												moveTo = moveTo.south();
											}
										}
										if (moveTo.isWithinDistance(npc.spawnPosition, 20)) {
											npc.setPosition(moveTo);
										}
										object.transform(state.objectId);
									});
									return;
								}
							}
						});
					});
				if (tick > 40) {
					cancel();
					GroundItem.create(owner, new Item(10008), object.getPosition());
					object.unregister();
					inactive = true;
				} else if (tick > 30) {
					state = State.BROKEN;
					object.transform(state.objectId);
				}
			}
		};
	}
}