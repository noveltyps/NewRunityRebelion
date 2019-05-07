package io.server.game.world.entity.mob;

import static io.server.game.world.entity.combat.CombatConstants.EMPTY_BONUSES;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.action.ActionManager;
import io.server.game.task.impl.ForceMovementTask;
import io.server.game.world.Interactable;
import io.server.game.world.World;
import io.server.game.world.entity.Entity;
import io.server.game.world.entity.EntityType;
import io.server.game.world.entity.combat.Combat;
import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.PoisonType;
import io.server.game.world.entity.combat.effect.CombatEffectType;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.strategy.CombatStrategy;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.movement.Movement;
import io.server.game.world.entity.mob.movement.waypoint.CombatWaypoint;
import io.server.game.world.entity.mob.movement.waypoint.FollowWaypoint;
import io.server.game.world.entity.mob.movement.waypoint.WalkToWaypoint;
import io.server.game.world.entity.mob.movement.waypoint.Waypoint;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.ForceMovement;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.relations.ChatMessage;
import io.server.game.world.entity.mob.prayer.PrayerBook;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.entity.skill.SkillManager;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.equipment.Equipment;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendPoison;
import io.server.util.MutableNumber;
import io.server.util.Utility;
import io.server.util.generic.BooleanInterface;
import io.server.util.generic.GenericAttributes;

/**
 * Handles the mob class.
 *
 * @author Daniel
 * @author Chex
 */
public abstract class Mob extends Entity {
	public int activityDamage;
	public boolean brutalMode;
	private int listIndex;
	public int id = -1;
	//private int transformId;
	private boolean dead;
	public boolean regionChange;
	public boolean positionChange;
	public boolean forceWalking;
	public boolean teleporting;
	public boolean inTeleport;
	public boolean teleportRegion;
	public boolean blockFace;
	public String forceChat;
	public Mob interactingWith;
	public Hit firstHit;
	public Hit secondHit;
	public Position lastPosition;
	public Position teleportTarget;
	public Position facePosition;
	public Activity activity;
	private Optional<Animation> animation = Optional.empty();
	private Optional<Graphic> graphic = Optional.empty();
	public List<Mob> followers = new LinkedList<>();
	public ForceMovement forceMovement;
	public final EnumSet<UpdateFlag> updateFlags = EnumSet.noneOf(UpdateFlag.class);
	public final GenericAttributes attributes = new GenericAttributes();
	public final SkillManager skills = new SkillManager(this);
	public final Movement movement = new Movement(this);
	public MobAnimation mobAnimation = new MobAnimation(updateFlags);
	public ActionManager action = new ActionManager();
	protected Waypoint cachedWaypoint;
	public PrayerBook prayer = new PrayerBook();
	private int[] bonuses = EMPTY_BONUSES;
	private final MutableNumber poisonDamage = new MutableNumber();
	private final MutableNumber venomDamage = new MutableNumber();
	public final Locking locking = new Locking(this);
	private PoisonType poisonType;
	public boolean inBattleRealm = false;
	public boolean abortBot = true;

	/** Constructs a new <code>Mob</code>. */
	public Mob(Position position) {
		super(position);
		this.lastPosition = position.copy();
	}

	public Mob(Position position, boolean visible) {
		super(position, visible);
		this.lastPosition = position.copy();
	}

	/** Sets the mob's forced chat. */
	public void speak(String forceChat) {
		if (forceChat == null || forceChat.isEmpty() || forceChat.length() > ChatMessage.CHARACTER_LIMIT)
			return;
		this.forceChat = forceChat;
		this.updateFlags.add(UpdateFlag.FORCED_CHAT);
	}

	public void animate(int animation) {
		animate(new Animation(animation));
	}

	/** Plays an animation. */
	public void animate(Animation animation) {
		animate(animation, false);
	}

	public void graphic(int graphic) {
		graphic(new Graphic(graphic));
	}

	/** Plays a graphic. */
	public void graphic(Graphic graphic) {
		graphic(graphic, false);
	}

	/** Plays an animation. */
	public void animate(Animation animation, boolean override) {
		Optional<Animation> result = Optional.ofNullable(animation);
		animation = result.orElse(Animation.RESET);

		if (!this.animation.isPresent() || override || this.animation.get().compareTo(animation) > 0) {
			this.animation = result;
			this.updateFlags.add(UpdateFlag.ANIMATION);
		}
	}

	public void runTo(Position destination) {
		movement.dijkstraPath(destination);
	}

	/** Plays a graphic. */
	public void graphic(Graphic graphic, boolean override) {
		Optional<Graphic> result = Optional.ofNullable(graphic);
		graphic = result.orElse(Graphic.RESET);

		if (!this.graphic.isPresent() || override || this.graphic.get().compareTo(graphic) > 0) {
			this.graphic = result;
			this.updateFlags.add(UpdateFlag.GRAPHICS);
		}
	}

	/** Transforms the mob. */
	public void transform(int transformId) {
		//this.transformId = transformId;
		this.id = transformId;
		this.updateFlags.add(UpdateFlag.TRANSFORM);
		this.updateFlags.add(UpdateFlag.APPEARANCE);

		if (isNpc()) {
			NpcDefinition definition = NpcDefinition.get(id);
			getNpc().definition = definition;
			setWidth(definition.getSize());
			setLength(definition.getSize());
			setBonuses(definition.getBonuses());
			mobAnimation.setNpcAnimations(definition);
		}
	}

	/** Resets the mob after an update. */
	public final void reset() {
		resetAnimation();
		resetGraphic();
	}

	/** Resets the waypoint */
	public final void resetWaypoint() {
		if (cachedWaypoint != null && cachedWaypoint.isRunning()) {
			cachedWaypoint.cancel();
		}
	}

	public void forceMove(int delay, int animation, int startSpeed, int endSpeed, Position offset,
			Direction direction) {
		forceMove(delay, 0, animation, startSpeed, endSpeed, offset, direction);
	}

	public void forceMove(int delay, int delay2, int animation, int startSpeed, int endSpeed, Position offset,
			Direction direction) {
		forceMove(delay, delay2, animation, 0, startSpeed, endSpeed, offset, direction);
	}

	/** Creates a force movement action for an entity. */
	public void forceMove(int delay, int delay2, int animation, int animationDelay, int startSpeed, int endSpeed,
			Position offset, Direction direction) {
		ForceMovement movement = new ForceMovement(getPosition(), offset, startSpeed, endSpeed, direction);
		World.schedule(new ForceMovementTask(this, delay, delay2, movement, new Animation(animation, animationDelay)));
	}

	/** Sets the mob interacting with another mob. */
	public void interact(Mob mob) {
		if (blockFace)
			return;
		this.interactingWith = mob;
		this.updateFlags.add(UpdateFlag.INTERACT);
	}

	/** Sets the client update flag to face a certain direction. */
	public void face(GameObject object) {
		if (blockFace)
			return;
		if (object == null || object.getPosition().equals(facePosition))
			return;
		this.facePosition = object.getPosition();
		this.updateFlags.add(UpdateFlag.FACE_COORDINATE);
	}

	public void face(Mob mob) {
		face(mob.getPosition());
	}

	/** Sets the client update flag to face a certain direction. */
	public void face(Position position) {
		if (blockFace)
			return;
		if (!position.equals(facePosition)) {
			this.facePosition = position;
			this.updateFlags.add(UpdateFlag.FACE_COORDINATE);
		}
	}

	/** Sets the client update flag to face a certain direction. */
	public void face(Direction direction) {
		if (blockFace)
			return;
		Position position = getPosition().transform(direction.getFaceLocation());
		if (!position.equals(facePosition)) {
			this.facePosition = position;
			this.updateFlags.add(UpdateFlag.FACE_COORDINATE);
		}
	}

	/** Resets the mob's face location. */
	public void resetFace() {
		if (blockFace || interactingWith == null)
			return;
		interactingWith = null;
		this.updateFlags.add(UpdateFlag.INTERACT);
	}

	public boolean isTeleporting() {
		return teleportRegion;
	}

	/** Moves the mob to a set position. */
	public void move(Position position) {
		if (regionChange)
			return;
		if (isPlayer() && !getPlayer().interfaceManager.isClear())
			getPlayer().interfaceManager.close(false);
		setPosition(position);
		if (Utility.isRegionChange(position, lastPosition)) {
			regionChange = true;
		} else {
			positionChange = true;
		}
		teleportRegion = true;
		getCombat().reset();
		resetFace();
		locking.lock(599, TimeUnit.MILLISECONDS, LockType.MASTER);
		onStep();
	}

	public void forceMove(Position position) {

		setPosition(position);

		if (Utility.isRegionChange(position, lastPosition)) {
			regionChange = true;
		} else {
			positionChange = true;
		}
		teleportRegion = true;
		getCombat().reset();
		resetFace();
		locking.lock(599, TimeUnit.MILLISECONDS, LockType.MASTER);
		onStep();
		System.err.println("Exeucting this method?");
	}

	public void walk(Position position) {
		walk(position, false);
	}

	public void walk(Position destination, boolean ignoreClip) {
		if (ignoreClip) {
			movement.walk(destination);
		} else {
			movement.simplePath(destination);
		}
	}

	public void walkTo(Position position) {
		getCombat().reset();
		walkTo(position, () -> {
			/* Do nothing on arrival */ });
	}

	public void walkTo(Position position, Runnable onDestination) {
		Interactable interactable = Interactable.create(position);
		walkTo(interactable, onDestination);
	}

	public void walkExactlyTo(Position position) {
		walkExactlyTo(position, () -> {
		});
	}

	public void walkExactlyTo(Position position, Runnable onDestination) {
		Interactable interactable = Interactable.create(position, 0, 0);
		walkTo(interactable, onDestination);
	}

	public void walkTo(Interactable target, Runnable onDestination) {
		walkTo(target, true, onDestination);
	}

	public void walkTo(Interactable target, boolean clearAction, Runnable onDestination) {
		Waypoint waypoint = new WalkToWaypoint(this, target, onDestination);

		if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
			resetWaypoint();
			getCombat().reset();
			movement.reset();

			if (clearAction) {
				action.clearNonWalkableActions();
			}

			World.schedule(cachedWaypoint = waypoint);
		}
	}

	public void follow(Mob target) {
		Waypoint waypoint = new FollowWaypoint(this, target);
		if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
			resetWaypoint();
			movement.reset();
			action.clearNonWalkableActions();
			World.schedule(cachedWaypoint = waypoint);
		}
	}

	public void attack(Mob target) {

		// if (near(getPlayer().getPosition(), 2)) {
		// return;
		// }
		Waypoint waypoint = new CombatWaypoint(this, target);
		if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
			resetWaypoint();
			movement.reset();
			action.clearNonWalkableActions();
			World.schedule(cachedWaypoint = waypoint);
		}
	}

	protected void setWaypoint(Waypoint waypoint) {
		if (cachedWaypoint == null || (!cachedWaypoint.isRunning() || !waypoint.equals(cachedWaypoint))) {
			resetWaypoint();
			movement.reset();
			action.clearNonWalkableActions();
			World.schedule(cachedWaypoint = waypoint);
		}
	}

	public void damage(Hit... hits) {
		for (Hit hit : hits)
			getCombat().queueDamage(hit);
	}

	public void writeFakeDamage(Hit hit) {
		if (!updateFlags.contains(UpdateFlag.FIRST_HIT)) {
			firstHit = hit;
			updateFlags.add(UpdateFlag.FIRST_HIT);
		} else {
			secondHit = hit;
			updateFlags.add(UpdateFlag.SECOND_HIT);
		}
	}

	public void writeDamage(Hit hit) {
		if (!updateFlags.contains(UpdateFlag.FIRST_HIT)) {
			firstHit = decrementHealth(hit);
			updateFlags.add(UpdateFlag.FIRST_HIT);
		} else {
			secondHit = decrementHealth(hit);
			updateFlags.add(UpdateFlag.SECOND_HIT);
		}
	}

	public Hit decrementHealth(Hit hit) {
		if (getCurrentHealth() - hit.getDamage() < 0)
			hit.modifyDamage(damage -> getCurrentHealth());
		skills.modifyLevel(level -> level - hit.getDamage(), Skill.HITPOINTS, 0, getCurrentHealth());
		skills.refresh(Skill.HITPOINTS);
		if (getCurrentHealth() < 1)
			appendDeath();

		return hit;
	}

	public void heal(int amount) {
		int health = getCurrentHealth();
		if (health >= getMaximumHealth())
			return;
		skills.modifyLevel(hp -> health + amount, Skill.HITPOINTS, 0, getMaximumHealth());
		skills.refresh(Skill.HITPOINTS);
	}

	/** Applies poison with an intensity of {@code type} to the entity. */
	public void poison(PoisonType type) {
		poisonType = type;
		CombatUtil.effect(this, CombatEffectType.POISON);
	}

	/** Applies venom to the entity. */
	public void venom() {
		CombatUtil.effect(this, CombatEffectType.VENOM);
	}

	public void setForceMovement(ForceMovement forceMovement) {
		this.forceMovement = forceMovement;
		if (forceMovement != null)
			this.updateFlags.add(UpdateFlag.FORCE_MOVEMENT);
	}

	public boolean inActivity() {
		return activity != null;
	}

	public boolean inActivity(ActivityType type) {
		return inActivity() && activity.getType() == type;
	}

	public void setActivity(Activity activity) {
		if (this.activity != null)
			this.activity.cleanup();
		this.activity = activity;
	}

	/** Resets the teleport target. */
	public void clearTeleportTarget() {
		this.teleportTarget = null;
	}

	/** Checks if mob requires an update. */
	public boolean isUpdateRequired() {
		return !updateFlags.isEmpty();
	}

	/** Check if an entity is an npc. */
	public final boolean isNpc() {
		return getType() == EntityType.NPC;
	}

	/** Check if an entity is an npc. */
	public final boolean isNpc(BooleanInterface<Npc> condition) {
		return getType() == EntityType.NPC && condition.activated(getNpc());
	}

	/** Check if an entity is a player */
	public final boolean isPlayer() {
		return getType() == EntityType.PLAYER;
	}

	public final Npc getNpc() {
		return (Npc) this;
	}

	/** Check if an entity is a player */
	public final boolean isPlayer(Function<Player, Boolean> condition) {
		return getType() == EntityType.PLAYER && condition.apply(getPlayer());
	}

	public ForceMovement getForceMovement() {
		return forceMovement;
	}

	public void unpoison() {
		poisonDamage.set(0);
		poisonType = null;

		if (this instanceof Player) {
			Player player = (Player) this;
			player.send(new SendPoison(SendPoison.PoisonType.NO_POISON));
		}
	}

	public void unvenom() {
		venomDamage.set(0);

		if (this instanceof Player) {
			Player player = (Player) this;
			player.send(new SendPoison(SendPoison.PoisonType.NO_POISON));
		}
	}

	public final boolean isPoisoned() {
		return poisonDamage.get() > 0;
	}

	public final boolean isVenomed() {
		return venomDamage.get() > 0;
	}

	public final MutableNumber getPoisonDamage() {
		return poisonDamage;
	}

	public MutableNumber getVenomDamage() {
		return venomDamage;
	}

	public PoisonType getPoisonType() {
		return poisonType;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public final Player getPlayer() {
		return (Player) this;
	}

	public int getCurrentHealth() {
		return skills.getLevel(Skill.HITPOINTS);
	}

	public int getMaximumHealth() {
		return skills.getMaxLevel(Skill.HITPOINTS);
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public int getBonus(int index) {
		return bonuses[index];
	}

	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	public void setIndexBonus(int i, int number) {
		this.bonuses[i] = number;
	}

	public void appendBonus(int index, int amount, boolean add, Item item) {

		if (item.isRangedEquipment() && add) {
			if (this.getPlayer().equipment.get(Equipment.ARROWS_SLOT) != null
					&& item.getEquipmentType().getSlot() == Equipment.WEAPON_SLOT
					|| this.getPlayer().equipment.get(Equipment.WEAPON_SLOT) != null
							&& this.getPlayer().equipment.get(Equipment.WEAPON_SLOT).isRangedEquipment()
							&& item.getEquipmentType().getSlot() == Equipment.ARROWS_SLOT)
				return;
		}

		if (bonuses == EMPTY_BONUSES)
			bonuses = new int[EMPTY_BONUSES.length];

		if (amount == 0)
			return;

		if (add)
			bonuses[index] += amount;
		else
			bonuses[index] -= amount < 0 ? 0 : amount;
	}

	public int getListIndex() {
		return listIndex;
	}

	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}

	public Optional<Animation> getAnimation() {
		return animation;
	}

	public Optional<Graphic> getGraphic() {
		return graphic;
	}

	public void resetAnimation() {
		this.animation = Optional.empty();
	}

	public void resetGraphic() {
		this.graphic = Optional.empty();
	}

	/** The method which is invoked every tick. */
	public abstract void sequence();

	/** State of the mob's auto retaliate. */
	public abstract boolean isAutoRetaliate();

	/** Handles the mob death. */
	protected abstract void appendDeath();

	/** The combat strategy of the mob. */
	public abstract <T extends Mob> CombatStrategy<? super T> getStrategy();

	/** The combat of the mob. */
	public abstract Combat<? extends Mob> getCombat();

	/* Calculates if you're within distance i of the position */
	public boolean near(Position calc, int i) {
		return Math.sqrt((Math.pow(calc.getX(), 2) - Math.pow(this.getX(), 2))
				- (Math.pow(calc.getY(), 2) - Math.pow(this.getY(), 2))) < i;
	}

}