/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

/**
 *
 * @author Vunb
 * @date Jun 9, 2014
 * @update Jun 9, 2014
 */
public class WebServiceConfig {

    public static final String WS_NAME = "APConfig";
    public static final String WS_SERVICE_NAME = "APConfig";
    public static final String WS_TARGET_NAMESPACE = "http://client.ws.acs.vnpttech.net";

    public static final String WS_NAMESPACE_APMANAGEMENT = WS_TARGET_NAMESPACE + "/apmgr";
    public static final String WS_NAMESPACE_WLAN_CONFIG = WS_TARGET_NAMESPACE + "/wlanconfig";
    public static final String WS_NAMESPACE_DEVICE_INFO = WS_TARGET_NAMESPACE + "/deviceinfo";

    public static class ErrorCode {

        public static final int SUCCESS = 0;
        // System fail
        public static final int SYSTEM_FAIL = -1;
        // No connection to the device
        public static final int CPE_CONNECTION_ERROR = 1;
        // ERROR configure the device
        public static final int CPE_CONFIG_ERROR = 2;
        // Authentication fail
        public static final int AUTHENTICATION_FAIL = 3;
        // No connection
        public static final int CONNECTION_ERROR = 4;
        // Already request to the device
        public static final int CPE_ENQUEUED_REQUEST = 5;
        // Request timeout
        public static final int CPE_REQUEST_TIMEOUT = 6;
    }

    public static class Message {

        public static final String SUCCESS = "Success";
        public static final String FAILURE = "Failure";
        public static final String Error = "Error";

    }
}
