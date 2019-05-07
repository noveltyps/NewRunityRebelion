package io.server.util.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import io.server.game.world.items.ItemDefinition;
import io.server.game.world.items.containers.equipment.EquipmentType;

/**
 * 
 * @author Adam_#6723
 *
 */
public class ItemStatsDumper {

	private static List<String> list = new ArrayList<>();

	public static void printStats() {
		for (int i = 0; i < 22538; ++i) {
			ItemDefinition item = ItemDefinition.get(i);
			if (item != null && item.getBonuses() != null && item.getEquipmentType() != EquipmentType.NOT_WIELDABLE) {
				String s = "" + item.getId() + "";
				for (int j = 0; j < item.getBonuses().length; ++j) {
					if (j != 11 && j != 12) {
						s += " " + ((int) item.getBonuses()[j]);
					}
				}
				list.add(s);
			}
		}
		print();
	}

	private static void print() {
		try (PrintWriter file = new PrintWriter(new FileOutputStream(new File("statsoutput.txt"), true))) {
			for (int i = 0; i < list.size(); i++) {
				file.append(list.get(i));
				file.println();
			}
			file.flush();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
