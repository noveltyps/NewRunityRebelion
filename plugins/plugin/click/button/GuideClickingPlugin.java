package plugin.click.button;

import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendURL;

/**
 * 
 * @author Adam_#6723
 *
 *
 **/
 public class GuideClickingPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		switch (button) {
	
		case -9472:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening Prayer Guide! credits to : Dani");
			break;
		case -9471:
			player.send(new SendURL("https://rebelionx.net/community"));
			player.message("@red@ Opening Fishing Guide! credits to : Dani");

			break;
		case -9470:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening Runecrafting Guide! credits to : Dani");
			break;
		case -9469:
				//player.send(new SendURL("https://rebelionx.net/community"));
				//player.message("@red@ Opening Mining Guide! credits to : Dani");
			break;
		case -9468:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One! credits to : Dani");
		break;
		case -9467:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9466:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9465:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9464:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9463:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9462:
			//player.send(new SendURL("https://rebelionx.net/community/"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9461:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9460:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9459:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9458:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening All Vs One V2! credits to : Dani");
		break;
		case -9457:
			//player.send(new SendURL("https://rebelionx.net/community"));
			//player.message("@red@ Opening Starter Guide credits to : Dani");
			break;
		}
		return false;
	}
}
