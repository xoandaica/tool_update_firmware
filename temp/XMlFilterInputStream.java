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
public class XMlFilterInputStream extends InputStream {

    private InputStream istream;
    private int lvl;
    private int lastchar;
    private int len;
    private int nextchar;
    private boolean intag = false;
    private StringBuffer buff = new StringBuffer(16);

    /**
     * Creates a new instance of xmlFilterInputStream
     */
    public XMlFilterInputStream(InputStream is, int l) {
        len = l;
        istream = is;
    }

    public int read() throws IOException {
        if (lastchar == '>' && lvl == 0) {
            return -1;
        }
        int l = lastchar;
        if (nextchar != -1) {
            lastchar = nextchar;
            nextchar = -1;
        } else {
            if (buff.length() > 0) {
                lastchar = buff.charAt(0);
                buff.deleteCharAt(0);
                return lastchar;
            } else {
                lastchar = istream.read();
            }
        }
        if (lastchar == '<') {
            intag = true;
        } else if (lastchar == '>') {
            intag = false;
        }

        if (!intag && lastchar == '&') {
            int amppos = buff.length();
            buff.append((char) lastchar);
            for (int c = 0; c < 10; c++) {
                int ch = istream.read();
                if (ch == -1) {
                    break;
                }
                if (ch == '&') {
                    nextchar = ch;
                    break;
                }
                buff.append((char) ch);
            }
            String s = buff.substring(amppos);
            if (!s.startsWith("&amp;") && !s.startsWith("&lt;") && !s.startsWith("&gt;") && !s.startsWith("&apos;") && !s.startsWith("&quot;") && !s.startsWith("&#")) {
                buff.replace(amppos, amppos + 1, "&amp;");
            }
            return read();
        }

        if (l == '<') {
            intag = true;
            if (lastchar == '/') {
                lvl--;
            } else {
                lvl++;
            }
        }
        len--;
        return lastchar;
    }

    public boolean next() throws IOException {
        while ((nextchar = istream.read()) != -1) {
            if (!Character.isWhitespace(nextchar)) {
                break;
            }
        }
        lvl = 0;
        lastchar = 0;
        return (nextchar != -1);
    }

}
