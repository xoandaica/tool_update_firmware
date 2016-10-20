/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.exception;

/**
 *
 * @author Vunb
 * @date Jul 3, 2015
 * @update Jul 3, 2015
 */
public class ConnectionException extends Exception {

    public ConnectionException() {

    }

    public ConnectionException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        if (super.getMessage() == null || super.getMessage().length() == 0) {
            return "Connection error";
        }
        return super.getMessage();
    }
}
