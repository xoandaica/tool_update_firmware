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
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.webapp.util.ActionType;

/**
 *
 * @author Dell
 */
public class AreaAction extends BaseAction {

    public JSONObject createTreeItem(Area areaItem, boolean expanded, JSONArray childs) {
        try {
            JSONObject result = new JSONObject();
            result.put("id", areaItem.getId());
            result.put("text", areaItem.getName());
            result.put("type", areaItem.getAreaType());
            result.put("description", areaItem.getDescription());
            result.put("parent", (areaItem.getArea() != null) ? areaItem.getArea().getId() : "");
            result.put("expanded", expanded);
            result.put("root", (childs != null) ? childs : "");
            return result;
        } catch (Exception ex) {
            log.error("ERROR createTreeItem: ", ex);
            return null;
        }
    }//End

    public JSONObject createTreeAreaView(List<Area> areaList) {
        try {
            List<Area> provinces = new ArrayList<Area>();
            List<Area> districts = new ArrayList<Area>();
            for (Area tmp : areaList) {
                if (tmp.getAreaType() == 1) { //province lvl
                    provinces.add(tmp);
                } else if (tmp.getAreaType() == 2) { //district lvl
                    districts.add(tmp);
                }
            }
            JSONArray provinceChildren = new JSONArray();
            for (Area province : provinces) {
                JSONArray districtChildren = new JSONArray();
                for (Area district : districts) {
                    if (district.getArea().getId() == province.getId()) {
                        JSONObject districtChild = createTreeItem(district, false, null);
                        districtChildren.put(districtChild);
                    }
                }
                JSONObject provinceChild = createTreeItem(province, false, districtChildren);
                provinceChildren.put(provinceChild);
            }
            Area vietnam = areaManager.get(new Long(-1));
            JSONObject result = createTreeItem(vietnam, true, provinceChildren);
            return result;
        } catch (Exception ex) {
            log.error("ERROR createTreeAreaView: ", ex);
            return null;
        }
    }//End

    public InputStream getLoadAllProvince() {
        try {
            JSONObject result = new JSONObject();
            List<Area> listArea = areaManager.getAll();
            JSONArray provinceList = new JSONArray();
            for (Area tmp : listArea) {
                if (tmp.getAreaType() == 1) {//province level
                    JSONObject obj = new JSONObject();
                    obj.put("id", tmp.getId());
                    obj.put("name", tmp.getName());
                    provinceList.put(obj);
                }
            }
            result.put("provinceList", provinceList);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadAllProvince: ", ex);
            return null;
        }
    }//End

    public InputStream getDeleteArea() {
        try {
            HttpServletRequest request = getRequest();
            Long itemId = Long.parseLong(request.getParameter("itemId"));
            JSONObject result = new JSONObject();
            if ((areaManager.get(itemId).getDevices() != null) && (areaManager.get(itemId).getAreas() != null)) {
                areaManager.remove(itemId);
                result.put("deleteStatus", "success");
                saveActionLogs(Area.class.getSimpleName(), ActionTypeEnum.DELETE, "ID = " + itemId);
            } else {
                result.put("deleteStatus", "fail");
            }
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getDeleteArea: ", ex);
            return null;
        }
    }//End

    public InputStream getSaveArea() {
        try {
            HttpServletRequest request = getRequest();
            Integer actionType = Integer.parseInt(request.getParameter("actionType"));
            String areaName = request.getParameter("areaName");
            Integer areaType = Integer.parseInt(request.getParameter("areaType"));
            Long areaParent = Long.valueOf(request.getParameter("areaParent"));
            String areaDescription = request.getParameter("areaDescription");
            Area saveItem = new Area();
            if (actionType == ActionType.CREATE) {
                //Do nothing
            } else if (actionType == ActionType.EDIT) {
                Long areaId = Long.valueOf(request.getParameter("areaId"));
                saveItem = areaManager.get(areaId);
            }
            saveItem.setName(areaName);
            saveItem.setAreaType(areaType);
            saveItem.setArea(areaManager.get(areaParent));
            saveItem.setDescription(areaDescription);
            areaManager.save(saveItem);
            if (actionType == ActionType.CREATE) {
                saveActionLogs(Area.class.getSimpleName(), ActionTypeEnum.CREATE, "CREATE NEW AREA");
            } else if (actionType == ActionType.EDIT) {
                saveActionLogs(Area.class.getSimpleName(), ActionTypeEnum.UPDATE, "NAME = " + saveItem.getName());
            }
            JSONObject result = new JSONObject();
            result.put("saveStatus", "success");
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));

        } catch (Exception ex) {
            log.error("ERROR getSearchArea: ", ex);
            return null;
        }
    }//End

    public InputStream getLoadArea() {
        try {
            HttpServletRequest request = getRequest();
            String areaName_Search = request.getParameter("areaName_Search");
            String parentAreaId_Search = request.getParameter("parentAreaId_Search");
            List<Area> searchResult = areaManager.searchArea(areaName_Search, parentAreaId_Search);
            JSONObject result = new JSONObject();
            List<Area> areaList = new ArrayList<Area>();
            if (!searchResult.isEmpty()) {
                HashMap<Long, Area> areaSet = new HashMap<Long, Area>();
                for (Area tmp : searchResult) {
                    areaSet.put(tmp.getId(), tmp);
                    if (tmp.getArea() != null) {
                        areaSet.put(tmp.getArea().getId(), tmp.getArea());
                    }
                }
                areaSet.put(new Long(-1), areaManager.get(Long.parseLong("-1")));
                areaList = new ArrayList(areaSet.values());

            }
            result.put("root", createTreeAreaView(areaList));
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getSearchArea: ", ex);
            return null;
        }
    }//End

}
