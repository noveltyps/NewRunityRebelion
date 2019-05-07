package io.server.net.codec.login;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.server.Config;
import io.server.net.codec.IsaacCipher;
import io.server.net.session.LoginSession;
import io.server.util.ByteBufUtils;

/**
 * The class that handles a connection through the login protocol.
 *
 * @author nshusa
 */
public final class LoginDecoder extends ByteToMessageDecoder {

	private static final Logger logger = LogManager.getLogger();

	private static final int LOGIN_HANDSHAKE = 14;
	private static final int NEW_CONNECTION_OPCODE = 16;
	private static final int RECONNECTION_OPCODE = 18;
	private static final int MAGIC_NUMBER = 255;
	private static final int LOGIN_BLOCK_HEADER_SIZE = 41;

	private static final Random RANDOM = new SecureRandom();

	private State state = State.HANDSHAKE;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		switch (state) {

		case HANDSHAKE:
			decodeHandshake(ctx, in);
			state = State.CONNECTION_TYPE;
			break;

		case CONNECTION_TYPE:
			decodeConnectionType(ctx, in);
			state = State.PAYLOAD;
			break;

		case PAYLOAD:
			decodePayload(ctx, in, out);
			break;
		default:
			break;

		}
	}

	private void decodeHandshake(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ctx.channel().attr(Config.SESSION_KEY).set(new LoginSession(ctx.channel()));

		if (in.readableBytes() >= 10) {

			final int handshake = in.readUnsignedByte();

			if (handshake != LOGIN_HANDSHAKE) {
				System.out.println("1");
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			int nameHash = in.readUnsignedByte();

			if (nameHash != 1) {
				System.out.println("2");
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			if (in.readLong() != 15) {
				System.out.println("3");
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			ByteBuf buf = ctx.alloc().buffer(17);
			buf.writeLong(0);
			buf.writeByte(0);
			buf.writeLong(RANDOM.nextLong());
			ctx.writeAndFlush(buf);
		} else {
			System.out.println("4");
			sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
		}
	}

	private void decodeConnectionType(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (in.readableBytes() >= 2) {
			final int connectionType = in.readUnsignedByte();

			if (connectionType != NEW_CONNECTION_OPCODE && connectionType != RECONNECTION_OPCODE) {
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
			}
		} else {
			sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
		}
	}

	private void decodePayload(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		final String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress();

		final int loginBlockSize = in.readUnsignedByte();

		if (in.isReadable(loginBlockSize)) {
			final int magicId = in.readUnsignedByte();

			if (magicId != MAGIC_NUMBER) {
				logger.warn(String.format("[%s] wrong magic id: %d", host, magicId));
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			final int gameVersion = in.readUnsignedShort();

			if (gameVersion != 6) {
				System.out.println("1");
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			String MAC = ByteBufUtils.readString(in);

			if (MAC.isEmpty() || MAC.length() != 17) {
				System.err.println("invalid mac address! "+MAC+" "+MAC.length());
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}
			
			int otherUID = in.readShort();
			
			if (otherUID != 117) {
				System.err.println("invalid uid! "+otherUID);
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			final int memoryVersion = in.readUnsignedByte();

			if (memoryVersion != 0 && memoryVersion != 1) {
				logger.warn(String.format("[%s] wrong memory version: %d", host, memoryVersion));
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			final int[] crcs = new int[9];

			for (int index = 0; index < crcs.length; index++) {
				crcs[index] = in.readInt();
			}

			final int expectedSize = in.readUnsignedByte(); // rsa header

			if (expectedSize != loginBlockSize - LOGIN_BLOCK_HEADER_SIZE) {
				logger.warn(String.format("[%s] wrong rsa block size: %d expecting: %d", host,
						(loginBlockSize - LOGIN_BLOCK_HEADER_SIZE), expectedSize));
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			final byte[] rsaBytes = new byte[loginBlockSize - LOGIN_BLOCK_HEADER_SIZE];
			in.readBytes(rsaBytes);

			ByteBuf rsaBuffer = Unpooled.wrappedBuffer(
					new BigInteger(rsaBytes).modPow(Config.RSA_EXPONENT, Config.RSA_MODULUS).toByteArray());

			final int rsa = rsaBuffer.readUnsignedByte();

			if (rsa != 127) {
				System.err.println(rsa+" value of rsa");
				logger.warn(String.format("[%s] failed decrypt rsa %d", host, rsa));
				sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}

			final long clientHalf = rsaBuffer.readLong();
			final long serverHalf = rsaBuffer.readLong();

			int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32),
					(int) serverHalf };

			final IsaacCipher decryptor = new IsaacCipher(isaacSeed);

			for (int index = 0; index < isaacSeed.length; index++) {
				isaacSeed[index] += 50;
			}

			final IsaacCipher encryptor = new IsaacCipher(isaacSeed);

			@SuppressWarnings("unused")
			final int uid = rsaBuffer.readInt();

			final String UUID = ByteBufUtils.readString(rsaBuffer);
			ByteBufUtils.readString(rsaBuffer);
			final String username = ByteBufUtils.readString(rsaBuffer);
			final String password = ByteBufUtils.readString(rsaBuffer);

			out.add(new LoginDetailsPacket(ctx, UUID, MAC, username, password, encryptor, decryptor));
		} else {
			sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
		}
	}

	private void sendResponseCode(ChannelHandlerContext ctx, LoginResponse response) {
		if (response == LoginResponse.LOGIN_SERVER_REJECTED_SESSION) {
			final String host = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostString();
			logger.warn(String.format("[%s] session was rejected", host));
		}
		ByteBuf buffer = ctx.alloc().buffer(Byte.BYTES);
		buffer.writeByte(response.getOpcode());
		ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
		state = State.IGNORE;
	}

	private enum State {
		HANDSHAKE, CONNECTION_TYPE, PAYLOAD, IGNORE
	}

}