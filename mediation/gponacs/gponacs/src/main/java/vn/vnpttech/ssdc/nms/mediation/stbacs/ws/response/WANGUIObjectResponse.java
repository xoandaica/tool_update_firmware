/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.WANGUIObject;

/**
 *
 * @author Vunb
 * @date Oct 22, 2014
 * @update Oct 22, 2014
 */
public class WANGUIObjectResponse extends TemplResponse<WANGUIObject> {

    @Override
    @XmlElementWrapper(name = "Results")
    @XmlElement(name = "WANGUIObject")
    public ArrayList<WANGUIObject> getRetValue() {
        return super.getRetValue();
    }

}
