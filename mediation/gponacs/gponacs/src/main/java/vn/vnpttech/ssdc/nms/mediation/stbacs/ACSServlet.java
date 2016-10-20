/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs;

import vn.vnpttech.ssdc.nms.mediation.stbacs.message.AddObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.GetRPCMethodsResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.DeleteObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.AddObjectResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.DownloadResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.Download;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.TransferComplete;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.GetParameterNamesResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.InformResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.TransferCompleteResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.GetRPCMethods;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.DeleteObjectResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.Reboot;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.Inform;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.GetParameterValues;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.GetParameterValuesResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.SetParameterValues;
import vn.vnpttech.ssdc.nms.mediation.stbacs.utils.Jms;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.Authenticator;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.soap.*;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.AddObjectCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.Command;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.CommandRequestFactory;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.DeleteObjectCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.GetValueCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.PeriodicCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.RebootCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.SetValueCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.TR069StaticParameter;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.UpdateFirmwareCommand;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import vn.vnpttech.ssdc.nms.mediation.stbacs.common.AppConfig;
import vn.vnpttech.ssdc.nms.mediation.stbacs.common.DeviceStatusInfo;
import vn.vnpttech.ssdc.nms.mediation.stbacs.common.ModelCache;
import vn.vnpttech.ssdc.nms.mediation.stbacs.executor.STBNMSWorker;
import vn.vnpttech.ssdc.nms.mediation.stbacs.http.ACSAuthenticator;
import vn.vnpttech.ssdc.nms.mediation.stbacs.message.SetParameterValuesResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.ZeroTouchCommand;
import static vn.vnpttech.ssdc.nms.mediation.stbacs.services.ACSServiceImpl.deviceManager;
import vn.vnpttech.ssdc.nms.mediation.stbacs.utils.BeanUtils;
import vn.vnpttech.ssdc.nms.mediation.stbacs.utils.ConfigUtils;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.DeviceModel;
import vn.vnpttech.ssdc.nms.model.PolicyHistory;
import vn.vnpttech.ssdc.nms.model.ServiceLog;
import vn.vnpttech.ssdc.nms.service.DeviceManager;
import vn.vnpttech.ssdc.nms.service.DeviceModelManager;
import vn.vnpttech.ssdc.nms.service.PolicyHistoryManager;
import vn.vnpttech.ssdc.nms.service.ServiceLogManager;

/**
 * ACSServlet manage device behind NAT
 *
 * @date 2015-06-12
 * @author Vunb
 * @version 1.0
 */
public class ACSServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(ACSServlet.class.getName());
    protected static final StringBuilder sb = new StringBuilder();
    protected static final String ATTR_LASTINFORM = "lastInform";
    //public static final String ATTR_CACHECOMMAND = "cacheEvent";
    private static final String ATTR_CONFIGURATOR = "cfgrun";
    private static final String ATTR_EVENT_DISALBE_WIFI = "Event_disable";
    private static final String EVENT_DISABLE = "disable_wifi";

    private static final String keyCheckPath = AppConfig.getKeyCheckPathVinaphoneWifi();
    private static final String keyCheckName = AppConfig.getKeyCheckNameVinaphoneWifi();
    private static final String pathWifi = AppConfig.getPathWifi();
    private static final String nameWifi = AppConfig.getWifiName();

    public static final DeviceModelManager deviceModelManager = BeanUtils.getInstance().getBean("deviceModelManager", DeviceModelManager.class);
    public static final DeviceManager deviceManager = BeanUtils.getInstance().getBean("deviceManager", DeviceManager.class);
    public static final PolicyHistoryManager policyHistoryManager = BeanUtils.getInstance().getBean("policyHistoryManager", PolicyHistoryManager.class);
    public static final ServiceLogManager serviceLogManager = BeanUtils.getInstance().getBean("serviceLogManager", ServiceLogManager.class);

    private class xmlFilterNS extends InputStream {
        // Dumb class to filter out declaration of default xmlns

        private final String pat = "xmlns=\"urn:dslforum-org:cwmp-1-0\"";
        private final String pat2 = "xmlns=\"urn:dslforum-org:cwmp-1-1\"";
        private int length = 0;
        private int pos = 0;
        private boolean f = false;
        private byte buff[] = new byte[1024];
        private final InputStream is;

        @Override
        public int read() throws IOException {
            if (!f) {
                length = is.read(buff);
                if (length < buff.length) {
                    byte[] b2 = new byte[length];
                    System.arraycopy(buff, 0, b2, 0, length);
                    buff = b2;
                }

                String b = new String(buff);
                b = b.replace(pat, "");
                b = b.replace(pat2, "");
                buff = b.getBytes();
                length = buff.length;
                f = true;
            }

            if (pos < length) {
                return buff[pos++];
            }
            return is.read();
        }

        public xmlFilterNS(InputStream is) {
            this.is = is;
        }
    }

    private class charsetConverterInputStream extends InputStream {

        private InputStream in;
        private PipedInputStream pipein;
        private OutputStream pipeout;
        private Reader r;
        private Writer w;

        public charsetConverterInputStream(String csFrom, String csTo, InputStream in) throws UnsupportedEncodingException, IOException {
            this.in = in;
            r = new InputStreamReader(in, csFrom);
            pipein = new PipedInputStream();
            pipeout = new PipedOutputStream(pipein);
            w = new OutputStreamWriter(pipeout, csTo);
        }

        @Override
        public int read() throws IOException {
            if (pipein.available() > 0) {
                return pipein.read();
            }
            int c = r.read();
            if (c == -1) {
                return -1;
            }
            w.write(c);
            w.flush();
            return pipein.read();
        }
    }

    private class xmlFilterInputStream extends InputStream {

        private InputStream istream;
        private int lvl;
        private int lastchar;
        private int len;
        private int nextchar;
        private boolean intag = false;
        private StringBuffer buff = new StringBuffer(16);

        /**
         * Creates a new instance of xmlFilterInputStream
         */
        public xmlFilterInputStream(InputStream is, int l) {
            //      System.out.println("Stream length is "+l);
            len = l;
            istream = is;
        }
//

        public int read() throws IOException {
            if (lastchar == '>' && lvl == 0) {
                //        System.err.println ("return EOF");
                return -1;
            }
            int l = lastchar;
            if (nextchar != -1) {
                lastchar = nextchar;
                nextchar = -1;
            } else {
                if (buff.length() > 0) {
                    lastchar = buff.charAt(0);
                    buff.deleteCharAt(0);
                    return lastchar;
                } else {
                    lastchar = istream.read();
                }
            }
            if (lastchar == '<') {
                intag = true;
            } else if (lastchar == '>') {
                intag = false;
            }

            if (!intag && lastchar == '&') {
                int amppos = buff.length();
                // fix up broken xml not encoding &
                buff.append((char) lastchar);
//                System.out.println("Appended buff len="+buff.length());
                for (int c = 0; c < 10; c++) {
                    int ch = istream.read();
                    //System.out.println("_yyy_"+nextchar);
                    if (ch == -1) {
                        break;
                    }
                    if (ch == '&') {
                        nextchar = ch;
                        break;
                    }
                    buff.append((char) ch);
//                System.out.println("Appended buff len="+buff.length());
                }
//                System.out.println ("xmlFilterInputStream: buff="+buff.substring(0, buff.length()));
                String s = buff.substring(amppos);
                if (!s.startsWith("&amp;") && !s.startsWith("&lt;") && !s.startsWith("&gt;") && !s.startsWith("&apos;") && !s.startsWith("&quot;") && !s.startsWith("&#")) {
                    buff.replace(amppos, amppos + 1, "&amp;");
                }
                return read();
            }

            if (l == '<') {
                intag = true;
                if (lastchar == '/') {
                    lvl--;
                } else {
                    lvl++;
                }
            }
            //           System.err.println ("return char="+(char)lastchar+" lvl="+lvl);
            //System.err.print ((char)lastchar);
            len--;
            return lastchar;
        }
//       

        public boolean next() throws IOException {
            while ((nextchar = istream.read()) != -1) {
                if (!Character.isWhitespace(nextchar)) {
                    break;
                }
            }
            //System.out.println ("Next char is "+nextchar);
            lvl = 0;
            lastchar = 0;
            return (nextchar != -1);
        }
//    
    }

    private boolean authenticate(HostsLocal host, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return HttpAuthentication.Authenticate(host.getUsername(), host.getPassword(), host.getAuthtype(), request, response);
    }

    private void log(Inform inf, Level level, String msg) {
        StringBuilder s = new StringBuilder(128);
        if (inf != null) {
            s.append("oui=").append(inf.getOui()).append(" sn=").append(inf.sn).append(" ");
        }
        s.append(msg);
        logger.log(level, s);
    }

    private void log(Inform inf, Level level, String msg, Throwable ex) {
        StringBuilder s = new StringBuilder(128);
        if (inf != null) {
            s.append("oui=").append(inf.getOui()).append(" sn=").append(inf.sn).append(" ");
        }
        s.append(msg);
        logger.log(level, s, ex);
    }

    //xuannv
    private ArrayList<String> getListEvent(Iterator<Entry<String, String>> iter) {
        ArrayList<String> returnValue = new ArrayList<String>();
        while (iter.hasNext()) {
            Entry<String, String> temp = iter.next();
            returnValue.add(temp.getKey());
        }
        return returnValue;
    }

    private boolean hasEvent(Iterator<Entry<String, String>> iter, String event) {
        boolean returnValue = false;

        while (iter.hasNext()) {
            Entry<String, String> temp = iter.next();
            if (temp.getKey().equals(event)) {
                returnValue = true;
            }
        }

        return returnValue;
    }
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

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     *
     *
     * @param request ACSServlet request
     * @param response ACSServlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            logger.debug("session: " + request.getSession().getId());
            ServletContext ctx = getServletContext();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            SOAPMessage soapMsg = null;
            xmlFilterInputStream f = new xmlFilterInputStream(request.getInputStream(), request.getContentLength());
            MessageFactory mf;

            mf = MSG_FACTORY.get();
            String ct = request.getContentType();
            int csix = (ct != null) ? ct.indexOf("charset=") : -1;
            String csFrom = (csix == -1) ? "ISO-8859-1" : ct.substring(csix + 8).replaceAll("\"", "");
            response.setContentType(ct != null ? ct : "text/xml;charset=UTF-8");
            HttpSession session = request.getSession();
            Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
            String serialNumber = "";
            if (lastInform != null) {
                serialNumber = lastInform.sn;
            }

            if (f.next()) {
                try {
                    MimeHeaders hdrs = new MimeHeaders();
                    hdrs.setHeader("Content-Type", "text/xml; charset=UTF-8");
                    InputStream in = (csFrom.equalsIgnoreCase("UTF-8")) ? new xmlFilterNS(f) : new charsetConverterInputStream(csFrom, "UTF-8", new xmlFilterNS(f));
                    soapMsg = mf.createMessage(hdrs, in);

                    Message msg = null;
                    try {
                        msg = Message.Parse(soapMsg);
                    } catch (Exception e) {
                        soapMsg.writeTo(out);
                        log(lastInform, Level.ERROR, "Parsing failed:\n" + out.toString(), e);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return;
                    }
                    String reqname = msg.getName();
                    log(lastInform, Level.DEBUG, "Request is " + reqname);
//                    log(lastInform, Level.DEBUG, msg.toString());

                    try {

                        try {
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            soapMsg.writeTo(bos);
                            String strMsg = new String(bos.toByteArray());
//                            sb.setLength(0);
//                            sb.append("RequestName: ")
//                                    .append(reqname)
//                                    .append(", DeviceResponse: ")
//                                    .append(strMsg);
//                            logger.debug(sb);
                        } catch (SOAPException ex) {
                        }

                        if (reqname.equals("Inform")) {
                            lastInform = (Inform) msg;
                            session.setAttribute(ATTR_LASTINFORM, lastInform);

                            InformResponse resp = new InformResponse(lastInform.getId(), 1);
                            resp.writeTo(out);

                            String url = lastInform.getURL();
                            log(lastInform, Level.INFO, "oui=" + lastInform.getOui() + ", sn=" + lastInform.sn + ", URL=" + url + ", hw=" + lastInform.getHardwareVersion() + ", sw=" + lastInform.getSoftwareVersion() + ", cfg=" + lastInform.getConfigVersion() + ", ProvisioningCode=" + lastInform.getProvisiongCode());
                        } else if (reqname.equals("TransferComplete")) {
                            log(lastInform, Level.INFO, ((TransferComplete) msg).toString());
                            TransferComplete tc = (TransferComplete) msg;
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
                                } else if (cm.getType().equals(Command.TYPE_PERIODIC)) {
                                    PeriodicCommand periodicCmd = (PeriodicCommand) cm;
                                    if (tc.FaultCode == 0) {
                                        periodicCmd.receiveResult();
                                    } else {
                                        periodicCmd.receiveError(tc.FaultString);
                                    }
                                }
                            }
//                        SendResponse(session, msg, out);
                            TransferCompleteResponse tr = new TransferCompleteResponse(tc.getId());
                            tr.writeTo(out);

                            //transfersComplete.add(tc);
                        } else if (reqname.equals("DeleteObjectResponse")) {
                            DeleteObjectResponse delResPon = (DeleteObjectResponse) msg;
                            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
                            if (delResPon.Status == 0) {
                                if (cm != null) {
                                    cm.receiveResult();
                                }
                            } else {
                                if (cm != null) {
                                    cm.receiveError("Can't delete object, need reboot");
                                }
                            }
                            SendResponse(session, msg);
                            //countEnvelopes++;
                        } else if (reqname.equals("DownloadResponse")) {
                            DownloadResponse downloadResPon = (DownloadResponse) msg;
                            if (downloadResPon.Status != 1) {
                                Command cm = CommandRequestFactory.getCommand(serialNumber);
                                if (cm != null) {
                                    if (cm.getType().equals(Command.TYPE_UPGRADE_FW)) {
                                        UpdateFirmwareCommand upCmd = (UpdateFirmwareCommand) cm;
                                        upCmd.setDownloadResponseStt(downloadResPon.Status);
                                        upCmd.receiveError("Can't receive download respone");
                                    }
                                }
                            } else {
                                Command cm = CommandRequestFactory.getCommand(serialNumber);
                                cm.receiveResult();
                            }
                            SendResponse(session, msg);
                            //countEnvelopes++;
                        } else if (reqname.equals("RebootResponse")) {
                            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
                            if (cm != null) {
                                if (Command.TYPE_REBOOT.equals(cm.getType())) {
                                    RebootCommand rbCmd = (RebootCommand) cm;
                                    rbCmd.receiveResult();
                                } else {
                                    logger.info("RebootResponse! But not found command request, SerialNumber=" + serialNumber);
                                }
                            }
//                    	RebootResponse rbRes = (RebootResponse) msg;
                            SendResponse(session, msg);
                            //countEnvelopes++;
                        } else if (reqname.equals("GetRPCMethods")) {
                            GetRPCMethodsResponse responseGetRPCMethods = new GetRPCMethodsResponse((GetRPCMethods) msg);
                            responseGetRPCMethods.writeTo(out);
                            //countEnvelopes++;
                        } else if (reqname.equals("GetParameterNamesResponse")) {
                            GetParameterNamesResponse responseGetName = (GetParameterNamesResponse) msg;
                            SendResponse(session, msg);
                            //countEnvelopes++;
                        } else if (reqname.equals("GetParameterValuesResponse")) {
                            GetParameterValuesResponse dataReceive = (GetParameterValuesResponse) msg;
                            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
                            if (cm != null) {
                                System.out.println("GetParameterValuesResponse with CommandType: " + cm.getType());
                                if (cm.getType().equals(Command.TYPE_GETTREEVALUE)) {
                                    GetValueCommand getVlCmd = (GetValueCommand) cm;

                                    getVlCmd.setReturnValue(dataReceive.values);
                                    getVlCmd.receiveResult();
                                } else if (cm.getType().equals(Command.TYPE_ADDOBJECT)) {
                                    AddObjectCommand addCmd = (AddObjectCommand) cm;

                                    addCmd.ifnameObject.setValue(dataReceive.getParam(addCmd.ifnameObject.getParameter()));

                                    addCmd.receiveResult();
                                } else if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) {
                                    sb.setLength(0);
                                    sb.append("Event type: ")
                                            .append(cm.getType())
                                            .append(", but do nothing !")
                                            .append(", SerialNumber=")
                                            .append(cm.getSerialNumberCPE());
                                    logger.debug(sb);
                                } else if (cm.getType().equals(Command.TYPE_PERIODIC)) {
                                    sb.setLength(0);
                                    sb.append("Event type: ")
                                            .append(cm.getType())
                                            .append(", but do nothing !")
                                            .append(", SerialNumber=")
                                            .append(cm.getSerialNumberCPE());
                                    logger.debug(sb);
                                } else {
                                    sb.setLength(0);
                                    sb.append("Event type: ")
                                            .append(cm.getType())
                                            .append(", but do nothing !");
                                    logger.debug(sb);
                                }
                            } else {
                                long startTime = System.currentTimeMillis();
                                String eventDisabel = (String) session.getAttribute(ATTR_EVENT_DISALBE_WIFI);

                                if (eventDisabel != null && eventDisabel.equals(EVENT_DISABLE)) {
                                    logger.info("[Disable wifi vinaphone] Start parse result");
                                    Map<String, String> result = dataReceive.values;
//                                    String keyCheckPath = AppConfig.getKeyCheckPathVinaphoneWifi();
//                                    String keyCheckName = AppConfig.getKeyCheckNameVinaphoneWifi();
                                    String startPath = null;
                                    for (Map.Entry<String, String> entry : result.entrySet()) {
                                        String key = entry.getKey();
                                        String value = entry.getValue();
                                        if (key.contains(keyCheckPath)) {
                                            if (value.contains(keyCheckName)) {
                                                int b = key.indexOf(keyCheckPath);
                                                startPath = key.substring(0, b);
                                                break;
                                            }
                                        }
                                    }
                                    logger.info("[Disable wifi vinaphone] path " + startPath);
                                    if (startPath != null) {
                                        logger.info("[Disable wifi vinaphone] Start send setCommand");
//                                        int point = startPath.indexOf("WlVirtIntfCfg");
//                                        String path = startPath.substring(0, point);
                                        //InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_BROADCOM_COM_WlanAdapter.WlBaseCfg.WlEnbl  
                                        //InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_BROADCOM_COM_WlanAdapter.WlVirtIntfCfg.1.WlEnblSsid

                                        String disableWifiPath = startPath + "WlEnblSsid";
                                        String disableBasicWifiPath = pathWifi + "WlBaseCfg.WlEnbl";

                                        SetParameterValues setParam = new SetParameterValues();
                                        // add object disable wifi                                   
                                        setParam.AddValue(disableWifiPath, "0", "xsd:int");
                                        setParam.AddValue(disableBasicWifiPath, "1", "xsd:int");
                                        // set lai ten wifi cho thiet bi
//                                        String wifiName = AppConfig.getWifiName();
                                        if (!nameWifi.isEmpty()) {
                                            setParam.AddValue(startPath + "WlSsid", nameWifi, "xsd:string");
                                        }
                                        setParam.writeTo(out);
                                    }
                                    long endTime = System.currentTimeMillis();
                                    logger.info("[Disable wifi vionaphone] TIME  = " + (endTime - startTime));
//                                logger.warn("GetParameterValuesResponse but Command is null ??? Please check !");
                                }
                            }
                            SendResponse(session, msg);
                        } else if (reqname.equals("SetParameterValuesResponse")) {
                            SetParameterValuesResponse setResponse = (SetParameterValuesResponse) msg;
                            logger.info("[SetParameterResposne] Status " + setResponse.Status);
                            Command cm = CommandRequestFactory.getCommand(serialNumber);
                            String eventDisabel = (String) session.getAttribute(ATTR_EVENT_DISALBE_WIFI);
                            if (eventDisabel != null && eventDisabel.equals(EVENT_DISABLE)) {
                                ServiceLog logService = new ServiceLog();
                                logService.setActionStartTime(new Date());
                                logService.setSerialNumber(serialNumber);
                                logService.setActionName("DISABLE WIFI VINAPHONE");
                                logService.setActionEndTime(new Date());
                                Device device = deviceManager.getDeviceBySerialNumber(serialNumber);
                                if (device != null) {
                                    logService.setIpDevice(device.getIpAddress());
                                }
                                if (setResponse.Status == 0) {
                                    logService.setResult("SUCCESS");
                                } else {
                                    logService.setResult("FAILED");
                                }
                                serviceLogManager.save(logService);
                            }

                            if (cm != null) {
                                if (Command.TYPE_ZERO_TOUCH.equals(cm.getType())) {
                                    ZeroTouchCommand zeroCmd = (ZeroTouchCommand) cm;
                                    // ZeroTouch complete
                                    sb.setLength(0);
                                    sb.append(cm.getType()).append(", ZeroTouch completed! SerialNumber=")
                                            .append(serialNumber);
                                    logger.info(sb);
                                    zeroCmd.orderCmd = 1;
                                    zeroCmd.receiveResult();

                                } else if (Command.TYPE_ADDOBJECT.equals(cm.getType())) {
                                    AddObjectCommand addCmd = (AddObjectCommand) cm;
                                    if (addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_LAYER2)
                                            || addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_WANSERVICE)) {
                                        GetParameterValues getVl = new GetParameterValues(addCmd.ifnameObject.getParameter());
                                        getVl.writeTo(out);
                                    } else {
                                        cm.receiveResult();
                                    }
                                } else if (Command.TYPE_PERIODIC.equals(cm.getType())) {
                                    PeriodicCommand periodicCmd = (PeriodicCommand) cm;
                                    periodicCmd.receiveResult();

                                } else {
                                    sb.setLength(0);
                                    sb.append("CommandType unknow! CommandType=")
                                            .append(cm.getType())
                                            .append(", SerialNumber=")
                                            .append(serialNumber);
                                    cm.receiveResult();
                                }
                            }
                            SendResponse(session, msg);
                        } else if (reqname.equals("AddObjectResponse")) {
                            AddObjectResponse respon = (AddObjectResponse) msg;
                            Command cm = CommandRequestFactory.getCommand(serialNumber);
                            if (cm != null) {
                                if (cm.getType().equals(Command.TYPE_ADDOBJECT)) {
                                    AddObjectCommand addCmd = (AddObjectCommand) cm;
                                    if (respon.Status != 0) {
                                        addCmd.receiveError("Need ReBoot Device");
                                    } else {
                                        addCmd.setInstance(respon.InstanceNumber);
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
                                                if (sObj.getName().equals("IfName")) {
                                                    // Update Interface Name
                                                    addCmd.ifnameObject = sObj;
                                                }
                                            }
                                        } else if (addCmd.getType_addObj().equals(AddObjectCommand.ADDOBJ_IPLEASE)) {
                                            for (SimpleObject sObj : addCmd.getListObj()) {
                                                sObj.setParameter(addCmd.getRootName() + "." + addCmd.getInstance() + "." + sObj.getParameter());
                                            }
                                        } else {
                                            for (SimpleObject sObj : addCmd.getListObj()) {
                                                sObj.setParameter(addCmd.getRootName() + "." + addCmd.getInstance() + "." + sObj.getParameter());
                                            }
                                        }
                                        if (addCmd.getListObj() != null && addCmd.getListObj().size() > 0) {
                                            //set Value Parameter Obj
                                            SetParameterValues setVlCmd = new SetParameterValues();
                                            for (SimpleObject sObj : addCmd.getListObj()) {
                                                System.out.println(sObj.getParameter() + "_____" + sObj.getValue());
                                                //if (!"IfName".equals(sObj.getName()))
                                                {
                                                    setVlCmd.AddValue(sObj.getParameter(), sObj.getValue(), sObj.getType());
                                                }
                                            }

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
                                } else if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) {
                                    ZeroTouchCommand cmd = (ZeroTouchCommand) cm;
                                    cmd.receiveResult();
                                }
                            }
                            SendResponse(session, msg);
                        } else if (reqname.equals("Fault")) {
                            Command cm = CommandRequestFactory.getCommand(lastInform.sn);
                            if (cm != null) {
                                cm.receiveError(msg.toString());
                                sb.setLength(0);
                                sb.append(cm.getType())
                                        .append(", ERROR: ")
                                        .append(msg.toString());
                                //if (soapMsg != null) {
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                soapMsg.writeTo(bos);
                                String strMsg = new String(bos.toByteArray());
                                sb.append(", RAW_MESSAGE: \n")
                                        .append(strMsg);

                                logger.error(sb);
                            } else {
                                sb.setLength(0);
                                sb.append("Request error. Message: ")
                                        .append(msg.toString());
                                logger.error(sb);

                            }
                            SendResponse(session, msg);
                        } else {

                            SendResponse(session, msg);
                        }
                        // </editor-fold>

                    } catch (NoSuchElementException e) {

                        log(lastInform, Level.ERROR, "While parsing", e);
                        return;
                    } catch (IllegalArgumentException e) {
                        log(lastInform, Level.WARN, "IllegalArgumentException", e);
                    }
                } catch (SOAPException e) {
                    log(lastInform, Level.ERROR, "While parsing", e);
                    //break;
                } catch (IllegalArgumentException e) {
                    log(lastInform, Level.WARN, "IllegalArgumentException", e);
                }

            } else if (lastInform != null && lastInform.getEvents() != null) {
                //check has EVENT_0 
                if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_0)) {

                    long time = System.currentTimeMillis();
//                    logger.debug("BOOTSTRAP........");
//                    DeviceModelManager deviceModelManager = BeanUtils.getInstance().getBean("deviceModelManager", DeviceModelManager.class);
//                    DeviceManager deviceManager = BeanUtils.getInstance().getBean("deviceManager", DeviceManager.class);
                    Device device = deviceManager.getDeviceBySerialNumber(serialNumber);
                    logger.info("TIME TO SEARCH DEVICE BY SERIAL = " + (System.currentTimeMillis() - time));
                    String userName = serialNumber;
                    // Check device info
                    if (device != null) {
                        sb.setLength(0);
                        sb.append("EVENT_0: ")
                                .append(TR069StaticParameter.EVENT_0)
                                .append(", SN=")
                                .append(serialNumber)
                                .append(", MANU=")
                                .append(lastInform.Manufacturer)
                                .append(", Update exists device !");
                        logger.info(sb);
                        String productClass = lastInform.ProductClass;
                        long s = System.currentTimeMillis();
                        String VNPTname = ConfigUtils.getInstance().getModelName(productClass);
                        DeviceModel model = ModelCache.mapCacheModel.get(VNPTname);
                        if (model == null) {
                            model = deviceModelManager.getModelByName(VNPTname);
                            ModelCache.mapCacheModel.put(VNPTname, model);
                        }
                        long s1 = System.currentTimeMillis();
                        logger.info("TIME TO GET MODEL BY NAME" + (s1 - s));
                        if (model == null) {
                            model = new DeviceModel();
                            model.setName(VNPTname);
                            model.setDescription(productClass);
                            model = deviceModelManager.save(model);
                        }

                        device.setSerialNumber(lastInform.sn);
                        device.setMac(lastInform.mac);
                        device.setDeviceModel(model);
                        device.setFirmwareVersion(lastInform.getSoftwareVersion());
                        device.setConnectionReq(lastInform.getConnectionRequestURL());
                        device.setIpAddress(getIPfromConnectionRequest(lastInform.getConnectionRequestURL()));
                        long schen = System.currentTimeMillis();
                        deviceManager.save(device);
                        long schen1 = System.currentTimeMillis();
                        logger.info("TIME TO UPDATE DEVICE = " + (schen1 - schen));
                    } else {
                        sb.setLength(0);
                        sb.append("EVENT_0: BOOTSTRAP")
                                .append(", SN=")
                                .append(serialNumber)
                                .append(", MANU=")
                                .append(lastInform.Manufacturer)
                                .append(", Create New Device");
                        logger.info(sb.toString());
                        String productClass = lastInform.ProductClass;
                        long s0 = System.currentTimeMillis();
                        String VNPTname = ConfigUtils.getInstance().getModelName(productClass);
                        DeviceModel model = ModelCache.mapCacheModel.get(VNPTname);
                        if (model == null) {
                            model = deviceModelManager.getModelByName(VNPTname);
                            ModelCache.mapCacheModel.put(VNPTname, model);
                        }
                        long s1 = System.currentTimeMillis();
                        logger.info("TIME TO GET MODEL BY NAME" + (s1 - s0));
                        if (model == null) {
                            model = new DeviceModel();
                            model.setName(VNPTname);
                            model.setDescription(productClass);
                            model = deviceModelManager.save(model);
                        }
                        device = new Device();
                        device.setSerialNumber(lastInform.sn);
                        device.setMac(lastInform.mac);
                        device.setDeviceModel(model);
                        device.setFirmwareVersion(lastInform.getSoftwareVersion());
                        device.setConnectionReq(lastInform.getConnectionRequestURL());
                        device.setIpAddress(getIPfromConnectionRequest(lastInform.getConnectionRequestURL()));
                        long schen = System.currentTimeMillis();
                        deviceManager.insertDevice(device);
                        long schen1 = System.currentTimeMillis();
                        logger.info("TIME TO SAVE DEVICE = " + (schen1 - schen));
                    }
                    long moinhe = System.currentTimeMillis();
                    if (AppConfig.isDisableWifiVinaphone()) {
                        logger.info("[Disable wifi vinaphone]Start get parameter");
                        GetParameterValues getParam = new GetParameterValues(pathWifi);
                        getParam.writeTo(out);
                        session.setAttribute(ATTR_EVENT_DISALBE_WIFI, EVENT_DISABLE);
                    }
                    long endTime = System.currentTimeMillis();
                    logger.info("TIME EVENT 0 BOOTTRAP = " + (endTime - time));

                } // check has EVENT_1
                else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)) {
                    String event = TR069StaticParameter.EVENT_1;
                    Command cm = CommandRequestFactory.getCommand(lastInform.sn);
                    if (cm != null) {
                        if (Command.TYPE_UPGRADE_FW.equals(cm.getType())) {
                            sb.setLength(0);
                            sb.append("Event: ")
                                    .append(event)
                                    .append(", Reboot after update firmware")
                                    .append(", SerialNumber=")
                                    .append(serialNumber);
                            logger.info(sb);
                            cm.receiveResult();
                        } else {
                            sb.setLength(0);
                            sb.append("Event: ")
                                    .append(event)
                                    .append(", CommandType timeout or not support")
                                    .append(", SerialNumber=")
                                    .append(serialNumber)
                                    .append(", CommandType=")
                                    .append(cm.getType());
                            logger.info(sb);
                            cm.receiveResult();
                        }
                    }
                    // Update Firmware Version or Create new Device if Not Found
//                    DeviceModelManager deviceModelManager = BeanUtils.getInstance().getBean("deviceModelManager", DeviceModelManager.class);
//                    DeviceManager deviceManager = BeanUtils.getInstance().getBean("deviceManager", DeviceManager.class);
                    Device device = deviceManager.getDeviceBySerialNumber(serialNumber);

                    // Check device info
                    logger.info("BOOT: Update device info, serialNumber=" + serialNumber);
                    if (device != null) {
                        sb.setLength(0);
                        sb.append(TR069StaticParameter.EVENT_1)
                                .append(", SN=")
                                .append(serialNumber)
                                .append(", MANU=")
                                .append(lastInform.Manufacturer)
                                .append(", Update exists device !");
                        logger.info(sb);
                        String productClass = lastInform.ProductClass;
                        String VNPTname = ConfigUtils.getInstance().getModelName(productClass);
                        DeviceModel model = ModelCache.mapCacheModel.get(VNPTname);
                        if (model == null) {
                            model = deviceModelManager.getModelByName(VNPTname);
                            ModelCache.mapCacheModel.put(VNPTname, model);
                        }
                        if (model == null) {
                            model = new DeviceModel();
                            model.setName(VNPTname);
                            model.setDescription(productClass);
                            model = deviceModelManager.save(model);
                        }

                        device.setSerialNumber(lastInform.sn);
                        device.setDeviceModel(model);
                        device.setMac(lastInform.mac);
                        device.setFirmwareVersion(lastInform.getSoftwareVersion());
                        device.setConnectionReq(lastInform.getConnectionRequestURL());
                        device.setIpAddress(getIPfromConnectionRequest(lastInform.getConnectionRequestURL()));
                        deviceManager.save(device);
                    } else {
                        sb.setLength(0);
                        sb.append(TR069StaticParameter.EVENT_1)
                                .append(", SN=")
                                .append(serialNumber)
                                .append(", MANU=")
                                .append(lastInform.Manufacturer)
                                .append(", Create New Device");
                        logger.info(sb.toString());

                        String productClass = lastInform.ProductClass;
                        String VNPTname = ConfigUtils.getInstance().getModelName(productClass);
                        DeviceModel model = ModelCache.mapCacheModel.get(VNPTname);
                        if (model == null) {
                            model = deviceModelManager.getModelByName(VNPTname);
                            ModelCache.mapCacheModel.put(VNPTname, model);
                        }
                        if (model == null) {
                            model = new DeviceModel();
                            model.setName(VNPTname);
                            model.setDescription(productClass);
                            model = deviceModelManager.save(model);
                        }

                        device = new Device();
                        device.setSerialNumber(lastInform.sn);
                        device.setMac(lastInform.mac);
                        device.setDeviceModel(model);
                        device.setFirmwareVersion(lastInform.getSoftwareVersion());
                        device.setConnectionReq(lastInform.getConnectionRequestURL());
                        device.setIpAddress(getIPfromConnectionRequest(lastInform.getConnectionRequestURL()));
                        long schen = System.currentTimeMillis();
                        deviceManager.insertDevice(device);
                        long schen1 = System.currentTimeMillis();
                        logger.info("TIME TO SAVE DEVICE = " + (schen1 - schen));

                    }
                    if (AppConfig.isDisableWifiVinaphone()) {
                        logger.info("[Disable wifi vinaphone]Start get parameter");
//                        String wifiTree = AppConfig.getPathWifi();
                        GetParameterValues getParam = new GetParameterValues(pathWifi);
                        getParam.writeTo(out);
                        session.setAttribute(ATTR_EVENT_DISALBE_WIFI, EVENT_DISABLE);
                    }
                }
                // check has EVENT_4
                if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_4)) {
                    // check if device notify Update Firmware success
                    String event = TR069StaticParameter.EVENT_4;
                    String commandKey = null;
                    for (Entry<String, String> evt : lastInform.getEvents()) {
                        if (TR069StaticParameter.EVENT_4.equals(evt.getKey())) {
                            commandKey = evt.getValue();
                        }
                    }

                    sb.setLength(0);
                    sb.append("Event: ")
                            .append(event)
                            .append(", Device notify change")
                            .append(", SerialNumber=")
                            .append(serialNumber)
                            .append(", Event_4: CommandKey=")
                            .append(commandKey);

                    logger.info(sb);

                    if ("Update Firmware Status".equalsIgnoreCase(commandKey)) {
                        logger.info("OOOOOOH NOOOOOOO, I GOT HIME !!!!!");
                        logger.info("Update Firmware Status " + lastInform.getUpdateFirmwareStatus());
                        // Get by SerialNumber, EndTime = NULL, Status = Updating in (Updating, Success, Fail)
                        // 1. Update PolicyHistory
                        Inform.UpdateFirmwareStatus updateStatus = lastInform.getUpdateFirmwareStatus();
                        int status = "0".equals(updateStatus.getErrorCode())
                                ? DeviceStatusInfo.UPDATE_FIRMWARE_SUCCESS
                                : DeviceStatusInfo.UPDATE_FIRMWARE_FAIL;
//                        PolicyHistoryManager policyHistoryManager = BeanUtils.getInstance().getBean("policyHistoryManager", PolicyHistoryManager.class);
                        List<PolicyHistory> policyHis = policyHistoryManager.getPolicyHistory(serialNumber, DeviceStatusInfo.UPDATE_FIRMWARE_UPDATING);
                        if (policyHis != null && !policyHis.isEmpty()) {
                            for (PolicyHistory his : policyHis) {

                                his.setFirmwareNewVersion(lastInform.getSoftwareVersion());
                                his.setEndTime(new Timestamp(new Date().getTime()));
                                his.setStatus(status);
                                policyHistoryManager.save(his);

                                StringBuilder sb = new StringBuilder();
                                sb.append("Update policyHistory success, policyHisID: ")
                                        .append(his.getId())
                                        .append(", SerialNumber: ")
                                        .append(serialNumber)
                                        .append(", Update Status: ")
                                        .append(updateStatus);
                                logger.info(sb);
                            }
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("No policyHistory not Found: SerialNumber: ")
                                    .append(serialNumber)
                                    .append(", Update Status: ")
                                    .append(updateStatus);
                            logger.warn(sb);
                        }
                    } else if ("IPAddress Change".equalsIgnoreCase(commandKey)) {
                        this.updateIPAddressDevice(lastInform, serialNumber);
                    }

                }
                // check has EVENT_6
                if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_6)) {
                    Command cm = CommandRequestFactory.getCommand(lastInform.sn);
                    System.out.println("Connection Request: EVENT_6 CONNECTION REQUEST");
                    if (cm != null) {
                        System.out.println("COMMAND_TYPE: " + cm.getType());
                        if (cm.getType().equals(Command.TYPE_GETTREEVALUE)) {
                            GetValueCommand gtValCmd = (GetValueCommand) cm;
                            GetParameterValues recm = new GetParameterValues(gtValCmd.getParams());
                            recm.writeTo(out);
                        } else if (cm.getType().equals(Command.TYPE_SETVALUE)) {
                            SetValueCommand setCmd = (SetValueCommand) cm;
                            SetParameterValues recm = new SetParameterValues();
                            for (int i = 0; i < setCmd.getListObj().size(); i++) {
                                recm.AddValue(setCmd.getListObj().get(i).getParameter(), setCmd.getListObj().get(i).getValue(), setCmd.getListObj().get(i).getType());
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
                        } else if (cm.getType().equals(Command.TYPE_ZERO_TOUCH)) {
                            //ZeroTouchCommand_GPON command = (ZeroTouchCommand_GPON) cm;
                            //Enable Interval & Notify Zero_Touch_Command !!
                            SetParameterValues recm = new SetParameterValues();
                            recm.AddValue(TR069StaticParameter.PeriodicInformEnable, "1", "xsd:boolean");
                            recm.writeTo(out);
                        } else if (cm.getType().equals(Command.TYPE_UPGRADE_FW)) {

                            UpdateFirmwareCommand upFWCmd = (UpdateFirmwareCommand) cm;
                            // check version
                            if (upFWCmd.getFileVersion() != null
                                    && upFWCmd.getFileVersion()
                                    .equals(lastInform.getSoftwareVersion())) {
                                // Update PolicyHistory
                                upFWCmd.receiveResult();
                                logger.info("Already update firmware: Version=" + lastInform.getSoftwareVersion());
                                PolicyHistoryManager policyHistoryManager = BeanUtils
                                        .getInstance()
                                        .getBean("policyHistoryManager", PolicyHistoryManager.class);
                                List<PolicyHistory> policyHis = policyHistoryManager
                                        .getPolicyHistory(serialNumber, DeviceStatusInfo.UPDATE_FIRMWARE_UPDATING);
                                if (policyHis != null && !policyHis.isEmpty()) {
                                    for (PolicyHistory his : policyHis) {
                                        his.setStatus(DeviceStatusInfo.UPDATE_FIRMWARE_SUCCESS);
                                        policyHistoryManager.save(his);
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Update policyHistory success, policyHisID: ")
                                                .append(his.getId())
                                                .append(", SerialNumber: ")
                                                .append(serialNumber)
                                                .append(", Update Status: 1");
                                        logger.info(sb);
                                    }
                                }
                            } else {
                                // Send update command
                                Download recm = new Download("", upFWCmd.getUrlFileServer(), Download.FT_FIRMWARE);
                                recm.UserName = upFWCmd.getUsernameFileServer();
                                recm.Password = upFWCmd.getPasswordFileServer();
                                recm.Version = upFWCmd.getFileVersion();
                                recm.writeTo(out);
                            }
                        }
                    } else {
                        sb.setLength(0);
                        sb.append("Not found command request for: ")
                                .append(lastInform.sn);
                        System.out.println(sb);
                        log(lastInform, Level.WARN, sb.toString());
                    }
                }
                // check has EVENT_2
                if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_2)) {
                    // get performance and auto update firmware
//                    sb.setLength(0);
//                    sb.append("CPE send event: ")
//                            .append(TR069StaticParameter.EVENT_2)
//                            .append(". But ACS Servlet do nothing !");
//                    logger.debug(sb);

//                    if (AppConfig.isDisableWifiVinaphone()) {
//                        logger.info("[Disable wifi vinaphone]Start get parameter");
//                        String wifiTree = AppConfig.getPathWifi();
//                        GetParameterValues getParam = new GetParameterValues(wifiTree);
//                        getParam.writeTo(out);
//                        session.setAttribute(ATTR_EVENT_DISALBE_WIFI, EVENT_DISABLE);
//                    }
                }

                // end check event !
            }

            if (out.size() == 0) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            response.setContentLength(out.size());
            String sout = out.toString();
            logger.debug("SENT_ Message:\n" + sout);

            sout = sout.replace('\'', '"');
            //sout = sout.replace(' ', '\n');
            // Write output to stream
            response.getOutputStream().print(sout);
            logger.debug("End of processing");
        } catch (Exception ex) {
            if (ex instanceof IOException) {
                throw (IOException) ex;
            } else {
                throw new ServletException(ex.getMessage(), ex);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     *
     *
     * @param request ACSServlet request
     * @param response ACSServlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processAsync(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     *
     *
     * @param request ACSServlet request
     * @param response ACSServlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processAsync(request, response);
//        processRequest(request, response);
    }

    /**
     * Returns a short description of the ACSServlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>

    private void processAsync(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        logger.info("AsyncLongRunningServlet Start::Name="
                + Thread.currentThread().getName() + "::ID="
                + Thread.currentThread().getId());
        long timeOut = AppConfig.getAsyncContextTimeou();
        AsyncContext asyncCtx = request.startAsync();
        asyncCtx.setTimeout(timeOut); // timeout handle session
        STBNMSWorker.getInstance().processMsg(asyncCtx, this, request, response);

//        AppContextListener.submit(new AsyncRequestHandler(asyncCtx, this, request, response));
        logger.info("time doPost put to thread pool:  " + (System.currentTimeMillis() - startTime));
    }

    private Configurator RunConfigurator(HttpServletRequest request, HostsLocal host, Inform lastInform, ArrayList<TransferComplete> transferComplete) {
//        String fwpath = Util.getFirmwarePath(this);
        String fwpath = Application.getFirmwarePath();
        String localAddr = Application.getOverrideServerName();
        try {
            if (Application.IsNoNATNetwork(request.getRemoteAddr()) || localAddr == null || localAddr.equals("")) {
                localAddr = request.getLocalAddr();
            }
        } catch (UnknownHostException ex) {
        }
        String urlServer = request.getScheme() + "://" + localAddr + ":" + request.getLocalPort() + request.getContextPath();
        Configurator cfgr = new Configurator(lastInform, host.getId(), transferComplete, fwpath, urlServer, request.getSession().getId());
        cfgr.SetCallType(queueingType);
        cfgr.start();
        return cfgr;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("PUT FILE");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public ACSServlet() {
    }

    @Override
    public void init() throws ServletException {
        //setupJMS();
        Authenticator.setDefault(new ACSAuthenticator());
        InitQueueing();
    }

    @Override
    public void destroy() {
        FinishQueueing();
    }
    private static final int QUEUEING_TYPE_JMS = 1;
    private static final int QUEUEING_TYPE_OBJ = 2;
//    private int queueingType = QUEUEING_TYPE_JMS;
    private final int queueingType = QUEUEING_TYPE_OBJ;

    private void InitQueueing() throws ServletException {
        switch (queueingType) {
            case QUEUEING_TYPE_JMS:
                jmsInitQueueing();
                break;
            case QUEUEING_TYPE_OBJ:
                break;
            default:
                throw new ServletException("Not implemented");
        }
    }

    private void jmsInitQueueing() throws ServletException {
        try {
            _jms = new Jms();
        } catch (Exception e) {
            log(null, Level.ERROR, e.getMessage());
            throw new ServletException(e);
        }
    }

    private void FinishQueueing() {
        switch (queueingType) {
            case QUEUEING_TYPE_JMS:
                jmsFinishQueueing();
                break;
        }
    }

    private void jmsFinishQueueing() {
        _jms.closeJMS();
    }

    private void SendResponse(HttpSession session, Message msg) {
        switch (queueingType) {
            case QUEUEING_TYPE_JMS:
                jmsSendResponse(session, msg);
                break;
            case QUEUEING_TYPE_OBJ:
                objSendResponse(session, msg);
                break;
        }
    }

    private void objSendResponse(HttpSession session, Message msg) {
        Configurator c = (Configurator) session.getAttribute(ATTR_CONFIGURATOR);
        if (c == null) {
            System.out.println("No configurator for this session in objSendResponse");
            return;
        }
        c.SendResponse(msg);
    }

    private void jmsSendResponse(HttpSession session, Message msg) {
        Inform lastInform = (Inform) session.getAttribute(ATTR_LASTINFORM);
        try {
            if (!msg.getId().startsWith("ignorereply")) {
                log(lastInform, Level.INFO, "Send JMS reply for " + msg.getId());
                _jms.sendResponseMessage(msg, msg.getId());
            }
        } catch (JMSException e) {
            log(lastInform, Level.WARN, "Exception while sending", e);
        }
    }

    private Message ReceiveRequest(HttpSession session, Object hwid, String sn, long w) throws JMSException {
        switch (queueingType) {
            case QUEUEING_TYPE_JMS:
                return jmsReceiveRequest(session, hwid, sn, w);
            case QUEUEING_TYPE_OBJ:
                return objReceiveRequest(session, hwid, sn, w);
        }
        return null;
    }

    private Message objReceiveRequest(HttpSession session, Object hwid, String sn, long w) throws JMSException {
        Configurator c = (Configurator) session.getAttribute(ATTR_CONFIGURATOR);
        if (c == null) {
            System.out.println("No configurator for this session");
            return null;
        }
        return c.ReceiveRequest(w);
    }

    private Message jmsReceiveRequest(HttpSession session, Object hwid, String sn, long w) throws JMSException {
        MessageConsumer consumer = (MessageConsumer) session.getAttribute("consumer");
        if (consumer == null) {
//                consumer = queuesession.createConsumer(queue, "OUI='" + oui + "' AND SN='" + sn + "'");
            //String filter = "OUI='" + oui + "' AND SN='" + sn + "'";
            String filter = "HWID='" + /*hw.getId()*/ hwid + "' AND SN='" + sn + "'";

            consumer = _jms.createConsumer(filter);
            session.setAttribute("consumer", consumer);
            //log(lastInform, Level.FINEST, "Created consumer: " + filter);
        }
        ObjectMessage jm = (w == 0) ? (ObjectMessage) consumer.receiveNoWait() : (ObjectMessage) consumer.receive(w);
        if (jm != null) {
            return (Message) jm.getObject();
        }
        return null;
    }
    private Jms _jms;

    private void updateIPAddressDevice(Inform lastInform, String serialNumber) {
        // Update Firmware Version or Create new Device if Not Found
//        DeviceModelManager deviceModelManager = BeanUtils.getInstance().getBean("deviceModelManager", DeviceModelManager.class);
//        DeviceManager deviceManager = BeanUtils.getInstance().getBean("deviceManager", DeviceManager.class);
        Device device = deviceManager.getDeviceBySerialNumber(serialNumber);

        // Check device info
        if (device != null) {
            sb.setLength(0);
            sb.append(TR069StaticParameter.EVENT_4)
                    .append(", SN=")
                    .append(serialNumber)
                    .append(", MANU=")
                    .append(lastInform.Manufacturer)
                    .append(", Update Device. IP Change");
            logger.info(sb);
            device.setIpAddress(lastInform.getIPAddress());
            deviceManager.save(device);
        }
    }

    private void logSOAPMessage(Object description, SOAPMessage soapMsg) {
        if (soapMsg != null) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                soapMsg.writeTo(bos);
                String strMsg = new String(bos.toByteArray());
                sb.setLength(0);
                sb.append(description)
                        .append(", SOAPMessage: ")
                        .append(strMsg);
                logger.debug(sb);
            } catch (Exception ex) {
            }
        }
    }

    public static String getIPfromConnectionRequest(String connectionRequest) {
        String buff = connectionRequest;
        for (int i = 7; i < buff.length(); i++) {
            if (buff.charAt(i) == ':') {
                return buff.substring(7, i);
            }
        }
        return null;
    }
}
