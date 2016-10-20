/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.main;

import vn.vnpttech.ssdc.nms.mediation.stbacs.ACSServlet;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author Vunb
 * @date Jun 4, 2014
 * @update Jun 4, 2014
 */
public class ACSServletPublisher {

    protected static final Logger logger = Logger.getLogger(ACSServletPublisher.class.getSimpleName());
    protected static final StringBuilder sb = new StringBuilder();

    public static final String NMS_ACS_HOSTNAME = "nms_acs.hostname";
    public static final String NMS_ACS_PORT = "nms_acs.port";

    public static void main(String[] args) throws Exception {
        ACSServletPublisher.publishACS();
    }

    public static void publishACS() throws Exception {
        String getPort = System.getProperty(NMS_ACS_PORT, "8099");
        int port = Integer.parseInt(getPort);
        Server server = new Server(port);
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/", true, false);
        servletContextHandler.addServlet(ACSServlet.class, "/");
        server.start();
        sb.setLength(0);
        sb.append("ACS Servlet started, on PORT: ").append(port);
        logger.info(sb);
    }
}
