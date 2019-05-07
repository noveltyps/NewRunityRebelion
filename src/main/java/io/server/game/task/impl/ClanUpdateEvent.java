package io.server.game.task.impl;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

import io.server.content.clanchannel.ClanRepository;
import io.server.content.clanchannel.channel.ClanChannel;
import io.server.game.task.TickableTask;
import io.server.util.GsonUtils;

/**
 * An randomevent which handles updating clan chats.
 *
 * @author Daniel | Obey
 */
public class ClanUpdateEvent extends TickableTask {

	public ClanUpdateEvent() {
		super(false, 60);
	}

	@Override
	protected void tick() {
		if (ClanRepository.ACTIVE_CHANNELS.isEmpty()) {
			return;
		}

		for (ClanChannel channel : ClanRepository.ACTIVE_CHANNELS) {
			if (channel.activeSize() <= 0) 
				continue;

			channel.forEach(clanMember -> channel.getHandler().updateMemberList(clanMember));

			new Thread(() -> {
				final File dir = Paths.get("data", "content", "clan").toFile();

				if (!dir.exists()) {
					dir.mkdirs();
				}

				try (FileWriter fw = new FileWriter(dir.toPath().resolve(channel.getOwner() + ".json").toFile())) {
					fw.write(GsonUtils.JSON_PRETTY_NO_NULLS.toJson(channel.toJson()));
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}).start();

		}
	}

}
