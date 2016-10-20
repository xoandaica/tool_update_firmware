package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

import java.util.ArrayList;
import java.util.Map;

import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.Layer2InterfaceObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.PerformanceJAXBWrapper;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.PerformanceObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.WanServiceObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.Root;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.DataFileUtils;
import vn.vnpttech.ssdc.nms.mediation.stbacs.tree.TreeUtils;

public class PeriodicCommand extends Command {

    //public static int count = 0;
    //get infor device
    private Root rootNameDevice = new Root("InternetGatewayDevice");
    private String upgradeFirmwareID;

    public Integer orderCmd = 0;

    public PeriodicCommand(String serialNumber, String connectionRequestURL, String username, String password) {
        this.serialNumberCPE = serialNumber;
        this.connectionRequestURL = connectionRequestURL;
        this.usernameCPE = username;
        this.passwordCPE = password;

        this.type = Command.TYPE_PERIODIC;
    }

    public void addDataToRoot(Map<String, String> data) {
        TreeUtils myTreeUtil = new TreeUtils();
        for (String element : data.keySet()) {
            rootNameDevice = myTreeUtil.createTree(rootNameDevice, element, data.get(element));
        }
    }

    public void insPeforDataToDB() throws Exception {
        //count++;
        //System.out.println("count count performance xxxxxxxxxx: "+count);
//        DataFileUtils myStringUtil = new DataFileUtils();
//        PerformanceObject data = getPerformanceDevice(rootNameDevice);
//        System.out.println("performance 1111111111111111111111111");
//        AdslPerformance pf = new AdslPerformance();
//        pf.setAdslId(this.serialNumberCPE);
//        pf.setPerformanceInfo(myStringUtil.convertPerObjToXML(data));
//        pf.setTime(new Date());

        throw new UnsupportedOperationException("Implement to insert database !");
        //ProcessQueuePutPerformance.getInstance().enQueue(pf);
        //boolean ok = dao.save(pf);

    }

    public Root getRootNameDevice() {
        return rootNameDevice;
    }

    public void setRootNameDevice(Root rootNameDevice) {
        this.rootNameDevice = rootNameDevice;
    }

    public ArrayList<Layer2InterfaceObject> getATMInterface(Root tempRoot) throws Exception {
        ArrayList<Layer2InterfaceObject> returnValue = new ArrayList<Layer2InterfaceObject>();
        //get data from file
        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();
        //ATM
        ArrayList<String> atmLine = DataFileUtils.getLineFromFile(FilePath.GET_ATM_INTERFACE);
        ArrayList<Integer> atmInstances = myTreeUtil.getListInstanceFromPath(tempRoot, atmLine.get(0));
        if (atmInstances != null) {
            for (Integer atmInstance : atmInstances) {
                Layer2InterfaceObject temp = new Layer2InterfaceObject();
                temp.setRootName(atmLine.get(0));
                temp.setType(Layer2InterfaceObject.ATM_TYPE);
                temp.setInstance(atmInstance);
                for (int j = 1; j < atmLine.size(); j++) {
                    ArrayList<String> element4 = DataFileUtils.get4ElementFromLine(atmLine.get(j));
                    SimpleObject tmp_ob = new SimpleObject();
                    tmp_ob.setId(Integer.parseInt(element4.get(0)));
                    tmp_ob.setName(element4.get(1));
                    tmp_ob.setParameter(atmLine.get(0) + "." + String.valueOf(atmInstance) + "." + element4.get(2));
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

    public ArrayList<WanServiceObject> getATMWANService(Root tempRoot) throws Exception {
        ArrayList<WanServiceObject> returnValue = new ArrayList<WanServiceObject>();

        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();

        ArrayList<Layer2InterfaceObject> layer2 = getATMInterface(tempRoot);

        //get PPPoE
        ArrayList<SimpleObject> listObj_PPP = DataFileUtils.getListSimpleObjectFromFile(FilePath.GET_PPPoE);
        for (Layer2InterfaceObject layer21 : layer2) {
            String str_ppp = layer21.getRootName() + "." + layer21.getInstance() + ".WANPPPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ppp_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ppp);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ppp_Instances != null) {
                //list Object from file
                for (Integer ppp_Instance : ppp_Instances) {
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setLayer2Interface(layer21);
                    temp3.setType(WanServiceObject.PPPoE_TYPE);
                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (SimpleObject listObj_PPP1 : listObj_PPP) {
                        listObj.add(listObj_PPP1.cloneObject());
                        //SimpleObject yyy = xxx.;
                    }
                    //end clone
                    temp3.setInstance(ppp_Instance);
                    for (SimpleObject listObj1 : listObj) {
//						System.out.println("uuuu : "+str_ppp_atm+"."+ppp_atmInstances.get(j)+"."+listObj.get(k).getParameter());
                        listObj1.setParameter(str_ppp + "." + ppp_Instance + "." + listObj1.getParameter());
                    }
                    temp3.setListObject(myTreeUtil.getArrayValues(tempRoot, listObj));
                    returnValue.add(temp3);
                }
            }
        }
        //get IPoE
        ArrayList<SimpleObject> listObj_IP = myUtil.getListSimpleObjectFromFile(FilePath.GET_IPoE);
        for (Layer2InterfaceObject layer21 : layer2) {
            String str_ip = layer21.getRootName() + "." + layer21.getInstance() + ".WANIPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ip_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ip);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ip_Instances != null) {
                //list Object from file
                for (Integer ip_Instance : ip_Instances) {
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setLayer2Interface(layer21);
                    temp3.setType(WanServiceObject.IPoE_TYPE);
                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (SimpleObject listObj_IP1 : listObj_IP) {
                        listObj.add(listObj_IP1.cloneObject());
                        //SimpleObject yyy = xxx.;
                    }
                    //end clone
                    temp3.setInstance(ip_Instance);
                    for (SimpleObject listObj1 : listObj) {
//						System.out.println("uuuu : "+str_ip_atm+"."+ip_atmInstances.get(j)+"."+listObj.get(k).getParameter());
                        listObj1.setParameter(str_ip + "." + ip_Instance + "." + listObj1.getParameter());
                    }
                    temp3.setListObject(myTreeUtil.getArrayValues(tempRoot, listObj));
                    returnValue.add(temp3);
                }
            }
        }

        return returnValue;
    }

    public PerformanceObject getPerformanceDevice(Root tempRoot) throws Exception {
        PerformanceObject returnValue = new PerformanceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        TreeUtils treeUtil = new TreeUtils();
        ///////////////////////////get Lan Performance
        ArrayList<PerformanceJAXBWrapper> lanPerformance = new ArrayList<PerformanceJAXBWrapper>();

        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.GET_LANPERFORMANCE);
        ArrayList<SimpleObject> lanPerformanceItem = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        ArrayList<Integer> instances = treeUtil.getListInstanceFromPath(tempRoot, "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig");
        if (instances != null) {
            for (Integer in : instances) {
                //clone lan performance item
                ArrayList<SimpleObject> cloneLanPerform = new ArrayList<SimpleObject>();
                for (SimpleObject simpleObj : lanPerformanceItem) {
                    cloneLanPerform.add(simpleObj.cloneObject());
                }
                //end clone & now edit parameter
                for (SimpleObject obj : cloneLanPerform) {
                    obj.setParameter(lines.get(0) + "." + in + "." + obj.getParameter());
                }
                //add value to list parameter
                cloneLanPerform = treeUtil.getArrayValues(tempRoot, cloneLanPerform);
                //add to returnvalue
                PerformanceJAXBWrapper lanWrapper = new PerformanceJAXBWrapper();
                lanWrapper.setPerformanceWrapper(cloneLanPerform);

                lanPerformance.add(lanWrapper);
            }
        }
        returnValue.setLanPerformance(lanPerformance);
		//end get lan performance

        ///////////////////////////get WLAN Performance
        ArrayList<SimpleObject> wlanPerformance = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_WLANPERFORMANCE);
        //add value to list parameter
        wlanPerformance = treeUtil.getArrayValues(tempRoot, wlanPerformance);
        //add to returnvalue
        returnValue.setWlanPerformance(wlanPerformance);
        //end get wlan performmanc	
        ////////////////////////get WAN Performance
        ArrayList<PerformanceJAXBWrapper> wanPerformance = new ArrayList<PerformanceJAXBWrapper>();
        ArrayList<WanServiceObject> wanServiceObj = getATMWANService(tempRoot);
        ArrayList<SimpleObject> fileObj = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_WANPERFORMANCE);
        for (WanServiceObject item : wanServiceObj) {
            //clone obj
            ArrayList<SimpleObject> cloneObj = new ArrayList<SimpleObject>();
            for (SimpleObject fileObjItem : fileObj) {
                cloneObj.add(fileObjItem.cloneObject());
            }
            //end clone & now edit parameter
            Layer2InterfaceObject layer2Obj = item.getLayer2Interface();
            String path = layer2Obj.getRootName() + "." + layer2Obj.getInstance() + ".";
            if (item.getType().equals(WanServiceObject.IPoE_TYPE)) {
                path = path + "WANIPConnection.";
            } else {
                path = path + "WANPPPConnection.";
            }
            path = path + item.getInstance() + ".";
            //edit parameter name
            for (SimpleObject obj : cloneObj) {
                obj.setParameter(path + obj.getParameter());
            }
            //add value to list parameter
            cloneObj = treeUtil.getArrayValues(tempRoot, cloneObj);
            PerformanceJAXBWrapper wanWrapper = new PerformanceJAXBWrapper();
            wanWrapper.setPerformanceWrapper(cloneObj);
            //add to returnvalue
            wanPerformance.add(wanWrapper);
        }
        returnValue.setWanPerformance(wanPerformance);
        ArrayList<SimpleObject> opticalInfo = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_OPTICAL_INFO);
        opticalInfo = treeUtil.getArrayValues(tempRoot, opticalInfo);
        returnValue.setOpticalInfo(opticalInfo);
        //end get wan performance
//        //<editor-fold defaultstate="collapsed" desc="DSL Performance not belong to GPON Device!">
///////////////////////get DSL Performance	
//        ArrayList<SimpleObject> dslPerformance = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_DSLPERFORMANCE);
//        //add value to list parameter
//        dslPerformance = treeUtil.getArrayValues(tempRoot, dslPerformance);
//        //add traffic type
//        SimpleObject trafficType = new SimpleObject("", "TrafficType", "ATM", "xsd:string");
//        trafficType.setId(16);
//        dslPerformance.add(trafficType);
//        //add to returnvalue
//        returnValue.setDslPerformance(dslPerformance);
//</editor-fold>
        //end get dsl	
        return returnValue;
    }

    @Override
    public boolean executeCommand() throws Exception {
        CommandRequestFactory.addCommand(this);
        return errorCheck;
    }

    private void setLogStatus(int status) {
//        AdslLog logobj = new AdslLog();
//        logobj.setUsername(user_Username);
//        logobj.setSerialNumber(serialNumberCPE);
//        logobj.setTime(new Date());
//        logobj.setAction(type);
//        logobj.setStatus(status);
        //DataDbManager.getInstance().enqueue(logobj);
        throw new UnsupportedOperationException("Implement to insert database !");
    }

    @Override
    public void receiveError(String errorString) {
        //hasResult =true;
        errorCheck = true;
        this.errorString = errorString;
        System.out.println("REceive error");
        //insert to lof 
        //AdslLogDao adslLogDao = new AdslLogDaoImpl();
        setLogStatus(0);
        //ProcessQueuePutPerformance.getInstance().enQueue(logobj);
        //adslLogDao.save(logobj);

        CommandRequestFactory.removeCommand(this);
//		notify();
    }

    @Override
    public void receiveResult() {
        System.out.println("ID 22222 Thread: " + Thread.currentThread().getId() + "_NAME: " + Thread.currentThread().getName());
        System.out.println("REceive result true");
        //hasResult = true;
        errorCheck = false;
        //insert to lof 
        //AdslLogDao adslLogDao = new AdslLogDaoImpl();
        setLogStatus(1);
        //ProcessQueuePutPerformance.getInstance().enQueue(logobj);		
        //adslLogDao.save(logobj);		

        CommandRequestFactory.removeCommand(this);
//		notify();
    }

    public String getUpgradeFirmwareID() {
        return upgradeFirmwareID;
    }

    public void setUpgradeFirmwareID(String upgradeFirmwareID) {
        this.upgradeFirmwareID = upgradeFirmwareID;
    }

}
