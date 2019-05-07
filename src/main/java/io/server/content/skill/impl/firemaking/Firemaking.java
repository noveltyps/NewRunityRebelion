package io.server.content.skill.impl.firemaking;

import io.server.Config;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.content.activity.randomevent.RandomEventHandler;
import io.server.content.clanchannel.content.ClanTaskKey;
import io.server.content.event.impl.ItemOnItemInteractionEvent;
import io.server.content.event.impl.ObjectInteractionEvent;
import io.server.content.experiencerate.ExperienceModifier;
import io.server.content.prestige.PrestigePerk;
import io.server.game.action.Action;
import io.server.game.action.policy.WalkablePolicy;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.object.GameObject;
import io.server.util.RandomUtils;

/**
 * The firemaking skill.
 *
 * @author Daniel
 */
public class Firemaking extends Skill {

	public Firemaking(int level, double experience) {
		super(Skill.FIREMAKING, level, experience);
	}

	@Override
	protected boolean useItem(Player player, ItemOnItemInteractionEvent event) {
		Item first = event.getFirst();
		Item second = event.getSecond();
		FiremakingData firemaking = null;
		
		Item item = null;
		if (!FiremakingData.forId(second.getId()).isPresent() && !FiremakingData.forId(first.getId()).isPresent())
			return false;
		if (first.getId() == 590) {
			firemaking = FiremakingData.forId(second.getId()).get();
			item = second;
		} else if (second.getId() == 590) {
			firemaking = FiremakingData.forId(first.getId()).get();
			item = first;
		}
		if (firemaking == null)
			return false;
		player.action.execute(new FiremakingAction(player, item, firemaking), true);
		return true;
	}

	@Override
	protected boolean clickObject(Player player, ObjectInteractionEvent event) {
		if (event.getOpcode() != 0 || event.getObject().getId() != 5249) {
			return false;
		}

		if (player.getPosition().equals(event.getObject().getPosition())) {
			player.message("Please step out of the fire!");
			return true;
		}

		FiremakingData firemaking = null;

		for (Item item : player.inventory) {
			if (item != null && FiremakingData.forId(item.getId()).isPresent()) {
				firemaking = FiremakingData.forId(item.getId()).get();
				break;
			}
		}

		if (firemaking == null) {
			player.dialogueFactory.sendStatement("You have no logs in your inventory to add to this fire!").execute();
			return true;
		}

		if (player.skills.getMaxLevel(Skill.FIREMAKING) < firemaking.getLevel()) {
			player.message("You need a firemaking level of " + firemaking.getLevel() + " to light this log!");
			return true;
		}

	System.out.println("HERE " + firemaking.getMoney());
		player.action.execute(bonfireAction(player, event.getObject(), firemaking,
		player.inventory.computeAmountForId(firemaking.getLog())));
		return true;
	}

	private Action<Player> bonfireAction(Player player, GameObject object, FiremakingData firemaking, int toBurn) {
		return new Action<Player>(player, 3) {
			int amount = toBurn;

			@Override
			public WalkablePolicy getWalkablePolicy() {
				return WalkablePolicy.NON_WALKABLE;
			}

			@Override
			public String getName() {
				return "Bonfire action";
			}

			@Override
			protected void execute() {
				if(object.getId() != 5249) {
					cancel();
				}
				if (amount <= 0) {
					cancel();
					System.err.println("Returning Here");
					return;
				}
				if (!object.active()) {
					cancel();
					return;
				}
				if (!player.inventory.contains(firemaking.getLog())) {
					player.message("You have no more logs!");
					cancel();
					return;
				}
				player.inventory.add(995, firemaking.getMoney());
				player.inventory.remove(firemaking.getLog(), 1);
				player.animate(733);
				player.skills.addExperience(Skill.FIREMAKING,
						(firemaking.getExperience() * Config.FIREMAKING_MODIFICATION)*
						new ExperienceModifier(player).getModifier());
				RandomEventHandler.trigger(player);
				// TODO HANDLES THE FIRMAKING.

				
				  player.inventory.add(995, firemaking.getCoins()); player.inventory.add(995,
				  firemaking.getMoney()); player.inventory.add(firemaking.getLog(), 1);
				  player.message("you have got " + firemaking.getCoins() + " " +
				  firemaking.getMoney() + " !" + " " + firemaking.getLog());
				 

				if (firemaking == FiremakingData.WILLOW_LOG) {
					
					player.forClan(channel -> channel.activateTask(ClanTaskKey.BURN_WILLOW_LOG, getMob().getName()));
					
					 if(PlayerRight.isDonator(player) || PlayerRight.isSuper(player) ||
							  PlayerRight.isDeveloper(player)) {
							  player.setskillingPoints(player.getskillingPoints() + 2);
							  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
							  " Skilling Points!"); } if(PlayerRight.isExtreme(player) ||
							  PlayerRight.isElite(player)) {
							  player.setskillingPoints(player.getskillingPoints() + 3);
							  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
							  " Skilling Points!"); } if(PlayerRight.isKing(player)) {
							  player.setskillingPoints(player.getskillingPoints() + 4);
							  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
							  " Skilling Points!"); } else {
							  player.setskillingPoints(player.getskillingPoints() + 1);
							  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
							  " Skilling Points!");
							  
							  }
					
					
				}
				if (firemaking == FiremakingData.OAK_LOG) {
					AchievementHandler.activate(getMob(), AchievementKey.BURN_AN_OAK_LOG, 1);
					
					  if(PlayerRight.isDonator(player) || PlayerRight.isSuper(player) ||
					  PlayerRight.isDeveloper(player)) {
					  player.setskillingPoints(player.getskillingPoints() + 2);
					  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
					  " Skilling Points!"); } if(PlayerRight.isExtreme(player) ||
					  PlayerRight.isElite(player)) {
					  player.setskillingPoints(player.getskillingPoints() + 3);
					  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
					  " Skilling Points!"); } if(PlayerRight.isKing(player)) {
					  player.setskillingPoints(player.getskillingPoints() + 4);
					  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
					  " Skilling Points!"); } else {
					  player.setskillingPoints(player.getskillingPoints() + 1);
					  player.message("<img=2>You now have @red@" + player.getskillingPoints() +
					  " Skilling Points!");
					  
					  }
					 

				}
				if (firemaking == FiremakingData.YEW_LOG) {
					AchievementHandler.activate(getMob(), AchievementKey.BURN100YEW, 1);

					if (PlayerRight.isDonator(player) || PlayerRight.isSuper(player)
							|| PlayerRight.isDeveloper(player)) {
						player.setskillingPoints(player.getskillingPoints() + 2);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					}
					if (PlayerRight.isExtreme(player) || PlayerRight.isElite(player)) {
						player.setskillingPoints(player.getskillingPoints() + 3);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					}
					if (PlayerRight.isKing(player)) {
						player.setskillingPoints(player.getskillingPoints() + 4);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					} else {
						player.setskillingPoints(player.getskillingPoints() + 1);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					}
				}
				if (firemaking == FiremakingData.NORMAL_LOG || firemaking == FiremakingData.OAK_LOG
						|| firemaking == FiremakingData.MAPLE_LOG
						|| firemaking == FiremakingData.MAGIC_LOG || firemaking == FiremakingData.ARCTIC_PINE_LOG) {
					AchievementHandler.activate(getMob(), AchievementKey.BURN100ANY, 1);

					if (PlayerRight.isDonator(player) || PlayerRight.isSuper(player)
							|| PlayerRight.isDeveloper(player)) {
						player.setskillingPoints(player.getskillingPoints() + 2);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					}
					if (PlayerRight.isExtreme(player) || PlayerRight.isElite(player)) {
						player.setskillingPoints(player.getskillingPoints() + 3);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					}
					if (PlayerRight.isKing(player)) {
						player.setskillingPoints(player.getskillingPoints() + 4);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					} else {
						player.setskillingPoints(player.getskillingPoints() + 1);
						player.message("<img=2>You now have @red@" + player.getskillingPoints() + " Skilling Points!");
					}
				}

				
				if (player.prestige.hasPerk(PrestigePerk.FLAME_ON) && RandomUtils.success(.25)) {
					player.skills.addExperience(Skill.FIREMAKING,
							(firemaking.getExperience() * Config.FIREMAKING_MODIFICATION)
									* new ExperienceModifier(player).getModifier());
				}
				player.inventory.add(995, firemaking.getMoney());
				player.inventory.remove(new Item(firemaking.getLog(), 1));
				player.animate(733);
				player.skills.addExperience(Skill.FIREMAKING,
						(firemaking.getExperience() * Config.FIREMAKING_MODIFICATION)
								* new ExperienceModifier(player).getModifier());
				RandomEventHandler.trigger(player);
				amount--;
			}

			@Override
			protected void onCancel(boolean logout) {
//                player.resetAnimation();
			}
		};
	}
}
