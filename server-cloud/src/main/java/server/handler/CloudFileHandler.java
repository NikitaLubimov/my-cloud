package server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import model.*;
import server.FilesStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {

    private Path currentDir;
    private Path currentDirUser;
    private FilesStorage filesStorage;

    public  CloudFileHandler (FilesStorage filesStorage) {
        this.filesStorage = filesStorage;
        this.currentDir = filesStorage.getUserPathStart();
    }




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        if (cloudMessage instanceof FileRequest fileRequest) {
            ctx.writeAndFlush(new ServerFilesListData(filesStorage.getFilesOnServer(currentDir)));
        } else if (cloudMessage instanceof ServerPathUpRequest) {
            if (currentDir != currentDirUser) {
                currentDir = filesStorage.currentDirUp(currentDir);
                ctx.writeAndFlush(new ServerFilesListData(filesStorage.getFilesOnServer(currentDir)));
            }
        } else if (cloudMessage instanceof  ServerPathInRequest spir) {
            currentDir = filesStorage.currentDirIn(spir,currentDir);
            ctx.writeAndFlush(new ServerFilesListData(filesStorage.getFilesOnServer(currentDir)));
        }
    }
}
