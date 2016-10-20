/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

import java.util.ArrayList;

/**
 *
 * @author Vunb
 * @date Aug 23, 2014
 * @update Aug 23, 2014
 */
public class ModemInterfaceGroups {

    private ArrayList<BrigdingGroup> brigdingGroup = new ArrayList<BrigdingGroup>();
    private ArrayList<DeviceInterface> deviceInterfaces = new ArrayList<DeviceInterface>();

    public ArrayList<BrigdingGroup> getBrigdingGroup() {
        return brigdingGroup;
    }

    public void setBrigdingGroup(ArrayList<BrigdingGroup> brigdingGroup) {
        this.brigdingGroup = brigdingGroup;
    }

    public ArrayList<DeviceInterface> getDeviceInterfaces() {
        return deviceInterfaces;
    }

    public void setDeviceInterfaces(ArrayList<DeviceInterface> deviceInterfaces) {
        this.deviceInterfaces = deviceInterfaces;
    }

}
