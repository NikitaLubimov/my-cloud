package model;


import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class FileInfoServer implements CloudMessage {
    public static final String HOME_DIR_NAME = " . . ";

    private String fileName;
    private long size;
    private boolean isDir;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }


    @Override
    public String toString() {
        return "ServerFile{" +
                (isDir ? "directory='": "fileName='")
                + fileName + '\'' +
                ", size=" + size + " bytes"+
                '}';
    }
}
