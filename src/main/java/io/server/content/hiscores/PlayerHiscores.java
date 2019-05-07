package io.server.content.hiscores;

import io.server.game.world.entity.mob.player.Player;

public class PlayerHiscores {

	//private int[] experience = new int[26];
	
	private Player player;

	public PlayerHiscores(Player player) {
		this.player = player;
	}

	public void execute() {//test2
		switch (player.right) {
		default:
			break;
		/*
		case ULTIMATE_IRONMAN:
			Hiscores.update("d6yzms44avukb49h1232j4i99pf19m99i3uqugiaspdshtui19wfjv364piqs8tbb0yyn3rt",  
					"Ultimate Ironman", player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
			break;
		case IRONMAN:
			Hiscores.update("d6yzms44avukb49h1232j4i99pf19m99i3uqugiaspdshtui19wfjv364piqs8tbb0yyn3rt",   
					"Ironman", player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
			break;
		case HARDCORE_IRONMAN:
			com.everythingrs.hiscores.Hiscores.update("d6yzms44avukb49h1232j4i99pf19m99i3uqugiaspdshtui19wfjv364piqs8tbb0yyn3rt",   
					"Hardcore Ironman", player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
			break;
		default:
			com.everythingrs.hiscores.Hiscores.update("d6yzms44avukb49h1232j4i99pf19m99i3uqugiaspdshtui19wfjv364piqs8tbb0yyn3rt",  
					"Normal Mode", player.getUsername(), player.right.ordinal(), getExperience(), player.debug);
			break;
*/
		}
	}
	/*
	private int[] getExperience() {
		for(int i = 0; i < player.skills.getSkills().length; i++) {
		//	System.out.println("Experience: " + Skill.getExperienceForLevel(99));
			experience[i] = (int) player.skills.getSkills()[i].getExperience();
		}
		return experience;
	}*/
}
