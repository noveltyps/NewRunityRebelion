package io.server.game.event.bus;

import java.util.HashSet;
import java.util.Set;

import io.server.game.event.Event;
import io.server.game.event.listener.PlayerEventListener;
import io.server.game.world.entity.mob.player.Player;

/**
 * <h2>A modified DataBus implementation specifically for player-related
 * events.</h2>
 *
 * <p>
 * The difference between a DataBus and publish/subscribe model is that. A
 * DataBus is a form of publish/subscribe, the only difference is that the bus
 * decouples the publisher and subscriber.
 *
 * This pattern is different from the observer pattern in the sense that the
 * observer pattern only allows for a one-to-many relationship wheres the
 * DataBus allows for a many-to-many relationship.
 *
 * {@link PlayerEventListener}'s must subscribe to the bus in order to recieve
 * {@link Event}'s send by the bus. {@link PlayerEventListener}'s can then
 * decide which {@link Event}'s they are interested in.
 *
 * The difference between this implementation and a regular DataBus
 * implementation is that {@link Player} is specified when publishing events.
 * Also this implementation knows if an {@link Event} was successfully handled
 * by a {@link PlayerEventListener}. If the {@link Event} was handled then the
 * {@link PlayerEventListener} chain break and the event does not propagate any
 * further across the chain.
 * </p>
 *
 * @see <a href="http://wiki.c2.com/?DataBusPattern">DataBusPattern</a>
 *
 * @author nshusa
 */
public final class PlayerDataBus {

	/**
	 * The single instance for this bus.
	 */
	private static final PlayerDataBus INSTANCE = new PlayerDataBus();

	/**
	 * Prevent instantiation
	 */
	private PlayerDataBus() {

	}

	/**
	 * Gets the singleton.
	 */
	public static PlayerDataBus getInstance() {
		return INSTANCE;
	}

	/**
	 * The set of listeners that are subscribed to this bus.
	 */
	private static final Set<PlayerEventListener> chain = new HashSet<>();

	/**
	 * Subscribes a listener to this bus. If a listener subscribes to this bus then
	 * that listener can receive events sent by the bus.
	 *
	 * @param listener The listener that is subscribing to this bus.
	 */
	public void subscribe(PlayerEventListener listener) {
		chain.add(listener);
	}

	/**
	 * Unsubscribes a listener from this bus. If a listener is unsubscribed that
	 * listener no longer receives messages sent by the bus.
	 *
	 * @param listener The listener that is unsubscribing.
	 */
	public void unsubscribe(PlayerEventListener listener) {
		chain.remove(listener);
	}

	/**
	 * Sends an {@link Event} to all subscribed event listeners. If a listener is
	 * able to handle the message the chain breaks. This is to prevent further
	 * propagating of the event.
	 *
	 * @param player The player that is sending the event.
	 *
	 * @param event  The event that is going to be sent to all listeners.
	 *
	 * @return {@code true} If an event was handled. Otherwise {@code false}.
	 */
	public boolean publish(Player player, Event event) {
		for (PlayerEventListener listener : chain) {
			if (listener.accept(player, event)) {
				return true;
			}
		}
		return false;
	}

}
