/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.WANGUIObjectEthernet;

/**
 *
 * @author Vunb
 * @date Oct 22, 2014
 * @update Oct 22, 2014
 */
public class WANGUIObjectEthernetResponse extends TemplResponse<WANGUIObjectEthernet> {

    @Override
    @XmlElementWrapper(name = "Results")
    @XmlElement(name = "WANGUIObjectEthernet")
    public ArrayList<WANGUIObjectEthernet> getRetValue() {
        return super.getRetValue();
    }

}
