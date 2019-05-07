package io.server.content.skill.impl;

import java.util.Optional;

import io.server.content.skill.SkillAction;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.ItemContainer;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.StringUtils;

/**
 * The skill action that represents an action where one item in an inventory is
 * replaced with a new one. This type of skill action is somewhat basic and
 * requires that a player have the item to be removed.
 * <p>
 * <p>
 * The skills that may use this type skill action include, but are not limited
 * to {@code COOKING}.
 * 
 * @author lare96 <http://github.com/lare96>
 * @see SkillAction
 * @see DestructionSkillAction
 * @see HarvestingSkillAction
 */
public abstract class ProducingSkillAction extends SkillAction {

	/**
	 * Creates a new {@link ProducingSkillAction}.
	 * 
	 * @param player   the player this skill action is for.
	 * @param position the position the player should face.
	 * @param instant  determines if this action should be ran instantly.
	 */
	public ProducingSkillAction(Player player, Optional<Position> position, boolean instant) {
		super(player, position, instant);
	}

	/**
	 * Creates a new {@link ProducingSkillAction}.
	 * 
	 * @param player   the player this skill action is for.
	 * @param position the position the player should face.
	 * @param delay    the delay between these producing skill actions.
	 * @param instant  determines if this action should be ran instantly.
	 */
	public ProducingSkillAction(Player player, Optional<Position> position, int delay, boolean instant) {
		super(player, position, delay, instant);
	}

	@Override
	public final boolean canRun() {
		Optional<Item[]> removeItem = removeItem();

		if (!removeItem.isPresent())
			return true;

		ItemContainer container = getMob().getPlayer().inventory.copy();
		if (container.removeAll(removeItem.get()) && !container.hasCapacityFor(produceItem().get())) {
			container.fireCapacityExceededEvent();
			return false;
		}
		if (getMob().getPlayer().inventory.containsAll(removeItem.get()))
			return true;

		if (!message().isPresent()) {
			for (Item item : removeItem.get()) {
				if (!getMob().getPlayer().inventory.contains(item)) {
					String anyOrEnough = item.getAmount() == 1 ? "any" : "enough";
					getMob().getPlayer().send(new SendMessage("You don't have " + anyOrEnough + " "
							+ StringUtils.appendPluralCheck(item.getName()) + " left."));
					return false;
				}
			}
		} else {
			getMob().getPlayer().send(new SendMessage(message().get()));
		}

		onProduce(false);
		return false;
	}

	@Override
	public final void onExecute() {
		getMob().skills.addExperience(skill(), experience());
		removeItem().ifPresent(getMob().getPlayer().inventory::removeAll);
		produceItem().ifPresent(getMob().getPlayer().inventory::addAll);
		onProduce(true);
	}

	/**
	 * The method executed upon production of an item.
	 * 
	 * @param success determines if the production was successful or not.
	 */
	public void onProduce(boolean success) {

	}

	/**
	 * The item that will be removed upon production.
	 * 
	 * @return the item that will be removed.
	 */
	public abstract Optional<Item[]> removeItem();

	/**
	 * The item that will be added upon production.
	 * 
	 * @return the item that will be added.
	 */
	public abstract Optional<Item[]> produceItem();

	/**
	 * The message that will be sent when the player doesn't have the items
	 * required.
	 * 
	 * @return the alphabetic value which represents the message.
	 */
	public Optional<String> message() {
		return Optional.empty();
	}

}
