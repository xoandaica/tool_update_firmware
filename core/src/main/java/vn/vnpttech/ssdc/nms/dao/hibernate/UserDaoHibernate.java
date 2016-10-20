package vn.vnpttech.ssdc.nms.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import vn.vnpttech.ssdc.nms.criteria.UserDisplay;
import vn.vnpttech.ssdc.nms.dao.UserDao;
import vn.vnpttech.ssdc.nms.model.User;

/**
 * This class interacts with Hibernate session to save/delete and retrieve User
 * objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 * Extended to implement Acegi UserDetailsService interface by David Carter
 * david@carter.net Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * to work with the new BaseDaoHibernate implementation that uses generics.
 * Modified by jgarcia (updated to hibernate 4)
 */
@Repository("userDao")
public class UserDaoHibernate extends GenericDaoHibernate<User, Long> implements UserDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoHibernate() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        Query qry = getSession().createQuery("from User u order by upper(u.username)");
        return qry.list();
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getId());
        }
        getSession().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getSession().flush();
        return user;
    }

    /**
     * Overridden simply to call the saveUser method. This is happening because
     * saveUser flushes the session and saveObject of BaseDaoHibernate does not.
     *
     * @param user the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public User save(User user) {
        return this.saveUser(user);
    }

    /**
     * {@inheritDoc}
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List users = getSession().createCriteria(User.class).add(Restrictions.eq("username", username)).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getUserPassword(Long userId) {
        JdbcTemplate jdbcTemplate
                = new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select password from " + table.name() + " where id=?", String.class, userId);
    }

    @SuppressWarnings("unchecked")
    public User getUserByUsernameOrEmail(String username, String email) {
        List<User> users = getSession().createCriteria(User.class)
                .add(Restrictions.or(Restrictions.eq("username", username), Restrictions.eq("email", email)))
                .list();
        if (users == null || users.isEmpty()) {
            return null;
        } else {
            return (User) users.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> searchCriteria(UserDisplay searchCriteria) {
        if (searchCriteria == null) {
            searchCriteria = new UserDisplay();
        }

        Criteria crit = getSession().createCriteria(User.class);

        crit.setProjection(Projections.distinct(Projections.id()));
        //crit.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );

        if (searchCriteria.getLimit() != null) {
            crit.setMaxResults(searchCriteria.getLimit());
        }

        if (searchCriteria.getStart() != null) {
            crit.setFirstResult(searchCriteria.getStart());
        }

        if (StringUtils.isNotBlank(searchCriteria.getFirstName())) {
            // crit.add(Restrictions.like("firtName", "%" + searchCriteria.getFirstName() + "%"));
            crit.add(Restrictions.like("firstName", searchCriteria.getFirstName(), MatchMode.ANYWHERE).ignoreCase());
        }

        if (StringUtils.isNotBlank(searchCriteria.getLastName())) {
            //crit.add(Restrictions.like("lastName", "%" + searchCriteria.getLastName() + "%"));
            crit.add(Restrictions.like("lastName", searchCriteria.getLastName(), MatchMode.ANYWHERE).ignoreCase());
        }

        if (StringUtils.isNotBlank(searchCriteria.getUsername())) {
//            crit.add(Restrictions.like("username", "%" + searchCriteria.getUsername() + "%"));
            crit.add(Restrictions.like("username", searchCriteria.getUsername(), MatchMode.ANYWHERE).ignoreCase());
        }

        if (StringUtils.isNotBlank(searchCriteria.getDepartment())) {
            //crit.add(Restrictions.like("department", "%" + searchCriteria.getDepartment() + "%"));
            crit.add(Restrictions.like("department", searchCriteria.getDepartment(), MatchMode.ANYWHERE).ignoreCase());

        }

        if (StringUtils.isNotBlank(searchCriteria.getStatus())) {
            crit.add(Restrictions.eq("enabled",
                    searchCriteria.getStatus().equals("1") ? Boolean.TRUE : Boolean.FALSE));
        }

        List<?> ids = crit.list();

        crit = getSession().createCriteria(User.class)
                .add(Restrictions.in("id", ids))
                .addOrder(Order.asc("username"))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        return crit.list();
    }

    @Override
    public int countUsers(UserDisplay searchCriteria) {
        if (searchCriteria == null) {
            searchCriteria = new UserDisplay();
        }

        Criteria crit = getSession().createCriteria(User.class);

         if (StringUtils.isNotBlank(searchCriteria.getFirstName())) {
            // crit.add(Restrictions.like("firtName", "%" + searchCriteria.getFirstName() + "%"));
            crit.add(Restrictions.like("firstName", searchCriteria.getFirstName(), MatchMode.ANYWHERE).ignoreCase());
        }

        if (StringUtils.isNotBlank(searchCriteria.getLastName())) {
            //crit.add(Restrictions.like("lastName", "%" + searchCriteria.getLastName() + "%"));
            crit.add(Restrictions.like("lastName", searchCriteria.getLastName(), MatchMode.ANYWHERE).ignoreCase());
        }

        if (StringUtils.isNotBlank(searchCriteria.getUsername())) {
//            crit.add(Restrictions.like("username", "%" + searchCriteria.getUsername() + "%"));
            crit.add(Restrictions.like("username", searchCriteria.getUsername(), MatchMode.ANYWHERE).ignoreCase());
        }

        if (StringUtils.isNotBlank(searchCriteria.getDepartment())) {
            //crit.add(Restrictions.like("department", "%" + searchCriteria.getDepartment() + "%"));
            crit.add(Restrictions.like("department", searchCriteria.getDepartment(), MatchMode.ANYWHERE).ignoreCase());

        }

        if (StringUtils.isNotBlank(searchCriteria.getStatus())) {
            crit.add(Restrictions.eq("enabled",
                    searchCriteria.getStatus().equals("1") ? Boolean.TRUE : Boolean.FALSE));
        }

        return ((Number) crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();

    }
}
