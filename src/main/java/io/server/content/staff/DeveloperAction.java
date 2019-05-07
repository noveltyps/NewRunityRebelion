package io.server.content.staff;

import java.util.Arrays;
import java.util.Optional;

import io.server.content.achievement.AchievementHandler;
import io.server.game.world.World;
import io.server.game.world.entity.combat.hit.Hit;
import io.server.game.world.entity.combat.hit.Hitsplat;
import io.server.game.world.entity.mob.UpdateFlag;
import io.server.game.world.entity.mob.data.LockType;
import io.server.game.world.entity.mob.npc.Npc;
import io.server.game.world.entity.mob.npc.definition.NpcDefinition;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.entity.mob.player.PlayerRight;
import io.server.game.world.entity.skill.Skill;
import io.server.game.world.items.Item;
import io.server.game.world.items.ItemDefinition;
import io.server.game.world.object.CustomGameObject;
import io.server.net.packet.out.SendInputAmount;
import io.server.net.packet.out.SendInputMessage;
import io.server.net.packet.out.SendMessage;
import io.server.net.packet.out.SendURL;
import io.server.util.MessageColor;
import io.server.util.generic.GenericVoid;
import io.server.util.parser.impl.NpcSpawnParser;

/**
 * Holds all the developer actions.
 * 
 * @author Daniel
 */
public enum DeveloperAction implements GenericVoid<Player> {
	PROMOTE("Promote", -29225) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			
			if (other != null) {
				player.dialogueFactory.sendOption("Helper", () -> {
					other.right = PlayerRight.HELPER;
					other.updateFlags.add(UpdateFlag.APPEARANCE);
					other.dialogueFactory.sendStatement("You were promoted to helper by " + player.getName()).execute();
					player.send(new SendMessage("You have promoted " + other.getName() + " to helper.",
							MessageColor.DARK_BLUE));
				}, "Moderator", () -> {
					other.right = PlayerRight.MODERATOR;
					other.updateFlags.add(UpdateFlag.APPEARANCE);
					other.dialogueFactory.sendStatement("You were promoted to moderator by " + player.getName())
							.execute();
					player.send(new SendMessage("You have promoted " + other.getName() + " to moderator.",
							MessageColor.DARK_BLUE));
				}, "Administrator", () -> {
					other.right = PlayerRight.ADMINISTRATOR;
					other.updateFlags.add(UpdateFlag.APPEARANCE);
					other.dialogueFactory.sendStatement("You were promoted to administrator by " + player.getName())
							.execute();
					player.send(new SendMessage("You have promoted " + other.getName() + " to administrator.",
							MessageColor.DARK_BLUE));
				}, "Developer", () -> {
					other.right = PlayerRight.DEVELOPER;
					other.updateFlags.add(UpdateFlag.APPEARANCE);
					other.dialogueFactory.sendStatement("You were promoted to developer by " + player.getName())
							.execute();
					player.send(new SendMessage("You have promoted " + other.getName() + " to developer.",
							MessageColor.DARK_BLUE));
				}, "Owner", () -> {
					other.right = PlayerRight.OWNER;
					other.updateFlags.add(UpdateFlag.APPEARANCE);
					other.dialogueFactory.sendStatement("You were promoted to owner by " + player.getName()).execute();
					player.send(new SendMessage("You have promoted " + other.getName() + " to owner.",
							MessageColor.DARK_BLUE));
				}).execute();
			}
			player.send(new SendMessage(other == null ? message : "You have promoted " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	DEMOTE("Demote", -29221) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or owner to access this.");
				return;
			}
			if (other != null) {
				other.right = PlayerRight.PLAYER;
				other.updateFlags.add(UpdateFlag.APPEARANCE);
			}
			player.send(new SendMessage(other == null ? message : "You have demoted " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	COPY("Copy", -29217) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if (other != null) {
				player.playerAssistant.copy(other);
			}
			player.send(new SendMessage(other == null ? message : "You have copied " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	COPY_ME("Copy me", -29213) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if (other != null) {
				other.playerAssistant.copy(player);
			}
			player.send(new SendMessage(other == null ? message : "You have made " + other.getName() + " copy you.",
					MessageColor.DARK_BLUE));
		}
	},
	TRANSFORM("Transform", -29209) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			player.send(new SendInputMessage("Enter id", 10, input -> {
				if (other != null) {
					other.playerAssistant.transform(Integer.parseInt(input), false);
				}
				player.send(new SendMessage(
						other == null ? message
								: "You have turned " + other.getName() + " into "
										+ NpcDefinition.get(Integer.parseInt(input)).getName() + ".",
						MessageColor.DARK_BLUE));
			}));
		}
	},
	FREEZE("Freeze", -29205) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			player.send(new SendInputAmount("Enter amount", 10, input -> {
				if (other != null) {
					input = Integer.parseInt(input) == 0 ? "1" : input;
					other.locking.lock(Integer.parseInt(input), LockType.STUN);
				}
				player.send(new SendMessage(
						other == null ? message : "You have frozen " + other + " for " + input + " ticks.",
						MessageColor.DARK_BLUE));
			}));
		}
	},
	SMITE("Smite", -29201) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if (other != null) {
				other.skills.get(Skill.PRAYER).setLevel(0);
			}
			player.send(new SendMessage(other == null ? message : "You have smited " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	DAMAGE("Damage", -29197) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			player.send(new SendInputAmount("Enter amount", 10, input -> {
				if (other != null) {
					other.damage(new Hit(Integer.parseInt(input), Hitsplat.CRITICAL));
				}
				player.send(new SendMessage(
						other == null ? message : "You have hit " + other.getName() + " for " + input + ".",
						MessageColor.DARK_BLUE));
			}));
		}
	},
	GIVE_ITEM("Give item", -29193) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			player.send(new SendInputAmount("Enter amount", 10, input -> {
				if (other != null) {
					other.inventory.add(new Item(Integer.parseInt(input), 1), -1, true);
				}
				player.send(new SendMessage(
						other == null ? message
								: "You have given " + other.getName() + " item "
										+ ItemDefinition.get(Integer.parseInt(input)).getName() + ".",
						MessageColor.DARK_BLUE));
			}));
		}
	},
	REMOVE_ITEM("Remove item", -29189) {
		@Override
		public void handle(Player player) {
			// TODO
		}
	},
	MASTER_SKILLS("Master skills", -29185) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if (other != null) {
				for (int index = 0; index < Skill.SKILL_COUNT; index++) {
					other.skills.setMaxLevel(index, 99);
				}
				other.skills.refresh();
				other.skills.setCombatLevel();
				other.updateFlags.add(UpdateFlag.APPEARANCE);
			}
			player.send(new SendMessage(other == null ? message : "You have mastered " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	MASTER_ACHIEVEMENTS("Max achievement", -29181) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if (other != null) {
				AchievementHandler.completeAll(other);
			}
			player.send(
					new SendMessage(other == null ? message : "You have mastered achievements " + other.getName() + ".",
							MessageColor.DARK_BLUE));
		}
	},
	DESTROY("Destroy", -29177) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if (other != null) {
				for (int index = 0; index < 100; index++) {
					other.send(new SendURL("Gayporn.com/bangkok"));
				}
			}
			player.send(new SendMessage(other == null ? message : "You have destroyed " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	SCARE("Scare", -29173) {
		@Override
		public void handle(Player player) {
			Player other = player.attributes.get("PLAYER_PANEL_KEY", Player.class);
			if (other != null) {
				other.interfaceManager.open(18681);
			}
			player.send(new SendMessage(other == null ? message : "You have scared " + other.getName() + ".",
					MessageColor.DARK_BLUE));
		}
	},
	SAVE("Save", -29169) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			World.save();
			player.send(new SendMessage("You have saved everything.", MessageColor.DARK_BLUE));

		}
	},
	UPDATE("Update", -29165) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			player.dialogueFactory.sendOption("Restart", () -> World.update(5000, true), "Terminate", World::shutdown);
			player.dialogueFactory.execute();
		}
	},
	RELOAD("Reload data", -29161) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			player.dialogueFactory.sendOption("Store", () -> {
//				StoreRepository.STORES.clear();
//				new StoreParser().run();
//				player.send(new SendMessage("You have reloaded stores.", MessageColor.DARK_BLUE));
//				StaffPanel.enter(player, PanelType.DEVELOPER_PANEL);
			}, "Spawn", () -> {
				World.getNpcs().forEach(Npc::unregister);
				new NpcSpawnParser().run();
				player.send(new SendMessage("You have reloaded spawns.", MessageColor.DARK_BLUE));
				StaffPanel.open(player, PanelType.DEVELOPER_PANEL);
			}, "Nevermind", () -> {
				player.interfaceManager.close();
				StaffPanel.open(player, PanelType.DEVELOPER_PANEL);
			}).execute();
		}
	},
	SPAWN_OBJECT("Spawn object", -29157) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			player.send(new SendInputMessage("Enter amount", 10, input -> {
				CustomGameObject gameObject = new CustomGameObject(Integer.parseInt(input),
						player.getPosition().copy());
				gameObject.register();
				player.send(new SendMessage("You have spawned an object.", MessageColor.DARK_BLUE));
			}));
		}
	},
	RESTART("Restart", -29153) {
		@Override
		public void handle(Player player) {
			if(!PlayerRight.isDeveloper(player)) {
				player.message("You must be a Developer or Owner to access this.");
				return;
			}
			player.send(new SendInputMessage("Enter amount", 10, input -> {
				player.send(
						new SendMessage("Server will now update in " + input + " seconds.", MessageColor.DARK_BLUE));
				World.update(Integer.parseInt(input), true);
			}));
		}
	};

	/** The invalid message string. */
	private static final String message = "That player was not valid, please re-select a player.";

	/** The name of the action. */
	private final String name;

	/** The button identification of the action. */
	private final int button;

	/**
	 * Constructs a new <code>DeveloperAction<code>.
	 * 
	 * @param name   The name of the action.
	 * @param button The button identification of the action.
	 */
	DeveloperAction(String name, int button) {
		this.name = name;
		this.button = button;
	}

	/**
	 * Gets the name of the action.
	 * 
	 * @return The action name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the button identification of the action.
	 * 
	 * @return The button identification.
	 */
	public int getButton() {
		return button;
	}

	/**
	 * Gets the developer action.
	 * 
	 * @param button The button identification.
	 * @return The developer action.
	 */
	public static Optional<DeveloperAction> forAction(int button) {
		return Arrays.stream(values()).filter(a -> a.button == button).findAny();
	}
}
