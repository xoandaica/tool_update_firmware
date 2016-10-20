/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

/**
 *
 * @author Vunb
 * @date Oct 28; 2014
 * @update Oct 28; 2014
 */
public class IPoEWanServiceDHCP {

    String serviceName;
    String addressingType;
    int vlanMux8021Priority;
    int vlanMuxID;
    boolean ipV4Enabled;
    boolean ipV6Enabled;
    boolean natEnabled;
    boolean firewallEnabled;

    public IPoEWanServiceDHCP(
            String serviceName,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled
    ) {
        this.serviceName = serviceName;
        this.vlanMux8021Priority = vlanMux802_1Priority;
        this.vlanMuxID = vlanMuxID;
        this.ipV4Enabled = ipV4Enabled;
        this.ipV6Enabled = ipV6Enabled;
        this.natEnabled = natEnabled;
        this.firewallEnabled = firewallEnabled;
        this.addressingType = "DHCP";
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAddressingType() {
        return addressingType;
    }

    public void setAddressingType(String addressingType) {
        this.addressingType = addressingType;
    }

    public int getVlanMux8021Priority() {
        return vlanMux8021Priority;
    }

    public void setVlanMux8021Priority(int vlanMux8021Priority) {
        this.vlanMux8021Priority = vlanMux8021Priority;
    }

    public int getVlanMuxID() {
        return vlanMuxID;
    }

    public void setVlanMuxID(int vlanMuxID) {
        this.vlanMuxID = vlanMuxID;
    }

    public boolean isIpV4Enabled() {
        return ipV4Enabled;
    }

    public void setIpV4Enabled(boolean ipV4Enabled) {
        this.ipV4Enabled = ipV4Enabled;
    }

    public boolean isIpV6Enabled() {
        return ipV6Enabled;
    }

    public void setIpV6Enabled(boolean ipV6Enabled) {
        this.ipV6Enabled = ipV6Enabled;
    }

    public boolean isNatEnabled() {
        return natEnabled;
    }

    public void setNatEnabled(boolean natEnabled) {
        this.natEnabled = natEnabled;
    }

    public boolean isFirewallEnabled() {
        return firewallEnabled;
    }

    public void setFirewallEnabled(boolean firewallEnabled) {
        this.firewallEnabled = firewallEnabled;
    }

}
