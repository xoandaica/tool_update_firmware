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
package vn.vnpttech.ssdc.nms.threadpool;

import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vn.vnpttech.ssdc.nms.acs.gpontool.services.ACSServiceImplService;

import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.webapp.util.ResourceBundleUtils;

/**
 *
 * @author longdq
 */
public class StaticRouteThread implements Runnable {

    private Device device;
    private String destIp;
    private String gateway;
    private String subnetMark;
    private String interfaceName;

    protected final transient Log log = LogFactory.getLog(getClass());

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getSubnetMark() {
        return subnetMark;
    }

    public void setSubnetMark(String subnetMark) {
        this.subnetMark = subnetMark;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public StaticRouteThread(Device device) {
        this.device = device;
        Thread.currentThread().setName(device.getSerialNumber());
    }

    public StaticRouteThread(Device device, String destIp, String gateway, String subnetMark, String interfaceName) {
        this.device = device;
        this.destIp = destIp;
        this.gateway = gateway;
        this.subnetMark = subnetMark;
        this.interfaceName = interfaceName;
    }

    @Override
    public void run() {
        log.info(device.getSerialNumber() + " --------------Set Static Route Start-----------------");
        ACSServiceImplService acs = new ACSServiceImplService();
        log.debug(ResourceBundleUtils.getAcsUrl() + "-----------------");
        log.debug(device.getConnectionReq() + "-----------------");

//        acs.getACSServiceImplPort().setIntervalAcsUrl(device.getSerialNumber(), ResourceBundleUtils.getAcsUrl(), device.getConnectionReq(), Integer.parseInt(ResourceBundleUtils.getInterval()));
       acs.getACSServiceImplPort().setStaticRoute(destIp, subnetMark, interfaceName, gateway, device.getConnectionReq(), device.getSerialNumber());
        log.info(device.getSerialNumber() + " -----------------------Set Static Route End-----------------");
    }

}
