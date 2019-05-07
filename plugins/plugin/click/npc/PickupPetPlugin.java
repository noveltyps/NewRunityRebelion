package plugin.click.npc;

import io.server.content.pet.PetData;
import io.server.game.Animation;
import io.server.game.event.impl.NpcClickEvent;
import io.server.game.plugin.PluginContext;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.net.packet.out.SendMessage;

public class PickupPetPlugin extends PluginContext {

	@Override
	protected boolean secondClickNpc(Player player, NpcClickEvent event) {
		if (!PetData.forNpc(event.getNpc().id).isPresent()) {
			return false;
		}

		if (event.getNpc().owner != null && event.getNpc().owner != player) {
			player.send(new SendMessage("This is not your pet!"));
			return true;
		}

		if (player.pet == null) {
			return true;
		}

		if (player.inventory.remaining() == 0) {
			player.send(new SendMessage("You need at least 1 free inventory space to do this."));
			return true;
		}

		PetData pets = PetData.forNpc(event.getNpc().id).get();
		player.interact(player.pet);
		player.animate(new Animation(827));
		World.schedule(1, () -> {
			player.pet.unregister();
			player.pet = null;
			player.inventory.add(pets.getItem(), 1);
		});
		return true;
	}

}
