/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Vunb
 * @param <T>
 * @date Jun 26, 2014
 * @update Jun 26, 2014
 */
public abstract class TemplResponse<T> {

    private int errorCode;
    private String message;
    private ArrayList<T> retValue = new ArrayList<T>();

    public TemplResponse() {
        this.errorCode = 0;
        this.message = "(not provided)";
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlTransient
    protected ArrayList<T> getRetValue() {
        return retValue;
    }

    public void setRetValue(ArrayList<T> retValue) {
        this.retValue = retValue;
    }

    public void addRetValue(T retValue) {
        if (retValue != null) {
            this.retValue.add(retValue);
        }
    }
}
