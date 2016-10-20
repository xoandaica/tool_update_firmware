package vn.vnpttech.ssdc.nms.mediation.stbacs.myobject;

import java.util.ArrayList;

public class WanServiceObject {

    public final static String IPoE_TYPE = "IPoE";
    public final static String PPPoE_TYPE = "PPPoE";
    public final static String Bridge_TYPE = "Bridge";
    public final static String IPoA_TYPE = "IPoA";
    public final static String PPPoA_TYPE = "PPPoA";

    private Layer2InterfaceObject layer2Interface = new Layer2InterfaceObject();

    private String type;
    private String path;

    private int instance;

    private ArrayList<SimpleObject> listObject = new ArrayList<SimpleObject>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Layer2InterfaceObject getLayer2Interface() {
        return layer2Interface;
    }

    public void setLayer2Interface(Layer2InterfaceObject layer2Interface) {
        this.layer2Interface = layer2Interface;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getInstance() {
        return instance;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }

    public ArrayList<SimpleObject> getListObject() {
        return listObject;
    }

    public void setListObject(ArrayList<SimpleObject> listObject) {
        this.listObject = listObject;
    }

}
