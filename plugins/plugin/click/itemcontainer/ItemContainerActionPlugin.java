package plugin.click.itemcontainer;

import io.server.content.activity.impl.duelarena.DuelRule;
import io.server.content.itemaction.ItemActionRepository;
import io.server.content.skill.impl.crafting.impl.Jewellery;
import io.server.content.store.Store;
import io.server.game.event.impl.ItemContainerContextMenuEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.InterfaceConstants;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendMessage;

public class ItemContainerActionPlugin extends PluginContext {

	@Override
	protected boolean firstClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		
		switch (interfaceId) {

		case 57307:
			player.lostUntradeables.claim(removeSlot, removeId);
			break;

		/* Clan showcase */
		case 57716:
			player.forClan(channel -> channel.getShowcase().select(player, removeId, removeSlot));
			break;

		/* Looting bag */
		case 26706: {
			Item item = player.lootingBag.get(removeSlot);
			if (item == null) {
				return true;
			}
			if (player.interfaceManager.isInterfaceOpen(60_000)) {
				player.lootingBag.withdrawBank(item.createWithAmount(1), removeSlot);
			}
			break;
		}
		/* Spawn container */
		case 37521:
                if (PlayerRight.isPriviledged(player)) {
			player.inventory.add(new Item(removeId));
                }
			break;

		/* Equipment */
		case InterfaceConstants.EQUIPMENT:
			player.equipment.unequip(removeSlot);
			break;

		/* Inventory */
		case InterfaceConstants.INVENTORY_STORE:
			if (player.attributes.get("BANK_KEY", Boolean.class)) {
				player.bank.deposit(removeSlot, 1);
				return true;
			}

			if (player.attributes.get("PRICE_CHECKER_KEY", Boolean.class)) {
				player.priceChecker.deposit(removeSlot, 1);
				return true;
			}

			if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
				player.donatorDeposit.deposit(removeId, removeSlot, 1);
				return true;
			}
			if (player.attributes.get("TOOLKIT_KEY", Boolean.class)) {
//                    player.toolkit.deposit(removeId, removeSlot, 1);
				return true;
			}
			break;

		case InterfaceConstants.PRICE_CHECKER:
			player.priceChecker.withdraw(removeId, 1);
			break;

		case InterfaceConstants.DONATOR_DEPOSIT:
			player.donatorDeposit.withdraw(removeId, removeSlot, 1);
			break;

		case InterfaceConstants.WITHDRAW_BANK:
			player.bank.withdraw(removeId, removeSlot, 1);
			break;

		case InterfaceConstants.PLAYER_STAKE_CONTAINER:
			player.exchangeSession.withdraw(removeSlot, 1);
			DuelRule.validateEquipmentRules(player);
			break;

		case InterfaceConstants.REMOVE_INVENTORY_ITEM:
			player.exchangeSession.deposit(removeSlot, 1);
			break;

		case InterfaceConstants.PLAYER_TRADE_CONTAINER:
			player.exchangeSession.withdraw(removeSlot, 1);
			break;

		/* Store shop value */
		case 40051:
			Store.exchange(player, removeId, removeSlot, 1, true);
			break;

		/* Store sell value */
		case 3823:
			Store.exchange(player, removeId, removeSlot, 1, false);
			break;

		/* Slayer shop */
		case 46503:
			player.slayer.store(removeSlot, 0, true);
			break;

		/* Jewellery */
		case 4233:
		case 4239:
		case 4245:
			Jewellery.click(player, removeId, 1);
			break;

		/* Place holder */
		case 968:
			player.bank.placeHolder(removeId, removeSlot);
			break;

		default:
			return false;
		}
		return true;
	}

	@Override
	protected boolean secondClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		Item item = new Item(removeId);
		switch (interfaceId) {

		/* Looting bag */
		case 26706: {
			item = player.lootingBag.get(removeSlot);
			if (item == null) {
				return true;
			}
			if (player.interfaceManager.isInterfaceOpen(60_000)) {
				player.lootingBag.withdrawBank(item.createWithAmount(5), removeSlot);
				return true;
			}
			return false;
		}

		/* Spawn container */
		case 37521:
                if (PlayerRight.isPriviledged(player)) {
			player.inventory.add(new Item(removeId, 10));
                }
			return true;

		/* Inventory */
		case InterfaceConstants.INVENTORY_STORE:
			if (player.attributes.get("PRICE_CHECKER_KEY", Boolean.class)) {
				player.priceChecker.deposit(removeSlot, 5);
				return true;
			}

			if (player.attributes.get("BANK_KEY", Boolean.class)) {
				player.bank.deposit(removeSlot, 5);
				return true;
			}

			if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
				player.donatorDeposit.deposit(removeId, removeSlot, 5);
				return true;
			}
			return false;

		/* Bank withdraw */
		case InterfaceConstants.WITHDRAW_BANK:
			player.bank.withdraw(removeId, removeSlot, 5);
			return true;

		/* Donator deposit */
		case InterfaceConstants.DONATOR_DEPOSIT:
			player.donatorDeposit.withdraw(removeId, removeSlot, 5);
			return true;

		case InterfaceConstants.PLAYER_STAKE_CONTAINER:
			player.exchangeSession.withdraw(removeSlot, 5);
			DuelRule.validateEquipmentRules(player);
			return true;

		/* Trade deposit */
		case 3322:
			player.exchangeSession.deposit(removeSlot, 5);
			return true;

		/* Trade withdraw */
		case 33021:
			player.exchangeSession.withdraw(removeSlot, 5);
			return true;

		/* Price checker */
		case InterfaceConstants.PRICE_CHECKER:
			player.priceChecker.withdraw(removeId, 5);
			return true;

		/* Store shop value */
		case 40051:
			Store.exchange(player, removeId, removeSlot, 2, true);
			return true;

		/* Store sell value */
		case 3823:
			Store.exchange(player, removeId, removeSlot, 2, false);
			return true;

		/* Slayer shop */
		case 46503:
			player.slayer.store(removeSlot, 1, false);
			return true;

		/* Equipment */
		case 1688:
			if (ItemActionRepository.equipment(player, item, 1)) {
				return true;
			}

			switch (removeId) {
			case 2550:
				int charge = player.ringOfRecoil;
				player.send(new SendMessage("Your ring of recoil has " + charge + " charges remaining."));
				return true;
			}
			return false;

		/* Jewellery */
		case 4233:
		case 4239:
		case 4245:
			Jewellery.click(player, removeId, 5);
			return true;

		}
		return false;
	}

	@Override
	protected boolean thirdClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		switch (interfaceId) {

		/* Looting bag */
		case 26706: {
			Item item = player.lootingBag.get(removeSlot);
			if (item == null) {
				return true;
			}
			if (player.interfaceManager.isInterfaceOpen(60_000)) {
				player.lootingBag.withdrawBank(item.createWithAmount(10), removeSlot);
			}
			break;
		}
		
		

		/* Spawn container */
		case 37521:
                if (PlayerRight.isPriviledged(player)) {
			player.inventory.add(new Item(removeId, 100));
                }
			break;

		/* Inventory */
		case InterfaceConstants.INVENTORY_STORE:
			if (player.attributes.get("PRICE_CHECKER_KEY", Boolean.class)) {
				player.priceChecker.deposit(removeSlot, 10);
				return true;
			}

			if (player.attributes.get("BANK_KEY", Boolean.class)) {
				player.bank.deposit(removeSlot, 10);
				return true;
			}

			if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
				player.donatorDeposit.deposit(removeId, removeSlot, 10);
				return true;
			}
			break;

		/* Bank withdraw */
		case InterfaceConstants.WITHDRAW_BANK:
			player.bank.withdraw(removeId, removeSlot, 10);
			break;

		/* Donator deposit */
		case InterfaceConstants.DONATOR_DEPOSIT:
			player.donatorDeposit.withdraw(removeId, removeSlot, 10);
			break;

		case InterfaceConstants.PLAYER_STAKE_CONTAINER:
			player.exchangeSession.withdraw(removeSlot, 10);
			DuelRule.validateEquipmentRules(player);
			break;

		/* Trade deposit */
		case 3322:
			player.exchangeSession.deposit(removeSlot, 10);
			break;

		/* Trade withdraw */
		case 33021:
			player.exchangeSession.withdraw(removeSlot, 10);
			break;

		/* Price checker */
		case InterfaceConstants.PRICE_CHECKER:
			player.priceChecker.withdraw(removeId, 10);
			break;

		/* Store shop value */
		case 40051:
			Store.exchange(player, removeId, removeSlot, 3, true);
			break;

		/* Store sell value */
		case 3823:
			Store.exchange(player, removeId, removeSlot, 3, false);
			break;

		/* Slayer shop */
		case 46503:
			player.slayer.store(removeSlot, 10, false);
			break;

		/* Jewellery */
		case 4233:
		case 4239:
		case 4245:
			Jewellery.click(player, removeId, 10);
			break;

		default:
			return false;

		}

		return true;
	}

	@Override
	protected boolean fourthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		switch (interfaceId) {

		/* Looting bag */
		case 26706: {
			Item item = player.lootingBag.get(removeSlot);
			if (item == null) {
				return true;
			}
			if (player.interfaceManager.isInterfaceOpen(60_000)) {
				player.lootingBag.withdrawBank(item.createWithAmount(Integer.MAX_VALUE), removeSlot);
			}
			break;
		}

		/* Spawn container */
		case 37521:
               if (PlayerRight.isPriviledged(player)) {
			player.inventory.add(new Item(removeId, 500));
			}
			break;

		/* Inventory */
		case InterfaceConstants.INVENTORY_STORE:
			if (player.attributes.get("PRICE_CHECKER_KEY", Boolean.class)) {
				player.priceChecker.deposit(removeSlot, Integer.MAX_VALUE);
				return true;
			}

			if (player.attributes.get("BANK_KEY", Boolean.class)) {
				player.bank.deposit(removeSlot, Integer.MAX_VALUE);
				return true;
			}

			if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
				player.donatorDeposit.deposit(removeId, removeSlot, Integer.MAX_VALUE);
				return true;
			}
			break;

		/* Bank withdraw */
		case InterfaceConstants.WITHDRAW_BANK:
			player.bank.withdraw(removeId, removeSlot, Integer.MAX_VALUE);
			break;

		/* Donator deposit */
		case InterfaceConstants.DONATOR_DEPOSIT:
			player.donatorDeposit.withdraw(removeId, removeSlot, Integer.MAX_VALUE);
			break;

		case InterfaceConstants.PLAYER_STAKE_CONTAINER:
			player.exchangeSession.withdraw(removeSlot, Integer.MAX_VALUE);
			DuelRule.validateEquipmentRules(player);
			break;

		/* Trade deposit */
		case 3322:
			player.exchangeSession.deposit(removeSlot, Integer.MAX_VALUE);
			break;

		/* Trade withdraw */
		case 33021:
			player.exchangeSession.withdraw(removeSlot, Integer.MAX_VALUE);
			break;

		/* Price checker */
		case InterfaceConstants.PRICE_CHECKER:
			player.priceChecker.withdraw(removeId, Integer.MAX_VALUE);
			break;

		/* Store shop value */
		case 40051:
			Store.exchange(player, removeId, removeSlot, 4, true);
			break;

		/* Store sell value */
		case 3823:
			Store.exchange(player, removeId, removeSlot, 4, false);
			break;

		default:
			return false;

		}
		return true;
	}

	@Override
	protected boolean fifthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		
		switch (interfaceId) {

		case InterfaceConstants.PLAYER_STAKE_CONTAINER:
			player.send(new SendInputAmount(
					amount -> player.exchangeSession.withdraw(removeSlot, Integer.parseInt(amount))));
			DuelRule.validateEquipmentRules(player);
			break;

		case InterfaceConstants.REMOVE_INVENTORY_ITEM:
			player.send(new SendInputAmount(
					amount -> player.exchangeSession.deposit(removeSlot, Integer.parseInt(amount))));
			break;

		case InterfaceConstants.REMOVE_TRADE_ITEM:
			player.send(new SendInputAmount(
					amount -> player.exchangeSession.withdraw(removeSlot, Integer.parseInt(amount))));
			break;

		/* Spawn container */
		case 37521:
                if (PlayerRight.isPriviledged(player)) {
			player.inventory.add(new Item(removeId, Integer.MAX_VALUE));
               }
			break;

		/* Store */
		case InterfaceConstants.SHOP_INTERFACE:
			Store.exchange(player, removeId, removeSlot, 5, true);
			break;

		/* Store sell value */
		case 3823:
			Store.exchange(player, removeId, removeSlot, 5, false);
			break;

		/* Bank x */
		case InterfaceConstants.WITHDRAW_BANK:
			player.send(new SendInputAmount("Enter an amount to withdraw:", 10,
					amount -> player.bank.withdraw(removeId, removeSlot, Integer.parseInt(amount))));
			System.out.println(player.getName() + "EXECUTED THE AMONT TO WITHDRAW " + removeId+ " " + " "+removeSlot);

			break;

		/* Donator deposit */
		case 57207:
			if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
				player.send(new SendInputAmount("Enter an amount to withdraw:", 10,
						amount -> player.donatorDeposit.withdraw(removeId, removeSlot, Integer.parseInt(amount))));
				System.out.println(player.getName() + "EXECUTED THE AMONT TO WITHDRAW " + removeId+ " " + " "+removeSlot);
			}
			break;

		/* Bank store x */
		case InterfaceConstants.INVENTORY_STORE: {
			if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
				player.send(new SendInputAmount("Enter an amount to deposit:", 10,
						amount -> player.donatorDeposit.deposit(removeId, removeSlot, Integer.parseInt(amount))));
				break;
			}

			if (player.attributes.get("PRICE_CHECKER_KEY", Boolean.class)) {
				player.send(new SendInputAmount("Enter an amount to price check:", 10,
						amount -> player.priceChecker.deposit(removeSlot, Integer.parseInt(amount))));
				break;
			}

			player.send(new SendInputAmount("Enter an amount to deposit:", 10,
					amount -> player.bank.deposit(removeSlot, Integer.parseInt(amount))));
		}
			break;

		default:
			return false;

		}
		return true;
	}

	@Override
	protected boolean sixthClickItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		final int amount = event.getAmount();

		switch (interfaceId) {

		case InterfaceConstants.INVENTORY_STORE:
			if (player.attributes.get("PRICE_CHECKER_KEY", Boolean.class)) {
				player.priceChecker.deposit(removeSlot, amount);
			}

			if (player.attributes.get("BANK_KEY", Boolean.class)) {
				player.bank.deposit(removeSlot, amount);
			}

			if (player.attributes.get("DONATOR_DEPOSIT_KEY", Boolean.class)) {
				player.donatorDeposit.deposit(removeId, removeSlot, amount);
			}
			break;

		case InterfaceConstants.WITHDRAW_BANK:
			player.bank.withdraw(removeId, removeSlot, amount);
			break;

		case InterfaceConstants.DONATOR_DEPOSIT:
			player.donatorDeposit.withdraw(removeId, removeSlot, amount);
			break;

		case InterfaceConstants.PLAYER_STAKE_CONTAINER:
			player.exchangeSession.withdraw(removeSlot, amount);
			DuelRule.validateEquipmentRules(player);
			break;

		/* Trade deposit */
		case 3322:
			player.exchangeSession.deposit(removeSlot, amount);
			break;

		/* Trade withdraw */
		case 33021:
			player.exchangeSession.withdraw(removeSlot, amount);
			break;

		case InterfaceConstants.PRICE_CHECKER:
			player.priceChecker.withdraw(removeId, amount);
			break;

		default:
			return false;
		}

		return true;
	}

	@Override
	protected boolean allButOneItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		switch (interfaceId) {
		case InterfaceConstants.WITHDRAW_BANK:
			Item item = player.bank.get(removeSlot);
			if (item == null || item.getId() != removeId || item.getAmount() <= 1) {
				break;
			}
			player.bank.withdraw(removeId, removeSlot, item.getAmount() - 1);
			break;

		default:
			return false;
		}
		return true;
	}

	@Override
	protected boolean modifiableXItemContainer(Player player, ItemContainerContextMenuEvent event) {
		final int interfaceId = event.getInterfaceId();
		final int removeSlot = event.getRemoveSlot();
		final int removeId = event.getRemoveId();
		final int amount = event.getAmount();
		switch (interfaceId) {
		case InterfaceConstants.WITHDRAW_BANK:
			Item item = player.bank.get(removeSlot);
			if (item == null || item.getId() != removeId) {
				break;
			}
			player.bank.withdraw(removeId, removeSlot, amount);
			break;

		default:
			return false;
		}

		return true;
	}

}