/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.http;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * @author Vunb
 * @date Jul 14, 2015
 * @update Jul 14, 2015
 */
public class ACSAuthenticator extends Authenticator {

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        String ui = this.getRequestingURL().getUserInfo();
        System.out.println("MyAuthenticator: ui=" + ui);
        if (ui == null || ui.equals("")) {
            return super.getPasswordAuthentication();
        }
        String up[] = ui.split(":");
        char[] pc = new char[up[1].length()];
        up[1].getChars(0, up[1].length(), pc, 0);
        PasswordAuthentication pa = new PasswordAuthentication(up[0], pc);
        return pa;
    }
}
