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
package vn.vnpttech.ssdc.nms.webapp.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import vn.vnpttech.ssdc.nms.model.ReportTemplate;

/**
 *
 * @author Dell
 */
public class ReportTemplateAction extends BaseAction {

    public String list() {
        return SUCCESS;
    }

    public InputStream getDataReportGrid() throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            List<ReportTemplate> reportTemplateList = reportTemplateManager.getAll();
            if (reportTemplateList != null && reportTemplateList.size() > 0) {
                for (ReportTemplate reportTemplate1 : reportTemplateList) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", reportTemplate1.getId());
                    obj.put("name", reportTemplate1.getName());
                    obj.put("description", reportTemplate1.getDescription());
                    obj.put("url", reportTemplate1.getUrl());
                    jsonArray.put(obj);
                }
            }
            result.put("reportList", jsonArray);
            result.put("reportCount", reportTemplateList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
    }
    
    public InputStream getReportTime() throws JSONException, UnsupportedEncodingException {
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject result = new JSONObject();
            String url = "";
            HttpServletRequest request = getRequest();

                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");
                String operatorModuleLog = getRequest().getRemoteUser();
                String currentDateModuleLog = request.getParameter("currentDateModuleLog");
                String urlModule = request.getParameter("urlModule");
                String hostModule = "http://10.84.8.62:8071/birt-viewer/frameset";
                url += hostModule + urlModule + "&action_start_time=" + startTime + "&action_end_time=" + endTime + "&currentTime=" + currentDateModuleLog + "&operator=" + operatorModuleLog;
            jsonArray.put(url);
            result.put("weekList", jsonArray);
            
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (NumberFormatException e) {
            
            return null;
        } catch (JSONException e) {
            
            
            return null;
        } catch (UnsupportedEncodingException e) {
            
            
            return null;
        }

    }
}
