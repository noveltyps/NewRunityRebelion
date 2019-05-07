package io.server.content.activity.annoucements;

public enum AnnouncementData {

	GUIDE("Do ::openguide to view a High Quality money making guide! Make Bank!"),
	UPDATES("Check BrutalOS's Discord #Updates to view our latest Updates!!"),
	EVENTS_MONDAY("Did you know on Mondays, we give all of our players 15% Bonus Drop Rate Boost!"),
	EVENTS_TUESDAY("Did you know on Tuesdays, we give all of our players Double PK Points!"),
	EVENTS_WEDNESDAY("Did you know on Wednesdays, we give all of ours players Double AvO Tickets/Rewards!"),
	EVENTS_THURSDAY("Did you know on Thursdays, we give all of ours players Double AvO Tickets/Rewards!"),
	EVENTS_FRIDAY("Did you know on Fridays, we give all our players 30% Bonus Drop Rate Boost!"),
	EVENTS_SATURDAY("Did you know on Saturdays, we give all of our players Double Experience!"),
	EVENTS_SUNDAY("Did you know on Sundays, we give all of our players Double Experience!"),
	DISCORD("Join ::discord We tend to post our #updates and host multiple #giveaways a day!"),
	DID_YOUKNOW("Did you know we released Duo Inferno! Duo instanced minigame! Use the portal at home!!"),

	POUCH("Did you know you can access your bank vault on the go? simply do ::pouch"),
	vault("Did you know you can publicly show of your dank bank vault, by doing ::vault"),
	OSRS("Donate OSRS By doing ::osrs in return for Bonds IN-Game!"),


	
	
	;
	
	private String content;
	
	AnnouncementData(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

}
