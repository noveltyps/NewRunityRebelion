package io.server.game.world.entity.mob.data;

import static io.server.content.activity.ActivityType.TUTORIAL;

import io.server.content.dialogue.Dialogue;
import io.server.content.event.EventDispatcher;
import io.server.content.event.impl.ClickButtonInteractionEvent;
import io.server.game.world.entity.mob.Mob;
import plugin.click.button.StarterKitButtonPlugin;

public enum PacketType {
	COMBAT {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	KEY {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	CLICK_ITEM {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	CLICK_OBJECT {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	COMMANDS {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	CLICK_NPC {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	CLICK_BUTTON {
		@Override
		public boolean exception(Mob mob, Object object) {
			if (!mob.isPlayer())
				return false;

			int button = (int) object;

			if (button == 2458)// logout
				return true;
			if (Dialogue.isDialogueButton(button))
				return true;
			if (StarterKitButtonPlugin.isButton(button))
				return true;
			if (mob.inActivity(TUTORIAL)
					&& EventDispatcher.execute(mob.getPlayer(), new ClickButtonInteractionEvent(button)))
				return true;

			return false;
		}
	},
	USE_ITEM {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	WIELD_ITEM {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	MOVE_ITEMS {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	PICKUP_ITEM {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	USE_MAGIC {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	DROP_ITEM {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	INTERACT {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	MOVEMENT {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	WALKING {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	},
	CHAT {
		@Override
		public boolean exception(Mob mob, Object object) {
			return false;
		}
	};

	/** Handles the execution of the lock. */
	public abstract boolean exception(Mob mob, Object object);

	public static final PacketType[] MASTER_WITH_MOVEMENT = { CLICK_ITEM, CLICK_OBJECT, COMMANDS, CLICK_NPC,
			CLICK_BUTTON, USE_ITEM, WIELD_ITEM, MOVE_ITEMS, PICKUP_ITEM, USE_MAGIC, DROP_ITEM, INTERACT, WALKING, CHAT,
			KEY, COMBAT };

	public static final PacketType[] MASTER_WITH_COMMANDS = { CLICK_ITEM, CLICK_OBJECT, CLICK_NPC, CLICK_BUTTON,
			USE_ITEM, WIELD_ITEM, MOVE_ITEMS, PICKUP_ITEM, USE_MAGIC, DROP_ITEM, INTERACT, WALKING, CHAT, KEY, COMBAT,
			WALKING, MOVEMENT };
}
