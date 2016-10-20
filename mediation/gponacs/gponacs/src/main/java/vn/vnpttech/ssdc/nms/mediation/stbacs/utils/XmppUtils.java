/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Util for xmpp management
 *
 * @author Vunb
 * @date Jun 11, 2015
 * @update Jun 11, 2015
 */
public class XmppUtils {

    public static String getMD5Password(String password, String salt) {
        try {
            String input = StringUtils.defaultIfBlank(salt, RandomStringUtils.random(10)).concat(password);
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(input.getBytes());
            byte byteData[] = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getMD5Password(String password) {
        return getMD5Password(password, "");
    }
}
