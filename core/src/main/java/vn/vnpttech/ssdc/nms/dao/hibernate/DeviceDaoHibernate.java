/*
 * Copyright 2015 Pivotal Software, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.vnpttech.ssdc.nms.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import vn.vnpttech.ssdc.nms.dao.DeviceDao;
import vn.vnpttech.ssdc.nms.model.Area;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.Policy;
import vn.vnpttech.ssdc.nms.util.Constant;

/**
 *
 * @author Dell
 */
public class DeviceDaoHibernate extends GenericDaoHibernate<Device, Long> implements DeviceDao {

    public DeviceDaoHibernate() {
        super(Device.class);
    }

    @Override
    public Map searchDevice(String deviceMAC, String deviceSerialNumber, String deviceStatus, String deviceFirmwareStatus,
            String deviceModel, String deviceFirmware, String deviceProvince, String deviceDistrict, List<Area> listArea, String ipAddress, String username, Long start, Long limit) {
        try {
            int total = 0;
            Map pagingMap = new HashMap();

            List<Device> listDevice = new ArrayList<Device>();
            Criteria criteria = getSession().createCriteria(Device.class);
            //MAC
            if (StringUtils.isNotBlank(deviceMAC)) {
                criteria.add(Restrictions.like("mac", deviceMAC.trim(), MatchMode.ANYWHERE).ignoreCase());
            }
            //Serial Number
            if (StringUtils.isNotBlank(deviceSerialNumber)) {
                criteria.add(Restrictions.like("serialNumber", "%" + deviceSerialNumber.trim() + "%").ignoreCase());
            }
            //Device status
            if (StringUtils.isNotBlank(deviceStatus)) {
                criteria.add(Restrictions.eq("cpeStatus", Integer.parseInt(deviceStatus.trim())));
            }
            //Firmware Status
            if (StringUtils.isNotBlank(deviceFirmwareStatus)) {
                criteria.add(Restrictions.eq("firmwareStatus", Integer.parseInt(deviceFirmwareStatus.trim())));
            }
            //Model
            if (StringUtils.isNotBlank(deviceModel)) {
                criteria.add(Restrictions.eq("deviceModel.id", Long.parseLong(deviceModel.trim())));
            }
            //Firmware
            if (StringUtils.isNotBlank(deviceFirmware)) {
                criteria.add(Restrictions.eq("firmwareVersion", deviceFirmware.trim()).ignoreCase());
            }

            //IP
            if (StringUtils.isNotBlank(ipAddress)) {
                criteria.add(Restrictions.like("ipAddress", ipAddress.trim(), MatchMode.ANYWHERE).ignoreCase());
            }
            // username 
            if (StringUtils.isNotBlank(username)) {
                criteria.add(Restrictions.like("stbUsername", username.trim(), MatchMode.ANYWHERE).ignoreCase());
            }

            //Province
            if (!listArea.isEmpty()) {
                criteria.add(Restrictions.in("area", listArea));
            }
//            if (StringUtils.isNotBlank(deviceProvince)) {
//                //District
//                if (StringUtils.isBlank(deviceDistrict)) { //province level
//                    criteria.createCriteria("area").add(Restrictions.eq("area.id", Long.parseLong(deviceProvince.trim())));
//                } else {//district level
//                    criteria.add(Restrictions.eq("area.id", Long.parseLong(deviceDistrict.trim())));
//                }
//            }
            criteria.addOrder(Order.desc("id"));
            //Paging
            if (limit != null && limit > 0) {
                // get the count
                ScrollableResults results = criteria.scroll();
                results.last();
                total = results.getRowNumber() + 1;
                results.close();
                //End get count
                criteria.setFirstResult(start.intValue());
                criteria.setMaxResults(limit.intValue());
            }

            listDevice = criteria.list();
            pagingMap.put("list", listDevice);
            pagingMap.put("totalCount", Long.parseLong(String.valueOf(total)));
            return pagingMap;
        } catch (Exception ex) {
            log.error("ERROR searchDevice: ", ex);
            return null;
        }
    }

    @Override
    public Device getDeviceBySerialNumber(String serialNumber) {
        try {
            Device result = null;
            Session ss = getSession();
            Criteria criteria = ss.createCriteria(Device.class);
            if (StringUtils.isNotBlank(serialNumber)) {
                criteria.add(Restrictions.eq("serialNumber", serialNumber.trim()).ignoreCase());
            }
            result = (Device) criteria.uniqueResult();

//            Query query = ss.createSQLQuery(
//                    "select * from device s where s.serial_number = :stockCode")
//                    .addEntity(Device.class)
//                    .setParameter("stockCode", serialNumber);
//            Query query = ss.createQuery("FROM device s where s.serial_number =: serial");
//            query.setParameter("serial", serialNumber);
//            query.
            return result;
        } catch (Exception ex) {
            log.error("ERROR getDeviceBySerialNumber: ", ex);
            return null;
        }
    }

    @Override
    public List<Device> getDeviceOfPolicy(Policy p) {
        Criteria c = getSession().createCriteria(Device.class);

        c.add(Restrictions.in("area", p.getAreas()));
        c.add(Restrictions.eq("deviceModel", p.getDeviceModel()));
        c.add(Restrictions.eq("cpeStatus", Constant.DEVICE_ON));//  get device ON.

        List<Device> ld = new ArrayList<Device>();
        //ld = q.list();
        ld = c.list();
        return ld;
    }

    @Override
    public List<Device> getDeviceFailedOfPolicy(Policy p) {
        List<Device> ld = null;
        Criteria c = getSession().createCriteria(Device.class)
                .createAlias("policiesHistory", "ph");
        c.add(Restrictions.eq("ph.policy", p));
        c.add(Restrictions.eq("ph.status", Constant.POLICY_HISTORY_FAIL));
        c.setProjection(Projections.distinct(Projections.property("ph.device")));

        ld = c.list();
        return ld;

    }

    @Override
    public List<Device> getDeviceNotUpdate(Policy p) {
        List<Device> ld = null;
        Criteria c = getSession().createCriteria(Device.class);
        c.add(Restrictions.eq("cpeStatus", Constant.DEVICE_ON));//  get device ON.
        Set<Area> allArea = p.getAreas();
        for (Area a : p.getAreas()) {
            Set<Area> as = a.getAreas();
            if (CollectionUtils.isNotEmpty(as)) {
                allArea.addAll(as);
            }
        }

        c.add(Restrictions.in("area", allArea));
        c.add(Restrictions.eq("deviceModel", p.getDeviceModel()));
        c.add(Restrictions.ne("firmwareVersion", p.getFirmware().getVersion()));

        ld = c.list();
        return ld;
    }

    @Override
    public List<Device> getDeviceByIdList(String id) {
        String hql = "from Device where id in ( " + id + " )";
        Query q = getSession().createQuery(hql);
        return q.list();
    }

    @Override
    public void insertDevice(Device device) {
        long startTime = System.currentTimeMillis();
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            log.info("time to get session (insert device): " + (System.currentTimeMillis() - startTime));
//            System.out.println("time to get session (insert device): " + (System.currentTimeMillis() - startTime));

            // update  status device
            Query query = session.createSQLQuery("INSERT into device ( mac, serial_number, model_id, area_id, "
                    + "cpu, ram, rom, cpe_username,connection_request, cpe_password, "
                    + "firmware_version, cpe_status, firmware_status, stb_username, "
                    + "stb_password, ip_address, homepage_url, upgrade_url ) "
                    + "VALUES "
                    + "(:mac, :serial_number, :model_id, :area_id, :cpu, :ram, :rom, :cpe_username,:connection_request, :cpe_password,"
                    + " :firmware_version , :cpe_status , :firmware_status, :stb_username,"
                    + " :stb_password , :ip_address, :homepage_url , :upgrade_url "
                    + " ) ");
            query.setParameter("mac", device.getMac());
            query.setParameter("serial_number", device.getSerialNumber());
            if (device.getDeviceModel() != null) {
                query.setParameter("model_id", device.getDeviceModel().getId());
            }

            if (device.getArea() != null) {
                query.setParameter("area_id", device.getArea().getId());
            } else {
                query.setParameter("area_id", null);
            }
            query.setParameter("cpu", device.getCpu());
            query.setParameter("ram", device.getRam());
            query.setParameter("rom", device.getRom());
            query.setParameter("cpe_username", device.getCpeUsername());
            query.setParameter("cpe_password", device.getCpePassword());
            query.setParameter("firmware_version", device.getFirmwareVersion());
            query.setParameter("cpe_status", device.getCpeStatus());
            query.setParameter("firmware_status", device.getFirmwareStatus());
            query.setParameter("stb_username", device.getStbUsername());
            query.setParameter("stb_password", device.getStbPassword());
            query.setParameter("ip_address", device.getIpAddress());
            query.setParameter("homepage_url", device.getHomepageUrl());
            query.setParameter("upgrade_url", device.getUpgradeUrl());
            query.setParameter("connection_request", device.getConnectionReq());
            query.executeUpdate();

        } catch (Exception ex) {
            log.error("ERROR insert device to db, serial: " + device.getSerialNumber(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
            log.info("time to insert device: " + (System.currentTimeMillis() - startTime));
//            System.out.println("time to insert device: " + (System.currentTimeMillis() - startTime));
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
