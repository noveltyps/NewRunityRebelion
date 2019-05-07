package io.server.content.activity.panel;

import io.server.Config;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendForceTab;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendProgressBar;
import io.server.net.packet.out.SendString;

public abstract class Activity_Panel {
	
	protected final Player player;
	private final String header;
	private final String[] text = new String[7];
	private String footer = "";
	private Item item;
	private int progress;

	protected Activity_Panel(Player player, String header) {
		this.player = player;
		this.header = header;
	}

	public void open() {
		player.send(new SendString(header, 38003));
		player.send(new SendString(footer, 38004));

		for (int index = 0; index <= 6; index++)
			set(index, "");

		if (!player.interfaceManager.isSidebar(Config.ACTIVITY_TAB, 38000))
			player.send(new SendForceTab(Config.ACTIVITY_TAB));

		player.interfaceManager.setSidebar(Config.ACTIVITY_TAB, 38000);
	}

	public void close() {
		player.send(new SendForceTab(Config.INVENTORY_TAB));
		player.interfaceManager.setSidebar(Config.ACTIVITY_TAB, -1);
	}

	protected void set(int index, String string) {
		if (!string.equals(text[index]))
			player.send(new SendString(text[index] = string, 38005 + index));
	}

	public void setFooter(String footer) {
		if (!footer.equals(this.footer))
			player.send(new SendString(this.footer = footer, 38004));
	}

	public void setItem(Item item) {
		if (!item.equals(this.item))
			player.send(new SendItemOnInterface(38016, this.item = item));
	}

	public void setProgress(int progress) {
		if (this.progress != progress)
			player.send(new SendProgressBar(38015, this.progress = progress));
	}
}
