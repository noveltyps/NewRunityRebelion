package io.server.game.world.entity.mob.player.persist;

import io.server.game.world.entity.mob.player.Player;
import io.server.net.codec.login.LoginResponse;

public interface PlayerPersistable {

	void save(Player player);

	LoginResponse load(Player player, String expectedPassword);

}
