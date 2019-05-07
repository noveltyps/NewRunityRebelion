package io.server.content.skill.impl.prayer;

import io.server.Config;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.dialogue.ChatBoxItemDialogue;
import io.server.content.event.impl.ItemInteractionEvent;
import io.server.content.event.impl.ItemOnObjectInteractionEvent;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.action.Action;
import io.server.game.action.impl.BuryBoneAction;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.object.GameObject;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendMessage;

/**
 * Handles sacrificing bones to an altar.
 *
 * @author Daniel
 */
public class BoneSacrifice extends Skill {

	public BoneSacrifice(int level, double experience) {
		super(Skill.PRAYER, level, experience);
	}

	@Override
	protected double modifier() {
		return Config.PRAYER_MODIFICATION;
	}

	@Override
	protected boolean clickItem(Player player, ItemInteractionEvent event) {
		if (event.getOpcode() != 0)
			return false;
		Item item = event.getItem();
		int slot = event.getSlot();
		if (!BoneData.forId(item.getId()).isPresent())
			return false;
		BoneData bone = BoneData.forId(item.getId()).get();
		new BuryBoneAction(player, bone, slot).start();
		return true;
	}

	@Override
	protected boolean useItem(Player player, ItemOnObjectInteractionEvent event) {
		Item item = event.getItem();
		GameObject object = event.getObject();
		if (object.getId() != 409)
			return false;
		if (!BoneData.forId(item.getId()).isPresent())
			return false;
		BoneData bone = BoneData.forId(item.getId()).get();

		if (player.inventory.computeAmountForId(bone.getId()) == 0) {
			player.action.execute(sacrifice(player, object.getPosition(), bone, 1), true);
		} else {
			ChatBoxItemDialogue.sendInterface(player, 1746, bone.getId(), 0, -5, 170);
			player.chatBoxItemDialogue = new ChatBoxItemDialogue(player) {
				@Override
				public void firstOption(Player player) {
					player.action.execute(sacrifice(player, object.getPosition(), bone, 1), true);
				}

				@Override
				public void secondOption(Player player) {
					player.action.execute(sacrifice(player, object.getPosition(), bone, 5), true);
				}

				@Override
				public void thirdOption(Player player) {
					player.send(
							new SendInputAmount("Enter the amount of bones you would like to sacrifice", 10,
									input -> player.action.execute(
											sacrifice(player, object.getPosition(), bone, Integer.parseInt(input)),
											true)));
				}

				@Override
				public void fourthOption(Player player) {
					player.action.execute(sacrifice(player, object.getPosition(), bone, 28), true);
				}
			};
		}
		return true;
	}

	private Action<Player> sacrifice(Player player, Position position, BoneData bone, int amount) {
		return new Action<Player>(player, 4, true) {
			int ticks = 0;

			@Override
			public void execute() {
				if (!player.inventory.contains(bone.getId())) {
					cancel();
					return;
				}
				Graphic graphic = new Graphic(624, true);
				World.sendGraphic(graphic, position);
				player.animate(new Animation(645, 5));
				player.inventory.remove(bone.getId(), 1);
				player.skills.addExperience(Skill.PRAYER,
						(bone.getExperience() * (modifier() * 1.8)) * new ExperienceModifier(player).getModifier());
				player.send(new SendMessage("You sacrifice the " + ItemDefinition.get(bone.getId()).getName() + " ."));
				RandomEventHandler.trigger(player);
				if (++ticks == amount) {
					cancel();
					return;
				}
			}

			@Override
			public String getName() {
				return "Bone sacrifice";
			}

			@Override
			public boolean prioritized() {
				return false;
			}

			@Override
			public WalkablePolicy getWalkablePolicy() {
				return WalkablePolicy.NON_WALKABLE;
			}
		};
	}

}
