package com.bdp.tx.framework.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 客户端向服务器发送消息工具类
 */
public class SocketUtils {

	/**
	 * 把消息转成JSON字符串
	 * 
	 * @param msg
	 * @return
	 */
	public static String getJson(Object msg) {
		String json;
		try {
			ByteBuf buf = (ByteBuf) msg;
			byte[] bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			json = new String(bytes);
		} finally {
			ReferenceCountUtil.release(msg);
		}
		return json;

	}

	/**
	 * 向tm发送消息
	 * 
	 * @param ctx
	 * @param msg
	 */
	public static void sendMsg(final ChannelHandlerContext ctx, final String msg) {
		ctx.writeAndFlush(Unpooled.buffer().writeBytes(msg.getBytes()));
	}

	// 这个方法没有到
	public static void sendMsg(final Channel ctx, final String msg) {
		ctx.writeAndFlush(Unpooled.buffer().writeBytes(msg.getBytes()));
	}
}
