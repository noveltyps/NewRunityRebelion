package io.server.content.activity.annoucements;

import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.util.Utility;

public class Announcement extends Task {

	public Announcement() {
		super(1800);
	}

	@Override
	protected void execute() {
		World.sendBroadcast(1, getData().getContent(), false);
		
	}

	//**nerik did all this 
	private AnnouncementData getData() {
		return AnnouncementData.values()[Utility.random(AnnouncementData.values().length)];
	}

}
