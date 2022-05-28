package ru.gb.cloudapplication;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInfo {

    public enum FileType {
        FILE("F"), DIRECTORY("D");

        private String name;

        FileType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private String filename;
    private FileType fileType;
    private long filesize;

    public String getFilename() {
        return filename;
    }

    public FileType getFileType() {
        return fileType;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public FileInfo(Path path) {
        try {
            this.filename = path.getFileName().toString();
            this.filesize = Files.size(path);
            this.fileType = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;

            if (this.fileType == FileType.DIRECTORY) {
                this.filesize = -1l;
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to create file info from path");
        }
    }
}
