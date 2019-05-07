package io.server.content.logger.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import io.server.content.logger.LoggerListener;
import io.server.game.world.entity.mob.player.Player;

public class TradingLogger implements LoggerListener {

	private Date date = new Date();
	
	@Override
	public String getPath() {
		return "./data/content/logs/trading";
	}

	@Override
	public void execute(Player player, Player other, String log) {
		Path path = Paths.get(getPath(), player.getUsername() + ".txt");
		File file = path.toFile();
		file.getParentFile().setWritable(true);
		
		if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException e) {
                System.out.println("Unable to create directory for player data!");
            }
        }
		
		try (PrintWriter object = new PrintWriter(new FileOutputStream(file, true))) {
			
			object.append("["+date+"] - " + log);
			object.println();
			object.flush();
			object.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
