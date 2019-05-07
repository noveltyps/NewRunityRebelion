package plugin.click.button;

import io.server.content.activity.Activity;
import io.server.game.plugin.PluginContext;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.prayer.Prayer;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.position.Area;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class PrayerButtonPlugin extends PluginContext {

	@Override
	protected boolean onClick(Player player, int button) {
		if (button >= 17202 && button <= 17231) { // selecting a quick prayer
			Prayer prayer = Prayer.values()[button - 17202];

			if (Activity.evaluate(player, it -> !it.canUsePrayer(player))) {
				prayer.reset(player);
				return true;
			}

			if (prayer.level > player.skills.getMaxLevel(Skill.PRAYER)) {
				player.send(new SendConfig(prayer.getQConfig(), 1));
				player.send(new SendMessage("You need a prayer level of @dbl@" + prayer.level + "@bla@ to use @dbl@"
						+ Utility.formatEnum(prayer.name) + "@bla@."));
				return true;
			}
			if (prayer == Prayer.PROTECT_ITEM && player.right.equals(PlayerRight.ULTIMATE_IRONMAN)) {
				player.send(new SendMessage("As an ultimate iron man you do not have access to this prayer!"));
				return true;
			}
			if (!PlayerRight.isPriviledged(player) && player.unlockedPrayers.contains(prayer)) {
				player.send(new SendMessage("You do not have this prayer unlocked! 1"));
				return true;
			}
			if (prayer == Prayer.CHIVALRY && player.skills.getMaxLevel(Skill.DEFENCE) < 60) {
				player.send(new SendMessage("You need a defence level of @dbl@60@bla@ to use @dbl@"
						+ Utility.formatEnum(prayer.name) + "@bla@."));
				return true;
			}
			if ((prayer == Prayer.PIETY || prayer == Prayer.RIGOUR || prayer == Prayer.AUGURY)
					&& player.skills.getMaxLevel(Skill.DEFENCE) < 70) {
				player.send(new SendMessage("You need a defence level of @dbl@70@bla@ to use @dbl@"
						+ Utility.formatEnum(prayer.name) + "@bla@."));
				return true;
			}

			if (!PlayerRight.isPriviledged(player)
					&& (prayer == Prayer.RIGOUR || prayer == Prayer.AUGURY || prayer == Prayer.PRESERVE)
					&& !player.unlockedPrayers.contains(prayer)) {
			//	player.message("You do not have this prayer unlocked! 2");
			//	return true;
				System.out.println("1 1 1");
			}
			player.quickPrayers.toggle(prayer);
			return true;
		}

		if (button == 5000) { // enabling quick prayers
			if (player.inActivity()) {
				if (!player.activity.canUsePrayer(player)) {
					player.send(new SendConfig(659, 0));
					return true;
				}
			}

			if (player.skills.getLevel(Skill.PRAYER) == 0) {
				player.send(new SendConfig(659, 0));
				player.prayer.reset();
				player.send(new SendMessage("You have run out of prayer points; you must recharge at an altar."));
				return true;
			}
			if (!player.quickPrayers.anyActive(Prayer.values())) {
				player.send(new SendConfig(659, 0));
				player.send(new SendMessage("You don't have any quick prayers set!"));
				return true;
			}
			player.prayer.setAs(player.quickPrayers);
			return true;
		}

		if (Prayer.forButton(button).isPresent()) { // using normal prayers
			Prayer prayer = Prayer.forButton(button).get();

			if (player.inActivity()) {
				if (!player.activity.canUsePrayer(player)) {
					prayer.reset(player);
					return true;
				}
			}

			if (player.skills.getLevel(Skill.PRAYER) == 0) {
				prayer.reset(player, "You have run out of prayer points; you must recharge at an altar.");
				return true;
			}

			if (prayer == Prayer.CHIVALRY && player.skills.getMaxLevel(Skill.DEFENCE) < 60) {
				String message = "You need a defence level of @dbl@60@bla@ to use @dbl@"
						+ Utility.formatEnum(prayer.name) + "@bla@.";
				prayer.reset(player, message);
				player.dialogueFactory.sendStatement(message).execute();
				return true;
			}

			if ((prayer == Prayer.PIETY || prayer == Prayer.RIGOUR || prayer == Prayer.AUGURY)
					&& player.skills.getMaxLevel(Skill.DEFENCE) < 70) {
				String message = "You need a defence level of @dbl@70@bla@ to use @dbl@"
						+ Utility.formatEnum(prayer.name) + "@bla@.";
				prayer.reset(player, message);
				player.dialogueFactory.sendStatement(message).execute();
				return true;
			}

			if (prayer.level > player.skills.getMaxLevel(Skill.PRAYER)) {
				String message = "You need a prayer level of @dbl@" + prayer.level + "@bla@ to use @dbl@"
						+ Utility.formatEnum(prayer.name) + "@bla@.";
				prayer.reset(player, message);
				player.dialogueFactory.sendStatement(message).execute();
				return true;
			}

			if (prayer == Prayer.PROTECT_ITEM && player.right.equals(PlayerRight.ULTIMATE_IRONMAN)) {
				String message = "As an ultimate iron man you do not have access to this prayer!";
				prayer.reset(player, message);
				player.dialogueFactory.sendStatement(message).execute();
				return true;
			}

			if ((prayer == Prayer.PROTECT_FROM_MAGIC || prayer == Prayer.PROTECT_FROM_MELEE
					|| prayer == Prayer.PROTECT_FROM_RANGE) && Area.inEventArena(player)) {
				String message = "You can not use protection prayers here!";
				prayer.reset(player, message);
				player.dialogueFactory.sendStatement(message).execute();
				return true;
			}

			if (!PlayerRight.isPriviledged(player)
					&& (prayer == Prayer.RIGOUR || prayer == Prayer.AUGURY || prayer == Prayer.PRESERVE)
					&& !player.unlockedPrayers.contains(prayer)) {
				String message = "You do not have this prayer unlocked! 3";
				prayer.reset(player, message);
				player.dialogueFactory.sendStatement(message).execute();
				return true;
			}

			player.prayer.toggle(prayer);
			return true;
		}
		return false;
	}

}
