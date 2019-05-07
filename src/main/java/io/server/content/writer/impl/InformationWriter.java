package io.server.content.writer.impl;

import io.server.content.writer.InterfaceWriter;
import io.server.game.world.World;
import io.server.game.world.entity.mob.npc.dropchance.DropChanceHandler;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendScrollbar;
import io.server.util.Utility;

/**
 * Class handles writing on the quest tab itemcontainer.
 *
 * @author Daniel
 */
public class InformationWriter extends InterfaceWriter {

	public InformationWriter(Player player) {
		super(player);
	}

	private String[] text = { "<icon=13> <col=FF7000>Game Information:",
			"     - Date: <col=FFB83F>" + Utility.getDate(), "     - Uptime: <col=FFB83F>" + Utility.getUptime(),
			"     - PK Bots online: <col=FFB83F>" + World.getBotCount(),
			"     - Staff online: <col=FFB83F>" + World.getStaffCount(),
			"     - Players online: <col=FFB83F>" + World.getPlayerCount(),
			"     - Wilderness online: <col=FFB83F>" + World.getWildernessCount(), "",
			"<icon=14> <col=FF7000>Player Information:",
			"     - Username: <col=FFB83F>" + Utility.formatName(player.getName()),
			"     - Rank: <col=FFB83F>" + PlayerRight.getCrown(player) + " " + player.right.getName(),
			"     - Created: <col=FFB83F>" + player.created,
			"     - Total play time: <col=FFB83F>" + Utility.getTime(player.playTime),
			"     - Session time: <col=FFB83F>" + Utility.getTime(player.sessionPlayTime),
			"     - Networth: <col=FFB83F>" + Utility.formatPrice(player.playerAssistant.networth()) + " "
					+ (Utility.formatPrice(player.playerAssistant.networth()).endsWith("!") ? "" : "GP"),
			"     - Money Spent: <col=FFB83F>$" + Utility.formatDigits(player.donation.getSpent()),
			"     - Donator Credits: <col=FFB83F>" + Utility.formatDigits(player.donation.getCredits()),
			"     - Blood Points: <col=FFB83F>" + Utility.formatDigits(player.bloodPoints),
			"     - Royalty Points: <col=FFB83F>" + player.royalty,
			"     - Royalty Level: <col=FFB83F>" + player.royaltyLevel,
			
			"     - Refferal Points: <col=FFB83F>" + player.getReferralPoints(),
			"     - Drop Rate: <col=FFB83F>" + DropChanceHandler.getRate(player) + " %",
			"     - Slayer Task: <col=FFB83F>" + (player.slayer.getTask() == null ? "None"
					: player.slayer.getTask().getName() + "(" + player.slayer.getAmount() + ")"),
			"     - Slayer Points: <col=FFB83F>" + Utility.formatDigits(player.slayer.getPoints()),
			"     - PK Points: <col=FFB83F>" + Utility.formatDigits(player.pkPoints),
			"     - Boss Points: <col=FFB83F>" + Utility.formatDigits(player.bossPoints),
			"     - Zombie Points: <col=FFB83F>" + Utility.formatDigits(player.zombiePoints),
			"     - Trivia Points: <col=FFB83F>" + Utility.formatDigits(player.triviaPoints),
			"     - Vote Points: <col=FFB83F>" + Utility.formatDigits(player.votePoints),
			"     - Total Vote: <col=FFB83F>" + Utility.formatDigits(player.totalVotes),
			"     - Skill Points: <col=FFB83F>" + Utility.formatDigits(player.skillingPoints),
			"     - All Vs One Points: <col=FFB83F>" + Utility.formatDigits(player.inventory.contains(7775) ? player.inventory.computeAmountForId(7775) : 0),
			"     - Raid Points: <col=FFB83F>" + Utility.formatDigits(player.inventory.contains(621) ? player.inventory.computeAmountForId(621) : 0),
			"     - Kills: <col=FFB83F>" + player.killCount, "     - Deaths: <col=FFB83F> " + player.deathCount,
			"     - KDR: <col=FFB83F>" + player.playerAssistant.getKdr(),
			"     - Killstreak: <col=FFB83F>" + player.killStreak };

	private int[][] font = { { 29451, 1 }, { 29459, 1 } };

	@Override
	protected int startingIndex() {
		return 29451;
	}

	@Override
	public void scroll() {
		player.send(new SendScrollbar(29450, 600));
	}

	@Override
	protected String[] text() {
		return text;
	}

	@Override
	protected int[][] color() {
		return null;
	}

	@Override
	protected int[][] font() {
		return font;
	}

}
