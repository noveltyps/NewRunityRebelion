package io.server.content;

import io.server.content.dialogue.DialogueFactory;
import io.server.game.task.Task;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.object.CustomGameObject;
import io.server.game.world.position.Position;
import io.server.util.Utility;

public class Gambling {
	
	Player player;
	
	long lastAction;
	
	public Gambling(Player player) {
		this.player = player;
		factory = new DialogueFactory(player);
		//factory.ignoreMovement = true;
	}
	
	public enum Flower {
		ASSORTED(2980, 2460),
		RED(2981, 2462),
		BLUE(2982, 2464),
		YELLOW(2983, 2466),
		PURPLE(2984, 2468),
		ORANGE(2985, 2470),
		MIXED(2986, 2472),
		WHITE(2987, 2474),
		BLACK(2988, 2476),
		;
		int objectId, itemId;
		Flower(int objectId) {
			this.objectId = objectId;
		}
		Flower(int objectId, int itemId) {
			this.objectId = objectId;
			this.itemId = itemId;
		}
	}
	
	private Flower flower;
	
	private CustomGameObject flowerObject;

    private final DialogueFactory factory;
	
	public void plantSeed() {
		if (!Utility.elapsedTicks(lastAction, 1))
			return;
		lastAction = System.currentTimeMillis();
		player.walkExactlyTo(new Position(player.getX()-1, player.getY()));
		player.inventory.remove(299, 1);
		flower = Utility.random(125) == 0 ? Utility.random(0, 1) == 0 ? Flower.BLACK : Flower.WHITE : Flower.values()[Utility.random(Flower.values().length-3)];
		flowerObject = new CustomGameObject(flower.objectId, player.getPosition().copy());
		flowerObject.register();
        factory.sendOption("Pick the flowers", this::pickFlower, "Leave the flowers", factory::clear);
        World.schedule(new Task(1) {
			protected void execute() {
				factory.execute();
				cancel();
			}
		});
		CustomGameObject removeObject = flowerObject;
		World.schedule(new Task(100) {
			protected void execute() {
				if (removeObject != null && removeObject.isRegistered())
					removeObject.unregister();
				cancel();
			}
		});
	}
	
	public void pickFlower() {
		if (flower == null || flowerObject == null || !flowerObject.isRegistered()) {
			player.message("The flower has already been picked.");
		} else if (player.inventory.remaining() >= 1) {
			flowerObject.unregister();
			player.inventory.add(flower.itemId, 1);
			flowerObject = null;
			flower = null;
		} else
			player.message("You do not have enough space in your inventory.");
	}
	
	public enum RollType {
		PUBLIC, CLAN, PRIVATE, BLACKJACK_START, BLACKJACK_HIT, BLACKJACK_STAY;
	}
	
	private int blackJackScore; 
	
	public void resetBlackjack() {
		blackJackScore = 0;
	}
	
	public void rollDice(int maxNumber, RollType rollType) {
		if (!Utility.elapsedTicks(lastAction, 3))
			return;
		lastAction = System.currentTimeMillis();
		int randomNumber = Utility.random(0, maxNumber);
		switch (rollType) {
		
		case BLACKJACK_START: 
			int roll1 = Utility.random(10) < 3 ? 10 : Utility.random(1, 11);
			int roll2 = Utility.random(10) < 3 ? 10 : Utility.random(1, 11);
			blackJackScore = roll1+roll2;
			if (blackJackScore == 21) {
				player.speak("The first two rolls are "+roll1+" and " +roll2+ " for a total of "+blackJackScore+". Blackjack!");
				player.inventory.replace(15100, 15099, true);
				resetBlackjack();
			} else {
				player.speak("The first two rolls are "+roll1+" and " +roll2+ " for a total of "+blackJackScore+". Hit or stay?");
				player.inventory.replace(15099, 15100, true);
			}
			break;
		case BLACKJACK_HIT:
			if (blackJackScore == 0) {
				player.inventory.replace(15100, 15099, true);
				break;
			}
			int roll = Utility.random(10) < 3 ? 10 : Utility.random(1, 11);
				blackJackScore += roll;
				if (blackJackScore > 21) {
					player.speak("The dice have landed on a "+roll+". The total is now " + blackJackScore+". Busted!");
					player.inventory.replace(15100, 15099, true);
					resetBlackjack();
				} else if (blackJackScore == 21) {
					player.speak("The dice have landed on a " + roll + ". The total is now " + blackJackScore+". W00t!");
					player.inventory.replace(15100, 15099, true);
					resetBlackjack();
				} else
					player.speak("The dice have landed on a "+roll+". The total is now " + blackJackScore+". Hit again?");
			break;
		case BLACKJACK_STAY:
			if (blackJackScore == 0) {
				player.inventory.replace(15100, 15099, true);
				break;
			}
			player.speak("Staying on " + blackJackScore + ".");
			resetBlackjack();
			player.inventory.replace(15100, 15099, true);
		break;
		case PUBLIC:
			player.speak("I have rolled a " + randomNumber + " of " + maxNumber + ".");
			break;
		case CLAN:
			player.speak("I have rolled a " + randomNumber + " of " + maxNumber + ".");
			if (player.clanChannel != null)
				player.clanChannel.message("@black@[Dice (up to 100)]@bla@ "+Utility.capitalizeSentence(player.getUsername())+": @dre@I have rolled a " + randomNumber + " of " + maxNumber + ".");
			else
				player.message("You are not in a clan.");
			break;
		case PRIVATE:
			player.message("<img=1> You have rolled a " + randomNumber + " of " + maxNumber + ".");
			break;
		}
	}
	
}