package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

import java.util.HashMap;

import vn.vnpttech.ssdc.nms.mediation.stbacs.exception.AlreadyEnqueuedException;
import org.apache.log4j.Logger;
import vn.vnpttech.ssdc.nms.mediation.stbacs.main.XmppManager;

public class Command {

    protected static final StringBuilder sb = new StringBuilder();
    protected static final Logger logger = Logger.getLogger(Command.class);
    private final HashMap<String, String> keyMap = new HashMap<String, String>();

    public final static String USERNAME_DEFAULT = "admin";
    public final static String PASSWORD_DEFAULT = "admin";

    public final static String TYPE_GETTREEVALUE = "Get Tree Value";
    public final static String TYPE_SETVALUE = "Set Value";
    public final static String TYPE_ADDOBJECT = "Add Object";
    public final static String TYPE_DELETEOBJECT = "Delete Object";
    public final static String TYPE_UPGRADE_FW = "Upgrade firmware";
    public final static String TYPE_REBOOT = "Reboot Device";
    public final static String TYPE_ZERO_TOUCH = "Zero Touch";
    public final static String TYPE_PERIODIC = "Get Performance and update firmware";

    //command duoc apply tren CPE nao
    protected String serialNumberCPE;

    //connectionRequest URL
    protected String connectionRequestURL;

    //user name connect CPE
    protected String usernameCPE;

    //password connect CPE
    protected String passwordCPE;

    //loai command:
    protected String type;

    // device version
    protected String cpeVersion;

    // request timeout
    protected long timeout;

    //check Command da nhan duoc ket qua
    //protected boolean hasResult = false;
    //check loi
    protected boolean errorCheck = true;

    //thoi gian ton tai
    public long timeExist;

    //thong bao loi
    protected String errorString;

    protected Exception exception;

    private int step;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNumberCPE() {
        return serialNumberCPE;
    }

    public void setSerialNumberCPE(String serialNumberCPE) {
        this.serialNumberCPE = serialNumberCPE;
    }

    public String getConnectionRequestURL() {
        return connectionRequestURL;
    }

    public void setConnectionRequestURL(String connectionRequestURL) {
        this.connectionRequestURL = connectionRequestURL;
    }

    public String getUsernameCPE() {
        return usernameCPE;
    }

    public void setUsernameCPE(String usernameCPE) {
        this.usernameCPE = usernameCPE;
    }

    public String getPasswordCPE() {
        return passwordCPE;
    }

    public void setPasswordCPE(String passwordCPE) {
        this.passwordCPE = passwordCPE;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isErrorCheck() {
        return errorCheck;
    }

    public void setErrorCheck(boolean errorCheck) {
        this.errorCheck = errorCheck;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public Exception getException() {
        if (exception == null) {
            exception = new Exception(errorString);
        }
        return exception;
    }

    public synchronized boolean executeCommand() throws Exception {
        try {
            // check if already requested
            if (CommandRequestFactory.getCommand(serialNumberCPE) != null) {
                throw new AlreadyEnqueuedException("CPE is requested by another");
            }

            long startTime = System.currentTimeMillis();
            String requestTimeout = System.getProperty("ACS_REQUEST_TIMEOUT", "1");
            timeout = Long.parseLong(requestTimeout) * 60 * 1000;

            CommandRequestFactory.addCommand(this);
            // send connection request (must set packetId --> id)
            //String packetId = XmppManager.getInstance().sendConnectionRequest(serialNumberCPE, usernameCPE, passwordCPE);
            ConnectionCPE.RequestConnectionHttp(getConnectionRequestURL(), getUsernameCPE(), getPasswordCPE());
            //setId(packetId);

            sb.setLength(0);
            sb.append(getType())
                    .append(", SerialNumber=")
                    .append(getSerialNumberCPE())
                    .append("::")
                    .append(getUsernameCPE())
                    .append("/")
                    .append(getPasswordCPE())
//                    .append(", PacketID=")
//                    .append(packetId)
                    .append(", Command waiting for (ms): ")
                    .append(timeout)
                    .append("ms");

            logger.info(sb);

            wait(timeout);
            if (System.currentTimeMillis() - startTime > timeout) {
                sb.setLength(0);
                sb.append(this.getType())
                        .append(", SerialNumber=")
                        .append(getSerialNumberCPE())
                        .append(", Timeout: ACS_REQUEST_TIMEOUT=")
                        .append(timeout)
                        .append("ms");
                logger.error(sb);
                receiveError("Timeout");
            } else {
                sb.setLength(0);
                sb.append(this.getType())
                        .append(", SerialNumber=")
                        .append(getSerialNumberCPE())
                        .append(", Success. Execute time: ")
                        .append(System.currentTimeMillis() - startTime)
                        .append("ms");
                logger.info(sb);
            }
        } catch (Exception ex) {
            receiveError(ex.getMessage());
            sb.setLength(0);
            sb.append(this.getType())
                    .append(", SerialNumber=")
                    .append(getSerialNumberCPE())
                    .append(", Error: ")
                    .append(ex.getMessage());
            logger.error(sb, ex);
            throw ex;
        } finally {
            CommandRequestFactory.removeCommand(this);
        }
        return !errorCheck;
    }

    public synchronized void receiveError(String errorString) {
        if (!errorCheck) {
            return;
        }
        errorCheck = true;
        this.errorString = errorString;
        CommandRequestFactory.removeCommand(this);
        notify();
    }

    public void receiveError(Exception ex) {
        if (ex != null) {
            this.exception = ex;
            this.receiveError(ex.getMessage());
        }
    }

    public synchronized void receiveResult() {
        errorCheck = false;
        CommandRequestFactory.removeCommand(this);
        notify();
    }

    public HashMap<String, String> getKeyMap() {
        return keyMap;
    }

    public String getCpeVersion() {
        return cpeVersion;
    }

    public void setCpeVersion(String cpeVersion) {
        this.cpeVersion = cpeVersion;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    /**
     * Increase step one value
     */
    public void nextStep() {
        this.step++;
    }

    protected String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
