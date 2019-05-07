package io.server.content.skill.impl.construction;

import java.util.List;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.ObjectDefinition;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

public class BuildableInterface {

	public static void open(Player player, BuildableType type) {
		player.attributes.set("CONSTRUCTION_BUILDTYPE_KEY", type);
		refresh(player, 0, type);
		click(player, -17995);
		player.interfaceManager.open(47500);
	}

	public static void click(Player player, int button) {
		int index = Math.abs((-17995 - button));
		BuildableType type = player.attributes.get("CONSTRUCTION_BUILDTYPE_KEY", BuildableType.class);
		List<BuildableObject> list = BuildableObject.get(type);

		if (index >= list.size()) {
			return;
		}

		display(player, list.get(index));
		refresh(player, index, type);
	}

	public static void display(Player player, BuildableObject object) {
		player.send(new SendString(object == null ? "" : "" + object.getName(), 47515));
		player.send(new SendString(
				object == null ? "" : "</col>Size: <col=5cf442>" + ObjectDefinition.lookup(object.getObject()).width,
				47518));
		player.send(new SendString(
				object == null ? "" : "</col>Level: <col=5cf442>" + Utility.formatDigits(object.getLevel()), 47519));
		player.send(new SendString(
				object == null ? "" : "</col>Experience: <col=5cf442>" + Utility.formatDigits(object.getExperience()),
				47520));
		player.send(new SendItemOnInterface(47517, object == null ? null : object.getItems()));
		player.attributes.set("CONSTRUCTION_BUILDOBJECT_KEY", object);
	}

	public static void refresh(Player player, int selected, BuildableType type) {
		List<BuildableObject> list = BuildableObject.get(type);

		for (int index = 0; index < 50; index++) {
			String name = index >= list.size() ? ""
					: ((selected == index ? "<col=5cf442>" : "</col>") + list.get(index).getName());
			player.send(new SendString(name, 47541 + index));
		}

		player.send(new SendString("Total object: " + list.size(), 47514));
	}
}
