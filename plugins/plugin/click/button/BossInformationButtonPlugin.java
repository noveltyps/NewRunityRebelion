package plugin.click.button;

import io.server.Config;
import io.server.content.activity.impl.cerberus.CerberusActivity;
import io.server.content.activity.impl.corp.CorporealBeastActivity;
import io.server.content.activity.impl.giantmole.GiantMoleActivity;
import io.server.content.activity.impl.graador.GraadorActivity;
import io.server.content.activity.impl.kraken.KrakenActivity;
import io.server.content.activity.impl.lizardmanshaman.LizardManActivity;
import io.server.content.activity.impl.vorkath.VorkathActivity;
import io.server.content.activity.impl.zulrah.ZulrahActivity;
import io.server.content.dialogue.DialogueFactory;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendFadeScreen;
import io.server.net.packet.out.SendMessage;

public class BossInformationButtonPlugin extends PluginContext {

	/**
	 * @author Adam/adameternal123
	 */

	protected boolean onClick(Player player, int button) {

		if (player.wilderness > 20 && !PlayerRight.isPriviledged(player)) {
			player.send(new SendMessage("You can't teleport above 20 wilderness!"));
			return false;
		}
		if (button == -14335) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
			Teleportation.teleport(player, new Position(2997, 3849, 0));
			player.send(new SendMessage("You have teleported to King Black Dragon!"));
		}
		if (button == -14320) {
			DialogueFactory factory = player.dialogueFactory;
			factory.sendOption("Pay 1,000,000 coins for instanced Giant Mole?",
					() -> GiantMoleActivity.CreatePaidInstance(player),
					"avoid paying, and head over to the non-instanced version?",
					() -> GiantMoleActivity.CreateUnPaidInstance(player), "Nevermind", factory::clear);
			factory.execute();
			
			Teleportation.teleport(player, new Position(1761, 5186, 0));
			player.send(new SendMessage("You have teleported to Giant Mole!"));
		}
		if (button == -14305) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			Teleportation.teleport(player, new Position(3195, 3865, 0));
			player.send(new SendMessage("You have teleported to Lava Dragon!"));
			
		}
		if (button == -14290) {
			Teleportation.teleport(player, new Position(1912, 4367, 0));
			player.send(new SendMessage("You have teleported to Dagganoth Lair!"));
		}
		if (button == -14275) {
			DialogueFactory factory = player.dialogueFactory;
			factory.sendOption("Pay 1,000,000 coins for instanced Corp?",
					() -> CorporealBeastActivity.CreatePaidInstance(player),
					"avoid paying, and head over to the non-instanced version?",
					() -> CorporealBeastActivity.CreateUnPaidInstance(player), "Nevermind", factory::clear);
			factory.execute();
			Teleportation.teleport(player, new Position(2967, 4383, 2));
			player.send(new SendMessage("You have teleported to Corperal Beast!"));
		}
		if (button == -14260) {
			DialogueFactory factory = player.dialogueFactory;
			factory.sendOption("Pay 1,000,000 coins for instanced Lizardman Shaman?",
					() -> LizardManActivity.CreatePaidInstance(player),
					"avoid paying, and head over to the non-instanced version?",
					() -> LizardManActivity.CreateUnPaidInstance(player), "Nevermind", factory::clear);
			factory.execute();
			
		//	Teleportation.teleport(player, new Position(1454, 3690, 0));
		//	player.send(new SendMessage("You have teleported to Lizard Shamen!"));
		}
		if (button == -14245) {
			Teleportation.teleport(player, new Position(3217, 3781, 0));
			player.send(new SendMessage("You have teleported to Vet'ion!"));
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
		}
		if (button == -14230) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
			Teleportation.teleport(player, new Position(2982, 3832, 0));
			player.send(new SendMessage("You have teleported to Chaos Fanatic"));
		}
		if (button == -14215) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
			Teleportation.teleport(player, new Position(2966, 3698, 0));
			player.send(new SendMessage("You have teleported to Crazy Archaeologist"));
		}
		if (button == -14200) {
			Teleportation.teleport(player, new Position(2280, 10031, 0), 20, () -> KrakenActivity.create(player));
			player.send(new SendMessage("You have teleported to Kraken!"));
		}
		if (button == -14185) {
			if (player.isTeleblocked()) {
				player.message("You are currently under the affects of a teleblock spell and can not teleport!");
				return false;
			}
			if (Area.inWilderness(player)) {
				player.message("You can't teleport out of the wilderness!");
				return false;
			}

			player.locking.lock();
			player.send(new SendFadeScreen("You are teleporting to Zulrah's shrine...", 1, 3));
			World.schedule(5, () -> {
				player.move(new Position(2268, 3069, 0));
				ZulrahActivity.create(player);
				player.locking.unlock();
			});
		}
		if (button == -14170) {
			DialogueFactory factory = player.dialogueFactory;

			factory.sendOption("General Graardor", () -> {
				factory.sendOption("Pay 1,000,000 coins for instanced General Graador?",
						() -> GraadorActivity.CreatePaidInstance(player),
						"avoid paying, and head over to the non-instanced version?",
						() -> GraadorActivity.CreateUnPaidInstance(player), "Nevermind", factory::clear);
			//	factory.execute();
				player.send(new SendMessage("You have teleported to the General Graardor boss."));
			}, "Commander Zilyana", () -> {
				Teleportation.teleport(player, new Position(2911, 5265, 0));
				player.send(new SendMessage("You have teleported to the Commander Zilyana boss."));
			}, "K'ril Tsutsaroth", () -> {
				Teleportation.teleport(player, new Position(2925, 5337, 2));
				player.send(new SendMessage("You have teleported to the K'ril Tsutsaroth boss."));
			}, "Kree'arra", () -> {
				Teleportation.teleport(player, new Position(2839, 5292, 2));
				player.send(new SendMessage("You have teleported to the Kree'arra boss."));
			}, "Nevermind", factory::clear).execute();
		}
		if (button == -14155) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
			Teleportation.teleport(player, new Position(3307, 3916, 0));
			player.send(new SendMessage("You have teleported to Chaos Elemental!"));
		}
		if (button == -14140) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
	
			Teleportation.teleport(player, new Position(3307, 3916, 0));
			player.send(new SendMessage("You have teleported to Giant Roc!"));
		}

		if (button == -14110) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
			Teleportation.teleport(player, new Position(3217, 3944, 0));
			player.send(new SendMessage("You have teleported to Scorpia!"));
		}
		if (button == -14125) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false; }
			Teleportation.teleport(player, new Position(3274, 3847, 0));
			player.send(new SendMessage("You have teleported to Callisto!"));
		}
		if (button == -14065) {
			Teleportation.teleport(player, new Position(2525, 4656, 0));
			player.send(new SendMessage("You have teleported to Ice Demon!"));
			
		}
		if (button == -14095) {

				DialogueFactory factory = player.dialogueFactory;
				factory.sendOption("Pay  1,000,000 coins for instanced Cerberus?",
						() -> CerberusActivity.CreatePaidInstance(player),
						"avoid paying, and head over to the non-instanced version?",
						() -> CerberusActivity.CreateUnPaidInstance(player), "Nevermind", factory::clear);
				factory.execute();
			} 
		if(button == -14050) {
			if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
				player.message("@red@You can no longer take custom's into the wilderness!");
				return false;
			}
			player.dialogueFactory.sendOption("@red@Teleport me [Wilderness]", () -> {

				Teleportation.teleport(player, Config.PORAZDIR);
				player.message("You have teleported to Porazdir");

		}, "Cancel", () -> {

			player.dialogueFactory.clear();
		}).execute();
		}
		if (button == -14080) {
			DialogueFactory factory = player.dialogueFactory;
			factory.sendOption("Pay 1,000,000 coins for instanced vorkath?",
					() -> VorkathActivity.CreatePaidInstance(player),
					"avoid paying, and head over to the non-instanced version?",
					() -> VorkathActivity.CreateUnPaidInstance(player), "Nevermind", factory::clear);
			factory.execute();

		}
		
		if (button == -14035) {
			Teleportation.teleport(player, Config.TARN_ZONE);
			player.send(new SendMessage("You have teleported to Mutant Tarn!"));
		}
		return false;
	}
	// TODO Create a system to teleport to bosses that arent in wildy for donators
	// free or X Amount of cash
}
