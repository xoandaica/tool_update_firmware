/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpt.tr069.handler;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Zan
 */
public class XMLFilterNS extends InputStream {
    // Dumb class to filter out declaration of default xmlns

    private String pat = "xmlns=\"urn:dslforum-org:cwmp-1-0\"";
    private String pat2 = "xmlns=\"urn:dslforum-org:cwmp-1-1\"";
    private int length = 0;
    private int pos = 0;
    private boolean f = false;
    private byte buff[] = new byte[1024];
    private InputStream is;

    @Override
    public int read() throws IOException {
        if (!f) {
            length = is.read(buff);
            if (length < buff.length) {
                byte[] b2 = new byte[length];
                System.arraycopy(buff, 0, b2, 0, length);
                buff = b2;
            }

            String b = new String(buff);
            b = b.replace(pat, "");
            b = b.replace(pat2, "");
            buff = b.getBytes();
            length = buff.length;
            f = true;
        }

        if (pos < length) {
            return buff[pos++];
        }
        return is.read();
    }

    public XMLFilterNS(InputStream is) {
        this.is = is;
    }
}
