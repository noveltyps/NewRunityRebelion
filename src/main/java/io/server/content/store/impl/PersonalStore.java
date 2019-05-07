package io.server.content.store.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.server.Config;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.store.SellType;
import io.server.content.store.Store;
import io.server.content.store.StoreConstant;
import io.server.content.store.StoreItem;
import io.server.content.store.StoreType;
import io.server.content.store.currency.CurrencyType;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.InterfaceManager.Type;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.ItemContainer;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendScrollbar;
import io.server.net.packet.out.SendString;
import io.server.net.packet.out.SendTooltip;
import io.server.util.GameSaver;
import io.server.util.MessageColor;
import io.server.util.Utility;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 4-1-2017.
 */
public class PersonalStore extends Store {

	/** The shops the player is currently viewing. */
	public static Map<Integer, PersonalStore> FEATURED_SHOPS = new HashMap<>();

	/** A mapping of a player name with the amount of coins waiting for him. */
	public static Map<String, Long> SOLD_ITEMS = new HashMap<>();

	/** The rank of this shop on the featured list. */
	public final int rank;

	/** The title of this shop. */
	public String title;

	/** The caption of this shop. */
	public String caption;

	/** checks the last day logged in **/
	public long lastLogin;
	
	/** The flag is the shop is being updated by the owner. */
	//private boolean updating = false;

	/** Creates a new {@link Store}. */
	public PersonalStore(String name, Optional<StoreItem[]> items, int rank, String title, String caption,
			CurrencyType currency) {
		super(name, ItemContainer.StackPolicy.STANDARD, currency, 28);
		this.rank = rank;
		this.title = title;
		this.caption = caption;
		items.ifPresent(i -> this.container.setItems(i, false));
	}

	public static void add(Player player, PersonalStore store) {
		store.lastLogin = System.currentTimeMillis();
		if (!PERSONAL_STORES.containsValue(store))
			PERSONAL_STORES.put(player.getName(), store);
	}
	
	public long earnings;
	
	public void loadData(Player player) {
		if (player.personalStoreTempItems != null)
		    this.container.set(player.personalStoreTempItems);
		if (!emptyStore())
			PersonalStore.add(player, player.personalStore);
		this.earnings = player.personalStoreTempEarnings;
	}
	
	private boolean emptyStore() {
		Item[] items = this.container.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				return false;
			}
		}
		return true;
	}

	/** Handles claiming coins from the personal store. */
	public void claimCoins(Player player) {
		DialogueFactory factory = player.dialogueFactory;
		
		if (earnings == 0) {
			factory.sendItem("Personal Store", "There are no coins available for you to collect.", Config.CURRENCY)
			.execute();
			return;
		}
		
		boolean added = player.bankVault.add(earnings);
		
		if (added) {
			earnings = 0;
		}
		String message = added ? "Your collected coins have been sent to your bank vault."
				: "There was not enough room inside your bank vault to hold your coins.";
		player.send(new SendMessage(message, MessageColor.DARK_GREEN));
		openMenu(player);
		factory.sendItem("Personal Store", message, Config.CURRENCY).execute();
	}

	public static void openMenu(Player player) {
		
		if (player == null) {
			return;
		}

		if (player.getName() == null) {
			return;
		}

		player.send(new SendString("<col=d38537>Active stores:</col> " + getPersonalShops().size(), 38210));
		player.send(new SendString("<col=d38537>Total Items Sold:</col> " + Utility.formatPrice(GameSaver.ITEMS_SOLD),
				38211));
		player.send(new SendString(
				"<col=d38537>Total Sold Worth:</col> " + Utility.formatPrice(GameSaver.PERSONAL_ITEM_WORTH), 38213));

		player.send(new SendString(player.personalStore.earnings < 1 ? "None" : player.personalStore.earnings, 38207));
		player.interfaceManager.open(Type.PLAYER_OWNED_STORE_MENU);
	}

	/** Creates the player's shops if non-existent or will enter. */
	public static void myShop(Player player) {
		if (!PERSONAL_STORES.containsKey(player.getName())) {
		     player.setPersonalStore(new PersonalStore(player.getName(), Optional.empty(), player.right.getCrown(), player.getName() + "'s Store", "No caption set", CurrencyType.COINS));
		     player.personalStore.open(player);
		} else
			PERSONAL_STORES.get(player.getUsername()).open(player);
	}

	/** Changes the name of the shop. */
	public static void changeName(Player player, String input, boolean caption) {
		if (!Store.PERSONAL_STORES.containsKey(player.getName())) {
			player.send(
					new SendMessage("You do not have a store. Set it up by first opening it from the previous menu."));
			return;
		}

		String type = caption ? "caption" : "title";
		
		if (Arrays.stream(Config.BAD_STRINGS).anyMatch(b -> input.contains(b))) {
			player.send(new SendMessage("You have entered an invalid shop " + type + "."));
			player.interfaceManager.close();
			return;
		}

		PersonalStore shop = (PersonalStore) Store.PERSONAL_STORES.get(player.getName());
		String context = Utility.formatName(input);

		if (caption) {
			shop.caption = context;
		} else {
			shop.title = context;
			player.send(new SendString(shop.title, 40002));
		}

		player.send(new SendMessage("You have changed your shop " + type + " to: " + context + "."));
	}

	/** Handles editing the personal store. */
	public static void edit(Player player) {
		DialogueFactory f = player.dialogueFactory;
		f.sendStatement("Loading your shop details...").sendOption("Change shop name", () -> {
			f.onAction(() -> {
				player.send(new SendInputMessage("Enter the name of your shop:", 30, input -> {
					changeName(player, input, false);
				}));
			});
		}, "Change caption", () -> {
			f.onAction(() -> {
				player.send(new SendInputMessage("Enter the caption of your shop:", 30, input -> {
					changeName(player, input, true);
				}));
			});
		}).execute();
	}
	public static void openPanel(Player player) {
		openPanel(player, null, false);
	}
	/** Opens the panel where a player can search for shops. */
	public static void openPanel(Player player, String search, boolean playerName) {
		List<PersonalStore> personalShops = getPersonalShops();
		int size = personalShops.size() < 10 ? 10 : personalShops.size();
		int count = 0;
		for (int string = 53031, index = 0; index < size; index++) {
			PersonalStore shop = index >= personalShops.size() ? null : personalShops.get(index);
			if (shop != null && search != null && !search.equals("")) {
				if (playerName) {
					if (!shop.name.toLowerCase().contains(search.toLowerCase()))
						continue;
				} else {
					boolean containsSearched = false;
					if (shop.container != null && !shop.container.isEmpty())
					for (Item item : shop.container.getItems()) {
						if (item != null && item.getName().toLowerCase().contains(search.toLowerCase())) {
							containsSearched = true;
							break;
						}
					}
					if (!containsSearched)
						continue;
				}
			}
			String tooltip = shop == null ? "" : "View <col=ffb000>" + shop.name + "<col=FFFFFF>'s shop";
			String name = shop == null ? "" : shop.name;
			String caption = shop == null ? "" : shop.caption;
			if (shop != null)
				player.viewing_shops.put(count++, shop);
			player.send(new SendTooltip(tooltip, string));
			string++;
			player.send(new SendString(name, string));
			string++;
			player.send(new SendString(caption, string));
			string++;
		}

		List<PersonalStore> featured_shops = getFeaturedShops();
		for (int index = 0, string = 53008; index < 10; index++, string++) {
			PersonalStore shop = index >= featured_shops.size() ? null : featured_shops.get(index);
			String name = shop == null ? "" : shop.name;
			String tooltip = shop == null ? "" : "View <col=ffb000>" + shop.name + "<col=FFFFFF>'s shop";
			if (shop != null)
				FEATURED_SHOPS.put(-(12528 + index), shop);
			player.send(new SendTooltip(tooltip, string));
			player.send(new SendString(name, string));
		}
		player.send(new SendString("Available stores: " + personalShops.size(), 53023));
		player.send(new SendScrollbar(53030, (size * 28)));
		player.interfaceManager.open(53000);
	}

	/** Handles adding an item to the player's personal shop. */
	public void add(Player player, Item item, int slot, boolean addX) {
		if (container.getItems()[container.getItems().length-1] != null) {
            player.sendMessage("Your personal store is full!");
            return;
        }
		if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID)) {
			return;
		}
		if (CurrencyType.isCurrency(item.getId())) {
			player.send(new SendMessage("You can not sell any currency in your shop!"));
			return;
		}
		
		

		Item invItem = player.inventory.get(slot);

		if (!item.isTradeable() || !invItem.isTradeable()) {
			player.send(new SendMessage("You can not sell untradeable items in your shop!"));
			return;
		}
		StoreItem storeItem = new StoreItem(invItem.getId(), item.getAmount());
		
		

		if (!addX) {
			player.send(new SendInputAmount("What do you want to value your <col=027399>" + item.getName() + "</col>?",
					10, value -> {
						setValue(player, invItem, storeItem, Integer.parseInt(value), slot);
					}));
			PlayerSerializer.save(player);
			return;
		}
	}

	private void setValue(Player player, Item invItem, StoreItem storeItem, int value, int slot) {
		
		if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID)) 
			return;
		
		int amount = player.inventory.computeAmountForId(invItem.getId());

		if (storeItem.getAmount() > amount && !storeItem.isStackable()) {
			storeItem.setAmount(amount);
		} else if (storeItem.getAmount() > player.inventory.get(slot).getAmount() && storeItem.isStackable()) {
			storeItem.setAmount(player.inventory.get(slot).getAmount());
		}

		player.inventory.remove(storeItem, slot);
		
		
		
		storeItem.setShopValue(value);
		
		Optional<Item> contains = container.stream().filter(i -> i != null && storeItem.getId() == i.getId()
				&& ((StoreItem) i).getPrice() == storeItem.getPrice()).findFirst();

		if (contains.isPresent()) {
			contains.get().incrementAmountBy(storeItem.getAmount());
		} else {
			if (!storeItem.isStackable() && storeItem.getAmount() > 1) {
				container.add(storeItem, true, true);
			} else {
				container.add(storeItem);
			}
		}
		
		
		
		/****
		 * Quick fix for the item prices... fml
		 */
		int index = 0;
		
		for (int i = 0; i < container.size(); i++) {
			if (container.get(i).getId() == storeItem.getId() && container.get(i).getAmount() == storeItem.getAmount())
				index = i;
		}
		
		StoreItem item = (StoreItem) container.retrieve(index).orElse(null);
		
		item.setShopValue(value);
		
		refresh(player);
	}

	/** Modifies the player's shop item. */
	private void modify(Player player, int slot) {
		if (!player.interfaceManager.isInterfaceOpen(StoreConstant.INTERFACE_ID)) {
			return;
		}
		DialogueFactory factory = player.dialogueFactory;
		StoreItem item = (StoreItem) container.retrieve(slot).orElse(null);
		if (item == null)
			return;
		//updating = true;
		factory.sendOption("Change value",
				() -> factory.onAction(() -> player.send(new SendInputAmount("Enter new value", 10, input -> {
					item.setShopValue(Integer.parseInt(input));
					player.send(new SendMessage("You've changed " + item.getName() + "'s value to "
							+ Utility.formatDigits(Integer.parseInt(input)) + " " + item.getShopCurrency(this)));
					refresh(player);
					player.dialogueFactory.clear();
					//updating = false;
				}))),

				//"Change currency", () -> factory.onAction(() -> updating = false),

				"Nevermind", () -> factory.onAction(() -> {
					//updating = false;
					player.dialogueFactory.clear();
				})).execute();
	}

	/** Checks if the player is the owner of the shop. */
	private boolean isOwner(Player player) {
		return this.name.equals(player.getName());
	}

	@Override
	public void itemContainerAction(Player player, int id, int slot, int action, boolean purchase) {
		switch (action) {
		case 1:
			if (purchase) {
				if (this.isOwner(player)) {
					this.modify(player, slot);
				} else {
					this.sendPurchaseValue(player, slot);
				}
			} else {
				if (this.isOwner(player)) {
					this.add(player, new Item(id, 1), slot, false);// this
				}
			}
			break;
		default:
			int count = purchase ? this.container.retrieve(slot).orElse(null).getAmount()
					: player.inventory.retrieve(slot).orElse(null).getAmount();
			int amount = action == 2 ? (purchase ? 1 : 5) : action == 3 ? 10 : action == 4 ? count : -100;

			if (purchase) {
				this.purchase(player, new Item(id, amount), slot);
			} else {
				if (this.isOwner(player)) {
					this.add(player, new Item(id, amount), slot, amount == -100);
				}
			}
			break;
		}
	}

	@Override
	public void onPurchase(Player player, Item item, int slot, Long value) { 
		Player seller = World.getPlayerByName(name);
		
		boolean forceSave = false;
		
		if (seller != null) {
			if (!isOwner(player))
				seller.sendMessage("One of your items from your store has been purchased!");
		} else {
			seller = PlayerSerializer.loadPlayer(name);
			seller.configureStore();
			forceSave = true;
		}
		
		if (isOwner(player)) {
			PlayerSerializer.save(player);
			return;
		}
		
		GameSaver.ITEMS_SOLD++;
		GameSaver.PERSONAL_ITEM_WORTH += value;
		
		seller.personalStore.earnings += value * item.getAmount();
		
		PlayerSerializer.save(player);
		
		if (!forceSave)
		   World.search(name).ifPresent(
				p -> p.send(new SendMessage("You have coins to collect from your shop!", MessageColor.DARK_GREEN)));
		
	}

	@Override
	public void refresh(Player player) {
		if (emptyStore() && PERSONAL_STORES.containsKey(name))
			PERSONAL_STORES.remove(name);
		if (!name.equals(player.personalStore.name) && player.personalStore.emptyStore() && PERSONAL_STORES.containsKey(player.getName()))
			PERSONAL_STORES.remove(player.personalStore.name);
		List<Integer> indicies_to_remove = new ArrayList<Integer>();
		for (Player p : this.players) {
			if (p != null) {
				if (PERSONAL_STORES.containsKey(name) || p.getName().equals(name)) {
					open(p);
					if (p != player)
						p.send(new SendMessage("[" + title + "] The shop has been updated!"));
				} else
					indicies_to_remove.add(p.getIndex());
			}
		}
		for (int idx : indicies_to_remove) {
			Optional<Player> p = World.getPlayerBySlot(idx);
			if (p.isPresent()) {
				close(p.get());
				p.get().message("The shop you were viewing is no-longer available.");
			}
		}
	}

	@Override
	public void open(Player player) {
		player.attributes.set("SHOP", name);
		players.add(player);
		StoreItem[] items = (StoreItem[]) this.container.getItems();

		player.send(new SendString((currencyType == CurrencyType.COINS ? "Coins" : "Points") + ": "
				+ CurrencyType.getValue(player, currencyType), 40008));
		
		int lastItem = 0;
		if (items.length != 0) {
			for (int i = 0; i < items.length; i++) {
	            if (items[i] != null) {
	                player.send(new SendString(items[i] == null ? "0" : items[i].getShopValue() + "," + 0, 40052 + i));
	                lastItem = i;
	            }
	        }
		}
		final int scrollBarSize = lastItem <= 32 ? 0 : (lastItem / 8) * 72;
		player.send(new SendScrollbar(40050, scrollBarSize));
		player.send(new SendString(title, 40002));
		player.send(new SendItemOnInterface(40051, items));
		player.send(new SendItemOnInterface(3823, player.inventory.toArray()));
		player.send(new SendString("Store size: " + items.length, 40007));
		player.interfaceManager.openInventory(StoreConstant.INTERFACE_ID, 3822);
	}

	@Override
	public void close(Player player) {
		players.remove(player);
		player.attributes.remove("SHOP");
		player.viewing_shops.clear();
		player.interfaceManager.close();
	}

	@Override
	public StoreType type() {
		return StoreType.PERSONAL;
	}

	@Override
	public SellType sellType() {
		return SellType.NONE;
	}
}