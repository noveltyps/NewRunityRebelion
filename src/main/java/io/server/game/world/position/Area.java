package io.server.game.world.position;

import static io.server.content.activity.impl.battlerealm.BattleRealmCallers.getAreas;

import com.google.common.collect.ImmutableList;

import io.server.game.world.Interactable;
import io.server.game.world.entity.Entity;
import io.server.game.world.entity.mob.Mob;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.position.impl.SquareArea;

/**
 * Handles checking if mobs are in a certain area.
 *
 * @author Adam_#6723 
 */
public abstract class Area {

	/** The General Graardor room. */
	public static final Area GRAARDOR_ROOM = new SquareArea("General Graardor room", 2864, 5351, 2876, 5369, 2);

	/** The General Graardor room. */
	public static final Area RFD_MINIGAME = new SquareArea("RFD Minigame", 1889, 5345, 1910, 5366, 2);

	public static final Area BOSS_ARENA1 = new SquareArea("Arena Zone", 2257, 5309, 2285, 5370);

	/** The General drop catcher quest room. */
	// public static final Area Dropcatcher_MINIGAME = new SquareArea("RFD
	// Minigame", 1889, 5345, 1910, 5366, 2);

	/** The collection of areas that resemble the wilderness resource area. */
	public static final ImmutableList<Area> WILDERNESS_RESOURCE = ImmutableList
			.of(new SquareArea("Wilderness Resource", 3174, 3924, 3196, 3944));

	/** The collection of areas that resemble the zulrah area. */
	private static final ImmutableList<Area> ZULRAH = ImmutableList
			.of(new SquareArea("Zulrah", 2235, 3050, 2288, 3083));

	/** The collection of areas that resemble the king black dragon area. */
	private static final ImmutableList<Area> KBD = ImmutableList
			.of(new SquareArea("King Black Dragon lair", 2256, 4680, 2287, 4711));

	/** The collection of areas that resemble the kraken area. */
	private static final ImmutableList<Area> KRAKEN = ImmutableList
			.of(new SquareArea("Kraken Cave", 2268, 10022, 2292, 10046));

	private static final ImmutableList<Area> SCHOOL = ImmutableList
			.of(new SquareArea("School Area", 1730, 5058, 1790, 5112, 2));

	private static final ImmutableList<Area> PEST_CONTROL_GAME = ImmutableList
			.of(new SquareArea("Pest Control Game", 2622, 2558, 2693, 2627));
	
	private static final ImmutableList<Area> BOSS_CONTROL_GAME = ImmutableList
			.of(new SquareArea("Boss Control Game", 2622, 2558, 2693, 2627));

	private static final ImmutableList<Area> EVENT_ARENA = ImmutableList
			.of(new SquareArea("Event Arena", 3075, 3506, 3082, 3513));

	public static boolean inEventArena(Interactable entity) {
		/*for (Area zone : EVENT_ARENA) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}*/
		return false;
	}

	public static final ImmutableList<Area> BOSS_ARENA = ImmutableList
			.of(new SquareArea("Arena Zone", 2257, 5309, 2285, 5370));
	
	public static final ImmutableList<Area> BOSS_ARENA_1 = ImmutableList
			.of(new SquareArea("Porazdir Zone", 2561, 3201, 2624, 3136));
	
	public static final ImmutableList<Area> INFERNO = ImmutableList
			.of(new SquareArea("Arena Zone", 2243, 5372, 2300, 5314));

	public static final ImmutableList<Area> CHIMERA_ARENA = ImmutableList
			.of(new SquareArea("Chimera Zone", 2363, 4738, 2433, 4667));

	private static final ImmutableList<Area> TOURNAMENT_ARENA = ImmutableList
			.of(new SquareArea("Tournament Arena", 3317, 3268, 4987, 4932));

	private static final ImmutableList<Area> BARROWS = ImmutableList.of(
			new SquareArea("Barrows", 3533, 3261, 3585, 3328),
			new SquareArea("Barrows Underground", 3523, 9672, 3591, 9725, 3));

	/** The collection of areas that resemble the duel arena. */
	private static final ImmutableList<Area> DUEL_ARENA_LOBBY = ImmutableList.of(
			new SquareArea("Lobby", 3355, 3262, 3379, 3279), new SquareArea("Lobby Altar", 3374, 3280, 3379, 3286),
			new SquareArea("Lobby east bank", 3380, 3263, 3384, 3273));

	/** CERB AREA **/
	private static final ImmutableList<Area> CERBERUS = ImmutableList
			.of(new SquareArea("Cerberus Lair", 1216, 1216, 1278, 1279));


	public static boolean inCerberus(Interactable entity) {
		for (Area zone : CERBERUS) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	/** The collection of areas that resemble multi-combat zones. */
	private static final ImmutableList<Area> MULTI_COMBAT_ZONES = ImmutableList.of(
			new SquareArea("Lizard shamans", 1408, 3692, 1457, 3729),
			new SquareArea("Al-kahrid Warrior", 3281, 3158, 3304, 3178),
			new SquareArea("Rock Crab", 2660, 3711, 2743, 3739), new SquareArea("Arena Zone", 2257, 5309, 2285, 5370),
			new SquareArea("Wilderness Resource", 3174, 3924, 3196, 3944),
			new SquareArea("Start of Varrock Wilderness", 3134, 3525, 3327, 3607),
			new SquareArea("North of GE, near gravestones", 3190, 3648, 3327, 3839),
			new SquareArea("Near Chaos Elemental", 3200, 3840, 3390, 3967),
			new SquareArea("Near wilderness agility course", 2992, 3912, 3007, 3967),
			new SquareArea("West wilderness altar", 2946, 3816, 2959, 3831),
			new SquareArea("Deep wilderness 1", 3008, 3856, 3199, 3903),
			new SquareArea("Near wilderness castle", 3008, 3600, 3071, 3711),
			new SquareArea("North of varrock lumbermill", 3072, 3608, 3327, 3647),
			new SquareArea("Pest control", 2624, 2550, 2690, 2619),
			new SquareArea("Fight caves", 2371, 5062, 2422, 5117),
			new SquareArea("Fight arena", 2896, 3595, 2927, 3630),
			new SquareArea("All vs One", 3129, 4991, 3201, 4923),
			new SquareArea("Scorpia lair", 3218, 10329, 3248, 10353),
			new SquareArea("Corporeal Beast lair", 2964, 4360, 3001, 4399),
			new SquareArea("Vorkath", 2261, 4054, 2289, 4048),
			new SquareArea("Revenant Cave", 3124, 10240, 3270, 10046),
			new SquareArea("Inferno", 2243, 5372, 2300, 5314),
			new SquareArea("All Vs One V3", 2496, 4990, 2623, 4919),
			new SquareArea("All Vs One V4", 3009, 5250, 3041, 5216),
			new SquareArea("Kurask's Lair", 3045, 5494, 3073, 5470),
			new SquareArea("Dagannoth lair", 2890, 4425, 2942, 4466),
			new SquareArea("Raids2", 3154, 4299, 3181, 4322),
			new SquareArea("Raids1", 3335, 5253, 3351, 5276),
			new SquareArea("Raids3", 3344, 3076, 3274, 3122),
			new SquareArea("Raids4", 2741, 9314, 2782, 9343),
			new SquareArea("Raids5", 1859, 5387, 1918, 5427),
			new SquareArea("ZombieRaid", 2757, 5128, 2779, 5147),
			new SquareArea("dzone1", 1987, 4504, 2002, 4484),
			new SquareArea("dzone2", 1988, 4540, 2002, 4519),
			new SquareArea("Ezone", 3290, 4775, 3320, 4798),
			new SquareArea("Ezone2", 3293, 4773, 3290, 4798),
			new SquareArea("Porazdir Zone", 2561, 3201, 2624, 3136));
	
			// (3335, 5253, 0), new Position(3351, 5276, 0));
		

	private static final ImmutableList<SquareArea> DUEL_ARENAS = ImmutableList
			.of(new SquareArea("Arena", 3327, 3203, 3389, 3290));
	

	private static final ImmutableList<SquareArea> DUEL_OBSTICLE_ARENAS = ImmutableList.of(
			new SquareArea("South East Obstacle Arena", 3362, 3205, 3390, 3221),
			new SquareArea("West Obstacle Arena", 3331, 3224, 3359, 3240),
			new SquareArea("North East Obstacle Arena", 3362, 3243, 3390, 3259));

	/** The collection of areas that resemble the wilderness area. */
	private static final ImmutableList<Area> WILDERNESS = ImmutableList.of(
			new SquareArea("Wilderness 1", 2941, 3525, 3392, 3966),
			new SquareArea("Wilderness 2", 2941, 9920, 3392, 10366),
			new SquareArea("Wilderness 3", 2250, 4672, 2296, 4721));

	public static boolean inMulti(Entity entity) {
		for (Area zone : MULTI_COMBAT_ZONES) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return inEventArena(entity) || inTournamentArena(entity) || inkolodionArena(entity) || inKraken(entity)
				|| inKingBlackDragon(entity) || inGodwars(entity) || inZulrah(entity);
	}

	public static boolean inWilderness(Position position) {
		for (Area wild : WILDERNESS) {
			if (wild.inArea(position)) {
				return true;
			}
		}
		return false;
	}

	public static boolean inWilderness(Interactable entity) {
		if (entity == null)
			return false;
		if (inKingBlackDragon(entity))
			return false;
		for (Area wild : WILDERNESS) {
			if (wild.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inDuelArenaLobby(Entity entity) {
		for (Area zone : DUEL_ARENA_LOBBY) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inDuelObsticleArena(Interactable entity) {
		for (Area zone : DUEL_OBSTICLE_ARENAS) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inDuelArena(Interactable entity) {
		for (Area zone : DUEL_ARENAS) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}
	
	

	public static boolean inZulrah(Interactable entity) {
		for (Area zone : ZULRAH) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inPestControl(Interactable entity) {
		for (Area zone : PEST_CONTROL_GAME) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean inBossControl(Interactable entity) {
		for (Area zone : BOSS_CONTROL_GAME) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inTournamentArena(Interactable entity) {
		for (Area zone : TOURNAMENT_ARENA) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * public static boolean inBossArena(Interactable entity) { for (Area zone :
	 * BOSS_ARENA) { if (zone.inArea(entity.getPosition())) { return true; } }
	 * return false; }
	 */

	public static boolean inEdgeville(Interactable entity) {
		return inArea(entity, new Position(3075, 3457, 0), new Position(3116, 3518, 0));
	}

	public static boolean inFightCaves(Interactable entity) {
		return inArea(entity, new Position(2368, 5057, 0), new Position(2428, 5120, 0));
	}
	public static boolean inAllVsOne(Interactable entity) {
		return inArea(entity, new Position(3089, 4970, 0), new Position(3250, 5023, 0));
	}
	
	public static boolean inAllVsOne2(Interactable entity) {
		return inArea(entity, new Position(2788, 4312, 0), new Position(3039, 4459, 0));
	}
	public static boolean inAllVsOne3(Interactable entity) {
		return inArea(entity, new Position(2456, 4900, 0), new Position(2723, 4999, 0));
	}
	
	public static boolean inAllVsOne4(Interactable entity) {
		return inArea(entity, new Position(3009, 5216, 0), new Position(3041, 5250, 0));
	}
	
	public static boolean inRaids1(Interactable entity) {
		return inArea(entity, new Position(3335, 5253, 0), new Position(3351, 5276, 0));
	}
	public static boolean inRaids2(Interactable entity) {
		return inArea(entity, new Position(3154, 4299, 0), new Position(3181, 4322, 0));
	}
	public static boolean inRaids3(Interactable entity) {
		return inArea(entity, new Position(3344, 3076, 0), new Position(3274, 3122, 0));
	}
	public static boolean inRaids4(Interactable entity) {
		return inArea(entity, new Position(2741, 9314, 0), new Position(2782, 9343, 0));
	}
	public static boolean inRaids5(Interactable entity) {
		return inArea(entity, new Position(1859, 5387, 0), new Position(1918, 5427, 0));
	}
	public static boolean inZombieRaid(Interactable entity) {
		return inArea(entity, new Position(2757, 5128, 0), new Position(2779, 5147, 0));
	}
	public static boolean inSoloRaids1(Interactable entity) {
		return inArea(entity, new Position(3335, 5253, 0), new Position(3351, 5276, 0));
	}
	
	public static boolean inSoloRaids2(Interactable entity) {
		return inArea(entity, new Position(3154, 4299, 0), new Position(3181, 4322, 0));
	}
	
	public static boolean inSoloRaids3(Interactable entity) {
		return inArea(entity, new Position(3344, 3076, 0), new Position(3274, 3122, 0));
	}
	public static boolean inSoloRaids4(Interactable entity) {
		return inArea(entity, new Position(2741, 9314, 0), new Position(2782, 9343, 0));
	}
	public static boolean inSoloRaids5(Interactable entity) {
		return inArea(entity, new Position(1859, 5387, 0), new Position(1918, 5427, 0));
	}
	
	
	public static boolean inGodwars(Interactable entity) {
		return inArea(entity, new Position(2816, 5243, 2), new Position(2960, 5400, 2));
	}

	public static boolean inWarriorGuild(Interactable entity) {
		return inArea(entity, new Position(2838, 3535, 0), new Position(2875, 3555, 0));
	}

	public static boolean inCyclops(Interactable entity) {
		return inArea(entity, new Position(2847, 3532, 2), new Position(2876, 3542, 2))
				|| inArea(entity, new Position(2838, 3543, 2), new Position(2877, 3577, 2));
	}

	public static boolean inkolodionArena(Entity entity) {
		return entity.instance != Mob.DEFAULT_INSTANCE_HEIGHT && inArea(entity,
				new Position(3092, 3921, entity.getHeight()), new Position(3117, 3947, entity.getHeight()));
	}

	public static boolean inDonatorZone(Interactable entity) {
		return inArea(entity, new Position(1794, 3531, entity.getHeight()),
				new Position(1832, 3580, entity.getHeight()));
	}

	public static boolean inPortalZone(Interactable entity) {
		return inArea(entity, new Position(1794, 3531, entity.getHeight()),
				new Position(1832, 3580, entity.getHeight()));
	}

	public static boolean inGnomeCourse(Interactable entity) {
		return inArea(entity, new Position(2464, 3412, entity.getHeight()),
				new Position(2490, 3444, entity.getHeight()));
	}

	public static boolean inBarbarianCourse(Interactable entity) {
		return inArea(entity, new Position(2529, 3542, entity.getHeight()),
				new Position(2553, 3559, entity.getHeight()));
	}

	public static boolean inWildernessCourse(Interactable entity) {
		return inArea(entity, new Position(2989, 3933, entity.getHeight()),
				new Position(3006, 3964, entity.getHeight()));
	}
	

	public static boolean inWildernessResource(Interactable entity) {
		return inArea(entity, new Position(3714, 3924, entity.getHeight()),
				new Position(3196, 3944, entity.getHeight()));
	}

	public static boolean inBarrowsChamber(Interactable entity) {
		return inArea(entity, new Position(3547, 9690, entity.getHeight()),
				new Position(3556, 9699, entity.getHeight()));
	}

	public static boolean inKingBlackDragon(Interactable entity) {
		for (Area zone : KBD) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inRFD(Interactable entity) {
		return RFD_MINIGAME.inArea(entity.getPosition());
	}

	/*
	 * public static boolean inDropcatcher(Interactable entity) { return
	 * Dropcatcher_MINIGAME.inArea(entity.getPosition()); }
	 */
	public static boolean inKraken(Interactable entity) {
		for (Area zone : KRAKEN) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inSchool(Interactable entity) {
		for (Area zone : SCHOOL) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inBattleRealm(Interactable entity) {
		for (Area zone : getAreas()) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inBattleRealmMap(Position position) {
//        System.out.println("Checking if " + position + " is in " + BattleRealm.getAreas().get(0).toString() + "(" + BattleRealm.getAreas().get(0).getName());
		return getAreas().get(0).inArea(position);
	}

	public static boolean inBarrows(Entity entity) {
		for (Area zone : BARROWS) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inGlodArena(Entity entity) {
		for (Area zone : BOSS_ARENA) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean inPorazdirArea(Entity entity) {
		for (Area zone : BOSS_ARENA_1) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}
	
	
	public static boolean inInferno(Entity entity) {
		for (Area zone1 : INFERNO) {
			if (zone1.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}
	

	public static boolean inChimeraZone(Entity entity) {
		for (Area zone : CHIMERA_ARENA) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

	public static boolean inGraardor(Mob mob) {
		return GRAARDOR_ROOM.inArea(mob.getPosition());
	}

	public static boolean inArea(Interactable interactable, Position bottomLeft, Position topRight) {
		return (interactable.getPosition().getX() >= bottomLeft.getX())
				&& (interactable.getPosition().getX() <= topRight.getX())
				&& (interactable.getPosition().getY() >= bottomLeft.getY())
				&& (interactable.getPosition().getY() <= topRight.getY());
	}

	public static boolean inAllArea(Interactable entity, Area... area) {
		for (Area zone : area) {
			if (!zone.inArea(entity.getPosition())) {
				return false;
			}
		}
		return true;
	}

	public static boolean inAnyArea(Interactable entity, Area... area) {
		for (Area zone : area) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}
	

	public static boolean inCorp(Interactable entity) {
		return inArea(entity, new Position(2964, 4360, entity.getHeight()),
				new Position(3001, 4399, entity.getHeight()));
	}
	
	public static boolean inMole(Interactable entity) {
		return inArea(entity, new Position(1714, 5246, entity.getHeight()),
				new Position(1795, 5122, entity.getHeight()));
	}
	public static boolean inShaman(Interactable entity) {
		return inArea(entity, new Position(1469, 3690, entity.getHeight()),
				new Position(1406, 3735, entity.getHeight()));
	}
	public static boolean inGraador(Interactable entity) {
		return inArea(entity, new Position(2863, 3501, entity.getHeight()),
				new Position(2878, 5372, entity.getHeight()));
	}

	public abstract boolean inArea(Position position);

	public abstract Position getRandomLocation();

	public static boolean inVorkath(Interactable entity) {
		return inArea(entity, new Position(2257, 4053, entity.getHeight()),
				new Position(2288, 4079, entity.getHeight()));
	}

	private static final ImmutableList<Area> PEST_CONTROL_LOBBY = ImmutableList
			.of(new SquareArea("Pest Control Game", 2660, 2638, 2663, 2643));
	
	private static final ImmutableList<Area> BOSS_CONTROL_LOBBY = ImmutableList
			.of(new SquareArea("Pest Control Game", 2660, 2638, 2663, 2643));

	public static boolean inPestControlLobby(Interactable entity) {
		for (Area zone : PEST_CONTROL_LOBBY) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean inBossControlLobby(Interactable entity) {
		for (Area zone : BOSS_CONTROL_LOBBY) {
			if (zone.inArea(entity.getPosition())) {
				return true;
			}
		}
		return false;
	}

}
