package xyz.un4ckn0wl3z;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;

@ChannelHandler.Sharable // shared , safe by multiple client
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8)); // log the message to console
        ctx.write(in); // write message to sender without flushing the outbound message
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // Flushes pending messages to the remote peer and closes the channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // Prints the exception stack trace
        ctx.close(); // Closes channel
    }
}
