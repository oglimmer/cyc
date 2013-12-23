package de.oglimmer.cyc.engineContainer;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Sharable
public class GameServerHandler extends SimpleChannelInboundHandler<String> {

	private TcpHandler tcpHandler;

	public GameServerHandler(TcpHandler tcpHandler) {
		this.tcpHandler = tcpHandler;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, String request) {

		String response;
		if ("exit".equals(request)) {
			response = tcpHandler.handleExit();
		} else if ("status".equals(request)) {
			response = tcpHandler.handleStatus(false);
		} else if ("extstatus".equals(request)) {
			response = tcpHandler.handleStatus(true);
		} else if ("version".equals(request)) {
			response = tcpHandler.handleVersion();
		} else {
			response = tcpHandler.handleRunGame(request);
		}

		ChannelFuture future = ctx.write(response);

		future.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.debug("Unexpected exception from downstream.", cause);
		ctx.close();
	}

}