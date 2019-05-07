package io.server.content.activity.impl.zulrah;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.server.content.activity.Activity;
import io.server.content.activity.ActivityListener;
import io.server.content.activity.ActivityType;
import io.server.game.world.World;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.NpcDeath;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.RandomUtils;
import io.server.util.Utility;

public class ZulrahActivity extends Activity {
	
	public final Player player;
	public ZulrahState state;
	boolean attackable;
	private boolean completed;
	public Position target;
	public Npc zulrah;
	public int count;
	List<Npc> snakes = new ArrayList<>();
	List<CustomGameObject> clouds = new ArrayList<>();
	private final ActivityListener<? extends Activity> ZULRAH_LISTENER = new ZulrahListener(this);

	private ZulrahActivity(Player player, int instance) {
		super(1, instance);
		this.count = 0;
		this.player = player;
		this.state = ZulrahState.INITIALIZE;
	}

	public static ZulrahActivity create(Player player) {
		ZulrahActivity activity = new ZulrahActivity(player, player.playerAssistant.instance());
		activity.add(player);
		player.send(new SendMessage("Welcome to Zulrah's shrine."));
		player.dialogueFactory.sendStatement("Welcome to Zulrah's shrine.").execute();
		// player.gameRecord.start();
		activity.start();
		return activity;
	}

	public ZulrahState getNextState() {
		if (zulrah.id == 2043)
			return ZulrahState.ATTACKING;
		Set<ZulrahState> states = new HashSet<>();
		states.add(ZulrahState.ATTACKING);
		if (clouds.size() <= ZulrahConstants.MAXIMUM_CLOUDS)
			states.add(ZulrahState.POISONOUS_CLOUD);
		if (snakes.size() <= ZulrahConstants.MAXIMUM_SNAKELINGS)
			states.add(ZulrahState.SNAKELINGS);
		return Utility.randomElement(states);
	}

	public Position getSnakelingPosition() {
		Set<Position> positions = new HashSet<>(Arrays.asList(new Position(2263, 3075), new Position(2263, 3071),
				new Position(2268, 3069), new Position(2273, 3071), new Position(2273, 3077)));
		snakes.forEach(snake -> {
			if (positions.contains(snake.getPosition())) {
				positions.remove(snake.getPosition());
			}
		});
		return Utility.randomElement(positions);
	}

	public Position getCloudPosition() {
		Set<Position> positions = new HashSet<>(Arrays.asList(ZulrahConstants.CLOUD_POSITIONS));
		clouds.forEach(cloud -> {
			if (positions.contains(cloud.getPosition())) {
				positions.remove(cloud.getPosition());
			}
		});
		return Utility.randomElement(positions);
	}

	@Override
	protected void start() {
		if (!clouds.isEmpty()) {
			for (CustomGameObject cloud : clouds) {
				Position[] boundaries = Utility.getInnerBoundaries(cloud.getPosition().transform(+1, +1), 2, 2);
				for (Position position : boundaries) {
					if (player.getPosition().equals(position))
						player.damage(new Hit(RandomUtils.inclusive(1, 5), Hitsplat.POISON));
				}
			}
		}
		state.execute(this);
	}

	@Override
	public void cleanup() {
		remove(zulrah);
		snakes.forEach(this::remove);
		clouds.forEach(CustomGameObject::unregister);
		clouds.clear();
		snakes.clear();
	}

	@Override
	public void finish() {
		cleanup();
		remove(player);
		if (completed) {
			player.send(new SendMessage("You've killed Zulrah!"));
		}
	}

	@Override
	public void onLogout(Player player) {
		finish();
	}

	@Override
	public void onRegionChange(Player player) {
		if (!Area.inZulrah(player))
			finish();
	}

	@Override
	public void onDeath(Mob mob) {
		if (mob.isNpc() && mob.getNpc().id != 2045) {
			World.schedule(new NpcDeath(mob.getNpc()));
			completed = true;
			finish();
			return;
		}
		if (mob.isNpc() && snakes.contains(mob.getNpc())) {
			World.schedule(new NpcDeath(mob.getNpc(), () -> {
				snakes.remove(mob.getNpc());
				remove(mob);
			}));
			return;
		}
		super.onDeath(mob);
	}

	@Override
	public boolean safe() {
		return true;
	}

	@Override
	protected Optional<? extends ActivityListener<? extends Activity>> getListener() {
		return Optional.of(ZULRAH_LISTENER);
	}

	@Override
	public boolean canTeleport(Player player) {
		return true;
	}

	@Override
	public ActivityType getType() {
		return ActivityType.ZULRAH;
	}
}
