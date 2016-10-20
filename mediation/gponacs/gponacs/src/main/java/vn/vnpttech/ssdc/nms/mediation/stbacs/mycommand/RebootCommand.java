package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

public class RebootCommand extends Command {

    public RebootCommand(String serialNumber, String connectionRequestURL, String username, String password) {
        this.serialNumberCPE = serialNumber;
        this.connectionRequestURL = connectionRequestURL;
        this.usernameCPE = username;
        this.passwordCPE = password;
        this.type = Command.TYPE_REBOOT;
    }
    
    public RebootCommand(String serialNumber) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = Command.USERNAME_DEFAULT;
        this.passwordCPE = Command.PASSWORD_DEFAULT;
        this.type = Command.TYPE_REBOOT;
    }
}
