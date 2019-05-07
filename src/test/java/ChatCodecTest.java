import java.util.Arrays;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import io.server.util.ChatCodec;
import io.server.util.Utility;

public class ChatCodecTest {

	@Test
	public void encode() throws Exception {
		String msg = "does this work? let's find out!";

		byte[] encoded = ChatCodec.encode(msg);
		String decoded = ChatCodec.decode(encoded);

		System.out.println("encode()");
		System.out.println(String.format("encoded: %s", Arrays.toString(encoded)));
		System.out.println(String.format("decoded: %s", decoded));
		Assertions.assertNotEquals(msg, decoded);
	}

	@Test
	public void decode() throws Exception {
		String original = "Does this work? Let's find out!";
		byte[] encoded = new byte[] { 10, 4, 1, 8, 0, 2, 6, 5, 8, 0, 14, 4, 9, 22, 39, 0, 11, 1, 2, 50, 8, 0, 17, 5, 7,
				10, 0, 4, 12, 2, 38 };
		String decoded = ChatCodec.decode(encoded);

		System.out.println("decode()");
		System.out.println(String.format("original: %s", original));
		System.out.println(String.format("decoded: %s", decoded));

		Assertions.assertEquals(decoded, original);
	}

	@Test
	public void longPayload() throws Exception {
		byte[] payload = new byte[4096];

		for (int i = 0; i < payload.length; i++) {
			payload[i] = (byte) Utility.random(0, 99);
		}

		String decoded = ChatCodec.decode(payload);

		System.out.println(payload.length + "  " + decoded.length());
	}

}
