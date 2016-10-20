/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpt.tr069.handler;

import org.apache.log4j.Logger;

/**
 *
 * @author Zan
 */
public class FactoryHandler {

    public static final String PRODUCT_TYPE_ONTV2 = "ONTv2";
    public static final String PRODUCT_TYPE_AON = "AON";
    public static final String PRODUCT_TYPE_DETECT_TYPE = "DETECT_TYPE";
    public static final String PRODUCT_TYPE_BASIC = "BASIC";
    public static final String PRODUCT_TYPE_ADSL = "ADSL_P300";

    public static final Logger logger = Logger.getLogger(FactoryHandler.class.getName());
//    public static final HashMap<String, Tr069Handler> listHandler = new HashMap(4);

//    static {
//        try {
//
//            listHandler.put(PRODUCT_TYPE_ONTV2, new ONTv2Handler());
//            listHandler.put(PRODUCT_TYPE_AON, new AONHandler());
//            listHandler.put(PRODUCT_TYPE_DETECT_TYPE, new DetectTypeHandler());
//            listHandler.put(PRODUCT_TYPE_BASIC, new BasicHandler());
//        } catch (Exception ex) {
//            logger.error("FactoryHandler.static", ex);
//        }
//    }
    public static Tr069Handler getHandlerByProductType(String type) {
        if (PRODUCT_TYPE_ONTV2.equalsIgnoreCase(type)) {

            return new ONTv2Handler();
        } else if (PRODUCT_TYPE_AON.equalsIgnoreCase(type)) {

            return new AONHandler();
        } else if (PRODUCT_TYPE_ADSL.equalsIgnoreCase(type)) {

            return new ADSLHandler();
        } else {

            return new BasicHandler();
        }
    }

    public static Tr069Handler getDetectTypeHandler() {
        return new DetectTypeHandler();
    }

}
