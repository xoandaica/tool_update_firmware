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

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vnpttech.ssdc.nms.dao.DeviceDao;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.service.DeviceManager;
import vn.vnpttech.ssdc.nms.model.Policy;

/**
 *
 * @author Dell
 */
public class DeviceManagerImpl extends GenericManagerImpl<Device, Long> implements DeviceManager {

    @Autowired
    DeviceDao deviceDao;

    public DeviceDao getDeviceDao() {
        return deviceDao;
    }

    public void setDeviceDao(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    public DeviceManagerImpl() {
    }

    public DeviceManagerImpl(DeviceDao deviceDao) {
        super(deviceDao);
    }

    @Override
    public Map searchDevice(String deviceMAC, String deviceSerialNumber, String deviceStatus, String deviceFirmwareStatus, String deviceModel, String deviceFirmware, String deviceProvince, String deviceDistrict, List<Area> listArea, String ipAddress, String username, Long start, Long limit) {
        return deviceDao.searchDevice(deviceMAC, deviceSerialNumber, deviceStatus, deviceFirmwareStatus, deviceModel, deviceFirmware, deviceProvince, deviceDistrict, listArea, ipAddress, username, start, limit);
    }

    @Override
    public Device getDeviceBySerialNumber(String serialNumber) {
        return deviceDao.getDeviceBySerialNumber(serialNumber);
    }

    @Override
    public List<Device> getDeviceOfPolicy(Policy p) {
        return deviceDao.getDeviceOfPolicy(p);
    }

    @Override
    public List<Device> getDeviceFailedOfPolicy(Policy p) {
        return deviceDao.getDeviceFailedOfPolicy(p);
    }

    @Override
    public List<Device> getDeviceNotUpdate(Policy p) {
        return deviceDao.getDeviceNotUpdate(p);
    }

    @Override
    public List<Device> getDeviceByIdList(String id) {
        return deviceDao.getDeviceByIdList(id);
    }

    @Override
    public void insertDevice(Device device) {
        deviceDao.insertDevice(device);
    }
    
     

}
