/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpt.tr069.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 *
 * @author Zan
 */
public class CharsetConverterInputStream extends InputStream {

    private InputStream in;
    private PipedInputStream pipein;
    private OutputStream pipeout;
    private Reader r;
    private Writer w;

    public CharsetConverterInputStream(String csFrom, String csTo, InputStream in) throws UnsupportedEncodingException, IOException {
        this.in = in;
        r = new InputStreamReader(in, csFrom);
        pipein = new PipedInputStream();
        pipeout = new PipedOutputStream(pipein);
        w = new OutputStreamWriter(pipeout, csTo);
    }

    @Override
    public int read() throws IOException {
        if (pipein.available() > 0) {
            return pipein.read();
        }
        int c = r.read();
        if (c == -1) {
            return -1;
        }
        w.write(c);
        w.flush();
        return pipein.read();
    }
}
