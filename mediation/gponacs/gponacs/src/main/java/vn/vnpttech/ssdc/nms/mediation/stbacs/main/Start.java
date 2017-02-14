/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jivesoftware.smack.XMPPException;

/**
 * Main Entry
 *
 * @author Vunb
 * @date Jun 13, 2015
 * @update Jun 13, 2015
 */
public class Start {

    private static final StringBuilder sb = new StringBuilder();
    private static final Logger logger = Logger.getLogger(Start.class);

//    static XmppManager xmpp;

    public static void main(String[] args) throws XMPPException, Exception {

        try {
            PropertyConfigurator.configure("E:/NMS/gnmstool/mediation/gponacs/etc/log4j.conf");
            Thread.currentThread().setName("GPON ACS Wrapper");
        } catch (Exception ex) {
            logger.info("Could not set Thread name", ex);
        }

        logger.info("Starting GPON ACS Servlet ...");
        ACSServletPublisher.publishACS();
        logger.info("Starting ACS Webservice ...");
        ACSWebServicePublisher.publishWebservice();
      //  logger.info("Starting ACS XMPP master ...");

    }
}
