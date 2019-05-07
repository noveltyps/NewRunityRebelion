package io.server.game.plugin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.server.game.event.Event;
import io.server.game.event.impl.ButtonClickEvent;
import io.server.game.event.impl.DropItemEvent;
import io.server.game.event.impl.ItemClickEvent;
import io.server.game.event.impl.ItemContainerContextMenuEvent;
import io.server.game.event.impl.ItemOnItemEvent;
import io.server.game.event.impl.ItemOnNpcEvent;
import io.server.game.event.impl.ItemOnObjectEvent;
import io.server.game.event.impl.ItemOnPlayerEvent;
import io.server.game.event.impl.MovementEvent;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.event.impl.PickupItemEvent;
import io.server.game.event.impl.log.PickupItemLogEvent;
import io.server.game.event.listener.PlayerEventListener;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendMessage;

/**
 * The base class that all plugins should extend. This class allows plugins to
 * have many different behaviors and listen to all types of events.
 *
 * @author nshusa
 */
public class PluginContext implements PlayerEventListener {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * The method that is called after this class is instantiated.
	 */
	public void onInit() {

	}

	@Override
	public boolean accept(Player player, Event event) {
		try {
			if (event instanceof ButtonClickEvent) {
				return handleButtonClickEvent(player, (ButtonClickEvent) event);
			} else if (event instanceof ItemClickEvent) {
				return handleItemClickEvent(player, (ItemClickEvent) event);
			} else if (event instanceof NpcClickEvent) {
				return handleNpcClickEvent(player, (NpcClickEvent) event);
			} else if (event instanceof ObjectClickEvent) {
				return handleObjectClickEvent(player, (ObjectClickEvent) event);
			} else if (event instanceof ItemOnItemEvent) {
				return handleItemOnItemEvent(player, (ItemOnItemEvent) event);
			} else if (event instanceof ItemOnNpcEvent) {
				return handleItemOnNpcEvent(player, (ItemOnNpcEvent) event);
			} else if (event instanceof ItemOnObjectEvent) {
				return handleItemOnObjectEvent(player, (ItemOnObjectEvent) event);
			} else if (event instanceof ItemOnPlayerEvent) {
				return handleItemOnPlayerEvent(player, (ItemOnPlayerEvent) event);
			} else if (event instanceof ItemContainerContextMenuEvent) {
				return handleItemContainerContextMenuEvent(player, (ItemContainerContextMenuEvent) event);
			} else if (event instanceof DropItemEvent) {
				return handleDropItemEvent(player, (DropItemEvent) event);
			} else if (event instanceof PickupItemEvent) {
				return handlePickupItemEvent(player, (PickupItemEvent) event);
			} else if (event instanceof MovementEvent) {
				return onMovement(player, (MovementEvent) event);
			}
		} catch (Exception ex) {
			logger.error(String.format("player=%s error while handling event: %s", player.getName(),
					this.getClass().getSimpleName()), ex);
		}

		// if event is not handled break the chain right away
		return true;
	}

	protected boolean onMovement(Player player, MovementEvent event) {
		return false;
	}

	private boolean handlePickupItemEvent(Player player, PickupItemEvent event) {
		if (onPickupItem(player, event)) {
			if (PlayerRight.isDeveloper(player) && player.debug) {
				player.send(new SendMessage(String.format("[%s]: item=%d position=%s", this.getClass().getSimpleName(),
						event.getItem().getId(), event.getPosition().toString())));
			}
			World.getDataBus().publish(new PickupItemLogEvent(player, event.getGroundItem()));
			return true;
		}
		return false;
	}

	protected boolean onPickupItem(Player player, PickupItemEvent event) {
		return false;
	}

	private boolean handleDropItemEvent(Player player, DropItemEvent event) {
		if (onDropItem(player, event)) {
			if (PlayerRight.isDeveloper(player) && player.debug) {
				player.send(new SendMessage(String.format("[%s]: item=%d amount=%d slot=%d position=%s",
						this.getClass().getSimpleName(), event.getItem().getId(), event.getItem().getAmount(),
						event.getSlot(), event.getPosition())));
			}
			return true;
		}
		return false;
	}

	protected boolean onDropItem(Player player, DropItemEvent event) {
		return false;
	}

	private boolean handleItemContainerContextMenuEvent(Player player, ItemContainerContextMenuEvent event) {
		switch (event.getType()) {
		case 1:
			if (firstClickItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;

		case 2:
			if (secondClickItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;

		case 3:
			if (thirdClickItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;

		case 4:
			if (fourthClickItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;

		case 5:
			if (fifthClickItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;

		case 6:
			if (sixthClickItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;

		case 7:
			if (allButOneItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;

		case 8:
			if (modifiableXItemContainer(player, event)) {
				if (PlayerRight.isDeveloper(player) && player.debug) {
					player.send(
							new SendMessage(String.format("[%s, type=%d]: interfaceId=%d (removeId=%d, removeSlot=%d)",
									this.getClass().getSimpleName(), event.getType(), event.getInterfaceId(),
									event.getRemoveId(), event.getRemoveSlot())));
				}
				return true;
			}
			break;
		}
		return false;
	}

	protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	protected boolean secondClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	protected boolean thirdClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	protected boolean fourthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	protected boolean fifthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	protected boolean sixthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	protected boolean allButOneItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	protected boolean modifiableXItemContainer(Player player, ItemContainerContextMenuEvent event) {
		return false;
	}

	private boolean handleItemOnPlayerEvent(Player player, ItemOnPlayerEvent event) {
		if (itemOnPlayer(player, event)) {
			if (player.debug) {
				player.send(new SendMessage(
						String.format("[%s]: item=%d slot=%d player=%s", this.getClass().getSimpleName(),
								event.getUsed().getId(), event.getSlot(), event.getOther().getName())));
			}
			return true;
		}
		return false;
	}

	protected boolean itemOnPlayer(Player player, ItemOnPlayerEvent event) {
		return false;
	}

	private boolean handleItemOnObjectEvent(Player player, ItemOnObjectEvent event) {
		if (itemOnObject(player, event)) {
			if (player.debug) {
				player.send(new SendMessage(
						String.format("[%s]: item=%d slot=%d object=%d", this.getClass().getSimpleName(),
								event.getUsed().getId(), event.getSlot(), event.getObject().getId())));
			}
			return true;
		}
		return false;
	}

	protected boolean itemOnObject(Player player, ItemOnObjectEvent event) {
		return false;
	}

	private boolean handleItemOnNpcEvent(Player player, ItemOnNpcEvent event) {
		if (itemOnNpc(player, event)) {
			if (player.debug) {
				player.send(new SendMessage(String.format("[%s]: item=%d slot=%d npc=%d",
						this.getClass().getSimpleName(), event.getUsed().getId(), event.getSlot(), event.getNpc().id)));
			}
			return true;
		}
		return false;
	}

	protected boolean itemOnNpc(Player player, ItemOnNpcEvent event) {
		return false;
	}

	private boolean handleItemOnItemEvent(Player player, ItemOnItemEvent event) {
		if (itemOnItem(player, event)) {
			if (player.debug) {
				player.send(new SendMessage(String.format("[%s]: used=%d usedSlot=%d with=%d withSlot=%d",
						this.getClass().getSimpleName(), event.getUsed().getId(), event.getUsedSlot(),
						event.getWith().getId(), event.getWithSlot())));
			}
			return true;
		}
		return false;
	}

	protected boolean itemOnItem(Player player, ItemOnItemEvent event) {
		return false;
	}

	private boolean handleObjectClickEvent(Player player, ObjectClickEvent event) {
		switch (event.getType()) {
		case 1:
			if (firstClickObject(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: object=%d",
							this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
				}
				return true;
			}
			break;

		case 2:
			if (secondClickObject(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: object=%d",
							this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
				}
				return true;
			}
			break;

		case 3:
			if (thirdClickObject(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: object=%d",
							this.getClass().getSimpleName(), event.getType(), event.getObject().getId())));
				}
				return true;
			}
			break;
		}
		return false;
	}

	protected boolean firstClickObject(Player player, ObjectClickEvent event) {
		return false;
	}

	protected boolean secondClickObject(Player player, ObjectClickEvent event) {
		return false;
	}

	protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
		return false;
	}

	private boolean handleNpcClickEvent(Player player, NpcClickEvent event) {
		switch (event.getType()) {
		case 1:
			if (firstClickNpc(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(),
							event.getType(), event.getNpc().id)));
				}
				return true;
			}
			break;

		case 2:
			if (secondClickNpc(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(),
							event.getType(), event.getNpc().id)));
				}
				return true;
			}
			break;

		case 3:
			if (thirdClickNpc(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(),
							event.getType(), event.getNpc().id)));
				}
				return true;
			}
			break;

		case 4:
			if (fourthClickNpc(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: npc=%d", this.getClass().getSimpleName(),
							event.getType(), event.getNpc().id)));
				}
				return true;
			}
			break;
		}

		return false;
	}

	protected boolean firstClickNpc(Player player, NpcClickEvent event) {
		return false;
	}

	protected boolean secondClickNpc(Player player, NpcClickEvent event) {
		return false;
	}

	protected boolean thirdClickNpc(Player player, NpcClickEvent event) {
		return false;
	}

	protected boolean fourthClickNpc(Player player, NpcClickEvent event) {
		return false;
	}

	private boolean handleItemClickEvent(Player player, ItemClickEvent event) {
		switch (event.getType()) {
		case 1:
			if (firstClickItem(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: item=%d", this.getClass().getSimpleName(),
							event.getType(), event.getItem().getId())));
				}
				return true;
			}
			break;

		case 2:
			if (secondClickItem(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: item=%d", this.getClass().getSimpleName(),
							event.getType(), event.getItem().getId())));
				}
				return true;
			}
			break;

		case 3:
			if (thirdClickItem(player, event)) {
				if (player.debug) {
					player.send(new SendMessage(String.format("[%s, type=%d]: item=%d", this.getClass().getSimpleName(),
							event.getType(), event.getItem().getId())));
				}
				return true;
			}
			break;
		}
		return false;
	}

	protected boolean firstClickItem(Player player, ItemClickEvent event) {
		return false;
	}

	protected boolean secondClickItem(Player player, ItemClickEvent event) {
		return false;
	}

	protected boolean thirdClickItem(Player player, ItemClickEvent event) {
		return false;
	}

	private boolean handleButtonClickEvent(Player player, ButtonClickEvent event) {
		if (onClick(player, event.getButton())) {
			if (PlayerRight.isDeveloper(player) && player.debug) {
				player.send(new SendMessage(
						String.format("[%s]: button=%d", this.getClass().getSimpleName(), event.getButton())));
			}
			return true;
		}
		return false;
	}

	protected boolean onClick(final Player player, final int button) {
		return false;
	}

}
