package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class MainStringInBoundHandler extends SimpleChannelInboundHandler<String> {

    private final SimpleDateFormat format;

    public MainStringInBoundHandler() {
        format = new SimpleDateFormat();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String str) throws Exception {
      log.debug("received: {}", str);
      String dateString = "{" + format.format(new Date()) + "]";
      str = dateString + str;
      log.debug("prcessed message: {}", str);
      ctx.writeAndFlush(str);
    }
}
