package io.server.game.world.entity.mob.player;

import io.server.Config;
import io.server.content.store.Store;
import io.server.game.world.entity.mob.player.exchange.ExchangeSessionType;
import io.server.net.packet.out.SendInterface;
import io.server.net.packet.out.SendInventoryInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendRemoveInterface;
import io.server.net.packet.out.SendSideBarInterface;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendWalkableInterface;

/**
 * Contains information about the state of interfaces enter in the client.
 *
 * @author Daniel
 */
public class InterfaceManager {

	/** The player instance. */
	private Player player;

	/** The current main interface. */
	private int main = -1;

	/** The current overlay interface. */
	private int overlay = -1;

	/** The current walkable-interface. */
	private int walkable = -1;

	/** The current dialogue. */
	private int dialogue = -1;

	private int[] sidebars = new int[15];
	
	public enum Type {
		
		PLAYER_OWNED_STORE_MENU(38200)
		
		
		
		;
		
		public int interfaceId;
		
		Type(int interfaceId) {
			this.interfaceId = interfaceId;
		}
		
	}

	/** Creates a new <code>InterfaceManager<code>. */
	InterfaceManager(Player player) {
		this.player = player;
	}

	/** Opens an interface for the player. */
	public void open(int identification) {
		open(identification, true);
	}
	
	public void open(Type identification) {
		open(identification.interfaceId, true);
	}
	


	/** Opens an interface for the player. */
	public void openNonDuplicate(int identification) {
		if (!isInterfaceOpen(identification)) {
			open(identification, true);
		}
	}
	
	


	/** Opens an itemcontainer for the player. */
	public void open(int identification, boolean secure) {
		if (secure) {

			if (player.isBot || main == identification) {
				return;
			}

			if (player.getCombat().inCombat()) {
				player.send(new SendMessage("You can't do this right now!"));
				return;
			}

			if (player.dialogueFactory.isActive() || player.dialogueFactory.isOption()) {
				player.dialogueFactory.clear();
			}
		}
		main = identification;
		player.movement.reset();
		player.send(new SendInterface(identification));
		player.send(new SendString("[CLOSE_MENU]", 0));
		setSidebar(Config.LOGOUT_TAB, -1);
	}

	/** Opens a walkable-itemcontainer for the player. */
	public void openWalkable(int identification) {
		if (walkable == identification) {
			return;
		}
		setWalkable(identification);
		player.send(new SendWalkableInterface(identification));
	}

	/** Opens an inventory interface for the player. */
	public void openInventory(int identification, int overlay) {
		if (player.isBot || main == identification && this.overlay == overlay) {
			return;
		}

		main = identification;
		this.overlay = overlay;
		player.movement.reset();
		player.send(new SendString("[CLOSE_MENU]", 0));
		player.send(new SendInventoryInterface(identification, overlay));
		setSidebar(Config.LOGOUT_TAB, -1);

	}

	/** Clears the player's screen. */
	public void close() {
		close(true);
	}

	/** Handles clearing the screen. */
	public void close(boolean walkable) {
		if (player.isBot) {
			return;
		}
		if (!player.abortBot) {
			return;
		}

		if (player.attributes.has("SHOP")) {
			Store.closeShop(player);
		}

		if (player.attributes.get("BANK_KEY") != null && player.attributes.get("BANK_KEY", Boolean.class)) {
			player.bank.close();
		}

		if (player.attributes.get("PRICE_CHECKER_KEY", Boolean.class)) {
			player.priceChecker.close();
		}

		if (player.attributes.get("TRADE_KEY", Boolean.class)) {
			player.exchangeSession.reset(ExchangeSessionType.TRADE);
		}

		if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
			player.donatorDeposit.close();
		}



		clean(walkable);
		player.dialogueFactory.clear();
		player.send(new SendRemoveInterface());
		player.send(new SendString("[CLOSE_MENU]", 0));
		setSidebar(Config.LOGOUT_TAB, 2449);
	}

	public void setSidebar(int tab, int id) {
		if (sidebars[tab] == id && id != -1) {
			return;
		}
		sidebars[tab] = id;
		player.send(new SendSideBarInterface(tab, id));

	}

	/** Cleans the interfaces. */
	public void clean(boolean walkableFlag) {
		main = -1;
		dialogue = -1;
		if (walkableFlag) {
			walkable = -1;
		}
	}

	/** Checks if a certain interface is enter. */
	public boolean isInterfaceOpen(int id) {
		return main == id;
	}

	/** Checks if the player's screen is clear. */
	public boolean isClear() {
		return main == -1 && dialogue == -1 && walkable == -1;
	}

	/** Checks if the main interface is clear. */
	public boolean isMainClear() {
		return main == -1;
	}

	/** Checks if the dialogue interface is clear. */
	public boolean isDialogueClear() {
		return dialogue == -1;
	}

	/** Sets the current interface. */
	public void setMain(int currentInterface) {
		this.main = currentInterface;
	}

	/** gets the current main interface. */
	public int getMain() {
		return main;
	}

	/** Gets the dialogue interface. */
	public int getDialogue() {
		return dialogue;
	}

	/** Sets the dialogue interface. */
	public void setDialogue(int dialogueInterface) {
		this.dialogue = dialogueInterface;
	}

	/** Gets the walkable interface. */
	public int getWalkable() {
		return walkable;
	}

	/** Sets the walkable interface. */
	public void setWalkable(int walkableInterface) {
		this.walkable = walkableInterface;
	}

	public int getSidebar(int tab) {
		if (tab > sidebars.length) {
			return -1;
		}
		return sidebars[tab];
	}

	public boolean isSidebar(int tab, int id) {
		return tab <= sidebars.length && sidebars[tab] == id;
	}

	public boolean hasSidebar(int id) {
		for (int sidebar : sidebars) {
			if (sidebar == id) {
				return true;
			}
		}
		return false;
	}
}