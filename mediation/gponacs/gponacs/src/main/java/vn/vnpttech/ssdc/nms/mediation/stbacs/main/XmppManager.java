/*
 * Copyright 2015 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import vn.vnpttech.ssdc.nms.mediation.stbacs.common.AppConstants;
import vn.vnpttech.ssdc.nms.mediation.stbacs.exception.ConnectionException;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.Command;
import vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand.CommandRequestFactory;
import vn.vnpttech.ssdc.nms.mediation.stbacs.packet.IQConnectionRequest;
import vn.vnpttech.ssdc.nms.mediation.stbacs.services.NotifyCallback;

/**
 * XMPP Broker makes a request connection to Set Top Box
 *
 * @author Vunb
 * @date Jun 3, 2015
 * @update Jun 3, 2015
 */
public class XmppManager {

    private final Logger logger = Logger.getLogger(XmppManager.class);
    private final StringBuilder sb = new StringBuilder();

    private static final int packetReplyTimeout = 30000; // millis
    private final ExecutorService notifyExecutor = Executors.newCachedThreadPool();
    private static XmppManager INSTANCE;

    private final String server;
    private final int port;

    private NotifyCallback notify;
    private AbstractXMPPConnection connection;

    private String username;
    private String password;

    static {
        //#Use: ProviderFileLoader

        //ProviderManager pm = ProviderManager.getInstance();
        //pm.addIQProvider("ping", "urn:xmpp:ping", new IQPingProvider());
        //pm.addIQProvider("connectionRequest",
        //        "urn:broadband-forum-org:cwmp:xmppConnReq-1-0",
        //       new IQConnectionRequestProvider());
        //ProviderManager.getIQProviders().add(new IQConnectionRequestProvider());
    }

    public XmppManager(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void init(final String username, final String password) throws Exception {

        logger.info(String.format("Initializing connection to server %1$s port %2$d", server, port));

        SmackConfiguration.setDefaultPacketReplyTimeout(packetReplyTimeout);
//        SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
//
//        config = new ConnectionConfiguration(server, port);
//        config.setSASLAuthenticationEnabled(true);
//        config.setSecurityMode(SecurityMode.disabled);
        
        boolean smackDebug = "true".equals(System.getProperty("smack.debug", "false"));

        this.username = username;
        this.password = password;

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username, password)
                .setServiceName(server)
                .setHost(server)
                .setPort(port)
                .setCompressionEnabled(false)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                //.allowEmptyOrNullUsernames()
                .setDebuggerEnabled(smackDebug)
                .setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String string, SSLSession ssls) {
                        return true;
                    }
                })
                .build();

        connection = new XMPPTCPConnection(config);
        connection.setPacketReplyTimeout(packetReplyTimeout);
        connection.connect();

        logger.info("Connected: " + connection.isConnected());
        logger.info("Add filter to listen online/offline state");
        connection.addAsyncStanzaListener(new StanzaListener() {
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {

                try {
                    StringBuilder sb = new StringBuilder();
                    final Presence presence = (Presence) packet;
                    sb.setLength(0);
                    sb.append("presenceChanged: Presence changed status. From=")
                            .append(presence.getFrom())
                            .append(", Status=")
                            .append(presence.getStatus())
                            .append(", Mode=")
                            .append(presence.getMode())
                            .append(", Type=")
                            .append(presence.getType())
                            .append(", Priority=")
                            .append(presence.getPriority());
                    logger.info(sb);
                    //connection.sendStanza(result);
                    final String serialNumber = StringUtils.substringBefore(presence.getFrom(), "@");
                    if (!username.equals(serialNumber)) {
                        notifyExecutor.execute(new Runnable() {
                            public void run() {
                                onNotify(serialNumber, presence);
                            }
                        });
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }, new StanzaTypeFilter(Presence.class));

        // Add packet Listener
        logger.info("Add filter to listen IQConnectionRequest");
        connection.addAsyncStanzaListener(new StanzaListener() {
            public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
                StringBuilder sb = new StringBuilder();
                sb.append("Received IQ.Type.GET, RAW=")
                        .append(packet.toXML());
                logger.debug(sb);

                final IQConnectionRequest iq = (IQConnectionRequest) packet;
                final IQ result = iq.getReply();

                logger.debug("Reply: " + result.toXML());
                connection.sendStanza(result);
            }
        }, new StanzaTypeFilter(IQConnectionRequest.class));

        // Listen for error
        logger.info("Add filter to listen Stanza Error");
        connection.addAsyncStanzaListener(new StanzaListener() {
            public void processPacket(Stanza packet) {
                StringBuilder sb = new StringBuilder();
                sb.append("Received IQ.Type.ERROR, RAW=")
                        .append(packet.toXML());
                logger.debug(sb);
                logger.debug("Error: Can't request to CPE --> Find packet id in command factory: " + packet.getStanzaId());
                Command cm = CommandRequestFactory.getCommandByPacketId(packet.getStanzaId());
                if (cm != null) {
                    String err = packet.getError().getConditionText()
                            + ":"
                            + packet.getError().getCondition()
                            + ":"
                            + packet.getError().getType()
                            + ":"
                            + packet.getError().getDescriptiveText();
                    logger.debug("Found command in factory, serialNumber: " + cm.getSerialNumberCPE() + ", commandID=" + cm.getId() + ", ERROR=" + err);
                    cm.receiveError(new ConnectionException("No connection to the device: " + cm.getSerialNumberCPE()));
                }
            }
        }, new StanzaFilter() {

            public boolean accept(Stanza stanza) {
                return stanza instanceof IQ && ((IQ) stanza).getType() == IQ.Type.error;
            }
        });
//        connection.addAsyncStanzaListener(new StanzaListener() {
//            public void processPacket(Stanza packet) {
//                StringBuilder sb = new StringBuilder();
//                sb.append("Received IQ.Type.RESULT, RAW=")
//                        .append(packet);
//                logger.debug(sb);
//            }
//        }, new StanzaFilter() {
//
//            public boolean accept(Stanza stanza) {
//                return (stanza instanceof IQ)
//                        && !(stanza instanceof UnparsedIQ)
//                        && ((IQ) stanza).getType() == IQ.Type.result;
//            }
//        });

//        logger.info("Add listener to listen online/offline state");
//        Roster roster = Roster.getInstanceFor(connection);
//        roster.addRosterListener(new RosterListener() {
//            public void presenceChanged(Presence presence) {
//                try {
//                    sb.setLength(0);
//                    sb.append("presenceChanged: Presence changed status. From=")
//                            .append(presence.getFrom())
//                            .append(", Status=")
//                            .append(presence.getStatus())
//                            .append(", Mode=")
//                            .append(presence.getMode())
//                            .append(", Type=")
//                            .append(presence.getType())
//                            .append(", Priority=")
//                            .append(presence.getPriority());
//                    logger.info(sb);
//                    String serialNumber = StringUtils.substringBefore(presence.getFrom(), "@");
//                    onNotify(serialNumber, presence);
//                } catch (Exception ex) {
//                    logger.error(ex.getMessage(), ex);
//                }
//            }
//
//            public void entriesAdded(Collection<String> addresses) {
//                logger.info("entriesAdded: WOOOOWH");
//                logger.info(addresses);
//            }
//
//            public void entriesUpdated(Collection<String> addresses) {
//                logger.info("entriesUpdated: WOOOOWH");
//                logger.info(addresses);
//            }
//
//            public void entriesDeleted(Collection<String> addresses) {
//                logger.info("entriesDeleted: WOOOOWH");
//                logger.info(addresses);
//            }
//
//        });
    }

    private void checkConnection() throws Exception {
        if (username == null || password == null) {
            throw new Exception("Username / Password not set");
        } else if (connection == null || !connection.isConnected()) {
            logger.debug("checkConnection: connection is null --> init & retry login");
            init(username, password);
            performLogin();
        } else if (!connection.isAuthenticated()) {
            logger.debug("checkConnection: connection disconnected --> retry login");
            performLogin();
        }
    }

    public void performLogin() throws Exception {
        if (connection != null && connection.isConnected()) {
            logger.info("Perform login: " + username);
            connection.login();
//            this.username = username;
//            this.password = password;
            logger.info("isAuthenticated: " + connection.isAuthenticated());
        } else {
            logger.error("Not connected: " + connection.isConnected());
        }
    }

    public void setStatus(boolean available, String status) throws SmackException.NotConnectedException {

        Presence.Type type = available ? Type.available : Type.unavailable;
        Presence presence = new Presence(type);

        presence.setStatus(status);
        connection.sendStanza(presence);

    }

    /**
     * Send XMPP Connection Request to STB via XMPP Server
     *
     * @param serialNumber
     * @param usernameCPE
     * @param passwordCPE
     * @return packet id of this Connection Request
     * @throws XMPPException
     */
    public String sendConnectionRequest(final String serialNumber, final String usernameCPE, final String passwordCPE
    ) throws XMPPException, Exception {
        String from = String.format("%s@%s/Smack", username, server);
        String to = String.format("%s@%s/Smack", serialNumber, server);
        long timeout = 12000;

        IQConnectionRequest iq = new IQConnectionRequest(usernameCPE, passwordCPE);
        iq.setFrom(from);
        iq.setTo(to);

        sb.setLength(0);
        sb.append("Send Connection Request. From: ")
                .append(from)
                .append(", To: ")
                .append(to)
                .append(", User: ")
                .append(usernameCPE)
                .append("/")
                .append(passwordCPE)
                .append(", Timeout: ")
                .append(timeout)
                .append(", RAW=")
                .append(iq.toXML());
        logger.debug(sb);

        // check connection
        checkConnection();

        // DONT WAIT Server Reply --> Use Async Function
        // SyncPacketSend.getReply(connection, iq, timeout);
        connection.sendStanza(iq);
        return iq.getStanzaId();
    }

    public static XmppManager getInstance() throws Exception {
        if (INSTANCE == null) {

            String username = System.getProperty(AppConstants.XMPP_ACS_USERNAME, "acsmaster");
            String password = System.getProperty(AppConstants.XMPP_ACS_PASSWORD, "acsmaster");
            String server = System.getProperty(AppConstants.XMPP_SERVER_ADDRESS, "10.84.8.33");
            String port = System.getProperty(AppConstants.XMPP_SERVER_PORT, "5222"); //port: 8222

            XmppManager xmpp = new XmppManager(server, Integer.parseInt(port));

            xmpp.init(username, password);
            xmpp.performLogin();
            xmpp.setStatus(true, "Hello users. I am stb master!");

//            String buddyJID = "vunb";
//            String buddyName = "Vunb";
//            xmpp.createEntry(buddyJID, buddyName);
//
//            xmpp.sendMessage("Hello mate", "vunb");
//            xmpp.sendConnectionRequest("vunb", "test", "test", null);
            INSTANCE = xmpp;
        }
        INSTANCE.checkConnection();

        return INSTANCE;

    }

    private void onNotify(String serialNumber, Object status) {
        if (this.notify != null) {
            this.notify.onNotify(serialNumber, status);
        }
    }

    public void setStatusChangeListener(final NotifyCallback callback) {
        this.notify = callback;
    }

    public void destroy() {
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    public void createEntry(String user, String name) throws Exception {
        logger.debug(String.format("Creating entry for buddy '%1$s' with name %2$s", user, name));
        Roster roster = Roster.getInstanceFor(connection);
        roster.createEntry(user, name, null);
    }

    public Roster.SubscriptionMode getSubscriptionMode() {
        Roster roster = Roster.getInstanceFor(connection);
        return roster.getSubscriptionMode();
    }

    public static class XmppFriend {

        public static final int OFFLINE = 0;
        public static final int ONLINE = 1;
        public static final int AWAY = 2;
        public static final int BUSY = 3;
    }

    public int getUserStatus(String userId) {

        String jid = String.format("%s@%s", userId, this.server);
        Roster roster = Roster.getInstanceFor(connection);
        Presence user = roster.getPresence(jid);
        logger.debug("getUserStatus: " + userId);
        logger.debug(user);
        return getUserStatus(user);
    }

    public static int getUserStatus(Presence presence) {
        if (presence == null) {
            return XmppFriend.OFFLINE;
        }

        // check user online
        Presence.Mode mode = presence.getMode();
        if (mode == Presence.Mode.dnd) {
            return XmppFriend.BUSY;
        } else if (mode == Presence.Mode.away || mode == Presence.Mode.xa) {
            return XmppFriend.AWAY;
        } else if (presence.isAvailable()) {
            return XmppFriend.ONLINE;
        } else {
            return XmppFriend.OFFLINE;
        }
    }
}
