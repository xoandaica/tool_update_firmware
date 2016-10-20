/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpt.tr069.handler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import net.jmx.run.DataDbManager;
import net.vnpt.haimv.Global.Global;
import net.vnpttech.collection.openacs.Message;
import net.vnpttech.collection.openacs.common.Properties;
import net.vnpttech.collection.openacs.database.DatabaseManager;
import net.vnpttech.collection.openacs.message.AddObject;
import net.vnpttech.collection.openacs.message.AddObjectResponse;
import net.vnpttech.collection.openacs.message.DeleteObject;
import net.vnpttech.collection.openacs.message.DeleteObjectResponse;
import net.vnpttech.collection.openacs.message.Download;
import net.vnpttech.collection.openacs.message.DownloadResponse;
import net.vnpttech.collection.openacs.message.FactoryReset;
import net.vnpttech.collection.openacs.message.GetParameterNames;
import net.vnpttech.collection.openacs.message.GetParameterNamesResponse;
import net.vnpttech.collection.openacs.message.GetParameterValues;
import net.vnpttech.collection.openacs.message.GetParameterValuesResponse;
import net.vnpttech.collection.openacs.message.GetRPCMethods;
import net.vnpttech.collection.openacs.message.GetRPCMethodsResponse;
import net.vnpttech.collection.openacs.message.Inform;
import net.vnpttech.collection.openacs.message.Reboot;
import net.vnpttech.collection.openacs.message.ScheduleInform;
import net.vnpttech.collection.openacs.message.SetParameterValues;
import net.vnpttech.collection.openacs.message.SetParameterValuesResponse;
import net.vnpttech.collection.openacs.message.TransferComplete;
import net.vnpttech.collection.openacs.message.TransferCompleteResponse;
import net.vnpttech.collection.openacs.message.Upload;
import net.vnpttech.collection.openacs.mycommand.AddObjectCommand;
import net.vnpttech.collection.openacs.mycommand.BackupCommand;
import net.vnpttech.collection.openacs.mycommand.Command;
import net.vnpttech.collection.openacs.mycommand.CommandConfigurationFactory;
import net.vnpttech.collection.openacs.mycommand.CommandRequestFactory;
import net.vnpttech.collection.openacs.mycommand.DeleteObjectCommand;
import net.vnpttech.collection.openacs.mycommand.FilePath;
import net.vnpttech.collection.openacs.mycommand.GetParameterNamesCommand;
import net.vnpttech.collection.openacs.mycommand.GetValueCommand;
import net.vnpttech.collection.openacs.mycommand.PeriodicCommand;
import net.vnpttech.collection.openacs.mycommand.PingDiagnosticsCommand;
import net.vnpttech.collection.openacs.mycommand.RebootCommand;
import net.vnpttech.collection.openacs.mycommand.ScheduleInformCommand;
import net.vnpttech.collection.openacs.mycommand.SetRestoreCommand;
import net.vnpttech.collection.openacs.mycommand.SetValueCommand;
import net.vnpttech.collection.openacs.mycommand.TR069StaticParameter;
import net.vnpttech.collection.openacs.mycommand.TracertDiagnosticsCommand;
import net.vnpttech.collection.openacs.mycommand.UpdateFirmwareBatchCommand;
import net.vnpttech.collection.openacs.mycommand.UpdateFirmwareCommand;
import net.vnpttech.collection.openacs.mycommand.ZeroTouchCommand_GPON;
import net.vnpttech.collection.openacs.myobject.DataFileModel;
import net.vnpttech.collection.openacs.myobject.SimpleObject;
import net.vnpttech.collection.openacs.tree.DataFileUtils;
import net.vnpttech.collection.openacs.utils.Subnet;
import net.vnpttech.wnms.datadb.model.Alarm;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.appfuse.model.AdslAutoConfigParameter;
import org.appfuse.model.AdslDevice;
import org.appfuse.model.AdslFirmware;
import org.appfuse.model.Area;
import org.appfuse.model.AreaDeviceMapping;
import org.appfuse.model.NodeType;

/**
 *
 * @author Zan
 */
public class BasicHandler implements Tr069Handler {

    public static final String ATTR_LASTINFORM = "lastInform";
    public static final String ATTR_LAST_EVENT = "lastEvent";
    public static final String ATTR_CONFIGURATOR = "cfgrun";
    public static final String ATTR_PRODUCT_TYPE = "productType";
    public static final String ATTR_STEP_ORDER = "stepOrder";

    private static Logger logger = Logger.getLogger(BasicHandler.class.getName());

    protected static ThreadLocal<MessageFactory> MSG_FACTORY = new ThreadLocal<MessageFactory>() {
        @Override
        protected MessageFactory initialValue() {
            try {
                return MessageFactory.newInstance();
            } catch (SOAPException ex) {
                return null;
            }
        }
    };

    public BasicHandler() {
    }

    @Override
    public String getHandlerName() {
        return FactoryHandler.PRODUCT_TYPE_BASIC;
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long t0 = System.currentTimeMillis();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        SOAPMessage soapMsg;
        XMlFilterInputStream f = new XMlFilterInputStream(request.getInputStream(), request.getContentLength());

        MessageFactory mf = MSG_FACTORY.get();

        if (mf == null) { // case nay it xay ra
            logger.warn("can not create message factory, server has internal error");
            request.getSession().invalidate();
            return;
        }

        String ct = request.getContentType();
        int csix = (ct != null) ? ct.indexOf("charset=") : -1;
        String csFrom = (csix == -1) ? "ISO-8859-1" : ct.substring(csix + 8).replaceAll("\"", "");
        response.setContentType(ct != null ? ct : "text/xml;charset=UTF-8");
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);

        if (f.next()) { // truong hop ban tin http post co soap 
            try {
                MimeHeaders hdrs = new MimeHeaders();
                hdrs.setHeader("Content-Type", "text/xml; charset=UTF-8");
                InputStream in = (csFrom.equalsIgnoreCase("UTF-8")) ? new XMLFilterNS(f) : new CharsetConverterInputStream(csFrom, "UTF-8", new XMLFilterNS(f));
                soapMsg = mf.createMessage(hdrs, in);

                Message msg = null;
                try {
                    msg = Message.Parse(soapMsg);
                } catch (Exception e) {
                    soapMsg.writeTo(out);
//                    log(lastInform, Level.ERROR, "Parsing failed:\n" + out.toString(), e);
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }

                String reqname = msg.getName();
                logger.info("Request is " + reqname);

                if (reqname.equals("Inform")) {

                    processInform(request, response, msg, out);
                } else if (reqname.equals("TransferComplete")) {

                    processTranferComplete(request, response, msg, out);
                } else if (reqname.equals("DeleteObjectResponse")) {

                    processDelObjectResponse(request, response, msg, out);
                } else if (reqname.equals("DownloadResponse")) {

                    processDownloadResponse(request, response, msg, out);
                } else if ("UploadResponse".equals(msg.getName())) {

                    processUploadResponse(request, response, msg, out);

                } else if (reqname.equals("RebootResponse")) {

                    processRebootResponse(request, response, msg, out);
                } else if (reqname.equals("FactoryResetResponse")) {

                    processFactoryResetResponse(request, response, msg, out);
                } else if (reqname.equals("GetRPCMethods")) {

                    processGetRPCMethods(request, response, msg, out);

                } else if (reqname.equals("GetParameterNamesResponse")) {

                    processGetParameterNamesResponse(request, response, msg, out);

                } else if (reqname.equals("GetParameterValuesResponse")) {

                    processGetParameterValuesResponse(request, response, msg, out);
                } else if (reqname.equals("SetParameterValuesResponse")) {

                    processSetParameterValuesResponse(request, response, msg, out);
                } else if (reqname.equals("AddObjectResponse")) {

                    processAddObjectResponse(request, response, msg, out);
                } else if (reqname.equals("Fault")) {

                    processFault(request, response, msg, out);
                } else {

                    logger.warn("unknown msg type: " + reqname);
                }

            } catch (SOAPException e) {

                log(lastInform, Level.ERROR, "While parsing", e);
            } catch (IllegalArgumentException e) {

                log(lastInform, Level.WARN, "IllegalArgumentException", e);
            } catch (Exception ex) {

                log(lastInform, Level.ERROR, "process message", ex);
            }

        } else // receive post empty, process event (work if have only 1 event)
        {
            processEvent(lastInform, request, response, lastInform, out);
        }

        // send response
        String sout = out.toString();
        sout = sout.replace('\'', '"');
        if (out.size() == 0) { // finish session

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            logger.info("ACS_SENT_Message: SC_NO_CONTENT");
            //hoangtuan
            session.invalidate();
        } else {

            logger.debug("ACS_SENT_Message:\n" + sout);
        }

        response.setContentLength(out.size());
        response.getOutputStream().print(sout);
        logger.info("End of processing in(ms): " + (System.currentTimeMillis() - t0));
    }

    @Override
    public void processInform(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {

        logger.warn("this code never executed");
    }

    @Override
    public void processEvent0Bootstrap(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        logger.info("processEvent0Bootstrap");
        HttpSession session = request.getSession();
        session.setAttribute(ATTR_LAST_EVENT, TR069StaticParameter.EVENT_0);
        if (Properties.isEnableSetInformTime()) {
            setInformTime(out);
        } else {
            //2015-05-21: Support ONT V2
            List<String> listParams = new ArrayList<String>(1);
            listParams.add(TR069StaticParameter.DeviceInfo);
            GetParameterValues getParaVl = new GetParameterValues(listParams);
            getParaVl.writeTo(out);
        }

    }

    @Override
    public void processEvent1Boot(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        session.setAttribute(ATTR_LAST_EVENT, TR069StaticParameter.EVENT_1);
        if (Properties.isEnableSetInformTime()) {
            setInformTime(out);
        } else {
            updateInforInEvent1(lastInform);
        }
    }

    @Override
    public void processEvent2Periodic(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);

        session.setAttribute(ATTR_LAST_EVENT, TR069StaticParameter.EVENT_2);
        StringBuilder sb = new StringBuilder(200);
        // get performance and auto update firmware
        PeriodicCommand periodicCmd = new PeriodicCommand("System", lastInform.sn, lastInform.getConnectionRequestURL());

        sb.append(periodicCmd.getType())
                .append(", SerialNumber=")
                .append(lastInform.sn)
                .append(", connectionRequest=")
                .append(lastInform.getConnectionRequestURL());
        logger.info(sb);
        try {
            periodicCmd.executeCommand();
            //get initValue: LanPerformance + DeviceInfo + OpticalInfo
            List<String> listParams = new ArrayList<String>(2);
            listParams.add(TR069StaticParameter.DeviceInfo);
            //hoangtuan TODO
            // add more wan 19/05/2016
            listParams.add(TR069StaticParameter.WanAvailable);

            GetParameterValues getParaVl = new GetParameterValues(listParams);
            getParaVl.writeTo(out);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public void processEvent4ValueChange(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        session.setAttribute(ATTR_LAST_EVENT, TR069StaticParameter.EVENT_4);
        if (Properties.isEnableSetInformTime()) {
            setInformTime(out);
        } else {
            updateInforInEvent4(lastInform);
        }
    }

    @Override
    public void processEvent6ConnectionRequest(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        session.setAttribute(ATTR_LAST_EVENT, TR069StaticParameter.EVENT_6);
        StringBuilder sb = new StringBuilder(50);
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        System.out.println("Connection Request: EVENT_6 CONNECTION REQUEST");
        if (cm != null) {
            System.out.println("COMMAND_TYPE: " + cm.getType());
            if (cm.getType().equals(Command.TYPE_GETTREEVALUE)) {
                GetValueCommand gtValCmd = (GetValueCommand) cm;
                GetParameterValues recm = new GetParameterValues(gtValCmd.getTreeNode());
                recm.writeTo(out);
            } else if (Command.TYPE_SETVALUE.equals(cm.getType())) {

                //TODO: Create IPPingServiceCommand extends SetValueCommand
                SetValueCommand setCmd = (SetValueCommand) cm;

                SetParameterValues recm = new SetParameterValues();
                for (int i = 0; i < setCmd.getListObj().size(); i++) {
                    recm.AddValue(setCmd.getListObj().get(i).getParameter(), setCmd.getListObj().get(i).getValue(), setCmd.getListObj().get(i).getType());
                }
                recm.writeTo(out);

            } else if (Command.TYPE_SET_IP_PING_DIAGNOSTICS.equals(cm.getType())) {

                PingDiagnosticsCommand pingcm = (PingDiagnosticsCommand) cm;

                SetParameterValues recm = new SetParameterValues();
                for (int i = 0; i < pingcm.getListObj().size(); i++) {
                    recm.AddValue(pingcm.getListObj().get(i).getParameter(), pingcm.getListObj().get(i).getValue(), pingcm.getListObj().get(i).getType());
                }
                recm.writeTo(out);

            } else if (Command.TYPE_SET_TRACERT_DIAGNOSTICS.equals(cm.getType())) {

                TracertDiagnosticsCommand tracertCm = (TracertDiagnosticsCommand) cm;

                SetParameterValues recm = new SetParameterValues();
                for (int i = 0; i < tracertCm.getListObj().size(); i++) {
                    recm.AddValue(tracertCm.getListObj().get(i).getParameter(), tracertCm.getListObj().get(i).getValue(), tracertCm.getListObj().get(i).getType());
                }
                recm.writeTo(out);
            } else if (cm.getType().equals(Command.TYPE_ADDOBJECT)) {
                AddObjectCommand addCmd = (AddObjectCommand) cm;
                AddObject recm = new AddObject(addCmd.getRootName() + ".", "");
                recm.writeTo(out);
            } else if (cm.getType().equals(Command.TYPE_DELETEOBJECT)) {
                DeleteObjectCommand delCmd = (DeleteObjectCommand) cm;
                DeleteObject recm = new DeleteObject(delCmd.getPath(), "");
                recm.writeTo(out);
            } else if (cm.getType().equals(Command.TYPE_REBOOT)) {
                //RebootCommand rbCmd = (RebootCommand)cm;
                Reboot recm = new Reboot();
                recm.writeTo(out);
            } else if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) { // flow nay chua ro, tam thoi off :(

                //ZeroTouchCommand_GPON command = (ZeroTouchCommand_GPON) cm;
                //Enable Interval & Notify Zero_Touch_Command !!
//                SetParameterValues recm = new SetParameterValues();
//                recm.AddValue(TR069StaticParameter.PeriodicInformEnable, "1", "xsd:boolean");
//                recm.writeTo(out);
            } else if (Command.TYPE_UPGRADE_FW.equals(cm.getType())
                    || Command.TYPE_BATCHUPDATE_FW.equals(cm.getType())) {
                UpdateFirmwareCommand upFWCmd = (UpdateFirmwareCommand) cm;
                Download recm = new Download("", upFWCmd.getUrlFileServer(), Download.FT_FIRMWARE);
                recm.UserName = upFWCmd.getUsernameFileServer();
                recm.Password = upFWCmd.getPasswordFileServer();
                recm.writeTo(out);
            } else if (Command.TYPE_FACTORY_RESET.equals(cm.getType())) {
                // hoangtuan
                // 1. Gui lenh
                // 2. Cho ResetFactoryResonse --> thong bao thanh cong
                FactoryReset reset = new FactoryReset();
                reset.writeTo(out);
            } else if (Command.TYPE_BACKUP.equals(cm.getType())) {
                BackupCommand cmBackup = (BackupCommand) cm;
                Upload msgUpload = new Upload();
//                        msgUpload.CommandKey = cmBackup.getCommandKey();
                msgUpload.CommandKey = "Upload RPC 145318240";
                msgUpload.FileType = cmBackup.getFileType();
                msgUpload.URL = cmBackup.getUrlFileServer();
                msgUpload.Username = cmBackup.getUsernameFileServer();
                msgUpload.Password = cmBackup.getPasswordFileServer();
                msgUpload.writeTo(out);
            } else if (Command.TYPE_RESTORE.equals(cm.getType())) {
                SetRestoreCommand cmRestore = (SetRestoreCommand) cm;
                Download msgUpload = new Download();
//                        msgUpload.CommandKey = cmRestore.getCommandKey();
                msgUpload.CommandKey = "Download RPC 145318240";
                msgUpload.FileType = cmRestore.getFileTypeRestore();
                msgUpload.url = cmRestore.getUrlFileServer();
                msgUpload.UserName = cmRestore.getUsernameFileServer();
                msgUpload.Password = cmRestore.getPasswordFileServer();
                msgUpload.writeTo(out);
            } else if (Command.TYPE_SCHEDULEINFORM.equals(cm.getType())) {
                ScheduleInformCommand scheduleCmd = (ScheduleInformCommand) cm;
                ScheduleInform scheMsg = new ScheduleInform(scheduleCmd.getTimeSchedule(), "schedule");
                scheMsg.writeTo(out);
            } else if (Command.TYPE_GETPARAMETER_NAMES.equals(cm.getType())) {
                GetParameterNamesCommand getParameterNamesCmd = (GetParameterNamesCommand) cm;
                GetParameterNames getParamterNamesMsg = new GetParameterNames(getParameterNamesCmd.getParameterNames(), getParameterNamesCmd.isNextLevel());
                getParamterNamesMsg.writeTo(out);
            }
        } else {
            sb.setLength(0);
            sb.append("Not found command request for: ")
                    .append(lastInform.sn);
            logger.warn(sb.toString());
        }

    }

    @Override
    public void processEvent7TranferComplete(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        session.setAttribute(ATTR_LAST_EVENT, TR069StaticParameter.EVENT_7);
    }

    @Override
    public void processEvent8DiagnosticComplete(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        logger.info(TR069StaticParameter.EVENT_8 + " is done !");
        if (cm != null && Command.TYPE_SET_IP_PING_DIAGNOSTICS.equals(cm.getType())) {
            cm.receiveResult();
        } else if (cm != null && Command.TYPE_SET_TRACERT_DIAGNOSTICS.equals(cm.getType())) {
            cm.receiveResult();
        }
    }

    @Override
    public void processAddObjectResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        StringBuilder sb = new StringBuilder(100);
        AddObjectResponse respon = (AddObjectResponse) msg;
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        if (TR069StaticParameter.EVENT_2.equals(lastEvent)) { // periodic session

            Command cm = CommandConfigurationFactory.getCommand(lastInform.sn);
            if (cm != null) {
                if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) {
                    ZeroTouchCommand_GPON zeroCmd = (ZeroTouchCommand_GPON) cm;
                    if (zeroCmd.orderCmd == 2) {
                        zeroCmd.orderCmd = 4;   // create Layer2LANInterface then PPPoE Wan Service
                        zeroCmd.instance = respon.InstanceNumber;
                        //add WAN object
                        AddObject recm = new AddObject(zeroCmd.getRootNameLAN() + "." + zeroCmd.instance + "." + "WANPPPConnection.", "");
                        recm.writeTo(out);

                    } else if (zeroCmd.orderCmd == 4) {
                        zeroCmd.orderCmd = 5;
                        //zeroCmd.orderCmd = 8;
                        //set value WAN obj
                        zeroCmd.setDataValueWAN(respon.InstanceNumber);
                        SetParameterValues setCmd = new SetParameterValues();
                        for (SimpleObject dataWAN : zeroCmd.dataWAN) {
                            sb.setLength(0);
                            sb.append("serialNumber=").append(lastInform.sn)
                                    .append(", ZeroTouch_WAN Parameters: PARAM=").append(dataWAN.getParameter())
                                    .append(", VALUE=").append(dataWAN.getValue())
                                    .append(", TYPE=").append(dataWAN.getType());
                            logger.info(sb);
                            setCmd.AddValue(dataWAN.getParameter(), dataWAN.getValue(), dataWAN.getType());
                        }
                        // 2015-03-04: add only default gateway in this step
                        ArrayList<SimpleObject> gatewayData = zeroCmd.getDefaultGatewayData();
                        for (SimpleObject gatewayData1 : gatewayData) {
                            setCmd.AddValue(gatewayData1.getParameter(), gatewayData1.getValue(), gatewayData1.getType());
                        }
                        setCmd.writeTo(out);
                    } else if (zeroCmd.orderCmd == 7) {
                        zeroCmd.orderCmd = 8;
                        //set Static route & default dns, GW
                        zeroCmd.setDataStaticRoute(respon.InstanceNumber);
                        SetParameterValues setCmd = new SetParameterValues();
                        for (SimpleObject dataStaticRoute : zeroCmd.dataStaticRoute) {
                            setCmd.AddValue(dataStaticRoute.getParameter(), dataStaticRoute.getValue(), dataStaticRoute.getType());
                        }
                        setCmd.writeTo(out);
                    }
                } //
            }

        } else if (TR069StaticParameter.EVENT_6.equals(lastEvent)) { // connection request session

            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
            if (cm != null) {
                if (cm.getType().equals(Command.TYPE_ADDOBJECT)) {
                    AddObjectCommand addCmd = (AddObjectCommand) cm;
                    if (respon.Status != 0) {
                        addCmd.receiveError("Need ReBoot Device");
                    } else {
                        addCmd.setInstance(respon.InstanceNumber); // cap nhat connection id vao phan set param value
                        CommandRequestFactory.saveCommand(addCmd);
                        //rename list parameter
                        if (addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_LAYER2)) {
                            for (SimpleObject sObj : addCmd.getListObj()) {
                                sObj.setParameter(addCmd.getRootName() + "." + addCmd.getInstance() + "." + sObj.getParameter());
                                if (sObj.getName().equals("IfName")) {
                                    addCmd.ifnameObject = sObj;
                                }
                            }

                        } else if (addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_WANSERVICE)) {
                            for (SimpleObject sObj : addCmd.getListObj()) {
                                sObj.setParameter(addCmd.getRootName() + "." + addCmd.getInstance() + "." + sObj.getParameter());
                                if ("IfName".equals(sObj.getName())) {
                                    // Update Interface Name ?
                                    // sObj.setValue(sObj.getValue() + addCmd.getInstance());
                                    addCmd.ifnameObject = sObj;
                                }
                            }
                        } else if (addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_IPLEASE)) {
                            for (SimpleObject sObj : addCmd.getListObj()) {
                                sObj.setParameter(addCmd.getRootName() + "." + addCmd.getInstance() + "." + sObj.getParameter());
                            }
                        } else if (addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_NOTTHING)) {
                            addCmd.receiveResult();
                            return;
                        } else { // case add bridge interface grouping
                            for (SimpleObject sObj : addCmd.getListObj()) {
                                sObj.setParameter(addCmd.getRootName() + "." + addCmd.getInstance() + "." + sObj.getParameter());
                            }
                        }
                        if (addCmd.getListObj() != null && addCmd.getListObj().size() > 0) {
                            //set Value Parameter Obj
                            SetParameterValues setVlCmd = new SetParameterValues();
                            sb.setLength(0);
                            sb.append("SetParameterValues: ");
                            for (SimpleObject sObj : addCmd.getListObj()) {
                                sb.append("\n").append(sObj.getParameter()).append("_____Value: ").append(sObj.getValue()).append("___");
                                //if (!"IfName".equals(sObj.getName()))
                                {
                                    setVlCmd.AddValue(sObj.getParameter(), sObj.getValue(), sObj.getType());
                                }
                            }
                            logger.info(sb);
                            setVlCmd.writeTo(out);
                        } else if (addCmd.ifnameObject != null
                                && addCmd.ifnameObject.getParameter() != null
                                && addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_LAYER2)
                                || addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_WANSERVICE)) {
                            GetParameterValues getVl = new GetParameterValues(addCmd.ifnameObject.getParameter());
                            getVl.writeTo(out);
                        } else {
                            addCmd.receiveResult();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void processDelObjectResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        DeleteObjectResponse delResPon = (DeleteObjectResponse) msg;
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        if (delResPon.Status == 0) {
            if (cm != null) {
                cm.receiveResult();
            }
        } else if (cm != null) {
            cm.receiveError("Can't delete object, need reboot");
        }

    }

    @Override
    public void processDownloadResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        DownloadResponse downloadResPon = (DownloadResponse) msg;
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);

        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        if (TR069StaticParameter.EVENT_2.equals(lastEvent)) { // periodict session

            Command cm = CommandConfigurationFactory.getCommand(lastInform.sn);
            if (cm != null && cm.getType().equals(Command.TYPE_PERIODIC) && downloadResPon.Status != 1) {
                PeriodicCommand periodicCmd = (PeriodicCommand) cm;
                String wifiIsEnabled = periodicCmd.getKeyMap().get(UpdateFirmwareCommand.UPDATE_FW_WIFI_STATE);
                if ("1".equals(wifiIsEnabled)) {
                    // turn on wifi

                    DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.Set_EnableWireless, cm.getModelName());
                    ArrayList<SimpleObject> dataEnableWifi = dataModel.getDataModel();

                    for (SimpleObject item : dataEnableWifi) {
                        item.setValue("1");
                    }

                    SetParameterValues setVlCmd = new SetParameterValues();
                    for (SimpleObject sObj : dataEnableWifi) {
                        logger.debug(sObj.getParameter() + "_____" + sObj.getValue());
                        setVlCmd.AddValue(sObj.getParameter(), sObj.getValue(), sObj.getType());
                    }
                    periodicCmd.getKeyMap().put(Command.CURRENT_STEP, UpdateFirmwareCommand.UPDATE_FW_TURN_ON_WIFI);
                    periodicCmd.getKeyMap().put(Command.NEXT_STEP, Command.COMPLETE_STEP);
                    setVlCmd.writeTo(out);
                } else {
                    periodicCmd.receiveError("Can't receive download respone");
                }
            }

        } else if (TR069StaticParameter.EVENT_6.equals(lastEvent)) { // connectionr request session
            // do nothing, wait for tranfer complete command

        }
    }

    @Override
    public void processFactoryResetResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        if (cm != null) {
            cm.receiveResult();
        }
    }

    @Override
    public void processFault(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);

        StringBuilder sb = new StringBuilder(100);
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        if (cm != null) {
            cm.receiveError(msg.toString());
            sb.setLength(0);
            sb.append(cm.getType())
                    .append(", ERROR: ")
                    .append(msg.toString());
            logger.error(sb);
        } else {
            sb.setLength(0);
            sb.append("Request error. Message: ")
                    .append(msg.toString());
            logger.error(sb);
        }
    }

    @Override
    public void processGetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        StringBuilder sb = new StringBuilder(100);
        GetParameterValuesResponse dataReceive = (GetParameterValuesResponse) msg;
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        if (TR069StaticParameter.EVENT_6.equals(lastEvent)) { // connection request sessionF

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

    @Override
    public void processRebootResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        if (cm != null) {
            if (cm.getType().equals(Command.TYPE_REBOOT)) {
                RebootCommand rbCmd = (RebootCommand) cm;
                rbCmd.receiveResult();
            }
        }
    }

    @Override
    public void processSetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        StringBuilder sb = new StringBuilder(100);

        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        if (TR069StaticParameter.EVENT_2.equals(lastEvent)) { // periodic session

            Command cm = CommandConfigurationFactory.getCommand(lastInform.sn);
            if (cm != null) {
                if (Command.TYPE_PERIODIC.equals(cm.getType())) {
                    PeriodicCommand periodicCmd = (PeriodicCommand) cm;
                    String stage = periodicCmd.getKeyMap().get(Command.NEXT_STEP);
                    if (UpdateFirmwareCommand.UPDATE_FW_DOWNLOAD_FILE_FIRMWARE.equals(stage)) {
                        AdslFirmware adslfw = DatabaseManager.ADSL_FIRMWARE_DAO.get(periodicCmd.getUpgradeFirmwareID());
                        //update fw command
                        Download recm = new Download("", adslfw.getPathToFirmware(), Download.FT_FIRMWARE);
                        recm.writeTo(out);
                    } else if (Command.COMPLETE_STEP.equals(stage)) {
                        periodicCmd.receiveResult();
                    }

                } else if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) {
                    ZeroTouchCommand_GPON zeroCmd = (ZeroTouchCommand_GPON) cm;
                    if (zeroCmd.orderCmd == 0) {
                        zeroCmd.orderCmd = 1;
                        // Get Infor device
                        // Change branch 1 to 3: WanDevice.3
//                                    String dataTree = "InternetGatewayDevice.ManagementServer.URL"; 
                        String dataTree = zeroCmd.getRootNameLAN() + ".";
                        Map<String, String> mapParams = new HashMap<String, String>();
                        mapParams.put(dataTree, "rootNameLAN");
                        //mapParams.put(TR069StaticParameter.DeviceInfo, "DeviceInfo");
                        GetParameterValues getInforDevice = new GetParameterValues(mapParams);
                        getInforDevice.writeTo(out);
                    } else if (zeroCmd.orderCmd == 3) {
                        zeroCmd.orderCmd = 4;
                        //add WAN object
                        AddObject recm = new AddObject(zeroCmd.getRootNameLAN() + "." + zeroCmd.instance + "." + "WANPPPConnection.", "");
                        recm.writeTo(out);
                    } else if (zeroCmd.orderCmd == 5) {
                        zeroCmd.orderCmd = 6;
                        //get ACS URL + PPPoE WanService gateway
                        String dataTree = "InternetGatewayDevice.ManagementServer.";
                        GetParameterValues getManageURL = new GetParameterValues(dataTree);
                        getManageURL.writeTo(out);
                    } else if (zeroCmd.orderCmd == 8) {
                        // ZeroTouch complete
                        sb.setLength(0);
                        sb.append(cm.getType()).append(", ZeroTouch completed! SN=")
                                .append(lastInform.sn);
                        logger.info(sb);
                        zeroCmd.orderCmd = 9;
                        zeroCmd.receiveResult();
                    }
                }
            }

        } else if (TR069StaticParameter.EVENT_6.equals(lastEvent)) { // connection request session

            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
            if (cm != null) {
                logger.info("Command type: " + cm.getType());
                if (cm.getType().equals(Command.TYPE_ADDOBJECT)) {
                    AddObjectCommand addCmd = (AddObjectCommand) cm;
                    if (addCmd.ifnameObject != null
                            && (addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_LAYER2)
                            || addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_WANSERVICE))) {
                        GetParameterValues getVl = new GetParameterValues(addCmd.ifnameObject.getParameter());
                        getVl.writeTo(out);
                    } else {
                        cm.receiveResult();
                    }
                } else if (Command.TYPE_SET_IP_PING_DIAGNOSTICS.equals(cm.getType())) {
                    // wait event 8
                } else if (Command.TYPE_SET_TRACERT_DIAGNOSTICS.equals(cm.getType())) {
                    // wait event 8
                } else {
                    cm.receiveResult();
                }
            }
        }

        // kiendt - bo sung xu ly set inform time - 02082016 - start 
        if (Properties.isEnableSetInformTime()) {
            if (TR069StaticParameter.EVENT_0.equals(lastEvent)) {
                SetParameterValuesResponse setValuesResponse = (SetParameterValuesResponse) msg;
                if (setValuesResponse.Status == 0) {
                    logger.info("Set Inform time success!!!");
                }
                List<String> listParams = new ArrayList<String>(1);
                listParams.add(TR069StaticParameter.DeviceInfo);
                GetParameterValues getParaVl = new GetParameterValues(listParams);
                getParaVl.writeTo(out);

            } else if (TR069StaticParameter.EVENT_1.equals(lastEvent)) {
                SetParameterValuesResponse setValuesResponse = (SetParameterValuesResponse) msg;
                if (setValuesResponse.Status == 0) {
                    logger.info("Set Inform time success!!!");
                }
                updateInforInEvent1(lastInform);
            } else if (TR069StaticParameter.EVENT_4.equals(lastEvent)) {
                SetParameterValuesResponse setValuesResponse = (SetParameterValuesResponse) msg;
                if (setValuesResponse.Status == 0) {
                    logger.info("Set Inform time success!!!");
                }
                updateInforInEvent4(lastInform);
            }
        }
        // kiendt - bo sung xu ly set inform time - 02082016 - end
    }

    @Override
    public void processTranferComplete(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        StringBuilder sb = new StringBuilder(100);

        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);
        TransferComplete tc = (TransferComplete) msg;

        if (TR069StaticParameter.EVENT_2.equals(lastEvent)) { // periodic session, kha nang cao ko vao, tam thoi chua xoa :

            Command cm = CommandConfigurationFactory.getCommand(lastInform.sn);
            if (cm != null && cm.getType().equals(Command.TYPE_PERIODIC)) {
                PeriodicCommand periodicCmd = (PeriodicCommand) cm;
                if (tc.FaultCode == 0) {
                    //periodicCmd.receiveResult();
                    String wifiIsEnabled = periodicCmd.getKeyMap().get(UpdateFirmwareCommand.UPDATE_FW_WIFI_STATE);
                    if ("1".equals(wifiIsEnabled)) {
                        // turn on wifi
                        DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.Set_EnableWireless, cm.getModelName());
                        ArrayList<SimpleObject> dataEnableWifi = dataModel.getDataModel();

                        for (SimpleObject item : dataEnableWifi) {
                            item.setValue("1");
                        }

                        SetParameterValues setVlCmd = new SetParameterValues();
                        sb.setLength(0);
                        sb.append("SetParameterValues: ");
                        for (SimpleObject sObj : dataEnableWifi) {
                            sb.append(sObj.getParameter()).append("_____Value: ").append(sObj.getValue()).append("___");
                            setVlCmd.AddValue(sObj.getParameter(), sObj.getValue(), sObj.getType());
                        }
                        logger.info(sb);
                        logger.info("TURN ON WIFI AFTER UPDATE FIRMWARE ...");
                        periodicCmd.getKeyMap().put(Command.CURRENT_STEP, UpdateFirmwareCommand.UPDATE_FW_TURN_ON_WIFI);
                        periodicCmd.getKeyMap().put(Command.NEXT_STEP, Command.COMPLETE_STEP);
                        setVlCmd.writeTo(out);
                    } else {
                        periodicCmd.receiveResult();
                    }
                } else {
                    periodicCmd.getKeyMap().put("ERROR", tc.FaultString);
                    periodicCmd.receiveError(tc.FaultString + " --> " + tc.toString());
                }
            }

        } else { // connection request session

            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
            if (cm != null) {
                if (cm.getType().equals(Command.TYPE_UPGRADE_FW)) {
                    UpdateFirmwareCommand upFW = (UpdateFirmwareCommand) cm;
                    upFW.setFaultcode(tc.FaultCode);
                    upFW.setFaultstring(tc.FaultString);

                    if (tc.FaultCode == 0) {
                        upFW.receiveResult();
                    } else {
                        upFW.receiveError(tc.FaultString);
                    }
                    logger.info("UpdateFirmwareCommand TransferComplete!");
                } else if (Command.TYPE_BATCHUPDATE_FW.equals(cm.getType())) {
                    UpdateFirmwareBatchCommand batchUpFW = (UpdateFirmwareBatchCommand) cm;
                    batchUpFW.setFaultcode(tc.FaultCode);
                    batchUpFW.setFaultstring(tc.FaultString);

                    if (tc.FaultCode == 0) {
                        batchUpFW.receiveResult();
                    } else {
                        batchUpFW.receiveError(tc.FaultString);
                    }
                    logger.info("UpdateFirmwareBatchCommand TransferComplete!");
                } else if (Command.TYPE_BACKUP.equals(cm.getType())) {
                    BackupCommand backup = (BackupCommand) cm;
                    if (tc.FaultCode == 0) {
                        backup.receiveResult();
                    } else {
                        backup.receiveError(tc.FaultString);
                    }

                    logger.info("Backup TransferComplete!");
                } else if (Command.TYPE_RESTORE.equals(cm.getType())) {
                    SetRestoreCommand restore = (SetRestoreCommand) cm;
                    if (tc.FaultCode == 0) {
                        restore.receiveResult();
                    } else {
                        restore.receiveError(tc.FaultString);
                    }
                    logger.info("Restore TransferComplete!");
                }
            }
        }

        if (tc.FaultCode == 0) {
            sb.setLength(0);
            sb.append("OUI=")
                    .append(lastInform.getOui()).append(", ProductClass=")
                    .append(lastInform.ProductClass).append(", SerialNumber=")
                    .append(lastInform.sn).append(", ")
                    .append(tc.toString());
            logger.info(sb.toString());
        } else {
            sb.setLength(0);
            sb.append("OUI=")
                    .append(lastInform.getOui()).append(", ProductClass=")
                    .append(lastInform.ProductClass).append(", SerialNumber=")
                    .append(lastInform.sn).append(", ")
                    .append(tc.toString());
            logger.warn(sb.toString());
        }

        TransferCompleteResponse tr = new TransferCompleteResponse(tc.getId());
        tr.writeTo(out);

    }

    @Override
    public void processUploadResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        logger.info("UploadResponse --> Do nothing !");
    }

    @Override
    public void processGetRPCMethods(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        GetRPCMethodsResponse responseGetRPCMethods = new GetRPCMethodsResponse((GetRPCMethods) msg);
        responseGetRPCMethods.writeTo(out);
    }

    @Override
    public void processGetParameterNamesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        StringBuilder sb = new StringBuilder(100);
        GetParameterNamesResponse dataReceive = (GetParameterNamesResponse) msg;
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        if (TR069StaticParameter.EVENT_6.equals(lastEvent)) {
            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
            if (cm != null) {
                if (cm.getType().equals(Command.TYPE_GETPARAMETER_NAMES)) {
                    GetParameterNamesCommand cmGetParameter = (GetParameterNamesCommand) cm;
                    cmGetParameter.setReturnValue(dataReceive.getDataValue());
                    cmGetParameter.receiveResult();
                }
            }
        }
    }

    protected void log(Inform inf, Level level, String msg) {
        StringBuilder s = new StringBuilder(128);
        if (inf != null) {
            s.append("oui=").append(inf.getOui()).append(" sn=").append(inf.sn).append(" ");
        }
        s.append(msg);
        logger.log(level, s.toString());
    }

    protected void log(Inform inf, Level level, String msg, Throwable ex) {
        StringBuilder s = new StringBuilder(128);
        if (inf != null) {
            s.append("oui=").append(inf.getOui()).append(" sn=").append(inf.sn).append(" ");
        }
        s.append(msg);
        logger.log(level, s.toString(), ex);
    }

    protected boolean hasEvent(Iterator<Map.Entry<String, String>> iter, String event) {
        boolean returnValue = false;

        while (iter.hasNext()) {
            Map.Entry<String, String> temp = iter.next();
            if (temp.getKey().equals(event)) {
                returnValue = true;
            }
        }

        return returnValue;
    }

    protected List<String> checkAreaID(String strIpAddress) {
        List<String> l_Return = new ArrayList<String>();
        l_Return.add(0, "-2");
        l_Return.add(1, "Ungrouped");
        List<AreaDeviceMapping> areaMapping = DatabaseManager.AREA_DEVICE_MAPPING_DAO.searchCriteria(null, null);
        for (AreaDeviceMapping temp : areaMapping) {
            String temp_IpAddress = temp.getIpAddress();
            Long temp_AreaID = temp.getAreaId();
            String temp_AreaName = temp.getAreaName();

            if (Subnet.checkIpInSubnetMatch(temp_IpAddress, strIpAddress)) {
                l_Return.add(0, temp_AreaID.toString());
                l_Return.add(1, temp_AreaName);
            }
        }
        return l_Return;
    }

    protected void saveDeviceInfo(HttpSession session, GetParameterValuesResponse dataReceive, Inform lastInform) {
        StringBuilder sb = new StringBuilder(100);
        AdslDevice device = DatabaseManager.ADSL_DEVICE_DAO.getByAdslSerialNumber(lastInform.sn);
        boolean zeroTouchEnabled = Integer.valueOf(DatabaseManager.ADSL_PARAM_CONFIG_DAO.get("ZeroTouch", "enableZero").getParamValue()) > 0;
        String intervalInform = DatabaseManager.ADSL_PARAM_CONFIG_DAO.get("ZeroTouch", "intervalInform").getParamValue();
        String ontModelName = dataReceive.values.get(TR069StaticParameter.DeviceInfoModelName);
        Long productID = DatabaseManager.getProductIDFromModelname(ontModelName);

        if (device != null) {
            sb.setLength(0);
            sb.append("SN=").append(lastInform.sn)
                    .append(", MANU=").append(lastInform.Manufacturer).append(", ZeroTouch_Enabled=")
                    .append(zeroTouchEnabled).append(", updating device infomation  ...");
            logger.info(sb.toString());

            device.setManufacturer(lastInform.Manufacturer);
            device.setOui(lastInform.getOui());
            device.setProductClass(lastInform.ProductClass);
            device.setHardwareVersion(lastInform.getHardwareVersion());
            device.setSoftwareVersion(lastInform.getSoftwareVersion());
            device.setProvisioningCode(lastInform.getProvisiongCode());
            device.setConnectionRequest(lastInform.getConnectionRequestURL());
            device.setIpAddress(DataFileUtils.getIPfromConnectionRequest(lastInform.getConnectionRequestURL()));
            device.setModelName(ontModelName != null ? ontModelName : "");
            device.setProductID(productID);
            DatabaseManager.ADSL_DEVICE_DAO.save(device);

        } else {
            sb.setLength(0);
            sb.append("SN=")
                    .append(lastInform.sn)
                    .append(", MANU=")
                    .append(lastInform.Manufacturer)
                    .append(", ZeroTouch_Enabled=")
                    .append(zeroTouchEnabled)
                    .append(", Creating new device infomation  ...");
            logger.info(sb.toString());

            AdslDevice newdevice = new AdslDevice();
            newdevice.setAdslSerialNumber(lastInform.sn);
            newdevice.setManufacturer(lastInform.Manufacturer);
            newdevice.setOui(lastInform.getOui());
            newdevice.setProductClass(lastInform.ProductClass);
            newdevice.setHardwareVersion(lastInform.getHardwareVersion());
            newdevice.setSoftwareVersion(lastInform.getSoftwareVersion());
            newdevice.setProvisioningCode(lastInform.getProvisiongCode());
            newdevice.setConnectionRequest(lastInform.getConnectionRequestURL());
            String ipAddress = DataFileUtils.getIPfromConnectionRequest(lastInform.getConnectionRequestURL());
            newdevice.setIpAddress(ipAddress);
            newdevice.setName(lastInform.sn);
            newdevice.setShortName(lastInform.sn);
            newdevice.setNodeType(new NodeType(10L, null, null));
            newdevice.setValidate(1); // valid all device
            //set more adsl name
            newdevice.setAdslName(lastInform.sn);
            newdevice.setModelName(ontModelName != null ? ontModelName : "");
            newdevice.setProductID(productID);

            if (ipAddress != null) {
                List<String> lstr_AreaID_Insert = checkAreaID(ipAddress);
                String str_AreaId_Insert = lstr_AreaID_Insert.get(0);
                String str_AreaCode_Insert = lstr_AreaID_Insert.get(1);
                newdevice.setArea(new Area(Long.parseLong(str_AreaId_Insert), str_AreaCode_Insert));
                newdevice.setAreaID(str_AreaId_Insert);
                newdevice.setAreaName(str_AreaCode_Insert);
            }

            DatabaseManager.ADSL_DEVICE_DAO.save(newdevice);
        }

        try {
            //zero touch function
            if (zeroTouchEnabled) {
                ZeroTouchCommand_GPON zeroCmd = new ZeroTouchCommand_GPON();
                zeroCmd.setZeroTouchEnabled(zeroTouchEnabled);
                zeroCmd.setSerialNumberCPE(lastInform.sn);
                zeroCmd.setConnectionRequestURL(lastInform.getConnectionRequestURL());
                zeroCmd.setIntervalInform(intervalInform);

                // Execute command
                zeroCmd.executeCommand();

                AdslAutoConfigParameter autoconfig = DatabaseManager.ADSL_AUTO_CONFIG_PARAM_DAO.getBySerialNumber(lastInform.sn);
                if (autoconfig == null) {
                    sb.setLength(0);
                    sb.append("Null. ZeroTouch skipped ! No parameters config for: ")
                            .append(lastInform.sn);
                    logger.warn(sb);
                    zeroCmd.setZeroTouchEnabled(false);
                } else {
                    sb.setLength(0);
                    sb.append("configuring ZeroTouch: ")
                            .append(", SN=")
                            .append(lastInform.sn)
                            .append(", MANU=")
                            .append(lastInform.Manufacturer)
                            .append(", AutoConfigParam=")
                            .append(autoconfig.toString());
                    logger.info(sb);
                    String vlanMuxId = autoconfig.getVlanMuxId();
                    String vlanMux8021p = autoconfig.getVlanMux8021p();
                    String userName = autoconfig.getUserName();
                    String password = autoconfig.getPassword();
                    zeroCmd.vlanMuxID = vlanMuxId;
                    zeroCmd.vlanMux802_1Priority = vlanMux8021p;
                    zeroCmd.PPPoE_username = userName;
                    zeroCmd.PPPoE_password = password;
                }
            }

        } catch (Exception ex) {
            logger.error("ZeroTouch Error: " + ex.getMessage(), ex);
        }

    }

    protected void processEvent(Inform lastInform, HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {

        /**
         * kiendt Bo sung xu ly da event
         */
        int sizeListEvent = lastInform.getEvents().size();
        if (sizeListEvent == 1) {
            if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_2)) {
                processEvent2Periodic(request, response, msg, out);
            } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_0)) {
                processEvent0Bootstrap(request, response, msg, out);
            } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_4)) {
                processEvent4ValueChange(request, response, msg, out);
            } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_6)) {
                processEvent6ConnectionRequest(request, response, msg, out);
            } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)) {
                processEvent1Boot(request, response, msg, out);
            } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_8)) {
                Command cm = CommandRequestFactory.getCommand(lastInform.sn);
                logger.info(TR069StaticParameter.EVENT_8 + " is done !");
                if (cm != null && Command.TYPE_SET_IP_PING_DIAGNOSTICS.equals(cm.getType())) {
                    cm.receiveResult();
                }
            } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_7)) {
                processEvent7TranferComplete(request, response, lastInform, out);
            }
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_2)
                && hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_4)) {
            processEvent4ValueChange(request, response, msg, out);
            processEvent2Periodic(request, response, msg, out);
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_0)
                && hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)) {
            // sau khi factory reset gui len 2 event 0, 1, chi can xu ly 0

            processEvent0Bootstrap(request, response, msg, out);
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)
                && hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_2)) {
            processEvent1Boot(request, response, msg, out);
            processEvent2Periodic(request, response, msg, out);
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)
                && hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_2)
                && hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_4)) {
            processEvent1Boot(request, response, msg, out);
            processEvent4ValueChange(request, response, msg, out);
            processEvent2Periodic(request, response, msg, out);
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)
                && hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_4)) {

            // sau khi auto update firmware, thiet bi gui len 4 event M Download, 7 tranfer complete
            // 1 boot, 4 value change
            // vi co the thiet bi thay doi lai serial number nen can save lai thong tin thiet bi
            processEvent1Boot(request, response, msg, out);
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)) { // uu tien event 1

            processEvent1Boot(request, response, msg, out);
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_6)) {

            processEvent6ConnectionRequest(request, response, lastInform, out);
        } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_4)) {

            processEvent4ValueChange(request, response, lastInform, out);
        }
    }

    @Override
    public void processEvent3ScheduleInform(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processScheduleInformResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        if (cm != null) {
            if (cm.getType().equals(Command.TYPE_SCHEDULEINFORM)) {
                ScheduleInformCommand rbCmd = (ScheduleInformCommand) cm;
                rbCmd.receiveResult();
            }
        }
    }

    protected void setInformTime(ByteArrayOutputStream out) {
        SetParameterValues setInformTime = new SetParameterValues();
        setInformTime.AddValue(TR069StaticParameter.PeriodicInformEnable, "1", "xsd:boolean");
        setInformTime.AddValue(TR069StaticParameter.PeriodicInformInterval, Properties.getInformIntervalDefault(), "xsd:unsignedInt");
        setInformTime.writeTo(out);
    }

    private void updateInforInEvent1(Inform lastInform) {
        Command cm = CommandRequestFactory.getCommand(lastInform.sn);
        if (cm != null) {
            if (Command.TYPE_UPGRADE_FW.equals(cm.getType()) || Command.TYPE_BATCHUPDATE_FW.equals(cm.getType())) {
                System.out.println("REBOOT: TYPE_UPGRADE_FW command in queue (need to remove)");
                cm.receiveResult();
            }
        }

        AdslDevice device = DatabaseManager.ADSL_DEVICE_DAO.getByAdslSerialNumber(lastInform.sn);

        //update thong tin thiet bi trong truong hop update firmware,....
        device.setManufacturer(lastInform.Manufacturer);
        device.setOui(lastInform.getOui());
        device.setProductClass(lastInform.ProductClass);
        device.setHardwareVersion(lastInform.getHardwareVersion());
        device.setSoftwareVersion(lastInform.getSoftwareVersion());
        device.setProvisioningCode(lastInform.getProvisiongCode());
        device.setConnectionRequest(lastInform.getConnectionRequestURL());
        device.setIpAddress(DataFileUtils.getIPfromConnectionRequest(lastInform.getConnectionRequestURL()));
        device.setValidate(1); // valid all device
        DatabaseManager.ADSL_DEVICE_DAO.save(device);

        //insert alarm
        Long area_id = -2L;
        String area_name = "Ungrouped";

        if (device.getArea().getId() != null) {
            area_id = device.getArea().getId();
        }
        if (device.getArea().getName() != null) {
            area_name = device.getArea().getName();
        }

        Alarm alarm = new Alarm();
        alarm.setNodeId(device.getId());
        alarm.setAlarmType("REBOOT");
        alarm.setName("REBOOT");
        alarm.setOriginalSeverity("Infor");
        alarm.setCurrentSeverity("Infor");
        alarm.setFirstTime(new Date());
        alarm.setLastTime(new Date());
        alarm.setNodeName(device.getAdslSerialNumber());
        alarm.setProductId(device.getProductID());
        alarm.setCount(1);
        alarm.setDescription("Device reboot On");
        alarm.setAreaID(area_id);
        alarm.setAreaName(area_name);
        DataDbManager.getInstance().enqueue(alarm);
    }

    private void updateInforInEvent4(Inform lastInform) {
        AdslDevice device = DatabaseManager.ADSL_DEVICE_DAO.getByAdslSerialNumber(lastInform.sn);

        Long provinceId = null, districtId = null;
        Area areaInstance = DatabaseManager.AREA_DAO.get(device.getAreaId());
        if (areaInstance.getAreaTypeId() == 0) {
            provinceId = device.getAreaId();
        } else if (areaInstance.getAreaTypeId() == 1) {
            districtId = device.getAreaId();
            provinceId = areaInstance.getParentId();
        }

        //insert alarm
        Long area_id = -2L;
        String area_name = "Ungrouped";

        if (device.getArea().getId() != null) {
            area_id = device.getArea().getId();
        }
        if (device.getArea().getName() != null) {
            area_name = device.getArea().getName();
        }

        Alarm alarm = new Alarm();
        alarm.setNodeId(device.getId());
        alarm.setAlarmType("VALUE CHANGE");
        alarm.setName("Value change on device");
        alarm.setCurrentSeverity("Infor");
        alarm.setFirstTime(new Date());
        alarm.setLastTime(new Date());
        //alarm.setNodeName(device.getAdslName());
        alarm.setProductId(device.getProductID());
        alarm.setProvince(provinceId);
        alarm.setDistrict(districtId);
        alarm.setNodeName(device.getAdslSerialNumber());
        alarm.setCount(1);
        if (lastInform.getConnectionRequestURL() == null || !lastInform.getConnectionRequestURL().equals(device.getConnectionRequest())) {
            alarm.setDescription("Ip device change");
        } else if (lastInform.getSoftwareVersion() == null || !lastInform.getSoftwareVersion().equals(device.getSoftwareVersion())) {
            alarm.setDescription("Firmware device change");
        } else if (lastInform.getProvisiongCode() == null || !lastInform.getProvisiongCode().equals(device.getProvisioningCode())) {
            alarm.setDescription("Provision code change");
        }
        alarm.setAreaID(area_id);
        alarm.setAreaName(area_name);
        //DataDbManager.getInstance().enqueue(alarm);
        DataDbManager.getInstance().enqueue(alarm);
        //end insert alarm
        //update adsl device	
        device.setManufacturer(lastInform.Manufacturer);
        device.setOui(lastInform.getOui());
        device.setProductClass(lastInform.ProductClass);
        device.setHardwareVersion(lastInform.getHardwareVersion());
        device.setSoftwareVersion(lastInform.getSoftwareVersion());
        device.setProvisioningCode(lastInform.getProvisiongCode());
        device.setConnectionRequest(lastInform.getConnectionRequestURL());
        device.setIpAddress(DataFileUtils.getIPfromConnectionRequest(lastInform.getConnectionRequestURL()));
        DatabaseManager.ADSL_DEVICE_DAO.save(device);
    }

    protected void enqueueUpdateFirmware(Inform lastInform) {

        AdslDevice deviceInfo = new AdslDevice();

        deviceInfo.setAdslSerialNumber(lastInform.sn);
        deviceInfo.setSerialNumber(lastInform.sn);
        deviceInfo.setManufacturer(lastInform.Manufacturer);
        deviceInfo.setOui(lastInform.getOui());

        deviceInfo.setProductClass(lastInform.ProductClass);
        deviceInfo.setHardwareVersion(lastInform.getHardwareVersion());
        deviceInfo.setSoftwareVersion(lastInform.getSoftwareVersion());
        deviceInfo.setProvisioningCode(lastInform.getProvisiongCode());

        deviceInfo.setConnectionRequest(lastInform.getConnectionRequestURL());
        String ipAddress = DataFileUtils.getIPfromConnectionRequest(lastInform.getConnectionRequestURL());
        deviceInfo.setIpAddress(ipAddress);
        deviceInfo.setName(lastInform.sn);
        deviceInfo.setShortName(lastInform.sn);

        Global.enqueueUpdateFw(deviceInfo);
    }
}
