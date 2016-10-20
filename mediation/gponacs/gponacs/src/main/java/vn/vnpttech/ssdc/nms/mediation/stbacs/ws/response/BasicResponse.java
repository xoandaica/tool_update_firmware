/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.NamedKeyValue;

/**
 *
 * @author Vunb
 * @date Jun 9, 2014
 * @update Jun 9, 2014
 */
public class BasicResponse extends TemplResponse<NamedKeyValue> {

    @Override
    @XmlElementWrapper(name = "Results")
    @XmlElement(name = "Item")
    public ArrayList<NamedKeyValue> getRetValue() {
        return super.getRetValue();
    }

    public void addResultValue(boolean retValue) {
        NamedKeyValue result = new NamedKeyValue();
        result.setKey("Result");
        result.setValue(retValue);
        this.addRetValue(result);
    }

    public void addValue(String key, String value) {
        NamedKeyValue result = new NamedKeyValue();
        result.setKey(key);
        result.setValue(value);
        this.addRetValue(result);
    }

    public void addValue(Object key, Object value) {
        NamedKeyValue result = new NamedKeyValue();
        result.setKey(String.valueOf(key));
        result.setValue(value);
        this.addRetValue(result);
    }

}
