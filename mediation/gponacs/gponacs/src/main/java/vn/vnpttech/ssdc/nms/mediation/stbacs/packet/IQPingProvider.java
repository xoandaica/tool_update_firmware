///*
// * Copyright 2015 VNPT-Technology. All rights reserved.
// * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
// */
//package vn.vnpttech.ssdc.nms.mediation.stbacs.packet;
//
//import org.jivesoftware.smack.packet.IQ;
//import org.jivesoftware.smack.provider.IQProvider;
//import org.xmlpull.v1.XmlPullParser;
//
///**
// *
// * @author Vunb
// * @date Jul 13, 2015
// * @update Jul 13, 2015
// */
//public class IQPingProvider extends IQ implements IQProvider {
//
//    @Override
//    public String getChildElementXML() {
//        return "<ping xmlns='urn:xmpp:ping'/>";
//    }
//
//    public IQ parseIQ(XmlPullParser parser) throws Exception {
//        return new IQPingProvider();
//    }
//
//}
