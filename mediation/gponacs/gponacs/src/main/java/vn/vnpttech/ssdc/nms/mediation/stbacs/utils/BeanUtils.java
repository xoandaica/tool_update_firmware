package vn.vnpttech.ssdc.nms.mediation.stbacs.utils;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/*
 * This document is confidential and copyright belongs to vnpt-technology
 * All rights reserved @ 2013
 */
/**
 *
 * @author vi
 * @date Oct 16, 2013
 */
public class BeanUtils {

    private ApplicationContext context;
    private static BeanUtils beanUtils;
    private final Logger logger = Logger.getLogger(BeanUtils.class);

    private BeanUtils() {
        try {
            String classPath = System.getProperty("stbacs.applicationContext", "classpath:applicationContext-stbacs.xml");
            context = new FileSystemXmlApplicationContext(classPath);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static BeanUtils getInstance() {
        if (beanUtils == null) {
            beanUtils = new BeanUtils();
        }
        return beanUtils;
    }

    public Object getBean(String name) {
        if (context != null) {
            return context.getBean(name);
        }
        return null;
    }

    public <T> T getBean(String name, Class<T> type) {
        if (context != null) {
            return context.getBean(name, type);
        }
        return null;
    }
}
