package vn.vnpttech.ssdc.nms.mediation.stbacs.tree;

import java.util.ArrayList;

public class Root extends TreeNode {

    private ArrayList<TreeNode> nextNode = new ArrayList<TreeNode>();
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    public Root(String nameID) {
        super(nameID);
        this.type = TreeNode.ROOT;
        this.path = nameID;
    }

    public ArrayList<TreeNode> getNextNode() {
        return nextNode;
    }

    public void setNextNode(ArrayList<TreeNode> nextNode) {
        this.nextNode = nextNode;
    }

    public TreeNode getChildNode(String idNode) {
        for (TreeNode node : nextNode) {
            if (node.getNameID().equals(idNode)) {
                return node;
            }
        }
        return null;
    }
}
