package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class CommandRequestFactory {

    private static final ArrayList<Command> listCommand = new ArrayList<Command>();
    private static final Logger logger = Logger.getLogger(CommandRequestFactory.class.getName());
    protected static final StringBuilder sb = new StringBuilder();

    public static ArrayList<Command> getInstance() {
        return listCommand;
    }

    public static boolean removeCommand(Command cm) {
        for (int i = 0; i < listCommand.size(); i++) {
            if (listCommand.get(i).getSerialNumberCPE().equals(cm.getSerialNumberCPE())) {
                getInstance().remove(i);
                long timenow = System.currentTimeMillis();
                sb.setLength(0);
                sb.append("Remove command: SerialNumber=")
                        .append(cm.getSerialNumberCPE())
                        .append(", TimeExist: ")
                        .append(timenow - cm.timeExist);
                logger.info(sb);
                return true;
            }
        }
        return false;
    }

    public static boolean addCommand(Command cm) {
        for (Command cmd : listCommand) {
            if (cmd.getSerialNumberCPE().equals(cm.getSerialNumberCPE())) {
                return false;
            }
        }
        cm.timeExist = System.currentTimeMillis();
        sb.setLength(0);
        sb.append("Add command: SerialNumber=")
                .append(cm.getSerialNumberCPE())
                .append(", CurrentTime: ")
                .append(cm.timeExist);
        logger.info(sb);
        getInstance().add(cm);
        return true;
    }

    public static Command getCommand(String serialNumberCPE) {
        Command returnValue = null;
        for (Command cmd : listCommand) {
            if (cmd.getSerialNumberCPE().equals(serialNumberCPE)) {
                returnValue = cmd;
            }
        }
        long timeout = getTimeoutConfig();
        for (Command cmd : listCommand) {
            long timenow = System.currentTimeMillis();
            // check timeout
            if ((timenow - cmd.timeExist) > timeout) {
                sb.setLength(0);
                sb.append("Timeout: SerialNumber=")
                        .append(cmd.getSerialNumberCPE());
                logger.info(sb);

                removeCommand(cmd);
            }
        }
        return returnValue;
    }

    public static Command getCommandByPacketId(String packetid) {
        Command returnValue = null;
        if (packetid != null) {
            for (Command cm : listCommand) {
                if (packetid.equals(cm.getId())) {
                    returnValue = cm;
                }
            }
        }
        long timeout = getTimeoutConfig();
        for (Command cm : listCommand) {
            long timenow = System.currentTimeMillis();
            // check timeout
            if ((timenow - cm.timeExist) > timeout) {
                sb.setLength(0);
                sb.append("Timeout: SerialNumber=")
                        .append(cm.getSerialNumberCPE());
                logger.info(sb);

                removeCommand(cm);
            }
        }

        return returnValue;
    }

    private static long getTimeoutConfig() {
        // Default wait 30 minutes
        String timeoutCfg = System.getProperty("CommandRequestFactory.timeout", "1800000");
        long timeout = Long.parseLong(timeoutCfg);

        return timeout;
    }
}
