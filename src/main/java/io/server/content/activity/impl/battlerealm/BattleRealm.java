package io.server.content.activity.impl.battlerealm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.server.content.activity.ActivityType;
import io.server.content.activity.GroupActivity;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.game.Animation;
import io.server.game.UpdatePriority;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendMessage;

public class BattleRealm extends GroupActivity {
	private static final int PLAYER_CAPACITY = 45;
	public boolean lobby;
	protected static final int LOBBY_COOLDOWN = 35;

	private ArrayList<CustomGameObject> gameObjects = new ArrayList<>();
	private ArrayList<Npc> monsters = new ArrayList<>();
	int realmiteLives;
	int morphicLives;

	BattleRealmSetup setup = new BattleRealmSetup();

	/** Constructs a new {@link BattleRealm} minigame object. */
	private BattleRealm() {
		super(1, PLAYER_CAPACITY);
		cooldown(LOBBY_COOLDOWN);
		setup.addLobbyObjects(this.getInstance());
		setup.spawnEveryObject(this.getInstance(), gameObjects);
		lobby = true;
	}

	/** Adds a player to the BattleRealm Lobby. */
	public static void enter(Player player) {
		BattleRealm battleRealm = getLobby();
		if (battleRealm.inLobby()) {
			battleRealm.addActivity(player, battleRealm.createNode(player));
			player.move(Constants.LOBBY);
			player.send(new SendMessage("You have entered the BattleRealm lobby."));
		} else {
			player.message("A BattleRealm game is already in progress. You will be added to the next one!");
			battleRealm.addActivity(player, battleRealm.createNode(player));
			player.move(Constants.LOBBY);
			player.send(new SendMessage("You have entered the BattleRealm lobby."));
		}
	}

	@Override
	public ActivityType getType() {
		return ActivityType.BATTLE_REALM;
	}

	@Override
	protected void start() {
		if (!lobby) {
			return;
		}

		if (getActiveSize() < 1) {
			groupMessage("There needs to be at least 1 player to start a game.");
			cooldown(LOBBY_COOLDOWN);
			return;
		}

		super.start();
		groupMessage("The Game Has Begun!");
		morphicLives = 15;
		realmiteLives = 15;
		setup.spawnMonsters(monsters);
		cooldown(6000); // 1 hour in ticks
		lobby = false;
	}

	/** Creates a new {@link BattleRealmNode} for a player. */
	private BattleRealmNode createNode(Player player) {
		BattleRealmNode node = new BattleRealmNode(this, player);
		BattleRealmPanel panel = new BattleRealmPanel(this, player);
		panel.open();
		node.setPanel(panel);
		return node;
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		handleDoors(event.getObject(), player);
		BattleRealmNode node = player.battleRealmNode;

		switch (event.getObject().getId()) {
		case 3680:
			player.message("You are now on Realmite's team!");
			node.team = 2;
			break;

		case 869:
			player.message("You are now on Morphic's  team!");
			node.team = 1;
			break;

		default:
			System.out.println(player.getName() + " clicked on " + event.getObject().getDefinition().getName() + "(id="
					+ event.getObject().getId() + ")");
		}
		return false;
	}

	private void handleDoors(GameObject ob, Player player) {
		switch (ob.getId()) {
		case 1517:
		case 4247:
		case 4696:

			player.animate(new Animation(2068, UpdatePriority.NORMAL));
			World.schedule(new Task(2) {
				@Override
				protected void execute() {
					System.out.println("Unregistering door at instanceHeight" + ob.getInstancedHeight());
					ob.unregister();
					cancel();
				}
			});
		}
	}

	/*
	 * Safety checks
	 * 
	 */

	@Override
	public boolean canTeleport(Player player) {
		System.out.println(player.getName() + " has been removed from the activity (canteleport)");
		player.message("You have been removed from the Battle Realm for teleporting.");
		removeActivity(player);
		return false;
	}

	@Override
	public void onLogout(Player player) {
		System.out.println(player.getName() + " has been removed from the activity (onlogout)");
		removeActivity(player);
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inBattleRealm(player)) {
			System.out.println(player.getName() + " has been removed from the activity (onregionchange)");
			removeActivity(player);
		}
	}

	@Override
	public boolean safe() {
		return true;
	}

	@Override
	public void onDeath(Mob mob) {
		if (mob.isPlayer()) {
			groupMessage(mob.getName() + " has died!");

			BattleRealmNode node = mob.getPlayer().battleRealmNode;
			if (node.getTeamLives() > 0) {
				mob.getPlayer().battleRealmNode.moveToSpawn();
				node.decrementTeamLives();
			} else {
				endGame(this);
			}
		}
	}

	@Override
	public void cleanup() {
		// Cleanup for the player..
	}

	/*
	 * For each Instance Stuff:
	 */

	/** A list of all the active battlerealm minigames. */
	private static final List<BattleRealm> ACTIVE = new LinkedList<>();

	/** Gets the next battlerealm control lobby. */
	private static BattleRealm getLobby() {
		for (BattleRealm next : ACTIVE)
			return next;

		BattleRealm next = new BattleRealm();
		System.out.println("CREATED A NEW BATTLEREALM LOBBY");
		ACTIVE.add(next);
		return next;
	}

	private boolean inLobby() {
		return lobby;
	}

	/** Sequences all the active pest control minigames. */
	public static void sequenceMinigame() {
		Iterator<BattleRealm> iterator = ACTIVE.iterator();

		while (iterator.hasNext()) {
			BattleRealm battleRealm = iterator.next();

			if (!battleRealm.lobby) {
				if (battleRealm.getActiveSize() == 0) {
					System.out.println("Game ending because no one's in it");
					endGame(battleRealm);
					iterator.remove();
					continue;
				}

				/*
				 * if (pestControl.getTicks() % 5 == 0) { pestControl.agressiveMonsters(); }
				 */
			}

			battleRealm.sequence();
		}
	}

	private static void endGame(BattleRealm battleRealm) {
		System.out.println("ENDING GAME -- STACK TRACE");
		Thread.dumpStack();

		battleRealm.removeAll();

		battleRealm.finish();
		battleRealm.removeAll();
		battleRealm.cleanup();
	}

	@Override
	public void finish() {
		// Despawn every object
		for (CustomGameObject n : gameObjects) {
			System.out.println("Unregistering " + n.getName() + " at instance " + n.instance);
			n.unregister();
		}

		super.finish();
	}

	@Override
	public void sequence() {
		super.sequence();
		forEachActivity((mob, activity) -> activity.getPanel()
				.ifPresent(panel -> ((BattleRealmPanel) panel).update((BattleRealmNode) activity)));
	}
}
