package io.server.content.quest;

import java.util.Optional;

import io.server.content.event.InteractionEvent;
import io.server.content.quest.impl.CooksAssistant;
import io.server.content.quest.impl.EvilSisters;
import io.server.content.quest.impl.KolodionsArena;
import io.server.content.quest.impl.MageArena2;
import io.server.content.quest.impl.RFD;
import io.server.content.quest.impl.ToySoldier;
import io.server.game.world.entity.mob.player.Player;

/**
 * Handles the quest manager.
 *
 * @author Daniel
 */
public class QuestManager {
	public static final int COOKS_ASSISTANT = 4;
	public static final int EVIL_SISTERS = 0;

	public static final int TOY_SOLDIER = 1;
	public static final int RFD = 2;

	public static final int KOLODIONS_ARENA = 3;

	public static final int MageArena2 = 5;

	/** The total amount of quests. */
	public static final int QUEST_COUNT = 6;

	/** The player instance. */
	private Player player;

	/** The quest points. */
	private int points;

	/** The amount of quests completed. */
	private int completed;

	/** The quest stages. */
	public int[] stage = new int[QUEST_COUNT];

	/** The quests. */
	public static Quest[] QUESTS = new Quest[QUEST_COUNT];

	static {
		QUESTS[EVIL_SISTERS] = new EvilSisters();
		QUESTS[COOKS_ASSISTANT] = new CooksAssistant();
		QUESTS[TOY_SOLDIER] = new ToySoldier();
		QUESTS[RFD] = new RFD();
		QUESTS[MageArena2] = new MageArena2();

		// QUESTS[Dropcatcher] = new Dropcatcher();

		QUESTS[KOLODIONS_ARENA] = new KolodionsArena();
	}

	/** Constructs a new <code>QuestManager<code>. */
	public QuestManager(Player player) {
		this.player = player;

		for (int index = 0; index < QUESTS.length; index++) {
			this.stage[index] = 0;
		}
	}

	/** Handles the quest events. */
	public boolean onEvent(InteractionEvent interactionEvent) {
		boolean success = false;
		for (final Quest quest : QUESTS) {
			success |= quest.onEvent(player, interactionEvent);
		}
		return success;
	}

	public int[] getStage() {
		return stage;
	}

	public QuestState getState(int index) {
		int stage = getStage()[index];
		return stage == 0 ? QuestState.NOT_STARTED : (stage == -1 ? QuestState.COMPLETED : QuestState.STARTED);
	}

	public boolean completed(int index) {
		return getState(index) == QuestState.COMPLETED;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getTotal() {
		return QUESTS.length;
	}

	public Optional<Quest> get(int index) {
		if (index < 0 || index > QUEST_COUNT)
			return Optional.empty();
		return Optional.of(QUESTS[index]);
	}
}
