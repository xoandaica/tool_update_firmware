package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

import java.util.ArrayList;

import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;

public class AddObjectCommand extends Command {

    public final static String ADDOBJ_LAYER2 = "add_object_layer2";
    public final static String ADDOBJ_WANSERVICE = "add_wan_service";
    public final static String ADDOBJ_IPLEASE = "add_ip_lease";
    public final static String ADDOBJ_BRIDGE = "add_brigde";
    public final static String ADDOBJ_LAYER3 = "add_object_layer3";

    private String type_addObj;
    private String rootName;
    private int instance;
    private final ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
    //trong truong hop add layer2 va add wan can lay them thong tin ifname
    public SimpleObject ifnameObject;
    //= new SimpleObject();

    public AddObjectCommand(String serialNumber, String connectionRequestURL, String rootName, ArrayList<SimpleObject> listObj, String typeAddObj, String username, String password) {
        this.serialNumberCPE = serialNumber;
        this.connectionRequestURL = connectionRequestURL;
        this.rootName = rootName;
        this.usernameCPE = username;
        this.passwordCPE = password;
        this.type = Command.TYPE_ADDOBJECT;
        this.type_addObj = typeAddObj;

        this.setListObj(listObj);
    }

    public String getType_addObj() {
        return type_addObj;
    }

    public void setType_addObj(String type_addObj) {
        this.type_addObj = type_addObj;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public int getInstance() {
        return instance;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }

    public ArrayList<SimpleObject> getListObj() {
        return listObj;
    }

    public final void setListObj(ArrayList<SimpleObject> listObj) {
        this.listObj.clear();
        if (listObj != null) {
            this.listObj.addAll(listObj);
        }
    }

}
