package io.server.fs.cache.decoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.server.fs.cache.FileSystem;
import io.server.game.world.object.ObjectDefinition;

public final class ObjectDefinitionDecoder implements Runnable {

	private final static Logger LOGGER = LogManager.getLogger();

	private final FileSystem fs;

	public ObjectDefinitionDecoder(FileSystem fs) {
		this.fs = fs;
	}

	@Override
	public void run() {
		LOGGER.info("Loading object definitions.");

		ObjectDefinition.init(fs.getArchive(FileSystem.CONFIG_ARCHIVE));
	}

}
