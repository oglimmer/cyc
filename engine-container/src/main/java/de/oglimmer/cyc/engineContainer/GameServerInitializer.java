package de.oglimmer.cyc.engineContainer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GameServerInitializer extends ChannelInitializer<SocketChannel> {

	private static StringDecoder DECODER = new StringDecoder();
	private static StringEncoder ENCODER = new StringEncoder();

	private GameServerHandler serverHandler;

	public GameServerInitializer(TcpHandler tcpHandler) {
		serverHandler = new GameServerHandler(tcpHandler);
	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", DECODER);
		pipeline.addLast("encoder", ENCODER);

		pipeline.addLast("handler", serverHandler);
	}
}