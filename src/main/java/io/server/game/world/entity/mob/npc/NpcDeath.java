package io.server.game.world.entity.mob.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import io.server.content.ActivityLog;
import io.server.content.achievement.AchievementHandler;
import io.server.content.achievement.AchievementKey;
import io.server.content.activity.impl.warriorguild.WarriorGuildUtility;
import io.server.content.clanchannel.content.ClanTaskKey;
import io.server.content.event.EventDispatcher;
import io.server.content.event.impl.OnKillEvent;
import io.server.game.Animation;
import io.server.game.action.Action;
import io.server.game.UpdatePriority;
import io.server.game.world.World;
import io.server.game.world.entity.combat.strategy.npc.boss.arena.ArenaUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.chimera.ChimeraDrops;
import io.server.game.world.entity.combat.strategy.npc.boss.galvek.GalvekUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.magearena.PorazdirUtility;
import io.server.game.world.entity.combat.strategy.npc.boss.skotizo.SkotizoUtility;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.MobDeath;
import io.server.game.world.entity.mob.npc.drop.NpcDropManager;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.items.Item;
import io.server.game.world.items.ground.GroundItem;
import io.server.game.world.position.Area;
import io.server.game.world.region.RegionManager;
import io.server.util.Utility;

/**
 * Handles a npc dying. (Used mostly for custom statements).
 *
 * @author Daniel
 * @author Adam_#6723
 * @author <a href=
 *         "http://www.rune-server.org/members/bitshifting/">Bitshifting</a>
 */
public final class NpcDeath extends MobDeath<Npc> {
	private Runnable runnable;

	/** Creates a new {@link MobDeath}. */
	public NpcDeath(Npc mob) {
		this(mob, () -> {
		});
	}

	public NpcDeath(Npc mob, Runnable runnable) {
		super(mob, mob.getDeathTime());
		this.runnable = runnable;
	}

	@Override
	public void preDeath(Mob killer) {
		if (mob.definition == null) {
			return;
		}

		mob.animate(new Animation(mob.definition.getDeathAnimation(), UpdatePriority.VERY_HIGH));
	}

	@Override
	public void death() {
		if (mob.owner != null || mob.definition == null || mob.definition.getRespawnTime() == -1) {
			mob.unregister();
			return;
		}

		mob.setVisible(false);
		World.schedule(mob.definition.getRespawnTime(), () -> {
			mob.move(mob.spawnPosition);
			mob.npcAssistant.respawn();
		});
	}

	@Override
	public void postDeath(Mob killer) {
		if (killer == null)
			return;

		/* Npc name. */
		String name = mob.getName().toUpperCase().replace(" ", "_");
		
		if (killer.getPlayer().getDynamicRegion() != null)
			killer.getPlayer().getDynamicRegion().getHandler().onNPCDeath(mob);

		runnable.run();

		switch (killer.getType()) {
		case PLAYER:
			Player playerKiller = killer.getPlayer();

				NpcDropManager.drop(playerKiller, mob);

			/* The slayer kill activator. */
			playerKiller.slayer.activate(mob, 1, false);

			/* The followers. */
			if (playerKiller.followers.contains(mob.getNpc())) {
				playerKiller.followers.remove(mob);
			}

			if (playerKiller.getBossPoints() >= 5000) {
				AchievementHandler.activate(playerKiller, AchievementKey.BOSSPOINT, 1);
			}
			/* Activity. */
			EventDispatcher.execute(playerKiller, new OnKillEvent(mob));

			/* Warrior Guild */
			if (Arrays.stream(WarriorGuildUtility.CYCLOPS).anyMatch(cyclop -> cyclop == mob.id)
					&& (Utility.random(20) == 0 || PlayerRight.isPriviledged(playerKiller))) {
				Item defender = new Item(WarriorGuildUtility.getDefender(playerKiller), 1);
				GroundItem.create(playerKiller, defender, mob.getPosition());
				return;
			}

			/* Switch statement. */
			
			
			switch (name) {
			case "FIERY_BEAST":
				playerKiller.activityLogger.add(ActivityLog.FIERY_BEAST_KILL);
			
			return;
			case "LAVA_GROUDON":
				playerKiller.activityLogger.add(ActivityLog.LAVA_GROUDON_KILL);
			
			return;
			case "GODZILLA":
				playerKiller.activityLogger.add(ActivityLog.GODZILLA_KILL);
			
			return;
			case "ARCANE_BOSS":
				playerKiller.activityLogger.add(ActivityLog.ARCANE_BOSS_KILL);
			
			return;
			
			case "SKOTIZO":
				SkotizoUtility.defeated(mob, playerKiller);
				playerKiller.activityLogger.add(ActivityLog.SKOTIZO);

				AchievementHandler.activate(playerKiller, AchievementKey.KILL_SKOTIZO, 1);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "GALVEK":
				if(Area.inAllVsOne(playerKiller) || Area.inAllVsOne(mob)) {
					System.out.println("DIDN'T EXECUTE BECAUSE " + playerKiller.getName() + " is in an instanced minigame.");
					return;
				}
				GalvekUtility.defeated(mob, playerKiller);
				playerKiller.activityLogger.add(ActivityLog.GALVEK);

				AchievementHandler.activate(playerKiller, AchievementKey.KILL_GALVEK, 1);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "JUSTICAR":
				playerKiller.activityLogger.add(ActivityLog.JUSTICAR);

				return;

			case "GLOD":
				ArenaUtility.defeated(mob, playerKiller);
				playerKiller.activityLogger.add(ActivityLog.GLOD);

				AchievementHandler.activate(playerKiller, AchievementKey.KILL_GLOD, 1);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "ZULRAH":
				playerKiller.activityLogger.add(ActivityLog.ZULRAH);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "CHIMERA":
				ChimeraDrops.defeated(mob, playerKiller);
				playerKiller.activityLogger.add(ActivityLog.CHIMERA);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
			case "VORKATH":
				playerKiller.activityLogger.add(ActivityLog.VORKATH);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
				
				
			case "MUTANT_TARN":
				playerKiller.activityLogger.add(ActivityLog.MUTANT_TARN);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "CERBERUS":
				playerKiller.activityLogger.add(ActivityLog.CERBERUS);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "GENERAL_GRAARDOR":
				playerKiller.activityLogger.add(ActivityLog.GENERAL_GRAARDOR);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "LAVA_DRAGON":
				playerKiller.activityLogger.add(ActivityLog.LAVA_DRAGON);
				AchievementHandler.activate(playerKiller, AchievementKey.KILL_LAVA_DRAGON, 1);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "KRAKEN":
				playerKiller.activityLogger.add(ActivityLog.KRAKEN);
				AchievementHandler.activate(playerKiller, AchievementKey.KILL_KRAKEN, 1);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "COMMANDER_ZILYANA":
				playerKiller.activityLogger.add(ActivityLog.COMMANDER_ZILYANA);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "KREE'ARRA":
				playerKiller.activityLogger.add(ActivityLog.KREE_ARRA);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "K'RIL TSUTSAROTH":
				playerKiller.activityLogger.add(ActivityLog.KRIL_TSUTSAROTH);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
				
				
			case "PORAZDIR":
				PorazdirUtility.defeated(mob, playerKiller);
				playerKiller.activityLogger.add(ActivityLog.PORAZDIR);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "CORPOREAL_BEAST":
				playerKiller.activityLogger.add(ActivityLog.CORPOREAL_BEAST);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "GIANT_ROC":
				playerKiller.activityLogger.add(ActivityLog.GIANT_ROC);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
				
				
			case "DAGANNOTH_SUPREME":
				playerKiller.activityLogger.add(ActivityLog.DAGGANOTH_SUPREME);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
				
			case "DAGANNOTH_PRIME":
				playerKiller.activityLogger.add(ActivityLog.DAGGANOTH_PRIME);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
				
			case "DAGANNOTH_REX":
				playerKiller.activityLogger.add(ActivityLog.DAGGANOTH_REX);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
				
				
				
			case "GIANT_MOLE":
				playerKiller.activityLogger.add(ActivityLog.GIANT_MOLE);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
					System.out.println("Here.... 1");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
					System.out.println("Here.... 2");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
					System.out.println("Here.... 3");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
					System.out.println("Here.... 4");

				}
				return;


			case "LIZARDMAN_SHAMAN":
				playerKiller.activityLogger.add(ActivityLog.LIZARDMAN_SHAMAN);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
			case "MITHRIL_DRAGON":
				playerKiller.activityLogger.add(ActivityLog.MITHRIL_DRAGON);
				return;

			case "DARK_BEAST":
				playerKiller.activityLogger.add(ActivityLog.DARK_BEAST);

				return;
				
			case "ICE_DEMON":
				playerKiller.activityLogger.add(ActivityLog.ICE_DEMON);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "MAN":
				playerKiller.activityLogger.add(ActivityLog.MAN);
				AchievementHandler.activate(playerKiller, AchievementKey.KILL_A_MAN, 1);

				return;

			case "GRIZZLY_BEAR":
				AchievementHandler.activate(playerKiller, AchievementKey.GRIZZLY_BEAR, 1);
				return;

			case "ANGRY_BARBARIAN_SPIRIT":
				GroundItem.create(playerKiller, new Item(2404));
				return;

			case "HILL_GIANT":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.HILL_GIANT, playerKiller.getName()));
				return;

			case "BLACK_DEMON":
			case "BLACK_DEMONS":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_DEMON, playerKiller.getName()));
				return;

			case "GREATER_DEMON":
			case "GREATER_DEMONS":
				playerKiller
						.forClan(channel -> channel.activateTask(ClanTaskKey.GREATER_DEMON, playerKiller.getName()));
				return;

			case "ROCK_CRAB":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.ROCK_CRAB, playerKiller.getName()));
				return;

			case "SAND_CRAB":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SAND_CRAB, playerKiller.getName()));
				return;

			case "BLUE_DRAGON":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLUE_DRAGON, playerKiller.getName()));
				return;

			case "RED_DRAGON":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.RED_DRAGON, playerKiller.getName()));
				return;

			case "GREEN_DRAGON":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.GREEN_DRAGON, playerKiller.getName()));
				return;

			case "BLACK_DRAGON":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_DRAGON, playerKiller.getName()));
				return;

			case "KING_BLACK_DRAGON":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.BLACK_DRAGON, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.KING_BLACK_DRAGON);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
			case "CHAOS_ELEMENTAL":
				playerKiller
						.forClan(channel -> channel.activateTask(ClanTaskKey.CHAOS_ELEMENTAL, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.CHAOS_ELEMENTAL);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "CHAOS_FANATIC":
				playerKiller
						.forClan(channel -> channel.activateTask(ClanTaskKey.CHAOS_FANATIC, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.CHAOS_FANATIC);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "CRAZY_ARCHAEOLOGIST":
				playerKiller.forClan(
						channel -> channel.activateTask(ClanTaskKey.CRAZY_ARCHAEOLOGIST, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.CRAZY_ARCHAEOLOGIST);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "CALLISTO":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.CALLISTO, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.CALLISTO);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
			case "SCORPIA":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.SCORPIA, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.SCORPIA);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "VET'ION":
				playerKiller.activityLogger.add(ActivityLog.VETION);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;
				
			case "VET'ION REBORN":
				playerKiller.activityLogger.add(ActivityLog.VETION);

				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 2);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "VENANTIS":
				playerKiller.forClan(channel -> channel.activateTask(ClanTaskKey.VENNANTIS, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.VENANTIS);
				if (PlayerRight.isDonator(playerKiller) || PlayerRight.isSuper(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
                return;
				}

				if (PlayerRight.isExtreme(playerKiller) || PlayerRight.isElite(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 3);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				if (PlayerRight.isKing(playerKiller) || PlayerRight.isSupreme(playerKiller)) {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 4);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");
	                return;
				}
				else {
					playerKiller.setBossPoints(playerKiller.getBossPoints() + 1);
					playerKiller.message("<img=2>You now have @red@" + playerKiller.getBossPoints() + " Boss Points!");

				}
				return;

			case "SKELETAL_WYVERN":
				playerKiller
						.forClan(channel -> channel.activateTask(ClanTaskKey.SKELETAL_WYVERN, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.SKELETAL_WYVERN);
				return;

			case "ABYSSAL_DEMON":
				playerKiller
						.forClan(channel -> channel.activateTask(ClanTaskKey.ABYSSAL_DEMON, playerKiller.getName()));
				playerKiller.activityLogger.add(ActivityLog.ABYSSAL_DEMON);
				return;
			}
			break;
		case NPC:
//      Npc npcKiller = killer.getNpc();
			break;
		default:
			break;
		}
	}
}
