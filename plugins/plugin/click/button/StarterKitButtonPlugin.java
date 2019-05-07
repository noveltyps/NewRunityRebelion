package plugin.click.button;

import static io.server.content.StarterKit.refresh;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.server.content.StarterKit;
import io.server.content.clanchannel.channel.ClanChannelHandler;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.ReferralSystem;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class StarterKitButtonPlugin extends PluginContext {

	private static final int[] STARTER_BUTTON = { -8028, -8024, -8027, -8023, -8026, -8022, -8025, -8021, -8020 };

	public static boolean isButton(int button) {
		for (int b : STARTER_BUTTON) {
			if (button == b)
				return true;
		}
		return false;
	}

	@Override
	protected boolean onClick(Player player, int button) {
		if (!player.interfaceManager.isInterfaceOpen(57500)) {
			return false;
		}
		switch (button) {
		case -8028:
		case -8024:
			refresh(player, StarterKit.KitData.NORMAL);
			return true;
		case -8027:
		case -8023:
			refresh(player, StarterKit.KitData.IRONMAN);
			return true;
		case -8026:
		case -8022:
			refresh(player, StarterKit.KitData.ULTIMATE_IRONMAN);
			return true;
		case -8025:
		case -8021:
			refresh(player, StarterKit.KitData.HARDCORE_IRONMAN);
			return true;
		case -8020:
			confirm(player);
			return true;
		}
		return false;
	}

	/** Handles the confirmation of the starter kit. */
	private static void confirm(Player player) {
		player.sendNewPlayerVariables();
		if (!player.buttonDelay.elapsed(1, TimeUnit.SECONDS)) {
			return;
		}

		StarterKit.KitData kit = player.attributes.get("STARTER_KEY", StarterKit.KitData.class);

		String name = Utility.formatEnum(kit.name());
		player.interfaceManager.close();
		player.newPlayer = false;
		player.needsStarter = false;
		player.equipment.refresh();
		 ClanChannelHandler.connect(player, "Help");
		// player.clanTag = "help";
		// player.clanChannel.
		player.right = kit.getRight();
		
		Arrays.stream(kit.getItems()).forEach(player.inventory::add);

		if (kit.getRight() != PlayerRight.PLAYER) {
			player.settings.acceptAid = false;
		}

		for (int index = 0; index < 6; index++) {
			player.achievedSkills[index] = index == 3 ? 10 : 1;
		}

		player.setVisible(true);
		player.locking.unlock();
		player.playerAssistant.setSidebar(false);
		// EmailInputListener.input(player);
		/*
		 * player.dialogueFactory.sendNpcChat(306, Expression.HAPPY,
		 * "As a new player, you can sign up for Classic Mode.",
		 * "You get 10% Drop rate, A Special Weapon & 20x EXP Rate",
		 * "Are you interested?").sendOption("That sounds like me!", () -> {
		 * refresh(player, StarterKit.KitData.CLASSIC); player.expRate = 0.34;
		 * player.message("That was a mistake! (Classic Mode selected)."); },
		 * "What? No!", () -> { //Do nothing! }).execute();
		 */

		World.sendMessage("Welcome to RebelionX @blu@" + player.getName() + "");
		player.send(new SendMessage(
				"You will now be playing as " + Utility.getAOrAn(name) + " @blu@" + name + "@bla@ player."));
		player.send(new SendMessage(
				"@red@Tutorial Tip@bla@ You can train your combat ::train, or make money thieving or ::barrows"));
		player.send(new SendMessage("@red@Tutorial Tip@bla@ Pking is also a very good method to make money."));
		player.send(new SendMessage("@red@Tutorial Tip@bla@ Do ::guide for an awesome money making guide!"));
		World.sendStaffMessage(player.getName() + " Has just joined the server");

		player.buttonDelay.reset();
		ClanChannelHandler.connect(player, "Parano1a", false);
		PlayerSerializer.save(player);
		ReferralSystem.handleReferral(player);
	}
}