package model;

import java.util.List;

public class ServerFilesListData implements  CloudMessage{
    private final List<FileInfoServer> filelist;

    public ServerFilesListData(List<FileInfoServer> filelist) {
        this.filelist = filelist;
    }

    public List<FileInfoServer> getFilelist() {
        return filelist;
    }
}
