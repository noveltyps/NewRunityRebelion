package io.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

	public static final Gson JSON_ALLOW_NULL = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

	public static final Gson JSON_PRETTY_ALLOW_NULL = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
			.serializeNulls().create();

	public static final Gson JSON_PRETTY_NO_NULLS = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping()
			.create();

}
