package io.server.content.skill.impl.runecrafting;

import io.server.Config;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.clanchannel.content.ClanTaskKey;
import io.server.content.event.impl.ItemInteractionEvent;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.pet.PetData;
import io.server.content.pet.Pets;
import io.server.content.skill.impl.magic.teleport.Teleportation;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;
import io.server.net.packet.out.SendMessage;

/**
 * Handles the runecrafting skill.
 *
 * @author Daniel
 */
public class Runecraft extends Skill {

	public Runecraft(int level, double experience) {
		super(Skill.RUNECRAFTING, level, experience);
	}

	@Override
	protected double modifier() {
		return Config.RUNECRAFTING_MODIFICATION;
	}

	@Override
	protected boolean clickItem(Player player, ItemInteractionEvent event) {
		Item item = event.getItem();

		RunecraftPouchData pouch = RunecraftPouchData.forItem(item.getId());

		if (pouch == null) {
			return false;
		}

		int opcode = event.getOpcode();

		switch (opcode) {
		case 0:
			player.runecraftPouch.deposit(pouch);
			break;
		case 1:
			player.runecraftPouch.withdraw(pouch);
			break;
		}
		return true;
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		if (event.getOpcode() != 0) {
			return false;
		}

		GameObject object = event.getObject();

		if (click(player, object)) {
			return true;
		}

		if (!RunecraftData.forId(object.getId()).isPresent()) {
			return false;
		}

		RunecraftData rune = RunecraftData.forId(object.getId()).get();

		if (player.skills.getLevel(Skill.RUNECRAFTING) < rune.getLevel()) {
			player.send(new SendMessage("You need a runecrafting level of " + rune.getLevel() + " to do this!"));
			return true;
		}

		int essence = getEssence(player);

		if (essence == -1) {
			player.send(new SendMessage("You do not have any essence!"));
			return true;
		}

		int amount = player.inventory.computeAmountForId(essence);

		player.animate(new Animation(791));
		player.graphic(new Graphic(186));
		player.inventory.remove(new Item(essence, amount), -1, true);
		player.skills.addExperience(Skill.RUNECRAFTING,
				((rune.getExperience() * amount) * modifier()) * new ExperienceModifier(player).getModifier());
		RandomEventHandler.trigger(player);

		if (rune == RunecraftData.BLOOD) {
			player.forClan(channel -> channel.activateTask(ClanTaskKey.BLOOD_RUNE, player.getName(), amount));
		} else if (rune == RunecraftData.DEATH) {
			player.forClan(channel -> channel.activateTask(ClanTaskKey.DEATH_RUNE, player.getName(), amount));
		}

		World.schedule(1, () -> {
			player.inventory.add(rune.getRunes(), amount * multiplier(player, rune));
			player.inventory.add(995, rune.getMoney());
			Pets.onReward(player, PetData.RIFT_GUARDIAN.getItem(), 297_647);
		});
		return true;
	}

	/** Handles teleporting to various runecrafting altars. */
	private boolean click(Player player, GameObject object) {
		if (!RunecraftTeleport.forId(object.getId()).isPresent()) {
			return false;
		}

		RunecraftTeleport teleport = RunecraftTeleport.forId(object.getId()).get();
		Teleportation.teleport(player, teleport.getPosition());
		return true;
	}

	/** Gets the essence identification from player. */
	private int getEssence(Player player) {
		return player.inventory.contains(1436) ? 1436 : (player.inventory.contains(7936) ? 7936 : -1);
	}

	/** The runecrafting multiplier. */
	private int multiplier(Player player, RunecraftData rune) {
		int multiplier = 0;
		for (int i = 1; i < rune.getMultiplier().length; i++) {
			if (player.skills.getLevel(Skill.RUNECRAFTING) >= rune.getMultiplier()[i]) {
				multiplier = i;
			}
		}
		return multiplier + 1;
	}
}
