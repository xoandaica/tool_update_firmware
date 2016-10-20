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
import net.vnpttech.collection.jms.JMSRequestUpdateFirmware;
import net.vnpttech.collection.openacs.AlarmData;
import net.vnpttech.collection.openacs.Message;
import net.vnpttech.collection.openacs.common.AppConfig;
import static net.vnpttech.collection.openacs.database.DatabaseManager.ADSL_DEVICE_DAO;
import net.vnpttech.collection.openacs.message.AddObject;
import net.vnpttech.collection.openacs.message.Download;
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
import net.vnpttech.collection.openacs.tree.DataFileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.appfuse.model.AdslDevice;

/**
 *
 * @author Windows 7
 */
public class ADSLHandler extends BasicHandler {

    private static Logger logger = Logger.getLogger(AONHandler.class.getName());

    @Override
    public String getHandlerName() {
        return FactoryHandler.PRODUCT_TYPE_ADSL;
    }

    @Override
    public void processGetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        StringBuilder sb = new StringBuilder(100);
        GetParameterValuesResponse dataReceive = (GetParameterValuesResponse) msg;
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);
        if (TR069StaticParameter.EVENT_0.equals(lastEvent) || TR069StaticParameter.EVENT_1.equals(lastEvent)) {
            System.out.println("SAVED..........");
            saveDeviceInfo(session, dataReceive, lastInform);

        } else if (TR069StaticParameter.EVENT_2.equals(lastEvent)) { // periodic session

            Command cm = CommandConfigurationFactory.getCommand(lastInform.sn);
            if (cm == null) {
                return;
            }
            logger.info("GetParameterValuesResponse with CommandType: " + cm.getType());
            if (cm.getType().equals(Command.TYPE_PERIODIC)) {

                enqueueUpdateFirmware(lastInform);

                PeriodicCommand periodicCmd = (PeriodicCommand) cm;
                HashMap hm_data = new HashMap(dataReceive.values);
                String KeyCheck = "InternetGatewayDevice.Layer2Bridging.AvailableInterface.";
                String ValueCheck = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
                List<String> ListWanGet = new ArrayList<String>(16);
                if (periodicCmd.orderCmd == 0) {
                    //periodicCmd.orderCmd = 1;
                    periodicCmd.orderCmd = 3;
                    //detect version
                    String ontModelName = dataReceive.values.get(TR069StaticParameter.DeviceInfoModelName);
                    // list chua cac tham so tr069 can lay
                    // performance cua lan + wire lan + wan + optical
                    List<String> listParams = new ArrayList<String>(150);

                    // lay danh sach cac wan interface 
                    for (Object variableName : hm_data.keySet()) {
                        String Key = variableName.toString();
                        String Value = hm_data.get(variableName).toString();

                        if (Value.contains(ValueCheck) && Key.contains(KeyCheck)) {
                            ListWanGet.add(Value);
                        }
                    }
                    //
                    //Kiem tra trong file GET_WANPERFORMANCE lay ra tat ca cac gia tri
                    DataFileModel wanDataFileModel = DataFileModel.createDataFileModel(FilePath.GET_WANPERFORMANCE, cm.getModelName());
                    List<SimpleObject> WanData = wanDataFileModel.getDataFile();
                    //Tong hop cac gia tri va cho vao ListWanFull
                    for (String strWanItem_First : ListWanGet) {
                        for (SimpleObject WanData1 : WanData) {
                            String strWanItem_End = WanData1.getParameter(); // posfix of wan parameter
                            String strWanFull = strWanItem_First + "." + strWanItem_End; // full path wan parameter of specific wan interface
                            listParams.add(strWanFull);
                        }
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
//                        AdslDevice deviceInfo = new AdslDevice();
//                        deviceInfo.setAdslSerialNumber(lastInform.sn);
//                        deviceInfo.setSerialNumber(lastInform.sn);
//                        deviceInfo.setManufacturer(lastInform.Manufacturer);
//                        deviceInfo.setOui(lastInform.getOui());
//                        deviceInfo.setProductClass(lastInform.ProductClass);
//                        deviceInfo.setHardwareVersion(lastInform.getHardwareVersion());
//                        deviceInfo.setSoftwareVersion(lastInform.getSoftwareVersion());
//                        deviceInfo.setProvisioningCode(lastInform.getProvisiongCode());
//                        deviceInfo.setConnectionRequest(lastInform.getConnectionRequestURL());
//                        String ipAddress = DataFileUtils.getIPfromConnectionRequest(lastInform.getConnectionRequestURL());
//                        deviceInfo.setIpAddress(ipAddress);
//                        deviceInfo.setName(lastInform.sn);
//                        deviceInfo.setShortName(lastInform.sn);
//
//                        Global.enqueueUpdateFw(deviceInfo);
//                    }
                }
            } else if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) {

                ZeroTouchCommand_GPON zeroCmd = (ZeroTouchCommand_GPON) cm;

                if (zeroCmd.orderCmd == 0) {
                    // Get device info (pre-step)
                    // DONOT: Increase OrderCmd
                    AdslDevice device = ADSL_DEVICE_DAO.getByAdslSerialNumber(lastInform.sn);
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
                        ADSL_DEVICE_DAO.save(device);
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

}
