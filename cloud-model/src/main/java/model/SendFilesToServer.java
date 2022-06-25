package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SendFilesToServer implements CloudMessage{

    private final long size;
    private final byte[] data;
    private final String name;

    public SendFilesToServer(Path path) throws IOException {
        this.size = Files.size(path);
        this.data = Files.readAllBytes(path);
        this.name = path.getFileName().toString();
    }

    public SendFilesToServer (byte[] data , String name) {
        this.data = data;
        this.size = data.length;
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public byte[] getData() {
        return data;
    }

    public String getName() {
        return name;
    }

}
