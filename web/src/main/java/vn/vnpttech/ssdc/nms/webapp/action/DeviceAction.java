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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import vn.vnpttech.ssdc.nms.acs.gpontool.services.ACSService;
import vn.vnpttech.ssdc.nms.acs.gpontool.services.ACSServiceImplService;
import vn.vnpttech.ssdc.nms.acs.gpontool.services.BasicResponse;

import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.Firmware;
import vn.vnpttech.ssdc.nms.model.PolicyHistory;
import vn.vnpttech.ssdc.nms.threadpool.SimpleThreadPool;
import vn.vnpttech.ssdc.nms.webapp.util.ActionType;
import vn.vnpttech.ssdc.nms.webapp.util.Constant;

/**
 *
 * @author Dell
 */
public class DeviceAction extends BaseAction {

    public JSONObject getDeviceResponseMessage(ActionTypeEnum actionType, int deviceResponseErrorCode) {
        try {
            JSONObject result = new JSONObject();
            String message = "";
            if (deviceResponseErrorCode == Constant.ErrorCode.SYSTEM_FAIL) {
                message = "management.device.message." + actionType.getName() + ".systemFail";
            } else if (deviceResponseErrorCode == Constant.ErrorCode.SUCCESS) {
                message = "management.device.message." + actionType.getName() + ".success";
            } else if (deviceResponseErrorCode == Constant.ErrorCode.CPE_CONNECTION_ERROR) {
                message = "management.device.message." + actionType.getName() + ".cpeConnectionError";
            } else if (deviceResponseErrorCode == Constant.ErrorCode.CPE_CONFIG_ERROR) {
                message = "management.device.message." + actionType.getName() + ".cpeConfigError";
            } else if (deviceResponseErrorCode == Constant.ErrorCode.AUTHENTICATION_FAIL) {
                message = "management.device.message." + actionType.getName() + ".authenticationFail";
            } else if (deviceResponseErrorCode == Constant.ErrorCode.CONNECTION_ERROR) {
                message = "management.device.message." + actionType.getName() + ".connectionError";
            } else if (deviceResponseErrorCode == Constant.ErrorCode.CPE_ENQUEUED_REQUEST) {
                message = "management.device.message." + actionType.getName() + ".cpeEnqueuedRequest";
            } else if (deviceResponseErrorCode == Constant.ErrorCode.REQUEST_TIMEOUT) {
                message = "management.device.message." + actionType.getName() + ".requestTimeout";
            }
            result.put("errorCode", deviceResponseErrorCode);
            result.put("message", getText(message));
            return result;
        } catch (Exception ex) {
            log.error("ERROR getDeviceResponseMessage: ", ex);
            return null;
        }

    }

    //Load Device  with Criteria
    public InputStream getLoadDevice() {
        try {
            HttpServletRequest request = getRequest();
            String deviceMAC = request.getParameter("deviceMAC");
            String deviceSerialNumber = request.getParameter("deviceSerialNumber");
            String deviceStatus = request.getParameter("deviceStatus");
            String deviceFirmwareStatus = request.getParameter("deviceFirmwareStatus");
            String deviceModel = request.getParameter("deviceModel");
            String deviceFirmware = request.getParameter("deviceFirmware");
            String deviceProvinceId = request.getParameter("deviceProvince");
            String deviceDistrictId = request.getParameter("deviceDistrict");
            String deviceIpAddress = request.getParameter("deviceIpAddress");
            String deviceUsername = request.getParameter("deviceUsername");

            List<Area> listArea = new ArrayList<Area>();
//            if (StringUtils.isNotEmpty(deviceProvinceId)) {
//                if (StringUtils.isNotEmpty(deviceDistrictId)) {
//                    listArea.add(areaManager.get(Long.parseLong(deviceDistrictId)));
//                } else {
//                    listArea = areaManager.searchArea("", deviceProvinceId);
//                    listArea.add(areaManager.get(Long.parseLong(deviceProvinceId)));
//                }
//            }
            Map pagingMap = deviceManager.searchDevice(deviceMAC, deviceSerialNumber, deviceStatus,
                    deviceFirmwareStatus, deviceModel, deviceFirmware, deviceProvinceId, deviceDistrictId, listArea, deviceIpAddress, deviceUsername, start, limit);
            List<Device> searchResult = new ArrayList<Device>();
            JSONArray deviceList = new JSONArray();
            if (pagingMap.get("list") != null) {
                searchResult = (List<Device>) pagingMap.get("list");
                for (Device tmp : searchResult) {
                    JSONObject obj = new JSONObject();
                    Map map = BeanUtils.describe(tmp);
                    for (Object key : map.keySet()) {
                        obj.put(key.toString(), map.get(key));
                    }
//                    if (tmp.getArea() != null) {
//                        if (tmp.getArea().getAreaType() == 1) {//Province
//                            obj.put("province", tmp.getArea().getName());
//                            obj.put("provinceId", tmp.getArea().getId());
//                            obj.put("district", "");
//                            obj.put("districtId", "");
//                        } else if (tmp.getArea().getAreaType() == 2) {//District
//                            obj.put("province", tmp.getArea().getArea().getName());
//                            obj.put("provinceId", tmp.getArea().getArea().getId());
//                            obj.put("district", tmp.getArea().getName());
//                            obj.put("districtId", tmp.getArea().getId());
//                        }
//                    }
                    obj.put("modelName", tmp.getDeviceModel().getName());
                    obj.put("modelId", tmp.getDeviceModel().getId());
                    deviceList.put(obj);
                }
            }
            Long count = 0L;
            if (pagingMap.get("totalCount") != null) {
                count = (Long) pagingMap.get("totalCount");
            }
            JSONObject result = new JSONObject();
            result.put("list", deviceList);
            result.put("totalCount", count);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadDevice: ", ex);
            return null;
        }
    }//End
    //Delete device

    public InputStream getDeleteDevice() {
        try {
            HttpServletRequest request = getRequest();
            String itemIdList = request.getParameter("itemIdList");
            JSONObject result = new JSONObject();
            policyHistoryManager.deletePolicyHistory(itemIdList);
            String[] listId = itemIdList.split(",");
            for (String idTmp : listId) {
                deviceManager.remove(Long.parseLong(idTmp));
            }
            result.put("deleteStatus", "success");
//            saveActionLogs(Device.class.getSimpleName(), ActionTypeEnum.DELETE, "IDs = " + itemIdList);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getDeleteDevice: ", ex);
            return null;
        }
    }//End

    public InputStream getDeleteAllDevice() {
        try {

            JSONObject result = new JSONObject();
            int count1 = 0;
            count1 = policyHistoryManager.deleteAllPolicyHistory();
            if (count1 > -1) {
                deviceManager.deleletAll();
            }
            result.put("deleteStatus", "success");
//            saveActionLogs(Device.class.getSimpleName(), ActionTypeEnum.DELETE, "IDs = " + itemIdList);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getDelete All Device: ", ex);
            return null;
        }
    }//End
    //Config Device 

    public InputStream getConfigDevice() {
        try {
            HttpServletRequest request = getRequest();
            String deviceSerialNumber = request.getParameter("deviceSerialNumber");
            String deviceUsername = request.getParameter("deviceUsername");
            String devicePassword = request.getParameter("devicePassword");
            String authenticationUrl = request.getParameter("deviceUrl");
            //TODO: call service
            ACSService acsService = new ACSServiceImplService().getACSServiceImplPort();
            BasicResponse acsResponse = acsService.setUserAccount(deviceSerialNumber, deviceUsername, devicePassword, authenticationUrl);
            JSONObject result = getDeviceResponseMessage(ActionTypeEnum.CONFIG_SERVICE, acsResponse.getErrorCode());
            log.debug("getConfigDevice: " + deviceSerialNumber
                    + "- ErrorCode: " + acsResponse.getErrorCode() + " - Message: " + acsResponse.getMessage());
            // saveActionLogs(Device.class.getSimpleName(), ActionTypeEnum.CONFIG_SERVICE, "S/N = " + deviceSerialNumber);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getConfigDevice: ", ex);
            return null;
        }
    }//End
    //Restart Device 

    public InputStream getRestartDevice() {
        try {
            HttpServletRequest request = getRequest();
            String deviceSerialNumber = request.getParameter("deviceSerialNumber");
            //TODO: call service
            ACSService acsService = new ACSServiceImplService().getACSServiceImplPort();
//            BasicResponse acsResponse = acsService.rebootDevice(deviceSerialNumber);
            BasicResponse acsResponse = null;
            JSONObject result = getDeviceResponseMessage(ActionTypeEnum.RESTART_DEVICE, acsResponse.getErrorCode());
            log.debug("getRestartDevice: " + deviceSerialNumber
                    + "- ErrorCode: " + acsResponse.getErrorCode() + " - Message: " + acsResponse.getMessage());
//            saveActionLogs(Device.class.getSimpleName(), ActionTypeEnum.RESTART_DEVICE, "S/N = " + deviceSerialNumber);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getRestartDevice: ", ex);
            return null;
        }
    }//End
    //Get Firmware update history

    public InputStream getDeviceUpdateFirmwareHistory() {
        try {
            HttpServletRequest request = getRequest();
            String deviceId = request.getParameter("itemId");
            JSONArray listPolicyHistory = new JSONArray();
            Long count = 0L;

            if (StringUtils.isNotEmpty(deviceId)) {
                Map pagingMap = policyHistoryManager.searchPolicyHistory("", deviceId, start, limit);
                if (pagingMap.get("list") != null) {
                    List<PolicyHistory> policyHistory = (List<PolicyHistory>) pagingMap.get("list");
                    for (PolicyHistory tmp : policyHistory) {
                        JSONObject obj = new JSONObject();
                        Map map = BeanUtils.describe(tmp);
                        for (Object key : map.keySet()) {
                            obj.put(key.toString(), map.get(key));
                        }
                        obj.put("policyName", tmp.getPolicy().getName());
                        listPolicyHistory.put(obj);
                    }
                }
                if (pagingMap.get("totalCount") != null) {
                    count = (Long) pagingMap.get("totalCount");
                }

            }
            JSONObject result = new JSONObject();
            result.put("list", listPolicyHistory);
            result.put("totalCount", count);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getDeviceUpdateFirmwareHistory: ", ex);
            return null;
        }
    }//End
    //Save device

    public InputStream getSaveDevice() {
        try {
            HttpServletRequest request = getRequest();
            Integer actionType = Integer.parseInt(request.getParameter("actionType"));
            Long deviceProvinceId = Long.parseLong(request.getParameter("deviceProvince"));
            String deviceDistrictId = request.getParameter("deviceDistrict");

            Device saveItem = new Device();
            if (actionType == ActionType.CREATE) {
                //Do nothing
            } else if (actionType == ActionType.EDIT) {
                Long deviceId = Long.valueOf(request.getParameter("deviceId"));
                saveItem = deviceManager.get(deviceId);
            }
            if (StringUtils.isEmpty(deviceDistrictId)) { //assign device for province
                saveItem.setArea(areaManager.get(deviceProvinceId));
            } else {//assign device for district
                saveItem.setArea(areaManager.get(Long.parseLong(deviceDistrictId)));
            }
            deviceManager.save(saveItem);
//            if (actionType == ActionType.CREATE) {
//                saveActionLogs(Device.class.getSimpleName(), ActionTypeEnum.CREATE, "CREATE NEW DEVICE");
//            } else if (actionType == ActionType.EDIT) {
//                saveActionLogs(Device.class.getSimpleName(), ActionTypeEnum.UPDATE, "S/N = " + saveItem.getSerialNumber());
//            }
            JSONObject result = new JSONObject();
            result.put("saveStatus", "success");
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getSavePolicy: ", ex);
            return null;
        }
    }//End

    public InputStream getUpgradeFirmware() {
        try {
            HttpServletRequest request = getRequest();
            String idList = request.getParameter("idList");
            List<Device> devices = null;

            List<Firmware> firmwares = firmwareManager.getFirmwareDefault();
            HashMap<String, Firmware> hmFirmware = new HashMap();
            if (firmwares != null) {
                for (Firmware f : firmwares) {
                    hmFirmware.put(f.getDeviceModel().getName(), f);
                }
            }

            if (StringUtils.isNotBlank(idList)) {
                devices = deviceManager.getDeviceByIdList(idList);
            } else {
                devices = deviceManager.getAll();
            }

            if (devices != null && firmwares != null) {
                SimpleThreadPool upgradePool = new SimpleThreadPool();
                upgradePool.upgradeFirmwarePool(devices, hmFirmware);
            }

            JSONObject result = new JSONObject();
            result.put("saveStatus", "success");
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }//End

    public InputStream getConfigGatewayDns() {
        try {
            HttpServletRequest request = getRequest();
            List<Device> devices = null;
            String idList = request.getParameter("idList");
            if (StringUtils.isNotBlank(idList)) {
                devices = deviceManager.getDeviceByIdList(idList);
            } else {
                devices = deviceManager.getAll();
            }

            if (devices != null) {
                SimpleThreadPool gwDns = new SimpleThreadPool();
                gwDns.setGwDnsPool(devices);
            }

            JSONObject result = new JSONObject();
            result.put("saveStatus", "success");
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }//EndF

    public InputStream getConfigAcsUrl() {
        try {
            HttpServletRequest request = getRequest();
            List<Device> devices = null;
            String idList = request.getParameter("idList");
            if (StringUtils.isNotBlank(idList)) {
                devices = deviceManager.getDeviceByIdList(idList);
            } else {
                devices = deviceManager.getAll();
            }

            if (devices != null) {
                SimpleThreadPool AcsUrl = new SimpleThreadPool();
                AcsUrl.setAcsUrlPool(devices);
            }

            JSONObject result = new JSONObject();
            result.put("saveStatus", "success");
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }//EndF

    public InputStream getStaticRoute() {
        try {
            HttpServletRequest request = getRequest();
            List<Device> devices = null;
            String idList = request.getParameter("idList");
            String destIp = request.getParameter("destIp");
            String gateway = request.getParameter("gateway");
            String subnetMark = request.getParameter("subnetMark");
            String interfaceName = request.getParameter("interfaceName");

            if (StringUtils.isNotBlank(idList)) {
                devices = deviceManager.getDeviceByIdList(idList);
            } else {
                devices = deviceManager.getAll();
            }

            if (devices != null) {
                SimpleThreadPool StaticRoute = new SimpleThreadPool();
                StaticRoute.setStaticRoute(devices, destIp, gateway, subnetMark, interfaceName);
            }

            JSONObject result = new JSONObject();
            result.put("saveStatus", "success");
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error(ex);
            return null;
        }
    }//EndF

}
