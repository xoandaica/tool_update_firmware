/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

/**
 *
 * @author Vunb
 * @date Mar 5, 2015
 * @update Mar 5, 2015
 */
public class WirelessSecurity {

    public static class DataTree {

        public static final String Root = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_BROADCOM_COM_WlanAdapter.WlVirtIntfCfg";
        public static final String WlSsid = "WlSsid";
        public static final String WlAuthMode = "WlAuthMode";
        public static final String WlWpaGTKRekey = "WlWpaGTKRekey";
        public static final String WlRadiusServerIP = "WlRadiusServerIP";
        public static final String WlRadiusKey = "WlRadiusKey";
        public static final String WlRadiusPort = "WlRadiusPort";
        public static final String WlWpa = "WlWpa";
        public static final String WlWep = "WlWep";
        public static final String WlPreauth = "WlPreauth";
        public static final String WlNetReauth = "WlNetReauth";
    }
    private String instance;
    private String wlSsid;
    private String wlAuthMode;
    private int wlWpaGTKRekey;
    private String wlRadiusServerIP;
    private String wlRadiusKey;
    private int wlRadiusPort;
    private String wlWpa;
    private String wlWep;
    private int wlPreauth;
    private int wlNetReauth;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getWlSsid() {
        return wlSsid;
    }

    public void setWlSsid(String wlSsid) {
        this.wlSsid = wlSsid;
    }

    public String getWlAuthMode() {
        return wlAuthMode;
    }

    public void setWlAuthMode(String wlAuthMode) {
        this.wlAuthMode = wlAuthMode;
    }

    public int getWlWpaGTKRekey() {
        return wlWpaGTKRekey;
    }

    public void setWlWpaGTKRekey(int wlWpaGTKRekey) {
        this.wlWpaGTKRekey = wlWpaGTKRekey;
    }

    public void setWlWpaGTKRekey(String wlWpaGTKRekey) {
        if (wlWpaGTKRekey != null && wlWpaGTKRekey.length() > 0) {
            this.wlWpaGTKRekey = Integer.parseInt(wlWpaGTKRekey);
        }
    }

    public String getWlRadiusServerIP() {
        return wlRadiusServerIP;
    }

    public void setWlRadiusServerIP(String wlRadiusServerIP) {
        this.wlRadiusServerIP = wlRadiusServerIP;
    }

    public String getWlRadiusKey() {
        return wlRadiusKey;
    }

    public void setWlRadiusKey(String wlRadiusKey) {
        this.wlRadiusKey = wlRadiusKey;
    }

    public int getWlRadiusPort() {
        return wlRadiusPort;
    }

    public void setWlRadiusPort(int wlRadiusPort) {
        this.wlRadiusPort = wlRadiusPort;
    }

    public void setWlRadiusPort(String wlRadiusPort) {
        if (wlRadiusPort != null && wlRadiusPort.length() > 0) {
            this.wlRadiusPort = Integer.parseInt(wlRadiusPort);
        }
    }

    public String getWlWpa() {
        return wlWpa;
    }

    public void setWlWpa(String wlWpa) {
        this.wlWpa = wlWpa;
    }

    public String getWlWep() {
        return wlWep;
    }

    public void setWlWep(String wlWep) {
        this.wlWep = wlWep;
    }

    public int getWlPreauth() {
        return wlPreauth;
    }

    public void setWlPreauth(int wlPreauth) {
        this.wlPreauth = wlPreauth;
    }

    public void setWlPreauth(String wlPreauth) {
        if (wlPreauth != null && wlPreauth.length() > 0) {
            this.wlPreauth = Integer.parseInt(wlPreauth);
        }
    }

    public int getWlNetReauth() {
        return wlNetReauth;
    }

    public void setWlNetReauth(int wlNetReauth) {
        this.wlNetReauth = wlNetReauth;
    }

    public void setWlNetReauth(String wlNetReauth) {
        if (wlNetReauth != null && wlNetReauth.length() > 0) {
            this.wlNetReauth = Integer.parseInt(wlNetReauth);
        }
    }

}
