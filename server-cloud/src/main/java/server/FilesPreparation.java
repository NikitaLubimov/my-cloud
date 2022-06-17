package server;

import model.FileInfoServer;

import java.nio.file.Path;

public class FilesPreparation {
    public static final String DIRECTORY = ".Server_files";

    public FilesPreparation () {

    }

    private FileInfoServer prepareFileInfoServer (Path path) {
        FileInfoServer fis = new FileInfoServer();
        fis.setFilename(path.getFileName().toString());

    }
}
