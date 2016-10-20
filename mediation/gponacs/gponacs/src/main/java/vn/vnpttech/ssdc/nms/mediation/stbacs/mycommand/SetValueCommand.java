package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

import java.util.ArrayList;

import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;

public class SetValueCommand extends Command {

    private ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();

    public SetValueCommand(String serialNumber, String connectionRequestURL, ArrayList<SimpleObject> listObj, String username, String password) {
        this.serialNumberCPE = serialNumber;
        this.connectionRequestURL = connectionRequestURL;
        this.listObj = listObj;
        this.usernameCPE = username;
        this.passwordCPE = password;
        this.type = Command.TYPE_SETVALUE;
    }

    public SetValueCommand(String serialNumber, ArrayList<SimpleObject> listObj, String username, String password) {
        this.serialNumberCPE = serialNumber;
        this.listObj = listObj;
        this.usernameCPE = username;
        this.passwordCPE = password;
        this.type = Command.TYPE_SETVALUE;
    }

    public SetValueCommand(String serialNumber, ArrayList<SimpleObject> listObj) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = Command.USERNAME_DEFAULT;
        this.passwordCPE = Command.PASSWORD_DEFAULT;
        this.listObj = listObj;
        this.type = Command.TYPE_SETVALUE;

    }

    public ArrayList<SimpleObject> getListObj() {
        return listObj;
    }

    public void setListObj(ArrayList<SimpleObject> listObj) {
        this.listObj = listObj;
    }

}
