package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket) throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client connect");
    }

    @Override
    public void run() {
        try {
            while (true) {
                String msg = in.readUTF();
            }
        } catch (Exception e) {
            System.out.println("Connection was broken");
        }

    }
}
