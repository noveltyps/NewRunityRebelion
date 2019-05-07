package io.server.content.activity.impl.bosscontrol;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.server.content.activity.ActivityType;
import io.server.content.activity.GroupActivity;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.game.world.entity.mob.Direction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.pathfinding.TraversalMap;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * A {@code BossControl} object is an {@link GroupActivity} that handles the
 * pest control minigame.
 *
 * @author Michael | Chex
 */
public class BossControl extends GroupActivity {

	/** The lobby cooldown timer in ticks. */
	protected static final int PLAYER_CAPACITY = 25;

	/** The lobby cooldown timer in ticks. */
	private static final int LOBBY_COOLDOWN = 30; // 15 seconds for testing

	/** The game cooldown timer in ticks. */
	private static final int GAME_COOLDOWN = 250; // 2 minutes for testing

	/** The portal names. */
	protected static final String[] PORTAL_NAMES = { "Boss 1", "Boss 2", "Boss 3", "Boss 4" };

	/** The position of the pest control boat. */
	protected static final Position BOAT = new Position(2656, 2609);

	/** The position of the pest control boat. */
	protected static final Position END_POSITION = new Position(2657, 2639);

	/** A list of all the active pest control minigames. */
	private static final List<BossControl> ACTIVE = new LinkedList<>();

	/** The Pest control activity listener. */
	private final BossControlListener listener = new BossControlListener(this);

	/** The void knight. */
	protected final Npc voidKnight = new Npc(1756, Position.create(2656, 2592));

	/** The portals. */
	protected final Portal[] portals = new Portal[] { /* Red */ new Portal(1742, Position.create(2645, 2569)),
			/* Blue */ new Portal(1740, Position.create(2680, 2588)),
			/* Purple */ new Portal(1739, Position.create(2628, 2591)),
			/* Yellow */ new Portal(1741, Position.create(2669, 2570)) };

	private static final int[] RAVAGERS = { 1704, 1705, 1706, 1707, 1708 };
	public static final int[] SPLATTERS = { 1691, 1692 };
	public static final int[] SHIFTERS = { 1698, 1699, 1700, 1701 };
	public static final int[] DEFILERS = { 1728, 1729 };
	public static final int[] TORCHERS = { 1728, 1729 };

	/** The active monsters. */
	protected Set<Npc> monsters = new HashSet<>();

	protected Set<Npc> portalSet = new HashSet<>();

	/** The lobby state. */
	protected boolean lobby;

	/** The void knight messages that he will chant. */
	private static final String[] VOID_KNIGHT_MESSAGES = { "We must not fail!", "Take down the portals",
			"The Void Knights will not fall!", "Hail the Void Knights!", "We are beating these scum!" };

	/** Constructs a new {@link BossControl} minigame object. */
	private BossControl() {
		super(1, PLAYER_CAPACITY);
		cooldown(LOBBY_COOLDOWN);
		lobby = true;
	}

	/** Adds a player to the pest control boat. */
	public static void enter(Player player) {
		BossControl BossControl = getLobby();
		BossControl.addActivity(player, BossControl.createNode(player));
		player.move(new Position(2661, 2639));
		player.send(new SendMessage("You have entered the pest control boat."));
	}

	/** Sequences all the active pest control minigames. */
	public static void sequenceMinigame() {
		Iterator<BossControl> iterator = ACTIVE.iterator();

		while (iterator.hasNext()) {
			BossControl BossControl = iterator.next();

			if (!BossControl.lobby) {
				if (BossControl.getTicks() == START || BossControl.getActiveSize() == 0
						|| BossControl.portalSet.isEmpty()) {
					BossControl.finish();
					BossControl.removeAll();
					BossControl.cleanup();
					iterator.remove();
					continue;
				}

				if (BossControl.getTicks() % 10 == 0 && BossControl.voidKnight != null) {
					BossControl.voidKnight.speak(Utility.randomElement(VOID_KNIGHT_MESSAGES));
				}

				if (BossControl.getTicks() % 5 == 0) {
					BossControl.agressiveMonsters();
				}
			}

			BossControl.sequence();
		}
	}

	@Override
	public void sequence() {
		super.sequence();
		forEachActivity((mob, activity) -> activity.getPanel()
				.ifPresent(panel -> ((BossControlPanel) panel).update((BossControlNode) activity)));
	}

	@Override
	protected void start() {
		if (!lobby) {
			return;
		}

		if (getActiveSize() < 2) {
			groupMessage("There needs to be at least 2 player to start a game.");
			cooldown(LOBBY_COOLDOWN);
			return;
		}

		add(voidKnight);
		voidKnight.blockFace = true;
		for (Npc portal : portals) {
			add(portal);
			portalSet.add(portal);
		}

		super.start();
		groupMessage("Protect the void knight at all costs, good luck!");
		spawnMonsters();
		cooldown(GAME_COOLDOWN);
		lobby = false;
	}

	private final Position[] blue_bounds = Utility.getInnerBoundaries(new Position(2678, 2589), 8, 8);
	private final Position[] red_bounds = Utility.getInnerBoundaries(new Position(2646, 2573), 8, 8);
	private final Position[] yellow_bounds = Utility.getInnerBoundaries(new Position(2670, 2574), 8, 8);
	private final Position[] purple_bounds = Utility.getInnerBoundaries(new Position(2631, 2592), 8, 8);
	private final Position[] knight_bounds = Utility.getInnerBoundaries(new Position(2657, 2593), 8, 8);

	private void spawnMonsters() {
		for (int index = 0; index < 5; index++) {
			spawn(Utility.randomElement(RAVAGERS), blue_bounds);
		}
		for (int index = 0; index < 5; index++) {
			spawn(Utility.randomElement(RAVAGERS), red_bounds);
		}
		for (int index = 0; index < 5; index++) {
			spawn(Utility.randomElement(RAVAGERS), yellow_bounds);
		}
		for (int index = 0; index < 5; index++) {
			spawn(Utility.randomElement(RAVAGERS), purple_bounds);
		}
		for (int index = 0; index < 5; index++) {
			spawn(Utility.randomElement(RAVAGERS), knight_bounds);
		}
	}

	private void spawn(int id, Position[] boundaries) {
		Position target = Utility.randomElement(boundaries);

		while (!TraversalMap.isTraversable(new Position(target.getX(), target.getY(), 0), Direction.NORTH, false)) {
			target = Utility.randomElement(boundaries);
		}

		Npc monster = new Npc(id, new Position(target.getX(), target.getY(), 0));
		monsters.add(monster);
		add(monster);
	}

	private void agressiveMonsters() {
		if (monsters.isEmpty())
			return;
		if (voidKnight == null)
			return;
		for (Npc monster : monsters) {
			if (monster.getCombat().inCombat())
				continue;
			if (monster.getPosition().isWithinDistance(voidKnight.getPosition(), 10)) {
				monster.getCombat().attack(voidKnight);
				continue;
			}
			activities.keySet().forEach(mob -> {
				if (!mob.isPlayer())
					return;
				if (monster.getPosition().isWithinDistance(mob.getPosition(), 8))
					monster.getCombat().attack(mob);
			});
		}
	}

	@Override
	public void cleanup() {
		super.cleanup();
		remove(voidKnight);
		for (Npc monster : monsters)
			remove(monster);
		for (Npc portal : portals)
			remove(portal);
		monsters.clear();
	}

	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	@Override
	public void onLogout(Player player) {
		removeActivity(player);
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inBossControl(player)) {
			removeActivity(player);
		}
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		if (event.getObject().getId() == 14314) {
			removeActivity(player);
			// player.send(new SendMessage("You are trapped in my MINIGAME HAHAH! You can
			// only leave once the minigame is completed."));
			return true;
		}
		return false;
	}

	@Override
	public boolean safe() {
		return true;
	}

	@Override
	public void onDeath(Mob mob) {
		if (mob.isNpc()) {
			Npc npc = mob.getNpc();

			if (npc.id >= 1739 && npc.id <= 1742) {
				portalSet.remove(npc);
			} else {
				monsters.remove(mob.getNpc());
			}

			remove(mob);
		} else {
			mob.move(BOAT.transform(Utility.random(4), Utility.random(6)));
		}
	}

	/** Gets the next pest control lobby. */
	private static BossControl getLobby() {
		for (BossControl next : ACTIVE)
			if (next.inLobby())
				return next;
		BossControl next = new BossControl();
		ACTIVE.add(next);
		return next;
	}

	/** Creates a new {@link BossControlNode} for a player. */
	private BossControlNode createNode(Player player) {
		BossControlNode node = new BossControlNode(this, player);
		BossControlPanel panel = new BossControlPanel(this, player);
		panel.open();
		node.setPanel(panel);
		return node;
	}

	/** Checks if this pest control minigame is in the lobby. */
	private boolean inLobby() {
		return lobby;
	}

	@Override
	protected Optional<BossControlListener> getListener() {
		return Optional.of(listener);
	}

	@Override
	public ActivityType getType() {
		return ActivityType.PEST_CONTROL;
	}

}