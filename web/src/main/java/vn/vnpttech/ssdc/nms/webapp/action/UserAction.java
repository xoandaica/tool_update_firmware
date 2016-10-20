package vn.vnpttech.ssdc.nms.webapp.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import vn.vnpttech.ssdc.nms.Constants;
import vn.vnpttech.ssdc.nms.criteria.UserDisplay;
import vn.vnpttech.ssdc.nms.model.ActionTypeEnum;
import vn.vnpttech.ssdc.nms.model.Role;
import vn.vnpttech.ssdc.nms.model.User;
import vn.vnpttech.ssdc.nms.service.UserExistsException;
import vn.vnpttech.ssdc.nms.webapp.util.RequestUtil;

import com.opensymphony.xwork2.Preparable;
import org.apache.commons.lang.StringUtils;

/**
 * Action for facilitating User Management feature.
 */
public class UserAction extends BaseAction implements Preparable {

    private static final long serialVersionUID = 6776558938712115191L;
    private List<User> users;
    private User user;
    private String id;
    private String query;

    private UserDisplay searchCriteria;
    private Long[] deleteIds;

    /**
     * Grab the entity from the database before populating with request
     * parameters
     */
    public void prepare() {
        // prevent failures on new
        /*if (getRequest().getMethod().equalsIgnoreCase("post") && (!"".equals(getRequest().getParameter("user.id")))) {
         user = userManager.getUser(getRequest().getParameter("user.id"));
         }*/
    }

    /**
     * Holder for users to display on list screen
     *
     * @return list of users
     */
    public List<User> getUsers() {
        return users;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQ(String q) {
        this.query = q;
    }

    public UserDisplay getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(UserDisplay searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public void setDeleteIds(Long[] deleteIds) {
        this.deleteIds = deleteIds;
    }

    /**
     * Delete the user passed in.
     *
     * @return success
     */
    public String delete() {
        userManager.removeUser(user.getId().toString());
        List<Object> args = new ArrayList<Object>();
        args.add(user.getFullName());
        saveMessage(getText("user.deleted", args));

        return SUCCESS;
    }

    /**
     * Grab the user from the database based on the "id" passed in.
     *
     * @return success if user found
     * @throws IOException can happen when sending a "forbidden" from
     * response.sendError()
     */
    public String edit() throws IOException {
        HttpServletRequest request = getRequest();
        boolean editProfile = request.getRequestURI().contains("editProfile");

        // if URL is "editProfile" - make sure it's the current user
        if (editProfile && ((request.getParameter("id") != null) || (request.getParameter("from") != null))) {
            ServletActionContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            log.warn("User '" + request.getRemoteUser() + "' is trying to edit user '"
                    + request.getParameter("id") + "'");
            return null;
        }

        // if a user's id is passed in
        if (id != null) {
            // lookup the user using that id
            user = userManager.getUser(id);
        } else if (editProfile) {
            user = userManager.getUserByUsername(request.getRemoteUser());
        } else {
            user = new User();
            user.addRole(new Role(Constants.USER_ROLE));
        }

        if (user.getUsername() != null) {
            user.setConfirmPassword(user.getPassword());

            // if user logged in with remember me, display a warning that they can't change passwords
            log.debug("checking for remember me login...");

            AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
            SecurityContext ctx = SecurityContextHolder.getContext();

            if (ctx != null) {
                Authentication auth = ctx.getAuthentication();

                if (resolver.isRememberMe(auth)) {
                    getSession().setAttribute("cookieLogin", "true");
                    saveMessage(getText("userProfile.cookieLogin"));
                }
            }
        }

        return SUCCESS;
    }

    /**
     * Default: just returns "success"
     *
     * @return "success"
     */
    public String execute() {
        return SUCCESS;
    }

    /**
     * Sends users to "home" when !from.equals("list"). Sends everyone else to
     * "cancel"
     *
     * @return "home" or "cancel"
     */
    public String cancel() {
        if (!"list".equals(from)) {
            return "home";
        }
        return "cancel";
    }

    /**
     * Save user
     *
     * @return success if everything worked, otherwise input
     * @throws Exception when setting "access denied" fails on response
     */
    public String save() throws Exception {

        Integer originalVersion = user.getVersion();

        boolean isNew = ("".equals(getRequest().getParameter("user.version")));
        // only attempt to change roles if user is admin
        // for other users, prepare() method will handle populating
        if (getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
            user.getRoles().clear(); // APF-788: Removing roles from user doesn't work
            String[] userRoles = getRequest().getParameterValues("userRoles");

            for (int i = 0; userRoles != null && i < userRoles.length; i++) {
                String roleName = userRoles[i];
                try {
                    user.addRole(roleManager.getRole(roleName));
                } catch (DataIntegrityViolationException e) {
                    return showUserExistsException(originalVersion);
                }
            }
        }

        try {
            userManager.saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(ade.getMessage());
            getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        } catch (UserExistsException e) {
            return showUserExistsException(originalVersion);
        }

        if (!"list".equals(from)) {
            // add success messages
            saveMessage(getText("user.saved"));
            return "home";
        } else {
            // add success messages
            List<Object> args = new ArrayList<Object>();
            args.add(user.getFullName());
            if (isNew) {
                saveMessage(getText("user.added", args));
                // Send an account information e-mail
                mailMessage.setSubject(getText("signup.email.subject"));
                try {
                    sendUserMessage(user, getText("newuser.email.message", args), RequestUtil.getAppURL(getRequest()));
                } catch (MailException me) {
                    addActionError(me.getCause().getLocalizedMessage());
                }
                return SUCCESS;
            } else {
                user.setConfirmPassword(user.getPassword());
                saveMessage(getText("user.updated.byAdmin", args));
                return INPUT;
            }
        }
    }

    private String showUserExistsException(Integer originalVersion) {
        List<Object> args = new ArrayList<Object>();
        args.add(user.getUsername());
        args.add(user.getEmail());
        addActionError(getText("errors.existing.user", args));

        // reset the version # to what was passed in
        user.setVersion(originalVersion);
        // redisplay the unencrypted passwords
        user.setPassword(user.getConfirmPassword());
        return INPUT;
    }

    /**
     * Fetch all users from database and put into local "users" variable for
     * retrieval in the UI.
     *
     * @return "success" if no exceptions thrown
     */
    public String list() {
        /*try {
         users = userManager.search(query);
         List<ActionLogs> list = logManager.getAll();
            
         System.out.println(list.size());
         } catch (Exception se) {
         se.printStackTrace();
         addActionError(se.getMessage());
         users = userManager.getUsers();
         }*/
        return SUCCESS;
    }

    /**
     * Get user list
     *
     *
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     * @throws Exception
     */
    public InputStream getUserList() throws JSONException, UnsupportedEncodingException, Exception {
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();

        int totalCount = 0;

        try {

            //Get total Count
            totalCount = userManager.countUsers(searchCriteria);

            List<User> list = userManager.searchCriteria(searchCriteria);

            log.info(totalCount + " users");

            if (list != null && list.size() > 0) {
                for (User item : list) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", item.getId());
                    obj.put("username", item.getUsername());
                    obj.put("firstName", item.getFirstName());
                    obj.put("lastName", item.getLastName());
                    obj.put("enabled", item.isEnabled());
                    obj.put("password", item.getPassword());
                    obj.put("email", item.getEmail());
                    obj.put("phoneNumber", item.getPhoneNumber());
                    obj.put("department", item.getDepartment());
                    obj.put("description", item.getDescription());
                    obj.put("version", item.getVersion());
                    obj.put("confirmPassword", item.getPassword());
                    //user-role
                    Set<Role> roles = item.getRoles();
                    JSONArray roleJsonArray = new JSONArray();
                    if (!roles.isEmpty()) {
                        Iterator<Role> iterator = roles.iterator();
                        while (iterator.hasNext()) {
                            Role role = iterator.next();

                            JSONObject roleJsonObj = new JSONObject();
                            roleJsonObj.put("id", role.getId());
                            roleJsonObj.put("name", role.getName());

                            roleJsonArray.put(roleJsonObj);
                        }
                    }
                    obj.put("roles", roleJsonArray);
                    array.put(obj);
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e);
        }

        JSONObject data = new JSONObject();
        data.put("totalCount", totalCount);
        data.put("list", array);
        result.put("result", data);

        return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
    }

    /**
     * Create or update
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public InputStream getCreateOrUpdateUser() throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        try {
            // only attempt to change roles if user is admin
            // for other users, prepare() method will handle populating
            boolean isNew = ("".equals(getRequest().getParameter("user.version")));
            if (isNew) {
                User userRecord = userManager.getUserByUsernameOrEmail(user.getUsername(), user.getEmail());
                if (userRecord != null) {
                    result.put(SUCCESS, false);
                    if (userRecord.getUsername().equals(user.getUsername())) {
                        result.put("msg", getText("management.user.existUsername"));
                    } else {
                        result.put("msg", getText("management.user.existEmail"));
                    }

                }

                if (userRecord != null) {
                    return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
                }
            }

            if (getRequest().isUserInRole(Constants.ADMIN_ROLE)) {
                user.getRoles().clear(); // APF-788: Removing roles from user doesn't work
                String[] userRoles = getRequest().getParameterValues("userRoles");

                for (int i = 0; userRoles != null && i < userRoles.length; i++) {
                    String roleId = userRoles[i];
                    user.addRole(roleManager.get(Long.parseLong(roleId)));
                }
            }

            user.setEnabled(true);

            userManager.saveUser(user);

            if (isNew) {
                saveActionLogs(User.class.getSimpleName(), ActionTypeEnum.CREATE, user.toString());
                log.info(ActionTypeEnum.CREATE.getName() + " " + user.toString());
            } else {
                saveActionLogs(User.class.getSimpleName(), ActionTypeEnum.UPDATE, user.toString());
                log.info(ActionTypeEnum.UPDATE.getName() + " " + user.toString());
            }

            result.put(SUCCESS, true);
            result.put("msg", "Update successful!");

        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e);
            //return new ByteArrayInputStream("".toString().getBytes());
        }

        return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
    }

    /**
     * Delete users
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public InputStream getDeleteUser() throws UnsupportedEncodingException {
        JSONObject result = new JSONObject();
        try {
            boolean all = true;
            if (deleteIds.length > 0) {
                for (int i = 0; i < deleteIds.length; i++) {
                    //check is admin user
                    if (deleteIds[i] == -2) {
                        result.put(SUCCESS, false);
                        result.put("msg", getText("management.user.notDeleteAdmin"));
                        all = false;
                        continue;
                    }

                    //check is current user
                    if (getCurrentUser().getId().equals(deleteIds[i])) {
                        result.put(SUCCESS, false);
                        result.put("msg", getText("management.user.notDeleteAdmin"));
                        all = false;
                        continue;
                    }

                    userManager.remove(deleteIds[i]);
                }
            }

            saveActionLogs(User.class.getSimpleName(), ActionTypeEnum.DELETE, "Delete users");
            log.info(ActionTypeEnum.DELETE.getName() + " users");

            if (all) {
                result.put(SUCCESS, true);
                result.put("msg", "Delete successful!");
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e);
            //return null;
        }

        return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
    }

    public InputStream getListRoles() throws JSONException, UnsupportedEncodingException {
        JSONArray array = new JSONArray();
        JSONObject result = new JSONObject();
        try {
            log.info("Get List Roles");
            List<Role> roles = roleManager.getAll();
            if (roles != null) {
                for (Role role : roles) {
                    JSONObject item = new JSONObject();
                    item.put("name", role.getName());
                    item.put("id", role.getId());

                    array.put(item);
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            log.error(e);
        }

        JSONObject data = new JSONObject();

        data.put("list", array);

        result.put("result", data);

        return new ByteArrayInputStream(result.toString().getBytes("UTF8"));

    }

    public InputStream getResetPassword() {
        JSONObject result = new JSONObject();
        try {
            result.put(SUCCESS, false);
            if (id != null) {
                // lookup the user using that id
                user = userManager.getUser(id);
                if (user != null) {
                    user.setPassword("abc13579");
                    userManager.saveUser(user);

                    result.put(SUCCESS, true);
                }
            }

            saveActionLogs(User.class.getSimpleName(), ActionTypeEnum.RESET_PASSWORD, user.toString());
            log.info(ActionTypeEnum.RESET_PASSWORD.getName() + " " + user.toString());

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            //return null;
        }

        return new ByteArrayInputStream(result.toString().getBytes());
    }

    public InputStream getLoadCurrentUserProfile() {
        try {
            JSONObject result = new JSONObject();
            User currentUser = getCurrentUser();
            JSONObject obj = new JSONObject();
            obj.put("id", currentUser.getId());
            obj.put("username", currentUser.getUsername());
            obj.put("firstName", currentUser.getFirstName());
            obj.put("lastName", currentUser.getLastName());
            obj.put("enabled", currentUser.isEnabled());
            obj.put("password", currentUser.getPassword());
            obj.put("email", currentUser.getEmail());
            obj.put("phoneNumber", currentUser.getPhoneNumber());
            obj.put("department", currentUser.getDepartment());
            obj.put("description", currentUser.getDescription());
            obj.put("version", currentUser.getVersion());
            obj.put("confirmPassword", currentUser.getPassword());

            Set<Role> roles = currentUser.getRoles();
            String roleList = "";
            if (!roles.isEmpty()) {
                Iterator<Role> iterator = roles.iterator();
                while (iterator.hasNext()) {
                    Role role = iterator.next();
                    roleList += role.getName() + ",";
                }
                roleList = roleList.substring(0, roleList.length() - 1);
            }
            obj.put("roles", roleList);
            result.put("currentUserProfile", obj);
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getLoadCurrentUserProfile: ", ex);
            return null;
        }
    }

    public InputStream getSaveCurrentUserProfile() {
        try {
            JSONObject result = new JSONObject();
            HttpServletRequest request = getRequest();
            String userId = request.getParameter("userId");
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String department = request.getParameter("department");
            String description = request.getParameter("description");
            if (StringUtils.isEmpty(userId)) {
                result.put(SUCCESS, false);
            } else {
                User saveUser = userManager.get(Long.parseLong(userId));
                saveUser.setEnabled(true);
                saveUser.setFirstName(firstName);
                saveUser.setLastName(lastName);
                saveUser.setPassword(password);
                saveUser.setPhoneNumber(phone);
                saveUser.setDepartment(department);
                saveUser.setDescription(description);
                userManager.saveUser(saveUser);
                saveActionLogs(User.class.getSimpleName(), ActionTypeEnum.UPDATE, saveUser.toString());
                log.info(ActionTypeEnum.UPDATE.getName() + " " + saveUser.toString());
                result.put(SUCCESS, true);
            }
            return new ByteArrayInputStream(result.toString().getBytes("UTF8"));
        } catch (Exception ex) {
            log.error("ERROR getSaveCurrentUserProfile: ", ex);
            return null;
        }
    }
}
