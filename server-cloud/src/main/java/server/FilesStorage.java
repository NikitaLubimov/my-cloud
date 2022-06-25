package server;

import model.FileInfoServer;
import model.SendFilesToServer;
import model.ServerPathInRequest;

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
        List<FileInfoServer> list = new ArrayList<>();
        File folder = new File(currentDirectory.toString());
        File [] files = folder.listFiles();

        assert files != null;
        for (File fl:files) {
            list.add(prepareFileInfo(fl.toPath()));
        }
        return list;
    }

    public Path currentDirUp (Path path) {
        return path.getParent();
    }

    public Path getUserPathStart () {
        return Path.of(DIRECTORY);
    }

    public Path currentDirIn (ServerPathInRequest spir, Path path) {
        Path newPath = path.resolve(spir.getNameDirect());
        return newPath;
    }

    public void saveFile (SendFilesToServer sendFilesToServer, Path currentDirectory) throws IOException {
        Files.write(currentDirectory.resolve(sendFilesToServer.getName()),sendFilesToServer.getData());
    }

    public byte[] getFileData (String name, Path path) throws IOException {
        Path pathFile = path.resolve(name);

        if (!Files.isDirectory(pathFile)) {
            throw new RuntimeException("Выбранное является деректорией");
        }

        return Files.readAllBytes(pathFile);
    }
}