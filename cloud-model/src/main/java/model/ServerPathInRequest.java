package model;

public class ServerPathInRequest implements CloudMessage {

    private String nameDirect;

    public ServerPathInRequest(String nameDirect) {
        this.nameDirect = nameDirect;
    }

    public String getNameDirect() {
        return nameDirect;
    }
}
