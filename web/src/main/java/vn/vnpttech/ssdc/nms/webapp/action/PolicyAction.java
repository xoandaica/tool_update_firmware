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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.model.Policy;
import vn.vnpttech.ssdc.nms.model.PolicyHistory;
import vn.vnpttech.ssdc.nms.webapp.util.ActionType;

/**
 *
 * @author Dell
 */
public class PolicyAction extends BaseAction {

    static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    //Load Policy  with Criteria
    public InputStream getLoadPolicy() {
        try {
            HttpServletRequest request = getRequest();
            String policyName = request.getParameter("policyName");
            String policyStatus = request.getParameter("policyStatus");
            String policyEnable = request.getParameter("policyEnable");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            Map pagingMap = policyManager.searchPolicy(policyName, policyStatus, policyEnable, startTime, endTime, start, limit);
            List<Policy> searchResult = new ArrayList<Policy>();
            JSONArray policyList = new JSONArray();
            if (pagingMap.get("list") != null) {
                searchResult = (List<Policy>) pagingMap.get("list");
                for (Policy tmp : searchResult) {
                    JSONObject obj = new JSONObject();
                    Map map = BeanUtils.describe(tmp);
                    for (Object key : map.keySet()) {
                        obj.put(key.toString(), map.get(key));
                    }
                    obj.put("startTime", sdf.format(tmp.getStartTime()));
                    obj.put("endTime", sdf.format(tmp.getEndTime()));
                    obj.put("modelId", tmp.getDeviceModel().getId());
                    obj.put("model", tmp.getDeviceModel().getName());
                    obj.put("firmwareId", tmp.getFirmware().getId());
                    obj.put("firmware", tmp.getFirmware().getVersion());
                    Set<Area> areaList = tmp.getAreas();
                    if (areaList.isEmpty()) {
                        obj.put("provinceId", "");
                        obj.put("districtId", "");
                    } else {
                        StringBuilder provinceList = new StringBuilder();
                        provinceList.setLength(0);
                        StringBuilder districtList = new StringBuilder();
                        districtList.setLength(0);
                        for (Area area : areaList) {
                            if (area.getAreaType() == 1) {//province lvl
                                provinceList.append(area.getId()).append(",");
                            } else if (area.getAreaType() == 2) {//district lvl
                                districtList.append(area.getId()).append(",");
                            }
                        }
                        if (provinceList.length() > 0) {
                            obj.put("provinceId", provinceList.deleteCharAt(provinceList.length() - 1).toString());
                        } else {
                            obj.put("provinceId", "");
                        }
                        if (districtList.length() > 0) {
                            obj.put("districtId", districtList.deleteCharAt(districtList.length() - 1).toString());
                        } else {
                            obj.put("districtId", "");
                        }
                    }
                    obj.put("totalDeviceNo", tmp.getDeviceFailedNo() + tmp.getDeviceSuccessNo());
                    policyList.put(obj);
                }
            }
            Long count = 0L;
            if (pagingMap.get("totalCount") != null) {
                count = (Long) pagingMap.get("totalCount");
            }
            JSONObject result = new JSONObject();
            result.put("list", policyList);
            result.put("totalCount", count);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadPolicy: ", ex);
            return null;
        }
    }//End
    //Load Policy History with Policy Id

    public InputStream getLoadPolicyHistory() {
        try {
            HttpServletRequest request = getRequest();
            String policyId = request.getParameter("itemId");
            JSONArray listPolicyHistory = new JSONArray();
            Long count = 0L;

            if (StringUtils.isNotEmpty(policyId)) {
                Map pagingMap = policyHistoryManager.searchPolicyHistory(policyId, "", start, limit);
                if (pagingMap.get("list") != null) {
                    List<PolicyHistory> policyHistory = (List<PolicyHistory>) pagingMap.get("list");
                    for (PolicyHistory tmp : policyHistory) {
                        JSONObject obj = new JSONObject();
                        Map map = BeanUtils.describe(tmp);
                        for (Object key : map.keySet()) {
                            obj.put(key.toString(), map.get(key));
                        }
                        obj.put("deviceMAC", tmp.getDevice().getMac());
                        obj.put("startTime", sdf.format(tmp.getStartTime()));
                        if (tmp.getEndTime() != null) {
                            obj.put("endTime", sdf.format(tmp.getEndTime()));
                        }
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
            log.error("ERROR getLoadPolicyHistory: ", ex);
            return null;
        }
    }//End
    //Delete policy

    public InputStream getDeletePolicy() {
        try {
            HttpServletRequest request = getRequest();

            String itemIdList = request.getParameter("itemIdList");
            JSONObject result = new JSONObject();
            String[] listId = itemIdList.split(",");
            for (String idTmp : listId) {
                policyManager.remove(Long.parseLong(idTmp));
            }
            result.put("deleteStatus", "success");
            saveActionLogs(Policy.class.getSimpleName(), ActionTypeEnum.DELETE, "IDs = " + itemIdList);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getDeletePolicy: ", ex);
            return null;
        }
    }//End
    //Save policy

    public InputStream getSavePolicy() {
        try {
            HttpServletRequest request = getRequest();
            Integer actionType = Integer.parseInt(request.getParameter("actionType"));
            String policyName = request.getParameter("policyName");
            Long policyModelType = Long.parseLong(request.getParameter("policyModelType"));
            Long policyFirmware = Long.parseLong(request.getParameter("policyFirmware"));
            String policyProvince = request.getParameter("policyProvince");
            String policyDistrict = request.getParameter("policyDistrict");
            Date policyStartTime = sdf.parse(request.getParameter("policyStartTime"));
            Date policyEndTime = sdf.parse(request.getParameter("policyEndTime"));
            Integer policyEnable = Integer.parseInt(request.getParameter("policyEnable"));
            Policy saveItem = new Policy();
            if (actionType == ActionType.CREATE) {
                //Do nothing
            } else if (actionType == ActionType.EDIT) {
                Long policyId = Long.valueOf(request.getParameter("policyId"));
                saveItem = policyManager.get(policyId);
            }
            saveItem.setName(policyName);
            saveItem.setDeviceModel(deviceModelManager.get(policyModelType));
            saveItem.setFirmware(firmwareManager.get(policyFirmware));
            Set<Area> areas = new HashSet<Area>();
            if (StringUtils.isNotEmpty(policyProvince)) {
                String[] provincePieces = policyProvince.split(",");
                for (String tmp : provincePieces) {
                    areas.add(areaManager.get(Long.parseLong(tmp)));
                }
            }
            if (StringUtils.isNotEmpty(policyDistrict)) {
                String[] districtPieces = policyDistrict.split(",");
                for (String tmp : districtPieces) {
                    areas.add(areaManager.get(Long.parseLong(tmp)));
                }
            }
            saveItem.setAreas(areas);
            saveItem.setStartTime(policyStartTime);
            saveItem.setEndTime(policyEndTime);
            if (actionType == ActionType.CREATE) {
                saveItem.setPolicyStatus(0);//not run
            }
            saveItem.setEnable(policyEnable);
            policyManager.save(saveItem);
            if (actionType == ActionType.CREATE) {
                saveActionLogs(Policy.class.getSimpleName(), ActionTypeEnum.CREATE, "CREATE NEW POLICY");
            } else if (actionType == ActionType.EDIT) {
                saveActionLogs(Policy.class.getSimpleName(), ActionTypeEnum.UPDATE, "NAME = " + saveItem.getName());
            }
            JSONObject result = new JSONObject();
            result.put("saveStatus", "success");
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getSavePolicy: ", ex);
            return null;
        }
    }//End

}
