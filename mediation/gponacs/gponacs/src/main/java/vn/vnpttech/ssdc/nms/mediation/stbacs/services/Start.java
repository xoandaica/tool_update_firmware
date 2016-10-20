/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.services;

import vn.vnpttech.ssdc.nms.mediation.stbacs.main.ACSWebServicePublisher;
import vn.vnpttech.ssdc.nms.mediation.stbacs.main.ACSServletPublisher;

/**
 *
 * @author Vunb
 * @date Jun 4, 2014
 * @update Jun 4, 2014
 */
public class Start {

    public static void main(String[] args) throws Exception {
        //BasicConfigurator.configure();
        ACSServletPublisher.publishACS();
        ACSWebServicePublisher.publishWebservice();
    }
    
}
