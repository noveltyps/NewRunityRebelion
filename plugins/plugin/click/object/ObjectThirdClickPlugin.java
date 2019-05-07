package plugin.click.object;

import io.server.Config;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.game.Animation;
import io.server.game.event.impl.ObjectClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.combat.magic.Autocast;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.region.dynamic.minigames.DuoInferno4Session;
import io.server.game.world.region.dynamic.minigames.Raids14Session;
import io.server.game.world.region.dynamic.minigames.Raids24Session;
import io.server.game.world.region.dynamic.minigames.Raids34Session;
import io.server.game.world.region.dynamic.minigames.Raids44Session;
import io.server.game.world.region.dynamic.minigames.Raids54Session;
import io.server.game.world.region.dynamic.minigames.ZombieRaidDuo4Session;
import io.server.net.packet.out.SendMessage;

public class ObjectThirdClickPlugin extends PluginContext {

	@Override
	protected boolean thirdClickObject(Player player, ObjectClickEvent event) {
		final int id = event.getObject().getId();

		switch (id) {
		case 409:
			Autocast.reset(player);
			player.animate(new Animation(645));
			player.spellbook = Spellbook.LUNAR;
			player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
			player.send(
					new SendMessage("You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			break;
			
		case 31858:
			Autocast.reset(player);
			player.animate(new Animation(645));
			player.spellbook = Spellbook.LUNAR;
			player.interfaceManager.setSidebar(Config.MAGIC_TAB, player.spellbook.getInterfaceId());
			player.send(
					new SendMessage("You are now using the " + player.spellbook.name().toLowerCase() + " spellbook."));
			break;
			
		case 13617:
			DuoInferno4Session session = new DuoInferno4Session();
            session.resetPartner();
			player.message("You have reset your duo inferno partner!");
			break;
			
		case 194:
			ZombieRaidDuo4Session session111111 = new ZombieRaidDuo4Session();
            session111111.resetPartner();
			player.message("You have reset your zombie raid duo partner!");
			break;
		case 4006:
			Raids54Session session11111 = new Raids54Session();
            session11111.resetPartner();
			player.message("You have reset your raids 5 partner!");
			break;
		case 13618:
			Raids24Session session1 = new Raids24Session();
            session1.resetPartner();
			player.message("You have reset your raids 2 partner!");
			break;
		case 19039:
			Raids44Session session1111 = new Raids44Session();
            session1111.resetPartner();
			player.message("You have reset your raids partner!");
			break;	
		case 13619:
			Raids14Session session11 = new Raids14Session();
            session11.resetPartner();
			player.message("You have reset your raids partner!");
			break;
		case 4031:
			Raids34Session session111 = new Raids34Session();
            session111.resetPartner();
			player.message("You have reset your raids partner!");
		break;

		}

		return false;
	}

}
