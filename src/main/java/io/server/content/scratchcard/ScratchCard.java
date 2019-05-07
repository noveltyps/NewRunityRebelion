package io.server.content.scratchcard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendItemOnInterface;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendString;
import io.server.util.Utility;

public class ScratchCard {

	private Player player;

	public ScratchCard(Player player) {
		this.player = player;
	}

	public void display() {

		if (inSession()) {
			return;
		}

		if (player.inventory.contains(455)) {
			player.inventory.remove(new Item(455, 1));

			cleanInterface();

			player.interfaceManager.open(20011);

			COMBINATION.add(new ScratchCardCombination(new ScratchCardInstanced(20016, getRandom()),
					new ScratchCardInstanced(20021, getRandom()), new ScratchCardInstanced(20026, getRandom())));
		}
	}

	/**
	 * On {@code display}
	 */
	private void cleanInterface() {

		setBonus(false);
		COMBINATION.clear();
		COMBINATION_COUNT.clear();

		for (int j = 20020; j < 20035; j += 5) {
			player.send(new SendItemOnInterface(j));
		}

		for (int i = 20019; i < 20036; i += 5) {
			player.send(new SendString("$", i));
		}

		player.send(new SendItemOnInterface(20036, new Item(6199)));
		player.send(new SendString("Match 3 to Win!", 20014));
	}

	/**
	 * Processes on button {@click}
	 */
	private void process() {
		if (COMBINATION_COUNT.size() >= 3) {
			if ((COMBINATION_COUNT.get(0).getId() == COMBINATION_COUNT.get(1).getId())
					&& (COMBINATION_COUNT.get(0).getId() == COMBINATION_COUNT.get(2).getId())) {
				player.inventory.add(new Item(COMBINATION_COUNT.get(0).getId()));
				player.send(new SendString("Congratulation's, you have won!", 20014));
			} else {
				player.send(new SendString("Bad luck, you have lost!", 20014));
			}
			getBonus();
		}
	}

	/**
	 * Reveals the clicked card
	 * 
	 * @param button
	 */
	public void reveal(int button) {
		if (button == 20037 || button == 20033) {
			miscButton(button);
			return;
		}
		switch (button) {
		case 20016:
			for (int i = 0; i < COMBINATION.size(); i++) {
				if (COMBINATION_COUNT.contains(COMBINATION.get(i).getFirst())) {
					player.send(new SendMessage("You've already revealed the first card!"));
					return;
				}
				COMBINATION_COUNT.add(COMBINATION.get(i).getFirst());
				player.send(new SendString("", 20019));
				player.send(new SendItemOnInterface(20020, new Item(COMBINATION.get(i).getFirst().getId())));
			}
			break;
		case 20021:
			for (int i = 0; i < COMBINATION.size(); i++) {
				if (COMBINATION_COUNT.contains(COMBINATION.get(i).getSecond())) {
					player.send(new SendMessage("You've already revealed the second card!"));
					return;
				}
				COMBINATION_COUNT.add(COMBINATION.get(i).getSecond());
				player.send(new SendString("", 20024));
				player.send(new SendItemOnInterface(20025, new Item(COMBINATION.get(i).getSecond().getId())));
			}
			break;
		case 20026:
			for (int i = 0; i < COMBINATION.size(); i++) {
				if (COMBINATION_COUNT.contains(COMBINATION.get(i).getThird())) {
					player.send(new SendMessage("You've already revealed the third card!"));
					return;
				}
				COMBINATION_COUNT.add(COMBINATION.get(i).getThird());
				player.send(new SendString("", 20029));
				player.send(new SendItemOnInterface(20030, new Item(COMBINATION.get(i).getThird().getId())));
			}
			break;
		}
		process();
	}

	private void miscButton(int button) {
		switch (button) {

	/*	case 20033:
			if (BONUS) {
				player.send(new SendMessage("You have revealed your bonus prize already!"));
				return;
			}
			getBonus();
			break; */
		case 20037:
			if (COMBINATION_COUNT.size() < 3 || !BONUS) {
				player.send(new SendMessage("Please finish your scratch session!"));
				return;
			}
			player.interfaceManager.close();
			break;
		}
	}

	/**
	 * Fetches a random bonus
	 */
	public void getBonus() {
		setBonus(true);
		Item[] items = new Item[] { new Item(995, Utility.random(35000000, 50000000)), new Item(6199, 1), new Item(4151, 1),
				new Item(13175, 1), // h'ween set
				new Item(21006, 1), // kodai wand
				new Item(21015, 1), // Dinh bulwark
				new Item(12821, 1), // spectral ss
				new Item(12817, 1), // ely ss
				new Item(12825, 1), // arcane ss
				new Item(20997, 1), // Twsited Bow
				new Item(13739, 1), // Lava Scythe
				new Item(13740, 1), // Twsited Bow

				new Item(11771, 1), // Archers ring (i)
				new Item(11773, 1), // Berserker ring (i)
				new Item(11772, 1), // warrior ring (i)
				
				new Item(12006, 1), // Abyssal Tentacle
				new Item(11832, 1), // Abyssal Tentacle
				new Item(11834, 1), // Abyssal Tentacle
				
				new Item(13239, 1), // Abyssal Tentacle
				new Item(13237, 1), // Abyssal Tentacle
				new Item(13235, 1), // Abyssal Tentacle

				new Item(4151, 1), // Abyssal Whip
				new Item(6585, 1), // Fury
				new Item(11840, 1), // Dragon boots
				};
		Item[] items1 = new Item[] { 
				new Item(995, Utility.random(35000000, 50000000)), 
				new Item(11806, 1), // h'ween set
				new Item(11808, 1), // kodai wand
				new Item(13722, 1), // kodai wand
				new Item(13833, 1), // kodai wand
				new Item(11832, 1), // kodai wand
				new Item(11834, 1), // kodai wand
				new Item(20604, 1), // kodai wand
				new Item(21003, 1), // kodai wand
				new Item(2528, 1), // kodai wand
				new Item(4718, 1), // Dinh bulwark
				new Item(4716, 1), // spectral ss
				new Item(12825, 1), // arcane ss
				new Item(20997, 1), // Twsited Bow
				new Item(13739, 1), // Lava Scythe
				new Item(13740, 1), // Twsited Bow
				new Item(13199, 1), // Lava Scythe
				new Item(13197, 1), // Lava Scythe
				new Item(12399, 1), // Lava Scythe
				
				new Item(12006, 1), // Abyssal Tentacle
				new Item(11832, 1), // Abyssal Tentacle
				new Item(11834, 1), // Abyssal Tentacle
				
				new Item(13239, 1), // Abyssal Tentacle
				new Item(13237, 1), // Abyssal Tentacle
				new Item(13235, 1), // Abyssal Tentacle

				new Item(11771, 1), // Archers ring (i)
				new Item(11773, 1), // Berserker ring (i)
				new Item(11772, 1), // warrior ring (i)
				
				new Item(12006, 1), // Abyssal Tentacle
				new Item(11832, 1), // Abyssal Tentacle
				new Item(11834, 1), // Abyssal Tentacle
				
				new Item(13239, 1), // Abyssal Tentacle
				new Item(13237, 1), // Abyssal Tentacle
				new Item(4720, 1), // arcane ss
				new Item(4722, 1), };
		Item item = items[random.nextInt(items.length)];
		Item item1 = items1[random.nextInt(items1.length)];
		if(Utility.random(10, 150) <= 20) {
		player.inventory.add(item);
		player.send(new SendItemOnInterface(20036, item));
		} else {
			player.inventory.add(item1);
			player.send(new SendItemOnInterface(20036, item1));
		}
		
	}

	/**
	 * Checks if player is in session
	 * 
	 * @return
	 */
	private boolean inSession() {
		if (player.interfaceManager.isInterfaceOpen(20011)) {
			player.send(new SendMessage("You are already in a scratch session!"));
			return true;
		}
		return false;
	}

	/**
	 * Fetches a random card
	 */
	private static Random random = new Random();

	private int getRandom() {
		return ScratchCardData.values()[random.nextInt(ScratchCardData.values().length)].getDisplayId();
	}

	/**
	 * Checks if the player has revealed the bonus prize
	 */
	private static boolean BONUS = false;

	public static void setBonus(boolean BONUS) {
		ScratchCard.BONUS = BONUS;
	}

	/**
	 * Stores the already used cards
	 */
	private static final List<ScratchCardInstanced> COMBINATION_COUNT = new ArrayList<>();

	/**
	 * Stores the randomly generated cards
	 */
	private static final List<ScratchCardCombination> COMBINATION = new ArrayList<>();
}