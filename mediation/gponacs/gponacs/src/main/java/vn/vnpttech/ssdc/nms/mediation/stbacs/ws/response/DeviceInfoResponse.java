/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response;

import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.DeviceInfo;

/**
 *
 * @author Vunb
 * @date Jul 2, 2015
 * @update Jul 2, 2015
 */
public class DeviceInfoResponse extends BasicResponse {

    private DeviceInfo deviceInfo;

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

}
