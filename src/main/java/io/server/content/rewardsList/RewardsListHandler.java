package io.server.content.rewardsList;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendString;

/**
 * 
 * @author Adam_#6723
 * Reward Interface Handler, which fetches data from the enum associated.
 * 
 */

public class RewardsListHandler {	
	
	protected Player player;
	
	public RewardsListHandler(Player player) {
		this.player = player;
	}

	public void Open() {
		if(player == null) {
			return;
		}	
		SendRewardStrings();
		player.interfaceManager.open(17550);
	}
	/** Force opens the rewards interface open method, to a specific hash map. **/
	public void Open(int id) {
		RewardsInterfaceData data = RewardsInterfaceData.byId.get(id);
		id = data.getIndex();
		if(player == null) {
			return;
		}	
		SendRewardStrings();
		ItemGroup(id);
		player.interfaceManager.open(17550);
	    

	}
	
	public void SendRewardStrings() {
		int startID = 17652;
		for (RewardsInterfaceData data : RewardsInterfaceData.values()) {
           player.send(new SendString(data.getText(), startID++));
		}
	}	

	/**
	 * So basically this fetches the items for a specific itemgroup.
	 * for instance, in the enum each value is numbered from 1 and so on.
	 * the int id represents what group of item you wish to access.
	 * @param id
	 */
	public void ItemGroup(int id) {
			RewardsInterfaceData data = RewardsInterfaceData.byId.get(id);
			if (data != null) {
				Item items[] = new Item[data.getItemID().length];
				for (int index = 0, count = 0; index < data.getItemID().length; index++, count++) {
					Item item = new Item(data.getItemID()[index]);
					items[count] = item;
				}
				player.send(new SendItemOnInterface(17752, items));
			}
	        player.send(new SendString("Rewards List From: " + data.getText(), 17556));
	}
	
	
	public void ResetFrame34() {
		int interfaceId = 17752;
		for (int index = 0; index < 100; index++) {
			player.send(new SendItemOnInterface(interfaceId, new Item(-1, 0)));
		}
	}
	
	public boolean button(int buttonId) {
		switch (buttonId) {
		case 17553:
			player.interfaceManager.close();
			return true;
		case 17652:
			ResetFrame34();
			ItemGroup(0);
			return true;
		case 17653:
			ResetFrame34();
			ItemGroup(1);
			return true;
		case 17654:
			ResetFrame34();
			ItemGroup(2);
			return true;
		case 17655:
			ResetFrame34();
			ItemGroup(3);
			return true;
		case 17656:
			ResetFrame34();
			ItemGroup(4);
			return true;
		case 17657:
			ResetFrame34();
			ItemGroup(5);
			return true;
		case 17658:
			ResetFrame34();
			ItemGroup(6);
			return true;
		case 17659:
			ResetFrame34();
			ItemGroup(7);
			return true;
		case 17660:
			ResetFrame34();
			ItemGroup(8);
			return true;
		case 17661:
			ResetFrame34();
			ItemGroup(9);
			return true;
		case 17662:
			ResetFrame34();
			ItemGroup(10);
			return true;
		}
	return false;
	}	
}
