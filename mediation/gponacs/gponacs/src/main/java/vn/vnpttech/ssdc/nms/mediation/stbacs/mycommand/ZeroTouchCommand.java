package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

public class ZeroTouchCommand extends Command {

    public int orderCmd = 0;

    public ZeroTouchCommand() {
        this.type = Command.TYPE_ZERO_TOUCH;
    }

    @Override
    public boolean executeCommand() throws Exception {
        CommandRequestFactory.addCommand(this);
        return errorCheck;
    }

    @Override
    public void receiveError(String errorString) {
        this.errorCheck = true;
        this.errorString = errorString;
        CommandRequestFactory.removeCommand(this);
        sb.setLength(0);
        sb.append("*******************************************************")
                .append("\n* receiveError: ")
                .append(errorString)
                .append("\n*************************************************************");
        logger.error(sb);
    }
    
    
}
