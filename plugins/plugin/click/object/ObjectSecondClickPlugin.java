package plugin.click.object;

import io.server.Config;
import io.server.content.Obelisks;
import io.server.content.WellOfGoodwill;
import io.server.content.combat.cannon.CannonManager;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.content.store.impl.PersonalStore;
import io.server.game.Animation;
import io.server.game.action.impl.FlaxPickingAction;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.combat.magic.Autocast;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Position;
import io.server.game.world.region.dynamic.minigames.DuoInferno4Session;
import io.server.game.world.region.dynamic.minigames.Raids14Session;
import io.server.game.world.region.dynamic.minigames.Raids24Session;
import io.server.game.world.region.dynamic.minigames.Raids34Session;
import io.server.game.world.region.dynamic.minigames.Raids54Session;
import io.server.game.world.region.dynamic.minigames.ZombieRaidDuo4Session;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;

public class ObjectSecondClickPlugin extends PluginContext {

	@Override
	protected boolean secondClickObject(Player player, ObjectClickEvent event) {
		final GameObject object = event.getObject();
		final int id = object.getId();
		switch (id) {

		case 26760:
			player.message("There are " + World.get().getWildernessResourcePlayers()
					+ " players in the wilderness resource area.");
			break;
		
		case 26044: {
			if (PlayerRight.isIronman(player)) {
				player.send(new SendMessage("As an iron man you may not access player owned stores!"));
				break;
			}
			DialogueFactory f = player.dialogueFactory;
			f.sendOption("Browse all stores", () -> f.onAction(() -> {
				PersonalStore.openPanel(player);
			}), "Open my shop", () -> f.onAction(() -> {
				PersonalStore.myShop(player);
			}), "Edit my shop", () -> f.onAction(() -> {
				PersonalStore.edit(player);
			}), "Collect coins", () -> f.onAction(() -> {
				// TODO
			})).execute();
		}
			break;
		case 194:
			DialogueFactory ffffff = player.dialogueFactory;
			ffffff.sendOption("Set Partner", () -> ffffff.onAction(() -> {
				player.send(new SendInputMessage("Enter name of partner:", 20, input -> {
					player.dialogueFactory.clear();
						Player other = World.getPlayerByName(input);
						
						if (other == player) {
							player.sendMessage("You cannot send yourself a partner request!");
							return;
						}
						if (!PlayerSerializer.saveExists(input)) {
							player.sendMessage("This player doesn't exist!");
							return;
						}
						if (other == null) {
							player.sendMessage(input+" is not online.");
							return;
						}
						if (other.hasAllForOnePartner()) {
							player.sendMessage(input+" already has an Zombie Raid Duo partner!");
							return;
						}
						if (player.lastPartnerRequest > System.currentTimeMillis()) {
							player.sendMessage("You can only send a partner request every 15 seconds!");
							return;
						}
						ZombieRaidDuo4Session.sendPartnerRequest(player, other);
					
				}));
			}), "Never Mind", () -> ffffff.onAction(() -> {
				player.dialogueFactory.clear();
			})).execute();
			break;
		case 4006:
			DialogueFactory fffff = player.dialogueFactory;
			fffff.sendOption("Set Partner", () -> fffff.onAction(() -> {
				player.send(new SendInputMessage("Enter name of partner:", 20, input -> {
					player.dialogueFactory.clear();
						Player other = World.getPlayerByName(input);
						
						if (other == player) {
							player.sendMessage("You cannot send yourself a partner request!");
							return;
						}
						if (!PlayerSerializer.saveExists(input)) {
							player.sendMessage("This player doesn't exist!");
							return;
						}
						if (other == null) {
							player.sendMessage(input+" is not online.");
							return;
						}
						if (other.hasAllForOnePartner()) {
							player.sendMessage(input+" already has an All For One 4 partner!");
							return;
						}
						if (player.lastPartnerRequest > System.currentTimeMillis()) {
							player.sendMessage("You can only send a partner request every 15 seconds!");
							return;
						}
						Raids54Session.sendPartnerRequest(player, other);
					
				}));
			}), "Never Mind", () -> fffff.onAction(() -> {
				player.dialogueFactory.clear();
			})).execute();
			break;
		case 4031:
			DialogueFactory f11111 = player.dialogueFactory;
			f11111.sendOption("Add Partner", () -> f11111.onAction(() -> {
				player.send(new SendInputMessage("Enter name of partner:", 20, input -> {
					player.dialogueFactory.clear();
						Player other = World.getPlayerByName(input);
						
						if (other == player) {
							player.sendMessage("You cannot send yourself a partner request!");
							return;
						}
						if (!PlayerSerializer.saveExists(input)) {
							player.sendMessage("This player doesn't exist!");
							return;
						}
						if (other == null) {
							player.sendMessage(input+" is not online.");
							return;
						}
						if (player.getRaidPartners().size() >= 3) {
							player.sendMessage("You already have the maximum number of players in your party!");
							return;
						} else if (other.partyLeader != null && other.partyLeader != player) {
							player.sendMessage(other.getUsername() + " is already in a party!");
							return;
						} else if (other.getRaidPartners().size() >= 3)  {
							player.sendMessage(other.getUsername() + " already have the maximum number of players in your party!");
							return;
						}
//						if (other.hasAllForOnePartner()) {
//							player.sendMessage(input+" already has an All For One 4 partner!");
//							return;
//						}
						if (player.lastPartnerRequest > System.currentTimeMillis()) {
							player.sendMessage("You can only send a partner request every 15 seconds!");
							return;
						}
						Raids34Session.sendPartnerRequest(player, other);
					
				}));
			}), "Never Mind", () -> f11111.onAction(() -> {
				player.dialogueFactory.clear();
			})).execute();
		
			break;
		case 19039:
			DialogueFactory f1 = player.dialogueFactory;
			f1.sendOption("Set Partner", () -> f1.onAction(() -> {
				player.send(new SendInputMessage("Enter name of partner:", 20, input -> {
					player.dialogueFactory.clear();
						Player other = World.getPlayerByName(input);
						
						if (other == player) {
							player.sendMessage("You cannot send yourself a partner request!");
							return;
						}
						if (!PlayerSerializer.saveExists(input)) {
							player.sendMessage("This player doesn't exist!");
							return;
						}
						if (other == null) {
							player.sendMessage(input+" is not online.");
							return;
						}
						if (other.hasAllForOnePartner()) {
							player.sendMessage(input+" already has an All For One 4 partner!");
							return;
						}
						if (player.lastPartnerRequest > System.currentTimeMillis()) {
							player.sendMessage("You can only send a partner request every 15 seconds!");
							return;
						}
						Raids34Session.sendPartnerRequest(player, other);
					
				}));
			}), "Never Mind", () -> f1.onAction(() -> {
				player.dialogueFactory.clear();
			})).execute();
		
			break;

		case 13617:
			DialogueFactory f11 = player.dialogueFactory;
			f11.sendOption("Set Partner", () -> f11.onAction(() -> {
				player.send(new SendInputMessage("Enter name of partner:", 20, input -> {
					player.dialogueFactory.clear();
						Player other = World.getPlayerByName(input);
						
						if (other == player) {
							player.sendMessage("You cannot send yourself a partner request!");
							return;
						}
						if (!PlayerSerializer.saveExists(input)) {
							player.sendMessage("This player doesn't exist!");
							return;
						}
						if (other == null) {
							player.sendMessage(input+" is not online.");
							return;
						}
						if (other.hasAllForOnePartner()) {
							player.sendMessage(input+" already has an All For One 4 partner!");
							return;
						}
						if (player.lastPartnerRequest > System.currentTimeMillis()) {
							player.sendMessage("You can only send a partner request every 15 seconds!");
							return;
						}
						DuoInferno4Session.sendPartnerRequest(player, other);
					
				}));
			}), "Never Mind", () -> f11.onAction(() -> {
				player.dialogueFactory.clear();
			})).execute();
		
			break;
		case 13618: // GLOBAL-BM-3
			DialogueFactory f111 = player.dialogueFactory;
			f111.sendOption("Add Partner", () -> f111.onAction(() -> {
				player.send(new SendInputMessage("Enter name of partner:", 20, input -> {
					player.dialogueFactory.clear();
						Player other = World.getPlayerByName(input);
						
						if (other == player) {
							player.sendMessage("You cannot send yourself a partner request!");
							return;
						}
						if (!PlayerSerializer.saveExists(input)) {
							player.sendMessage("This player doesn't exist!");
							return;
						}
						if (other == null) {
							player.sendMessage(input+" is not online.");
							return;
						}
						if (player.getRaidPartners().size() >= 3) {
							player.sendMessage("You already have the maximum number of players in your party!");
							return;
						} else if (other.partyLeader != null && other.partyLeader != player) {
							player.sendMessage(other.getUsername() + " is already in a party!");
							return;
						} else if (other.getRaidPartners().size() >= 3)  {
							player.sendMessage(other.getUsername() + " already have the maximum number of players in your party!");
							return;
						}
//						if (other.hasAllForOnePartner()) {
//							player.sendMessage(input+" already has an All For One 4 partner!");
//							return;
//						}
						if (player.lastPartnerRequest > System.currentTimeMillis()) {
							player.sendMessage("You can only send a partner request every 15 seconds!");
							return;
						}
						Raids24Session.sendPartnerRequest(player, other);
					
				}));
			}), "Never Mind", () -> f111.onAction(() -> {
				player.dialogueFactory.clear();
			})).execute();
		
			break;
		case 13619:
			DialogueFactory f1111 = player.dialogueFactory;
			f1111.sendOption("Add Partner", () -> f1111.onAction(() -> {
				player.send(new SendInputMessage("Enter name of partner:", 20, input -> {
					player.dialogueFactory.clear();
						Player other = World.getPlayerByName(input);
						
						if (other == player) {
							player.sendMessage("You cannot send yourself a partner request!");
							return;
						}
						if (!PlayerSerializer.saveExists(input)) {
							player.sendMessage("This player doesn't exist!");
							return;
						}
						if (other == null) {
							player.sendMessage(input+" is not online.");
							return;
						}
						if (player.getRaidPartners().size() >= 3) {
							player.sendMessage("You already have the maximum number of players in your party!");
							return;
						} else if (other.partyLeader != null && other.partyLeader != player) {
							player.sendMessage(other.getUsername() + " is already in a party!");
							return;
						} else if (other.getRaidPartners().size() >= 3)  {
							player.sendMessage(other.getUsername() + " already have the maximum number of players in your party!");
							return;
						}
//						if (other.hasAllForOnePartner()) {
//							player.sendMessage(input+" already has an All For One 4 partner!");
//							return;
//						}
						if (player.lastPartnerRequest > System.currentTimeMillis()) {
							player.sendMessage("You can only send a partner request every 15 seconds!");
							return;
						}
						Raids14Session.sendPartnerRequest(player, other);
					
				}));
			}), "Never Mind", () -> f1111.onAction(() -> {
				player.dialogueFactory.clear();
			})).execute();
		
			break;
			
		
		case 14826:
		case 14827:
		case 14828:
		case 14829:
		case 14830:
		case 14831:
			if (player.isTeleblocked()) {
				player.message("You cannot use the obelisk while teleblocked.");
			} else {
				Obelisks.get().open(player, id);
				player.message("you have teleported using the obelisk.");
			}
			break;
		case 25016:
			if (player.getPosition().getX() < object.getPosition().getX()) {
				player.move(new Position(player.getX() + 1, player.getY(), player.getHeight()));
			} else if (player.getPosition().getX() > object.getPosition().getX()) {
				player.move(new Position(player.getX() - 1, player.getY(), player.getHeight()));
			}
			break;

		case 409:
			Autocast.reset(player);
			player.animate(new Animation(645));
			player.spellbook = Spellbook.ANCIENT;
			player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
			player.send(
					new SendMessage("You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			break;
			
		case 31858:
			Autocast.reset(player);
			player.animate(new Animation(645));
			player.spellbook = Spellbook.ANCIENT;
			player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
			player.send(
					new SendMessage("You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			break;

		/* Well */
		case 884:
			WellOfGoodwill.open(player);
			break;

		/* Dwarf cannon. */
		case 6:
			CannonManager.pickup(player);
			break;

		/* Flax picking. */
		case 14896:
			if (player.inventory.remaining() == 0) {
				player.dialogueFactory.sendStatement("You do not have enough inventory spaces to do this!").execute();
				break;
			}
			player.action.execute(new FlaxPickingAction(player, object), true);
			break;

		default:
			return false;

		}
		return true;
	}

}
