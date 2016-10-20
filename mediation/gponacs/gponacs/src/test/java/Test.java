
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.PropertyConfigurator;
import vn.vnpttech.ssdc.nms.dao.hibernate.DeviceDaoHibernate;
import static vn.vnpttech.ssdc.nms.mediation.stbacs.ACSServlet.deviceModelManager;
import vn.vnpttech.ssdc.nms.mediation.stbacs.main.ACSServletPublisher;
import vn.vnpttech.ssdc.nms.mediation.stbacs.main.ACSWebServicePublisher;
import vn.vnpttech.ssdc.nms.mediation.stbacs.utils.BeanUtils;
import vn.vnpttech.ssdc.nms.mediation.stbacs.utils.ConfigUtils;
import vn.vnpttech.ssdc.nms.model.Device;
import vn.vnpttech.ssdc.nms.model.DeviceModel;
import vn.vnpttech.ssdc.nms.model.ServiceLog;
import vn.vnpttech.ssdc.nms.service.DeviceManager;
import vn.vnpttech.ssdc.nms.service.ServiceLogManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SSDC
 */
public class Test {

    static ThreadInsert[] thread = new ThreadInsert[10];
    public static final ServiceLogManager serviceLogManager = BeanUtils.getInstance().getBean("serviceLogManager", ServiceLogManager.class);

    public static void main(String[] args) throws Exception {
        try {
            PropertyConfigurator.configure("../etc/log4j.conf");
            Thread.currentThread().setName("GPON ACS Wrapper");
        } catch (Exception ex) {
        }

        ACSServletPublisher.publishACS();
        ACSWebServicePublisher.publishWebservice();
        
        
        ServiceLog log = new ServiceLog();
        log.setActionEndTime(new Date());
        log.setActionName("TEST");
        log.setSerialNumber("AAAA");
        log.setActionStartTime(new Date());
        serviceLogManager.save(log);
        
        
    }

    static class ThreadInsert extends Thread {

        DeviceManager deviceManager = BeanUtils.getInstance().getBean("deviceManager", DeviceManager.class);

        public ThreadInsert() {
        }

        @Override
        public void run() {
            String VNPTname = null;
            try {
                VNPTname = ConfigUtils.getInstance().getModelName("968380GERG");
            } catch (IOException ex) {
                ex.printStackTrace();

            }
            DeviceModel model = deviceModelManager.getModelByName(VNPTname);
            long s1 = System.currentTimeMillis();
            Device device = new Device();

            device.setMac("AAAAAAAA");
            device.setDeviceModel(model);
            device.setFirmwareVersion("SET");
            device.setConnectionReq("123455");
            int i = new Random().nextInt(100000);
            while (true) {
                i++;
                device.setSerialNumber("TEST" + i);
                long startTime = System.currentTimeMillis();
                deviceManager.save(device);
                long endTime = System.currentTimeMillis();
                System.out.println("THREADF" + this.getName() + ", TIME = " + (endTime - startTime));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
