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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import vn.vnpttech.ssdc.nms.fileprocess.HttpDownloadFile;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.model.DeviceModel;
import vn.vnpttech.ssdc.nms.model.Firmware;
import vn.vnpttech.ssdc.nms.model.User;
import vn.vnpttech.ssdc.nms.webapp.util.ResourceBundleUtils;
import vn.vnpttech.ssdc.nms.fileprocess.ReadFile;
import vn.vnpttech.ssdc.nms.model.Policy;
import vn.vnpttech.ssdc.nms.webapp.util.Constant;
import vn.vnpttech.ssdc.nms.webapp.util.FileUtils;

/**
 *
 * @author Dell
 */
public class FirmwareAction extends BaseAction {

    private Long model;
    private String version;
    private List<Long> idDelete = new ArrayList<Long>();
    private File file;
    private String fileContentType;
    private String fileFileName;

    public Long getModel() {
        return model;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setModel(Long model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Long> getIdDelete() {
        return idDelete;
    }

    public void setIdDelete(List<Long> idDelete) {
        this.idDelete = idDelete;
    }

    public InputStream getDeleteFirmware() throws JSONException, UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        try {
            HttpServletRequest request = getRequest();
            Integer count = -1;
            if (idDelete != null && idDelete.size() > 0) //            firmwareDao.remove(itemId);
            {
                count = firmwareManager.deleteList(idDelete);
            }
            // result.put("firmwareList", getAllFirmwareFromDatabase());
            if (count != -1) {
                result.put("deleteStatus", true);
                saveActionLogs(User.class.getSimpleName(), ActionTypeEnum.DELETE, idDelete.toString());
            } else {
                result.put("deleteStatus", false);
            }
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("deleteStatus", false);
            result.put("failedMsg", e.getMessage());
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        }
    }//End

    public InputStream getSearchFirmware() throws JSONException, UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        Long rowCount = 0L;
        List<Firmware> fs = new ArrayList<Firmware>();
        JSONArray firmwareList = new JSONArray();
        try {
            HttpServletRequest request = getRequest();
            String sd = request.getParameter("startDate");
            String ed = request.getParameter("endDate");
            Date startDate = null;
            Date endDate = null;
            DateFormat df = new SimpleDateFormat(Constant.DATE_FORMAT);
            if (StringUtils.isNotBlank(sd)) {
                startDate = df.parse(sd);
            }
            if (StringUtils.isNotBlank(ed)) {
                endDate = df.parse(ed);
            }

            fs = firmwareManager.searchFirmware(model, version, startDate, endDate, start == null ? null : start.intValue(), limit == null ? null : limit.intValue());
            rowCount = firmwareManager.countFirmware(model, version, startDate, endDate);

            firmwareList = this.firmwareToJson(fs);

            result.put("firmwareList", firmwareList);
            result.put("totalCount", rowCount);

            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("firmwareList", firmwareList);
            result.put("totalCount", rowCount);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        }
    }//End

    private JSONArray firmwareToJson(List<Firmware> fs) {
        JSONArray ja = new JSONArray();
        try {
            if (!fs.isEmpty()) {
                for (Firmware f : fs) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", f.getId());
                    jo.put("modelId", f.getDeviceModel().getId());
                    jo.put("modelName", f.getDeviceModel().getName());
                    jo.put("version", f.getVersion());
                    String policyList = "";
                    if (!f.getPolicies().isEmpty()) {
                        Set<Policy> policies = f.getPolicies();
                        for (Policy tmp : policies) {
                            policyList += tmp.getName() + ",";
                        }
                        policyList = policyList.substring(0, policyList.length() - 1);
                    }
                    jo.put("policies", policyList);
                    if (f.getReleaseDate() != null) {
                        SimpleDateFormat dt = new SimpleDateFormat(Constant.DATE_FORMAT);
                        jo.put("releaseDate", dt.format(new Date(f.getReleaseDate().getTime())));
                    }
                    jo.put("releaseNote", f.getReleaseNote());
                    jo.put("path", f.getFirmwarePath());
                    jo.put("usageNo", f.getDeviceUseageNumber());
                    jo.put("fwDefault", f.getFwDefault());
                    ja.put(jo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ja;
    }

    public InputStream getAllModel() throws JSONException, UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        JSONArray modelList = new JSONArray();
        try {

            JSONObject jso = new JSONObject();
            List<DeviceModel> models = new ArrayList<DeviceModel>();
            models = deviceModelManager.getAll();
            if (models != null && models.size() > 0) {
                for (DeviceModel tmp : models) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", tmp.getId());
                    obj.put("name", tmp.getName());

                    modelList.put(obj);
                }
            }

            result.put("modelList", modelList);
            result.put("status", true);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        }
    }

    public InputStream getCheckNewFirmware() throws JSONException, UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        JSONArray modelList = new JSONArray();
        try {
            String msg = "";

            JSONObject jso = new JSONObject();
            List<DeviceModel> models = new ArrayList<DeviceModel>();
            //Download file
            boolean downloadflag = false;
            downloadflag = HttpDownloadFile.downloadFile(ResourceBundleUtils.getHttpUrl(), ResourceBundleUtils.getSaveDir());
            if (downloadflag) {
                List<Firmware> fs = new ArrayList<Firmware>();
                fs = ReadFile.parseXml();
                if (fs != null && fs.size() > 0) {
                    for (Firmware f : fs) {
                        if (f.getReleaseDate() != null) {
                            boolean tem = false;
                            tem = firmwareManager.checkNewFirmware(f.getReleaseDate(), f.getModelName(), f.getVersion());
                            if (tem) {// insert new firmware
                                DeviceModel d = deviceModelManager.getModelByName(f.getModelName());
                                if (d != null) {
                                    f.setDeviceModel(d);
                                    firmwareManager.save(f);

                                } else if (d == null) {
                                    d = new DeviceModel();
                                    d.setName(f.getModelName());
                                    d.setDescription("Auto generated Model: " + f.getModelName());
                                    d = deviceModelManager.save(d);
                                    if (d != null) {
                                        f.setDeviceModel(d);
                                        firmwareManager.save(f);
                                    }
                                }
                                List<String> ls = new ArrayList<String>();
                                ls.add(f.getVersion());
                                ls.add(d.getName());
                                msg = getText("management.firmware.create", ls);
                                saveActionLogs(Firmware.class.getSimpleName(), ActionTypeEnum.CREATE, msg);
                                System.out.println(getText("management.firmware.create", ls));
                            } else {// update firmware
                                //get firmware
                                Firmware fw = firmwareManager.getByModelVersion(f.getModelName(), f.getVersion());
                                if (fw != null) {
                                    DeviceModel d = deviceModelManager.getModelByName(f.getModelName());
                                    if (d != null) {
                                        fw.setDeviceModel(d);
                                        fw.setFirmwarePath(f.getFirmwarePath());
                                        fw.setVersion(f.getVersion());
                                        fw.setReleaseDate(f.getReleaseDate());
                                        fw.setReleaseNote(f.getReleaseNote());
                                        firmwareManager.save(fw);
                                    } else if (d == null) {
                                        d = new DeviceModel();
                                        d.setName(f.getModelName());
                                        d.setDescription("Auto generated Model: " + f.getModelName());
                                        d = deviceModelManager.save(d);
                                        if (d != null) {
                                            fw.setDeviceModel(d);
                                            fw.setFirmwarePath(f.getFirmwarePath());
                                            fw.setVersion(f.getVersion());
                                            fw.setReleaseDate(f.getReleaseDate());
                                            fw.setReleaseNote(f.getReleaseNote());
                                            firmwareManager.save(fw);
                                        }
                                    }
                                    List<String> ls = new ArrayList<String>();
                                    ls.add(fw.getVersion());
                                    ls.add(d.getName());
                                    msg = getText("management.firmware.update");
                                    saveActionLogs(Firmware.class.getSimpleName(), ActionTypeEnum.CREATE, msg);
                                    System.out.println(getText("management.firmware.update"));
                                }

                            }
                        }
                    }
                }
            }

            result.put("status", true);
            if (StringUtils.isEmpty(msg)) {
                msg = getText("management.firmware.nothingtodo");
            }
            result.put("msg", msg);

            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        }
    }

    public InputStream getSaveFirmware() throws JSONException, UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        Long rowCount = 0L;
        List<Firmware> fs = new ArrayList<Firmware>();
        JSONArray firmwareList = new JSONArray();
        try {
            HttpServletRequest request = getRequest();
            String id = request.getParameter("fwId");
            String path = request.getParameter("fwPath");
            String fwReleaseDate = request.getParameter("fwReleaseDate");
            String fwReleaseNote = request.getParameter("fwReleaseNote");
            String fwModel = request.getParameter("fwModel");
            String fwVersion = request.getParameter("fwVersion");

            // check firmware ton tai chua
            if (!(StringUtils.isNotBlank(id.trim()) && StringUtils.isNumeric(id.trim()))) {
                boolean exist = false;
                Long modelId = null;
                if (StringUtils.isNotBlank(fwModel) && StringUtils.isNumeric(fwModel)) {
                    modelId = Long.parseLong(fwModel);
                }
                exist = firmwareManager.checkFirmwareExist(modelId, fwVersion);

                if (exist) {//đa ton tai
                    result.put("status", false);
                    result.put("message", getText("management.firmware.exist"));
                    return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
                }
            }
            Date releaseDate = null;
            DateFormat df = new SimpleDateFormat(Constant.DATE_FORMAT);
            if (StringUtils.isNotBlank(fwReleaseDate)) {
                releaseDate = df.parse(fwReleaseDate);
            }

            Firmware f = null;

            if (StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
                f = firmwareManager.get(Long.parseLong(id));
            } else {
                f = new Firmware();
            }

            if (StringUtils.isNotBlank(fwModel) && StringUtils.isNumeric(fwModel)) {
                f.setDeviceModel(deviceModelManager.get(Long.parseLong(fwModel)));
            }
            f.setReleaseDate(new Timestamp(releaseDate.getTime()));
            f.setFirmwarePath(path);
            f.setVersion(fwVersion);
            f.setReleaseNote(fwReleaseNote);
            f.getReleaseNote();
            f.getReleaseDate();

            firmwareManager.save(f);
            result.put("status", true);

            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        }
    }//End

    public InputStream getUpdateFirmwareDefault() throws JSONException, UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        Long rowCount = 0L;
        List<Firmware> fs = new ArrayList<Firmware>();
        JSONArray firmwareList = new JSONArray();
        try {
            HttpServletRequest request = getRequest();
            String id = request.getParameter("fwId");

            Firmware f = null;

            if (StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
                f = firmwareManager.get(Long.parseLong(id));
            } else {
                f = new Firmware();
            }

            f.setFwDefault(1);

            f = firmwareManager.save(f);
            if (f != null) {
                firmwareManager.updateDefaultFw(f.getId(), f.getDeviceModel().getId());
            }
            result.put("status", true);

            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        }
    }//End

    public InputStream getUploadFirmware() throws JSONException, UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        try {
            HttpServletRequest request = getRequest();
            String fwModel = request.getParameter("fwModel");
            String fwVersion = request.getParameter("fwVersion");
            // check firmware ton tai chua
            boolean exist = false;
            Long modelId = null;
            if (StringUtils.isNotBlank(fwModel) && StringUtils.isNumeric(fwModel)) {
                modelId = Long.parseLong(fwModel);
            }
            exist = firmwareManager.checkFirmwareExist(modelId, fwVersion);

            if (exist) {//đa ton tai
                result.put(SUCCESS, false);
                return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
            }

            System.out.println("getRealPath: " + getRequest().getServletContext().getRealPath("/"));

            String sepa = File.separator;
            String uploadDir = getRequest().getServletContext().getRealPath("/")
                    + sepa + ResourceBundleUtils.getFirmwareUploadDir() + sepa;
            System.out.println("upload dir: " + uploadDir);

            File dirPath = new File(uploadDir);

            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }

            // retrieve the file data
            InputStream stream = new FileInputStream(file);
            String dns = ResourceBundleUtils.getFirmwareDns();
            String pathToFirmware = dns + "/" + ResourceBundleUtils.getFirmwareUploadDir() + "/" + fileFileName;
            System.out.println("pathToFirmware: " + pathToFirmware);

            // write the file to the file specified
            OutputStream bos = new FileOutputStream(uploadDir + fileFileName);
            int bytesRead;
            byte[] buffer = new byte[8192];

            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.close();
            stream.close();

            // save to db
            Firmware f = new Firmware();
            f.setReleaseDate(new Timestamp(new Date().getTime()));
            f.setFirmwarePath(pathToFirmware.replace(sepa, "/"));
            f.setVersion(fwVersion);
            f.setReleaseNote("");
            f.setDeviceModel(deviceModelManager.get(modelId));
            firmwareManager.save(f);
            result.put(SUCCESS, true);
            return new ByteArrayInputStream(result.toString().replace("\"", "")
                    .getBytes("UTF8"));
        } catch (Exception e) {
            result.put(SUCCESS, false);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        }

    }
}
