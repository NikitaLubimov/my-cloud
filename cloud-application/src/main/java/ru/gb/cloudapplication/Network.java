package ru.gb.cloudapplication;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import model.*;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Network {

    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private MainController mainController;

    public Network(int port, MainController controller) throws IOException {
        Socket socket = new Socket("localhost", port);
        os = new ObjectEncoderOutputStream(socket.getOutputStream());
        is = new ObjectDecoderInputStream(socket.getInputStream());
        mainController = controller;
    }

    public CloudMessage read() throws IOException, ClassNotFoundException {
        return (CloudMessage) is.readObject();
    }

    public void write(CloudMessage msg) throws IOException {
        os.writeObject(msg);
        os.flush();
    }

    public void msgReader() throws IOException, ClassNotFoundException {
        while (true) {
            try {
                CloudMessage msg = read();
                if (msg instanceof ServerFilesListData serverFilesListData) {
                    updateServerFilesList(serverFilesListData);
                } else  if (msg instanceof SendFilesToServer) {
                    downFileInServer((SendFilesToServer) msg);
                }
                mainController.updateList(Path.of(mainController.getCurrentPath()));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateServerFilesList(ServerFilesListData serverFilesListData) {
        mainController.updateServerList(serverFilesListData.getFilelist());
    }

    public void pathChangeRequest(FileInfoServer fis) throws IOException {
        if (fis.getFileName().equals(FileInfoServer.HOME_DIR_NAME)) {
            write(new ServerPathUpRequest());
        } else {
            write(new ServerPathInRequest(fis.getFileName()));
        }
    }

    public void sendFileToServer(Path path) throws IOException {
        write(new SendFilesToServer(path));
    }

    public void requestFilesToServer() throws IOException {
        write(new RequestFilesToServer());
    }

    public void getFileFromServer(String name) throws IOException {
        write(new FileDownloadInServer(name));
    }

    private void downFileInServer (SendFilesToServer sendFilesToServer) {
        Path newFile = Path.of(mainController.getCurrentPath()).resolve(sendFilesToServer.getName());
        try {
            Files.write(newFile,sendFilesToServer.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
