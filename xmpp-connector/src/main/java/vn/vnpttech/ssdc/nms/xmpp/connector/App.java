package vn.vnpttech.ssdc.nms.xmpp.connector;

import vn.vnpttech.ssdc.nms.xmpp.connector.dao.RosterUsersDao;
import vn.vnpttech.ssdc.nms.xmpp.connector.dao.RosterUsersDaoImpl;
import vn.vnpttech.ssdc.nms.xmpp.connector.dao.UsersDao;
import vn.vnpttech.ssdc.nms.xmpp.connector.dao.UsersDaoImpl;

import vn.vnpttech.ssdc.nms.xmpp.model.Rosterusers;
import vn.vnpttech.ssdc.nms.xmpp.model.Users;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {

        System.out.println("Starting....");
        RosterUsersDao rd = new RosterUsersDaoImpl(Rosterusers.class);
        UsersDao ud = new UsersDaoImpl(Users.class);

//        Rosterusers r = new Rosterusers("abc13579", "xyz2460@xyz.con.vn", null);
//        rd.save(r);
//        //rd.remove(7L);
//        //System.out.println(r.getId().toString());
//        List<Rosterusers> rs = new ArrayList<Rosterusers>();
//        rs = rd.getAll();
//        System.out.println(rs.size() + "Rosteruser======");
//
//        Users u = new Users("longdq2019", "pass", new Date());
//
//        UsersDao ud = new UsersDaoImpl(Users.class);
//        ud.save(u);
//        List<Users> us = new ArrayList<Users>();
//        us = ud.getAll();
//        System.out.println(us.size());
//        Rosterusers ru = new Rosterusers();
//        ru = rd.get(10L);
//        if (ru != null) {
//            System.out.println(ru.getUsername());
//        } else {
//            System.out.println("Opp. Can not find model.");
//        }
//        Users u = new Users();
//        u = ud.get(5L);
//
//        Users u2 = new Users();
//        u2 = ud.getUserByUserName("longdq");
//        if(u2 != null)
//            System.out.println(u2.getPassword()+ "++++++++++++++++");
//        else 
//            System.out.println("Cannot find user !!!");
        Rosterusers r = rd.getRosterUser("acsmaster2", "vunb@10.84.8.33");
        if (r != null) {
            System.out.println(r.getUsername() + "...........");
        } else {
            System.out.println("Null.................");
        }
        System.out.println("Bye bye");

    }
}
