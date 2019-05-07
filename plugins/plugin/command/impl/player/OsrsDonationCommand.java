package plugin.command.impl.player;

import io.server.content.command.Command;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendInputMessage;
import io.server.util.Utility;
import io.server.utility.emailsystem.EmailSystem;

/**
 * @author Adam_#6723
 */

public class OsrsDonationCommand implements Command {

	@Override
	public void execute(Player player, String command, String[] parts) {
		input(player);
	}

	public void input(Player player) {
		player.send(new SendInputMessage("Please enter your discord username so BrutalOS Team can contact you", 20, string -> {
			try {
				sendEmail(player, string);
			} catch (Exception e) {
				System.err.println("Erorr.");
			}
		}));
	}
	
	public void sendEmail(Player player, String string) {
		String from = EmailSystem.USER_NAME;
		String pass = EmailSystem.PASSWORD;
		String[] to = { EmailSystem.RECIPIENT }; // list of recipient email addresses
		String subject = "OSRS Donation Request";
		String body = "Player: " + player.getName()
				+ " Wishes to donate OSRS, Contact him whenever you can! --- Requested at: " + Utility.getTime() + " Users Discord: " + string;
		EmailSystem.sendFromGMail(from, pass, to, subject, body);
		player.message("You have requested to donate OSRS, We have alerted the owners for your request");
		player.message("They will get back to you as soon as possible.");		
	}

	@Override
	public boolean canUse(Player player) {
		return true;
	}

	@Override
	public String description() {
		return "Donate OSRS";
	}
}
