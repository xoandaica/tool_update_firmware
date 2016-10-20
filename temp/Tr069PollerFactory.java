/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpttech.collection.openacs.poller;

import java.util.HashMap;

/**
 *
 * @author Windows 7
 */
public class Tr069PollerFactory {

    private static Tr069PollerFactory instance = null;
    private HashMap<String, String> mapDeviceType = new HashMap<String, String>();
    private final HashMap<String, TR069Poller> mapTr069Client = new HashMap<String, TR069Poller>();

    public static synchronized Tr069PollerFactory getInstance() {
        if (instance == null) {
            instance = new Tr069PollerFactory();
            System.out.println("[Init Tr069 Factory] Init success");
        }
        return instance;
    }

    public Tr069PollerFactory() {
//        mapDeviceType.put("96318REF_P300_Broadcom_96318REF_P300", "GPON");
        mapDeviceType.put("96318REF_P300", "ADSL");

    }

    public TR069Poller getTr069Class(String productType, String serialNumber, String cpeUrl) {
        TR069Poller tr069Poller = null;
        if (productType.equalsIgnoreCase("ADSL_P300")) {
            tr069Poller = new TR069ADSLPoller("System", serialNumber, cpeUrl);
        } else if (productType.equalsIgnoreCase("ONTv2")) {
            tr069Poller = new TR069ONTPoller("System", serialNumber, cpeUrl);
        } else if (productType.equalsIgnoreCase("AON")) {
            tr069Poller = new TR069ONTPoller("System", serialNumber, cpeUrl);
        }
        return tr069Poller;
    }
}
