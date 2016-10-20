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
package vn.vnpttech.ssdc.nms.mediation.stbacs;

import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface DSLStatsLocalHome extends EJBLocalHome {

    vn.vnpttech.ssdc.nms.mediation.stbacs.DSLStatsLocal findByPrimaryKey(vn.vnpttech.ssdc.nms.mediation.stbacs.DSLStatsPK key) throws FinderException;

    vn.vnpttech.ssdc.nms.mediation.stbacs.DSLStatsLocal create(Integer hostid, Timestamp time) throws CreateException;

    vn.vnpttech.ssdc.nms.mediation.stbacs.DSLStatsLocal create(Integer hostid, Timestamp time,
            Integer DownstreamAttenuation,
            Integer DownstreamCurrRate,
            Integer DownstreamMaxRate,
            Integer DownstreamNoiseMargin,
            Integer DownstreamPower,
            Integer UpstreamAttenuation,
            Integer UpstreamCurrRate,
            Integer UpstreamMaxRate,
            Integer UpstreamNoiseMargin,
            Integer UpstreamPower,
            String Status,
            String ModulationType) throws CreateException;

//    Collection findByCpeAndTime(Integer cpeid, Timestamp tmfrom, Timestamp tmto) throws FinderException;
    Collection<DSLStatsLocal> findByCpeAndTime2(Integer cpeid, Timestamp timeFrom, Timestamp timeTo) throws FinderException;

    DSLStatsLocal findByCpeAndTime(Integer cpeid, Timestamp time) throws FinderException;

    Collection<DSLStatsLocal> findByTimeBeforeAndHost(Integer hostid, Timestamp tm) throws FinderException;
}
