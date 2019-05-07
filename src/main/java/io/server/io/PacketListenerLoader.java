package io.server.io;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.server.net.packet.PacketListener;
import io.server.net.packet.PacketListenerMeta;
import io.server.net.packet.PacketRepository;

/**
 * The class that loads all packet listeners.
 *
 * @author nshusa
 */
public final class PacketListenerLoader implements Runnable {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public void run() {
		new FastClasspathScanner().matchClassesImplementing(PacketListener.class, subclass -> {
			PacketListenerMeta annotation = subclass.getAnnotation(PacketListenerMeta.class);
			try {
				PacketListener listener = subclass.newInstance();
				Arrays.stream(annotation.value()).forEach(it -> PacketRepository.registerListener(it, listener));
			} catch (InstantiationException | IllegalAccessException ex) {
				logger.error("error loading packet listeners.", ex);
			}
		}).scan();
	}

}
