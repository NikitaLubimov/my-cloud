package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.*;
import server.FilesStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {

    public  CloudFileHandler (FilesStorage filesStorage) {
        this.filesStorage = filesStorage;
    }

    private Path currentDir = Path.of("Server_files");
    private FilesStorage filesStorage;

//    public CloudFileHandler () {
//        currentDir = Path.of("Server_files");
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        if (cloudMessage instanceof FileRequest fileRequest) {
            ctx.writeAndFlush(new ServerFilesListData(filesStorage.getFilesOnServer(currentDir)));
        } else if (cloudMessage instanceof FileMessage fileMessage) {

        }
    }
}
