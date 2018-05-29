package xyz.un4ckn0wl3z;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
//        if (args.length!=1){
//            System.err.println(
//                    "Usage: " +EchoServer.class.getSimpleName() + "<port>"
//            );
//        }
//        int port = Integer.parseInt(args[0]);
        new EchoServer(8000).start();
    }

    public void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup(); // create EventLoopGroup
        try {
            ServerBootstrap b = new ServerBootstrap(); // create the ServerBootstrap
            b.group(group)
                    .channel(NioServerSocketChannel.class) // specifies NIO Channel for use
                    .localAddress(new InetSocketAddress(port)) // send socket address using specified port
                    .childHandler( // add channel pipeline
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {
                                    socketChannel.pipeline().addLast(serverHandler); // EchoServerHandler  is @Sharable so can always use the same one.
                                }
                            }
                    );
            ChannelFuture f = b.bind().sync(); // Binds the server
            f.channel().closeFuture().sync(); // Gets the CloseFuture of the Channel and blocks the current  thread until it’s complete
        }finally {
            group.shutdownGracefully().sync(); // Shuts down the EventLoopGroup, releasing all resources
        }
    }
}
