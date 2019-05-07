package io.server.content.activity.randomevent;

import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;

/**
 * The random event handler.
 *
 * @author Daniel.
 */
public abstract class RandomEvent extends Activity {

	/** The player instance. */
	public Player player;

	/** The event npc. */
	protected Npc eventNpc;

	/** Th message count. */
	private int count;

	/** Flag if the event is angered. */
	protected boolean angered;

	/** Constructs a new <code>RandomEvent</code>. */
	public RandomEvent(Player player, int cooldown) {
		super(cooldown, player.instance);
		this.player = player;
		this.count = 0;
	}

	/** The event npc identification. */
	protected abstract int eventNpcIdentification();

	/** The event npc shout messages. */
	protected abstract String[] eventNpcShout();

	@Override
	protected void start() {
		if (count >= eventNpcShout().length) {
			player.damage(new Hit(5));
			finish();
			return;
		}
		if (eventNpc == null) {
			eventNpc = new Npc(eventNpcIdentification(), player.getPosition());
			add(eventNpc);
			eventNpc.interact(player);
			eventNpc.follow(player);
			eventNpc.graphic(new Graphic(86, true));
			eventNpc.owner = player;
			cooldown(2);
			return;
		}
		eventNpc.animate(new Animation(863));
		eventNpc.speak(eventNpcShout()[count].replace("%name", player.getName()));
		eventNpc.follow(player);
		count++;
		if (count >= eventNpcShout().length) {
			angered = true;
			eventNpc.graphic(new Graphic(86, true));
			eventNpc.animate(new Animation(864));
			cooldown(2);
		} else {
			resetCooldown();
		}
	}

	@Override
	public void finish() {
		cleanup();
		remove(player);
	}

	@Override
	public void onRegionChange(Player player) {
		if (eventNpc != null && eventNpc.getPosition().isWithinDistance(player.getPosition(), 15)) {
			finishCooldown();
		}
	}

	@Override
	public void onLogout(Player player) {
		finish();
	}

	@Override
	public void cleanup() {
		remove(eventNpc);
	}

	@Override
	public boolean safe() {
		return false;
	}

	@Override
	public ActivityType getType() {
		return ActivityType.RANDOM_EVENT;
	}
}
