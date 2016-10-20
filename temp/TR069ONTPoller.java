/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package net.vnpttech.collection.openacs.poller;

import net.vnpttech.collection.openacs.mycommand.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import net.vnpttech.collection.openacs.common.AppConfig;
import net.vnpttech.collection.openacs.exception.Tr069Exception;
import net.vnpttech.collection.openacs.myobject.DataFileModel;
import net.vnpttech.collection.openacs.ws.model.DeviceInfo;
import net.vnpttech.collection.openacs.ws.model.WirelessSecurity;
import org.apache.commons.lang.StringUtils;
import net.vnpttech.collection.openacs.myobject.LanSetupObject;
import net.vnpttech.collection.openacs.myobject.LanSetup_IPLease;
import net.vnpttech.collection.openacs.myobject.Layer2InterfaceObject;
import net.vnpttech.collection.openacs.myobject.Layer3GUIObject;
import net.vnpttech.collection.openacs.myobject.PerformanceJAXBWrapper;
import net.vnpttech.collection.openacs.myobject.PerformanceObject;
import net.vnpttech.collection.openacs.myobject.SimpleObject;
import net.vnpttech.collection.openacs.myobject.StaticDynamicDNSObject;
import net.vnpttech.collection.openacs.myobject.StaticRouteObject;
import net.vnpttech.collection.openacs.myobject.WANGUIObject;
import net.vnpttech.collection.openacs.myobject.WANGUIObjectEthernet;
import net.vnpttech.collection.openacs.myobject.WanServiceObject;
import net.vnpttech.collection.openacs.myobject.WirelessObject;
import net.vnpttech.collection.openacs.tree.Root;
import net.vnpttech.collection.openacs.tree.DataFileUtils;
import net.vnpttech.collection.openacs.tree.TreeUtils;
import net.vnpttech.collection.openacs.ws.model.BrigdingGroup;
import net.vnpttech.collection.openacs.ws.model.DeviceInterface;
import net.vnpttech.collection.openacs.ws.model.IPoEWanServiceDHCP;
import net.vnpttech.collection.openacs.ws.model.IPoEWanServiceStatic;
import net.vnpttech.collection.openacs.ws.model.ModelUtils;
import net.vnpttech.collection.openacs.ws.model.ModemDataTree;
import net.vnpttech.collection.openacs.ws.model.ModemInterfaceGroups;
import net.vnpttech.collection.openacs.ws.model.ParameterInforStruct;
import net.vnpttech.collection.openacs.ws.model.ParameterModel;

/**
 *
 * @author Vunb
 * @date Oct 9, 2014
 * @update Oct 9, 2014
 */
public class TR069ONTPoller extends TR069Poller {

    public TR069ONTPoller(String user_Username, String serialNumber, String connectionRequestURL) {
        super(user_Username, serialNumber, connectionRequestURL);
    }

    public TR069ONTPoller(String serialNumber, String connectionRequestURL) {
        super("System", serialNumber, connectionRequestURL);
    }

    public Layer2InterfaceObject addLayer2LanObjByVersion(String modelName) throws Exception {
        DataFileUtils myUtil = new DataFileUtils();
        //ArrayList<String> atmLine = myUtil.getLineFromFile(FilePath.GET_ETHERNET_INTERFACE);
        DataFileModel datamodel = myUtil.getListSimpleObjectFromFile(FilePath.GET_ETHERNET_INTERFACE, modelName);
        SimpleObject root = datamodel.getRoot();

        String defInstance = System.getProperty("Layer2_LAN_DefaultInstance", "1");
        int atmInstance = Integer.parseInt(defInstance);
        ArrayList<SimpleObject> listObject = datamodel.getDataModel(atmInstance);

        Layer2InterfaceObject layer2 = new Layer2InterfaceObject();
        layer2.setDeviceModelName(modelName);
        layer2.setRootName(root.getParameter());
        layer2.setType(Layer2InterfaceObject.ETHERNET_TYPE);
        layer2.setInstance(atmInstance);
        layer2.setListObject(listObject);
        //return temp;

        return layer2;
    }

    public Layer2InterfaceObject addLayer2LanObj() throws Exception {
        return addLayer2LanObj(0);
    }

    public Layer2InterfaceObject addLayer2LanObj(int dslLatency) throws Exception {
        Layer2InterfaceObject returnValue = new Layer2InterfaceObject();
        returnValue.setIfName("eth0");  // V1: eth0

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJECT_LANLayer2);
        //build Command
        Integer instance = addObjectValue(lines.get(0), null, AddObjectCommand.ADDOBJ_LAYER2);
        if (instance != null) {
            sb.setLength(0);
            sb.append("addLayer2LanObj success: New instance=")
                    .append(instance);

            logger.debug(sb);
            returnValue.setRootName(lines.get(0));
            returnValue.setInstance(instance);
            returnValue.setType(Layer2InterfaceObject.ETHERNET_TYPE);   // ONT working only on Ethernet Interface !!! :D
            return returnValue;
        } else {
            sb.setLength(0);
            sb.append("addLayer2LanObj_V2 error: SerialNumber=")
                    .append(serialNumber)
                    .append(", Instance=")
                    .append(instance)
                    .append(", Path=")
                    .append(lines.get(0));
            logger.error(sb);
            throw new Exception("addLayer2LanObj fail: instance NULL");
        }
    }

    public Layer2InterfaceObject addLayer2LanObj_VER2() throws Exception {
        Layer2InterfaceObject returnValue = new Layer2InterfaceObject();
        returnValue.setIfName("veip0");  // VER2: veip0

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJECT_LANLayer2_V2);
        ArrayList<SimpleObject> datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //set value
        datas.get(0).setValue(returnValue.getIfName());
        datas.get(1).setValue("VlanMuxMode");
        datas.get(2).setValue("TRUE");

        //build Command
        Integer instance = addObjectValue(lines.get(0), datas, AddObjectCommand.ADDOBJ_LAYER2);
        if (instance != null) {
            sb.setLength(0);
            sb.append("addLayer2LanObj_V2 success: SerialNumber=")
                    .append(serialNumber)
                    .append(", Instance=")
                    .append(instance)
                    .append(", Path=")
                    .append(lines.get(0));
            logger.debug(sb);
            returnValue.setRootName(lines.get(0));
            returnValue.setInstance(instance);
            returnValue.setType(Layer2InterfaceObject.ETHERNET_TYPE);   // ONT working only on Ethernet Interface !!! :D
            return returnValue;
        } else {
            sb.setLength(0);
            sb.append("addLayer2LanObj_V2 error: SerialNumber=")
                    .append(serialNumber)
                    .append(", Instance=")
                    .append(instance)
                    .append(", Path=")
                    .append(lines.get(0));
            logger.error(sb);
            throw new Exception("addLayer2LanObj_V2 error: instance NULL");
        }
    }

    public Layer2InterfaceObject getDefaulLayer2LanObj_VER3() throws IOException {
        DataFileUtils myUtil = new DataFileUtils();
        ArrayList<String> atmLine = myUtil.getLineFromFile(FilePath.GET_ETHERNET_INTERFACE);
        String rootName = atmLine.get(0);

        String defInstance = System.getProperty("Layer2_LAN_DefaultInstance", "1");
        int atmInstance = Integer.parseInt(defInstance);

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
        return temp;
    }

    public WanServiceObject addPPPoEWanService(
            Layer2InterfaceObject layer2,
            String serviceName,
            String username, String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String connectionId, int wanIndex
    ) throws Exception {
        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myUtil = new DataFileUtils();
        String modelName = layer2.getDeviceModelName();
        DataFileModel datamodel = myUtil.getListSimpleObjectFromFile(FilePath.ADDOBJEC_PPPWANSERVICE, modelName);

        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANPPPConnection";
        String ipV4Enabe = ipV4Enabled ? "1" : "0";
        String ipV6Enabe = ipV6Enabled ? "1" : "0";
        String natEnable = natEnabled ? "1" : "0";
        String firewallEnable = firewallEnabled ? "1" : "0";

        datamodel.setValue("ServiceDescription", serviceName);
        datamodel.setValue("VlanMux8021p", vlanMux802_1Priority);
        datamodel.setValue("VlanMuxID", vlanMuxID);
        datamodel.setValue("Username", username);
        datamodel.setValue("Password", password);

        datamodel.setValue("IPv4Enabled", ipV4Enabe);
        datamodel.setValue("IPv6Enabled", ipV6Enabe);
        datamodel.setValue("NATEnabled", natEnable);
        datamodel.setValue("FirewallEnabled", firewallEnable);
        datamodel.setValue("ConnectionId", connectionId);
        datamodel.setValue("WanIndex", wanIndex);
        datamodel.setValue("IfName", serviceName);

        //build command
        ArrayList<SimpleObject> datas = datamodel.getDataModel(null, null);
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.PPPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }
        return returnValue;
    }

    public WanServiceObject addPPPoEWanService(
            Layer2InterfaceObject layer2,
            String serviceName,
            String username, String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String connectionId
    ) throws Exception {
        return addPPPoEWanService(
                layer2,
                serviceName,
                username, password,
                vlanMux802_1Priority,
                vlanMuxID,
                ipV4Enabled,
                ipV6Enabled,
                natEnabled,
                firewallEnabled,
                connectionId, 0); // default wanindex = 0
    }

    public WanServiceObject addIPoEWanService(
            Layer2InterfaceObject layer2,
            IPoEWanServiceDHCP ipoeWanDHCP
    ) throws Exception {
        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myUtil = new DataFileUtils();
//        ArrayList<String> lines = myUtil.getLineFromFile(FilePath.ADDOBJECT_IPWANService);
//        ArrayList<SimpleObject> datas = myUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        String modelName = layer2.getDeviceModelName();
        DataFileModel datamodel = myUtil.getListSimpleObjectFromFile(FilePath.ADDOBJECT_IPWANService, modelName);

        String ipV4Enabe = ipoeWanDHCP.isIpV4Enabled() ? "1" : "0";
        String ipV6Enabe = ipoeWanDHCP.isIpV6Enabled() ? "1" : "0";
        String natEnable = ipoeWanDHCP.isNatEnabled() ? "1" : "0";
        String firewallEnable = ipoeWanDHCP.isFirewallEnabled() ? "1" : "0";

        // use default in datafile
        datamodel.setValue("ServiceDescription", ipoeWanDHCP.getServiceName());
        datamodel.setValue("VlanMux8021p", ipoeWanDHCP.getVlanMux8021Priority());
        datamodel.setValue("VlanMuxID", ipoeWanDHCP.getVlanMuxID());
        datamodel.setValue("IPv4Enabled", ipV4Enabe);
        datamodel.setValue("IPv6Enabled", ipV6Enabe);
        datamodel.setValue("NATEnabled", natEnable);
        datamodel.setValue("FirewallEnabled", firewallEnable);
        datamodel.setValue("ConnectionId", ipoeWanDHCP.getConnectionId());
        datamodel.setValue("WanIndex", ipoeWanDHCP.getWanIndex());
//        datamodel.setValue("IfName", ipoeWanDHCP.getServiceName()); 
        ArrayList<SimpleObject> datas = datamodel.getDataModel(null, null);
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.IPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }

        return returnValue;
    }

    public WanServiceObject addIPoEWanService(
            Layer2InterfaceObject layer2,
            IPoEWanServiceStatic ipoeWanStatic
    ) throws Exception {
        WanServiceObject returnValue = new WanServiceObject();
        DataFileUtils myUtil = new DataFileUtils();

        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        String modelName = layer2.getDeviceModelName();
        DataFileModel datamodel = myUtil.getListSimpleObjectFromFile(FilePath.ADDOBJECT_IPWANServiceStatic, modelName);

        String ipV4Enabe = ipoeWanStatic.isIpV4Enabled() ? "1" : "0";
        String ipV6Enabe = ipoeWanStatic.isIpV6Enabled() ? "1" : "0";
        String natEnable = ipoeWanStatic.isNatEnabled() ? "1" : "0";
        String firewallEnable = ipoeWanStatic.isFirewallEnabled() ? "1" : "0";

        // use default in datafile
        //datamodel.setValue("Enable", "1");
        //datamodel.setValue("ConnectionType", "IP_Routed");
        datamodel.setValue("ServiceDescription", ipoeWanStatic.getServiceName());
        datamodel.setValue("VlanMux8021p", ipoeWanStatic.getVlanMux8021Priority());
        datamodel.setValue("VlanMuxID", ipoeWanStatic.getVlanMuxID());
        datamodel.setValue("IPv4Enabled", ipV4Enabe);
        datamodel.setValue("IPv6Enabled", ipV6Enabe);
        datamodel.setValue("NATEnabled", natEnable);
        datamodel.setValue("FirewallEnabled", firewallEnable);
        //datamodel.setValue("AddressingType", ipoeWanStatic.getAddressingType());
        //datamodel.setValue("MaxMTUSize", "1500");
        datamodel.setValue("ConnectionId", ipoeWanStatic.getConnectionId());
        datamodel.setValue("WanIndex", ipoeWanStatic.getWanIndex());
        datamodel.setValue("IPAddress", ipoeWanStatic.getExternalIPAddress());
        datamodel.setValue("SubnetMask", ipoeWanStatic.getSubnetMask());
        datamodel.setValue("DefaultGateway", ipoeWanStatic.getDefaultGateway());
        datamodel.setValue("ConnectionId", ipoeWanStatic.getConnectionId());
        datamodel.setValue("WanIndex", ipoeWanStatic.getWanIndex());
        datamodel.setValue("IfName", ipoeWanStatic.getServiceName());

//        ArrayList<String> lines = myUtil.getLineFromFile(FilePath.ADDOBJECT_IPWANServiceStatic);
//        ArrayList<SimpleObject> datas = myUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
//        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
//        String ipV4Enabe = ipoeWanStatic.isIpV4Enabled() ? "1" : "0";
//        String ipV6Enabe = ipoeWanStatic.isIpV6Enabled() ? "1" : "0";
//        String natEnable = ipoeWanStatic.isNatEnabled() ? "1" : "0";
//        String firewallEnable = ipoeWanStatic.isFirewallEnabled() ? "1" : "0";
        //add value
//        datas.get(0).setValue("1");  // enable xsd:boolean
//        datas.get(1).setValue(layer2.getIfName() + "." + layer2.getInstance()); // if name
//        datas.get(2).setValue("IP_Routed");
//        datas.get(3).setValue(ipoeWanStatic.getServiceName());
//        datas.get(4).setValue("" + ipoeWanStatic.getVlanMux8021Priority()); // (0)
//        datas.get(5).setValue("" + ipoeWanStatic.getVlanMuxID()); //100
//        datas.get(6).setValue(ipV4Enabe);           // ipV4Enabled (TRUE:1)
//        datas.get(7).setValue(ipV6Enabe);           // ipV6Enabled (FALSE:0)
//        datas.get(8).setValue(natEnable);          // natEnabled (FALSE:0)
//        datas.get(9).setValue(firewallEnable);     // firewallEnabled (FALSE:0)
//        datas.get(10).setValue(ipoeWanStatic.getAddressingType());     // AddressingType (Static)
//        datas.get(11).setValue(ipoeWanStatic.getExternalIPAddress());     // ExternalIPAddress
//        datas.get(12).setValue(ipoeWanStatic.getSubnetMask());     // SubnetMask
//        datas.get(13).setValue(ipoeWanStatic.getDefaultGateway());     // DefaultGateway
//        if (ipoeWanStatic.getWanIndex() != null && !"0".equals(ipoeWanStatic.getWanIndex())) {
//            datas.get(14).setValue(String.valueOf(ipoeWanStatic.getWanIndex()));
//        } else {
//            datas.remove(14);
//        }
        //build command
        ArrayList<SimpleObject> datas = datamodel.getDataModel(null, null);
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
           if (instance != null) {
            // config DNS
            sb.setLength(0);
            if (!StringUtils.isBlank(ipoeWanStatic.getDnsIfName())) {
                sb.append(ipoeWanStatic.getDnsIfName());
            }
            // for dns2
            if (!StringUtils.isBlank(ipoeWanStatic.getDnsServers())) {
                if (sb.length() != 0) {
                    sb.append(",").append(ipoeWanStatic.getDnsServers());
                } else {
                    sb.append(ipoeWanStatic.getDnsServers());
                }
            }
            if (sb.length() != 0) {
                DataFileModel dnsModel = myUtil.getListSimpleObjectFromFile(FilePath.Set_DNSServers, modelName);
                //dnsModel.setValue(modelName, null);
                dnsModel.setValue("DNSServers", ipoeWanStatic.getDnsServers());
                ArrayList<SimpleObject> dns = dnsModel.getDataModel();
                for (SimpleObject tmp : dns) {
                    if (tmp.getParameter().contains("DNSIfName")) {
                        tmp.setValue("");
                    } else if (tmp.getParameter().contains("DNSServers")) {
                        tmp.setValue(sb.toString());
                    }
                }
                setValueObjects(dns);
            }

            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.IPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }

        return returnValue;
    }

    public WanServiceObject addBridgeWanService(Layer2InterfaceObject layer2,
            String serviceDescription, int vlanMux802_1Priority,
            int vlanMuxID, String connectionId, int wanIndex) throws Exception {
        String serviceName = serviceDescription;
        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myUtil = new DataFileUtils();
//        ArrayList<String> lines = myUtil.getLineFromFile(FilePath.ADDOBJEC_BridgeWANSERVICE);
//        ArrayList<SimpleObject> datas = myUtil.getListSimpleObjectFromList(lines, 1, lines.size());

        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        String modelName = layer2.getDeviceModelName();
        DataFileModel datamodel = myUtil.getListSimpleObjectFromFile(FilePath.ADDOBJEC_BridgeWANSERVICE, modelName);

//        // use default in datafile
//        datamodel.setValue("Enable", "TRUE");
//        datamodel.setValue("ConnectionType", "IP_Bridged");
//        datamodel.setValue("IGMP_SOURCEEnabled", "TRUE");
//        datamodel.setValue("MLD_SOURCEEnabled", "TRUE");
//        datamodel.setValue("VlanTpid", "0");
        datamodel.setValue("ServiceDescription", serviceName);
        datamodel.setValue("VlanMux8021p", vlanMux802_1Priority);
        datamodel.setValue("VlanMuxID", vlanMuxID);
        datamodel.setValue("ConnectionId", connectionId);
        datamodel.setValue("WanIndex", wanIndex);
        datamodel.setValue("IfName", serviceName);
        datamodel.remove(DataFileModel.ROOT);

        //rebuid parameter
//        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
//        //add value
//        datas.get(0).setValue("TRUE");
//        datas.get(1).setValue(layer2.getIfName() + "." + layer2.getInstance()); // 2104-12-04: change to eth0 from eth1
//        datas.get(2).setValue("IP_Bridged"); //Unconfigured, IP_Routed, IP_Bridged
//        datas.get(3).setValue(serviceName);
//        datas.get(4).setValue("TRUE");
//        datas.get(5).setValue("TRUE");
//        datas.get(6).setValue(String.valueOf(vlanMux802_1Priority));
//        datas.get(7).setValue(String.valueOf(vlanMuxID));
//        datas.get(8).setValue("TRUE");
//        datas.get(9).setValue("0");
        // xoa 6-SOURCEEnabled-X_BROADCOM_COM_IGMP_SOURCEEnabled-xsd:boolean
        // Do: khong the set duoc value: xsd:boolean(1)
        //datas.remove(8);    // V1, V2: ko có ?
        //datas.remove(5);    // V1, V3: ko có ?
        //build command
        ArrayList<SimpleObject> datas = datamodel.getDataModel(null, null);
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.Bridge_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }
        return returnValue;
    }

    public WanServiceObject addBridgeWanService(Layer2InterfaceObject layer2,
            String serviceDescription, int vlanMux802_1Priority,
            int vlanMuxID, String connectionId) throws Exception {
        return addBridgeWanService(layer2, serviceDescription, vlanMux802_1Priority, vlanMuxID, connectionId, 0); // default cho wanidex = 0
    }

    public WanServiceObject addPPPoEWanService_ethernet(
            String serviceName,
            String username,
            String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled
    ) throws Exception {

        if (true) {
            throw new Exception("This version does not support ONT v2");
        }
        //set init data
        // 2104-12-04: change to eth0 from eth1
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.Enable", "1", "xsd:boolean");
        SimpleObject obj2 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_IfName", "eth0", "xsd:string");
        SimpleObject obj3 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_ConnectionMode", "VlanMuxMode", "xsd:string");
        datasetup.add(obj1);
        datasetup.add(obj2);
        datasetup.add(obj3);
        setValueObjects(datasetup);

        //add layer 2
        Layer2InterfaceObject layer2 = addLayer2LanObj();

        //add wan interface
        //WanServiceObject returnVl = addPPPoEWanService(layer2, "", username, password, "pppoe_eth0.200");
        WanServiceObject returnVl = addPPPoEWanService(layer2, serviceName, username, password, vlanMux802_1Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled, null);
        return returnVl;
    }

    public WanServiceObject addIPoEWanServiceDHCP_ethernet(IPoEWanServiceDHCP dhcpWanService) throws Exception {
        //set init data
        // 2104-12-04: change to eth0 from eth1
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.Enable", "1", "xsd:boolean");
        SimpleObject obj2 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_IfName", "eth0", "xsd:string");
        SimpleObject obj3 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_ConnectionMode", "VlanMuxMode", "xsd:string");
        datasetup.add(obj1);
        datasetup.add(obj2);
        datasetup.add(obj3);
        setValueObjects(datasetup);

        //add layer 2
        Layer2InterfaceObject layer2 = addLayer2LanObj();

        if (org.apache.commons.lang.StringUtils.isBlank(dhcpWanService.getServiceName())) {
            String wanPort = "eth0";
            dhcpWanService.setServiceName("ipoe_" + wanPort + "." + dhcpWanService.getVlanMuxID()); //ipoe_eth0.100
        }

        //add wan interface
        WanServiceObject returnVl = addIPoEWanService(layer2, dhcpWanService);
        return returnVl;
    }

    public WanServiceObject addIPoEWanServiceStatic_ethernet(IPoEWanServiceStatic staticWanService) throws Exception {
        //set init data
        // 2104-12-04: change to eth0 from eth1
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.Enable", "1", "xsd:boolean");
        SimpleObject obj2 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_IfName", "eth0", "xsd:string");
        SimpleObject obj3 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_ConnectionMode", "VlanMuxMode", "xsd:string");
        datasetup.add(obj1);
        datasetup.add(obj2);
        datasetup.add(obj3);
        setValueObjects(datasetup);

        //add layer 2
        Layer2InterfaceObject layer2 = addLayer2LanObj();

        if (org.apache.commons.lang.StringUtils.isBlank(staticWanService.getServiceName())) {
            String wanPort = layer2.getIfName(); // "eth0";
            staticWanService.setServiceName("ipoe_" + wanPort + "." + staticWanService.getVlanMuxID()); //ipoe_eth0.100
        }

        //add wan interface
        WanServiceObject returnVl = addIPoEWanService(layer2, staticWanService);
        return returnVl;
    }

    public WanServiceObject addBridgeWanService_ethernet(String serviceDescription, int vlanMux802_1Priority,
            int vlanMuxID, String connectionId) throws Exception {
        //set init data
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.Enable", "1", "xsd:boolean");
        SimpleObject obj2 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_IfName", "eth0", "xsd:string");
        SimpleObject obj3 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_ConnectionMode", "VlanMuxMode", "xsd:string");
        datasetup.add(obj1);
        datasetup.add(obj2);
        datasetup.add(obj3);
        setValueObjects(datasetup);

        //add layer 2
        Layer2InterfaceObject layer2 = addLayer2LanObj();

        //add wan interface
        // 2104-12-04: change to eth0 from eth1
        // 2105-01-14: enter prameters from user
        WanServiceObject returnVl = addBridgeWanService(layer2, serviceDescription, vlanMux802_1Priority, vlanMuxID, connectionId);
        return returnVl;
    }

    public DeviceInfo getDeviceInfo() throws IOException, Exception {
        Map<String, String> datas = getTreeValue(DeviceInfo.DataTree.Root + ".");
        DeviceInfo devInfo = ModelUtils.parseDeviceInfo(datas);
        return devInfo;
    }

    /**
     * ham cu ko dung, chuan bi xoa
     *
     * @return
     * @throws Exception
     */
    public ModemInterfaceGroups getDeviceInterfaceGroups2() throws Exception {
        String dataTree = "InternetGatewayDevice."; // InternetGatewayDevice.Layer2Bridging.";
        Map<String, String> datas = getTreeValue(dataTree);
        ModemInterfaceGroups ifgroups = new ModemInterfaceGroups();

        Map<String, BrigdingGroup> brigdingGroups = ModelUtils.parseBrigdingGroup(datas);
        Map<String, DeviceInterface> devinterfaces = ModelUtils.parseDeviceInterface(datas);

        ArrayList<BrigdingGroup> bgroups = new ArrayList<BrigdingGroup>();
        SortedSet<String> set = new TreeSet<String>(brigdingGroups.keySet());
        for (String key : set) {
            bgroups.add(brigdingGroups.get(key));
        }
        ArrayList<DeviceInterface> devifs = new ArrayList<DeviceInterface>();
        set = new TreeSet<String>(devinterfaces.keySet());
        for (String key : set) {
            DeviceInterface devif = devinterfaces.get(key);
            try {
                String ifRef = devif.getIfReference() + ".";// + "." + ModemDataTree.InterfaceDeviceName;
                //datas = getTreeValue(ifRef);
                String ifName = org.apache.commons.lang.StringUtils.defaultIfEmpty(datas.get(ifRef + ModemDataTree.X_InterfaceDeviceName), datas.get(ifRef + ModemDataTree.WlIfcname));
                String name = datas.get(ifRef + ModemDataTree.InterfaceDeviceName);
                devif.setIfName(ifName);
                devif.setName(name);
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
                ex.printStackTrace(System.out);
            }
            devifs.add(devif);
        }

        ifgroups.setBrigdingGroup(bgroups);
        ifgroups.setDeviceInterfaces(devifs);
        return ifgroups;
    }

    /**
     * ham nay thuc hien get interface và interface group theo 2 buoc nhanh hon
     * so voi cach get cu lay ca thong tin InternetGatewayDevice.
     *
     * @return
     * @throws Exception
     */
    public ModemInterfaceGroups getDeviceInterfaceGroups() throws Exception {
        String dataTree = "InternetGatewayDevice.Layer2Bridging.";
        Map<String, String> datas = getTreeValue(dataTree);
        // lay ve thong tin interface group
        // thong tin ve interface key, group key
        Map<String, BrigdingGroup> brigdingGroups = ModelUtils.parseBrigdingGroup(datas);
        Map<String, DeviceInterface> devinterfaces = ModelUtils.parseDeviceInterface(datas);
        ArrayList<BrigdingGroup> bgroups = new ArrayList<BrigdingGroup>();
        ArrayList<DeviceInterface> digroup = new ArrayList<DeviceInterface>();
        ModemInterfaceGroups ifgroups = new ModemInterfaceGroups();
        //lay duoc xong brigdingGroups
        for (Map.Entry<String, BrigdingGroup> entry : brigdingGroups.entrySet()) {
            bgroups.add(entry.getValue());
        }

        // get lan dau
        //van con thieu 3 tham so la filterKey-ifGroupKey-ifName thực hiện lấy tiếp 
        List<String> listSendToGet = new ArrayList<String>();
        for (Map.Entry<String, DeviceInterface> entry : devinterfaces.entrySet()) {
            String Key = entry.getKey();
            DeviceInterface Value = entry.getValue();

            String strSend = Value.getIfReference();
            String strIfName = "";
            if (strSend.contains(WlVirtIntfCfg)) {
                strIfName = strSend + "." + WlIfcname;
            } else {
                strIfName = strSend + "." + X_BROADCOM_COM_IfName;
            }

            if (strSend.contains(WANConnectionDevice)) { // voi wan interface thi lay duoc name
                String name = strSend + "." + InterfaceDeviceName;
                listSendToGet.add(name);
            }
            listSendToGet.add(strIfName);
        }

        // get ve name va interface name
        GetValueCommand cm = new GetValueCommand(serialNumber, connectionRequestURL);
        for (String strSend : listSendToGet) {
            cm.addParameter(strSend);
        }
        boolean hasError = cm.executeCommand();
        if (hasError) {
            throw new Exception(cm.getErrorString());
        }

        Map<String, String> mapAdd = cm.getReturnValue();
        for (Map.Entry<String, DeviceInterface> entry : devinterfaces.entrySet()) {
            DeviceInterface diTemp = entry.getValue();
            String ifRef = diTemp.getIfReference() + ".";

            String ifName = org.apache.commons.lang.StringUtils.defaultIfEmpty(mapAdd.get(ifRef + X_BROADCOM_COM_IfName), mapAdd.get(ifRef + WlIfcname));
            String name = mapAdd.get(ifRef + InterfaceDeviceName);
            diTemp.setName(name);
            diTemp.setIfName(ifName);
            digroup.add(diTemp);
        }
        ifgroups.setBrigdingGroup(bgroups);
        ifgroups.setDeviceInterfaces(digroup);
        return ifgroups;
    }

    public ModemInterfaceGroups getDeviceInterfaceGroupsV2() throws Exception {
        String dataTree = "InternetGatewayDevice.Layer2Bridging";
        Map<String, String> datas = getTreeValue(ModemDataTree.Layer2Bridging.Root + ".");
        ModemInterfaceGroups ifgroups = new ModemInterfaceGroups();

        Map<String, BrigdingGroup> brigdingGroups = ModelUtils.parseBrigdingGroup(datas);
        Map<String, DeviceInterface> devinterfaces = ModelUtils.parseDeviceInterface(datas);

        ArrayList<BrigdingGroup> bgroups = new ArrayList<BrigdingGroup>();
        SortedSet<String> set = new TreeSet<String>(brigdingGroups.keySet());
        for (String key : set) {
            bgroups.add(brigdingGroups.get(key));
        }
        ArrayList<DeviceInterface> devifs = new ArrayList<DeviceInterface>();
        set = new TreeSet<String>(devinterfaces.keySet());
        for (String key : set) {
            DeviceInterface devif = devinterfaces.get(key);
            try {
                String ifRef = devif.getIfReference() + ".";// + "." + ModemDataTree.InterfaceDeviceName;
                datas = getTreeValue(ifRef);
                String ifName = org.apache.commons.lang.StringUtils.defaultIfEmpty(datas.get(ifRef + ModemDataTree.X_InterfaceDeviceName), datas.get(ifRef + ModemDataTree.WlIfcname));
                String name = datas.get(ifRef + ModemDataTree.InterfaceDeviceName);
                devif.setIfName(ifName);
                devif.setName(name);
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
                ex.printStackTrace();
            }
            devifs.add(devif);
        }

        ifgroups.setBrigdingGroup(bgroups);
        ifgroups.setDeviceInterfaces(devifs);
        return ifgroups;
    }

    public boolean setDataTree(Map<String, String> mapData) throws Exception {
        if (mapData != null && !mapData.isEmpty()) {
            ArrayList<SimpleObject> listData = new ArrayList<SimpleObject>();
            for (String key : mapData.keySet()) {
                String value = mapData.get(key);
                listData.add(new SimpleObject(key, value, "xsd:string"));
            }
            return setValueObjects(listData);
        } else {
            return true;
        }
    }

    public Layer2InterfaceObject addLayer2ATMObj(int vpi, int vci, int dslLatency) throws Exception {
        Layer2InterfaceObject returnValue = new Layer2InterfaceObject();

        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJEC_ATM);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //set value
        datas.get(0).setValue("PVC: " + vpi + "/" + vci);
        datas.get(1).setValue(TR069StaticParameter.LinkType_EOA);
        datas.get(2).setValue(String.valueOf(dslLatency));
        //datas.get(3).setValue("atm");
        datas.get(4).setValue("VlanMuxMode");
        datas.get(5).setValue(String.valueOf(8));
        datas.get(6).setValue(String.valueOf(1));
        datas.get(7).setValue(String.valueOf(1));
        datas.get(8).setValue(String.valueOf(8));
        //build Command
        Integer instance = addObjectValue(lines.get(0), datas, AddObjectCommand.ADDOBJ_LAYER2);
        if (instance != null) {
            returnValue.setRootName(lines.get(0));
            returnValue.setInstance(instance);
            returnValue.setType(Layer2InterfaceObject.ATM_TYPE);
            ArrayList<SimpleObject> dataList = new ArrayList<SimpleObject>();
            for (int i = 0; i < 4; i++) {
                dataList.add(datas.get(i));
            }
            returnValue.setListObject(dataList);
            return returnValue;
        }
        return null;
    }

    public Layer2InterfaceObject addLayer2PTMObj(int dslLatency) throws Exception {
        Layer2InterfaceObject returnValue = new Layer2InterfaceObject();

        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJECT_PTM);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //set Value
        datas.get(0).setValue(String.valueOf(dslLatency));
        datas.get(1).setValue("ptm");
        datas.get(2).setValue("VlanMuxMode");
        datas.get(3).setValue("1");
        datas.get(4).setValue("1");
        //build command
        Integer instance = addObjectValue(lines.get(0), datas, AddObjectCommand.ADDOBJ_LAYER2);
        if (instance != null) {
            returnValue.setRootName(lines.get(0));
            returnValue.setInstance(instance);
            returnValue.setType(Layer2InterfaceObject.PTM_TYPE);
            ArrayList<SimpleObject> dataList = new ArrayList<SimpleObject>();
            for (int i = 0; i < 2; i++) {
                dataList.add(datas.get(i));
            }
            returnValue.setListObject(dataList);
            return returnValue;
        }
        return null;
//		return false;
    }

    public WanServiceObject new_addPPPoEWanService(int dslLatency, int vpi, int vci, String username, String password) throws Exception {
        Layer2InterfaceObject layer2 = addLayer2ATMObj(vpi, vci, dslLatency);

        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJEC_PPPWANSERVICE);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANPPPConnection";
        //add value
        datas.get(0).setValue("pppoe_" + dslLatency + "_" + vpi + "_" + vci);
        datas.get(1).setValue("ppp" + layer2.getInstance() + ".");
        datas.get(2).setValue(username);
        datas.get(3).setValue(password);
        datas.get(4).setValue("pppoe");
        datas.get(5).setValue("1");
        datas.get(6).setValue("1");
        datas.get(7).setValue("1");
        datas.get(8).setValue("IP_Routed");
        //build command
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.PPPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }
        return returnValue;
    }

    public WanServiceObject addPPPoEWanService(Layer2InterfaceObject layer2, String description, String username, String password, String servicename) throws Exception {
        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJEC_PPPWANSERVICE);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANPPPConnection";
        //add value
        datas.get(0).setValue(description);
        datas.get(1).setValue("ppp" + layer2.getInstance() + ".");
        datas.get(2).setValue(username);
        datas.get(3).setValue(password);
        datas.get(4).setValue(servicename);
        datas.get(5).setValue("1");
        datas.get(6).setValue("1");
        datas.get(7).setValue("1");
        datas.get(8).setValue("IP_Routed");
        //build command
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.PPPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }
        return returnValue;
    }

    public WanServiceObject new_addPPPoEWanService_ethernet(int dslLatency, String username, String password) throws Exception {
        //setup bien ban dau
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.IfName", "eth0", "xsd:string");
        SimpleObject obj2 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.MoveToWANSide", "1", "xsd:boolean");
        SimpleObject obj3 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.MoveToLANSide", "0", "xsd:boolean");
        SimpleObject obj4 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.Enable", "1", "xsd:boolean");
        SimpleObject obj5 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_ConnectionMode", "VlanMuxMode", "xsd:string");
        datasetup.add(obj1);
        datasetup.add(obj2);
        datasetup.add(obj3);
        datasetup.add(obj4);
        datasetup.add(obj5);
        setValueObjects(datasetup);

//      //add layer 2
        Layer2InterfaceObject retu = addLayer2LanObj(dslLatency);

        ////add wan interface
        WanServiceObject returnVl = addPPPoEWanService(retu, "pppoe_eth0", username, password, "pppoelan");
        return returnVl;

    }

    public BrigdingGroup addInterfaceGroup(String bridgeEnable, String bridgeName, String wanInterfaceId, String... lanInterfaceIds) throws Exception {

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJEC_InterfaceGroup);

        ArrayList<SimpleObject> datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //set value
        datas.get(0).setValue(bridgeEnable);
        datas.get(1).setValue(bridgeName);
        //build Command, add object InternetGatewayDevice.Layer2Bridging.Bridge.
        // return number instance      
        // set param InternetGatewayDevice.Layer2Bridging.Bridge.6.BridgeEnable, InternetGatewayDevice.Layer2Bridging.Bridge.6.BridgeName
        Integer instance = addObjectValue(lines.get(0), datas, AddObjectCommand.ADDOBJ_BRIDGE);
        if (instance != null && instance > 0) {
            // add FilterBridgeReference
            String groupDataTree = ModemDataTree.Layer2Bridging.Bridge.Root + "." + instance + ".";
            // get new bridge group info InternetGatewayDevice.Layer2Bridging.Bridge.new.
            Map<String, String> groupDatas = getTreeValue(groupDataTree);
            Map<String, BrigdingGroup> groupMaps = ModelUtils.parseBrigdingGroup(groupDatas);
            String index = instance.toString();
            BrigdingGroup newGroup = groupMaps.get(index); // group moi duoc add them

            // update Available Interface ?
            ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
            SimpleObject obj1 = new SimpleObject(ModemDataTree.Layer2Bridging.Filter.Root
                    + "." + wanInterfaceId + "."
                    + ModemDataTree.Layer2Bridging.Filter.FilterBridgeReference,
                    newGroup.getBridgeKey(), ModemDataTree.DataType.INT);
            datasetup.add(obj1);
            for (String lanInterfaceId : lanInterfaceIds) {
                SimpleObject obj2 = new SimpleObject(ModemDataTree.Layer2Bridging.Filter.Root
                        + "." + lanInterfaceId + "."
                        + ModemDataTree.Layer2Bridging.Filter.FilterBridgeReference,
                        newGroup.getBridgeKey(), ModemDataTree.DataType.INT);
                datasetup.add(obj2);
            }
            // set values FilterBridgeReference
            boolean resultAddGroup = setValueObjects(datasetup);
            System.out.println("[*************** RESULT *****************]");
            System.out.println("RESULT add interfaces to group: " + resultAddGroup);
            return newGroup;
        }
        return null;
    }

    public boolean removeInterfaceGroup(String bridgeGroupKey, String wanInterfaceId, String... lanInterfaceIds) throws Exception {

        if ("0".equals(bridgeGroupKey) || StringUtils.isBlank(bridgeGroupKey)) {
            // do not delete default group
            return false;
        }
        // update Available Interface ?
        String defaultGroupKey = "0";
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject(ModemDataTree.Layer2Bridging.Filter.Root
                + "." + wanInterfaceId + "."
                + ModemDataTree.Layer2Bridging.Filter.FilterBridgeReference,
                defaultGroupKey, ModemDataTree.DataType.INT);
        datasetup.add(obj1);
        for (String lanInterfaceId : lanInterfaceIds) {
            SimpleObject obj2 = new SimpleObject(ModemDataTree.Layer2Bridging.Filter.Root
                    + "." + lanInterfaceId + "."
                    + ModemDataTree.Layer2Bridging.Filter.FilterBridgeReference,
                    defaultGroupKey, ModemDataTree.DataType.INT);
            datasetup.add(obj2);
        }
        // update Default
        boolean result = setValueObjects(datasetup);
        System.out.println("Update Default Group result: " + result);
        // Delete Object
        String dataTree = ModemDataTree.Layer2Bridging.Bridge.Root + "." + bridgeGroupKey + ".";
        result = deleteObject(dataTree);
        // update BridgeFilter
        return result;
    }

    public WanServiceObject addBridgeWanService(int dslLatency, int vpi, int vci) throws Exception {

        Layer2InterfaceObject layer2 = addLayer2ATMObj(vpi, vci, dslLatency);

        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJEC_BridgeWANSERVICE);
        ArrayList<SimpleObject> datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        //add value
        datas.get(0).setValue("br_" + dslLatency + "_" + vpi + "_" + vci);
        datas.get(1).setValue("atm" + layer2.getInstance() + ".");
        datas.get(2).setValue("1");
        datas.get(3).setValue("1");
        datas.get(4).setValue("0");
        datas.get(5).setValue("IP_Bridged");
        //build command
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.Bridge_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }
        return returnValue;
    }

    public WanServiceObject new_addIPoEWanServiceDHCP_ethernet(int dslLatency, boolean nat, boolean firewall) throws Exception {
        //setup bien ban dau
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.IfName", "eth0", "xsd:string");
        SimpleObject obj2 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.MoveToWANSide", "1", "xsd:boolean");
        SimpleObject obj3 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.MoveToLANSide", "0", "xsd:boolean");
        SimpleObject obj4 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.Enable", "1", "xsd:boolean");
        SimpleObject obj5 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_ConnectionMode", "VlanMuxMode", "xsd:string");
        datasetup.add(obj1);
        datasetup.add(obj2);
        datasetup.add(obj3);
        datasetup.add(obj4);
        datasetup.add(obj5);
        setValueObjects(datasetup);

//      //add layer 2
        Layer2InterfaceObject retu = addLayer2LanObj(dslLatency);

        ////add wan interface
        WanServiceObject returnVl = addIPoEWanServiceDHCP(retu, "ipoe_eth0", nat, firewall);
        return returnVl;

    }

    public WanServiceObject new_addIPoEWanServiceStatic_ethernet(int dslLatency, String ipaddress, String subnetmask, String gateway, String dns1, String dns2, boolean nat, boolean firewall) throws Exception {
        //setup bien ban dau
        ArrayList<SimpleObject> datasetup = new ArrayList<SimpleObject>();
        SimpleObject obj1 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.IfName", "eth0", "xsd:string");
        SimpleObject obj2 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.MoveToWANSide", "1", "xsd:boolean");
        SimpleObject obj3 = new SimpleObject("InternetGatewayDevice.X_BROADCOM_COM_InterfaceControl.MoveToLANSide", "0", "xsd:boolean");
        SimpleObject obj4 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.Enable", "1", "xsd:boolean");
        SimpleObject obj5 = new SimpleObject("InternetGatewayDevice.WANDevice.3.WANEthernetInterfaceConfig.X_BROADCOM_COM_ConnectionMode", "VlanMuxMode", "xsd:string");
        datasetup.add(obj1);
        datasetup.add(obj2);
        datasetup.add(obj3);
        datasetup.add(obj4);
        datasetup.add(obj5);
        setValueObjects(datasetup);

//      //add layer 2
        Layer2InterfaceObject retu = addLayer2LanObj(dslLatency);

        ////add wan interface
        WanServiceObject returnVl = addIPoEWanServiceStatic(retu, "ipoe_eth0", ipaddress, subnetmask, gateway, dns1, dns2, nat, firewall);
        return returnVl;

    }

    public WanServiceObject new_addIPoEWanServiceDHCP(int dslLatency, int vpi, int vci, boolean nat, boolean firewall) throws Exception {

        Layer2InterfaceObject layer2 = addLayer2ATMObj(vpi, vci, dslLatency);

        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJECT_IPWANServiceDHCP);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        //add value
        datas.get(0).setValue("ipoe_" + dslLatency + "_" + vpi + "_" + vci);

//		datas.get(1).setValue("atm0.");
        datas.get(2).setValue(TR069StaticParameter.AddressingType_DHCP);
        datas.get(3).setValue("1");
        datas.get(4).setValue(String.valueOf(nat));
        datas.get(5).setValue(String.valueOf(firewall));
        datas.get(6).setValue("IP_Routed");
        //build command
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.IPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }

        return returnValue;
    }

    public WanServiceObject addIPoEWanServiceDHCP(Layer2InterfaceObject layer2, String description, boolean nat, boolean firewall) throws Exception {
        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJECT_IPWANServiceDHCP);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        //add value
        datas.get(0).setValue(description);

//		datas.get(1).setValue("atm0.");
        datas.get(2).setValue(TR069StaticParameter.AddressingType_DHCP);
        datas.get(3).setValue("1");
        datas.get(4).setValue(String.valueOf(nat));
        datas.get(5).setValue(String.valueOf(firewall));
        datas.get(6).setValue("IP_Routed");
        //build command
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.IPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }

        return returnValue;
    }

    public WanServiceObject new_addIPoEWanServiceStatic(int dslLatency, int vpi, int vci,
            String ipaddress, String subnetmask, String gateway, String dns1, String dns2, boolean nat, boolean firewall) throws Exception {
        Layer2InterfaceObject layer2 = addLayer2ATMObj(vpi, vci, dslLatency);

        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJECT_IPWANServiceStatic);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        //add value
        datas.get(0).setValue("ipoe_" + dslLatency + "_" + vpi + "_" + vci);
//		datas.get(1).setValue("atm0.");
        datas.get(2).setValue(TR069StaticParameter.AddressingType_Static);
        datas.get(3).setValue(ipaddress);
        datas.get(4).setValue(subnetmask);
        datas.get(5).setValue(gateway);
        datas.get(6).setValue("1");
        datas.get(7).setValue(String.valueOf(nat));
        datas.get(8).setValue(String.valueOf(firewall));
        datas.get(9).setValue("IP_Routed");
        datas.get(10).setValue(dns1 + "," + dns2);
        //build command
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.IPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }

        return returnValue;
    }

    public WanServiceObject addIPoEWanServiceStatic(Layer2InterfaceObject layer2, String description,
            String ipaddress, String subnetmask, String gateway, String dns1, String dns2, boolean nat, boolean firewall) throws Exception {
        WanServiceObject returnValue = new WanServiceObject();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDOBJECT_IPWANServiceStatic);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //rebuid parameter
        String path = layer2.getRootName() + "." + layer2.getInstance() + "." + "WANIPConnection";
        //add value
        datas.get(0).setValue(description);
//		datas.get(1).setValue("atm0.");
        datas.get(2).setValue(TR069StaticParameter.AddressingType_Static);
        datas.get(3).setValue(ipaddress);
        datas.get(4).setValue(subnetmask);
        datas.get(5).setValue(gateway);
        datas.get(6).setValue("1");
        datas.get(7).setValue(String.valueOf(nat));
        datas.get(8).setValue(String.valueOf(firewall));
        datas.get(9).setValue("IP_Routed");
        datas.get(10).setValue(dns1 + "," + dns2);
        //build command
        Integer instance = addObjectValue(path, datas, AddObjectCommand.ADDOBJ_WANSERVICE);
        if (instance != null) {
            returnValue.setLayer2Interface(layer2);
            returnValue.setType(WanServiceObject.IPoE_TYPE);
            returnValue.setInstance(instance);
            returnValue.setListObject(datas);
        }

        return returnValue;
    }

    public boolean addLANStaticIPLease(String macAddress, String ipAddress) throws Exception {
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();

        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.ADDLAN_IPLEASE);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        //set value
        datas.get(0).setValue(macAddress);
        datas.get(1).setValue(ipAddress);
        datas.get(2).setValue(String.valueOf(1));
        //build command
        Integer instance = addObjectValue(lines.get(0), datas, AddObjectCommand.ADDOBJ_IPLEASE);
        if (instance != null) {
            return true;
        }

        return false;

    }

    public boolean addStaticRoute(String destIP, String subnetMask, String type, String ifname, String gateway) throws Exception {
        ArrayList<SimpleObject> datas = new ArrayList<SimpleObject>();
        DataFileUtils myStringUtil = new DataFileUtils();
        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.STATIC_ROUTE_IP);
        datas = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());

        datas.get(0).setValue(gateway);
        datas.get(1).setValue(subnetMask);
        datas.get(2).setValue(ifname);
        datas.get(3).setValue("-1");
        datas.get(4).setValue(destIP);
        datas.get(5).setValue("-1");
        datas.get(6).setValue("1");
        if (type.equals(WanServiceObject.PPPoE_TYPE) || type.equals(WanServiceObject.PPPoA_TYPE)) {
            datas.remove(0);
        }
        //build command
        Integer instance = addObjectValue(lines.get(0), datas, AddObjectCommand.ADDOBJ_IPLEASE);
        return instance != null;
    }

    public ArrayList<SimpleObject> getInfoManagement() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.ManagementServer.");
        //get data from file		
        DataFileUtils myUtil = new DataFileUtils();
        ArrayList<SimpleObject> returnValue = myUtil.getListSimpleObjectFromFile(FilePath.GET_MANAGEMENT_SERVER);
        //set data
        TreeUtils myTreeUtil = new TreeUtils();
        myTreeUtil.getArrayValues(tempRoot, returnValue);

        return returnValue;
    }

    public WANGUIObjectEthernet getWANGUIObject_ethernet() throws Exception {
        int __VERSION = 1;
        return getWANGUIObject_ethernet(__VERSION);
    }

    /**
     * This api is old, please use modelName instead of this
     *
     * @param __VERSION
     * @return WANGUIObjectEthernet
     * @throws java.lang.Exception
     * @deprecated use {@link #getWANGUIObject_ethernet(String modelName)}
     * instead.
     */
    @Deprecated
    public WANGUIObjectEthernet getWANGUIObject_ethernet(final int __VERSION) throws Exception {
        String pathTemplate = "InternetGatewayDevice.WANDevice.%s.WANConnectionDevice";
        String dataTree;
        if (__VERSION == 2) {
            dataTree = String.format(pathTemplate, AppConfig.ONT_V2_WANDEVICE_INDEX);
        } else {
            // default __VERSION = 1;
            dataTree = String.format(pathTemplate, AppConfig.ONT_V1_WANDEVICE_INDEX);
        }
        Root tempRoot = getTreeRoot(dataTree + ".");
        tempRoot.setPath(dataTree);
        tempRoot.setVersion(__VERSION);
        WANGUIObjectEthernet returnValue = new WANGUIObjectEthernet();
        ArrayList<Layer2InterfaceObject> layer2 = getLayer2LanInterface(tempRoot);
        ArrayList<Layer2InterfaceObject> layer2Data = new ArrayList<Layer2InterfaceObject>();
        for (Layer2InterfaceObject item : layer2) {
            if (TR069StaticParameter.LinkType_EOA.equals(item.getListObject().get(1).getValue())) {
                layer2Data.add(item);
            }
        }
        returnValue.setLayer2LanInterface(layer2Data);
        returnValue.setWanService(getEthernetWANService(tempRoot));
        return returnValue;
    }

    public WANGUIObject getWANGUIObject() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.");
        WANGUIObject returnValue = new WANGUIObject();
        ArrayList<Layer2InterfaceObject> layer2 = getATMInterface(tempRoot);
        ArrayList<Layer2InterfaceObject> layer2Data = new ArrayList<Layer2InterfaceObject>();
        for (Layer2InterfaceObject item : layer2) {
            if (TR069StaticParameter.LinkType_EOA.equals(item.getListObject().get(1).getValue())) {
                layer2Data.add(item);
            }
        }
        returnValue.setAtmInterface(layer2Data);
        returnValue.setWanService(getATMWANService(tempRoot));
        return returnValue;
    }

    public ArrayList<WanServiceObject> getEthernetWANService() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.WANDevice.3.WANConnectionDevice.");
        return getEthernetWANService(tempRoot);
    }

    public ArrayList<WanServiceObject> getEthernetWANService(Root tempRoot) throws Exception {
        return toEthernetWANService(tempRoot);
    }

    public ArrayList<WanServiceObject> getATMWANService() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.");
        return getATMWANService(tempRoot);
    }

    public ArrayList<WanServiceObject> getATMWANService(Root tempRoot) throws Exception {
        ArrayList<WanServiceObject> returnValue = new ArrayList<WanServiceObject>();

        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();

        ArrayList<Layer2InterfaceObject> layer2 = getATMInterface(tempRoot);

        //get PPPoE
        ArrayList<SimpleObject> listObj_PPP = myUtil.getListSimpleObjectFromFile(FilePath.GET_PPPoE);
        for (int i = 0; i < layer2.size(); i++) {
            String str_ppp = layer2.get(i).getRootName() + "." + layer2.get(i).getInstance() + ".WANPPPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ppp_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ppp);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ppp_Instances != null) {
                //list Object from file				
                for (int j = 0; j < ppp_Instances.size(); j++) {
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setLayer2Interface(layer2.get(i));
                    if (TR069StaticParameter.LinkType_EOA.equals(layer2.get(i).getListObject().get(1).getValue())) {
                        temp3.setType(WanServiceObject.PPPoE_TYPE);
                    } else {
                        temp3.setType(WanServiceObject.PPPoA_TYPE);
                    }

                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (int u = 0; u < listObj_PPP.size(); u++) {
                        listObj.add(listObj_PPP.get(u).cloneObject());
                        //SimpleObject yyy = xxx.;
                    }
                    //end clone

                    temp3.setInstance(ppp_Instances.get(j));
                    for (int k = 0; k < listObj.size(); k++) {
//						System.out.println("uuuu : "+str_ppp_atm+"."+ppp_atmInstances.get(j)+"."+listObj.get(k).getParameter());
                        listObj.get(k).setParameter(str_ppp + "." + ppp_Instances.get(j) + "." + listObj.get(k).getParameter());
                    }
                    temp3.setListObject(myTreeUtil.getArrayValues(tempRoot, listObj));
                    returnValue.add(temp3);
                }
            }
        }
        //get IPoE
        ArrayList<SimpleObject> listObj_IP = myUtil.getListSimpleObjectFromFile(FilePath.GET_IPoE);
        for (int i = 0; i < layer2.size(); i++) {
            String str_ip = layer2.get(i).getRootName() + "." + layer2.get(i).getInstance() + ".WANIPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ip_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ip);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ip_Instances != null) {
                //list Object from file				
                for (int j = 0; j < ip_Instances.size(); j++) {
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setLayer2Interface(layer2.get(i));
                    if (TR069StaticParameter.LinkType_EOA.equals(layer2.get(i).getListObject().get(1).getValue())) {
                        temp3.setType(WanServiceObject.IPoE_TYPE);
                    } else {
                        temp3.setType(WanServiceObject.IPoA_TYPE);
                    }

                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (int u = 0; u < listObj_IP.size(); u++) {
                        listObj.add(listObj_IP.get(u).cloneObject());
                        //SimpleObject yyy = xxx.;
                    }
                    //end clone

                    temp3.setInstance(ip_Instances.get(j));
                    for (int k = 0; k < listObj.size(); k++) {
//						System.out.println("uuuu : "+str_ip_atm+"."+ip_atmInstances.get(j)+"."+listObj.get(k).getParameter());
                        listObj.get(k).setParameter(str_ip + "." + ip_Instances.get(j) + "." + listObj.get(k).getParameter());
                    }
                    temp3.setListObject(myTreeUtil.getArrayValues(tempRoot, listObj));
                    returnValue.add(temp3);
                }
            }
        }

        return returnValue;
    }

    public ArrayList<WanServiceObject> getWANService() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.WANDevice.");
        return getWANService(tempRoot);
    }

    public ArrayList<WanServiceObject> getWANService(Root tempRoot) throws Exception {
        ArrayList<WanServiceObject> returnValue = new ArrayList<WanServiceObject>();

        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();

        ArrayList<Layer2InterfaceObject> layer2 = getLayer2Interface(tempRoot);

        //get PPPoE
        ArrayList<SimpleObject> listObj_PPP = myUtil.getListSimpleObjectFromFile(FilePath.GET_PPPoE);
        for (int i = 0; i < layer2.size(); i++) {
            String str_ppp = layer2.get(i).getRootName() + "." + layer2.get(i).getInstance() + ".WANPPPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ppp_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ppp);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ppp_Instances != null) {
                //list Object from file				
                for (int j = 0; j < ppp_Instances.size(); j++) {
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setLayer2Interface(layer2.get(i));
                    temp3.setType(WanServiceObject.PPPoE_TYPE);

                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (int u = 0; u < listObj_PPP.size(); u++) {
                        listObj.add(listObj_PPP.get(u).cloneObject());
                        //SimpleObject yyy = xxx.;
                    }
                    //end clone

                    temp3.setInstance(ppp_Instances.get(j));
                    for (int k = 0; k < listObj.size(); k++) {
//						System.out.println("uuuu : "+str_ppp_atm+"."+ppp_atmInstances.get(j)+"."+listObj.get(k).getParameter());
                        listObj.get(k).setParameter(str_ppp + "." + ppp_Instances.get(j) + "." + listObj.get(k).getParameter());
                    }
                    temp3.setListObject(myTreeUtil.getArrayValues(tempRoot, listObj));
                    returnValue.add(temp3);
                }
            }
        }
        //get IPoE
        ArrayList<SimpleObject> listObj_IP = myUtil.getListSimpleObjectFromFile(FilePath.GET_IPoE);
        for (int i = 0; i < layer2.size(); i++) {
            String str_ip = layer2.get(i).getRootName() + "." + layer2.get(i).getInstance() + ".WANIPConnection";
            //System.out.println("123124556: "+str_ppp_atm);
            ArrayList<Integer> ip_Instances = myTreeUtil.getListInstanceFromPath(tempRoot, str_ip);
            //System.out.println("xxxxx: "+ppp_atmInstances.size());
            if (ip_Instances != null) {
                //list Object from file				
                for (int j = 0; j < ip_Instances.size(); j++) {
                    WanServiceObject temp3 = new WanServiceObject();
                    temp3.setLayer2Interface(layer2.get(i));
                    temp3.setType(WanServiceObject.IPoE_TYPE);

                    //clone list obj2 
                    ArrayList<SimpleObject> listObj = new ArrayList<SimpleObject>();
                    for (int u = 0; u < listObj_IP.size(); u++) {
                        listObj.add(listObj_IP.get(u).cloneObject());
                        //SimpleObject yyy = xxx.;
                    }
                    //end clone

                    temp3.setInstance(ip_Instances.get(j));
                    for (int k = 0; k < listObj.size(); k++) {
//						System.out.println("uuuu : "+str_ip_atm+"."+ip_atmInstances.get(j)+"."+listObj.get(k).getParameter());
                        listObj.get(k).setParameter(str_ip + "." + ip_Instances.get(j) + "." + listObj.get(k).getParameter());
                    }
                    temp3.setListObject(myTreeUtil.getArrayValues(tempRoot, listObj));
                    returnValue.add(temp3);
                }
            }
        }

        return returnValue;
    }

    public ArrayList<Layer2InterfaceObject> getATMInterface(String root) throws Exception {
        Root tempRoot = getTreeRoot(root);
        return getATMInterface(tempRoot);
    }

    public ArrayList<Layer2InterfaceObject> getATMInterface(Root tempRoot) throws Exception {
        ArrayList<Layer2InterfaceObject> returnValue = new ArrayList<Layer2InterfaceObject>();
        //get data from file
        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();
        //ATM
        ArrayList<String> atmLine = myUtil.getLineFromFile(FilePath.GET_ATM_INTERFACE);
        ArrayList<Integer> atmInstances = myTreeUtil.getListInstanceFromPath(tempRoot, atmLine.get(0));
        if (atmInstances != null) {
            for (int i = 0; i < atmInstances.size(); i++) {
                Layer2InterfaceObject temp = new Layer2InterfaceObject();
                temp.setRootName(atmLine.get(0));
                temp.setType(Layer2InterfaceObject.ATM_TYPE);
                temp.setInstance(atmInstances.get(i));

                for (int j = 1; j < atmLine.size(); j++) {
                    ArrayList<String> element4 = myUtil.get4ElementFromLine(atmLine.get(j));
                    SimpleObject tmp_ob = new SimpleObject();
                    tmp_ob.setId(Integer.parseInt(element4.get(0)));
                    tmp_ob.setName(element4.get(1));
                    tmp_ob.setParameter(atmLine.get(0) + "." + String.valueOf(atmInstances.get(i)) + "." + element4.get(2));
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

    public ArrayList<Layer2InterfaceObject> getLayer2LanInterface() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.WANDevice.3.WANConnectionDevice.");
        return getLayer2Interface(tempRoot);
    }

    public ArrayList<Layer2InterfaceObject> getLayer2LanInterface(Root tempRoot) throws Exception {
        return toLayer2LanInterface(tempRoot);
    }

    public ArrayList<Layer2InterfaceObject> getLayer2Interface() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.WANDevice.");
        return getLayer2Interface(tempRoot);
    }

    public ArrayList<Layer2InterfaceObject> getLayer2Interface(Root tempRoot) throws Exception {
        ArrayList<Layer2InterfaceObject> returnValue = new ArrayList<Layer2InterfaceObject>();

        //Root tempRoot = getTreeRoot("InternetGatewayDevice.WANDevice.");
        //listTree(tempRoot);
        //get data from file
        DataFileUtils myUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();
        //ATM
        ArrayList<String> atmLine = myUtil.getLineFromFile(FilePath.GET_ATM_INTERFACE);
        ArrayList<Integer> atmInstances = myTreeUtil.getListInstanceFromPath(tempRoot, atmLine.get(0));
        for (int i = 0; i < atmInstances.size(); i++) {
            Layer2InterfaceObject temp = new Layer2InterfaceObject();
            temp.setRootName(atmLine.get(0));
            temp.setType(Layer2InterfaceObject.ATM_TYPE);
            temp.setInstance(atmInstances.get(i));

            for (int j = 1; j < atmLine.size(); j++) {
                ArrayList<String> element4 = myUtil.get4ElementFromLine(atmLine.get(j));
                SimpleObject tmp_ob = new SimpleObject();
                tmp_ob.setId(Integer.parseInt(element4.get(0)));
                tmp_ob.setName(element4.get(1));
                tmp_ob.setParameter(atmLine.get(0) + "." + String.valueOf(atmInstances.get(i)) + "." + element4.get(2));
                tmp_ob.setType(element4.get(3));

                temp.getListObject().add(tmp_ob);
            }

            returnValue.add(temp);
        }
        //PTM
        ArrayList<String> ptmLine = myUtil.getLineFromFile(FilePath.GET_PTM_INTERFACE);
        ArrayList<Integer> ptmInstances = myTreeUtil.getListInstanceFromPath(tempRoot, ptmLine.get(0));
        if (ptmInstances != null) {
            for (int i = 0; i < ptmInstances.size(); i++) {
                Layer2InterfaceObject temp = new Layer2InterfaceObject();
                temp.setRootName(ptmLine.get(0));
                temp.setType(Layer2InterfaceObject.PTM_TYPE);
                temp.setInstance(ptmInstances.get(i));

                for (int j = 1; j < ptmLine.size(); j++) {
                    ArrayList<String> element4 = myUtil.get4ElementFromLine(ptmLine.get(j));
                    SimpleObject tmp_ob = new SimpleObject();
                    tmp_ob.setId(Integer.parseInt(element4.get(0)));
                    tmp_ob.setName(element4.get(1));
                    tmp_ob.setParameter(ptmLine.get(0) + "." + String.valueOf(ptmInstances.get(i)) + "." + element4.get(2));
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

    public ArrayList<LanSetup_IPLease> getLAN_IPLease(String pathRoot, Root tempRoot) throws IOException {
        ArrayList<LanSetup_IPLease> returnValue = new ArrayList<LanSetup_IPLease>();

        DataFileUtils myStringUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();

        ArrayList<String> getLine = myStringUtil.getLineFromFile(FilePath.READ_LANIPLEASE);
        ArrayList<Integer> listInstances = myTreeUtil.getListInstanceFromPath(tempRoot, pathRoot);
        if (listInstances != null) {
            for (int i = 0; i < listInstances.size(); i++) {
                LanSetup_IPLease returnObj = new LanSetup_IPLease();
                returnObj.setRootName(pathRoot);
                returnObj.setInstance(listInstances.get(i));

                for (int j = 1; j < getLine.size(); j++) {
                    ArrayList<String> element4 = myStringUtil.get4ElementFromLine(getLine.get(j));
                    SimpleObject tmp = new SimpleObject();
                    tmp.setId(Integer.parseInt(element4.get(0)));
                    tmp.setName(element4.get(1));
                    tmp.setParameter(pathRoot + "." + listInstances.get(i) + "." + element4.get(2));
                    tmp.setType(element4.get(3));

                    returnObj.getListObj().add(tmp);
                }

                myTreeUtil.getArrayValues(tempRoot, returnObj.getListObj());
                returnValue.add(returnObj);
            }
        }

        return returnValue;
    }

    public LanSetupObject getLanSetup(String bridgeIndex) throws Exception {

        // get param from file
        DataFileModel dm = DataFileModel.createDataFileModel(FilePath.GET_LANPARAM, null);
        dm.setInstance(bridgeIndex);
        GetValueCommand cm = new GetValueCommand(serialNumber, connectionRequestURL);

        List<SimpleObject> datas = dm.getParsedValues();
        for (SimpleObject data : datas) {
            cm.addParameter(data.getParameter());
        }

        boolean hasError = cm.executeCommand();
        if (hasError) {
            throw new Exception("can not get cpe params");
        }

        // read config from return value
        LanSetupObject returnValue = new LanSetupObject();
        Map<String, String> vlue = cm.getReturnValue();

        // get lan common params
        ArrayList<SimpleObject> lanCommonList = new ArrayList<SimpleObject>(datas.size());
        for (SimpleObject lanConfig : datas) {
            if (vlue.containsKey(lanConfig.getParameter())) {
                lanConfig.setValue(vlue.get(lanConfig.getParameter()));
                lanCommonList.add(lanConfig);
            }

        }
        returnValue.setLanCommon(lanCommonList);

        // read lan ip lease
        Root tempRoot = new Root("InternetGatewayDevice");
        TreeUtils myTreeUtil = new TreeUtils();
        for (Map.Entry<String, String> element : vlue.entrySet()) {
            tempRoot = myTreeUtil.createTree(tempRoot, element.getKey(), element.getValue());
        }
        String pathRoot = "InternetGatewayDevice.LANDevice." + bridgeIndex + ".LANHostConfigManagement.DHCPConditionalServingPool";
        ArrayList<LanSetup_IPLease> temp = getLAN_IPLease(pathRoot, tempRoot);
        returnValue.setLanIPLease(temp);
        return returnValue;
    }

    public ArrayList<WirelessObject> getWirelessInfor() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.");
        return getWirelessInfor(tempRoot);
    }

    public ArrayList<WirelessObject> getWirelessInfor(Root tempRoot) throws IOException {
        ArrayList<WirelessObject> returnValue = new ArrayList<WirelessObject>();
        //get data
        DataFileUtils myStringUtil = new DataFileUtils();
        TreeUtils myTreeUtil = new TreeUtils();

        ArrayList<SimpleObject> dataFile = null;
        for (int i = 0; i < 4; i++) {
            WirelessObject wlObj = new WirelessObject();
            if (i == 0) {
                dataFile = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_WIRELESS);
            } else if (i == 1) {
                dataFile = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_WIRELESS_1);
            } else if (i == 2) {
                dataFile = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_WIRELESS_2);
            } else if (i == 3) {
                dataFile = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_WIRELESS_3);
            }
            dataFile = myTreeUtil.getArrayValues(tempRoot, dataFile);
            if (i == 0) {
                //enable
                wlObj.getEnableWireless().add(dataFile.get(0));
                wlObj.getEnableWireless().add(dataFile.get(1));
                wlObj.getEnableWireless().add(dataFile.get(2));
                //ssid
                wlObj.getSsidName().add(dataFile.get(3));
                wlObj.getSsidName().add(dataFile.get(4));
                //isolate
                wlObj.setIsolateClient(dataFile.get(5));
                //hidden
                wlObj.getHidden().add(dataFile.get(6));
                wlObj.getHidden().add(dataFile.get(7));
                //DiableWMM
                wlObj.setDisableWMM(dataFile.get(8));
                //Enable WMF
                wlObj.setEnableWMF(dataFile.get(9));
                //max client
                wlObj.setMaxClient(dataFile.get(10));
                //security
                wlObj.getSecurity().add(dataFile.get(11));
                wlObj.getSecurity().add(dataFile.get(12));
                wlObj.getSecurity().add(dataFile.get(13));
                wlObj.getSecurity().add(dataFile.get(14));
                wlObj.getSecurity().add(dataFile.get(15));
            } else {
                //enable
                wlObj.getEnableWireless().add(dataFile.get(0));
                wlObj.getEnableWireless().add(dataFile.get(1));
                //ssid
                wlObj.getSsidName().add(dataFile.get(2));
                wlObj.getSsidName().add(dataFile.get(3));
                //isolate
                wlObj.setIsolateClient(dataFile.get(4));
                //hidden
                wlObj.getHidden().add(dataFile.get(5));
                wlObj.getHidden().add(dataFile.get(6));
                //DiableWMM
                wlObj.setDisableWMM(dataFile.get(7));
                //Enable WMF
                wlObj.setEnableWMF(dataFile.get(8));
                //max client
                wlObj.setMaxClient(dataFile.get(9));
                //security
                wlObj.getSecurity().add(dataFile.get(10));
                wlObj.getSecurity().add(dataFile.get(11));
                wlObj.getSecurity().add(dataFile.get(12));
                wlObj.getSecurity().add(dataFile.get(13));
                wlObj.getSecurity().add(dataFile.get(14));
            }

            returnValue.add(wlObj);
        }

        return returnValue;
    }

    public PerformanceObject getPerformanceDevice() throws Exception {
        Root tempRoot = getTreeRoot("InternetGatewayDevice.");
        return getPerformanceDevice(tempRoot);
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
        //end get wan performance
        ///////////////////////get DSL Performance	
        ArrayList<SimpleObject> dslPerformance = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_DSLPERFORMANCE);
        ArrayList<SimpleObject> opticalInfo = myStringUtil.getListSimpleObjectFromFile(FilePath.GET_OPTICAL_INFO);
        //add value to list parameter
        dslPerformance = treeUtil.getArrayValues(tempRoot, dslPerformance);
        opticalInfo = treeUtil.getArrayValues(tempRoot, opticalInfo);
        //add traffic type
        SimpleObject trafficType = new SimpleObject("", "TrafficType", "ATM", "xsd:string");
        trafficType.setId(16);
        dslPerformance.add(trafficType);
        //add to returnvalue
        returnValue.setDslPerformance(dslPerformance);
        returnValue.setOpticalInfo(opticalInfo);
        //end get dsl	
        return returnValue;
    }

//    public Layer3GUIObject getLayer3GUIObject() throws Exception {
//        Root tempRoot = getTreeRoot("InternetGatewayDevice.");
//        return getLayer3GUIObject(tempRoot);
//    }
//
//    public Layer3GUIObject getLayer3GUIObject(Root tempRoot) throws Exception {
//        Layer3GUIObject returnValue = new Layer3GUIObject();
//        DataFileUtils myStringUtil = new DataFileUtils();
//        ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.STATIC_ROUTE_IP);
//        TreeUtils treeUtil = new TreeUtils();
//
//        //set value default gateway
//        SimpleObject defaultGateway = new SimpleObject();
//        defaultGateway.setParameter(TR069StaticParameter.Default_Gateway);
//        defaultGateway.setType("xsd:string");
//        defaultGateway.setValue(treeUtil.getValueFromPath(tempRoot, TR069StaticParameter.Default_Gateway));
//        returnValue.setDefaultGateway(defaultGateway);
//        //set value default dns
//        SimpleObject defaultDNS = new SimpleObject();
//        defaultDNS.setParameter(TR069StaticParameter.Default_DNS);
//        defaultDNS.setType("xsd:string");
//        defaultDNS.setValue(treeUtil.getValueFromPath(tempRoot, TR069StaticParameter.Default_DNS));
//        returnValue.setDefaultDNS(defaultDNS);
//        //set value list wan service
//        ArrayList<WanServiceObject> listWanServiceObj = getATMWANService(tempRoot);
//        ArrayList<WanServiceObject> returnWanObj = new ArrayList<WanServiceObject>();
//        for (WanServiceObject item : listWanServiceObj) {
//            if (item.getType().equals(WanServiceObject.IPoE_TYPE) || item.getType().equals(WanServiceObject.PPPoE_TYPE)) {
//                returnWanObj.add(item);
//            }
//        }
//        returnValue.setListWanService(returnWanObj);
//        //set value list static ip
//        ArrayList<StaticRouteObject> staticRouteObj = new ArrayList<StaticRouteObject>();
//        ArrayList<Integer> listInstance = treeUtil.getListInstanceFromPath(tempRoot, lines.get(0));
//        ArrayList<SimpleObject> dataSample = myStringUtil.getListSimpleObjectFromList(lines, 1, lines.size());
//        if (listInstance != null) {
//            for (Integer item : listInstance) {
//                StaticRouteObject sttObj = new StaticRouteObject();
//                sttObj.setInstance(item);
//                //clone
//                ArrayList<SimpleObject> cloneObj = new ArrayList<SimpleObject>();
//                for (SimpleObject obj : dataSample) {
//                    cloneObj.add(obj.cloneObject());
//                }
//                //edit parameter name
//                for (SimpleObject obj : cloneObj) {
//                    obj.setParameter(lines.get(0) + "." + item + "." + obj.getParameter());
//                }
//                //set value
//                cloneObj = treeUtil.getArrayValues(tempRoot, cloneObj);
//                //end clone
//                sttObj.setListObj(cloneObj);
//                //add to list
//                staticRouteObj.add(sttObj);
//            }
//        }
//        returnValue.setListStaticRoute(staticRouteObj);
//
//        return returnValue;
//    }

    public String getUptime() throws Exception {
        GetValueCommand cmand = new GetValueCommand(user_Username, serialNumber, connectionRequestURL, "InternetGatewayDevice.DeviceInfo.UpTime");
        boolean ok = cmand.executeCommand();
        Map<String, String> vlue = null;
        if (!ok) {
            vlue = cmand.getReturnValue();
        }
        return vlue.get("InternetGatewayDevice.DeviceInfo.UpTime");
    }

    public List<ParameterModel> getValue(List<String> listParam) throws Exception {
        GetValueCommand cmand = new GetValueCommand(serialNumber, connectionRequestURL);
        cmand.setTreeNode(listParam);
        boolean ok = cmand.executeCommand();
        Map<String, String> vlue = null;
        if (!ok) {
            vlue = cmand.getReturnValue();
        }
        if (ok) {
            throw new Tr069Exception(cmand.getErrorString());
        }
        if (vlue != null) {
            List<ParameterModel> listParamModel = new ArrayList<ParameterModel>(vlue.size());
            for (Map.Entry<String, String> entry : vlue.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
                ParameterModel tmp = new ParameterModel();
                tmp.setParameter(entry.getKey());
                tmp.setValue(entry.getValue());
                listParamModel.add(tmp);
            }
            return listParamModel;
        }
        return null;
    }

    public String scheduleInform(int timeSchedule) throws Exception {
        ScheduleInformCommand cm = new ScheduleInformCommand(serialNumber, connectionRequestURL, timeSchedule);
        boolean ok = cm.executeCommand();
        return ok == false ? "Success" : "Failed";

    }

    public List<ParameterInforStruct> getParameterNames(String parameter, boolean nextLevel) throws Exception {
        GetParameterNamesCommand cmand = new GetParameterNamesCommand(serialNumber, connectionRequestURL);
        cmand.setParameterNames(parameter);
        cmand.setNextLevel(nextLevel);
        boolean ok = cmand.executeCommand();
        Map<String, String> vlue = null;
        if (!ok) {
            vlue = cmand.getReturnValue();
        }
        if (vlue != null) {
            List<ParameterInforStruct> listParamModel = new ArrayList<ParameterInforStruct>(vlue.size());
            for (Map.Entry<String, String> entry : vlue.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
                ParameterInforStruct tmp = new ParameterInforStruct();
                tmp.setParameterName(entry.getKey());
                tmp.setWriteAble(Boolean.parseBoolean(entry.getValue()));
                listParamModel.add(tmp);
            }
            return listParamModel;
        }
        return null;

    }

    public StaticDynamicDNSObject addDynamicDNSObject(StaticDynamicDNSObject DDNSObject, String modelName) throws IOException, Exception {
        StaticDynamicDNSObject returnValue = new StaticDynamicDNSObject();

        DataFileModel dm = DataFileModel.createDataFileModel(FilePath.StaticDynamicDNS, modelName);
        String Root = dm.getRoot().getParameter();

        //dm.setValue("enable", DDNSObject.getEnable());
        dm.setValue("fullyQualifiedDomainName", DDNSObject.getFullyQualifiedDomainName());
        dm.setValue("ifName", DDNSObject.getIfName());
        dm.setValue("fassword", DDNSObject.getPassWord());
        dm.setValue("providerName", DDNSObject.getProviderName());
        dm.setValue("userName", DDNSObject.getUserName());

        ArrayList<SimpleObject> datas = dm.getDataModel(null, null);
        Integer instance = addObjectValue(Root, datas, AddObjectCommand.ADDBJ_DDNS);

        return returnValue;
    }

//<editor-fold defaultstate="collapsed" desc="@Vunb 2015-03-06 add feature: wifi offload">
    public Map<String, WirelessSecurity> getAllWirelessSecurity() throws IOException, Exception {
        Map<String, String> datas = getTreeValue(WirelessSecurity.DataTree.Root + ".");
        Map<String, WirelessSecurity> listWlSecurity = ModelUtils.parseWirelessSecurity(datas);
        return listWlSecurity;
    }

    public boolean configWirelessSecurity(WirelessSecurity wlSecurity) throws IOException, Exception {
        //0. check condition
        if (StringUtils.isBlank(wlSecurity.getWlAuthMode())) {
            throw new Exception("Network Authentication is required: NULL");
        } else if (StringUtils.isBlank(wlSecurity.getWlSsid())) {
            throw new Exception("SSID is required: NULL");
        }
        //1. get all wireless security enable
        WirelessSecurity selectSSID = null;
        Map<String, String> datas = getTreeValue(WirelessSecurity.DataTree.Root + ".");
        Map<String, WirelessSecurity> listWlSecurity = ModelUtils.parseWirelessSecurity(datas);
        for (WirelessSecurity item : listWlSecurity.values()) {
            if (wlSecurity.getWlSsid().equalsIgnoreCase(item.getWlSsid())) {
                selectSSID = item;
            }
        }
        if (selectSSID == null) {
            throw new Exception("SSID not Found.");
        }
        //2. set up data wireless security
        DataFileUtils dataFileUtil = new DataFileUtils();
        ArrayList<String> lines = dataFileUtil.getLineFromFile(FilePath.Set_WirelessSecurity);
        ArrayList<SimpleObject> wlSecurityObj = dataFileUtil.getListSimpleObjectFromList(lines, 1, lines.size());
        String wlsecRoot = lines.get(0) + "." + selectSSID.getInstance();
        for (SimpleObject simpleObject : wlSecurityObj) {
            simpleObject.setParameter(wlsecRoot + "." + simpleObject.getParameter());
        }
        wlSecurityObj.get(0).setValue(wlSecurity.getWlSsid());
        wlSecurityObj.get(1).setValue(wlSecurity.getWlAuthMode());
        wlSecurityObj.get(2).setValue(String.valueOf(wlSecurity.getWlWpaGTKRekey()));
        wlSecurityObj.get(3).setValue(wlSecurity.getWlRadiusServerIP());
        wlSecurityObj.get(4).setValue(wlSecurity.getWlRadiusKey());
        wlSecurityObj.get(5).setValue(String.valueOf(wlSecurity.getWlRadiusPort()));
        wlSecurityObj.get(6).setValue(wlSecurity.getWlWpa());
        wlSecurityObj.get(7).setValue(wlSecurity.getWlWep());
        wlSecurityObj.get(8).setValue(String.valueOf(wlSecurity.getWlPreauth()));
        wlSecurityObj.get(9).setValue(String.valueOf(wlSecurity.getWlNetReauth()));

        // check and remove unnecessary
        if (wlSecurity.getWlAuthMode().contains("wp2")) {
            wlSecurityObj.remove(8);
            wlSecurityObj.remove(9);
        }
        // request device
        boolean result = setValueObjects(wlSecurityObj);
        return result;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="2015-05-14: Support ONT VER2">
    public WanServiceObject addPPPoEWanService_ethernet(
            String serviceName,
            String username,
            String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String modelName,
            String connectionId, int wanIndex
    ) throws Exception {
        Layer2InterfaceObject layer2 = addLayer2LanObjByVersion(modelName);
        //add wan interface
        serviceName = StringUtils.defaultIfEmpty(serviceName, "pppoe_veip0");
        WanServiceObject returnVl = addPPPoEWanService(layer2, serviceName, username, password, vlanMux802_1Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled, connectionId, wanIndex);
        return returnVl;
    }

    public WanServiceObject addPPPoEWanService_ethernet(
            String serviceName,
            String username,
            String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String modelName,
            String connectionId
    ) throws Exception {
        return addPPPoEWanService_ethernet(
                serviceName,
                username,
                password,
                vlanMux802_1Priority,
                vlanMuxID,
                ipV4Enabled,
                ipV6Enabled,
                natEnabled,
                firewallEnabled,
                modelName,
                connectionId, 0
        ); // default wanindex = 0
    }

    public WanServiceObject addIPoEWanServiceDHCP_ethernet(
            IPoEWanServiceDHCP dhcpWanService,
            String modelName) throws Exception {
        Layer2InterfaceObject layer2 = addLayer2LanObjByVersion(modelName);
        //add wan interface
        WanServiceObject returnVl = addIPoEWanService(layer2, dhcpWanService);
        return returnVl;
    }

    public WanServiceObject addIPoEWanServiceStatic_ethernet(
            IPoEWanServiceStatic staticWanService, String modelName) throws Exception {
        Layer2InterfaceObject layer2 = addLayer2LanObjByVersion(modelName);
        //add wan interface
        WanServiceObject returnVl = addIPoEWanService(layer2, staticWanService);
        return returnVl;
    }

    public WanServiceObject addBridgeWanService(String serviceDescription, int vlanMux802_1Priority,
            int vlanMuxID, String modelName, String connectionId, int wanIndex) throws Exception {
        Layer2InterfaceObject layer2 = addLayer2LanObjByVersion(modelName);
        //add wan interface
        sb.setLength(0);
        sb.append("addBridgeWanService: ServiceDescription=")
                .append(serviceDescription)
                .append(", vlanMux802_1Priority=")
                .append(vlanMux802_1Priority)
                .append(", vlanMuxID=")
                .append(vlanMuxID)
                .append(", __MODEL_NAME=")
                .append(modelName);
        logger.info(sb);

        WanServiceObject returnVl = addBridgeWanService(layer2, serviceDescription, vlanMux802_1Priority, vlanMuxID, connectionId, wanIndex);
        return returnVl;
    }

    public WanServiceObject addBridgeWanService(String serviceDescription, int vlanMux802_1Priority,
            int vlanMuxID, String modelName, String connectionId) throws Exception {
        return addBridgeWanService(serviceDescription, vlanMux802_1Priority, vlanMuxID, modelName, connectionId, 0); // default wanIndex = 0
    }

//</editor-fold>
}
