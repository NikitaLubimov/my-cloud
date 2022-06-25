package model;

public class FileDownloadInServer implements CloudMessage {

    private String fileName;

    public FileDownloadInServer(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
