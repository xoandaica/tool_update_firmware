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
public class IPoEWanServiceStatic extends IPoEWanServiceDHCP {

    private String externalIPAddress;
    private String subnetMask;
    private String defaultGateway;
    private String dnsIfName;
    private String dnsServers;

    public IPoEWanServiceStatic(
            String serviceName,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled
    ) {
        super(serviceName, vlanMux802_1Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled);
    }

    public IPoEWanServiceStatic(
            String serviceName,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String externalIPAddress,
            String subnetMask,
            String defaultGateway,
            String dnsIfName,
            String dnsServers
    ) {
        super(serviceName, vlanMux802_1Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled);
        this.externalIPAddress = externalIPAddress;
        this.subnetMask = subnetMask;
        this.defaultGateway = defaultGateway;
        this.dnsIfName = dnsIfName;
        this.dnsServers = dnsServers;
    }

    @Override
    public String getAddressingType() {
        return "Static";
    }

    public String getExternalIPAddress() {
        return externalIPAddress;
    }

    public void setExternalIPAddress(String externalIPAddress) {
        this.externalIPAddress = externalIPAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getDefaultGateway() {
        return defaultGateway;
    }

    public void setDefaultGateway(String defaultGateway) {
        this.defaultGateway = defaultGateway;
    }

    public String getDnsIfName() {
        return dnsIfName;
    }

    public void setDnsIfName(String dnsIfName) {
        this.dnsIfName = dnsIfName;
    }

    public String getDnsServers() {
        return dnsServers;
    }

    public void setDnsServers(String dnsServers) {
        this.dnsServers = dnsServers;
    }

}
