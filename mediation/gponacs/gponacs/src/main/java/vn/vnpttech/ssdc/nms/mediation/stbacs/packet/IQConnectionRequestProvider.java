/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.packet;

import java.io.IOException;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author Vunb
 * @date Jun 23, 2015
 * @update Jun 23, 2015
 */
public class IQConnectionRequestProvider extends IQProvider<IQConnectionRequest> {

    public IQ parseIQ(XmlPullParser parser) throws Exception {

        String username = "";
        String password = "";

        // Start parsing loop
        int eventType = parser.next();
        outerloop:
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String elementName = parser.getName();
                    if ("username".equals(elementName)) {
                        username = parser.nextText();
                    } else if ("password".equals(elementName)) {
                        password = parser.nextText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getDepth() == 3) {
                        break outerloop;
                    }
                    break;
            }
            eventType = parser.next();
        }
        return new IQConnectionRequest(username, password);
    }

    @Override
    public IQConnectionRequest parse(XmlPullParser parser, int initialDepth)
            throws XmlPullParserException,
            IOException, SmackException {
        return parse(parser);
    }

}
