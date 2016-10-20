package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.util.ArrayList;

public class Command {

    public final static String USERNAME_DEFAULT = "xuan";
    public final static String PASSWORD_DEFAULT = "123";

    //return Value
    ArrayList<Object> listParams;

    //command duoc apply tren CPE nao
    protected String serialNumberCPE;

    //connectionRequest URL
    protected String connectionRequestURL;

    //user name connect CPE
    protected String usernameCPE;

    //password connect CPE
    protected String passwordCPE;

    //loai command: GetParameter, SetParameter, AddObject, Download
    protected String type;

    //check Command da nhan duoc ket qua
    protected boolean hasResult = false;

    //check loi
    protected boolean errorCheck = true;

	//thong bao loi
    protected String errorString;

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

    public boolean isHasResult() {
        return hasResult;
    }

    public void setHasResult(boolean hasResult) {
        this.hasResult = hasResult;
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

    public ArrayList<Object> getListParam() {
        return listParams;
    }

    public void addParamObject(Object paraOb) {
        this.listParams.add(paraOb);
    }

    public void setListParam(ArrayList<Object> listParam) {
        this.listParams = listParam;
    }

    public void deleteAllParamObject() {
        this.listParams.clear();
    }

    public boolean executeCommand() throws Exception {
        FactoryCommand.addCommand(this);
        if (getConnectionRequestURL() == null) {
            return true;
        }
        if (getConnectionRequestURL().equals("")) {
            return true;
        }
        ConnectionCPE.RequestConnectionHttp(getConnectionRequestURL(), getUsernameCPE(), getPasswordCPE());

//		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        System.out.println("ID 1111 Thread: " + Thread.currentThread().getId() + "_NAME: " + Thread.currentThread().getName());
        synchronized (this) {
            while (!hasResult) {
                wait(5000);
            }
        }

        return errorCheck;
    }

    public synchronized void receiveError(String errorString) {
        hasResult = true;
        errorCheck = true;
        this.errorString = errorString;
        FactoryCommand.removeCommand(this);
        notify();
    }

    public synchronized void receiveResult(ArrayList<Object> listParam) {
        System.out.println("ID 22222 Thread: " + Thread.currentThread().getId() + "_NAME: " + Thread.currentThread().getName());
        this.listParams = listParam;
        System.out.println("REceive result true");
        hasResult = true;
        errorCheck = false;
        FactoryCommand.removeCommand(this);
        notify();
    }
}
