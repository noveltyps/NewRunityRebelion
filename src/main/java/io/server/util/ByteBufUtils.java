package io.server.util;

import io.netty.buffer.ByteBuf;

public final class ByteBufUtils {

	private ByteBufUtils() {

	}

	public static String readString(ByteBuf buf) {
		byte temp;
		StringBuilder builder = new StringBuilder();
		while (buf.isReadable() && (temp = buf.readByte()) != 10) {
			builder.append((char) temp);
		}
		return builder.toString();
	}

}
