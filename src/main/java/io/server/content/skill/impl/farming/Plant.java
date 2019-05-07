package io.server.content.skill.impl.farming;

import io.server.game.Animation;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

public class Plant {

	public int patch;
	public int plant;
	public long time = 0;
	public byte stage = 0;
	public byte disease = -1;
	public byte watered = 0;

	private boolean dead = false;

	public byte harvested = 0;
	boolean harvesting = false;

	public Plant(int patchId, int plantId) {
		patch = patchId;
		plant = plantId;
	}

	public boolean doDisease() {
		return false;
	}

	public boolean doWater() {
		return false;
	}

	public void water(Player player, int item) {
		if (item == 5332) {
			return;
		}
		player.locking.lock(2);
		if (getPatch().seedType == SeedType.HERB) {
			player.message("This patch doesn't need watering.");
			return;
		}

		if (isWatered()) {
			player.send(new SendMessage("Your plants have already been watered."));
			return;
		}

		if (item == 5331) {
			player.send(new SendMessage("Your watering can is empty."));
			return;
		}

		player.send(new SendMessage("You water the plant."));
		player.getFarming().nextWateringCan(item);
		player.animate(new Animation(2293));
		watered = -1;
		doConfig(player);
	}

	public void setTime() {
		time = System.currentTimeMillis();
	}

	public void click(Player player, int option) {
		if (option == 1) {
			if (dead)
				player.send(new SendMessage("Oh dear, your plants have died!"));
			else if (isDiseased())
				player.send(new SendMessage("Your plants are diseased!"));
			else if (stage == Plants.values()[plant].stages)
				harvest(player);
			else {
				String s = "Your plants are healthy";
				if (!isWatered() && getPatch().seedType != SeedType.HERB)
					s += " but need some water to survive";
				else
					s += " and are currently growing";
				s += ".";
				player.send(new SendMessage(s));
			}
		} else if ((option == 2) && (stage == Plants.values()[plant].stages))
			player.send(new SendMessage("Your plants are healthy and ready to harvest."));
	}

	public void harvest(final Player player) {
		if (harvesting)
			return;
		if (player.inventory.contains(FarmingPatches.values()[patch].harvestItem)) {
			player.locking.lock(1);
			final Plant instance = this;
			boolean magicWateringCan = player.inventory.contains(18682);
			World.schedule(new Task(3) {
				@Override
				public void execute() {
					if (player.movement.isMoving()) {
						cancel();
						return;
					}
					if (player.inventory.getFreeSlots() == 0) {
						cancel();
						return;
					}
					player.animate(new Animation(2282));
					Item add = null;
					int id = Plants.values()[plant].harvest;
					add = ItemDefinition.get(id).isNoted() ? new Item(id - 1, 1) : new Item(id, 1);
					player.inventory.add(add.getId(), add.getAmount());
					String name = ItemDefinition.get(Plants.values()[plant].harvest).getName();
					if (name.endsWith("s"))
						name = name.substring(0, name.length() - 1);
					player.send(new SendMessage("You harvest " + Utility.getAOrAn(name) + " " + name + "."));
					player.skills.addExperience(Skill.FARMING, (int) Plants.values()[plant].harvestExperience);
					if (harvested == 1 && magicWateringCan && Utility.random(10) == 0) {
						player.send(new SendMessage("You receive a seed back from your Magic watering can."));
						player.inventory.add(Plants.values()[plant].seed, 1);
					}
					harvested++;
					if (player.inventory.getFreeSlots() <= 0) {
						player.dialogueFactory.sendStatement("Your inventory is full.");
						cancel();
						return;
					}
					if (getPatch().seedType == SeedType.FLOWER
							|| harvested >= (magicWateringCan ? 10 : 5) && Utility.random(4) <= 1) {
						player.getFarming().remove(instance);
						cancel();
						return;
					}
				}
			});
		} else {
			String name = ItemDefinition.get(FarmingPatches.values()[patch].harvestItem).getName();
			player.send(
					new SendMessage("You need " + Utility.getAOrAn(name) + " " + name + " to harvest these plants."));
		}
	}

	public boolean useItemOnPlant(final Player player, int item) {
		if (item == 952) {
			player.animate(new Animation(830));
			player.getFarming().remove(this);
			World.schedule(new Task(2) {
				@Override
				public void execute() {
					player.send(new SendMessage("You remove your plants from the plot."));
					player.animate(new Animation(65535));
					cancel();
				}
			});
			return true;
		}
		if (item == 6036) {
			if (dead) {
				player.send(new SendMessage("Your plant is dead!"));
			} else if (isDiseased()) {
				player.send(new SendMessage("You cure the plant."));
				player.animate(new Animation(2288));
				player.inventory.remove(6036, 1);
				disease = -1;
				doConfig(player);
			} else {
				player.send(new SendMessage("Your plant does not need this."));
			}

			return true;
		}
		if ((item >= 5331) && (item <= 5340) || item == 18682) {
			water(player, item);
			return true;
		}

		return false;
	}

	public void process(Player player) {
		if (dead || stage >= Plants.values()[plant].stages) {
			return;
		}
		long elapsed = (System.currentTimeMillis() - time) / 5_000;
		int grow = Plants.values()[plant].minutes;
		if (elapsed >= grow) {
			for (int i = 0; i < elapsed / grow; i++) {
				/*
				 * if (isDiseased()) { /*} else if (!isWatered()) {
				 * player.getPacketSender().sendMessage("You need to water your plant.");
				 */
				if (isWatered() || getPatch().seedType == SeedType.HERB) {
					stage++;
					player.getFarming().doConfig();
					if (stage >= Plants.values()[plant].stages) {
						player.send(new SendMessage("<img=10> @blu@A seed you planted has finished growing!"));
						return;
					}
				}

			}
			setTime();
		}
	}

	public void doConfig(Player player) {
		player.getFarming().doConfig();
	}

	public int getConfig() {
		Plants plants = Plants.values()[plant];
		FarmingPatches patches = FarmingPatches.values()[patch];
		if (plants.type == SeedType.ALLOTMENT && stage == 0 && isWatered()) {
			return (plants.healthy + stage + 64) * patches.mod;
		}
		return (plants.healthy + stage) * patches.mod;
	}

	public FarmingPatches getPatch() {
		return FarmingPatches.values()[patch];
	}

	public boolean isDiseased() {
		return disease > -1;
	}

	public boolean isWatered() {
		return watered == -1;
	}
}
