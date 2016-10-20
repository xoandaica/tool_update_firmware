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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vn.vnpttech.ssdc.nms.acs.gpontool.services.ACSServiceImplService;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.Firmware;

/**
 *
 * @author longdeviceq
 */
public class UpgradeFirmwareThread implements Runnable {

    private Device device;
    private Firmware firmware;

    protected final transient Log log = LogFactory.getLog(getClass());

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Firmware getFirmware() {
        return firmware;
    }

    public void setFirmware(Firmware firmware) {
        this.firmware = firmware;
    }

    public UpgradeFirmwareThread(Device d, Firmware f) {
        this.device = d;
        this.firmware = f;
        Thread.currentThread().setName(d.getSerialNumber());
    }

    @Override
    public void run() {

        log.info(Thread.currentThread().getName() + " -----------------Start. Upgrade firmware -------------------");
        processUpgradeFirmware();
        log.info(Thread.currentThread().getName() + " ------------------- End.Upgrad firmware---------------------");
    }

    private void processUpgradeFirmware() {
        try {
            ACSServiceImplService acs = new ACSServiceImplService();

            if (firmware != null && !firmware.getVersion().equalsIgnoreCase(device.getFirmwareVersion())) {
                log.info("Current Firmware: " + device.getFirmwareVersion());
                log.info("Up to Firmware : " + firmware.getVersion());
                log.debug(device.getSerialNumber() + " calling web service......");
                acs.getACSServiceImplPort().upgradeFirmware(device.getSerialNumber(), device.getConnectionReq(), firmware.getFirmwarePath(), firmware.getVersion(), "", "");
                log.debug(device.getSerialNumber() + " called web service......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
