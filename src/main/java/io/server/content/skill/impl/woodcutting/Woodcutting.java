package io.server.content.skill.impl.woodcutting;

import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.runeliteplugin.RuneliteSkillingOverlay;
import io.server.content.skill.SkillRepository;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendMessage;
import io.server.util.StringUtils;

/**
 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
 * @since 24-12-2016.
 */
public final class Woodcutting extends Skill {

	/**
	 * Constructs a new {@link Woodcutting} skill.
	 */
	public Woodcutting(int level, double experience) {
		super(Skill.WOODCUTTING, level, experience);
	}

	public static boolean success(Player player, TreeData tree, AxeData axe) {
		return SkillRepository.isSuccess(player, Skill.WOODCUTTING, tree.levelRequired, axe.level);
	}
	
	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		TreeData tree = TreeData.forId(event.getObject().getId());

		if (tree == null) {
			return false;
		}

		AxeData axe = AxeData.getDefinition(player).orElse(null);

		if (event.getType() == ObjectInteractionEvent.InteractionType.SECOND_CLICK_OBJECT && tree.tree[0] == 2092) // AFK
																													// TREE
		{
			Integer left = event.getObject().getGenericAttributes().get("logs", Integer.class);
			if (left == null) {
				player.send(new SendMessage("The AFK tree has already been fully replenished!"));
			} else {
				player.send(new SendMessage("You have successfully replenished the tree!"));
				event.getObject().getGenericAttributes().set("logs", 20000);
			}
		}

		if (event.getType() == ObjectInteractionEvent.InteractionType.THIRD_CLICK_OBJECT && tree.tree[0] == 2092) // AFK
																													// TREE
		{
			Integer left = event.getObject().getGenericAttributes().get("logs", Integer.class);
			if (left == null) {
				player.send(new SendMessage("The AFK tree is fully replenished!"));
			} else {
				if (left > 1000) {
					player.send(new SendMessage("The AFK tree has " + left / 1000 + ","
							+ (left.toString().substring(left.toString().length() - 3, left.toString().length()))
							+ " logs left.."));
				} else {
					player.send(new SendMessage("The AFK tree has " + left + " logs left.."));
				}
			}
		}

		if (axe == null) {
			if (event.getType() == ObjectInteractionEvent.InteractionType.FIRST_CLICK_OBJECT)
				player.send(new SendMessage("You don't have a hatchet."));
			return true;
		}

		if (!player.skills.getSkills()[Skill.WOODCUTTING].reqLevel(axe.level)) {
			if (event.getType() == ObjectInteractionEvent.InteractionType.FIRST_CLICK_OBJECT)
				player.send(new SendMessage("You need a level of " + axe.level + " to use this hatchet!"));
			return true;
		}

		if (!player.skills.getSkills()[Skill.WOODCUTTING].reqLevel(tree.levelRequired)) {
			if (event.getType() == ObjectInteractionEvent.InteractionType.FIRST_CLICK_OBJECT)
				player.send(new SendMessage("You need a level of " + tree.levelRequired + " to cut "
						+ StringUtils.appendIndefiniteArticle(event.getObject().getDefinition().name) + "."));
			return true;
		}

		if (!player.inventory.hasCapacityFor(new Item(tree.item))) {
			if (event.getType() == ObjectInteractionEvent.InteractionType.FIRST_CLICK_OBJECT)
				player.message("You do not have enough inventory spaces to do this!");
			return true;
		}

		player.action.execute(new WoodcuttingAction(player, event.getObject(), tree, axe));
		player.skills.get(Skill.WOODCUTTING).setDoingSkill(true);
		if (event.getType() == ObjectInteractionEvent.InteractionType.FIRST_CLICK_OBJECT)
			player.message("You swing your axe at the tree...");
		new RuneliteSkillingOverlay(player).onOpen(player.random, "Woodcutting", "Logs");
		return true;
	}
}
