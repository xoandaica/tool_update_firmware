/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.common;

import java.util.HashMap;
import java.util.Map;
import vn.vnpttech.ssdc.nms.mediation.stbacs.utils.BeanUtils;
import vn.vnpttech.ssdc.nms.model.DeviceModel;
import vn.vnpttech.ssdc.nms.service.DeviceModelManager;

/**
 *
 * @author SSDC
 */
public class ModelCache {

    public static Map<String, DeviceModel> mapCacheModel = new HashMap<String, DeviceModel>();
    public static final DeviceModelManager deviceModelManager = BeanUtils.getInstance().getBean("deviceModelManager", DeviceModelManager.class);
    // load cache
 

}
