/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.BrigdingGroup;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.DeviceInterface;

/**
 *
 * @author Vunb
 * @date Aug 23, 2014
 * @update Aug 23, 2014
 */
public class ModemInterfaceGroupResponse {

    private int errorCode;
    private String message;
    private ArrayList<BrigdingGroup> brigdingGroup = new ArrayList<BrigdingGroup>();
    private ArrayList<DeviceInterface> deviceInterfaces = new ArrayList<DeviceInterface>();

    public ModemInterfaceGroupResponse() {
        this.errorCode = 0;
        this.message = "(not provided)";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElementWrapper(name = "InterfaceGroups")
    @XmlElement(name = "InterfaceGroup")
    public ArrayList<BrigdingGroup> getBrigdingGroup() {
        return brigdingGroup;
    }

    public void setBrigdingGroup(ArrayList<BrigdingGroup> brigdingGroup) {
        this.brigdingGroup = brigdingGroup;
    }

    @XmlElementWrapper(name = "DeviceInterfaces")
    @XmlElement(name = "DeviceInterface")
    public ArrayList<DeviceInterface> getDeviceInterfaces() {
        return deviceInterfaces;
    }

    public void setDeviceInterfaces(ArrayList<DeviceInterface> deviceInterfaces) {
        this.deviceInterfaces = deviceInterfaces;
    }
    
    public void addBrigdingGroup(BrigdingGroup group) {
        if (group != null) {
            this.brigdingGroup.add(group);
        }
    }

}
