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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.Firmware;

/**
 *
 * @author longdq
 */
public class SimpleThreadPool {

    protected final transient Log log = LogFactory.getLog(getClass());

    public SimpleThreadPool() {
    }

    public void upgradeFirmwarePool(List<Device> devices, HashMap<String, Firmware> hmFirmware) {
        if (devices != null && !devices.isEmpty()) {
//            int poolsize = devices.size();

//            ExecutorService executor = Executors.newFixedThreadPool(poolsize);
            for (Device d : devices) {
                Firmware f = (Firmware) hmFirmware.get(d.getDeviceModel().getName());
                Runnable worker = new UpgradeFirmwareThread(d, f);
                ThreadPoolExecutor.getInstance().processMsg(worker);
            }
//            executor.shutdown();
//            while (!executor.isTerminated()) {
//            }
            log.info("Finished all task upgrad firmware");
        } else {
            log.info("Number of task upragde firmware = 0 ");
        }
    }

    public void setGwDnsPool(List<Device> devices) {
        if (devices != null && devices.size() > 0) {
//            int poolsize = devices.size();

//            ExecutorService executor = Executors.newFixedThreadPool(poolsize);
            for (Device d : devices) {
                Runnable worker = new GwDnsThread(d);
                ThreadPoolExecutor.getInstance().processMsg(worker);
            }
//            executor.shutdown();
//            while (!executor.isTerminated()) {
//            }
            log.info("Finished all task Set GW and DNS ");
        } else {
            log.info("Number of task Set GW and DNS = 0 ");
        }
    }

    public void setAcsUrlPool(List<Device> devices) {
        if (devices != null && !devices.isEmpty()) {
//            int poolsize = devices.size();

//            ExecutorService executor = Executors.newFixedThreadPool(poolsize);
            for (Device d : devices) {
                Runnable worker = new AcsUrlThread(d);
                ThreadPoolExecutor.getInstance().processMsg(worker);
            }
//            executor.shutdown();
//            while (!executor.isTerminated()) {
//            }
            log.info("Finished all task change ACS URL ");
        } else {
            log.info("Number of task change ACS URL = 0 ");
        }
    }

    public void setStaticRoute(List<Device> devices, String destIp, String gateway, String subnetMark, String interfaceName) {
        if (devices != null && !devices.isEmpty()) {
//            int poolsize = devices.size();

//            ExecutorService executor = Executors.newFixedThreadPool(poolsize);
            for (Device d : devices) {
                Runnable worker = new StaticRouteThread(d, destIp, gateway, subnetMark, interfaceName);
                ThreadPoolExecutor.getInstance().processMsg(worker);
            }
//            executor.shutdown();
//            while (!executor.isTerminated()) {
//            }
            log.info("Finished all task  Set Static Route ");
        } else {
            log.info("Number of task Set Static Route = 0 ");
        }
    }
}
