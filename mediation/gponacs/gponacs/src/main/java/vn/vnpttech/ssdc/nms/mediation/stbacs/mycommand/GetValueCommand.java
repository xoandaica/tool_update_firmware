package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetValueCommand extends Command {

    private Map<String, String> returnValue;
    private final List<String> params = new ArrayList<String>();

    public GetValueCommand(String serialNumber) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = Command.USERNAME_DEFAULT;
        this.passwordCPE = Command.PASSWORD_DEFAULT;
        this.type = Command.TYPE_GETTREEVALUE;
    }

    public GetValueCommand(String serialNumber, String param) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = Command.USERNAME_DEFAULT;
        this.passwordCPE = Command.PASSWORD_DEFAULT;
        this.type = Command.TYPE_GETTREEVALUE;

        this.params.add(param);
    }

    public GetValueCommand(String serialNumber, List<String> params) {
        this.serialNumberCPE = serialNumber;
        this.usernameCPE = Command.USERNAME_DEFAULT;
        this.passwordCPE = Command.PASSWORD_DEFAULT;
        this.type = Command.TYPE_GETTREEVALUE;

        this.params.addAll(params);
    }

    public Map<String, String> getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Map<String, String> returnValue) {
        this.returnValue = returnValue;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params.clear();
        if (params != null) {
            this.params.addAll(params);
        }
    }

    public void addParam(String param) {
        this.params.add(param);
    }

}
