/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.GetValueCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.RebootCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.SetValueCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.BrigdingGroup;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.DeviceInterface;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.ModelUtils;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.ModemInterfaceGroups;
import vn.vnpttech.ssdc.nms.model.Device;

/**
 *
 * @author SSDC
 */
public class ServiceConfigGateway {

    private static final Logger logger = Logger.getLogger(ServiceConfigGateway.class.getName());

    public static final String WlVirtIntfCfg = "WlVirtIntfCfg";
    public static final String WlIfcname = "WlIfcname";
    public static final String InterfaceDeviceName = "Name";
    public static final String X_BROADCOM_COM_IfName = "X_BROADCOM_COM_IfName";
    public static final String FilterBridgeReference = "FilterBridgeReference";
    public static final String FBRHead = "InternetGatewayDevice.Layer2Bridging.Filter.";
    public static final String FBRFoot = ".FilterBridgeReference";
    public static final String WANConnectionDevice = "WANConnectionDevice";
    public static final String ExternalIPAddress = "ExternalIPAddress";
    public static final String DEFAULT_GATEWAY_PARAM = "InternetGatewayDevice.Layer3Forwarding.X_BROADCOM_COM_DefaultConnectionServices";
    public static final String DEFAULT_DNS_PARAM = "InternetGatewayDevice.X_BROADCOM_COM_NetworkConfig.DNSIfName";

    public static final String NOTE = "SET_GATEWAY";

    public static ModemInterfaceGroups getDeviceInterfaceGroups(String serialNumber, String connectionRequestURL) throws Exception {
        String dataTree = "InternetGatewayDevice.Layer2Bridging.";
        Map<String, String> datas = getTreeValue(dataTree, serialNumber, connectionRequestURL);
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

                // lay them connection type cua wan connection
                String connectionType = strSend + "." + "ConnectionType";
                listSendToGet.add(connectionType);

                // lay them vlan id
                String vlanId = strSend + "." + "X_BROADCOM_COM_VlanMuxID";
                listSendToGet.add(vlanId);
            }
            listSendToGet.add(strIfName);
            // lay them tham so ExternalIPAddress cua pppoe
            if (strSend.contains("WANPPPConnection") || strSend.contains("WANIPConnection")) {
                String externalIp = strSend + "." + ExternalIPAddress;
                listSendToGet.add(externalIp);
            }
        }

        // get ve name va interface name
        GetValueCommand cm = new GetValueCommand(serialNumber);
        cm.setConnectionRequestURL(connectionRequestURL);
        for (String strSend : listSendToGet) {
            cm.addParam(strSend);
        }
        boolean hasError = cm.executeCommand();
        if (!hasError) {
            throw new Exception(cm.getErrorString());
        }

        Map<String, String> mapAdd = cm.getReturnValue();
        for (Map.Entry<String, DeviceInterface> entry : devinterfaces.entrySet()) {
            DeviceInterface diTemp = entry.getValue();
            String ifRef = diTemp.getIfReference() + ".";

            String ifName = org.apache.commons.lang.StringUtils.defaultIfEmpty(mapAdd.get(ifRef + X_BROADCOM_COM_IfName), mapAdd.get(ifRef + WlIfcname));
            String name = mapAdd.get(ifRef + InterfaceDeviceName);

            if (ifRef.contains(WANConnectionDevice)) {

                String externalIp = mapAdd.get(ifRef + ExternalIPAddress);
                diTemp.setExternalIp(externalIp);

                String connectionType = mapAdd.get(ifRef + "ConnectionType");
                diTemp.setConnectionType(connectionType.trim());

                String vlanId = mapAdd.get(ifRef + "X_BROADCOM_COM_VlanMuxID");
                diTemp.setVlanId(vlanId.trim());
            }

            diTemp.setName(name);
            diTemp.setIfName(ifName);

            digroup.add(diTemp);
        }
        ifgroups.setBrigdingGroup(bgroups);
        ifgroups.setDeviceInterfaces(digroup);
        return ifgroups;
    }

    public static Map<String, String> getTreeValue(String pathTree, String serialNumber, String connectionRequestURL) throws Exception {
//        GetValueCommand cmand = new GetValueCommand("@VuSnail", serialNumber, connectionRequestURL, pathTree);
        GetValueCommand cmand = new GetValueCommand(serialNumber, pathTree);
        cmand.setConnectionRequestURL(connectionRequestURL);
        boolean ok = cmand.executeCommand();
        if (ok) {
            return cmand.getReturnValue();
        } else {
            String error = org.apache.commons.lang.StringUtils.defaultIfEmpty(cmand.getErrorString(), "Execute command failed");
            throw new Exception(error);
        }
    }

    /**
     * luat lay default gateway
     *
     * 1. Loại bỏ các WAN sau khỏi list chọn default gateway/dns.:
     *
     * a. Vlan ID 4000 – veip0.1 ( Quản lý TR-069).
     *
     * b. Vlan ID 441 (veip0.2), 400 ( veip0.3) (Wifi offload).
     *
     * 2. Nếu có WAN PPPoE:
     *
     * a. Nếu có 1 WAN PPPoE  chọn làm default GW/DNS.
     *
     * b. * Nếu có 2 WAN PPPoE  Chọn wan PPPoE đầu tiên có IP làm default
     * GW/DNS.
     *
     * 3. Nếu không có WAN PPPoE  Chọn WAN đầu tiên có IP làm default GW/DNS.
     *
     * 4. Nếu không có WAN nào có IP  bật cờ lỗi không set được default GW/DNS.
     *
     *
     * @param ig
     * @return
     */
    public static String chooseDefautGateway(ModemInterfaceGroups ig) {

        String defaultGateway = null;
//        String defaultGateway = "ppp0.4";

        // uu tien cac wan pppoe truoc        
        for (DeviceInterface deviceInterface : ig.getDeviceInterfaces()) {

            if ("4000".equalsIgnoreCase(deviceInterface.getVlanId())
                    || "441".equalsIgnoreCase(deviceInterface.getVlanId())
                    || "400".equalsIgnoreCase(deviceInterface.getVlanId())) {

                continue;
            }

            if (deviceInterface.getIfReference().contains("WANPPPConnection")) {
                logger.info("DEFAULT GATE IP: " + deviceInterface.getExternalIp());
                if (deviceInterface.getExternalIp() != null && deviceInterface.getExternalIp().length() > 0 && !deviceInterface.getExternalIp().toLowerCase().contains("null")) {
//                    logger.info("DEFAULT GATWE" + deviceInterface.get);
                    defaultGateway = deviceInterface.getIfName();
                    break;
                }
            }
        }

        if (defaultGateway != null) {

            return defaultGateway;
        }

        // sau do moi xet den cac wan ipoe, tach ra 2 vong for do ko the sure duoc order cua cac wan pppoe va ipoe trong list deviceInterface
        for (DeviceInterface deviceInterface : ig.getDeviceInterfaces()) {

            if ("4000".equalsIgnoreCase(deviceInterface.getVlanId())
                    || "441".equalsIgnoreCase(deviceInterface.getVlanId())
                    || "400".equalsIgnoreCase(deviceInterface.getVlanId())) {

                continue;
            }

            if (deviceInterface.getIfReference().contains("WANIPConnection")) {
                if (deviceInterface.getExternalIp() != null && deviceInterface.getExternalIp().length() > 0
                        && "IP_Routed".equalsIgnoreCase(deviceInterface.getConnectionType())) {
                    defaultGateway = deviceInterface.getIfName();
                    break;
                }
            }
        }

        return defaultGateway;
    }

    public static void configDefautGateway(Device device, String defaultGateway) throws Exception {

        if (defaultGateway == null) {
            throw new Exception("default gateway has no value");
        }
        System.out.println("defaultgateWayt " + defaultGateway);
        logger.info("configDefautGateway: " + defaultGateway + ", sn= " + device.getSerialNumber());
        ArrayList<SimpleObject> data = new ArrayList<SimpleObject>(2);
        SimpleObject gatewayObject = new SimpleObject(DEFAULT_GATEWAY_PARAM, null, defaultGateway, "xsd:string");
        SimpleObject dnsObject = new SimpleObject(DEFAULT_DNS_PARAM, null, defaultGateway, "xsd:string");
        data.add(gatewayObject);
        data.add(dnsObject);
        // cap nhat trang thai log
//        UpdateFirmwareLog ufl = DatabaseManager.UFL_DAO.getLogBySN(device.getAdslSerialNumber());
        // thuc hien set lai default gateway
        SetValueCommand cmand = new SetValueCommand(device.getSerialNumber(), data);
        cmand.setConnectionRequestURL(device.getConnectionReq());
        boolean hasError = cmand.executeCommand();

        if (!hasError) {
            logger.error("set default gateway fail: " + cmand.getErrorString());
            throw new Exception(cmand.getErrorString());
        }
        // thuc hien reboot lai thiet bi
        RebootCommand rebootCmd = new RebootCommand(device.getSerialNumber());
        rebootCmd.setConnectionRequestURL(device.getConnectionReq());
        // set them note de sau khi reboot thiet bi khong thuc hine laij set gatewayt nua
        rebootCmd.setNote(NOTE);
        boolean rebootHasError = rebootCmd.executeCommand();

        if (!rebootHasError) {
            logger.error("reboot cpe fail: " + rebootCmd.getErrorString());
            throw new Exception(rebootCmd.getErrorString());

        }

    }
}
