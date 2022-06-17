package model;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInfoServer {

    public enum FileTypeServer {
        FILE("F"), DIRECTORY("D");

        private String name;

        FileTypeServer(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private String filename;
    private FileTypeServer fileType;
    private long filesize;

    public String getFilename() {
        return filename;
    }

    public FileTypeServer getFileType() {
        return fileType;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFileType(FileTypeServer fileType) {
        this.fileType = fileType;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public FileInfoServer(Path path) {
        try {
            this.filename = path.getFileName().toString();
            this.filesize = Files.size(path);
            this.fileType = Files.isDirectory(path) ? FileTypeServer.DIRECTORY : FileTypeServer.FILE;

            if (this.fileType == FileTypeServer.DIRECTORY) {
                this.filesize = -1l;
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to create file info from path");
        }
    }
}
