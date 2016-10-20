/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpt.tr069.handler;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import static net.vnpt.tr069.handler.BasicHandler.ATTR_LASTINFORM;
import static net.vnpt.tr069.handler.BasicHandler.ATTR_LAST_EVENT;
import static net.vnpt.tr069.handler.BasicHandler.ATTR_PRODUCT_TYPE;
import net.vnpttech.collection.openacs.Message;
import net.vnpttech.collection.openacs.common.Properties;
import net.vnpttech.collection.openacs.database.DatabaseManager;
import net.vnpttech.collection.openacs.message.GetParameterValues;
import net.vnpttech.collection.openacs.message.GetParameterValuesResponse;
import net.vnpttech.collection.openacs.message.Inform;
import net.vnpttech.collection.openacs.message.InformResponse;
import net.vnpttech.collection.openacs.message.SetParameterValuesResponse;
import net.vnpttech.collection.openacs.message.TransferComplete;
import net.vnpttech.collection.openacs.message.TransferCompleteResponse;
import net.vnpttech.collection.openacs.mycommand.TR069StaticParameter;
import org.apache.log4j.Logger;
import org.appfuse.model.AdslDevice;
import org.appfuse.model.Product;

/**
 *
 * @author Zan
 */
public class DetectTypeHandler extends BasicHandler {

    private static final Logger logger = Logger.getLogger(DetectTypeHandler.class.getName());

    @Override
    public String getHandlerName() {
        return FactoryHandler.PRODUCT_TYPE_DETECT_TYPE;
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

            if (lastInform == null) { // ban tin dau tien join vao he thong

                if ("Inform".equals(reqname)) {

                    processInform(request, response, msg, out);
                } else {

                    logger.warn("first message is not inform: " + reqname);
                }
            } else {

                if ("GetParameterValuesResponse".equals(reqname)) {

                    processGetParameterValuesResponse(request, response, msg, out);
                } else if ("SetParameterValuesResponse".equals(reqname)) {

                    processSetParameterValuesResponse(request, response, msg, out);
                } else if ("TransferComplete".equals(reqname)) {

                    processTranferComplete(request, response, msg, out);
                } else {

                    logger.warn("weird message request: " + reqname);
                }
            }

        } else { // process event

            if (lastInform == null) {

                logger.warn("lastInform is null");

            } else {

                if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_0)) {

                    // receive post empty, process event 0
                    processEvent0Bootstrap(request, response, lastInform, out);
                } else if (hasEvent(lastInform.getEvents().iterator(), TR069StaticParameter.EVENT_1)) {

                    processEvent1Boot(request, response, lastInform, out);
                } else {
                    // print list event for debug
                    StringBuilder sb = new StringBuilder(50);
                    Iterator<Map.Entry<String, String>> iter = lastInform.getEvents().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, String> temp = iter.next();
                        sb.append(temp.getKey()).append(" ");
                    }

                    logger.warn("weird event, sn= " + lastInform.sn);
                    logger.warn("Events: " + lastInform.getEvents() == null ? "null" : sb.toString());
                }
            }
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
        long startTime = System.currentTimeMillis();
        Inform lastInform = (Inform) msg;
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(30); // timeout for session
        session.setAttribute(ATTR_LASTINFORM, lastInform);

        if (lastInform.sn != null) {

            // cap nhat product type vao session de tuy chon handler phu hop
            String productName = getProductTypeBySerial(lastInform.sn);
            if (productName != null) {
                session.setAttribute(ATTR_PRODUCT_TYPE, productName);
            }

            InformResponse resp = new InformResponse(lastInform.getId(), 1);
            resp.writeTo(out);
        } else { // truong hop serial number cua ban tin inform null thi reject

            logger.warn("inform message with serial number null");
        }

        System.out.println("processInform: " + (System.currentTimeMillis() - startTime));
    }

    @Override
    public void processGetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        StringBuilder sb = new StringBuilder(100);
        HttpSession session = request.getSession();
        GetParameterValuesResponse dataReceive = (GetParameterValuesResponse) msg;
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        if (TR069StaticParameter.EVENT_0.equals(lastEvent) || TR069StaticParameter.EVENT_1.equals(lastEvent)) {
            String modelName = dataReceive.values.get(TR069StaticParameter.DeviceInfoModelName);

            long productID = DatabaseManager.getProductIDFromModelname(modelName);
            Product product = DatabaseManager.PRODUCT_DAO.get(productID);
            String productName = product.getProductName();

            sb.append("Model Name: ").append(modelName).append("|").append("Product name: ").append(productName);
            logger.info(sb.toString());

            session.setAttribute(ATTR_PRODUCT_TYPE, productName);

            Tr069Handler newHandler = FactoryHandler.getHandlerByProductType(productName);
            logger.info("detect type: " + newHandler.getHandlerName());
            newHandler.processGetParameterValuesResponse(request, response, msg, out);
        }
    }

    /**
     * ham nay mat tam 30ms
     *
     * @param serialNumber
     * @return
     */
    protected String getProductTypeBySerial(String serialNumber) {
//        long startTime = System.currentTimeMillis();
        try {

            AdslDevice device = DatabaseManager.ADSL_DEVICE_DAO.getByAdslSerialNumber(serialNumber);
            if (device == null) {
                return null;
            }

            if (device.getProductID() == null) {
                logger.warn("device with null productID: " + serialNumber);
                return null;
            }

            Product product = DatabaseManager.PRODUCT_DAO.get(device.getProductID());
            if (product == null) {
                return null;
            }

            String productName = product.getProductName();
            return productName;
        } catch (Exception ex) {

            logger.warn("can not getProductTypeBySerial: " + serialNumber, ex);
            return null;
        } finally {
//            System.out.println("getProductTypeBySerial: " + (System.currentTimeMillis() - startTime));
        }

    }

    @Override
    public void processEvent1Boot(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        logger.info("processEvent1Boot");
        HttpSession session = request.getSession();
        session.setAttribute(ATTR_LAST_EVENT, TR069StaticParameter.EVENT_1);
        if (Properties.isEnableSetInformTime()) {
            setInformTime(out);
        } else {

            // get device info to get model name -> detect handler
            List<String> listParams = new ArrayList<String>(1);
            listParams.add(TR069StaticParameter.DeviceInfo);
            GetParameterValues getParaVl = new GetParameterValues(listParams);
            getParaVl.writeTo(out);
        }
    }

    @Override
    public void processSetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {
        HttpSession session = request.getSession();
        String lastEvent = (String) session.getAttribute(ATTR_LAST_EVENT);

        // kiendt - bo sung xu ly set inform time - 02082016 - start 
        if (Properties.isEnableSetInformTime()) {
            if (TR069StaticParameter.EVENT_0.equals(lastEvent) || TR069StaticParameter.EVENT_1.equals(lastEvent)) {
                SetParameterValuesResponse setValuesResponse = (SetParameterValuesResponse) msg;
                if (setValuesResponse.Status == 0) {
                    logger.info("Set Inform time success!!!");
                }

                List<String> listParams = new ArrayList<String>(1);
                listParams.add(TR069StaticParameter.DeviceInfo);
                GetParameterValues getParaVl = new GetParameterValues(listParams);
                getParaVl.writeTo(out);

            } else {

                logger.warn("weird event here: " + lastEvent);
            }
        }
    }

    @Override
    public void processTranferComplete(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception {

        TransferComplete tc = (TransferComplete) msg;
        TransferCompleteResponse tr = new TransferCompleteResponse(tc.getId());
        tr.writeTo(out);
    }

}
