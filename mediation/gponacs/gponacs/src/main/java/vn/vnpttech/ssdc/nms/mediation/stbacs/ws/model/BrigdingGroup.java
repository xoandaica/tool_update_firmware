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
public class BrigdingGroup {

    private String index;
    private String BridgeKey;
    private String BridgeName;
    private String BridgeEnable;
    
    private DeviceInterface deviceInterface;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getBridgeEnable() {
        return BridgeEnable;
    }

    public void setBridgeEnable(String BridgeEnable) {
        this.BridgeEnable = BridgeEnable;
    }

    public String getBridgeKey() {
        return BridgeKey;
    }

    public void setBridgeKey(String BridgeKey) {
        this.BridgeKey = BridgeKey;
    }

    public String getBridgeName() {
        return BridgeName;
    }

    public void setBridgeName(String BridgeName) {
        this.BridgeName = BridgeName;
    }

    public String getBridgeStatus() {
        return BridgeStatus;
    }

    public void setBridgeStatus(String BridgeStatus) {
        this.BridgeStatus = BridgeStatus;
    }

    public String getVlanId() {
        return vlanId;
    }

    public void setVlanId(String vlanId) {
        this.vlanId = vlanId;
    }
    private String BridgeStatus;
    private String vlanId;

}
