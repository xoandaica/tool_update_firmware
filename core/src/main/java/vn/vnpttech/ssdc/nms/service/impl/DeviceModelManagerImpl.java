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
package vn.vnpttech.ssdc.nms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vn.vnpttech.ssdc.nms.dao.DeviceModelDao;
import vn.vnpttech.ssdc.nms.service.DeviceModelManager;
import vn.vnpttech.ssdc.nms.model.DeviceModel;

/**
 *
 * @author longdq
 */
public class DeviceModelManagerImpl extends GenericManagerImpl<DeviceModel, Long> implements DeviceModelManager {

    @Autowired
    DeviceModelDao deviceModelDao;

    public DeviceModelManagerImpl() {

    }

    public DeviceModelManagerImpl(DeviceModelDao deviceModelDao) {
        super(deviceModelDao);
    }

    public DeviceModelDao getDeviceModelDao() {
        return deviceModelDao;
    }

    public void setDeviceModelDao(DeviceModelDao deviceModelDao) {
        this.deviceModelDao = deviceModelDao;
    }

    @Override
    public DeviceModel getModelByName(String modelName) {
        return deviceModelDao.getModelByName(modelName);
    }

}
