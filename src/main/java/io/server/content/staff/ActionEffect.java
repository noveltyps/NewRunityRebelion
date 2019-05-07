package io.server.content.staff;

/**
 * The action execute of the staff panel.
 * 
 * @author Daniel
 *
 * @param <T>
 */
public interface ActionEffect<T> {

	/**
	 * Handles the execute of the staff panel action.
	 * 
	 * @param player The player causing the action.
	 * @param other  The other player enduring the action execute.
	 */
	void handle(final T player, final T other);
}
