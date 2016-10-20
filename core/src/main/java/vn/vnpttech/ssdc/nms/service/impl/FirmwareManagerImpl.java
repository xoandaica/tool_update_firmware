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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import vn.vnpttech.ssdc.nms.dao.FirmwareDao;
import vn.vnpttech.ssdc.nms.model.Firmware;
import vn.vnpttech.ssdc.nms.service.FirmwareManager;

/**
 *
 * @author longdq
 */
public class FirmwareManagerImpl extends GenericManagerImpl<Firmware, Long> implements FirmwareManager {

    @Autowired
    FirmwareDao firmwareDao;

    public FirmwareManagerImpl() {

    }

    public FirmwareManagerImpl(FirmwareDao firmwareDao) {
        super(firmwareDao);
    }

    public FirmwareDao getFirmwareDao() {
        return firmwareDao;
    }

    public void setFirmwareDao(FirmwareDao firmwareDao) {
        this.firmwareDao = firmwareDao;
    }

    @Override
    public List<Firmware> searchFirmware(Long modelId, String version, Date startDate, Date endDate, int start, int limit) {
        return firmwareDao.searchFirmware(modelId, version, startDate, endDate, start, limit);
    }

    @Override
    public Long countFirmware(Long modelId, String version, Date startDate, Date endDate) {
        return firmwareDao.countFirmware(modelId, version, startDate, endDate);
    }

    @Override
    public Integer deleteList(List<Long> ids) {
        return firmwareDao.deleteList(ids);
    }

    @Override
    public boolean checkNewFirmware(Timestamp t, String modelName, String version) {
        return firmwareDao.checkNewFirmware(t, modelName, version);
    }

    @Override
    public Firmware getByModelVersion(String modelName, String version) {
        return firmwareDao.getByModelVersion(modelName, version);
    }

    @Override
    public boolean checkFirmwareExist(Long modelId, String fwVersion) {
        return firmwareDao.checkFirmwareExist(modelId, fwVersion);
    }

    @Override
    public void updateDefaultFw(Long id, Long modelId) {
        firmwareDao.updateDefaultFw(id, modelId);
    }

    @Override
    public List<Firmware> getFirmwareDefault() {
        return firmwareDao.getFirmwareDefault();
    }

}
