package de.oglimmer.cyc.engineContainer;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
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
		try {
			Message msg = new Message(request);
			checkpassword(msg);

			if ("exit".equals(msg.getCommand())) {
				response = tcpHandler.handleExit();
			} else if ("status".equals(msg.getCommand())) {
				response = tcpHandler.handleStatus(false);
			} else if ("extstatus".equals(msg.getCommand())) {
				response = tcpHandler.handleStatus(true);
			} else if ("version".equals(msg.getCommand())) {
				response = tcpHandler.handleVersion();
			} else {
				response = tcpHandler.handleRunGame(msg.getCommand());
			}

		} catch (NotAuthorizedException e) {
			log.info("UnauthorizedException. {}", request);
			response = "Unauthorized";
		}
		ChannelFuture future = ctx.write(response);
		future.addListener(ChannelFutureListener.CLOSE);
	}

	private void checkpassword(Message msg) throws NotAuthorizedException {
		String password = EngineContainerProperties.INSTANCE.getEnginePassword();
		if (password != null && !password.isEmpty() && !password.equals(msg.getAuthorization())) {
			throw new NotAuthorizedException();
		}
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

	class Message {
		@Getter
		private String authorization;
		@Getter
		private String command;

		public Message(String request) {
			if (request.startsWith("Authorization:")) {
				String passToCheck = request.substring("Authorization:".length());
				if (passToCheck.indexOf(';') > -1) {
					passToCheck = passToCheck.substring(0, passToCheck.indexOf(";"));
					command = request.substring(request.indexOf(';') + 1);
				}
				authorization = passToCheck.trim();
			} else {
				command = request;
			}
		}
	}
}