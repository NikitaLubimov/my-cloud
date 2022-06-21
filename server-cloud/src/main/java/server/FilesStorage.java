package server;

import model.FileInfoServer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FilesStorage {

    public static final String DIRECTORY = "Server_files";

    private FileInfoServer prepareFileInfo(Path path) {
        FileInfoServer fis = new FileInfoServer();
        fis.setFileName(path.getFileName().toString());
        fis.setDir(Files.isDirectory(path));
        try {
            fis.setSize(fis.isDir() ? -1L : Files.size(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fis;
    }

    private FileInfoServer prepareParentDirFile(Path currentDirectory) {
        FileInfoServer parent = new FileInfoServer();
        return parent;
    }


    public List<FileInfoServer> getFilesOnServer(Path currentDirectory) {
        return (List<FileInfoServer>) prepareFileInfo(currentDirectory);
    }
}