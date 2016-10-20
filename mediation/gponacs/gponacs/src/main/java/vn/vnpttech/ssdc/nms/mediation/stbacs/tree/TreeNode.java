package vn.vnpttech.ssdc.nms.mediation.stbacs.tree;

public class TreeNode {

    public final static String ROOT = "root";
    public final static String NODE = "node";
    public final static String LEAF = "leaf";

    protected String type;//3 type

    protected String path;

    protected String nameID;

    public TreeNode() {
    }

    public TreeNode(String nameID) {
        this.nameID = nameID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameID() {
        return nameID;
    }

    public void setNameID(String nameID) {
        this.nameID = nameID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
