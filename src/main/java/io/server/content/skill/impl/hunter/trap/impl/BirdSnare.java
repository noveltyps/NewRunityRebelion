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

public class BirdSnare {

	private Player owner;
	public GameObject object;
	private State state = State.SET;
	private Bird bird;
	public boolean inactive;

	public BirdSnare(Player owner, GameObject object) {
		this.owner = owner;
		this.object = object;
	}
	public BirdSnare(Player owner, GameObject object, State state, Bird bird) {
		this.owner = owner;
		this.object = object;
		this.state = state;
		this.bird = bird;
	}
	public enum Bird {//npc ids are 1st one
		CRIMSON_SWIFT(5549, 9379, 10088, 1, 34), 
		GOLDEN_WARBLERS(5551, 9377, 10090, 5, 47), 
		COPPER_LONGTAILS(5552, 9373, 10091, 9, 61), 
		CERULEAN_TWITCHES(5550, 9375, 10089, 11, 65), 
		TROPICAL_WAGTAILS(5548, 9348, 10087, 19, 95);

		int npcId, objectId, itemId, level, exp;

		Bird(int npcId, int objectId, int itemId, int level, int exp) { 
			this.npcId = npcId; 
			this.objectId = objectId; 
			this.itemId = itemId;
			this.level = level;
			this.exp = exp;
		}
	}
	public enum State {
		BROKEN(9344), 
		SET(9345);
		public int objectId;
		State(int objectId) { 
			this.objectId = objectId; 
		}
	}

	public void setTrap() {
		for (BoxTrap trap : owner.boxes) {
			if (trap != null && !trap.inactive && trap.object.getPosition().equals(owner.getPosition())) {
				this.inactive = true;
				return;
			}
		}
		for (BirdSnare snare : owner.snares) {
			if (snare != null && snare != this && !snare.inactive && snare.object.getPosition().equals(owner.getPosition())) {
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
		World.schedule(snareTask());
		owner.inventory.remove(10006);
		object.register();
	}

	public void pickUp() {
		if (state == null || state.equals(State.BROKEN) || bird == null)
			owner.inventory.addOrDrop(new Item(10006));
		else {
			owner.inventory.addOrDrop(new Item(bird.itemId, Utility.random(10) + 5));
			owner.inventory.addOrDrop(new Item(9978));
			owner.inventory.addOrDrop(new Item(526));
			owner.inventory.addOrDrop(new Item(10006));
			owner.skills.addExperience(Skill.HUNTER, (bird.exp * Config.HUNTER_MODIFICATION));
		}
		inactive = true;
		object.unregister();
	}

	private TickableTask snareTask() {
		int in = 8 - owner.skills.getLevel(Skill.HUNTER)/25;
		return new TickableTask(false, in) {
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
				if (!state.equals(State.BROKEN) && bird == null)
					World.getNpcs().stream().filter(Objects::nonNull).forEach(npc -> {
						Arrays.asList(Bird.values()).stream().filter(b -> b.npcId == npc.id).forEach(b -> {
							int chance = (99 - (lvl - b.level)) / 20;
							if ((chance <= 0 || Utility.random(chance) <= 1) && Utility.random(0, 10, true) > 3) {
								if (npc.getPosition().equals(object.getPosition())) {
									bird = b;
									npc.locking.lock();
									npc.appendDeath();
									World.schedule(6, () -> object.transform(bird.objectId-1));
									World.schedule(7, () -> {
										Position moveTo = object.getPosition();
										for (int i = 0; i < 3; i++) {
											int random = Utility.random(1, 4);
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
										object.transform(bird.objectId);
									});
									return;
								}
							}
						});
					});

				if (tick > 40) {
					cancel();
					GroundItem.create(owner, new Item(10006), object.getPosition());
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