package io.server.game.plugin;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.server.game.event.bus.PlayerDataBus;
import io.server.util.Logger;

/**
 * This class handles how plugins are loaded/unloaded and accessed.
 *
 * @author nshusa
 */
public final class PluginManager {

	/**
	 * The set of plugin classpaths, these could be useful for hotswapping. Right
	 * now these are only used to tell how many plugins there are.
	 */
	private static final Set<String> plugins = new HashSet<>();

	/**
	 * The data bus used to sent events to plugins.
	 */
	private static final PlayerDataBus dataBus = PlayerDataBus.getInstance();

	/**
	 * There should only ever be 1 instance of plugin manager so don't allow it be
	 * created.
	 */
	private PluginManager() {

	}

	/**
	 * Scans the classpath for classes with a specific annotation, in this case the
	 * marker annotation "Marker". If the annotation is found the plugin gets
	 * created.
	 */
	public static void load() {
		new FastClasspathScanner().matchSubclassesOf(PluginContext.class, clazz -> {
			try {
				if (!Modifier.isAbstract(clazz.getModifiers())) {
					PluginContext listener = clazz.getConstructor().newInstance();
					listener.onInit();
					dataBus.subscribe(listener);
					plugins.add(clazz.getName());
				}
			} catch (Exception ex) {
				Logger.error(String.format("Error loading plugin=%s", clazz.getSimpleName()));
				Logger.parent(ex.getMessage());
			}
		}).scan();
		Logger.log(String.format("Loaded: %d plugins.", PluginManager.getPluginCount()));
	}

	/**
	 * Gets the amount of individual plugins that were loaded on startup.
	 */
	public static int getPluginCount() {
		return plugins.size();
	}

	/**
	 * Gets the bus used to sent events.
	 */
	public static PlayerDataBus getDataBus() {
		return dataBus;
	}

}
