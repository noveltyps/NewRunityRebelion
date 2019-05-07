package io.server.util.parser;

import java.io.FileReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class provides an easy to use google gson parser specifically designed
 * for parsing JSON files.
 *
 * @author Seven
 * @author Daniel
 */
public abstract class GsonParser extends GenericParser {

	private static final Logger logger = LogManager.getLogger();

	/** The {@code Gson} object. */
	protected transient Gson builder;

	/**
	 * Creates a new {@code GsonParser}.
	 *
	 * @param path The specified path of the json file to parse.
	 */
	public GsonParser(String path) {
		this(path, true);
	}

	/**
	 * Creates a new {@code GsonParser}.
	 *
	 * @param path The specified path of the json file to parse.
	 * @param log  The flag that denotes to log messages.
	 */
	public GsonParser(String path, boolean log) {
		super(path, ".json", log);
		this.builder = new GsonBuilder().create();
	}

	public void initialize(int size) {
	}

	/**
	 * The method allows a user to modify the data as its being parsed.
	 *
	 * @param data The {@code JsonObject} that contains all serialized information.
	 */
	protected abstract void parse(JsonObject data);

	/**
	 * This method handles what happens after the parser has ended.
	 */
	protected void onEnd() {
	}

	@Override
	public final void deserialize() {
		try (FileReader reader = new FileReader(path.toFile())) {
			final JsonParser parser = new JsonParser();
			final JsonElement element = parser.parse(reader);

			if (element.isJsonNull()) {
				logger.warn(String.format("json document=%s is null", path.toString()));
				return;
			}

			if (element.isJsonArray()) {
				final JsonArray array = element.getAsJsonArray();
				initialize(array.size());
				for (index = 0; index < array.size(); index++) {
					JsonObject data = (JsonObject) array.get(index);
					parse(data);// the id is to big u gotta lower it, its literally +1 from before, and 22517 is
								// the last item in OSRS's cache
					// well like u see the fucking console says outofbounds idk if u can read, yeah
					// i know what it says, its just stupid l000l
				}
			} else if (element.isJsonObject()) {
				parse(element.getAsJsonObject());
			}

			onEnd();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
