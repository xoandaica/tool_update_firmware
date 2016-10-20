package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

public class UpdateFirmwareCommand extends Command {

    private String urlFileServer;
    private String fileVersion;
    private String usernameFileServer;
    private String passwordFileServer;
    private int downloadResponseStt;

    private int faultcode;
    private String faultstring;

    public UpdateFirmwareCommand(String serialNumber,
            String usernameCPE, String passwordCPE,
            String urlFileServer, String fileVersion,
            String usernameFileServer, String passwordFileServer) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = usernameCPE;
        this.passwordCPE = passwordCPE;
        this.urlFileServer = urlFileServer;
        this.usernameFileServer = usernameFileServer;
        this.passwordFileServer = passwordFileServer;
        this.type = Command.TYPE_UPGRADE_FW;
    }

    public UpdateFirmwareCommand(String serialNumber) {
        this.usernameCPE = Command.USERNAME_DEFAULT;
        this.passwordCPE = Command.PASSWORD_DEFAULT;
        this.serialNumberCPE = serialNumber;
        this.type = Command.TYPE_UPGRADE_FW;
    }

    public String getUrlFileServer() {
        return urlFileServer;
    }

    public void setUrlFileServer(String urlFileServer) {
        this.urlFileServer = urlFileServer;
    }

    public String getUsernameFileServer() {
        return usernameFileServer;
    }

    public void setUsernameFileServer(String usernameFileServer) {
        this.usernameFileServer = usernameFileServer;
    }

    public String getPasswordFileServer() {
        return passwordFileServer;
    }

    public void setPasswordFileServer(String passwordFileServer) {
        this.passwordFileServer = passwordFileServer;
    }

    public int getFaultcode() {
        return faultcode;
    }

    public void setFaultcode(int faultcode) {
        this.faultcode = faultcode;
    }

    public String getFaultstring() {
        return faultstring;
    }

    public void setFaultstring(String faultstring) {
        this.faultstring = faultstring;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }

    public int getDownloadResponseStt() {
        return downloadResponseStt;
    }

    public void setDownloadResponseStt(int downloadResponseStt) {
        this.downloadResponseStt = downloadResponseStt;
    }

    @Override
    public synchronized boolean executeCommand() throws Exception {
        boolean ok = super.executeCommand();
//        if (ok) {
//            // keep in factory to receive notify success completely
//            CommandRequestFactory.addCommand(this);
//        }
        
        return ok;
    }

    
}
