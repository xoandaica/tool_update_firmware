/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.packet;

import org.jivesoftware.smack.packet.IQ;

/**
 * IQConnectionRequest CPE
 *
 * @author Vunb
 * @date Jun 5, 2015
 * @update Jun 5, 2015
 */
public class IQConnectionRequest extends IQ {

    public static final String ELEMENT = "connectionRequest";
    public static final String NAMESPACE = "urn:broadband-forum-org:cwmp:xmppConnReq-1-0";

    protected String username;
    protected String password;

    public IQConnectionRequest() {
        super(ELEMENT, NAMESPACE);
    }

    /**
     * CPE Username
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    @Override
//    public String getChildElementXML() {
//        String query
//                = "<connectionRequest xmlns=\"urn:broadband-forum-org:cwmp:xmppConnReq-1-0\">"
//                + "<username>%s</username>"
//                + "<password>%s</password>"
//                + "</connectionRequest>";
//        return String.format(query, username, password);
//    }
    /**
     * Create new IQ Connection Request to CPE
     *
     * @param username CPE Username
     * @param password CPE Password
     */
    public IQConnectionRequest(String username, String password) {
        this();
        this.username = username;
        this.password = password;
        this.setType(Type.get);
    }

    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {
        xml.rightAngleBracket();
        xml.append("<username>").append(username).append("</username>");
        xml.append("<password>").append(password).append("</password>");
        return xml;
    }

    public IQ getReply() {
        return createResultIQ(this);
    }

    public static IQConnectionRequest createResponse(IQ request) {
        IQConnectionRequest res = new IQConnectionRequest();
        res.setType(Type.result);
        res.setTo(request.getFrom());
        res.setUsername("ok");
        res.setPassword("ok");
        return res;
    }
}
