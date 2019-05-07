package io.server.util.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import com.google.gson.JsonObject;

import io.server.content.store.SellType;
import io.server.content.store.Store;
import io.server.content.store.StoreItem;
import io.server.content.store.currency.CurrencyType;
import io.server.content.store.impl.DefaultStore;
import io.server.util.parser.GsonParser;

//import io.battlerune.content.shop.Store;
//import io.battlerune.content.shop.StoreItem;
//import io.battlerune.content.shop.StoreRepository;

/**
 * Parses throug the shops files and creates in-game shop object for the game on
 * startup.
 *
 * @author Daniel | Obey
 */
public class StoreParser extends GsonParser {

	public StoreParser() {
		super("def/store/stores", false);
	}

	@Override
	protected void parse(JsonObject data) {
		final String name = Objects.requireNonNull(data.get("name").getAsString());
		final CurrencyType currency = builder.fromJson(data.get("currency"), CurrencyType.class);
		final boolean restock = data.get("restock").getAsBoolean();
		final String sellType = data.get("sellType").getAsString().toUpperCase();
		final LoadedItem[] loadedItems = builder.fromJson(data.get("items"), LoadedItem[].class);

		final List<StoreItem> storeItems = new ArrayList<>(loadedItems.length);

		for (LoadedItem loadedItem : loadedItems) {
			OptionalInt value = loadedItem.value == 0 ? OptionalInt.empty() : OptionalInt.of(loadedItem.value);
			storeItems
					.add(new StoreItem(loadedItem.id, loadedItem.amount, value, Optional.ofNullable(loadedItem.type)));
		}

		StoreItem[] items = storeItems.toArray(new StoreItem[storeItems.size()]);
		Store.STORES.put(name, new DefaultStore(items, name, SellType.valueOf(sellType), restock, currency));
	}

	private static final class LoadedItem {

		private final int id;

		private final int amount;

		private final int value;

		private final CurrencyType type;

		@SuppressWarnings("unused")
		public LoadedItem(int id, int amount, int value, CurrencyType type) {
			this.id = id;
			this.amount = amount;
			this.value = value;
			this.type = type;
		}
	}

}
