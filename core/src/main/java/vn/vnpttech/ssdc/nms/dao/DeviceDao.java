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
package vn.vnpttech.ssdc.nms.dao;

import java.util.List;
import java.util.Map;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.Policy;

/**
 *
 * @author Dell
 */
public interface DeviceDao extends GenericDao<Device, Long> {

    public Map searchDevice(String deviceMAC, String deviceSerialNumber, String deviceStatus, String deviceFirmwareStatus,
            String deviceModel, String deviceFirmware, String deviceProvince, String deviceDistrict, List<Area> listArea, String ipAddress,String username, Long start, Long limit);

    public Device getDeviceBySerialNumber(String serialNumber);

    //longdq
    public List<Device> getDeviceOfPolicy(Policy p);

    public List<Device> getDeviceFailedOfPolicy(Policy p);

    public List<Device> getDeviceNotUpdate(Policy p);
    
    public List<Device> getDeviceByIdList(String id);
    
    public void insertDevice(Device device);
}
