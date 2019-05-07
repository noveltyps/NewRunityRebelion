package io.server.content.donators;

import io.server.Config;
import io.server.content.writer.InterfaceWriter;
import io.server.content.writer.impl.InformationWriter;
import io.server.game.world.World;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.net.packet.out.SendBanner;
import io.server.util.Utility;

/**
 * This class handles everything related to donators.
 *
 * @author Daniel
 */
public class Donation {

	//private static final Logger logger = LogManager.getLogger();

	private final Player player;
	private int credits;
	private int spent;

	public Donation(Player player) {
		this.player = player;
	}

	public void redeem(DonatorBond bond) {
		setSpent(getSpent() + bond.moneySpent);
		setCredits(getCredits() + bond.credits);
		player.message("<col=FF0000>You have claimed your donator bond. You now have "
				+ Utility.formatDigits(getCredits()) + " donator credits!");
		World.sendMessage("<col=CF2192>RebelionXOS: <col=" + player.right.getColor() + ">" + player.getName()
				+ " </col>has opened <col=CF2192>" + Utility.formatEnum(bond.name()) + "</col>! What a damn legend!");
		updateRank(false);
	}

	public void updateRank(boolean login) {
		PlayerRight rank = PlayerRight.forSpent(spent);

		if (rank == null) {
			return;
		}

		if (player.right.equals(rank)) {
			return;
		}

		//final int groupId = PlayerRight.getForumGroupId(rank);

		if (Config.LIVE_SERVER || !Config.SERVER_DEBUG) {

		}
		/*
		 * if (groupId != -1) { try { new JdbcSession(ForumService.getConnection())
		 * .sql("UPDATE core_members SET member_group_id = ? WHERE member_id = ?")
		 * .set(groupId) .set(player.getMemberId()) .update(Outcome.VOID); } catch
		 * (SQLException e) { logger.error(String.
		 * format("error assigning donator group: player=%s right=%s", player.getName(),
		 * rank.name()), e); } } }
		 */

		if (login) {
			if (!PlayerRight.isIronman(player) && !PlayerRight.isManagement(player)) {
				player.right = rank;
				player.updateFlags.add(UpdateFlag.APPEARANCE);
			}
			return;
		}

		if (PlayerRight.isIronman(player)) {
			player.message("Since you are an iron man, your ran icon will not change.");
		} else if (!PlayerRight.isManagement(player)) {
			player.right = rank;
			player.updateFlags.add(UpdateFlag.APPEARANCE);
		}

		String name = rank.getName();
		player.send(new SendBanner("Rank Level Up!",
				"You are now " + Utility.getAOrAn(name) + " " + PlayerRight.getCrown(player) + " " + name + "!",
				0x1C889E));
		player.dialogueFactory
				.sendStatement(
						"You are now " + Utility.getAOrAn(name) + " " + PlayerRight.getCrown(player) + " " + name + "!")
				.execute();
		InterfaceWriter.write(new InformationWriter(player));
	}

	public int getCredits() {
		return credits;
	}

	public int getSpent() {
		return spent;
	}

	public void setCredits(long l) {
		this.credits = (int) l;
	}

	public void setSpent(int spent) {
		this.spent = spent;
	}
}
