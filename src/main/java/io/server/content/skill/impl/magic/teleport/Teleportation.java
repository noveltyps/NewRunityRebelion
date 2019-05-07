package io.server.content.skill.impl.magic.teleport;

import java.util.Optional;

import io.server.Config;
import io.server.content.activity.Activity;
import io.server.content.freeforall.FreeForAll;
import io.server.content.freeforall.FreeForAllType;
import io.server.content.freeforall.impl.FreeForAllLeaveTask;
import io.server.content.skill.impl.magic.Spellbook;
import io.server.game.Animation;
import io.server.game.Graphic;
import io.server.game.UpdatePriority;
import io.server.game.action.impl.TeleportAction;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.position.Area;
import io.server.game.world.position.Position;
import io.server.net.packet.out.SendMessage;
import io.server.util.Utility;

/**
 * Handles a player teleporting.
 *
 * @author Daniel
 */
public class Teleportation {

	/** Teleports player to a position. */
	public static boolean teleport(Mob mob, Position position) {
		return teleport(mob, position, 20, () -> {
			/* Empty */ });
	}

	/** Teleports player to a position. */
	public static boolean teleport(Mob mob, Position position, int wildernessLevel, Runnable onDestination) {
		
		 if (FreeForAll.KEY_MAP.containsKey(mob.getPlayer())) {
			new FreeForAllLeaveTask(mob.getPlayer(), 
					FreeForAll.getType(mob.getPlayer()).equals(FreeForAllType.LOBBY) ? "lobby" : "game").execute();
			return true;
		} 
		
		if (mob.isNpc()) {
			teleport(mob, position, TeleportationData.MODERN, onDestination);
			return true;
		}

		Player player = mob.getPlayer();

		if (!player.interfaceManager.isClear()) 
			player.interfaceManager.close(false);

		
		if (Activity.evaluate(player, it -> !it.canTeleport(player))) 
			return false;

		if (player.wilderness > wildernessLevel && !PlayerRight.isPriviledged(player)) {
			player.send(new SendMessage("You can't teleport past " + wildernessLevel + " wilderness!"));
			return false;
		}

		if (player.isTeleblocked()) {
			player.message("You are currently under the affects of a teleblock spell and can not teleport!");
			return false;
		}

		if (Area.inWilderness(position) && player.pet != null) {
			player.dialogueFactory
					.sendNpcChat(player.pet.id, "I'm sorry #name,", "but I can not enter the wilderness with you!")
					.execute();
			return false;
		}
        boolean wilderness = Area.inWilderness(position);

        if (wilderness && player.playTime < 3000) {
            player.message("You cannot enter the wilderness until you have 30 minutes of playtime. " + Utility.getTime((3000 - player.playTime) * 3 / 5) + " minutes remaining.");
            return false;
        }

		TeleportationData type = TeleportationData.MODERN;

		if (position.equals(Config.DONATOR_ZONE) || position.equals(Config.STAFF_ZONE)) {
			type = TeleportationData.DONATOR;
		} else if (position.equals(Config.DEFAULT_POSITION) && !player.getCombat().inCombat()) {
			type = TeleportationData.HOME;
		} else if (player.spellbook == Spellbook.ANCIENT) {
			type = TeleportationData.ANCIENT;
		}

		if (player.action.getCurrentAction() != null) {
			player.action.getCurrentAction().cancel();
		}
		TeleportationData finalType = type;
		if (wilderness) {
			player.dialogueFactory.sendStatement("Are you sure you want to teleport into the wilderness?");
			player.dialogueFactory.sendOption("Yes (DANGEROUS)", () -> {
				teleport(player, position, finalType, onDestination);
			}, "No thanks, I should bank first.", () -> player.dialogueFactory.clear());
			player.dialogueFactory.execute();
		} else
			teleport(player, position, type, onDestination);
		//kk should be good
		return true;
	}

	/** Teleports player using a certain data type. */
	public static boolean teleportNoChecks(Mob mob, Position position, TeleportationData type) {
		return teleport(mob, position, type, () -> {
			/* Empty */ });
	}

	/** Teleports player using a certain data type. */
	public static boolean teleport(Mob mob, Position position, TeleportationData type, Runnable onDestination) {
		
		if (mob.getPlayer().getDynamicRegion() != null && mob.getPlayer().getDynamicRegion().getHandler() != null) {
			if (!mob.getPlayer().getDynamicRegion().getHandler().allowTeleportation(mob.getPlayer()))
				return true;
		}
		if (type != TeleportationData.HOME)
			mob.getCombat().reset();
		
		mob.action.execute(new TeleportAction(mob, position, type, onDestination), true);
		return true;
	}

	/** Holds all the teleportation data. */
	public enum TeleportationData {
		TABLET(2, new Animation(4069), new Graphic(678, 5, false), new Animation(4731)),
		MODERN(3, new Animation(714), new Graphic(308, 43, true)),
		OBELISK(3, new Animation(1816), new Graphic(308, 43, true), new Animation(6304)),
		ANCIENT(3, new Animation(1979), new Graphic(392, false)),
		HOME(3, new Animation(714), new Graphic(308, 43, true)),
		LEVER(3, new Animation(714), new Graphic(308, 43, true)),
		DONATOR(4, new Animation(6999), new Graphic(284), new Animation(65535, UpdatePriority.VERY_HIGH)),
		CREVICE(2, new Animation(6301), new Graphic(571, false));

		private final int delay;
		private final Optional<Animation> startAnimation;
		private final Optional<Graphic> startGraphic;
		private final Optional<Animation> middleAnimation;
		private final Optional<Graphic> middleGraphic;
		private final Optional<Animation> endAnimation;
		private final Optional<Graphic> endGraphic;
		private final boolean lockMovement;

		TeleportationData(int delay, Animation startAnimation, Animation middleAnimation, Graphic middleGraphic) {
			this(delay, startAnimation, null, middleAnimation, middleGraphic, null, null, true);
		}

		TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation) {
			this(delay, startAnimation, startGraphic, endAnimation, null, true);
		}

		TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, boolean lockMovement) {
			this(delay, startAnimation, startGraphic, null, null, lockMovement);
		}

		TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation,
				boolean lockMovement) {
			this(delay, startAnimation, startGraphic, endAnimation, null, lockMovement);
		}

		TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation,
				Graphic endGraphic) {
			this(delay, startAnimation, startGraphic, endAnimation, endGraphic, true);
		}

		TeleportationData(int delay, Animation startAnimation, Graphic startGraphic) {
			this(delay, startAnimation, startGraphic, null, null, true);
		}

		TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation endAnimation,
				Graphic endGraphic, boolean lockMovement) {
			this(delay, startAnimation, startGraphic, null, null, endAnimation, endGraphic, lockMovement);
		}

		TeleportationData(int delay, Animation startAnimation, Graphic startGraphic, Animation middleAnimation,
				Graphic middleGraphic, Animation endAnimation, Graphic endGraphic, boolean lockMovement) {
			this.delay = delay;
			this.startAnimation = Optional.ofNullable(startAnimation);
			this.startGraphic = Optional.ofNullable(startGraphic);
			this.middleAnimation = Optional.ofNullable(middleAnimation);
			this.middleGraphic = Optional.ofNullable(middleGraphic);
			this.endAnimation = Optional.ofNullable(endAnimation);
			this.endGraphic = Optional.ofNullable(endGraphic);
			this.lockMovement = lockMovement;
		}

		public int getDelay() {
			return delay;
		}

		public Optional<Animation> getStartAnimation() {
			return startAnimation;
		}

		public Optional<Graphic> getStartGraphic() {
			return startGraphic;
		}

		public Optional<Animation> getMiddleAnimation() {
			return middleAnimation;
		}

		public Optional<Graphic> getMiddleGraphic() {
			return middleGraphic;
		}

		public Optional<Animation> getEndAnimation() {
			return endAnimation;
		}

		public Optional<Graphic> getEndGraphic() {
			return endGraphic;
		}

		public boolean lockMovement() {
			return lockMovement;
		}
	}
}
