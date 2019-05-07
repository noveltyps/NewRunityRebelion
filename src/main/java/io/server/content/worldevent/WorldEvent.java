package io.server.content.worldevent;

import java.util.Date;
import java.util.GregorianCalendar;

import io.server.util.Utility;

/**
 * @author Austin X (Rune-Server)
 */

public class WorldEvent {

	private WorldEventType type;
	
	private Date startDate, endDate;

	private double modifier = 3.0;
	
	public WorldEvent(WorldEventType type, int days, int hours) {
		this.setType(type);
		this.setStartDate(new GregorianCalendar().getTime());
		this.setEndDate(Utility.getModifiedDate(startDate, days, hours));
	}
	
	public WorldEvent(WorldEventType type, Date startDate, int days, int hours) {
		this.setType(type);
		this.setStartDate(startDate);
		this.setEndDate(Utility.getModifiedDate(startDate, days, hours));
	}
	
	public WorldEvent(WorldEventType type, Date startDate, Date endDate) {
		this.setType(type);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
	}
	public WorldEvent(WorldEventType type, Date startDate, int days, int hours, double modifier) {
		this.setType(type);
		this.setStartDate(startDate);
		this.setEndDate(Utility.getModifiedDate(startDate, days, hours));
		this.setModifier(modifier);
	}
	
	public WorldEvent(WorldEventType type, Date startDate, Date endDate, double modifier) {
		this.setType(type);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setModifier(modifier);
	}
	
	public Long getHoursRemaining() {
		return (getEndDate().getTime() - System.currentTimeMillis() + 1000) / 1000 / 60 / 60;
	}
	
	public Long getSecondsRemaining() {
		return (getEndDate().getTime() - System.currentTimeMillis()) / 1000;
	}
	
    public String getFormattedSeconds() {
    	long seconds = getSecondsRemaining();
        final long h = seconds / 3600, m = seconds / 60 % 60, s = seconds % 60;
        return (h < 10 ? "0" + h : h) + " hours : " + (m < 10 ? "0" + m : m) + " minutes : " + (s < 10 ? "0" + s : s) + " seconds";
    }
	
    public String getShortenedFormattedSeconds() {
    	long seconds = getSecondsRemaining();
        final long h = seconds / 3600, m = seconds / 60 % 60, s = seconds % 60;
        return (h < 10 ? "0" + h : h) + " H : " + (m < 10 ? "0" + m : m) + " M : " + (s < 10 ? "0" + s : s) + " S";
    }
	
	public WorldEventType getType() {
		return type;
	}
	
	public void setType(WorldEventType type) {
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getModifier() {
		return modifier;
	}

	public WorldEvent setModifier(double modifier) {
		this.modifier = modifier; return this;
	}

}