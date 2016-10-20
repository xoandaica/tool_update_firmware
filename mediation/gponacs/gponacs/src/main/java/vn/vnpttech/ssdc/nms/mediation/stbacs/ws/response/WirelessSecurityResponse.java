/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.WirelessSecurity;

/**
 *
 * @author Vunb
 * @date Mar 5, 2015
 * @update Mar 5, 2015
 */
public class WirelessSecurityResponse extends TemplResponse<WirelessSecurity> {

    @Override
    @XmlElementWrapper(name = "Results")
    @XmlElement(name = "WirelessSecurity")
    public ArrayList<WirelessSecurity> getRetValue() {
        return super.getRetValue();
    }
}
