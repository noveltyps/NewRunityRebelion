package io.server.content.worldevent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import io.server.game.world.World;
import io.server.util.Utility;

/**
 * @author Austin X (Rune-Server)
 */

public class WorldEventHandler {

	private static ArrayList<WorldEvent> activeEvents = new ArrayList<WorldEvent>();
	private static ArrayList<WorldEvent> scheduledEvents = new ArrayList<WorldEvent>();
	
	public void announce(String message) {
		String header = Utility.formatHeader("[World Event] ");
		World.sendMessage(header + message);
	}

	Timer timer = new Timer();
	
	public void execute() {
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				update();
			}
		};

		timer.schedule(task, 5000, 5000);
		
	}
	
	public boolean addEvent(WorldEvent event) {
		for (WorldEvent e : getAllEvents())
			if (e.getType() == event.getType())
				return false;
		System.out.println("Adding event: " + event.getType().getName());
		scheduledEvents.add(event);
		return true;
	}
	/*
	public void displayEvents(Player player) {
		String header = Utility.formatHeader("[World Event] ");
		for (WorldEvent event : activeEvents) {
			String eventName = Utility.highlightText("+" + event.getModifier()*100 + "% " + event.getType().getName());
			String hours = Utility.highlightText(event.getHoursRemaining() + " hours");
			player.sendMessage(header + "The " + eventName + " event is currently active! (Ends in " + hours+")");
		}
	} */
	
	public void update() {

		Calendar calendar = new GregorianCalendar();
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		
		if (dayOfMonth <= 7) {
			addEvent(new WorldEvent(WorldEventType.VOTE, calendar.getTime(), 8-dayOfMonth, 0, .5));
		}
		
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1: // sunday
			addEvent(new WorldEvent(WorldEventType.EXP, calendar.getTime(), 1, 0, .5));
			break;
		case 6: // friday
			addEvent(new WorldEvent(WorldEventType.EXP, calendar.getTime(), 3, 0, .5));
			break;
		case 7: // saturday
			addEvent(new WorldEvent(WorldEventType.EXP, calendar.getTime(), 2, 0, .5));
			break;
		}
		
		
		
		if (scheduledEvents.size() > 0) {
			for (int idx = 0; idx < scheduledEvents.size(); idx++) {
				if (scheduledEvents.get(idx).getStartDate().getTime() <= System.currentTimeMillis()) {
					activeEvents.add(scheduledEvents.get(idx));
					String event = Utility.highlightText(scheduledEvents.get(idx).getType().getName());
//					String hours = Utility.highlightText(scheduledEvents.get(idx).getHoursRemaining() + " hours");
					announce("The " + event + " event has started! [Ends in " + Utility.highlightText(scheduledEvents.get(idx).getShortenedFormattedSeconds()) + "]");
					scheduledEvents.remove(scheduledEvents.get(idx));
				}
			}
		}
		
		

		if (activeEvents.size() > 0) {
			for (int index = 0; index < activeEvents.size(); index++) {
				if (activeEvents.get(index) == null)
					continue;
				if (activeEvents.get(index).getEndDate().getTime() <= System.currentTimeMillis()) {
					String event = Utility.highlightText(activeEvents.get(index).getType().getName());
					announce("The " + event + " event has ended!");
					activeEvents.remove(activeEvents.get(index));
				}
			}
		}
	}
	
	public void clearFromAllEvents(WorldEventType type) {
		
		clearFromActiveEvents(type);
		clearFromScheduledEvents(type);
		
	}
	
	public void clearFromActiveEvents(WorldEventType type) {
		
		for (WorldEvent event : activeEvents)
			if (event.getType() == type)
				activeEvents.remove(event);
		
	}
	
	public void clearFromScheduledEvents(WorldEventType type) {
		
		for (WorldEvent event : scheduledEvents)
			if (event.getType() == type)
				scheduledEvents.remove(event);
		
	}
	
	public ArrayList<WorldEvent> getActiveEvents() { return activeEvents; }

	public ArrayList<WorldEvent> getAllEvents() {
		ArrayList<WorldEvent> events = new ArrayList<WorldEvent>();
		for (WorldEvent event : activeEvents)
			events.add(event);
		for (WorldEvent event : scheduledEvents)
			events.add(event);
		return events;
	}

	public WorldEvent getEvent(WorldEventType type) {
		for (WorldEvent event : activeEvents)
			if (event.getType() == type)
				return event;
		return null;
	}
}