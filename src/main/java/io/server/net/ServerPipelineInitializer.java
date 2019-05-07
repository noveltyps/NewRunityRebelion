package io.server.net;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.server.Config;
import io.server.net.codec.login.LoginDecoder;
import io.server.net.codec.login.LoginResponseEncoder;
import io.server.net.session.Session;

/**
 * The {@link ChannelInitializer} implementation that will setup the games
 * networking pipeline.
 *
 * @author nshusa
 */
@Sharable
public final class ServerPipelineInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * The first part of the pipeline where connections can be filtered before
	 * propagating down the pipeline.
	 */
	private static final ChannelFilter FILTER = new ChannelFilter();

	/**
	 * The part of the pipeline that handles exceptions caught, channel being read,
	 * in-active channels, triggered events.
	 */
	private static final ChannelHandler HANDLER = new ChannelHandler();

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		final ChannelPipeline pipeline = ch.pipeline();
		ch.attr(Config.SESSION_KEY).setIfAbsent(new Session(ch));

		pipeline.addLast("channel-filter", FILTER);
		pipeline.addLast("login-decoder", new LoginDecoder());
		pipeline.addLast("login-encoder", new LoginResponseEncoder());
		pipeline.addLast("timeout", new IdleStateHandler(Config.IDLE_TIMEOUT, 0, 0));
		pipeline.addLast("channel-handler", HANDLER);
	}

}
