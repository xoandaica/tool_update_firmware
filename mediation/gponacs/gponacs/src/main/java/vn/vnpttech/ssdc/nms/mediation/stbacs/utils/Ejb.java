/*
 * 
 * Copyright 2007-2012 Audrius Valunas
 * 
 * This file is part of OpenACS.

 * OpenACS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenACS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OpenACS.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.utils;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import vn.vnpttech.ssdc.nms.mediation.stbacs.ATMErrorsStatsLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.BackupLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.CPELocal;
import vn.vnpttech.ssdc.nms.mediation.stbacs.CPELocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ConfigurationLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.DSLStatsLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.DataModelLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.DeviceProfile2SoftwareLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.DeviceProfileLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.HardwareModelLocal;
import vn.vnpttech.ssdc.nms.mediation.stbacs.HardwareModelLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.Host2ServiceLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.HostPropertyLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.HostsLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.OuiMapLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ProfilePropertyLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.PropertyLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ScriptLocal;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ScriptLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ServiceLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ServicePropertyLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.SoftwareDetailLocalHome;
import vn.vnpttech.ssdc.nms.mediation.stbacs.SoftwareLocalHome;

public class Ejb {

    /**
     * Creates a new instance of Ejb
     */
    public Ejb() {
    }

    static public ConfigurationLocalHome lookupConfigurationBean() {
        try {
            Context c = new InitialContext();
            return (ConfigurationLocalHome) c.lookup("java:comp/env/ejb/ConfigurationBean");
        } catch (NamingException ne) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }

    static public SoftwareLocalHome lookupSoftwareBean() {
        try {
            Context c = new InitialContext();
            SoftwareLocalHome rv = (SoftwareLocalHome) c.lookup("java:comp/env/ejb/SoftwareBean");
            return rv;
        } catch (NamingException ne) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }

    static public SoftwareDetailLocalHome lookupSoftwareDetailBean() {
        try {
            Context c = new InitialContext();
            SoftwareDetailLocalHome rv = (SoftwareDetailLocalHome) c.lookup("java:comp/env/ejb/SoftwareDetailBean");
            return rv;
        } catch (NamingException ne) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }

    static public HostsLocalHome lookupHostsBean() {
        try {
            Context c = new InitialContext();
            HostsLocalHome rv = (HostsLocalHome) c.lookup("java:comp/env/ejb/HostsBean");
            return rv;
        } catch (NamingException ne) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }

    static public CPELocal lookupCPEBean() {
        try {
            Context c = new InitialContext();
            CPELocalHome rv = (CPELocalHome) c.lookup("java:comp/env/ejb/CPEBean");
            return rv.create();
        } catch (Exception ne) {
            //Logger.getLogger(getClass().getName()).log(Level.SEVERE,"exception caught" ,ne);
            throw new RuntimeException(ne);
        }
    }

    static public ScriptLocalHome lookupScriptBean() {
        try {
            Context c = new InitialContext();
            ScriptLocalHome rv = (ScriptLocalHome) c.lookup("java:comp/env/ejb/ScriptBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public HardwareModelLocalHome lookupHardwareModelBean() {
        try {
            Context c = new InitialContext();
            HardwareModelLocalHome rv = (HardwareModelLocalHome) c.lookup("java:comp/env/ejb/HardwareModelBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public DSLStatsLocalHome lookupDSLStatsBean() {
        try {
            Context c = new InitialContext();
            DSLStatsLocalHome rv = (DSLStatsLocalHome) c.lookup("java:comp/env/ejb/DSLStatsBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public ATMErrorsStatsLocalHome lookupATMErrorsStatsBean() {
        try {
            Context c = new InitialContext();
            ATMErrorsStatsLocalHome rv = (ATMErrorsStatsLocalHome) c.lookup("java:comp/env/ejb/ATMErrorsStatsBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public DeviceProfileLocalHome lookupDeviceProfileBean() {
        try {
            Context c = new InitialContext();
            DeviceProfileLocalHome rv = (DeviceProfileLocalHome) c.lookup("java:comp/env/ejb/DeviceProfileBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public PropertyLocalHome lookupPropertyBean() {
        try {
            Context c = new InitialContext();
            PropertyLocalHome rv = (PropertyLocalHome) c.lookup("java:comp/env/ejb/PropertyBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public DataModelLocalHome lookupDataModelBean() {
        try {
            Context c = new InitialContext();
            DataModelLocalHome rv = (DataModelLocalHome) c.lookup("java:comp/env/ejb/DataModelBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public BackupLocalHome lookupBackupBean() {
        try {
            Context c = new InitialContext();
            BackupLocalHome rv = (BackupLocalHome) c.lookup("java:comp/env/ejb/BackupBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public DeviceProfile2SoftwareLocalHome lookupDeviceProfile2SoftwareBean() {
        try {
            Context c = new InitialContext();
            DeviceProfile2SoftwareLocalHome rv = (DeviceProfile2SoftwareLocalHome) c.lookup("java:comp/env/ejb/DeviceProfile2SoftwareBean");
            return rv;
        } catch (NamingException ne) {
            //java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    static public ProfilePropertyLocalHome lookupProfilePropertyBean() {
        try {
            Context c = new InitialContext();
            ProfilePropertyLocalHome rv = (ProfilePropertyLocalHome) c.lookup("java:comp/env/ejb/ProfilePropertyBean");
            return rv;
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    static public ServicePropertyLocalHome lookupServicePropertyBean() {
        try {
            Context c = new InitialContext();
            ServicePropertyLocalHome rv = (ServicePropertyLocalHome) c.lookup("java:comp/env/ejb/ServicePropertyBean");
            return rv;
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    static public HostPropertyLocalHome lookupHostPropertyBean() {
        try {
            Context c = new InitialContext();
            HostPropertyLocalHome rv = (HostPropertyLocalHome) c.lookup("java:comp/env/ejb/HostPropertyBean");
            return rv;
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    static public Host2ServiceLocalHome lookupHost2ServiceBean() {
        try {
            Context c = new InitialContext();
            Host2ServiceLocalHome rv = (Host2ServiceLocalHome) c.lookup("java:comp/env/ejb/Host2ServiceBean");
            return rv;
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    static public ServiceLocalHome lookupServiceBean() {
        try {
            Context c = new InitialContext();
            ServiceLocalHome rv = (ServiceLocalHome) c.lookup("java:comp/env/ejb/ServiceBean");
            return rv;
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    static public ScriptLocal lookupScriptBean(String name) throws FinderException {
        return lookupScriptBean().findByPrimaryKey(name);
    }

    static public Map<Integer, HardwareModelLocal> getHardwareModelMap() {
        Hashtable<Integer, HardwareModelLocal> m = new Hashtable<Integer, HardwareModelLocal>();
        HardwareModelLocalHome hh = lookupHardwareModelBean();
        try {
            Collection<HardwareModelLocal> i = hh.findAll();
            for (HardwareModelLocal h : i) {
                m.put((Integer) h.getId(), h);
            }
        } catch (FinderException ex) {
        }

        return m;
    }

    static public OuiMapLocalHome lookupOuiMapBean() {
        try {
            Context c = new InitialContext();
            OuiMapLocalHome rv = (OuiMapLocalHome) c.lookup("java:comp/env/ejb/OuiMapBean");
            return rv;
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
