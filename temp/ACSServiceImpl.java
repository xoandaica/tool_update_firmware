package net.vnpttech.collection.openacs.services;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.jws.WebParam;

import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import net.vnpttech.collection.openacs.database.DatabaseManager;

import net.vnpttech.collection.openacs.mycommand.TR069StaticParameter;
import net.vnpttech.collection.openacs.myobject.Layer2InterfaceObject;
import net.vnpttech.collection.openacs.myobject.SimpleObject;
import net.vnpttech.collection.openacs.myobject.WanServiceObject;
import net.vnpttech.collection.openacs.exception.AlreadyEnqueuedException;
import net.vnpttech.collection.openacs.exception.DeviceNotFoundException;
import net.vnpttech.collection.openacs.exception.ProductNotFoundException;
import net.vnpttech.collection.openacs.mycommand.AddObjectCommand;
import net.vnpttech.collection.openacs.mycommand.BackupCommand;
import net.vnpttech.collection.openacs.mycommand.FactoryResetCommand;
import net.vnpttech.collection.openacs.mycommand.FilePath;
import net.vnpttech.collection.openacs.mycommand.GetValueCommand;
import net.vnpttech.collection.openacs.mycommand.PingDiagnosticsCommand;
import net.vnpttech.collection.openacs.mycommand.RebootCommand;
import net.vnpttech.collection.openacs.mycommand.SetDownloadCommand;
import net.vnpttech.collection.openacs.mycommand.SetRestoreCommand;
import net.vnpttech.collection.openacs.mycommand.SetUploadCommand;
import net.vnpttech.collection.openacs.mycommand.SetValueCommand;
import net.vnpttech.collection.openacs.mycommand.TracertDiagnosticsCommand;
import net.vnpttech.collection.openacs.mycommand.UpdateFirmwareBatchCommand;
import net.vnpttech.collection.openacs.mycommand.UpdateFirmwareCommand;
import net.vnpttech.collection.openacs.myobject.AAAConfigurationObject;
import net.vnpttech.collection.openacs.myobject.DataFileModel;
import net.vnpttech.collection.openacs.myobject.DslDeviceObject;
import net.vnpttech.collection.openacs.myobject.StaticDNSObject;
import net.vnpttech.collection.openacs.myobject.StaticDynamicDNSObject;
import net.vnpttech.collection.openacs.myobject.StaticRouteObject;
import net.vnpttech.collection.openacs.myobject.UserAndPassObject;
import net.vnpttech.collection.openacs.myobject.WANGUIObjectEthernet;
import net.vnpttech.collection.openacs.myobject.WirelessObject_2;
import net.vnpttech.collection.openacs.myobject.WirelessObject_Advance;
import net.vnpttech.collection.openacs.poller.TR069ADSLPoller;
import net.vnpttech.collection.openacs.poller.TR069AONPoller;
import net.vnpttech.collection.openacs.poller.TR069ONTPoller;
import net.vnpttech.collection.openacs.poller.TR069Poller;
import net.vnpttech.collection.openacs.poller.Tr069PollerFactory;
import net.vnpttech.collection.openacs.tree.DataFileUtils;
import net.vnpttech.collection.openacs.ws.model.*;
import net.vnpttech.collection.openacs.ws.response.*;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.appfuse.model.AdslDevice;
import org.appfuse.model.Product;

@WebService(endpointInterface = "net.vnpttech.collection.openacs.services.ACSService")
public class ACSServiceImpl implements ACSService, Serializable {

    public static final Logger logger = Logger.getLogger("webservice");
    private final StringBuilder sbLog = new StringBuilder();

    @Override
    public Layer2InterfaceObjectResponse getAtmInterface(String serialNumber, String urlRequestCpe) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getAtmInterface. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());
        Layer2InterfaceObjectResponse result = new Layer2InterfaceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                String rootTree = "InternetGatewayDevice.WANDevice.1.WANConnectionDevice.";
                result.setRetValue(poller.getATMInterface(rootTree));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        
        return result;
    }

    /**
     * lay toan bo thong tin cau hinh cua
     * InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @return
     */
    @Override
    public LanSetupObjectResponse getLanSetup(String serialNumber, String urlRequestCpe, String bridgeIndex
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getLanSetup. [Input] Serial = ").append(serialNumber).append(", urlRequest = ").append(urlRequestCpe).append(", bridgeIndex = ").append(bridgeIndex);
        logger.info(sbLog.toString());
        LanSetupObjectResponse result = new LanSetupObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.getLanSetup(bridgeIndex));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public WirelessObjectResponse getWireLessInfor(String serialNumber, String urlRequestCpe
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getWireLessInfor. [Input] Serial = ").append(serialNumber).append(", urlRequest = ").append(urlRequestCpe);
        logger.info(sbLog.toString());
        WirelessObjectResponse result = new WirelessObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.setRetValue(poller.getWireLessInfor());
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public Layer2InterfaceObjectResponse addLayer2ATMObj(String serialNumber, String urlRequestCpe, int vpi, int vci, int dslLatency
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addLayer2ATMObj. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", vpi = ")
                .append(vpi)
                .append(", vci = ")
                .append(vci)
                .append(", dslLatency = ")
                .append(dslLatency);
        logger.info(sbLog.toString());

        Layer2InterfaceObjectResponse result = new Layer2InterfaceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.addLayer2ATMObj(vpi, vci, dslLatency));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    /**
     * ham xoa mot dich vu wan
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @param path
     * @return
     */
    @Override
    public BasicResponse deleteObject(String serialNumber, String urlRequestCpe, String path
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] deleteObject. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", path = ")
                .append(path);
        logger.info(sbLog.toString());
        BasicResponse result = new BasicResponse();
        try {

            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.deleteObject(path));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse addLANStaticIPLease(String serialNumber, String urlRequestCpe, String macAddress, String ipAddress
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addLANStaticIPLease. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", macAddress = ")
                .append(macAddress)
                .append(", ipAddress = ")
                .append(ipAddress);
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.addLANStaticIPLease(macAddress, ipAddress));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public WANGUIObjectResponse getWANGUIObject(String serialNumber, String urlRequestCpe
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getWANGUIObject. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());

        WANGUIObjectResponse result = new WANGUIObjectResponse();
        try {

            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.getWanGuiObject());
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }

        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    /**
     * TODO co the xoa khong
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @return
     */
    @Override
    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet(
            String serialNumber, String urlRequestCpe
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getWANGUIObject_ethernet. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());
        WANGUIObjectEthernetResponse result = new WANGUIObjectEthernetResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.getWANGUIObject_ethernet());
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse setValue(String serialNumber, String urlRequestCpe, ArrayList<SimpleObject> data
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setValue. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", ListObject = ");
        for (SimpleObject tmp : data) {
            sbLog.append("[").append(tmp.toString()).append("]");
        }
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null ) {
                result.addRetValue(poller.setValueObjects(data));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addPPPoEWanService(String serialNumber, String urlRequestCpe,
            Layer2InterfaceObject layer2, String description, String username,
            String password, String servicename
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addPPPoEWanService. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", Layer2InterfaceObject = ")
                .append(layer2.toString())
                .append(", description = ")
                .append(description)
                .append(", username = ")
                .append(username)
                .append(", password = ")
                .append(password)
                .append(", servicename = ")
                .append(servicename);
        logger.info(sbLog.toString());
        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.addPPPoEWanService(layer2, description, username, password, servicename));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public Layer3GUIObjectResponse getLayer3GUIObject(String serialNumber, String urlRequestCpe
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getLayer3GUIObject. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());

        Layer3GUIObjectResponse result = new Layer3GUIObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.getLayer3GUIObject());
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse addStaticRoute(String serialNumber, String urlRequestCpe, String destIP,
            String subnetMask, String type, String ifname, String gateway
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addStaticRoute. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", destIP = ")
                .append(destIP)
                .append(", subnetMask = ")
                .append(subnetMask)
                .append(", type = ")
                .append(type)
                .append(", ifname = ")
                .append(ifname)
                .append(", gateway = ")
                .append(gateway);
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.addStaticRoute(destIP, subnetMask, type, ifname, gateway));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;

    }

    @Override
    public WanServiceObjectResponse addIpoEWanService(String serialNumber, String urlRequestCpe,
            Layer2InterfaceObject layer2, String type, String description,
            String ipaddress, String subnetmask, String gateway, String dns1,
            String dns2, boolean nat, boolean firewall
    ) {
        // TODO Auto-generated method stub
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanService. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", Layer2InterfaceObject = ")
                .append(layer2.toString())
                .append(", type = ")
                .append(type)
                .append(", description = ")
                .append(description)
                .append(", ipaddress = ")
                .append(ipaddress)
                .append(", subnetmask = ")
                .append(subnetmask)
                .append(", gateway = ")
                .append(gateway)
                .append(", dns1 = ")
                .append(dns1)
                .append(", dns2 = ")
                .append(dns2)
                .append(", nat = ")
                .append(nat)
                .append(", firewall = ")
                .append(firewall);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                WanServiceObject wanObj;
                if (type.equals(TR069StaticParameter.AddressingType_DHCP)) {
                    wanObj = poller.addIPoEWanServiceDHCP(layer2, description, nat, firewall);
                } else if (type.equals(TR069StaticParameter.AddressingType_Static)) {
                    wanObj = poller.addIPoEWanServiceStatic(layer2, description, ipaddress, subnetmask, gateway, dns1, dns2, nat, firewall);
                } else {
                    result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                    result.setMessage("Type " + type + " is unknow");
                    return result;
                }

                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }

        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet(
            String serialNumber,
            String urlRequestCpe,
            String serviceName,
            int vlanMux8021Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceDHCP_ethernet. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", serviceName = ")
                .append(serviceName)
                .append(", vlanMux8021Priority = ")
                .append(vlanMux8021Priority)
                .append(", vlanMuxID = ")
                .append(vlanMuxID)
                .append(", ipaddress = ")
                .append(ipV4Enabled)
                .append(", ipV4Enabled = ")
                .append(ipV6Enabled)
                .append(", ipV6Enabled = ")
                .append(natEnabled)
                .append(", natEnabled = ")
                .append(firewallEnabled)
                .append(", firewallEnabled = ");
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {

            IPoEWanServiceDHCP ipoeWanDHCP = new IPoEWanServiceDHCP(serviceName, vlanMux8021Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled);
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.addIPoEWanServiceDHCP_ethernet(ipoeWanDHCP));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet(
            String serialNumber,
            String urlRequestCpe,
            String serviceName,
            int vlanMux8021Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String externalIPAdress,
            String subnetMask,
            String defaultGateway,
            String dnsIfName,
            String dnsServers
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceStatic_ethernet. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", serviceName = ")
                .append(serviceName)
                .append(", vlanMux8021Priority = ")
                .append(vlanMux8021Priority)
                .append(", vlanMuxID = ")
                .append(vlanMuxID)
                .append(", ipV4Enabled = ")
                .append(ipV4Enabled)
                .append(", ipV6Enabled = ")
                .append(ipV6Enabled)
                .append(", natEnabled = ")
                .append(natEnabled)
                .append(", firewallEnabled = ")
                .append(firewallEnabled)
                .append(", externalIPAdress = ")
                .append(externalIPAdress)
                .append(", subnetMask = ")
                .append(subnetMask)
                .append(", defaultGateway = ")
                .append(defaultGateway)
                .append(", dnsIfName = ")
                .append(dnsIfName)
                .append(", dnsServers = ")
                .append(dnsServers);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {

            IPoEWanServiceStatic ipoeWanStatic = new IPoEWanServiceStatic(
                    serviceName,
                    vlanMux8021Priority,
                    vlanMuxID,
                    ipV4Enabled,
                    ipV6Enabled,
                    natEnabled,
                    firewallEnabled,
                    externalIPAdress,
                    subnetMask,
                    defaultGateway,
                    dnsIfName,
                    dnsServers
            );
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.addIPoEWanServiceStatic_ethernet(ipoeWanStatic));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public BasicResponse getUptime(String serialNumber, String urlRequestCpe
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getUptime. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());
        BasicResponse result = new BasicResponse();
        try {
            sbLog.setLength(0);
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.getUptime());
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
            sbLog.append("ProductType : ").append(productType).append("TR069Poller").append(poller);
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse upgradeFirmware(String serialNumber, String urlRequestCpe,
            String url, String username, String password
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] upgradeFirmware. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", url = ")
                .append(url)
                .append(", username = ")
                .append(username)
                .append(", password = ")
                .append(password);
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {

            UpdateFirmwareCommand cmand = new UpdateFirmwareCommand(serialNumber, urlRequestCpe, url, username, password);
            boolean hasError = cmand.executeCommand();

            if (hasError) {

                result.addRetValue(cmand.getErrorString());
                result.setErrorCode(WebServiceConfig.ErrorCode.SYSTEM_FAIL);
                result.setMessage(cmand.getErrorString());
            } else {
                result.addRetValue(hasError);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
            return result;
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse rebootDevice(String serialNumber, String urlRequestCpe
    ) {

        sbLog.setLength(0);
        sbLog.append("[Calling] rebootDevice. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);

        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {

            RebootCommand cmand = new RebootCommand("System", serialNumber, urlRequestCpe);
            boolean hasError = cmand.executeCommand();
            if (hasError) {
                result.addRetValue(cmand.getErrorString());
                result.setErrorCode(WebServiceConfig.ErrorCode.SYSTEM_FAIL);
                result.setMessage(cmand.getErrorString());
            } else {
                result.addRetValue(hasError);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
            return result;
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse factoryReset(String serialNumber, String urlRequestCpe
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] factoryReset. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());

        BasicResponse response = new BasicResponse();
        try {
            FactoryResetCommand cmand = new FactoryResetCommand("System", serialNumber, urlRequestCpe);
            boolean result = cmand.executeCommand();

            if (!result) {
                response.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                response.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                response.setMessage(cmand.getErrorString());
            }
            return response;

        } catch (AlreadyEnqueuedException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return response;
    }

    @Override
    public WanServiceObjectResponse addPPPoEWanService_ethernet(
            String serialNumber,
            String urlRequestCpe,
            String serviceName,
            String username,
            String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addPPPoEWanService_ethernet. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", serviceName = ")
                .append(serviceName)
                .append(", username = ")
                .append(username)
                .append(", password = ")
                .append(password)
                .append(", vlanMux802_1Priority = ")
                .append(vlanMux802_1Priority)
                .append(", vlanMuxID = ")
                .append(vlanMuxID)
                .append(", ipV4Enabled = ")
                .append(ipV4Enabled)
                .append(", ipV6Enabled = ")
                .append(ipV6Enabled)
                .append(", natEnabled = ")
                .append(natEnabled)
                .append(", firewallEnabled = ")
                .append(firewallEnabled);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            throw new Exception("Deprecated! Please use addPPPoEWanService_ethernet_V2");
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addBridgeWanService(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "dslLatency")
            @XmlElement(required = true) int dslLatency,
            @WebParam(name = "vpi")
            @XmlElement(required = true) int vpi,
            @WebParam(name = "vci")
            @XmlElement(required = true) int vci
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addBridgeWanService. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", dslLatency = ")
                .append(dslLatency)
                .append(", vpi = ")
                .append(vpi)
                .append(", vci = ")
                .append(vci);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.addRetValue(poller.addBridgeWanService(dslLatency, vpi, vci));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addBridgeWanService_ethernet(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe
    ) {
        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            throw new Exception("Deprecated! Please use addBridgeWanService_ethernet_V2");
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addBridgeWanServiceV2_ethernet(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "serviceDescription")
            @XmlElement(required = true) String serviceDescription,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID
    ) {
        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            throw new Exception("Deprecated! Please use addBridgeWanService_ethernet_V2");
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse getDataTree(@WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "dataTree")
            @XmlElement(required = true) String dataTree
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getDataTree. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", dataTree = ")
                .append(dataTree);
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                result.setRetValue(poller.getDataTree(dataTree));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
        return result;
    }

    @Override
    public ModemInterfaceGroupResponse getDeviceInterfaceGroups(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getDeviceInterfaceGroups. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());

        ModemInterfaceGroupResponse result = new ModemInterfaceGroupResponse();
        try {

            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                ModemInterfaceGroups ig = poller.getDeviceInterfaceGroups();
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
                result.setBrigdingGroup(ig.getBrigdingGroup());
                result.setDeviceInterfaces(ig.getDeviceInterfaces());
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.SYSTEM_FAIL);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    /**
     * api tao mapping giua lan/wlan voi wan interface
     *
     * @param sn
     * @param requestUrl
     * @param groupName
     * @param wanInterface
     * @param lanInterface
     * @return
     */
    @Override
    public ModemInterfaceGroupResponse addInterfaceGroup(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "groupName")
            @XmlElement(required = true) String groupName,
            @WebParam(name = "wanInterface")
            @XmlElement(required = true) String wanInterface,
            @WebParam(name = "lanInterface")
            @XmlElement(required = true) String lanInterface
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addInterfaceGroup. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", groupName = ")
                .append(groupName)
                .append(", wanInterface = ")
                .append(wanInterface)
                .append(", lanInterface = ")
                .append(lanInterface);
        logger.info(sbLog.toString());

        ModemInterfaceGroupResponse result = new ModemInterfaceGroupResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                String bridgeEnable = "1";
                String[] lanInterfaces = StringUtils.split(lanInterface, ";,-");
                BrigdingGroup newGroup = poller.addInterfaceGroup(bridgeEnable, groupName, wanInterface, lanInterfaces);
                result.setErrorCode(newGroup != null ? WebServiceConfig.ErrorCode.SUCCESS : WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                result.setMessage(newGroup != null ? WebServiceConfig.Message.SUCCESS : "null");
                result.addBrigdingGroup(newGroup);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse removeInterfaceGroup(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "groupIndex")
            @XmlElement(required = true) String groupIndex,
            @WebParam(name = "wanInterfaceId")
            @XmlElement(required = true) String wanInterfaceId,
            @WebParam(name = "lanInterfaceIds")
            @XmlElement(required = true) String lanInterfaceIds
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] removeInterfaceGroup. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", groupIndex = ")
                .append(groupIndex)
                .append(", wanInterfaceId = ")
                .append(wanInterfaceId)
                .append(", lanInterfaceIds = ")
                .append(lanInterfaceIds);
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                String[] lanInterfaces = StringUtils.split(lanInterfaceIds, ";,-");
                boolean ret = poller.removeInterfaceGroup(groupIndex, wanInterfaceId, lanInterfaces);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
                result.addRetValue(ret);
            }
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="2015-03-05 add feature: Wifi Offload">
    @Override
    public WirelessSecurityResponse getAllWirelessSecurity(String serialNumber, String urlRequestCpe
    ) {
        WirelessSecurityResponse result = new WirelessSecurityResponse();
        try {
            TR069ONTPoller poller = new TR069ONTPoller("ACS", serialNumber, urlRequestCpe);
            Map<String, WirelessSecurity> ret = poller.getAllWirelessSecurity();
            result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
            result.setMessage(WebServiceConfig.Message.SUCCESS);
            result.setRetValue(new ArrayList<WirelessSecurity>(ret.values()));
            return result;
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }

    @Override
    public BasicResponse setWirelessSecurity(String serialNumber, String urlRequestCpe, WirelessSecurity wlSecurity
    ) {
        BasicResponse result = new BasicResponse();
        try {
            TR069ONTPoller poller = new TR069ONTPoller("ACS", serialNumber, urlRequestCpe);
            boolean ret = poller.configWirelessSecurity(wlSecurity);
            result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
            result.setMessage(WebServiceConfig.Message.SUCCESS);
            result.addRetValue(ret);
            return result;
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }
//</editor-fold>

    /**
     * ham nay goi khi nguoi dung double click tab wan setup
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @param modelName
     * @return
     */
    @Override
    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet_V2(
            String serialNumber,
            String urlRequestCpe,
            String modelName
    ) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getWANGUIObject_ethernet_V2. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", modelName = ")
                .append(modelName);
        logger.info(sbLog.toString());

        WANGUIObjectEthernetResponse result = new WANGUIObjectEthernetResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                WANGUIObjectEthernet listWanADSLMode = newPoller.getWANGUIObject_ethernet_ADSL_ADSLMode(modelName);
                WANGUIObjectEthernet listWanAccessMode = newPoller.getWANGUIObject_ethernet_ADSL_AccessMode(modelName);
                WANGUIObjectEthernet finalList = newPoller.consultList(listWanADSLMode, listWanAccessMode);
                result.addRetValue(finalList);
//                result.addRetValue(newPoller.getWANGUIObject_ethernet_ADSL_AccessMode(modelName));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.addRetValue(poller.getWANGUIObject_ethernet(modelName));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            result.setErrorCode(getErrorCode(ex));
        }
        return result;
    }

    private int getErrorCode(Exception ex) {
        logger.error(ex.getMessage(), ex);
        if (ex instanceof AlreadyEnqueuedException) {
            return (WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
        } else if (ex instanceof ConnectTimeoutException) {
            return (WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
        } else {
            return (WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
        }
    }

    /**
     * add bridge wan service
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @param modelName
     * @param serviceDescription
     * @param vlanMux802_1Priority
     * @param vlanMuxID
     * @param connectionId
     * @return
     */
    @Override
    public WanServiceObjectResponse addBridgeWanService_ethernet_V2(
            String serialNumber, String urlRequestCpe,
            String modelName, String serviceDescription,
            int vlanMux802_1Priority, int vlanMuxID, String connectionId, int wanIndex) {

        sbLog.setLength(0);
        sbLog.append("[Calling] addBridgeWanService_ethernet_V2. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", modelName = ")
                .append(modelName)
                .append(", serviceDescription = ")
                .append(serviceDescription)
                .append(", vlanMux802_1Priority = ")
                .append(vlanMux802_1Priority)
                .append(", vlanMuxID = ")
                .append(vlanMuxID)
                .append(", connectionId = ")
                .append(connectionId)
                .append(", wanIndex = ")
                .append(wanIndex);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                WanServiceObject wanObj = poller.addBridgeWanService(serviceDescription, vlanMux802_1Priority, vlanMuxID, modelName, connectionId, wanIndex);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    /**
     * add pppoe wan
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @param modelName
     * @param serviceName
     * @param username
     * @param password
     * @param vlanMux802_1Priority
     * @param vlanMuxID
     * @param ipV4Enabled
     * @param ipV6Enabled
     * @param natEnabled
     * @param firewallEnabled
     * @param connectionId
     * @return
     */
    @Override
    public WanServiceObjectResponse addPPPoEWanService_ethernet_V2(
            String serialNumber, String urlRequestCpe,
            String modelName, String serviceName,
            String username, String password,
            int vlanMux802_1Priority, int vlanMuxID,
            boolean ipV4Enabled, boolean ipV6Enabled,
            boolean natEnabled, boolean firewallEnabled,
            String connectionId, int wanIndex
    ) {

        sbLog.setLength(0);
        sbLog.append("[Calling] addPPPoEWanService_ethernet_V2. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", modelName = ")
                .append(modelName)
                .append(", serviceName = ")
                .append(serviceName)
                .append(", username = ")
                .append(username)
                .append(", password = ")
                .append(password)
                .append(", vlanMux802_1Priority = ")
                .append(vlanMux802_1Priority)
                .append(", vlanMuxID = ")
                .append(vlanMuxID)
                .append(", ipV4Enabled = ")
                .append(ipV4Enabled)
                .append(", ipV6Enabled = ")
                .append(ipV6Enabled)
                .append(", natEnabled = ")
                .append(natEnabled)
                .append(", firewallEnabled = ")
                .append(firewallEnabled)
                .append(", connectionId = ")
                .append(connectionId)
                .append(", wanIndex = ")
                .append(wanIndex);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                WanServiceObject wanObj = poller.addPPPoEWanService_ethernet(
                        serviceName,
                        username, password,
                        vlanMux802_1Priority, vlanMuxID,
                        ipV4Enabled, ipV6Enabled,
                        natEnabled, firewallEnabled,
                        modelName,
                        connectionId, wanIndex
                );

                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    /**
     * add pppoe dhcp
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @param modelName
     * @param serviceName
     * @param vlanMux802_1Priority
     * @param vlanMuxID
     * @param ipV4Enabled
     * @param ipV6Enabled
     * @param natEnabled
     * @param firewallEnabled
     * @param wanIndex
     * @param connectionId
     * @return
     */
    @Override
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet_V2(String serialNumber,
            String urlRequestCpe, String modelName, String serviceName, int vlanMux802_1Priority,
            int vlanMuxID, boolean ipV4Enabled, boolean ipV6Enabled, boolean natEnabled,
            boolean firewallEnabled, String wanIndex, String connectionId) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceDHCP_ethernet_V2. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", modelName = ")
                .append(modelName)
                .append(", serviceName = ")
                .append(serviceName)
                .append(", vlanMux802_1Priority = ")
                .append(vlanMux802_1Priority)
                .append(", vlanMuxID = ")
                .append(vlanMuxID)
                .append(", ipV4Enabled = ")
                .append(ipV4Enabled)
                .append(", ipV6Enabled = ")
                .append(ipV6Enabled)
                .append(", natEnabled = ")
                .append(natEnabled)
                .append(", firewallEnabled = ")
                .append(firewallEnabled)
                .append(", connectionId = ")
                .append(connectionId)
                .append(", wanIndex = ")
                .append(wanIndex);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                IPoEWanServiceDHCP ipoeWanDHCP = new IPoEWanServiceDHCP(serviceName, vlanMux802_1Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled);
                ipoeWanDHCP.setWanIndex(wanIndex);
                ipoeWanDHCP.setConnectionId(connectionId);
                WanServiceObject wanObj = poller.addIPoEWanServiceDHCP_ethernet(ipoeWanDHCP, modelName);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    /**
     * adđ pppoe static ip
     *
     * @param serialNumber
     * @param urlRequestCpe
     * @param modelName
     * @param serviceName
     * @param vlanMux802_1Priority
     * @param vlanMuxID
     * @param ipV4Enabled
     * @param ipV6Enabled
     * @param natEnabled
     * @param firewallEnabled
     * @param wanIndex
     * @param externalIPAdress
     * @param subnetMask
     * @param defaultGateway
     * @param dnsIfName
     * @param dnsServers
     * @param wanConnectionId
     * @return
     */
    @Override
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet_V2(
            String serialNumber, String urlRequestCpe, String modelName,
            String serviceName, int vlanMux802_1Priority,
            int vlanMuxID, boolean ipV4Enabled,
            boolean ipV6Enabled, boolean natEnabled,
            boolean firewallEnabled, String wanIndex, String externalIPAdress,
            String subnetMask, String defaultGateway,
            String dnsIfName, String dnsServers, String wanConnectionId) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceStatic_ethernet_V2. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", modelName = ")
                .append(modelName)
                .append(", serviceName = ")
                .append(serviceName)
                .append(", vlanMux802_1Priority = ")
                .append(vlanMux802_1Priority)
                .append(", vlanMuxID = ")
                .append(vlanMuxID)
                .append(", ipV4Enabled = ")
                .append(ipV4Enabled)
                .append(", ipV6Enabled = ")
                .append(ipV6Enabled)
                .append(", natEnabled = ")
                .append(natEnabled)
                .append(", firewallEnabled = ")
                .append(firewallEnabled)
                .append(", wanIndex = ")
                .append(wanIndex)
                .append(", externalIPAdress = ")
                .append(externalIPAdress)
                .append(", subnetMask = ")
                .append(subnetMask)
                .append(", defaultGateway = ")
                .append(defaultGateway)
                .append(", dnsIfName = ")
                .append(dnsIfName)
                .append(", dnsServers = ")
                .append(dnsServers)
                .append(", wanConnectionId = ")
                .append(wanConnectionId);
        logger.info(sbLog.toString());

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {

            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                IPoEWanServiceStatic ipoeWanStatic = new IPoEWanServiceStatic(
                        serviceName,
                        vlanMux802_1Priority,
                        vlanMuxID,
                        ipV4Enabled,
                        ipV6Enabled,
                        natEnabled,
                        firewallEnabled,
                        externalIPAdress,
                        subnetMask,
                        defaultGateway,
                        dnsIfName,
                        dnsServers
                );
                ipoeWanStatic.setWanIndex(wanIndex);
                ipoeWanStatic.setConnectionId(wanConnectionId);
                WanServiceObject wanObj = poller.addIPoEWanServiceStatic_ethernet(ipoeWanStatic, modelName);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="2015-06-17: Batch Update Firmware">
    @Override
    public NamedKeyValueResponse batUpdateFirmware(ArrayList<ConnectionRequest> listDeviceRequest,
            String fileUrl, String usernameFileServer, String passwordFileServer) {
        final NamedKeyValueResponse res = new NamedKeyValueResponse();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        try {
            // Explorer request
            for (final ConnectionRequest requestInfo : listDeviceRequest) {
                String serialNumber = requestInfo.getSerialNumber();
                String connectionRequestUrl = requestInfo.getUrl();
                Runnable worker = new UpdateFirmwareBatchCommand(
                        serialNumber,
                        connectionRequestUrl,
                        fileUrl,
                        usernameFileServer, passwordFileServer
                ) {

                    @Override
                    protected void onDoneCallback(String err, Object result) {
                        NamedKeyValue value = new NamedKeyValue();
                        if (err != null) {
                            value.setKey(requestInfo.getSerialNumber());
                            value.setValue(String.valueOf(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR));
                            value.setTag(err);
                        } else {
                            value.setKey(requestInfo.getSerialNumber());
                            value.setValue(String.valueOf(WebServiceConfig.ErrorCode.SUCCESS));
                            value.setTag(WebServiceConfig.Message.SUCCESS);
                        }
                        res.addRetValue(value);
                    }
                };
                executor.execute(worker);
            }
            String timeoutStr = System.getProperty("gponacs.batchUpdateTimeout", "300");
            long timeout = Long.parseLong(timeoutStr);
            executor.shutdown();
            boolean done = executor.awaitTermination(timeout, TimeUnit.SECONDS);
            if (!done) {
                List<Runnable> remainTask = executor.shutdownNow();
                for (Runnable item : remainTask) {
                    UpdateFirmwareBatchCommand cm = (UpdateFirmwareBatchCommand) item;
                    NamedKeyValue value = new NamedKeyValue();
                    value.setKey(cm.getSerialNumberCPE());
                    value.setValue(String.valueOf(WebServiceConfig.ErrorCode.TIMEOUT));
                    value.setTag("Command not execute");
                    res.addRetValue(value);
                }
            }
            res.setMessage(WebServiceConfig.Message.SUCCESS);
            return res;

        } catch (Exception ex) {
            res.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            res.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return res;
        }
    }
    //</editor-fold>

    @Override
    public AssociatedDeviceResponse getAssociatedDevice(String serialNumber, String urlRequestCpe) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getAssociatedDevice. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe);
        logger.info(sbLog.toString());

        final AssociatedDeviceResponse res = new AssociatedDeviceResponse();

        try {
            DataFileUtils myStringUtil = new DataFileUtils();
            ArrayList<String> lines = myStringUtil.getLineFromFile(FilePath.GET_ASSOCIATED_DEVICE);
            String root = lines.get(0);
            final String dataTree = StringUtils.stripEnd(root, ".") + ".";
            GetValueCommand cmand = new GetValueCommand("", serialNumber,
                    urlRequestCpe, dataTree);
            boolean ok = cmand.executeCommand();
            if (!ok) {
                Map<String, String> map = cmand.getReturnValue();
                map.put(AssociatedDevice.DataTree.ROOT, root);

                Map<String, AssociatedDevice> result = ModelUtils.getAssociatedDevice(map);
                res.setRetValue(new ArrayList<AssociatedDevice>(result.values()));

                logger.info("getAssociatedDevice: connected " + result.size());
            } else {
                throw new Exception(cmand.getErrorString());
            }
            res.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
            res.setMessage(WebServiceConfig.Message.SUCCESS);
            return res;
        } catch (Exception ex) {
            res.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            res.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return res;
        }
    }

    @Override
    public BasicResponse addObjectValue(String serialNumber, String urlRequestCpe, String dataTree, String typeAddObj, ArrayList<SimpleObject> data) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addObjectValue. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", dataTree = ")
                .append(dataTree)
                .append(", typeAddObj = ")
                .append(typeAddObj)
                .append(", ListObject = ");

        for (SimpleObject tmp : data) {
            sbLog.append("[").append(tmp.toString()).append("]");
        }
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            if (StringUtils.isBlank(typeAddObj)) {
                typeAddObj = AddObjectCommand.ADDOBJ_WANSERVICE;//TYPE_ADDOBJECT;
            }
            String path = StringUtils.stripEnd(dataTree, ".");
            AddObjectCommand cmand = new AddObjectCommand("",
                    serialNumber,
                    urlRequestCpe,
                    path, data,
                    typeAddObj);
            if (!cmand.executeCommand()) {
                int instance = cmand.getInstance();
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
                result.addRetValue(String.valueOf(instance));
                return result;
            } else {
                throw new Exception("Add fail: " + cmand.getErrorString());
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse setAAAConfiguration(String serialNumber, String urlRequestCpe, String model, AAAConfigurationObject AAAConfigObject) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setAAAConfiguration. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", model = ")
                .append(model)
                .append(", AAAConfigObject = ")
                .append(AAAConfigObject.toString());
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            if (StringUtils.isBlank(AAAConfigObject.getAaaInterface())) {
                throw new Exception("AAA Interface is null");
            }

            DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.AAA_Configuration, model);
            dataModel.setValue("ipAAA", AAAConfigObject.getIpAAA());
            dataModel.setValue("authenPort", AAAConfigObject.getAuthenPort());
            dataModel.setValue("acountingPort", AAAConfigObject.getAcountingPort());
            dataModel.setValue("coaPort", AAAConfigObject.getCoaPort());
            dataModel.setValue("secretKey", AAAConfigObject.getSecretKey());
            dataModel.setValue("interimUpdateInterval", AAAConfigObject.getInterimUpdateInterval());
            dataModel.setValue("aaaInterface", AAAConfigObject.getAaaInterface());

            ArrayList<SimpleObject> dataConfig = dataModel.getDataModel();

            SetValueCommand cm = new SetValueCommand("System", serialNumber, urlRequestCpe, dataConfig);

            boolean isError = cm.executeCommand();
            if (!isError) {
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                result.setMessage(cm.getErrorString());
            }
            return result;

        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }

    }

    @Override
    public BasicResponse setWirelessConfiguration(String serialNumber,
            String urlRequestCpe, String model,
            ArrayList<WirelessObject_2> wirelessList) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setWirelessConfiguration. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", model = ")
                .append(model)
                .append(", wirelessList = ");

        for (WirelessObject_2 tmp : wirelessList) {
            sbLog.append("[").append(tmp.toString()).append("]");
        }
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            ArrayList<SimpleObject> dataConfig = new ArrayList<SimpleObject>();
            for (int i = 0; i < wirelessList.size(); i++) {
                String fileName = FilePath.GET_WIRELESS;
                if (i > 0) {
                    fileName += "_" + i;
                }
                WirelessObject_2 item = wirelessList.get(i);
                DataFileModel dataModel = DataFileModel.createDataFileModel(fileName, model);

                dataModel.setValue("Enable", item.getEnable());
                dataModel.setValue("WlEnableSsid", item.getWlEnableSsid());
                dataModel.setValue("WlEnable", item.getWlEnable());
                dataModel.setValue("newSSID", item.getNewSSID());
                dataModel.setValue("oldSSID", item.getOldSSID());
                dataModel.setValue("WlAPIsolation", item.getWlAPIsolation());
                dataModel.setValue("HideSSID", item.getHideSSID());
                dataModel.setValue("WlHide", item.getWlHide());
                dataModel.setValue("DisableWMM", item.getDisableWMM());
                dataModel.setValue("EnableWMF", item.getEnableWMF());
                dataModel.setValue("MaxClient", item.getMaxClient());
                dataModel.setValue("SecurityAuthMode", item.getSecurityAuthMode());
                dataModel.setValue("Password", item.getPassword());
                dataModel.setValue("BeaconType", item.getBeaconType());
                dataModel.setValue("RekeyTime", item.getRekeyTime());

                dataModel.setValue("WlWpa", item.getWlWpa());

                // check Rename SSID
                if (item.isChangeSSIDName()) {

                }

                dataConfig.addAll(dataModel.getDataModel());
            }
            SetValueCommand cm = new SetValueCommand("System", serialNumber, urlRequestCpe, dataConfig);
            boolean isError = cm.executeCommand();
            if (!isError) {
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                result.setMessage(cm.getErrorString());
            }
            return result;
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="2016-01-19: Configure LAN Device by GROUP names">
    @Override
    public ModemInterfaceGroupResponse getInterfaceGroupNames(DeviceInfo deviceInfo) {
        ModemInterfaceGroupResponse result = new ModemInterfaceGroupResponse();
        try {

            DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.Get_InterfaceGroup, deviceInfo.getProductClass());
            String root = dataModel.getRoot().getParameter();
            final String dataTree = StringUtils.stripEnd(root, ".") + ".";
            GetValueCommand cmand = new GetValueCommand("", deviceInfo.getSerialNumber(),
                    deviceInfo.getConnectionRequest(), dataTree);
            boolean ok = cmand.executeCommand();
            if (!ok) {
                Map<String, String> map = cmand.getReturnValue();

                Map<String, BrigdingGroup> brigdingGroups = ModelUtils.parseBrigdingGroup(map);

                ArrayList<BrigdingGroup> bgroups = new ArrayList<BrigdingGroup>();
                SortedSet<String> set = new TreeSet<String>(brigdingGroups.keySet());
                for (String key : set) {
                    bgroups.add(brigdingGroups.get(key));
                }

                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
                result.setBrigdingGroup(bgroups);
            } else {
                throw new Exception(cmand.getErrorString());
            }

            return result;
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }

    @Override
    public LANConfigurationResponse getLanConfiguration(DeviceInfo deviceInfo, String bridgeIndex) {
        LANConfigurationResponse result = new LANConfigurationResponse();
        try {

            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.Get_LanConfiguration, deviceInfo.getProductClass());
            dm.setInstance(bridgeIndex);
            GetValueCommand cm = new GetValueCommand(deviceInfo.getSerialNumber(),
                    deviceInfo.getConnectionRequest());

            List<SimpleObject> datas = dm.getParsedValues();
            for (SimpleObject data : datas) {
                cm.addParameter(data.getParameter());
            }

            boolean ok = cm.executeCommand();
            if (!ok) {
                Map<String, String> map = cm.getReturnValue();
                dm.updateValue(map);
                LANConfiguration lanConfig = dm.cast(LANConfiguration.class);
                lanConfig.setIndex(bridgeIndex);
                result.addRetValue(lanConfig);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception(cm.getErrorString());
            }

            return result;
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }

    @Override
    public BasicResponse setLanConfiguration(DeviceInfo deviceInfo, LANConfiguration lanConfiguration) {
        BasicResponse result = new BasicResponse();
        try {
            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.Set_LanConfiguration, deviceInfo.getProductClass());
            dm.setInstance(lanConfiguration.getIndex());
            dm.updateValueObject(lanConfiguration);
            ArrayList<SimpleObject> datas = (ArrayList<SimpleObject>) dm.getDataModel();
            SetValueCommand cm = new SetValueCommand("", deviceInfo.getSerialNumber(),
                    deviceInfo.getConnectionRequest(),
                    null);
            cm.setListObj(datas);

            boolean ok = cm.executeCommand();
            if (!ok) {
                result.addRetValue(!ok);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception(cm.getErrorString());
            }
            return result;
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }
//</editor-fold>

    @Override
    public BasicResponse setRestore(String serialNumber, String urlRequestCpe,
            String url, String username, String password) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setRestore. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", url = ")
                .append(url)
                .append(", username = ")
                .append(username)
                .append(", password = ")
                .append(password);
        logger.info(sbLog.toString());

        BasicResponse response = new BasicResponse();
        try {
            SetRestoreCommand cmand = new SetRestoreCommand(serialNumber, urlRequestCpe,
                    url, username, password, "3 Vendor Configuration File");
            boolean result = cmand.executeCommand();
            if (!result) {
                response.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                response.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                response.setMessage(cmand.getErrorString());
            }
            return response;
        } catch (AlreadyEnqueuedException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return response;
    }

    @Override
    public BasicResponse setBackUp(String serialNumber, String urlRequestCpe,
            String url, String usernameFileServer, String passwordFileServer) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setBackUp. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", url = ")
                .append(url)
                .append(", usernameFileServer = ")
                .append(usernameFileServer)
                .append(", passwordFileServer = ")
                .append(passwordFileServer);
        logger.info(sbLog.toString());

        BasicResponse response = new BasicResponse();
        try {
            BackupCommand cm = new BackupCommand(serialNumber, urlRequestCpe);
            cm.setFileType("1 Vendor Configuration File");
            cm.setCommandKey("Upload RPC 145318240");
            cm.setUrlFileServer(url);
            cm.setUsernameFileServer(usernameFileServer);
            cm.setPasswordFileServer(passwordFileServer);

            boolean result = cm.executeCommand();
            if (!result) {
                response.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                response.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                response.setMessage(cm.getErrorString());
            }
            return response;
        } catch (AlreadyEnqueuedException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return response;
    }

    @Override
    public BasicResponse setUpload(String serialNumber, String urlRequestCpe, String commandKey,
            String filetype, String url, String username, String password, long filesize,
            String targetFileName, int delaySeconds, String successURL, String failureURL) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setUpload. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", url = ")
                .append(url)
                .append(", username = ")
                .append(username)
                .append(", password = ")
                .append(password)
                .append(", filesize = ")
                .append(filesize)
                .append(", targetFileName = ")
                .append(targetFileName)
                .append(", delaySeconds = ")
                .append(delaySeconds)
                .append(", successURL = ")
                .append(successURL)
                .append(", failureURL = ")
                .append(failureURL);
        logger.info(sbLog.toString());

        BasicResponse response = new BasicResponse();
        try {
            SetUploadCommand ucmand = new SetUploadCommand(serialNumber, urlRequestCpe,
                    commandKey, filetype, url, username, password, filesize, targetFileName,
                    delaySeconds, successURL, failureURL);
            ucmand.setSerialNumberCPE(serialNumber);
            ucmand.setConnectionRequestURL(urlRequestCpe);
            ucmand.setCommandKey(commandKey);
            ucmand.setFileType(filetype);
            ucmand.setUrl(url);
            ucmand.setUserName(username);
            ucmand.setPassWord(password);
            ucmand.setFileSize(filesize);
            ucmand.setTargetFileName(targetFileName);
            ucmand.setDelaySeconds(delaySeconds);
            ucmand.setSuccessUrl(url);
            ucmand.setFailureUrl(failureURL);

            boolean result = ucmand.executeCommand();
            if (!result) {
                response.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                response.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                response.setMessage(ucmand.getErrorString());
            }
            return response;
        } catch (AlreadyEnqueuedException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return response;
    }

    @Override
    public BasicResponse setDownload(String serialNumber, String urlRequestCpe,
            String commandKey, String filetype, String url, String username,
            String password, long filesize, String targetFileName,
            int delaySeconds, String successURL, String failureURL) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setDownload. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", url = ")
                .append(url)
                .append(", username = ")
                .append(username)
                .append(", password = ")
                .append(password)
                .append(", filesize = ")
                .append(filesize)
                .append(", targetFileName = ")
                .append(targetFileName)
                .append(", delaySeconds = ")
                .append(delaySeconds)
                .append(", successURL = ")
                .append(successURL)
                .append(", failureURL = ")
                .append(failureURL);
        logger.info(sbLog.toString());

        BasicResponse response = new BasicResponse();
        try {
            SetDownloadCommand dcmand = new SetDownloadCommand(serialNumber, urlRequestCpe,
                    commandKey, filetype, url, username, password, filesize, targetFileName,
                    delaySeconds, successURL, failureURL);
            dcmand.setSerialNumberCPE(serialNumber);
            dcmand.setConnectionRequestURL(urlRequestCpe);
            dcmand.setCommandKey(commandKey);
            dcmand.setFileType(filetype);
            dcmand.setUrl(url);
            dcmand.setUserName(username);
            dcmand.setPassWord(password);
            dcmand.setFileSize(filesize);
            dcmand.setTargetFileName(targetFileName);
            dcmand.setDelaySeconds(delaySeconds);
            dcmand.setSuccessUrl(url);
            dcmand.setFailureUrl(failureURL);
            boolean result = dcmand.executeCommand();
            if (!result) {
                response.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                response.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                response.setMessage(dcmand.getErrorString());
            }
            return response;
        } catch (AlreadyEnqueuedException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (DeviceNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            response.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            response.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return response;
    }

    @Override
    public AAAConfigurationResponse getAAAConfiguration(DeviceInfo deviceInfo) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getAAAConfiguration. [Input] DeviceInfo = ")
                .append(deviceInfo.toString());
        logger.info(sbLog.toString());

        AAAConfigurationResponse result = new AAAConfigurationResponse();
        try {
            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.AAA_Configuration, deviceInfo.getModelName());
            GetValueCommand cm = new GetValueCommand(deviceInfo.getSerialNumber(),
                    deviceInfo.getConnectionRequest());
            List<SimpleObject> datas = dm.getParsedValues();
            for (SimpleObject data : datas) {
                cm.addParameter(data.getParameter());
            }
            boolean ok = cm.executeCommand();
            if (!ok) {
                Map<String, String> map = cm.getReturnValue();
                dm.updateValue(map);
                AAAConfigurationObject lanConfig = dm.cast(AAAConfigurationObject.class);
                result.addRetValue(lanConfig);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception(cm.getErrorString());
            }
            return result;
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    /**
     * ham ping device to acs
     *
     * @param deviceInfo
     * @param ipPingDiagnostics
     * @return
     */
    @Override
    public IPPingDiagnosticsResponse ipPingDiagnostics(DeviceInfo deviceInfo, IPPingDiagnostics ipPingDiagnostics) {
        sbLog.setLength(0);
        sbLog.append("[Calling] ipPingDiagnostics. [Input] DeviceInfo = ")
                .append(deviceInfo.toString())
                .append(", ipPingDiagnostics = ")
                .append(ipPingDiagnostics.toString());
        logger.info(sbLog.toString());

        IPPingDiagnosticsResponse result = new IPPingDiagnosticsResponse();
        try {
            DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.Set_IPPingDiagnostics, deviceInfo.getModelName());

            dataModel.setValue("diagnosticsState", ipPingDiagnostics.getDiagnosticsState());
            dataModel.setValue("dscp", ipPingDiagnostics.getDscp());
            dataModel.setValue("host", ipPingDiagnostics.getHost());
            dataModel.setValue("numberOfRepetitions", ipPingDiagnostics.getNumberOfRepetitions());
            dataModel.setValue("timeout", ipPingDiagnostics.getTimeout());
            dataModel.setValue("dataBlockSize", ipPingDiagnostics.getDataBlockSize());

            ArrayList<SimpleObject> dataConfig = dataModel.getDataModel();

            PingDiagnosticsCommand pingcm = new PingDiagnosticsCommand("System", deviceInfo.getSerialNumber(), deviceInfo.getConnectionRequest(), dataConfig);

            boolean isError = pingcm.executeCommand();

            // truong hop error ko phai timeout thi tra ve ket qua ngay
            if (isError) {
                throw new Exception(pingcm.getErrorString());
            }

            // truong hop thuc hien lenh ping co timeout thi van doc cac tham so ping o thiet bi
            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.Get_IPPingDiagnostics, deviceInfo.getProductClass());
            GetValueCommand cm = new GetValueCommand(deviceInfo.getSerialNumber(),
                    deviceInfo.getConnectionRequest());
            List<SimpleObject> datas = dm.getParsedValues();
            for (SimpleObject data : datas) {
                cm.addParameter(data.getParameter());
            }
            boolean hasError = cm.executeCommand();
            if (!hasError) {
                Map<String, String> map = cm.getReturnValue();
                dm.updateValue(map);
                IPPingDiagnostics ipPingResult = dm.cast(IPPingDiagnostics.class);
                result.addRetValue(ipPingResult);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception("can not read ping parameter");
            }
            return result;

        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }
    //</editor-fold>

    @Override
    public BasicResponse setWirelessAdvance(String serialNumber, String urlRequestCpe, String modelName, WirelessObject_Advance wirelessObject_Advance) {
        sbLog.setLength(0);
        sbLog.append("[Calling] setWirelessAdvance. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", model = ")
                .append(modelName)
                .append(", wirelessObject_Advance = ")
                .append(wirelessObject_Advance);
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            ArrayList<SimpleObject> dataConfig = new ArrayList<SimpleObject>();
            String fileName = FilePath.Get_Wireless_Advance;
            DataFileModel dataModel = DataFileModel.createDataFileModel(fileName, modelName);
            dataModel.setValue("channel", wirelessObject_Advance.getChannel());
            dataModel.setValue("autoChannelTimer", wirelessObject_Advance.getAutoChannelTimer());
            dataModel.setValue("transmitPower", wirelessObject_Advance.getTransmitPower());
            dataModel.setValue("enable", wirelessObject_Advance.getEnable());
            dataConfig.addAll(dataModel.getDataModel());
            SetValueCommand cm = new SetValueCommand("System", serialNumber, urlRequestCpe, dataConfig);
            boolean isError = cm.executeCommand();
            if (!isError) {
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                result.setMessage(cm.getErrorString());
            }
            return result;
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public WirelessObject_AdvanceResponse getWirelessAdvance(DeviceInfo deviceInfo) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getWirelessAdvance. [Input] DeviceInfo = ")
                .append(deviceInfo.toString());
        logger.info(sbLog.toString());

        WirelessObject_AdvanceResponse result = new WirelessObject_AdvanceResponse();
        try {
            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.Get_Wireless_Advance, deviceInfo.getModelName());
            GetValueCommand cm = new GetValueCommand(deviceInfo.getSerialNumber(),
                    deviceInfo.getConnectionRequest()
            );
            List<SimpleObject> datas = dm.getParsedValues();
            for (SimpleObject data : datas) {
                cm.addParameter(data.getParameter());
            }
            boolean ok = cm.executeCommand();
            if (!ok) {
                Map<String, String> map = cm.getReturnValue();
                dm.updateValue(map);
                WirelessObject_Advance wirelessInfo = dm.cast(WirelessObject_Advance.class);

                result.addRetValue(wirelessInfo);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception(cm.getErrorString());
            }
            return result;
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public ResponseFromDevice pingACSToDeviceMethod(String ipAddress) {

        sbLog.setLength(0);
        sbLog.append("[Calling] pingACSToDeviceMethod. [Input] ipAddress = ")
                .append(ipAddress);
        logger.info(sbLog.toString());

        ResponseFromDevice device = new ResponseFromDevice();
        try {
            device = PingACSToDevice.PingPongACSToDevice(ipAddress);
        } catch (InterruptedException ex) {
            logger.error("pingACSToDeviceMethod", ex);
        }
        return device;
    }

    /*
    @Override
    public BasicResponse1 deleteObject1(String serialNumber, String urlRequestCpe, String path) {
        BasicResponse1 result = new BasicResponse1();
        try {
            TR069ONTPoller poller = new TR069ONTPoller(serialNumber, urlRequestCpe);
            boolean resultDetele = poller.deleteObject(path);
            if (resultDetele) {
                result.addRetValue(resultDetele);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.addRetValue(resultDetele);
                result.setErrorCode(WebServiceConfig.ErrorCode.SYSTEM_FAIL);
                result.setMessage(WebServiceConfig.Message.FAILURE);
            }

        } catch (AlreadyEnqueuedException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }
     */
    @Override
    public GetParameterValueResposne getValue(String serialNumber, String urlRequestCpe, List<String> data) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getValue. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", list = ");
        for (String tmp : data) {
            sbLog.append("[").append(tmp).append("]");
        }
        logger.info(sbLog.toString());

        GetParameterValueResposne result = new GetParameterValueResposne();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                List<ParameterModel> ret = poller.getValue(data);
//                result.setRetValue(ret);
                for (ParameterModel tmp : ret) {
                    result.addRetValue(tmp);
                }
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public BasicResponse scheduleInform(String serialNumber, String urlRequestCpe, int time) {
        sbLog.setLength(0);
        sbLog.append("[Calling] scheduleInform. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", time = ")
                .append(time);
        logger.info(sbLog.toString());

        BasicResponse result = new BasicResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            String ret = poller.scheduleInform(time);
            result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
            result.setMessage(WebServiceConfig.Message.SUCCESS);
            return result;
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public GetParameterNamesResponse getParameterNames(String serialNumber, String urlRequestCpe, String parameter, boolean nextLevel) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getParameterNames. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", parameter = ")
                .append(parameter)
                .append(", nextLevel = ")
                .append(nextLevel);
        logger.info(sbLog.toString());

        GetParameterNamesResponse result = new GetParameterNamesResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null) {
                ArrayList<ParameterInforStruct> ret = (ArrayList) poller.getParameterNames(parameter, nextLevel);
                result.setRetValue(ret);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public DSLDeviceAdslResponse getDslInformation(String modelName, String serialNumber, String urlRequestCpe) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getDslInformation. [Input] Serial = ")
                .append(serialNumber)
                .append(", urlRequest = ")
                .append(urlRequestCpe)
                .append(", modelName = ")
                .append(modelName);
        logger.info(sbLog.toString());

        DSLDeviceAdslResponse result = new DSLDeviceAdslResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                DslDeviceObject wanObj = newPoller.getDslInformationDevice(modelName);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    private String getProductTypeBySerial(String serialNumber) throws DeviceNotFoundException, ProductNotFoundException {
        try {

            AdslDevice device = DatabaseManager.ADSL_DEVICE_DAO.getByAdslSerialNumber(serialNumber);
            if (device == null) {
                throw new DeviceNotFoundException();
            }

            if (device.getProductID() == null) {
                logger.warn("device with null productID: " + serialNumber);
                return null;
            }

            Product product = DatabaseManager.PRODUCT_DAO.get(device.getProductID());
            if (product == null) {
                throw new ProductNotFoundException();
            }

            String productName = product.getProductName();
            return productName;
        } catch (Exception ex) {
            logger.warn("can not getProductTypeBySerial: " + serialNumber, ex);
            return null;
        }
    }

    @Override
    public WanServiceObjectResponse addPPPoEWanService_ethernet_ADSL_ADSLMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceName,
            String username,
            String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            int VPIValue,
            int VCIValue) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addPPPoEWanService_ethernet_ADSL_ADSLMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceName = ").append(serviceName)
                .append(", username = ").append(username)
                .append(", password = ").append(password)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append(", ipV4Enabled = ").append(ipV4Enabled)
                .append(", ipV6Enabled = ").append(ipV6Enabled)
                .append(", natEnabled = ").append(natEnabled)
                .append(", firewallEnabled = ").append(firewallEnabled)
                .append(", VPIValue = ").append(VPIValue)
                .append(", VCIValue = ").append(VCIValue)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                WanServiceObject wanObj = newPoller.addPPPoEWanService_ethernet_ADSL_ADSLMode(
                        serviceName,
                        username, password,
                        vlanMux802_1Priority, vlanMuxID,
                        ipV4Enabled, ipV6Enabled,
                        natEnabled, firewallEnabled,
                        modelName, VPIValue, VCIValue
                );

                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }

        return result;
    }

    @Override
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet_ADSL_ADSLMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceName,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String wanIndex,
            String externalIPAdress,
            String subnetMask,
            String defaultGateway,
            String dnsIfName,
            String dnsServers,
            int VPIValue,
            int VCIValue) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceStatic_ethernet_ADSL_ADSLMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceName = ").append(serviceName)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append(", ipV4Enabled = ").append(ipV4Enabled)
                .append(", ipV6Enabled = ").append(ipV6Enabled)
                .append(", natEnabled = ").append(natEnabled)
                .append(", firewallEnabled = ").append(firewallEnabled)
                .append(", wanIndex = ").append(wanIndex)
                .append(", externalIPAdress = ").append(externalIPAdress)
                .append(", subnetMask = ").append(subnetMask)
                .append(", defaultGateway = ").append(defaultGateway)
                .append(", dnsIfName = ").append(dnsIfName)
                .append(", dnsServers = ").append(dnsServers)
                .append(", VPIValue = ").append(VPIValue)
                .append(", VCIValue = ").append(VCIValue)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                IPoEWanServiceStatic ipoeWanStatic = new IPoEWanServiceStatic(
                        serviceName,
                        vlanMux802_1Priority,
                        vlanMuxID,
                        ipV4Enabled,
                        ipV6Enabled,
                        natEnabled,
                        firewallEnabled,
                        externalIPAdress,
                        subnetMask,
                        defaultGateway,
                        dnsIfName,
                        dnsServers
                );
                WanServiceObject wanObj = newPoller.addIPoEWanServiceStatic_ethernet_ADSL_ADSLMode(ipoeWanStatic, modelName, VPIValue, VCIValue);

                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet_ADSL_ADSLMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceName,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String wanIndex,
            int VPIValue,
            int VCIValue) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceDHCP_ethernet_ADSL_ADSLMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceName = ").append(serviceName)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append(", ipV4Enabled = ").append(ipV4Enabled)
                .append(", ipV6Enabled = ").append(ipV6Enabled)
                .append(", natEnabled = ").append(natEnabled)
                .append(", firewallEnabled = ").append(firewallEnabled)
                .append(", wanIndex = ").append(wanIndex)
                .append(", VPIValue = ").append(VPIValue)
                .append(", VCIValue = ").append(VCIValue)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                IPoEWanServiceDHCP ipoeWanDHCP = new IPoEWanServiceDHCP(serviceName, vlanMux802_1Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled);
                WanServiceObject wanObj = newPoller.addIPoEWanServiceDHCP_ethernet_ADSL_ADSLMode(ipoeWanDHCP, modelName, VPIValue, VCIValue);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addBridgeWanService_ethernet_ADSL_ADSLMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceDescription,
            int vlanMux802_1Priority,
            int vlanMuxID,
            int VPIValue,
            int VCIValue) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addBridgeWanService_ethernet_ADSL_ADSLMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceDescription = ").append(serviceDescription)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append(", VPIValue = ").append(VPIValue)
                .append(", VCIValue = ").append(VCIValue)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                WanServiceObject wanObj = newPoller.addBridgeWanService_ADSL_ADSLMode(serviceDescription, vlanMux802_1Priority, vlanMuxID, modelName, VPIValue, VCIValue);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addPPPoEWanService_ethernet_ADSL_AccessMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceName,
            String username,
            String password,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addPPPoEWanService_ethernet_ADSL_AccessMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceName = ").append(serviceName)
                .append(", username = ").append(username)
                .append(", password = ").append(password)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append(", ipV4Enabled = ").append(ipV4Enabled)
                .append(", ipV6Enabled = ").append(ipV6Enabled)
                .append(", natEnabled = ").append(natEnabled)
                .append(", firewallEnabled = ").append(firewallEnabled)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                WanServiceObject wanObj = newPoller.addPPPoEWanService_ethernet_ADSL_AccessMode(
                        serviceName,
                        username, password,
                        vlanMux802_1Priority, vlanMuxID,
                        ipV4Enabled, ipV6Enabled,
                        natEnabled, firewallEnabled,
                        modelName
                );
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet_ADSL_AccessMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceName,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String wanIndex,
            String externalIPAdress,
            String subnetMask,
            String defaultGateway,
            String dnsIfName,
            String dnsServers) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceStatic_ethernet_ADSL_AccessMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceName = ").append(serviceName)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append(", ipV4Enabled = ").append(ipV4Enabled)
                .append(", ipV6Enabled = ").append(ipV6Enabled)
                .append(", natEnabled = ").append(natEnabled)
                .append(", firewallEnabled = ").append(firewallEnabled)
                .append(", wanIndex = ").append(wanIndex)
                .append(", externalIPAdress = ").append(externalIPAdress)
                .append(", subnetMask = ").append(subnetMask)
                .append(", defaultGateway = ").append(defaultGateway)
                .append(", dnsIfName = ").append(dnsIfName)
                .append(", dnsServers = ").append(dnsServers)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                IPoEWanServiceStatic ipoeWanStatic = new IPoEWanServiceStatic(
                        serviceName,
                        vlanMux802_1Priority,
                        vlanMuxID,
                        ipV4Enabled,
                        ipV6Enabled,
                        natEnabled,
                        firewallEnabled,
                        externalIPAdress,
                        subnetMask,
                        defaultGateway,
                        dnsIfName,
                        dnsServers
                );
                WanServiceObject wanObj = newPoller.addIPoEWanServiceStatic_ethernet_AccessMode(ipoeWanStatic, modelName);

                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet_AccessMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceName,
            int vlanMux802_1Priority,
            int vlanMuxID,
            boolean ipV4Enabled,
            boolean ipV6Enabled,
            boolean natEnabled,
            boolean firewallEnabled,
            String wanIndex) {

        sbLog.setLength(0);
        sbLog.append("[Calling] addIpoEWanServiceDHCP_ethernet_AccessMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceName = ").append(serviceName)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append(", ipV4Enabled = ").append(ipV4Enabled)
                .append(", ipV6Enabled = ").append(ipV6Enabled)
                .append(", natEnabled = ").append(natEnabled)
                .append(", firewallEnabled = ").append(firewallEnabled)
                .append(", wanIndex = ").append(wanIndex)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                IPoEWanServiceDHCP ipoeWanDHCP = new IPoEWanServiceDHCP(serviceName, vlanMux802_1Priority, vlanMuxID, ipV4Enabled, ipV6Enabled, natEnabled, firewallEnabled);
                ipoeWanDHCP.setWanIndex(wanIndex);
                WanServiceObject wanObj = newPoller.addIPoEWanServiceDHCP_ethernet_AccessMode(ipoeWanDHCP, modelName);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WanServiceObjectResponse addBridgeWanService_ethernet_ADSL_AccessMode(
            String serialNumber,
            String urlRequestCpe,
            String modelName,
            String serviceDescription,
            int vlanMux802_1Priority,
            int vlanMuxID) {
        sbLog.setLength(0);
        sbLog.append("[Calling] addBridgeWanService_ethernet_ADSL_AccessMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append(", serviceDescription = ").append(serviceDescription)
                .append(", Vlan802 = ").append(vlanMux802_1Priority)
                .append(", vlanMuxID = ").append(vlanMuxID)
                .append("\n");
        logger.info(sbLog);

        WanServiceObjectResponse result = new WanServiceObjectResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                WanServiceObject wanObj = newPoller.addBridgeWanService_AccessMode(serviceDescription, vlanMux802_1Priority, vlanMuxID, modelName);
                result.addRetValue(wanObj);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;
    }

    @Override
    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet_ADSL_ADSLMode(String serialNumber, String urlRequestCpe, String modelName) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getWANGUIObject_ethernet_ADSL_ADSLMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append("\n");
        logger.info(sbLog);

        WANGUIObjectEthernetResponse result = new WANGUIObjectEthernetResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                result.addRetValue(newPoller.getWANGUIObject_ethernet(modelName));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            result.setErrorCode(getErrorCode(ex));
        }
        return result;
    }

    @Override
    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet_ADSL_AcessMode(String serialNumber, String urlRequestCpe, String modelName) {

        sbLog.setLength(0);
        sbLog.append("[Calling] getWANGUIObject_ethernet_ADSL_AcessMode. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append("\n");
        logger.info(sbLog);
        WANGUIObjectEthernetResponse result = new WANGUIObjectEthernetResponse();
        try {
            String productType = getProductTypeBySerial(serialNumber);
            TR069Poller poller = Tr069PollerFactory.getInstance().getTr069Class(productType, serialNumber, urlRequestCpe);
            if (poller != null && poller instanceof TR069ADSLPoller) {
                TR069ADSLPoller newPoller = (TR069ADSLPoller) poller;
                result.addRetValue(newPoller.getWANGUIObject_ethernet(modelName));
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.UNSUPPORT_POLLER);
                result.setMessage("Unsupport poller");
            }
        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            result.setErrorCode(getErrorCode(ex));
        }
        return result;
    }

    @Override
    public StaticDNSObjectResponse getStaticDNSObject(String serialNumber, String urlRequestCpe, String modelName) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getStaticDNSObject. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append("\n");
        logger.info(sbLog);

        StaticDNSObjectResponse result = new StaticDNSObjectResponse();
        try {
            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.StaticDNS, modelName);
            GetValueCommand cm = new GetValueCommand(serialNumber, urlRequestCpe);
            List<SimpleObject> datas = dm.getParsedValues();
            for (SimpleObject data : datas) {
                cm.addParameter(data.getParameter());
            }
            boolean ok = cm.executeCommand();
            if (!ok) {
                Map<String, String> map = cm.getReturnValue();
                dm.updateValue(map);
                StaticDNSObject castObject = dm.cast(StaticDNSObject.class);
                result.addRetValue(castObject);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception(cm.getErrorString());
            }
            return result;
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }

    @Override
    public BasicResponse setStaticDNSObject(String serialNumber, String urlRequestCpe, String productClass, StaticDNSObject StaticDNSObject) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getStaticDNSObject. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", productClass = ").append(productClass)
                .append(", StaticDNSObject = ").append(StaticDNSObject)
                .append("\n");
        logger.info(sbLog);

        //continue code here
        BasicResponse result = new BasicResponse();
        try {
            DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.StaticDNS, productClass);
            dataModel.setValue("dnSIfName", "");
            dataModel.setValue("dnSServers", StaticDNSObject.getDnSServers());
            ArrayList<SimpleObject> dataConfig = dataModel.getDataModel();
            SetValueCommand cm = new SetValueCommand("System", serialNumber, urlRequestCpe, dataConfig);

            boolean isError = cm.executeCommand();
            if (!isError) {
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                result.setMessage(cm.getErrorString());
            }
            return result;
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }

    @Override
    public StaticDynamicDNSResponse getStaticDynamicDNSObject(String serialNumber, String urlRequestCpe, String modelName) {

        sbLog.setLength(0);
        sbLog.append("[Calling] getStaticDynamicDNSObject. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append("\n");
        logger.info(sbLog);

        StaticDynamicDNSResponse result = new StaticDynamicDNSResponse();
        try {
            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.StaticDynamicDNS, modelName);
            GetValueCommand cm = new GetValueCommand(serialNumber, urlRequestCpe);
            ArrayList<StaticDynamicDNSObject> listReturn = new ArrayList<StaticDynamicDNSObject>();

            String root = dm.getRoot().getParameter();
            cm.addParameter(root + ".");
            boolean ok = cm.executeCommand();

            if (!ok) {
                Map<String, String> map = cm.getReturnValue();
                //sort_Map
                if (!map.isEmpty()) {
                    Map<String, String> treeMap = new TreeMap<String, String>(map);
                    String instance = "";
                    String keyPath = "";
                    StaticDynamicDNSObject temp = new StaticDynamicDNSObject();

                    for (String key : treeMap.keySet()) {
                        String value = treeMap.get(key);
                        if (instance.equals("")) {
                            instance = key.split("\\.")[2];
                            //
                            temp = checkValue(key, value, modelName, temp);
                        } else {
                            String nextInstance = key.split("\\.")[2];
                            if (instance.equals(nextInstance)) {
                                //
                                keyPath = key.split("\\.")[0] + "." + key.split("\\.")[1] + "." + key.split("\\.")[2];
                                temp = checkValue(key, value, modelName, temp);
                            } else {
                                temp.setKeyPath(keyPath);
                                listReturn.add(temp);
                                temp = new StaticDynamicDNSObject();
                                instance = nextInstance;

                                //
                                temp = checkValue(key, value, modelName, temp);
                            }
                        }
                    }
                    temp.setKeyPath(keyPath);
                    listReturn.add(temp);
                    result.setRetValue(listReturn);
                    result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                    result.setMessage(WebServiceConfig.Message.SUCCESS);
                } else {
                    result.setRetValue(listReturn);
                    result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                    result.setMessage(WebServiceConfig.Message.SUCCESS);
                }
            } else {
                throw new Exception(cm.getErrorString());
            }
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

    @Override
    public StaticDynamicDNSObjectResponse setStaticDynamicDNSObject(String serialNumber, String urlRequestCpe, String productClass, StaticDynamicDNSObject staticDynamicDNSObject) {

        sbLog.setLength(0);
        sbLog.append("[Calling] setStaticDynamicDNSObject. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", productClass = ").append(productClass)
                .append(", StaticDynamicDNSObject = ").append(staticDynamicDNSObject)
                .append("\n");
        logger.info(sbLog);

        StaticDynamicDNSObjectResponse result = new StaticDynamicDNSObjectResponse();
        try {
            TR069ONTPoller poller = new TR069ONTPoller(serialNumber, urlRequestCpe);
            StaticDynamicDNSObject object = poller.addDynamicDNSObject(staticDynamicDNSObject, productClass);
            result.addRetValue(object);
            result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
            result.setMessage(WebServiceConfig.Message.SUCCESS);
            return result;
        } catch (Exception ex) {
            // set ErrorCode
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            if (ex instanceof AlreadyEnqueuedException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_ENQUEUED_REQUEST);
            } else if (ex instanceof ConnectTimeoutException) {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONNECTION_ERROR);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
            }
        }
        return result;

    }

    @Override
    public UserAndPassObjectResponse getUserAndPass(String serialNumber, String urlRequestCpe, String modelName) {
        sbLog.setLength(0);
        sbLog.append("[Calling] getUserAndPass. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", modelName = ").append(modelName)
                .append("\n");
        logger.info(sbLog);

        UserAndPassObjectResponse result = new UserAndPassObjectResponse();
        try {
            DataFileModel dm = DataFileModel.createDataFileModel(FilePath.UserAndPass, modelName);
            GetValueCommand cm = new GetValueCommand(serialNumber, urlRequestCpe);
            List<SimpleObject> datas = dm.getParsedValues();
            for (SimpleObject data : datas) {
                cm.addParameter(data.getParameter());
            }
            boolean ok = cm.executeCommand();
            if (!ok) {
                Map<String, String> map = cm.getReturnValue();
                dm.updateValue(map);
                UserAndPassObject castObject = dm.cast(UserAndPassObject.class);
                result.addRetValue(castObject);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception(cm.getErrorString());
            }
            return result;
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }

    @Override
    public BasicResponse setUserAndPass(String serialNumber, String urlRequestCpe, String productClass, UserAndPassObject userAndPassObject) {
        sbLog.append("[Calling] setUserAndPass. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlRequestCpe)
                .append(", productClass = ").append(productClass)
                .append(", UserAndPassObject = ").append(userAndPassObject)
                .append("\n");
        logger.info(sbLog);

        //continue code here
        BasicResponse result = new BasicResponse();
        try {
            DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.UserAndPass, productClass);
            dataModel.setValue("adminUser", userAndPassObject.getAdminUser());
            dataModel.setValue("adminPassword", userAndPassObject.getAdminPassword());
            dataModel.setValue("operatorUser", userAndPassObject.getOperatorUser());
            dataModel.setValue("operatorPassword", userAndPassObject.getOperatorPassword());

            ArrayList<SimpleObject> dataConfig = dataModel.getDataModel();
            SetValueCommand cm = new SetValueCommand("System", serialNumber, urlRequestCpe, dataConfig);

            boolean isError = cm.executeCommand();
            if (!isError) {
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                result.setErrorCode(WebServiceConfig.ErrorCode.CPE_CONFIG_ERROR);
                result.setMessage(cm.getErrorString());
            }
            return result;
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return result;
        }
    }

    private StaticDynamicDNSObject checkValue(String key, String value, String modelName, StaticDynamicDNSObject temp) throws IOException {
        DataFileModel dm = DataFileModel.createDataFileModel(FilePath.StaticDynamicDNS, modelName);

        if (key.contains(dm.get("enable").getParameter())) {
            temp.setEnable(value);
        } else if (key.contains(dm.get("providerName").getParameter())) {
            temp.setProviderName(value);
        } else if (key.contains(dm.get("fullyQualifiedDomainName").getParameter())) {
            temp.setFullyQualifiedDomainName(value);
        } else if (key.contains(dm.get("userName").getParameter())) {
            temp.setUserName(value);
        } else if (key.contains(dm.get("fassword").getParameter())) {
            temp.setPassWord(value);
        } else if (key.contains(dm.get("ifName").getParameter())) {
            temp.setIfName(value);
        }
        return temp;
    }


    @Override
    public TracertDiagnosticsResponse tracertDiagnostics(String serialNumber, String urlrequest, String modelName, TracertDiagnostic tracertDiagnostics) {
        sbLog.setLength(0);
        sbLog.append("[Calling] tracertDiagnostics. [Input] ").append("Serial = ").append(serialNumber)
                .append(", UrlRequest = ").append(urlrequest)
                .append(", modelName = ").append(modelName)
                .append(", tracertDiagnostics = ").append(tracertDiagnostics)
                .append("\n");
        logger.info(sbLog);
        
        TracertDiagnosticsResponse result = new TracertDiagnosticsResponse();
        try {
            DataFileModel dataModel = DataFileModel.createDataFileModel(FilePath.Set_TracertDiagnostics, modelName);

            dataModel.setValue("diagnosticsState", tracertDiagnostics.getDiagnosticState());
            dataModel.setValue("dscp", tracertDiagnostics.getDscp());
            dataModel.setValue("host", tracertDiagnostics.getHost());
            dataModel.setValue("numberOfRepetitions", tracertDiagnostics.getNumberEntries());
            dataModel.setValue("timeout", tracertDiagnostics.getTimeOut());
            dataModel.setValue("dataBlockSize", tracertDiagnostics.getDataBlockSize());

            ArrayList<SimpleObject> dataConfig = dataModel.getDataModel();

            TracertDiagnosticsCommand tracertCm = new TracertDiagnosticsCommand("System", serialNumber, urlrequest, dataConfig);

            boolean isError = tracertCm.executeCommand();

            // truong hop error ko phai timeout thi tra ve ket qua ngay
            if (isError) {
                throw new Exception(tracertCm.getErrorString());
            }

            // truong hop thuc hien lenh ping co timeout thi van doc cac tham so ping o thiet bi
            GetValueCommand cm = new GetValueCommand(serialNumber, urlrequest);
            cm.addParameter(TR069StaticParameter.TRACERT_DIAGNOSTIC);
            boolean hasError = cm.executeCommand();
            if (!hasError) {
                TracertDiagnostic tracertDiagnostic = new TracertDiagnostic();
                Map<String, String> map = cm.getReturnValue();
                Set<String> setRouteHop = new HashSet<String>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String parameter = entry.getKey();
                    String value = entry.getValue();
                    if (parameter.contains("DiagnosticsState")) {
                        tracertDiagnostic.setDiagnosticState(modelName);
                    } else if (parameter.contains("Interface")) {
                        tracertDiagnostic.setInterfaceTracert(value);
                    } else if (parameter.contains("Host")) {
                        tracertDiagnostic.setHost(value);
                    } else if (parameter.contains("NumberOfTries")) {
                        tracertDiagnostic.setNumberEntries(Integer.valueOf(value));
                    } else if (parameter.contains("Timeout")) {
                        tracertDiagnostic.setTimeOut(Integer.valueOf(value));
                    } else if (parameter.contains("DataBlockSize")) {
                        tracertDiagnostic.setDataBlockSize(Integer.valueOf(value));
                    } else if (parameter.contains("DSCP")) {
                        tracertDiagnostic.setDscp(Integer.valueOf(value));
                    } else if (parameter.contains("MaxHopCount")) {
                        tracertDiagnostic.setMaxHopCount(Integer.valueOf(value));
                    } else if (parameter.contains("ResponseTime")) {
                        tracertDiagnostic.setResponseTime(Integer.valueOf(value));
                    } else if (parameter.contains("RouteHopsNumberOfEntries")) {
                        tracertDiagnostic.setRouteHopNumberOfEntries(Integer.valueOf(value));
                    } else if (parameter.contains("RouteHops")) {
                        int x = parameter.lastIndexOf(".");
                        parameter = parameter.substring(0, x);
                        setRouteHop.add(parameter);
                    }
                }
                List<RouteHop> listRouteHop = new ArrayList<RouteHop>();
                Iterator iter = setRouteHop.iterator();
                while (iter.hasNext()) {
                    String value = (String) iter.next();
                    // get instance
                    RouteHop routeHop = new RouteHop();
                    routeHop.setHopHost(value + ".HopHost");
                    routeHop.setHopAddress(value + ".HopHostAddress");
                    routeHop.setHopAddress(value + ".HopErrorCode");
                    routeHop.setHopAddress(value + ".HopErrorCode");
                    listRouteHop.add(routeHop);

                }
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    for (RouteHop tmp : listRouteHop) {
                        if (tmp.getHopAddress().equals(key)) {
                            tmp.setHopAddress(value);
                        } else if (tmp.getHopErrorCode().equals(key)) {
                            tmp.setHopErrorCode(value);
                        } else if (tmp.getHopHost().equals(key)) {
                            tmp.setHopHost(value);
                        } else if (tmp.getHopRTTime().equals(key)) {
                            tmp.setHopRTTime(value);
                        }
                    }
                }
                tracertDiagnostic.setLstRouteHop(listRouteHop);
                result.addRetValue(tracertDiagnostic);
                result.setErrorCode(WebServiceConfig.ErrorCode.SUCCESS);
                result.setMessage(WebServiceConfig.Message.SUCCESS);
            } else {
                throw new Exception("can not read ping parameter");
            }
            return result;

        } catch (DeviceNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.DEVICE_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (ProductNotFoundException ex) {
            result.setErrorCode(WebServiceConfig.ErrorCode.PRODUCT_NOT_FOUND);
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        } catch (Exception ex) {
            result.setErrorCode(getErrorCode(ex));
            result.setMessage(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
        return result;
    }

}
