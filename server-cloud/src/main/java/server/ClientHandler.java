package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ClientHandler implements Runnable{


    private final String serverDir = "server_files";
    private DataInputStream in;
    private DataOutputStream out;

    public ClientHandler(Socket socket) throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client connect");
        List<String> files = getFiles(serverDir);
        for (String file : files) {
            out.writeUTF(file);
        }
        out.flush();
    }

    private List<String> getFiles(String dir) {
        String[] list = new File(dir).list();
        assert list!= null;
        return Arrays.asList(list);
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
