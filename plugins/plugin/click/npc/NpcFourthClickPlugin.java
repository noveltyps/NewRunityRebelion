package plugin.click.npc;

import java.util.ArrayList;

import io.server.content.dialogue.DialogueFactory;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.mob.player.persist.PlayerSerializer;
import io.server.net.packet.out.SendInputMessage;

/**
 * 
 * @author Teek
 *
 */
public class NpcFourthClickPlugin extends PluginContext {

	@Override
	protected boolean fourthClickNpc(Player player, NpcClickEvent event) {
		switch (event.getNpc().id) {
		case 311:
			handleIronmanOptions(player);
			break;
		}
		return false;
	}
	
	
	private void handleIronmanOptions(Player player) {
		if (!PlayerRight.isIronman(player)) {
			player.sendMessage("Only Ironman can access this feature!");
			return;
		}
		DialogueFactory factory = player.dialogueFactory;
		
		factory.sendOption("Create Ironman Group", () -> {
			
			if (player.hasIronmanGroup()) {
				reset(player, true, "You need to leave your current Ironman Group if you..", ".. wish to create your own group!");
				return;
			}
			modifyGroup(player, true);
			reset(player, true, "You have created an Ironman Group!", "If you wish to invite players into your group 'Right Click' Ironman Options > Invite");
			
		}, "Invite Players To Your Group", () -> {
			
			if (!player.hasIronmanGroup()) {
				reset(player, true, "You aren't apart of an Ironman group to invite players!");
				return;
			}
			if (player.ironmanGroupLeader != player.getUsername()) {
				reset(player, true, "Only your Ironman Group Leader can invite players!");
				return;
			}
			if (player.ironManGroup.size() >= 3) {
				player.sendMessage("Your Ironman Group is full!");
				return;
			}
			factory.onAction(() -> {
				sendInviteNameInput(player);
			});
			
			
		}, "Leave Ironman Group", () -> {
			if (player.hasIronmanGroup()) {
				modifyGroup(player, false);
				reset(player, true, "You have left your Ironman Group.");
				return;
			}
			reset(player, true, "You arent' in an Ironman Group to leave.");
		}, "Never mind.", () -> {
			reset(player, true, new String[] {});
		});
		factory.execute();
		
	}


	private void sendInviteNameInput(Player player) {
		player.send(new SendInputMessage("Enter", 12, input -> {
			handleIronmanInvitation(player, input);
		}));
	}


	private void handleIronmanInvitation(Player player, String name) {
		
		reset(player, true, new String[] {});
		
		Player other = World.getPlayerByName(name);
		
		if (!PlayerSerializer.saveExists(name)) {
			player.sendMessage("Error inviting player.. player account doesn't exist!");
			return;
		}
		
		if (other == null) {
			player.sendMessage(name+" isn't online to invite.");
			return;
		} else {
			
			player.sendMessage("Sending Ironman Group Invitation to "+other.getUsername()+"...");
			
			if (!PlayerRight.isIronman(other)) {
				player.sendMessage(other.getUsername()+" is not an Ironman and cannot be invited to your group!");
				return;
			}
			
			if (other.hasIronmanGroup()) {
				player.sendMessage(other.getUsername()+" is already apart of an Ironman Group!");
				return;
			}
			
            DialogueFactory factory = other.dialogueFactory;
			
			factory.sendOption("Accept Ironman Group Invitation", () -> {
				player.sendMessage(other.getUsername()+" has accepted your Ironman Group Invitation");
				reset(player, true, "You have Accepted "+player.getUsername()+" Group Ironman invitation");
				joinIronmanGroup(player, other);
			}, "Decline Ironman Group Invitation", () -> {
				reset(player, true, "You have Declined "+player.getUsername()+" Group Ironman invitation");
				player.sendMessage(other.getUsername()+" has declined your Ironman Group Invitation");
			});
			factory.execute();
		}
	} 


	private void joinIronmanGroup(Player player, Player other) {
		player.sendIronmanGroupMessage("(Ironman Group): "+other.getUsername()+" has joined your Ironman Group!");
		player.ironManGroup.add(other.getUsername());
		other.ironmanGroupLeader = player.getUsername();
		other.ironManGroup = player.ironManGroup;
	}


	public void reset(Player player, boolean closeInterface, String... msg) {
		DialogueFactory factory = player.dialogueFactory;
		if (factory != null)
		    factory.clear();
		if (closeInterface)
			player.interfaceManager.close();
		if (msg != null) {
			player.sendMessage(msg[0]);
		 if (msg.length > 1 && msg[1] != null)
			player.sendMessage(msg[1]);
		}
	}
	
	private void modifyGroup(Player player, boolean setup) {
		player.ironmanGroupLeader = setup ? player.getUsername() : null;
		player.ironManGroup = new ArrayList<String>(3);
	}
	
}
