package io.server.content.activity.impl.school;

import io.server.Config;
import io.server.content.activity.Activity;
import io.server.content.activity.ActivityType;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Position;

public class SchoolActivity extends Activity {
	private final Player player;
	private static int LEAVE_PLATFORM_ID = 16634;
	private static int PODUM_ID = 16599;
	private Position profPosition = new Position(1761, 5087, 2);

	private void askQuestion() {
		Npc prof = new Npc(554, profPosition, 15);
		prof.owner = player;
		prof.setVisible(false);
		add(prof);

	}

	private void setPosition(GameObject object) {
		boolean walk_state = player.movement.isRunningToggled();
		player.movement.setRunningToggled(true);
		player.locking.lock(LockType.MASTER_WITH_MOVEMENT);

		player.walkExactlyTo(new Position(1766, 5087, 2));
		player.face(object);

		player.movement.setRunningToggled(walk_state);
	}

	private SchoolActivity(Player player) {
		super(30, Mob.DEFAULT_INSTANCE_HEIGHT);
		this.player = player;
	}

	public static SchoolActivity create(Player player) {
		SchoolActivity activity = new SchoolActivity(player);
		activity.add(player);
		activity.resetCooldown();
		player.setVisible(true);
		return activity;
	}

	@Override
	protected void start() {
		// Loop

	}

	@Override
	public void finish() {
		remove(player);
		player.move(Config.DEFAULT_POSITION);
		player.message("Hope you had fun!");
	}

	@Override
	public void cleanup() {

	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		if (event.getObject().getId() == PODUM_ID) {
			setPosition(event.getObject());
			askQuestion();
		}
		if (event.getObject().getId() == LEAVE_PLATFORM_ID) {
			player.dialogueFactory.sendStatement("Are you ready to leave the Brain Game?").sendOption("Yes!", () -> {
				finish();
			}, "No!", () -> {
			}).execute();
			return true;
		}
		return super.clickObject(player, event);
	}

	@Override
	public void onLogout(Player player) {
		finish();
		super.onLogout(player);
	}

	@Override
	public ActivityType getType() {
		return ActivityType.SCHOOL_GAME;
	}
}
