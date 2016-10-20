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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import vn.vnpttech.ssdc.nms.model.Firmware;

/**
 *
 * @author Dell
 */
public interface FirmwareDao extends GenericDao<Firmware, Long> {

    List<Firmware> searchFirmware(Long modelId, String version, Date startDate, Date endDate, Integer start, Integer limit);

    Long countFirmware(Long modelId, String version, Date startDate, Date endDate);

    Integer deleteList(List<Long> ids);

    boolean checkNewFirmware(Timestamp t, String modelName, String version);

    Firmware getByModelVersion(String modelName, String version);

    boolean checkFirmwareExist(Long modelId, String fwVersion);

    void updateDefaultFw(Long id, Long modelId);

    List<Firmware> getFirmwareDefault();

}
