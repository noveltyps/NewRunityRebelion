package io.server.omen.endpoints;

import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import neytorokx.model.IEndpoint;
import neytorokx.model.Request;
import neytorokx.model.Response;

public class LoadComputerEndpoint implements IEndpoint {

	@Override
	public void process(Request req, Response res) {
		
		res.sendHeaders(200, 0);
		
		res.close();
		
		Player player = World.getPlayerByName(req.getQuery().get("username"));
		
		if (player != null)
			player.setCompID(req.getQuery().get("CID"));
		
	}

}
