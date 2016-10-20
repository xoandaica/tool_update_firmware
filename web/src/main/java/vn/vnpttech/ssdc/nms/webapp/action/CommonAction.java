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
package vn.vnpttech.ssdc.nms.webapp.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.model.DeviceModel;
import vn.vnpttech.ssdc.nms.model.Firmware;

/**
 *
 * @author Dell
 */
public class CommonAction extends BaseAction {

    //Load Model Type
    public InputStream getLoadModelCommon() {
        try {
            List<DeviceModel> listModel = deviceModelManager.getAll();
            JSONArray listDeviceModel = new JSONArray();
            if (!listModel.isEmpty()) {
                for (DeviceModel tmp : listModel) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", tmp.getId());
                    obj.put("name", tmp.getName());
                    listDeviceModel.put(obj);
                }
            }
            JSONObject result = new JSONObject();
            result.put("listDeviceModel", listDeviceModel);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadModelCommon: ", ex);
            return null;
        }
    }//End
    //Load Firmware by Model

    public InputStream getLoadFirmwareByModelCommon() {
        try {
            HttpServletRequest request = getRequest();
            JSONObject result = new JSONObject();
            JSONArray listFirmware = new JSONArray();
            if (StringUtils.isNotEmpty(request.getParameter("modelTypeId"))) {
                Long modelTypeId = Long.parseLong(request.getParameter("modelTypeId"));
                Set<Firmware> listFirmwareByModelId = deviceModelManager.get(modelTypeId).getFirmwares();
                if (!listFirmwareByModelId.isEmpty()) {
                    for (Firmware tmp : listFirmwareByModelId) {
                        JSONObject obj = new JSONObject();
                        obj.put("id", tmp.getId());
                        obj.put("version", tmp.getVersion());
                        listFirmware.put(obj);
                    }
                }
            }
            result.put("listFirmware", listFirmware);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadFirmwareByModelCommon: ", ex);
            return null;
        }
    }//End
    //Load Area

    public InputStream getLoadAreaCommon() {
        try {
            List<Area> listAreaLoad = areaManager.getAll();
            JSONArray listArea = new JSONArray();
            if (!listAreaLoad.isEmpty()) {
                for (Area tmp : listAreaLoad) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", tmp.getId());
                    obj.put("name", tmp.getName());
                    listArea.put(obj);
                }
            }
            JSONObject result = new JSONObject();
            result.put("listArea", listArea);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadAreaCommon: ", ex);
            return null;
        }
    }//End
    //Load Province

    public InputStream getLoadProvinceCommon() {
        try {
            List<Area> listProvinceLoad = areaManager.getAllProvince();
            JSONArray listProvince = new JSONArray();
            if (!listProvinceLoad.isEmpty()) {
                for (Area tmp : listProvinceLoad) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", tmp.getId());
                    obj.put("name", tmp.getName());
                    listProvince.put(obj);
                }
            }
            JSONObject result = new JSONObject();
            result.put("listProvince", listProvince);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadProvinceCommon: ", ex);
            return null;
        }
    }//End
    //Load District by ProvinceId

    public InputStream getLoadDistrictByProvinceIdCommon() {
        try {
            HttpServletRequest request = getRequest();
            String provinceId = request.getParameter("provinceId");
            List<Area> listDistrictLoad = areaManager.getDistrictByProvinceId(provinceId);
            JSONArray listDistrict = new JSONArray();
            if (!listDistrictLoad.isEmpty()) {
                for (Area tmp : listDistrictLoad) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", tmp.getId());
                    obj.put("name", tmp.getName());
                    listDistrict.put(obj);
                }
            }
            JSONObject result = new JSONObject();
            result.put("listDistrict", listDistrict);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadDistrictByProvinceIdCommon: ", ex);
            return null;
        }
    }//End
    //Load District by list Province Id

    public InputStream getLoadDistrictByProvinceListIdCommon() {
        try {
            HttpServletRequest request = getRequest();
            String provinceListId = request.getParameter("provinceListId");
            List<Area> listDistrictLoad = areaManager.getDistrictByProvinceListId(provinceListId);
            JSONArray listDistrict = new JSONArray();
            if (!listDistrictLoad.isEmpty()) {
                for (Area tmp : listDistrictLoad) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", tmp.getId());
                    obj.put("name", tmp.getName());
                    listDistrict.put(obj);
                }
            }
            JSONObject result = new JSONObject();
            result.put("listDistrict", listDistrict);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadDistrictByProvinceListIdCommon: ", ex);
            return null;
        }
    }//End

}
