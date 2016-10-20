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
import vn.vnpttech.ssdc.nms.webapp.util.ResourceBundleUtils;

/**
 *
 * @author longdq
 */
public class AcsUrlThread implements Runnable {

    private Device device;

    protected final transient Log log = LogFactory.getLog(getClass());

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public AcsUrlThread(Device device) {
        this.device = device;
        Thread.currentThread().setName(device.getSerialNumber());
    }

    @Override
    public void run() {
        log.info(device.getSerialNumber() + " Start.... Change AcsUrl-----------------");
        ACSServiceImplService acs = new ACSServiceImplService();
        log.info(ResourceBundleUtils.getAcsUrl() + "-----------------");
        log.info(device.getConnectionReq() + "-----------------");

        acs.getACSServiceImplPort().setIntervalAcsUrl(device.getSerialNumber(), ResourceBundleUtils.getAcsUrl(), device.getConnectionReq(), Integer.parseInt(ResourceBundleUtils.getInterval()));
        log.info(device.getSerialNumber() + " End.... Change AcsUrl-----------------");
        
        // 
        ThreadPoolExecutor.getInstance().showQueue();
    }

}
