package io.server.game.world.items.containers.equipment;

import static io.server.game.world.entity.mob.MobAnimation.PLAYER_RUN;
import static io.server.game.world.entity.mob.MobAnimation.PLAYER_STAND;
import static io.server.game.world.entity.mob.MobAnimation.PLAYER_WALK;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableSet;

import io.server.content.emote.Skillcape;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.world.entity.combat.CombatType;
import io.server.game.world.entity.combat.attack.listener.CombatListenerManager;
import io.server.game.world.entity.combat.ranged.RangedAmmunition;
import io.server.game.world.entity.combat.ranged.RangedWeaponDefinition;
import io.server.game.world.entity.combat.ranged.RangedWeaponType;
import io.server.game.world.entity.combat.weapon.WeaponInterface;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.game.world.items.containers.ItemContainer;
import io.server.game.world.items.containers.ItemContainerAdapter;
import io.server.game.world.items.containers.inventory.Inventory;
import io.server.game.world.items.ground.GroundItem;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

/**
 * The container that manages the equipment for a player.
 *
 * @author lare96 <http://github.com/lare96> <--- Useless fucking cunt, so many
 *         unresolved issues he left out.
 * @author adam Trinity Because lare96 did a fucking shit job with this.
 */
public final class Equipment extends ItemContainer {

	/** The size of all equipment instances. */
	public static final int SIZE = 14;

	/** The equipment item display widget identifier. */
	private static final int EQUIPMENT_DISPLAY_ID = 1688;

	/** Equipment slot constants. */
	public static final int HEAD_SLOT = 0, HELM_SLOT = 0, CAPE_SLOT = 1, AMULET_SLOT = 2, WEAPON_SLOT = 3,
			CHEST_SLOT = 4, SHIELD_SLOT = 5, LEGS_SLOT = 7, HANDS_SLOT = 9, FEET_SLOT = 10, RING_SLOT = 12,
			ARROWS_SLOT = 13;

	/** Equipment bonus constants. */
	public static final int STAB_OFFENSE = 0, SLASH_OFFENSE = 1, CRUSH_OFFENSE = 2, MAGIC_OFFENSE = 3,
			RANGED_OFFENSE = 4, STAB_DEFENSE = 5, SLASH_DEFENSE = 6, CRUSH_DEFENCE = 7, MAGIC_DEFENSE = 8,
			RANGED_DEFENSE = 9, STRENGTH_BONUS = 10, RANGED_STRENGTH = 11, MAGIC_STRENGTH = 12, PRAYER_BONUS = 13;

	/** An array of bonus ids. */
	private static final int[] BONUS_IDS = IntStream.rangeClosed(15130, 15143).toArray();

	/** Item bonus names. */
	private static final String[] BONUS_NAMES = { /* 00 */ "Stab", /* 01 */ "Slash", /* 02 */ "Crush", /* 03 */ "Magic",
			/* 04 */ "Range",
			/* - */
			/* 05 */ "Stab", /* 06 */ "Slash", /* 07 */ "Crush", /* 08 */ "Magic", /* 09 */ "Range",
			/* - */
			/* 10 */ "Strength", /* 11 */ "Ranged Strength", /* 12 */ "Magic Strength", /* 13 */ "Prayer" };

	// this method below. what bout it
	// im pretty sure something related to that is causing the issue for the
	// getmaxhit to double

	private void updateBonus() {
		for (int index = 0; index < player.getBonuses().length; index++)
			player.setIndexBonus(index, 0);

		for (Item equipment : toArray()) {
			if (equipment != null) {
				addBonus(equipment);
			}
		}
	}

	/**
	 * The error message printed when certain functions from the superclass are
	 * utilized.
	 */
	private static final String EXCEPTION_MESSAGE = "Please use { equipment.set(index, Item) } instead";

	/**
	 * An {@link ImmutableSet} containing equipment indexes that don't require
	 * appearance updates.
	 */
	private static final ImmutableSet<Integer> NO_APPEARANCE = ImmutableSet.of(RING_SLOT, ARROWS_SLOT);

	/** The player who's equipment is being managed. */
	private final Player player;

	/** Creates a new {@link Equipment}. */
	public Equipment(Player player) {
		super(SIZE, StackPolicy.STANDARD);
		this.player = player;
		addListener(new EquipmentListener());
	}

	public void fightcavesrefresh() {
		updateBonus();
		updateWeight();
		refresh();
	}

	/**
	 * Handles refreshing all the equipment items.
	 */

	public void login() {
		// something do with this here this still the old method?
		// let me think, i found the fix for this somewhere on r-s im pretty sure
		// Arrays.fill(player.getBonuses(), 0);
		for (int index = 0; index < getItems().length; index++) {
			set(index, get(index), false);
		}

		WeaponInterface.execute(player, getWeapon());
		updateBonus();
		updateWeight();
		/**
		 * Combat listener manager, should update combat listneres upon login/
		 * author @Adam_#6723 if this is broken, to let me know.
		 */
		CombatListenerManager.equipmenteffectsonlogin(player);
		/*
		 * 
		 */
		refresh();

	}

	/** Handles opening the equipment screen itemcontainer. */
	public void openInterface() {

		updateBonus();
		writeBonuses();
		player.interfaceManager.open(15106);
		player.send(new SendString(Utility.formatDigits(updateWeight()) + " kg", 15145));
		player.send(new SendString(
				"Melee Maxhit: <col=ff7000>" + player.playerAssistant.getMaxHit(player, CombatType.MELEE) + "</col>",
				15116));
		player.send(new SendString(
				"Range Maxhit: <col=ff7000>" + player.playerAssistant.getMaxHit(player, CombatType.RANGED) + "</col>",
				15117));
		player.send(new SendString(Utility.formatDigits(player.playerAssistant.weight()) + " kg", 15145));
	}

	/**
	 * Adds an item to the equipment container.
	 *
	 * @param item
	 *            The {@link Item} to deposit.
	 * @param preferredIndex
	 *            The preferable index to deposit {@code item} to.
	 * @param refresh
	 *            The condition if we will be refreshing our container.
	 */
	@Override
	public boolean add(Item item, int preferredIndex, boolean refresh) {
		return true;
	}

	/**
	 * Removes an item from the equipment container.
	 *
	 * @param item
	 *            The {@link Item} to withdraw.
	 * @param preferredIndex
	 *            The preferable index to withdraw {@code item} from.
	 * @param refresh
	 *            The condition if we will be refreshing our container.
	 */
	@Override
	public boolean remove(Item item, int preferredIndex, boolean refresh) {
		boolean removed = super.remove(item, preferredIndex, refresh);
		if (removed && !contains(item)) {
			this.appearanceForIndex(item.getEquipmentType().getSlot());
		}
		return removed;
	}

	/** @return the current weight value of the player */
	private double updateWeight() {
		double weight = 0;
		for (Item equipment : toArray()) {
			if (equipment == null)
				continue;
			weight += equipment.getWeight();
		}
		for (Item item : player.inventory.toArray()) {
			if (item == null)
				continue;
			weight += item.getWeight();
		}
		return weight;
	}

	/**
	 * Manually wears multiple items (does not have any restrictions).
	 *
	 * @param items
	 *            The items to wear.
	 */
	public void manualWearAll(Item[] items) {
		for (Item item : items) {
			manualWear(item);
		}
	}

	/**
	 * Manually wears an item (does not have any restrictions).
	 *
	 * @param item
	 *            The item to wear.
	 */
	public void manualWear(Item item) {
		if (item == null)
			return;
		if (!item.isEquipable())
			return;
		EquipmentType type = item.getEquipmentType();
		if (type.getSlot() == -1)
			return;
		set(type.getSlot(), item, false);
		appearanceForIndex(type.getSlot());
	}

	public boolean equip(Item item) {
		int index = player.inventory.computeIndexForId(item.getId());
		return equip(index);
	}

	public boolean equip(int inventoryIndex) {
		if (inventoryIndex == -1)
			return false;

		Inventory inventory = player.inventory;
		Item item = inventory.get(inventoryIndex);

		if (!Item.valid(item))
			return false;

		if (!item.isEquipable())
			return false;

		if (!Utility.checkRequirements(player, item.getRequirements(), "to equip this item."))
			return false;

		if (!Skillcape.equip(player, item))
			return false;

		if (item.getId() == 7927)
            player.playerAssistant.transform(Utility.random(5538, 5544), true);
		
		if (item.getId() == 20017)
            player.playerAssistant.transform(7315, true);

        if (item.getId() == 20005)
            player.playerAssistant.transform(7314, true);
        
        if (item.getId() == 6583)
            player.playerAssistant.transform(2188, true);
		
		if (item.getId() == 21633)
			player.graphic(new Graphic(1395, true, UpdatePriority.VERY_HIGH));

		EquipmentType type = item.getEquipmentType();
		Item current = get(type.getSlot());
		Item toRemove = null;

		if (current != null && item.isStackable() && isItem(type.getSlot(), item.getId())) {
			int amount = item.getAmount();
			if (Integer.MAX_VALUE - current.getAmount() < amount) {
				amount = Integer.MAX_VALUE - current.getAmount();
			}
			set(type.getSlot(), current.createAndIncrement(amount), true);
			inventory.remove(new Item(item.getId(), amount), inventoryIndex, true);
			return true;
		}

		if (hasWeapon() && type.equals(EquipmentType.SHIELD))
			if (item.isTwoHanded() || getWeapon().isTwoHanded())
				toRemove = getWeapon();

		if (hasShield() && type.equals(EquipmentType.WEAPON))
			if (item.isTwoHanded() || getShield().isTwoHanded())
				toRemove = getShield();

		if (inventory.getTotalItemCount() == 28 && hasShield() && hasWeapon() && item.isTwoHanded()) {
			player.send(new SendMessage("You do not have enough space in your inventory."));
			return false;
		}
		/** Dupe related fix - adam trinity **/
		if (current != null && inventory.contains(current.getId()) && current.isStackable()) {
			Item inventoryItem = player.inventory.get(inventoryIndex);
			set(type.getSlot(), inventoryItem, true); // putting item in equipment
			player.inventory.set(inventoryIndex, null, false); // removing item from inventory
			int index = inventory.computeIndexForId(current.getId());
			current.setAmount(current.getAmount() + inventory.get(index).getAmount());
			player.inventory.set(index, current, true); // putting the item from equipment + inventory in inventory
		} else {
			transfer(type.getSlot(), inventoryIndex, player.inventory, true);
		}
		appearanceForIndex(type.getSlot());
		player.getCombat().reset();

		if (toRemove != null) {
			int slot = toRemove.getEquipmentType().getSlot();
			set(slot, null, true);
			appearanceForIndex(slot);
			inventory.add(toRemove, inventoryIndex, true);
		}

		if (player.interfaceManager.isInterfaceOpen(15106)) {
			openInterface();
		}

		return true;
	}

	/**
	 * Unequips an {@link Item} from the underlying player's {@code Equipment}.
	 *
	 * @param equipmentIndex
	 *            The {@code Equipment} index to unequip the {@code
	 *                       Item} from.
	 * @return {@code true} if the item was unequipped, {@code false} otherwise.
	 */
	public boolean unequip(int equipmentIndex) {
		return unequip(equipmentIndex, -1, player.inventory);
	}

	/**
	 * Unequips an {@link Item} from the underlying player's {@code Equipment}.
	 *
	 * @param equipmentIndex
	 *            The {@code Equipment} index to unequip the {@code
	 *                       Item} from.
	 * @param preferredIndex
	 *            The preferred inventory slot.
	 * @param container
	 *            The container to which we are putting the items on.
	 * @return {@code true} if the item was unequipped, {@code false} otherwise.
	 */
	private boolean unequip(int equipmentIndex, int preferredIndex, ItemContainer container) {
		if (equipmentIndex == -1)
			return false;

		Item unequip = get(equipmentIndex);
		if (unequip == null)
			return false;

		if (!container.add(unequip, preferredIndex, true)) {
			return false;
		}

		set(equipmentIndex, null, true);
		appearanceForIndex(equipmentIndex);
		player.getCombat().reset();

		if (!player.interfaceManager.isClear() && !player.interfaceManager.isInterfaceOpen(15106)) {
			player.interfaceManager.close(false);
		}

		if (player.interfaceManager.isInterfaceOpen(15106)) {
			openInterface();
		}

		return true;
	}

	/**
	 * Flags the {@code APPEARANCE} update block, only if the equipment piece on
	 * {@code equipmentIndex} requires an appearance update.
	 */
	private void appearanceForIndex(int equipmentIndex) {
		if (!NO_APPEARANCE.contains(equipmentIndex)) {
			player.updateFlags.add(UpdateFlag.APPEARANCE);
		}
	}

	private void addBonus(Item item) {

		for (int index = 0; index < item.getBonuses().length; index++) 
			player.appendBonus(index, item.getBonus(index), true, item);
		
	}

	private void removeBonus(Item item) {
		for (int index = 0; index < item.getBonuses().length; index++) {
			player.appendBonus(index, item.getBonus(index), false, item);
		}
	}

	/** Writes a specific the bonus value on the equipment itemcontainer. */
	private void writeBonuses() {
		for (int i = 0; i < player.getBonuses().length; i++) {
			String bonus = BONUS_NAMES[i] + ": ";

			if (player.getBonus(i) >= 0)
				bonus += "+";

			bonus += player.getBonus(i);

			if (i == 12)
				bonus += "%";

			player.send(new SendString(bonus, BONUS_IDS[i]));
		}
	}

	public boolean hasHead() {
		return get(HEAD_SLOT) != null;
	}

	public boolean hasAmulet() {
		return get(AMULET_SLOT) != null;
	}

	public boolean hasAmmo() {
		return get(ARROWS_SLOT) != null;
	}

	public boolean hasChest() {
		return get(CHEST_SLOT) != null;
	}

	public boolean hasLegs() {
		return get(LEGS_SLOT) != null;
	}

	public boolean hasHands() {
		return get(HANDS_SLOT) != null;
	}

	public boolean hasFeet() {
		return get(FEET_SLOT) != null;
	}

	public boolean hasRing() {
		return get(RING_SLOT) != null;
	}

	public Item getAmuletSlot() {
		return get(AMULET_SLOT);
	}

	public boolean hasWeapon() {
		return get(WEAPON_SLOT) != null;
	}

	public boolean hasCape() {
		return get(CAPE_SLOT) != null;
	}

	public Item getWeapon() {
		return get(WEAPON_SLOT);
	}

	public Item getCape() {
		return get(CAPE_SLOT);
	}

	public boolean hasShield() {
		return get(SHIELD_SLOT) != null;
	}

	public Item getShield() {
		return get(SHIELD_SLOT);
	}

	public Item[] getEquipment() {
		Item[] equipment = new Item[15];
		equipment[1] = player.equipment.get(Equipment.HELM_SLOT);
		equipment[3] = player.equipment.get(Equipment.CAPE_SLOT);
		equipment[4] = player.equipment.get(Equipment.AMULET_SLOT);
		equipment[5] = player.equipment.get(Equipment.ARROWS_SLOT);
		equipment[6] = player.equipment.get(Equipment.WEAPON_SLOT);
		equipment[7] = player.equipment.get(Equipment.CHEST_SLOT);
		equipment[8] = player.equipment.get(Equipment.SHIELD_SLOT);
		equipment[10] = player.equipment.get(Equipment.LEGS_SLOT);
		equipment[12] = player.equipment.get(Equipment.HANDS_SLOT);
		equipment[13] = player.equipment.get(Equipment.FEET_SLOT);
		equipment[14] = player.equipment.get(Equipment.RING_SLOT);
		return equipment;
	}

	public boolean isNaked() {
		for (Item i : getEquipment()) {
			if (i != null) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Forces a refresh of {@code Equipment} items to the {@code
	 * EQUIPMENT_DISPLAY_ID} widget.
	 */
	public void refresh() {
		refresh(player, EQUIPMENT_DISPLAY_ID);
	}

	/**
	 * Forces a refresh of {@code Equipment} items to the {@code
	 * EQUIPMENT_DISPLAY_ID} widget.
	 */
	@Override
	public void refresh(Player player, int widget) {
		player.send(new SendItemOnInterface(widget, toArray()), true);

	}

	@Override
	public void clear() {
		super.clear();
		Arrays.fill(player.getBonuses(), 0);
	}

	private boolean isItem(int slot, int itemId) {
		Item item = get(slot);
		return item != null && item.getId() == itemId;
	}

	public void unEquip(Item item) {
		if (item == null) {
			return;
		}
		for (Item equip : getItems()) {
			if (equip != null && equip.getId() == item.getId()) {
				EquipmentType type = item.getEquipmentType();
				set(type.getSlot(), null, true);
				appearanceForIndex(type.getSlot());
				if (!player.inventory.add(equip))
					GroundItem.create(player, equip);
			}
		}
	}

	private void onEquip(Item item) {
		EquipmentType type = item.getEquipmentType();

		if (type == EquipmentType.SHIELD || type == EquipmentType.WEAPON) {
			updateRangedEquipment();

			if (item.matchesId(12926) && player.blowpipeDarts != null) {
				addBonus(player.blowpipeDarts);
				retrieve(ARROWS_SLOT).ifPresent(this::removeBonus);
				return;
			}

			item.getRangedDefinition().filter(def -> def.getType().equals(RangedWeaponType.THROWN))
					.ifPresent(def -> retrieve(ARROWS_SLOT).ifPresent(this::removeBonus));
		} else

		if (type == EquipmentType.ARROWS) {
			updateRangedEquipment();

			if (!hasWeapon())
				return;

			if (getWeapon().matchesId(12_926)) {
				removeBonus(item);
				return;
			}

			getWeapon().getRangedDefinition().filter(def -> def.getType().equals(RangedWeaponType.THROWN))
					.ifPresent(def -> removeBonus(item));
		}
	}

	private void onRemove(Item item) {
		EquipmentType type = item.getEquipmentType();

		if (type == EquipmentType.SHIELD || type == EquipmentType.WEAPON) {
			boolean isBlowpipe = item.matchesId(12_926);

			if (isBlowpipe && player.blowpipeDarts != null) {
				removeBonus(player.blowpipeDarts);
			}

			if (isBlowpipe || item.getRangedDefinition().filter(def -> def.getType().equals(RangedWeaponType.THROWN))
					.isPresent()) {
				retrieve(ARROWS_SLOT).ifPresent(this::addBonus);
			}
			updateRangedEquipment();
		} else

		if (type == EquipmentType.ARROWS) {
			if (!hasWeapon())
				return;

			boolean isBlowpipe = getWeapon().matchesId(12_926);

			if (isBlowpipe || getWeapon().getRangedDefinition()
					.filter(def -> def.getType().equals(RangedWeaponType.THROWN)).isPresent()) {
				addBonus(item);
			}

			updateRangedEquipment();
		}
	}

	public void updateRangedEquipment() {
		if (!hasWeapon() || !getWeapon().getRangedDefinition().isPresent()) {
			if (hasWeapon() && getWeapon().matchesId(12_926) && player.blowpipeDarts != null) {
				player.rangedAmmo = RangedAmmunition.find(getWeapon(), player.blowpipeDarts);
			} else {
				player.rangedAmmo = retrieve(ARROWS_SLOT).map(arrow -> RangedAmmunition.find(getWeapon(), arrow))
						.orElse(null);
			}
			player.rangedDefinition = null;
			return;
		}

		RangedWeaponDefinition def = getWeapon().getRangedDefinition().get();
		player.rangedDefinition = def;

		switch (def.getType()) {

		case SHOT:
			player.rangedAmmo = retrieve(ARROWS_SLOT).map(arrow -> RangedAmmunition.find(getWeapon(), arrow))
					.orElse(null);
			break;

		case THROWN:
			player.rangedAmmo = RangedAmmunition.find(getWeapon(), getWeapon());
			break;
		}
	}

	/**
	 * Updates the weapon animation.
	 */
	public void updateAnimation() {
		int stand = PLAYER_STAND;
		int walk = PLAYER_WALK;
		int run = PLAYER_RUN;

		if (hasWeapon()) {
			Item weapon = getWeapon();
			stand = weapon.getStandAnimation();
			walk = weapon.getWalkAnimation();
			run = weapon.getRunAnimation();
		}

		player.mobAnimation.setStand(stand);
		player.mobAnimation.setWalk(walk);
		player.mobAnimation.setRun(run);
	}

	public static boolean isWearingDFS(Player player) {
		return player.equipment.hasShield()
				&& (player.equipment.getShield().getId() == 11283 || player.equipment.getShield().getId() == 11284);
	}

	public static boolean hasAva(Player player) {
		Item cape = player.equipment.getCape();
		return cape != null && (cape.getId() == 13337 || cape.getId() == 10498 || cape.getId() == 10499 || cape.getId() == 21898);
	}

	/**
	 * An {@link ItemContainerAdapter} implementation that listens for changes to
	 * equipment.
	 */
	private final class EquipmentListener extends ItemContainerAdapter {

		/** Creates a new {@link EquipmentListener}. */
		EquipmentListener() {
			super(player);
		}

		@Override
		public int getWidgetId() {
			return EQUIPMENT_DISPLAY_ID;
		}

		@Override
		public String getCapacityExceededMsg() {
			throw new IllegalStateException(EXCEPTION_MESSAGE);
		}

		@Override
		public void itemUpdated(ItemContainer container, Optional<Item> oldItem, Optional<Item> newItem, int index,
				boolean refresh) {
			if (oldItem.equals(newItem))
				return;

			boolean weapon = oldItem.filter(item -> item.getWeaponInterface() != null)
					.orElse(newItem.filter(item -> item.getWeaponInterface() != null).orElse(null)) != null;

			oldItem.ifPresent(item -> {
				removeBonus(item);
				onRemove(item);
				CombatListenerManager.removeListener(player, item.getId());
			});

			newItem.ifPresent(item -> {
				addBonus(item);
				onEquip(item);
				CombatListenerManager.addListener(player, item.getId());
			});

			if (weapon)
				WeaponInterface.execute(player, getWeapon());

			if (refresh)
				sendItemsToWidget(container);
		}

		@Override
		public void bulkItemsUpdated(ItemContainer container) {
			sendItemsToWidget(container);
		}
	}

}