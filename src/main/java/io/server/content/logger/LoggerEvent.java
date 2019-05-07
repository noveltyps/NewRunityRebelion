package io.server.content.logger;

import java.util.HashMap;
import java.util.Map;

import io.server.content.logger.impl.TradingLogger;

public class LoggerEvent {

	public static Map<String, LoggerListener> LISTENER = new HashMap<>();
	
	static {
		
		LISTENER.putIfAbsent("trading", new TradingLogger());
	}
}
