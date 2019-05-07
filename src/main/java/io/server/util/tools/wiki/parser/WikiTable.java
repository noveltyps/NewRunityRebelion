package io.server.util.tools.wiki.parser;

import org.jsoup.nodes.Document;

import com.google.gson.JsonArray;

public abstract class WikiTable {
	private final String link;
	protected final JsonArray table = new JsonArray();

	protected WikiTable(String link) {
		this.link = link;
	}

	public String getLink() {
		return link;
	}

	protected abstract void parseDocument(Document document);

	public JsonArray getTable() {
		return table;
	}

}
