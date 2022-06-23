package ru.gb.cloudapplication;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;
import javafx.application.Platform;
import model.CloudMessage;
import model.FileInfoServer;
import model.ServerFilesListData;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class Network {

    private ObjectDecoderInputStream is;
    private ObjectEncoderOutputStream os;
    private MainController mainController;

    public Network(int port, MainController controller) throws IOException {
        Socket socket = new Socket("localhost",port);
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
             }
         } catch (IOException | ClassNotFoundException e) {
             e.printStackTrace();
         }
     }
    }

    private void updateServerFilesList(ServerFilesListData serverFilesListData) {
        mainController.updateServerList(serverFilesListData.getFilelist());
    }
}
