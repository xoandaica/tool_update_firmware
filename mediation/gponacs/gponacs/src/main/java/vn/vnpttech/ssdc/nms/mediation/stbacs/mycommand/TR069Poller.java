package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.Layer2InterfaceObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.WanServiceObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.Leaf;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.Node;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.Root;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.DataFileUtils;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.TreeNode;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.TreeUtils;
import org.apache.log4j.Logger;

public class TR069Poller {

    protected String serialNumber;
    protected String connectionRequestURL;

    protected final Logger logger = Logger.getLogger(TR069Poller.class);
    protected final StringBuilder sb = new StringBuilder();

    public TR069Poller(String serialNumber, String connectionRequestURL) {
        this.serialNumber = serialNumber;
        this.connectionRequestURL = connectionRequestURL;
    }

    public void listTree(TreeNode rootNode) {
        if (rootNode.getType().equals(TreeNode.ROOT)) {
            Root temp = (Root) rootNode;
            System.out.println("Type: " + temp.getType() + " Path:" + temp.getPath());
            for (int i = 0; i < temp.getNextNode().size(); i++) {
                listTree(temp.getNextNode().get(i));
            }
        } else if (rootNode.getType().equals(TreeNode.NODE)) {
            Node temp = (Node) rootNode;
            System.out.println("Type: " + temp.getType() + " Path:" + temp.getPath());
            for (int i = 0; i < temp.getNextNode().size(); i++) {
                listTree(temp.getNextNode().get(i));
            }
        } else if (rootNode.getType().equals(TreeNode.LEAF)) {
            Leaf temp = (Leaf) rootNode;
            System.out.println("Type: " + temp.getType() + " Path:" + temp.getPath() + " Value: " + temp.getValue());
        }
    }

    public Map<String, String> getTreeValue(String pathTree) throws Exception {
        GetValueCommand cmand = new GetValueCommand(serialNumber, pathTree);
        boolean ok = cmand.executeCommand();
        if (!ok) {
            return cmand.getReturnValue();
        } else {
            String error = org.apache.commons.lang.StringUtils.defaultIfEmpty(cmand.getErrorString(), "Execute command failed");
            throw new Exception(error);
        }
    }

    public Root getTreeRoot(String pathTree) throws Exception {
        Root returnValue = new Root("InternetGatewayDevice");

        GetValueCommand cmand = new GetValueCommand(serialNumber, pathTree);
        boolean ok = cmand.executeCommand();
        if (!ok) {
            TreeUtils myTreeUtil = new TreeUtils();

            Map<String, String> vlue = cmand.getReturnValue();
            for (String element : vlue.keySet()) {
                returnValue = myTreeUtil.createTree(returnValue, element, vlue.get(element));
            }
        }
        return returnValue;
    }

    public ArrayList<String> getDataTreeRoot() throws Exception {
        return getDataTree("InternetGatewayDevice.");
    }

    public ArrayList<String> getDataTree(String dataTree) throws Exception {
        dataTree = (dataTree == null || "".equals(dataTree.trim())) ? "InternetGatewayDevice." : dataTree;
        Map<String, String> datas = getTreeValue(dataTree);
        ArrayList<String> result = new ArrayList<String>();
        SortedSet<String> keys = new TreeSet<String>(datas.keySet());
        for (String key : keys) {
            result.add(key + "=" + datas.get(key));
        }
        return result;
    }

    public boolean rebootDevice() throws Exception {
        RebootCommand cmand = new RebootCommand(serialNumber, connectionRequestURL, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT);
        return (!cmand.executeCommand());
    }

    public boolean upgradeFirmware(String url, String username, String password) throws Exception {
        // check current wifi
        boolean redoEnableWifi = false;
        String dataTree = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.Enable";
        GetValueCommand getCmd = new GetValueCommand(serialNumber, dataTree);
        boolean ok = getCmd.executeCommand();
        if (!ok) {
            Map<String, String> vlue = getCmd.getReturnValue();
            redoEnableWifi = "1".equals(vlue.get(dataTree));
        }
        //InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.Enable
        // turnoff wireless !
        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.Set_EnableWireless);

        ArrayList<SimpleObject> datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //set value
        for (SimpleObject simpleObject : datas) {
            simpleObject.setValue("0");
        }
        boolean rest;
        if (redoEnableWifi) {
            rest = setValueObjects(datas);
            System.out.println("Turn off wifi: " + rest);
            System.out.println("Updating firmware ....");
        }
        UpdateFirmwareCommand cmand = new UpdateFirmwareCommand(serialNumber, connectionRequestURL, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT, url, username, password);
        boolean upgradeStatus = !cmand.executeCommand();

        if (redoEnableWifi) {
            //turn on wireless
            for (SimpleObject simpleObject : datas) {
                simpleObject.setValue("1");
            }
            rest = setValueObjects(datas);
            System.out.println("Turn on wifi: " + rest);
        }

        return upgradeStatus;
    }

    public Integer addObjectValue(String path, ArrayList<SimpleObject> data, String typeAddObj) throws Exception {
        AddObjectCommand cmand = new AddObjectCommand(serialNumber, connectionRequestURL, path, data, typeAddObj, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT);
        if (!cmand.executeCommand()) {
            return cmand.getInstance();
        }
        return null;
    }

    public boolean deleteObject(String path) throws Exception {
        DeleteObjectCommand cmand = new DeleteObjectCommand(serialNumber, connectionRequestURL, path, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT);
        return (!cmand.executeCommand());
    }

    public boolean setValueObjects(ArrayList<SimpleObject> data) throws Exception {
        SetValueCommand cmand = new SetValueCommand(serialNumber, connectionRequestURL, data, Command.USERNAME_DEFAULT, Command.PASSWORD_DEFAULT);
        return (!cmand.executeCommand());
    }

    /* UTILS */
    public static ArrayList<Layer2InterfaceObject> toLayer2LanInterface(Root tempRoot) throws Exception {
        ArrayList<Layer2InterfaceObject> returnValue = new ArrayList<Layer2InterfaceObject>();
        //get data from file
        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();
        //ATM
        String dataFile = tempRoot.getVersion() == 2
                ? FilePath.GET_ETHERNET_INTERFACE_V2
                : FilePath.GET_ETHERNET_INTERFACE;
        ArrayList<String> atmLine = myUtil.getLineFromFile(dataFile);
        String rootName = atmLine.get(0);
        ArrayList<Integer> atmInstances = myTreeUtil.getListInstanceFromPath(tempRoot, rootName);
        if (atmInstances != null) {
            for (Integer atmInstance : atmInstances) {
                Layer2InterfaceObject temp = new Layer2InterfaceObject();
                temp.setRootName(rootName);
                temp.setType(Layer2InterfaceObject.ETHERNET_TYPE);
                temp.setInstance(atmInstance);
                for (int j = 1; j < atmLine.size(); j++) {
                    ArrayList<String> element4 = myUtil.get4ElementFromLine(atmLine.get(j));
                    SimpleObject tmp_ob = new SimpleObject();
                    tmp_ob.setId(Integer.parseInt(element4.get(0)));
                    tmp_ob.setName(element4.get(1));
                    tmp_ob.setParameter(rootName + "." + String.valueOf(atmInstance) + "." + element4.get(2));
                    tmp_ob.setType(element4.get(3));
                    temp.getListObject().add(tmp_ob);
                }
                returnValue.add(temp);
            }
        }
        //set data
        myTreeUtil.getArrayValues_2(tempRoot, returnValue);

        return returnValue;
    }

    public static ArrayList<WanServiceObject> toEthernetWANService(Root tempRoot) throws Exception {
        ArrayList<WanServiceObject> returnValue = new ArrayList<WanServiceObject>();

        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();

        ArrayList<Layer2InterfaceObject> layer2List = toLayer2LanInterface(tempRoot);

        //get PPPoE
        ArrayList<SimpleObject> listObj_PPP = myUtil.getListSimpleObjectFromFile(FilePath.GET_PPPoE);
        for (Layer2InterfaceObject layer2 : layer2List) {
            String str_ppp = layer2.getRootName() + "." + layer2.getInstance() + ".WANPPPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ppp_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ppp);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ppp_Instances != null) {
                //list Object from file
                for (Integer ppp_Instance : ppp_Instances) {
                    String path = str_ppp + "." + ppp_Instance;
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setLayer2Interface(layer2);
                    temp3.setPath(path);
                    String linkType = layer2.getListObject().get(1).getValue();
                    if (TR069StaticParameter.LinkType_EOA.equals(linkType)) {
                        temp3.setType(WanServiceObject.PPPoE_TYPE);
                    } else {
                        temp3.setType(WanServiceObject.PPPoA_TYPE);
                    }
                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (SimpleObject listObj_PPP1 : listObj_PPP) {
                        listObj.add(listObj_PPP1.cloneObject());
                    }
                    //end clone
                    temp3.setInstance(ppp_Instance);
                    for (SimpleObject listObj1 : listObj) {
                        listObj1.setParameter(str_ppp + "." + ppp_Instance + "." + listObj1.getParameter());
//                        String paramName = listObj.get(k).getParameter();
//                        if ("ConnectType".equalsIgnoreCase(paramName)) {
//                            String connectionType = listObj.get(k).getValue();
//                            temp3.setType(WanServiceObject.PPPoE_TYPE);
//                        }
                    }
                    temp3.setType(WanServiceObject.PPPoE_TYPE);
                    temp3.setListObject(myTreeUtil.getArrayValues(tempRoot, listObj));
                    returnValue.add(temp3);
                }
            }
        }
        //get IPoE
        ArrayList<SimpleObject> listObj_IP = myUtil.getListSimpleObjectFromFile(FilePath.GET_IPoE);
        for (Layer2InterfaceObject layer2 : layer2List) {
            String str_ip = layer2.getRootName() + "." + layer2.getInstance() + ".WANIPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ip_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ip);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ip_Instances != null) {
                //list Object from file
                for (Integer ip_Instance : ip_Instances) {
                    String path = str_ip + "." + ip_Instance + ".";
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setPath(path);
                    temp3.setLayer2Interface(layer2);
//                    String linkType = layer2.getListObject().get(1).getValue();
//                    if (TR069StaticParameter.LinkType_EOA.equals(linkType)) {
//                        // Check bridge type ?
//                        if (true) {
//                            temp3.setType(WanServiceObject.IPoE_TYPE);
//                        }
//                    } else {
//                        temp3.setType(WanServiceObject.IPoA_TYPE);
//                    }
//                    if (layer2.get(i).getListObject().get(1).getValue().equals(TR069StaticParameter.LinkType_EOA)) {
//                        temp3.setType(WanServiceObject.IPoE_TYPE);
//                    } else {
//                        temp3.setType(WanServiceObject.IPoA_TYPE);
//                    }
                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (SimpleObject listObj_IP1 : listObj_IP) {
                        listObj.add(listObj_IP1.cloneObject());
                    }
                    //end clone
                    String connectionTypePath = ""; //IP_Bridged, IP_Routed
                    temp3.setInstance(ip_Instance);
                    for (SimpleObject listObj1 : listObj) {
                        listObj1.setParameter(str_ip + "." + ip_Instance + "." + listObj1.getParameter());
                        String paramName = listObj1.getName();
                        if ("ConnectType".equals(paramName)) {
                            connectionTypePath = listObj1.getParameter();
                        }
                    }
                    ArrayList<SimpleObject> wanListObj = myTreeUtil.getArrayValues(tempRoot, listObj);
                    String connectionType = myTreeUtil.getValueFromPath(tempRoot, connectionTypePath);
                    // Check Type
                    if ("IP_Routed".equalsIgnoreCase(connectionType)) {
                        temp3.setType(WanServiceObject.IPoE_TYPE);
                    } else if ("IP_Bridged".equalsIgnoreCase(connectionType)) {
                        temp3.setType(WanServiceObject.Bridge_TYPE);
                    } else {
                        temp3.setType(WanServiceObject.IPoA_TYPE);
                    }
                    temp3.setListObject(wanListObj);
                    returnValue.add(temp3);
                }
            }
        }

        return returnValue;
    }

}
