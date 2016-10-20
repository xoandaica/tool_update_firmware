/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

/**
 *
 * @author Vunb
 * @date Aug 23, 2014
 * @update Aug 23, 2014
 */

public class DeviceInterface {
    
    private String name;
    private String ifName;
    private String ifGroupKey; // --> FilterBridgeReference
    private String ifReference;
    private String ifType;
    private String ifKey;
    private String filterKey;

    public String getIfGroupKey() {
        return ifGroupKey;
    }

    public void setIfGroupKey(String ifGroupKey) {
        this.ifGroupKey = ifGroupKey;
    }

    public String getIfReference() {
        return ifReference;
    }

    public void setIfReference(String ifReference) {
        this.ifReference = ifReference;
    }

    public String getIfType() {
        return ifType;
    }

    public void setIfType(String ifType) {
        this.ifType = ifType;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public String getIfKey() {
        return ifKey;
    }

    public void setIfKey(String ifKey) {
        this.ifKey = ifKey;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
