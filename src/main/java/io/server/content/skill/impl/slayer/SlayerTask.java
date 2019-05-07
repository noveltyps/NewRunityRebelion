package io.server.content.skill.impl.slayer;

import java.util.ArrayList;
import java.util.Arrays;

import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.position.Position;
import io.server.util.Utility;

/**
 * Holds all the slayer task data.
 *
 * @author Daniel
 * @author Adam_#6723
 */
public enum SlayerTask implements TaskInterface<Player> {
	
	
	PENG("Penguin", 1, 300, TaskDifficulty.EASY, new Position(1, 1), "::train", 2063) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	
	BIG_PENG("Big Penguin", 1, 300, TaskDifficulty.MEDIUM, new Position(1, 1), "::train", 1203) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},

	ROCK_CRAB("Rock crab", 1, 300, TaskDifficulty.EASY, new Position(1, 1), "Camelot", 2261, 5940, 100, 102) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	CRAWLING_HAND("Crawling hand", 1, 375, TaskDifficulty.EASY, new Position(1, 1), "Slayer Tower", 120, 448, 449, 450,
			451, 452, 453, 454, 455, 456, 457) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	// AVIANSIES("Aviansies", 1, 300, TaskDifficulty.MEDIUM, new Position(1, 1),
	// "Stronghold of Security", 3169, 3170, 3171, 3172, 3163, 3174, 3175, 3176,
	// 3177, 3178, 3179, 3180, 3181, 3182, 3183) {
//        @Override
//        public boolean canAssign(Player player) {
//            return true;
//        }
//
//        @Override
//        public boolean canAttack(Player player) {
//            return true;
//        }
//    },
	FIRE_GIANT("Fire giant", 5, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Slayer Cave", 2075, 2076, 2077, 2078,
			2079, 2080, 2081, 2082, 2083, 2084) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	GREEN_DRAGON("Green Dragon", 5, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Chaos Tunnels", 2918, 260, 261,
			262, 263, 264) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	BANSHEE("Banshee", 15, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Pollnivneach Smoke Dungeon", 414) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;// player.getEquipment().isWearing(EARMUFFS); <- Not sure if this goes here.
		}
	},
	BLOODVELDS("Bloodveld", 15, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Slayer Tower", 484, 485, 486, 3138) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	INFERNAL_MAGES("Infernal mage", 45, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Pollnivneach Well Dungeon",
			443, 444, 445, 446, 447) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	LESSER_DEMONS("Lesser Demons", 72, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Edgeville Dung & KBD cave", 2006,
			2005, 2018, 2008) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	BLUE_DRAGONS("Blue Dragons", 60, 50, TaskDifficulty.MEDIUM, new Position(1, 1), "Taverly Dungeon", 267, 268) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	DUST_DEVILS("Dust devil", 65, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Pollnivneach Smoke Dungeon", 423,
			424) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;// player.getEquipment().isWearing(FACEMASK); <- Not sure if this goes here.
		}
	},
	ABERRANT_SPECTRES("Aberrant Spectres", 60, 150, TaskDifficulty.MEDIUM, new Position(1, 1), "Slayer Tower", 2, 3, 4,
			5, 6, 7) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	REV_CAVES("Revenant Monsters", 5, 200, TaskDifficulty.MEDIUM, new Position(1, 1), "Revenant Cave", 7936,  7940,
		      7932, 7937, 7939, 7935, 7934, 7933, 7931, 7938) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	ANKOU("Ankou", 1, 300, TaskDifficulty.HARD, new Position(1, 1), "Stronghold of Security", 2514, 2515, 2516, 2517,
			2518, 2519) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	BLACK_DEMONS("Black demon", 1, 300, TaskDifficulty.HARD, new Position(1, 1), "Taverly Dungeon", 240, 2052, 2048,
			2049, 2051, 2050) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	DAGANNOTHS("Dagannoth", 1, 375, TaskDifficulty.HARD, new Position(1, 1), "Fremennik Province Light House", 3185,
			970, 971, 972, 973, 974, 975, 976, 977, 978, 979, 980, 981, 982, 983, 984, 985, 986, 987, 988, 2250, 2251,
			2252, 2253, 2254, 2255, 2256, 2257, 2258, 2259, 2265, 2266, 2267) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	HELLHOUNDS("Hellhound", 1, 375, TaskDifficulty.HARD, new Position(1, 1), "Taverly Dungeon", 104, 105, 135, 3133) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	WATERFIENDS("Waterfiend", 1, 375, TaskDifficulty.HARD, new Position(1, 1),
	"Ancient Caverns", 2916, 2917) {
       @Override
        public boolean canAssign(Player player) {
            return true;
        }

        @Override
        public boolean canAttack(Player player) {
            return true;
        }
    },
	JAD("TzTok monsters", 1, 375, TaskDifficulty.MEDIUM, new Position(2446, 5169), "Fight Caves", 6506, 3127, 2173) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	MITHRIL_DRAGON("Mithril dragon", 1, 150, TaskDifficulty.HARD, new Position(1, 1), "Ancient Caverns", 2919) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	STEEL_DRAGONS("Steel dragon", 1, 150, TaskDifficulty.HARD, new Position(1, 1), "Karamja Dungeon", 139, 274, 275) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	IRON_DRAGONS("Iron dragon", 1, 150, TaskDifficulty.HARD, new Position(1, 1), "Karamja Dungeon", 272, 273) {
		@Override
		public boolean canAssign(Player player) {
			return true;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	GARGOYLES("Gargoyle", 75, 150, TaskDifficulty.HARD, new Position(1, 1), "Slayer Tower", 412, 413) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 75;
		}

		@Override
		public boolean canAttack(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 75;
		}
	},
	CRAZY_ARCHAEOLOGIST("Crazy archaeologist", 60, 300, TaskDifficulty.HARD, new Position(1, 1), "Wilderness", 6618) {
		@Override
		public boolean canAssign(Player player) {
			return player.slayer.getUnlocked().contains(SlayerUnlockable.CRAZY_ARCHAEOLOGIST);
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	CHAOS_ELEMENTAL("Chaos elemental", 1, 150, TaskDifficulty.HARD, new
	 Position(1, 1), "Wilderness", 2054) {
        @Override
        public boolean canAssign(Player player) {
            return player.slayer.getUnlocked().contains(SlayerUnlockable.CHAOS_ELEMENT);
        }

        @Override
        public boolean canAttack(Player player) {
            return true;
        }
    },
	 SPIRTUAL_RANGERS("Spiritual Ranger", 63, 50, TaskDifficulty.HARD, new
	 Position(1, 1), "Godwars Dungeon", 2211) {
        @Override        public boolean canAssign(Player player) {
            return true;
        }

        @Override
        public boolean canAttack(Player player) {
            return true;
        }
    },
    SPIRTUAL_WARRIORS("Spiritual Warrior", 68, 50, TaskDifficulty.HARD, new Position(1, 1), "Godwars Dungeon", 2210) {
        @Override
        public boolean canAssign(Player player) {
            return true;
        }

        @Override
        public boolean canAttack(Player player) {
            return true;
        }
    },
	SKELETAL_WYVERN("Skeletal wyvern", 72, 50, TaskDifficulty.HARD, new Position(1, 1), "Asgarnia Ice Dungeon", 465,
			466, 467, 468) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 72;
		}

		@Override
		public boolean canAttack(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 72;
		}
	},
	 SPIRTUAL_MAGES("Spiritual Mage", 83, 50, TaskDifficulty.HARD, new Position(1,
	 1), "Godwars Dungeon", 2212) {
        @Override
       public boolean canAssign(Player player) {
            return true;
        }

       @Override
        public boolean canAttack(Player player) {
            return true;
        }
    },
	ABYSSAL_DEMONS("Abyssal Demons", 85, 200, TaskDifficulty.HARD, new Position(1, 1), "Slayer Tower", 124, 415) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 85;
		}

		@Override
		public boolean canAttack(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 85;
		}
	},
	DARK_BEAST("Dark beast", 90, 200, TaskDifficulty.HARD, new Position(1, 1), "Temple of Light dungeon", 4005) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 90;
		}

		@Override
		public boolean canAttack(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 90;
		}
	},
	KRAKEN("Kraken", 87, 50, TaskDifficulty.BOSS, new Position(1, 1), "Kraken cave", 494) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= getLevel();
		}

		@Override
		public boolean canAttack(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= getLevel();
		}

	},
	CERBERUS("Cerberus", 1, 50, TaskDifficulty.BOSS, new Position(1, 1), "Wilderness", 5862) {
		@Override
		public boolean canAssign(Player player) {
			return player.slayer.getUnlocked().contains(SlayerUnlockable.CERBERUS);
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	PORAZDIR("Porazdir", 1, 50, TaskDifficulty.BOSS, new Position(1, 1), "Wilderness Rev Caves", 7860) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 92;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	VORKATH("Vorkath", 1, 50, TaskDifficulty.BOSS, new Position(1, 1), "Vorkath Teleport", 8060) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 92;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	GIANT_ROC("Giant Roc", 1, 50, TaskDifficulty.BOSS, new Position(1, 1), "Giant Roc Teleport", 763) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 92;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	TARN("Tarn", 1, 10, TaskDifficulty.BOSS, new Position(1, 1), "::tarn", 6477) {
		@Override
		public boolean canAssign(Player player) {
			return player.skills.getMaxLevel(Skill.SLAYER) >= 50;
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	KING_BLACK_DRAGON("King black dragon", 1, 50, TaskDifficulty.BOSS, new Position(1, 1), "Wilderness", 239) {
		@Override
		public boolean canAssign(Player player) {
			return player.slayer.getUnlocked().contains(SlayerUnlockable.KING_BLACK_DRAGON);
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	},
	ZULRAH("Zulrah", 95, 60, TaskDifficulty.BOSS, new Position(1, 1), "Zulrah's Shrine", 2042, 2043, 2044) {
		@Override
		public boolean canAssign(Player player) {
			return player.slayer.getUnlocked().contains(SlayerUnlockable.ZULRAH);
		}

		@Override
		public boolean canAttack(Player player) {
			return true;
		}
	};

	/** The name of the task. */
	private final String name;

	/** The slayer level required to receive the task. */
	private final int level;

	/** The display size of the npc. */
	private int size;

	/** The difficulty of the task. */
	private TaskDifficulty difficulty;

	/** The teleport position for the task. */
	private final Position position;

	/** The string for the position. */
	private final String locationString;

	/** Array of the slayer npc. */
	private final int[] npc;

	/** Constructs a new <code>SlayerTask<code>. */
	SlayerTask(String name, int level, int size, TaskDifficulty difficulty, Position position, String locationString,
			int... npc) {
		this.name = name;
		this.level = level;
		this.size = size;
		this.difficulty = difficulty;
		this.position = position;
		this.locationString = locationString;
		this.npc = npc;
	}

	/** Gets the name of the task. */
	public String getName() {
		return name;
	}

	/** Gets the level of the task. */
	public int getLevel() {
		return level;
	}

	/** Gets the display size of the npc. */
	public int getSize() {
		return size;
	}

	/** Gets the difficulty of the slayer task. */
	public TaskDifficulty getDifficulty() {
		return difficulty;
	}

	/** Gets the position of the task. */
	public Position getPosition() {
		return position;
	}

	/** Gets the teleport string. */
	public String getLocationString() {
		return locationString;
	}

	/** Gets the npcs of the task. */
	public int[] getNpc() {
		return npc;
	}

	/** Gets the combat level of the npc. */
	public int getCombatLevel() {
		return NpcDefinition.get(npc[0]).getCombatLevel();
	}

	/** Checks if the npc is a slayer task assignment. */
	public boolean valid(int id) {
		return Arrays.stream(npc).anyMatch(npcId -> npcId == id);
	}

	/** Checks if a player can attack a slayer monster. */
	public static boolean canAttack(Player player, int id) {
		for (SlayerTask task : values()) {
			for (int npc : task.getNpc()) {
				if (npc == id) {
					return task.canAttack(player);
				}
			}
		}
		return true;
	}

	/** Assigns a slayer task for the player. */
	public static SlayerTask assign(Player player, TaskDifficulty difficulty) {
		ArrayList<SlayerTask> tasks = asList(player, difficulty);

		if (tasks.isEmpty()) {
			return null;
		}

		return Utility.randomElement(tasks);
	}

	/** Returns all the available slayer tasks for the player as a list. */
	public static ArrayList<SlayerTask> asList(Player player, TaskDifficulty difficulty) {
		ArrayList<SlayerTask> tasks = new ArrayList<>();
		for (SlayerTask task : values()) {
			if (task.getLevel() > player.skills.getLevel(Skill.SLAYER))
				continue;
			if (!task.canAssign(player))
				continue;
			if (player.slayer.getBlocked().contains(task))
				continue;
			if (task.difficulty != difficulty)
				continue;
			tasks.add(task);
		}
		return tasks;
	}
	
	public static void TeleportToPosition(Player player) {

	}

	public static int getPoints(TaskDifficulty difficulty) {
		switch (difficulty) {
		case EASY:
			return 20;
		case MEDIUM:
			return 22;
		case HARD:
			return 28;
		case BOSS:
			return 30;
		}
		return 0;
	}
}

interface TaskInterface<T> {
	boolean canAssign(final T player);

	boolean canAttack(final T player);
}