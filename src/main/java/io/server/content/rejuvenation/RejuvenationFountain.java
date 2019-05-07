package io.server.content.rejuvenation;

import java.util.concurrent.TimeUnit;

import io.server.game.world.entity.combat.CombatUtil;
import io.server.game.world.entity.combat.effect.CombatEffectType;
import io.server.game.world.entity.combat.strategy.player.special.CombatSpecial;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendRunEnergy;
import io.server.util.Utility;

public class RejuvenationFountain {

	private Player player;

	public RejuvenationFountain(Player player) {
		this.player = player;
	}

	public void execute() {
		int length = PlayerRight.isDonator(player) ? 1 : 2;
		if(!player.rejuvenation.elapsed(PlayerRight.isDonator(player) ? 1 : 2, TimeUnit.MINUTES)) {
			player.dialogueFactory.sendNpcChat(1152, "You can only do this once every "+length+" minute(s)!",
					"Time Passed: " + Utility.getTime(player.restoreDelay.elapsedTime())).execute();
			return;
		}
		player.runEnergy = 100;
		player.send(new SendRunEnergy());
		player.skills.restoreAll();
		CombatSpecial.restore(player, 100);
		player.dialogueFactory.sendNpcChat(1152, "Your health & special attack have been restored!").execute();
		player.restoreDelay.reset();
		player.unpoison();
		CombatUtil.cancelEffect(player, CombatEffectType.POISON);
		CombatUtil.cancelEffect(player, CombatEffectType.VENOM);
	}

}
