package io.server.content.mysterybox2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.server.game.task.TickableTask;
import io.server.game.world.World;
import io.server.game.world.entity.mob.player.Player;
import io.server.game.world.items.Item;
import io.server.net.packet.out.SendColor;
import io.server.net.packet.out.SendConfig;
import io.server.net.packet.out.SendItemOnInterfaceSlot;
import io.server.net.packet.out.SendString;
import io.server.util.RandomUtils;
import io.server.util.Utility;

/**
 * Mystery Box Event
 * @author Adam_#6723
 */


public class MysteryBoxEvent extends TickableTask {
    private final Player player;
    private List<Item> items;
    private List<Item> allItems;
    private int speed;
    private final MysteryBoxManager mysteryBox;


    MysteryBoxEvent(Player player) {
        super(false, 1);
        this.player = player;
        this.items = new ArrayList<>();
        this.allItems = new ArrayList<>();
        this.speed = Utility.random(8, 12);
        this.mysteryBox = player.mysteryBox;
    }

    private void move() {
        allItems.add(items.get(0));
        items.remove(0);
        Item next = getNextItem();
        allItems.remove(next);
        items.add(next);
    }

    private Item getNextItem() {
        Item next = null;
        for (Item item : allItems) {
            if (!items.contains(item)) {
                next = item;
                break;
            }
        }
        return next;
    }

    private void reward() {
        Item reward = items.get(5);
        String boxName = mysteryBox.box.name();

        Item newItem = reward;
        if (reward.getAmount() > 1 && reward.isNoteable()) {
            newItem = new Item(reward.getNotedId(), reward.getAmount());
        }

        player.inventory.add(newItem);
        player.dialogueFactory.sendItem(boxName, "Congratulations, you have won " + Utility.getAOrAn(reward.getName()) + " " + reward.getName() + "!", reward.getId()).execute();
        player.send(new SendColor(59508, 0x37991C));

        byte type = getType(reward);
        if (type != 1) {
            World.sendMessage("<icon=17><col=5739B3> Brutal: <col=" + player.right.getColor() + ">" + player.getName() + " </col>has won " + Utility.getAOrAn(reward.getName()) + " <col=5739B3>" + reward.getName() + " </col>from the <col=5739B3>" + boxName + "</col>.");
        }

        player.locking.unlock();
    }

    @Override
    protected boolean canSchedule() {
        return player.mysteryBox.box != null;
    }

    @Override
    protected void onSchedule() {
        player.dialogueFactory.clear();
        player.locking.lock();

        allItems.addAll(Arrays.asList(mysteryBox.box.rewards()));
        player.inventory.remove(mysteryBox.box.item(), 1);
        mysteryBox.count = player.inventory.computeAmountForId(mysteryBox.box.item());
        Collections.shuffle(allItems);
        Collections.shuffle(allItems);


        for (int index = 0; index < 11; index++) {
            if (index >= allItems.size())
                continue;
            Item item = allItems.get(index);
            items.add(item);
            allItems.remove(index);
        }

        player.send(new SendColor(59508, 0xF01616));
        player.send(new SendString("You have " + mysteryBox.count + " mystery box available!", 59507));

    }

    private byte getType(Item item) {
        byte type = 1;
        if (item.getValue() * item.getAmount() >= mysteryBox.box.rareValue()) {
            type = 2;
        } else if (!item.isTradeable()) {
            type = 0;
        }
        return type;
    }

    @Override
    protected void tick() {
        move();

        for (int index = 0; index < 11; index++) {
            if (index >= items.size())
                continue;
            Item item = items.get(index);

            player.send(new SendConfig((430 + index), getType(item)));
            player.send(new SendItemOnInterfaceSlot(59512, item, index));
        }

        if (tick == speed) {
            Item reward = items.get(6);
            if (getType(reward) == 2 && RandomUtils.success(.50)) {
                speed++;
            } else if (getType(reward) == 1 && RandomUtils.success(.55)) {
                speed++;
            } else {
                setDelay(1);
            }
        }
        if (tick > speed)
            cancel();
    }

    @Override
    protected void onCancel(boolean logout) {
        if (logout) {
            player.inventory.add(mysteryBox.box.item(), 1);
        } else {
            reward();
        }
    }
}
