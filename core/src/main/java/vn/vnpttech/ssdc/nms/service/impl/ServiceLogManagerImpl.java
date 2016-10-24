/*
 * Copyright 2016 Pivotal Software, Inc..
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
package vn.vnpttech.ssdc.nms.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vnpttech.ssdc.nms.dao.ServiceLogDao;
import vn.vnpttech.ssdc.nms.model.ServiceLog;
import vn.vnpttech.ssdc.nms.service.ServiceLogManager;

/**
 *
 * @author SSDC
 */
public class ServiceLogManagerImpl extends GenericManagerImpl<ServiceLog, Long> implements ServiceLogManager {

    @Autowired
    ServiceLogDao serviceLogDao;

    public ServiceLogDao getDeviceDao() {
        return serviceLogDao;
    }

    public void setDeviceDao(ServiceLogDao serviceLogDao) {
        this.serviceLogDao = serviceLogDao;
    }

    public ServiceLogManagerImpl() {
    }

    public ServiceLogManagerImpl(ServiceLogDao serviceLogDao) {
        super(serviceLogDao);
    }

    @Override
    public List<ServiceLog> searchLogBySerial(String serialNumber) {
        return serviceLogDao.searchLogBySerial(serialNumber);
    }
}
