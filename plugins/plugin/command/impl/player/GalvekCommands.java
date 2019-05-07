package plugin.command.impl.player;

import io.server.Config;
import io.server.content.command.Command;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.world.entity.combat.strategy.npc.boss.galvek.GalvekUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.galvek.GalvekUtility.SpawnData1;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class GalvekCommands implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		SpawnData1 galvekpos = GalvekUtility.spawn;

		if(player.inventory.containsAny(Config.CUSTOM_ITEMS) || player.equipment.containsAny(Config.CUSTOM_ITEMS)) {
			player.message("@red@You can no longer take custom's into the wilderness!");
			return;
		}		
		player.dialogueFactory.sendOption("@red@Teleport me [Wilderness]", () -> {

			if (galvekpos != null) {
				Teleportation.teleport(player, galvekpos.getPosition());
				player.message("You have teleported to Galvek");
			} else {
				player.send(new SendMessage("@red@There is currently no galvek event running!"));
			}

		}, "Cancel", () -> {

			player.dialogueFactory.clear();
		}).execute();
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Teleports you to Galvek in the wilderness.";
	}
}
