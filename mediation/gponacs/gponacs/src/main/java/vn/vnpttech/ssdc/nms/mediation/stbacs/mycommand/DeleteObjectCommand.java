package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

public class DeleteObjectCommand extends Command {

    private String path;

    public DeleteObjectCommand(String serialNumber, String connectionRequestURL, String path, String username, String password) {
        this.serialNumberCPE = serialNumber;
        this.connectionRequestURL = connectionRequestURL;
        this.usernameCPE = username;
        this.passwordCPE = password;
        this.path = path;
        this.type = Command.TYPE_DELETEOBJECT;
    }

    public DeleteObjectCommand(String serialNumber, String path, String username, String password) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = username;
        this.passwordCPE = password;
        this.path = path;
        this.type = Command.TYPE_DELETEOBJECT;
    }

    public DeleteObjectCommand(String serialNumber, String path) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = Command.USERNAME_DEFAULT;
        this.passwordCPE = Command.PASSWORD_DEFAULT;
        this.path = path;
        this.type = Command.TYPE_DELETEOBJECT;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
