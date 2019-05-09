package io.server.content.managers;

import java.util.HashMap;

import io.server.content.managers.impl.LotteryManager;

public class ManagerInitializer {

    private static final HashMap<String, Manager> MANAGERS = new HashMap<>();

    public static void init() {
    	
    	MANAGERS.put("Lottery Manager", new LotteryManager().startManager());

    }

    public static Manager getManager(String manager) { return MANAGERS.get(manager); }

}
