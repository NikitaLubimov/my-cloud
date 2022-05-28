package ru.gb.cloudapplication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {

    private final int port;

    private DataInputStream in;
    private DataOutputStream out;

    public Network(int port) throws IOException {
        this.port = port;
        Socket socket = new Socket("localhost",port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public String readMSG() throws IOException {
        return in.readUTF();
    }

    public void writeMSG (String msg) throws IOException {
        out.writeUTF(msg);
        out.flush();
    }
}
