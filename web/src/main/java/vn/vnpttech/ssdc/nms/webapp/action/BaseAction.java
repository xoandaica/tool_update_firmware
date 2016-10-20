package vn.vnpttech.ssdc.nms.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import vn.vnpttech.ssdc.nms.Constants;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.model.User;
import vn.vnpttech.ssdc.nms.service.ActionLogsManager;
import vn.vnpttech.ssdc.nms.service.AreaManager;
import vn.vnpttech.ssdc.nms.service.DeviceManager;
import vn.vnpttech.ssdc.nms.service.DeviceModelManager;
import vn.vnpttech.ssdc.nms.service.FirmwareManager;
import vn.vnpttech.ssdc.nms.service.MailEngine;
import vn.vnpttech.ssdc.nms.service.PolicyManager;
import vn.vnpttech.ssdc.nms.service.RoleManager;
import vn.vnpttech.ssdc.nms.service.UserManager;

import com.opensymphony.xwork2.ActionSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vn.vnpttech.ssdc.nms.service.PolicyHistoryManager;

public class BaseAction extends ActionSupport {

    private static final long serialVersionUID = 3525445612504421307L;

    /**
     * Constant for cancel result String
     */
    public static final String CANCEL = "cancel";

    /**
     * Transient log to prevent session synchronization issues - children can
     * use instance for logging.
     */
    protected final transient Log log = LogFactory.getLog(getClass());

    protected Long start;
    protected Long limit;
    /**
     * The UserManager
     */
    protected UserManager userManager;

    /**
     * The RoleManager
     */
    protected RoleManager roleManager;

    /**
     * The ActionLogsManager
     */
    @Autowired
    protected ActionLogsManager logManager;

    /**
     * The AreaManager
     */
    @Autowired
    protected AreaManager areaManager;
    /**
     * The FirmwareManager
     */
    @Autowired
    protected FirmwareManager firmwareManager;

    /**
     * The PolicyHistoryManager
     */
    @Autowired
    protected PolicyHistoryManager policyHistoryManager;

    /**
     * Set to "page" when a "page" request parameter is passed in protected
     * Integer page;
     *
     * /** The DeviceModelManager
     */
    @Autowired
    protected DeviceModelManager deviceModelManager;

    /**
     * The DeviceManager
     */
    @Autowired
    protected DeviceManager deviceManager;

    /**
     * The DeviceModelManager
     */
    @Autowired
    protected PolicyManager policyManager;

    /**
     * Indicator if the user clicked cancel
     */
    protected String cancel;

    /**
     * Indicator for the page the user came from.
     */
    protected String from;

    /**
     * Set to "delete" when a "delete" request parameter is passed in
     */
    protected String delete;

    /**
     * Set to "save" when a "save" request parameter is passed in
     */
    protected String save;

    /**
     * MailEngine for sending e-mail
     */
    protected MailEngine mailEngine;

    /**
     * A message pre-populated with default data
     */
    protected SimpleMailMessage mailMessage;

    /**
     * Velocity template to use for e-mailing
     */
    protected String templateName;

    /**
     * Simple method that returns "cancel" result
     *
     * @return "cancel"
     */
    public String cancel() {
        return CANCEL;
    }

    /**
     * Save the message in the session, appending if messages already exist
     *
     * @param msg the message to put in the session
     */
    @SuppressWarnings("unchecked")
    protected void saveMessage(String msg) {
        List messages = (List) getRequest().getSession().getAttribute(
                "messages");
        if (messages == null) {
            messages = new ArrayList();
        }
        messages.add(msg);
        getRequest().getSession().setAttribute("messages", messages);
    }

    /**
     * Convenience method to get the Configuration HashMap from the servlet
     * context.
     *
     * @return the user's populated form from the session
     */
    protected Map getConfiguration() {
        Map config = (HashMap) getSession().getServletContext().getAttribute(
                Constants.CONFIG);
        // so unit tests don't puke when nothing's been set
        if (config == null) {
            return new HashMap();
        }
        return config;
    }

    /**
     * Convenience method to get the request
     *
     * @return current request
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * Convenience method to get the response
     *
     * @return current response
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    /**
     * Convenience method to get the session. This will create a session if one
     * doesn't exist.
     *
     * @return the session from the request (request.getSession()).
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * Convenience method to send e-mail to users
     *
     * @param user the user to send to
     * @param msg the message to send
     * @param url the URL to the application (or where ever you'd like to send
     * them)
     */
    protected void sendUserMessage(User user, String msg, String url) {
        if (log.isDebugEnabled()) {
            log.debug("sending e-mail to user [" + user.getEmail() + "]...");
        }

        mailMessage.setTo(user.getFullName() + "<" + user.getEmail() + ">");

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
		// TODO: figure out how to get bundle specified in struts.xml
        // model.put("bundle", getTexts());
        model.put("message", msg);
        model.put("applicationURL", url);
        mailEngine.sendMessage(mailMessage, templateName, model);
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void setLogManager(ActionLogsManager logManager) {
        this.logManager = logManager;
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public void setAreaManager(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public PolicyManager getPolicyManager() {
        return policyManager;
    }

    public void setPolicyManager(PolicyManager policyManager) {
        this.policyManager = policyManager;
    }

    /**
     * Convenience method for setting a "from" parameter to indicate the
     * previous page.
     *
     * @param from indicator for the originating page
     */
    public void setFrom(String from) {
        this.from = from;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public DeviceModelManager getDeviceModelManager() {
        return deviceModelManager;
    }

    public void setDeviceModelManager(DeviceModelManager deviceModelManager) {
        this.deviceModelManager = deviceModelManager;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    /**
     * Log action to database
     *
     * @param actionObject
     * @param actionType
     * @param description
     */
    public void saveActionLogs(String actionObject, ActionTypeEnum actionType,
            String description) {
        String username = this.getRequest().getRemoteUser();
        logManager.saveActionLog(username, actionObject, actionType,
                description);
    }

    protected User getCurrentUser() {
        User currentUser = null;
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx.getAuthentication() != null) {
            Authentication auth = ctx.getAuthentication();
            if (auth.getPrincipal() instanceof UserDetails) {
                currentUser = (User) auth.getPrincipal();
            } else if (auth.getDetails() instanceof UserDetails) {
                currentUser = (User) auth.getDetails();
            } else {
                throw new AccessDeniedException("User not properly authenticated.");
            }
        }

        return currentUser;
    }

}
