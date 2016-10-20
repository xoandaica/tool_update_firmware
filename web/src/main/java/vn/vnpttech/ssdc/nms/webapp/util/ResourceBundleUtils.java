/*
 * Copyright 2015 Pivotal Software, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.vnpttech.ssdc.nms.webapp.util;

import java.util.ResourceBundle;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author longdq
 */
public class ResourceBundleUtils {

    static private ResourceBundle rb = null;

    /**
     * Creates a new instance of ResourceBundleUtils
     */
    public ResourceBundleUtils() {
    }

    /**
     * Common function
     *
     * @yourName
     * @param key
     * @return
     */
    public static String getName(String key) {
        rb = ResourceBundle.getBundle("ApplicationResources");
        String temp = "";
        try {
            temp = rb.getString(key);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return temp;
    }

    /**
     *
     * @param key
     * @param replace
     * @return
     */
    public static String getName(String key, String replace) {
        rb = ResourceBundle.getBundle("ApplicationResources");
        String temp = "";
        try {
            temp = rb.getString(key);
            temp = temp.replace("{0}", replace);
        } catch (Exception e) {
        }
        return temp;
    }

    /**
     *
     * @return
     */
    public static String getFTPServer() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("ftpserver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getHttpUrl() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("httpUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getSaveDir() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("saveDir");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getReportDir() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("reportDir");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getAuthenticationUrl() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("authenticationUrl");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getACSLocator() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("acsLocator");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String getFirmwareDns() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("firmwareDNS");
        } catch (Exception e) {
        }
        return temp;
    }

    public static String getFirmwareUploadDir() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("firmwareDir");
        } catch (Exception e) {
        }
        return temp;
    }

    public static String getAcsUrl() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("acsUrl");
        } catch (Exception e) {
        }
        return temp;
    }

    public static String getInterval() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("interval");
            if (StringUtils.isBlank(temp)) {
                temp = "900";
            }
        } catch (Exception e) {
        }
        return temp;
    }
//    corePoolSize=50
//maxPoolSize=100
//timeOutQueue=600000
//maxNumberQueue=200000

    public static int getCorePoolSize() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("corePoolSize");
            if (StringUtils.isBlank(temp)) {
                temp = "50";
            }
        } catch (Exception e) {
        }
        return Integer.valueOf(temp);
    }

    public static int getMaxPoolSize() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("maxPoolSize");
            if (StringUtils.isBlank(temp)) {
                temp = "50";
            }
        } catch (Exception e) {
        }
        return Integer.valueOf(temp);
    }

    public static int getTimeOutForQueue() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("timeOutQueue");
            if (StringUtils.isBlank(temp)) {
                temp = "50";
            }
        } catch (Exception e) {
        }
        return Integer.valueOf(temp);
    }

    public static int getMaxNumberInQueue() {
        rb = ResourceBundle.getBundle("config");
        String temp = "";
        try {
            temp = rb.getString("maxNumberQueue");
            if (StringUtils.isBlank(temp)) {
                temp = "50";
            }
        } catch (Exception e) {
        }
        return Integer.valueOf(temp);
    }

}
