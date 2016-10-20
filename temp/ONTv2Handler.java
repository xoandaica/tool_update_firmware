/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpt.tr069.handler;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.vnpt.haimv.Global.Global;
import static net.vnpt.tr069.handler.BasicHandler.ATTR_LASTINFORM;
import static net.vnpt.tr069.handler.BasicHandler.ATTR_LAST_EVENT;
import net.vnpttech.collection.openacs.AlarmData;
import net.vnpttech.collection.openacs.Message;
import net.vnpttech.collection.openacs.common.AppConfig;
import net.vnpttech.collection.openacs.common.Properties;
import net.vnpttech.collection.openacs.database.DatabaseManager;
import net.vnpttech.collection.openacs.message.AddObject;
import net.vnpttech.collection.openacs.message.GetParameterValues;
import net.vnpttech.collection.openacs.message.GetParameterValuesResponse;
import net.vnpttech.collection.openacs.message.Inform;
import net.vnpttech.collection.openacs.message.SetParameterValues;
import net.vnpttech.collection.openacs.mycommand.AddObjectCommand;
import net.vnpttech.collection.openacs.mycommand.Command;
import net.vnpttech.collection.openacs.mycommand.CommandConfigurationFactory;
import net.vnpttech.collection.openacs.mycommand.CommandRequestFactory;
import net.vnpttech.collection.openacs.mycommand.FilePath;
import net.vnpttech.collection.openacs.mycommand.GetValueCommand;
import net.vnpttech.collection.openacs.mycommand.PeriodicCommand;
import net.vnpttech.collection.openacs.mycommand.TR069StaticParameter;
import net.vnpttech.collection.openacs.mycommand.UpdateFirmwareCommand;
import net.vnpttech.collection.openacs.mycommand.ZeroTouchCommand_GPON;
import net.vnpttech.collection.openacs.myobject.DataFileModel;
import net.vnpttech.collection.openacs.myobject.SimpleObject;
import net.vnpttech.collection.openacs.utils.NMSStringUtils;
import net.vnpttech.hni.BridgeList;
import net.vnpttech.hni.ConfigDeviceRunnable;
import net.vnpttech.hni.IPOEList;
import net.vnpttech.hni.PPPOEList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.appfuse.model.AdslDevice;
import org.appfuse.model.HNIConfig;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Zan
 */
public class ONTv2Handler extends BasicHandler {

    private static final Logger logger = Logger.getLogger(ONTv2Handler.class.getName());

    @Override
    public String getHandlerName() {
        return FactoryHandler.PRODUCT_TYPE_ONTV2;
    }

    @Override
    public void processGetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        StringBuilder sb = new StringBuilder(100);
        GetParameterValuesResponse dataReceive = (GetParameterValuesResponse) msg;
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        if (TR069StaticParameter.EVENT_0.equals(lastEvent)) {

            String stepOrder = (String) session.getAttribute(ATTR_STEP_ORDER);
            if (stepOrder == null) {

                saveDeviceInfo(session, dataReceive, lastInform);

                List<String> listParams = new ArrayList<String>(1);
                listParams.add(TR069StaticParameter.GPON_INFO_MEN_NUMBER);
                session.setAttribute(ATTR_STEP_ORDER, "getMenNumber");
                GetParameterValues getParaVl = new GetParameterValues(listParams);
                getParaVl.writeTo(out);

            } else {

                updateCPEInfo(session, dataReceive, lastInform);
                startConfigDevice(session, dataReceive, lastInform);
            }

        } else if (TR069StaticParameter.EVENT_1.equals(lastEvent)) {

            String stepOrder = (String) session.getAttribute(ATTR_STEP_ORDER);
            if (stepOrder == null) {

                saveDeviceInfo(session, dataReceive, lastInform);

                List<String> listParams = new ArrayList<String>(1);
                listParams.add(TR069StaticParameter.GPON_INFO_MEN_NUMBER);
                session.setAttribute(ATTR_STEP_ORDER, "getMenNumber");
                GetParameterValues getParaVl = new GetParameterValues(listParams);
                getParaVl.writeTo(out);

            } else {

                updateCPEInfo(session, dataReceive, lastInform);
            }
        } else if (TR069StaticParameter.EVENT_2.equals(lastEvent)) { // periodic session

            Command cm = CommandConfigurationFactory.getCommand(lastInform.sn);
            if (cm == null) {
                return;
            }

            logger.info("GetParameterValuesResponse with CommandType: " + cm.getType());
            if (cm.getType().equals(Command.TYPE_PERIODIC)) {

                enqueueUpdateFirmware(lastInform);

                PeriodicCommand periodicCmd = (PeriodicCommand) cm;

                HashMap<String, String> hm_data = new HashMap(dataReceive.values);
                String KeyCheck = "InternetGatewayDevice.Layer2Bridging.AvailableInterface.";

                String ValueCheck_Wan = "InternetGatewayDevice.WANDevice.5.WANConnectionDevice.";
                List<String> ListWanGet = new ArrayList<String>(16);

                String ValueCheck_Lan = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.";
                List<String> ListLanGet = new ArrayList<String>(10);

                String ValueCheck_Wlan = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.";
                List<String> ListWlanGet = new ArrayList<String>(10);

                if (periodicCmd.orderCmd == 0) {
                    //periodicCmd.orderCmd = 1;
                    periodicCmd.orderCmd = 3;
                    //detect version
                    String ontModelName = dataReceive.values.get(TR069StaticParameter.DeviceInfoModelName);
                    // list chua cac tham so tr069 can lay
                    // performance cua lan + wire lan + wan + optical
                    List<String> listParams = new ArrayList<String>(150);

                    // lay danh sach cac wan interface 
                    for (Map.Entry<String, String> entry : hm_data.entrySet()) {
                        String Key = entry.getKey();
                        String Value = entry.getValue();
                        //Wan
                        if (Value.contains(ValueCheck_Wan) && Key.contains(KeyCheck)) {
                            ListWanGet.add(Value);
                        }
                        //Lan
                        if (Value.contains(ValueCheck_Lan) && Key.contains(KeyCheck)) {
                            ListLanGet.add(Value);
                        }
                        //Wlan
                        if (Value.contains(ValueCheck_Wlan) && Key.contains(KeyCheck)) {
                            ListWlanGet.add(Value);
                        }
                    }
                    //
                    List<String> listFullParam = getFullParam(ListLanGet, ListWlanGet, ListWanGet);
                    for (String strValue : listFullParam) {
                        listParams.add(strValue);
                    }

                    cm.setModelName(ontModelName);
                    //add lan performance
                    periodicCmd.addDataToRoot(dataReceive.values);
                    // get Performance
                    DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.Get_DevicePerformance, cm.getModelName());
                    ArrayList<SimpleObject> listDevicePerformance = dataModel.getDataModel();
                    for (SimpleObject item : listDevicePerformance) {
                        listParams.add(item.getParameter());
                    }

                    //get wlan performance
                    if (listParams.size() > 0) {
                        logger.info("Get_DevicePerformance: " + listParams.size());
                        GetParameterValues getParaVl = new GetParameterValues(listParams);
                        getParaVl.writeTo(out);
                    } else {
                        logger.warn("Get_DevicePerformance: Not run");
                    }

                } else if (periodicCmd.orderCmd == 1) {
                    //periodicCmd.orderCmd = 2;
                    // DSL Performance not belong to GPON Device! --> Skip step 2
                    periodicCmd.orderCmd = 3;
                    //add wlan performance
                    periodicCmd.addDataToRoot(dataReceive.values);
                    // checkWifi is enabled ?
                    String dataTree = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.Enable";
                    String wifiIsEnabled = dataReceive.values.get(dataTree);
                    periodicCmd.getKeyMap().put(UpdateFirmwareCommand.UPDATE_FW_WIFI_STATE, wifiIsEnabled);
                    //get wan performance & opticalInfo
                    //String treeIndex = AppConfig.isDEV_V2(cm.getModelName()) ? "5" : "3";
                    DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.GET_WAN_SERVICE, cm.getModelName());
                    SimpleObject root = dataModel.getRoot();

                    String wanPerformancePath = StringUtils.stripEnd(root.getParameter(), ".") + ".";

                    sb.setLength(0);
                    sb.append("PeriodicCommand: DeviceModelName=")
                            .append(cm.getModelName())
                            .append(", SerialNumber=")
                            .append(lastInform.sn)
                            .append(", wanPerformancePath=")
                            .append(wanPerformancePath);
                    logger.info(sb);

                    Map<String, String> listParams = new HashMap<String, String>();
                    if (AppConfig.isDEV_V2(cm.getModelName())) {
                        // only get OpticalInfo in V2 that supported
                        logger.info(cm.getType() + ": ONT V2 is supported !");
                        listParams.put(TR069StaticParameter.OpticalInfo, "OpticalInfo");
                    }
                    listParams.put(wanPerformancePath, "wanPerformancePath");
                    GetParameterValues getParaVl = new GetParameterValues(listParams);
                    getParaVl.writeTo(out);
                } else if (periodicCmd.orderCmd == 2) {
                    periodicCmd.orderCmd = 3;
                    //add wan performance
                    periodicCmd.addDataToRoot(dataReceive.values);
                    //get dsl performance
                    GetParameterValues getParaVl = new GetParameterValues(TR069StaticParameter.DslPerformancePath);
                    getParaVl.writeTo(out);
                } else if (periodicCmd.orderCmd == 3) {
                    periodicCmd.orderCmd = 4;
                    //add dsl performance
                    try {
                        ///hoangtuan check update alarm
//                                        long beforeEnque = System.currentTimeMillis();
                        HashMap hm_datasend = new HashMap(dataReceive.values);
                        AlarmData alarm = new AlarmData();
                        alarm.hashdata = hm_datasend;
                        alarm.serial = lastInform.sn;
                        Global.enqueueAlarm(alarm);

                    } catch (Exception e) {
                        logger.error("error insert data performance to database", e);
                    }

                    // hoangtuan: bo update Firmware chuyen sang jms + firmware Policy
                    // Neu co yeu cau Update Firmware trong 
                    // hoangtuan check 
//                    Command cmUpdate = JMSRequestUpdateFirmware.getUpdateFirmwareRequest().remove(lastInform.sn);
//                    if (cmUpdate != null) {
//                        UpdateFirmwareCommand updateFirmwareInfo = (UpdateFirmwareCommand) cmUpdate;
//                        String urlFileFirmware = updateFirmwareInfo.getUrlFileServer();
//                        // Send message command
//                        Download recm = new Download("", urlFileFirmware, Download.FT_FIRMWARE);
//                        recm.writeTo(out);
//
//                        // Remove from request
//                    } else {
//                    }
                }
            } else if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) {

                ZeroTouchCommand_GPON zeroCmd = (ZeroTouchCommand_GPON) cm;

                if (zeroCmd.orderCmd == 0) {
                    // Get device info (pre-step)
                    // DONOT: Increase OrderCmd
                    AdslDevice device = DatabaseManager.ADSL_DEVICE_DAO.getByAdslSerialNumber(lastInform.sn);
                    String ontModelName = dataReceive.values.get(TR069StaticParameter.DeviceInfoModelName);
                    cm.setModelName(ontModelName);

                    if (device != null) {
                        int ontVersion = AppConfig.getDeviceVersion(ontModelName);
                        sb.setLength(0);
                        sb.append("ZeroTouch Update Device Version: ONT_VERSION=")
                                .append(ontVersion)
                                .append(", MODEL_NAME=")
                                .append(ontModelName);
                        logger.info(sb);
                        device.setModelName(ontModelName);
                        device.setDeviceVersion(ontVersion);
                        DatabaseManager.ADSL_DEVICE_DAO.save(device);
                    }

                    // Config PeriodicInformEnable
                    String intervalInform = zeroCmd.getIntervalInform();
                    SetParameterValues recm = new SetParameterValues();
                    recm.AddValue(TR069StaticParameter.PeriodicInformEnable, "1", "xsd:boolean");
                    recm.AddValue(TR069StaticParameter.PeriodicInformInterval, intervalInform, "xsd:unsignedInt");
                    recm.writeTo(out);
                } else if (zeroCmd.orderCmd == 1) {
                    //zeroCmd.orderCmd = 2; //--> not create Layer 2 --> use default instance 1
                    zeroCmd.orderCmd = 4;
                    //add ATM object
                    try {
                        zeroCmd.setRootDevice(dataReceive.values);
//                                        zeroCmd.setACSServer(dataReceive.getParam(TR069StaticParameter.ManagementURL));
                    } catch (Exception e) {
                        logger.error("ZeroTouch, Step 2: " + e.getMessage(), e);
                    }
                    //AddObject recm = new AddObject(zeroCmd.getRootNameLAN() + ".", "");
                    //recm.writeTo(out);
                    sb.setLength(0);
                    sb.append(cm.getType())
                            .append(", create WANPPPConnection")
                            .append(", serialNumber=")
                            .append(lastInform.sn)
                            .append(", modelName=")
                            .append(cm.getModelName());
                    logger.info(sb);
                    AddObject recm = new AddObject(zeroCmd.getRootNameLAN() + ".1." + "WANPPPConnection.", "");
                    recm.writeTo(out);
                } else if (zeroCmd.orderCmd == 6) {
                    zeroCmd.orderCmd = 7;
                    try {
//                                        zeroCmd.setACSServer(dataReceive.getParam(TR069StaticParameter.ManagementURL));
                        zeroCmd.setACSManagementServer(dataReceive.values);
                    } catch (Exception e) {
                        logger.error("ZeroTouch, Step 7: " + e.getMessage(), e);
                    }
                    //add static route
                    // Vunb 2015-03-04: Remove static route --> add only default gateway
                    AddObject recm = new AddObject(zeroCmd.rootNameForwarding + ".", "");
                    recm.writeTo(out);
                }
            } //

        } else if (TR069StaticParameter.EVENT_6.equals(lastEvent)) { // connection request sessionF

            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
            if (cm != null) {
                logger.info("GetParameterValuesResponse with CommandType: " + cm.getType());

                if (cm.getType().equals(Command.TYPE_GETTREEVALUE)) {

                    GetValueCommand getVlCmd = (GetValueCommand) cm;
                    getVlCmd.setReturnValue(dataReceive.values);
                    getVlCmd.receiveResult();
                } else if (cm.getType().equals(Command.TYPE_ADDOBJECT)) {

                    AddObjectCommand addCmd = (AddObjectCommand) cm;
                    addCmd.ifnameObject.setValue(dataReceive.getParam(addCmd.ifnameObject.getParameter()));
                    addCmd.receiveResult();
                }
            }
        }

    }

    private List<String> getFullParam(List<String> listLan, List<String> listWlan, List<String> listWan) {
        ///Varible Wan
        String wanIfName = "X_BROADCOM_COM_IfName";
        String wanDescription = "Name";
        String wanReceivedBytes = "Stats.EthernetBytesReceived";
        String wanReceivedPkts = "Stats.EthernetPacketsReceived";
        String wanReceivedErrs = "Stats.X_BROADCOM_COM_RxErrors";
        String wanReceivedDrops = "Stats.X_BROADCOM_COM_RxDrops";
        String wanTransmittedBytes = "Stats.EthernetBytesSent";
        String wanTransmittedPkts = "Stats.EthernetPacketsSent";
        String wanTransmittedErrs = "Stats.X_BROADCOM_COM_TxErrors";
        String wanTransmittedDrops = "Stats.X_BROADCOM_COM_TxDrops";
        //Varible Lan
        String lanIfName = "X_BROADCOM_COM_IfName";
        String lanReceivedBytes = "Stats.BytesReceived";
        String lanReceivedPkts = "Stats.PacketsReceived";
        String lanReceivedErrs = "Stats.X_BROADCOM_COM_RxErrors";
        String lanReceivedDrops = "Stats.X_BROADCOM_COM_RxDrops";
        String lanTransmittedBytes = "Stats.BytesSent";
        String lanTransmittedPkts = "Stats.X_BROADCOM_COM_PacketsSent";
        String lanTransmittedErrs = "Stats.X_BROADCOM_COM_TxErrors";
        String lanTransmittedDrops = "Stats.X_BROADCOM_COM_TxDrops";
        //Varible Wlan
        String wlanIfName = "WlIfcname";
        String wlanReceivedBytes = "Stats.TotalBytesReceived";
        String wlanReceivedPkts = "Stats.TotalPacketsReceived";
        String wlanReceivedErrs = "Stats.X_BROADCOM_COM_RxErrors";
        String wlanReceivedDrops = "Stats.X_BROADCOM_COM_RxDrops";
        String wlanTransmittedBytes = "Stats.TotalBytesSent";
        String wlanTransmittedPkts = "Stats.TotalPacketsSent";
        String wlanTransmittedErrs = "Stats.X_BROADCOM_COM_TxErrors";
        String wlanTransmittedDrops = "Stats.X_BROADCOM_COM_TxDrops";

        List<String> listReturn = new ArrayList<String>();
        //listLan
        for (int i = 0; i < listLan.size(); i++) {
            String strValue = listLan.get(i);
            //
            listReturn.add(strValue + "." + lanIfName);
            listReturn.add(strValue + "." + lanReceivedBytes);
            listReturn.add(strValue + "." + lanReceivedPkts);
            listReturn.add(strValue + "." + lanReceivedErrs);
            listReturn.add(strValue + "." + lanReceivedDrops);
            listReturn.add(strValue + "." + lanTransmittedBytes);
            listReturn.add(strValue + "." + lanTransmittedPkts);
            listReturn.add(strValue + "." + lanTransmittedErrs);
            listReturn.add(strValue + "." + lanTransmittedDrops);
        }
        //listWan
        for (int i = 0; i < listWan.size(); i++) {
            String strValue = listWan.get(i);
            //
            listReturn.add(strValue + "." + wanIfName);
            listReturn.add(strValue + "." + wanDescription);
            listReturn.add(strValue + "." + wanReceivedBytes);
            listReturn.add(strValue + "." + wanReceivedPkts);
            listReturn.add(strValue + "." + wanReceivedErrs);
            listReturn.add(strValue + "." + wanReceivedDrops);
            listReturn.add(strValue + "." + wanTransmittedBytes);
            listReturn.add(strValue + "." + wanTransmittedPkts);
            listReturn.add(strValue + "." + wanTransmittedErrs);
            listReturn.add(strValue + "." + wanTransmittedDrops);
        }
        //listWlan
        for (int i = 0; i < listWlan.size(); i++) {
            String strValue = listWlan.get(i);
            //
            listReturn.add(strValue + "." + wlanIfName);
            listReturn.add(strValue + "." + wlanReceivedBytes);
            listReturn.add(strValue + "." + wlanReceivedPkts);
            listReturn.add(strValue + "." + wlanReceivedErrs);
            listReturn.add(strValue + "." + wlanReceivedDrops);
            listReturn.add(strValue + "." + wlanTransmittedBytes);
            listReturn.add(strValue + "." + wlanTransmittedPkts);
            listReturn.add(strValue + "." + wlanTransmittedErrs);
            listReturn.add(strValue + "." + wlanTransmittedDrops);
        }
        return listReturn;
    }

    /**
     * save thong tin device va thuc hien config thiet bi
     *
     * @param session
     * @param dataReceive
     * @param lastInform
     */
    private void updateCPEInfo(HttpSession session, GetParameterValuesResponse dataReceive, Inform lastInform) {
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        // need update cpe info
        // men numbe la duy nhat nen update phai dac biet
        String menID = dataReceive.values.get(TR069StaticParameter.GPON_INFO_MEN_NUMBER);
        if (menID != null) {

            String menAscii = null;

            if (menID.length() > Properties.getMaxSLIDAsciiLength()) { // hex format

                menAscii = NMSStringUtils.hexToASCII(menID).trim();

            } else { // ascii format

                menAscii = menID.trim();
            }

            StringBuilder sb = new StringBuilder("update CPE: ").append(lastInform.sn);
            sb.append(" with MEN (raw data) number: ").append(menID).append(" (ascii mode) ").append(menAscii);
            logger.info(sb.toString());
            
            DatabaseManager.ADSL_DEVICE_DAO.updateMenNumber(lastInform.sn, menAscii);
        } else {
            logger.warn("cpe MEN number is empty: " + lastInform.sn);
        }

    }

    private void startConfigDevice(HttpSession session, GetParameterValuesResponse dataReceive, Inform lastInform) {
        long startTime = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        AdslDevice device = DatabaseManager.ADSL_DEVICE_DAO.getByAdslSerialNumber(lastInform.sn);
        if (device == null) {
            logger.warn("can not get device with serial: " + lastInform.sn);
            return;
        }

        String wanConfig = null;
        if (device.getWanConfig() == null || device.getWanConfig().length() == 0) {
            // truong hop thiet bi chua co config, day la lan dau join vao he thong
            // read config from hni_config table

//            String menID = dataReceive.values.get(TR069StaticParameter.GPON_INFO_MEN_NUMBER);
            String menAscii = device.getMenID();
            if (menAscii != null) {

                HNIConfig config = DatabaseManager.HNI_CONFIG_DAO.get(menAscii);
                if (config != null) {
                    wanConfig = config.getWanConfig();
                    // move config and command id from table hni_config to device
                    device.setCommandID(config.getCommandID());
                    device.setWanConfig(wanConfig);
                    DatabaseManager.ADSL_DEVICE_DAO.save(device);
                    DatabaseManager.HNI_CONFIG_DAO.remove(menAscii);
                }
            } else {
                logger.info("cpe MEN number is empty: " + lastInform.sn);
            }
        } else {
            // truong hop thiet bi da co tren he thong
            wanConfig = device.getWanConfig();
        }

        System.out.println("ConfigDeviceBoostrap: " + (System.currentTimeMillis() - startTime));

        if (wanConfig == null || wanConfig.length() == 0) {
            logger.warn("device config is null, cpe: " + lastInform.sn);
            return;
        }

        if (logger.isDebugEnabled()) {
            sb.setLength(0);
            sb.append("config of device: ").append(lastInform.sn).append(wanConfig);
            logger.debug(sb.toString());
        }

        // parse config va luu vao runnable
        JSONParser parser = new JSONParser();
        JSONObject json;
        try {
            json = (JSONObject) parser.parse(wanConfig);
            BridgeList bridgeList = BridgeList.parseFromJson(json);
            IPOEList ipoeList = IPOEList.parseIPOEList(json);
            PPPOEList pppoeList = PPPOEList.parsePPPOEList(json);
            ConfigDeviceRunnable task = new ConfigDeviceRunnable();
            task.bridgeList = bridgeList;
            task.iPOEList = ipoeList;
            task.pppoeList = pppoeList;
            task.device = device;
            ConfigDeviceRunnable.submit(task);
            logger.info("startConfigDevice starting..." + lastInform.sn);
        } catch (Exception ex) {
            sb.setLength(0);
            sb = new StringBuilder("can not parse cpe: ").append(lastInform.sn);
            sb.append(" with config number: ").append(wanConfig);
            logger.error(sb.toString(), ex);
        }
    }

}
