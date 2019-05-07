package io.server.fs.cache.decoder;

import java.nio.ByteBuffer;

import io.server.fs.cache.FileSystem;
import io.server.fs.cache.archive.Archive;
import io.server.game.world.region.RegionDefinition;
import io.server.util.Logger;

/**
 * A class which parses {@link RegionDefinition}s
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class MapDefinitionDecoder implements Runnable {

	/** The IndexedFileSystem. */
	private final FileSystem fs;

	/**
	 * Creates the {@link MapDefinitionDecoder}.
	 *
	 * @param fs The {@link FileSystem}.
	 */
	public MapDefinitionDecoder(FileSystem fs) {
		this.fs = fs;
	}

	@Override
	public void run() {
		Logger.log("Loading region definitions.");
		Archive archive = fs.getArchive(FileSystem.MANIFEST_ARCHIVE);
		ByteBuffer buffer = archive.getData("map_index");
		int count = buffer.getShort() & 0xFFFF;
		for (int i = 0; i < count; i++) {
			int hash = buffer.getShort() & 0xFFFF;
			int terrainFile = buffer.getShort() & 0xFFFF;
			int objectFile = buffer.getShort() & 0xFFFF;
			RegionDefinition.set(new RegionDefinition(hash, terrainFile, objectFile));
		}
		Logger.parent("Loaded " + count + " region definitions.");
	}

}