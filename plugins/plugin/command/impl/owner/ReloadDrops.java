package plugin.command.impl.owner;

import io.server.content.command.Command;
import io.server.content.skill.SkillRepository;
import io.server.content.store.Store;
import io.server.game.world.World;
import io.server.game.world.entity.combat.attack.listener.CombatListenerManager;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.ItemDefinition;
import io.server.net.packet.out.SendMessage;
import io.server.util.parser.impl.CombatProjectileParser;
import io.server.util.parser.impl.NpcDropParser;
import io.server.util.parser.impl.NpcForceChatParser;
import io.server.util.parser.impl.NpcSpawnParser;
import io.server.util.parser.impl.StoreParser;

/**
 * 
 * @author Adam_#6723
 *
 */

public class ReloadDrops implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {

		//final String name = String.format(parts[1]);
		new NpcDropParser().run();
		player.send(new SendMessage("Npc drops have been successfully loaded."));
	}

	@Override
	public boolean canUse(Player player) {
		return PlayerRight.isDeveloper(player);
	}

	@Override
	public String description() {
		return "Reloads drops data from .json files etc.";
	}
}
