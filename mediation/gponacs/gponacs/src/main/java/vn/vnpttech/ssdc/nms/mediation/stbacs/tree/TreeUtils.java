package vn.vnpttech.ssdc.nms.mediation.stbacs.tree;

import java.util.ArrayList;

import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.Layer2InterfaceObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;

public class TreeUtils {

    public Root createTree(Root input, String pathLeaf, String value) {

        ArrayList<String> elements = DataFileUtils.getElementTreeNodeFromString(pathLeaf);

        if (!input.getNameID().equals(elements.get(0))) {
            return null;
        }
        TreeNode pointNode = input;
        for (int i = 1; i < elements.size(); i++) {
            if (pointNode.getType().equals(TreeNode.ROOT)) {
                Root rot = (Root) pointNode;
                if (rot.getChildNode(elements.get(i)) == null) {
                    Node nod = new Node(elements.get(i));
                    nod.setPath(rot.getPath() + "." + elements.get(i));
                    rot.getNextNode().add(nod);
                    pointNode = nod;
                } else {
                    pointNode = rot.getChildNode(elements.get(i));
                }
            } else {
                Node nod = (Node) pointNode;
                if (nod.getChildNode(elements.get(i)) == null) {
                    if (i != (elements.size() - 1)) {
                        Node childNode = new Node(elements.get(i));
                        childNode.setPath(nod.getPath() + "." + elements.get(i));
                        nod.getNextNode().add(childNode);
                        pointNode = childNode;
                    } else {
                        Leaf leafNode = new Leaf(elements.get(i));
                        leafNode.setPath(nod.getPath() + "." + elements.get(i));
                        leafNode.setValue(value);
                        nod.getNextNode().add(leafNode);
                        pointNode = leafNode;
                    }
                } else {
                    pointNode = nod.getChildNode(elements.get(i));
                }
            }
        }
        return input;
    }

    public TreeNode getNodeFromPath(Root input, String path) {
        ArrayList<String> elements = DataFileUtils.getElementTreeNodeFromString(path);
        TreeNode pointNode = input;
        for (int i = 1; i < elements.size(); i++) {
            String element = elements.get(i);
            if (pointNode.getType().equals(TreeNode.ROOT)) {
                Root rot = (Root) pointNode;
                TreeNode tmp = rot.getChildNode(element);
                if (tmp != null) {
                    pointNode = tmp;
                } else {
                    return null;
                }
            } else if (pointNode.getType().equals(TreeNode.NODE)) {
                Node nod = (Node) pointNode;
                TreeNode tmp = nod.getChildNode(element);

                if (tmp != null) {
                    pointNode = tmp;
                } else {
                    return null;
                }
            }
        }
        return pointNode;
    }

    public ArrayList<Integer> getListInstanceFromPath(Root input, String path) throws Exception {
        ArrayList<Integer> returnValue = new ArrayList<Integer>();

        TreeNode tnode = getNodeFromPath(input, path);
        if (tnode instanceof Root) {
            throw new Exception("Path invalid: " + path);
        } else if (tnode instanceof Node) {
            Node nod = (Node) tnode;
            for (int i = 0; i < nod.getNextNode().size(); i++) {
                returnValue.add(Integer.parseInt(nod.getNextNode().get(i).getNameID()));
            }
            return returnValue;
        } else {
            return null;
        }
    }

    public String getValueFromPath(Root input, String path) {
        ArrayList<String> elements = DataFileUtils.getElementTreeNodeFromString(path);
        TreeNode pointNode = input;
        for (int i = 1; i < elements.size(); i++) {
            String element = elements.get(i);
            if (pointNode.getType().equals(TreeNode.ROOT)) {
                Root rot = (Root) pointNode;
                TreeNode tmp = rot.getChildNode(element);
                if (tmp != null) {
                    pointNode = tmp;
                } else {
                    return null;
                }
            } else if (pointNode.getType().equals(TreeNode.NODE)) {
                Node nod = (Node) pointNode;
                TreeNode tmp = nod.getChildNode(element);
                if (tmp != null) {
                    pointNode = tmp;
                } else {
                    return null;
                }
            }
        }
        //
        return ((Leaf) pointNode).getValue();
    }

    public SimpleObject getValue(Root input, SimpleObject data) {
        data.setValue(getValueFromPath(input, data.getParameter()));
        return data;
    }

    public ArrayList<SimpleObject> getArrayValues(Root input, ArrayList<SimpleObject> data) {
        for (SimpleObject temp : data) {
            temp.setValue(getValueFromPath(input, temp.getParameter()));
        }
        return data;
    }

    public ArrayList<Layer2InterfaceObject> getArrayValues_2(Root input, ArrayList<Layer2InterfaceObject> data) {
        for (Layer2InterfaceObject layer2 : data) {
            ArrayList<SimpleObject> dataset = layer2.getListObject();
            for (SimpleObject simpleObj : dataset) {
                simpleObj.setValue(getValueFromPath(input, simpleObj.getParameter()));
            }
        }
        return data;
    }

}
