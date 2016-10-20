package vn.vnpttech.ssdc.nms.mediation.stbacs.services;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlElement;

import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model.DeviceInfo;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response.BasicResponse;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ws.response.DeviceInfoResponse;

@WebService
@SOAPBinding(style = Style.RPC)
public interface ACSService {

    public static final String METHOD_GETDATA_TREE = "getDataTree";

    @WebMethod
    BasicResponse getDataTree(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "dataTree")
            @XmlElement(required = true, name = "parameter") String dataTreeItem,
            @WebParam(name = "connectionRequest")
            @XmlElement(required = true) String connectionRequest
    );

    public static final String METHOD_SETVALUE = "setValue";

    @WebMethod
    public BasicResponse setValue(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "simpleObject")
            @XmlElement(required = true) ArrayList<SimpleObject> data
    );

    public static final String METHOD_DELETE_OBJECT = "deleteObject";

    @WebMethod
    public BasicResponse deleteObject(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "path") @XmlElement(required = true) String path
    );

    public static final String METHOD_UPGRADE_FIRMWARE = "upgradeFirmware";

    @WebMethod
    public BasicResponse upgradeFirmware(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "connectionRequest")
            @XmlElement(required = true) String connectionRequest,
            @WebParam(name = "fileUrl")
            @XmlElement(required = true) String fileUrl,
            @WebParam(name = "version")
            @XmlElement(required = true) String version,
            @WebParam(name = "usernameFileServer")
            @XmlElement(required = true) String usernameFileServer,
            @WebParam(name = "passwordFileServer")
            @XmlElement(required = true) String passwordFileServer
    );

    public static final String METHOD_UPGRADE_FIRMWARE_POLICY = "upgradeFirmwareByPolicy";

    @WebMethod
    public BasicResponse upgradeFirmwareByPolicy(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "fileUrl")
            @XmlElement(required = true) String fileUrl,
            @WebParam(name = "version")
            @XmlElement(required = true) String version,
            @WebParam(name = "usernameFileServer")
            @XmlElement(required = true) String usernameFileServer,
            @WebParam(name = "passwordFileServer")
            @XmlElement(required = true) String passwordFileServer,
            @WebParam(name = "policyId")
            @XmlElement(required = true) long policyId,
            @WebParam(name = "deviceInfo")
            @XmlElement(required = true) DeviceInfo deviceInfo
    );

    public static final String METHOD_REBOOT_DEVICE = "rebootDevice";

    @WebMethod
    public BasicResponse rebootDevice(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "connectionRequest")
            @XmlElement(required = true) String connectionRequest
    );

    public static final String METHOD_GET_DEVICE_INFO = "getDeviceInfo";

    @WebMethod
    public DeviceInfoResponse getDeviceInfo(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "connectionRequest")
            @XmlElement(required = true) String connectionRequest
    );

    public static final String METHOD_SET_USERACCOUNT = "setUserAccount";

    @WebMethod
    public BasicResponse setUserAccount(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "userName")
            @XmlElement(required = true) String userName,
            @WebParam(name = "password")
            @XmlElement(required = true) String password,
            @WebParam(name = "authenticationURL")
            @XmlElement(required = true) String authURL
    );

    public static final String METHOD_SET_DEFAULTGATEWAYDNS = "setDefaulGatewayDNS";

    @WebMethod
    public BasicResponse setDefaulGatewayDNS(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "connectionRequest")
            @XmlElement(required = true) String connectionRequest
    );

    public static final String METHOD_GETCPE_STATUS = "getCPEStatus";

    @WebMethod
    public BasicResponse getCPEStatus(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber
    );

    public static final String METHOD_SET_INTERVAL_ACSURL = "setIntervalAcsUrl";

    @WebMethod
    public BasicResponse setIntervalAcsUrl(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "acsUrl")
            @XmlElement(required = true) String acsUrl,
            @WebParam(name = "connectionRequest")
            @XmlElement(required = true) String connectionRequest,
            @WebParam(name = "interval")
            @XmlElement(required = true) int interval
    );

    public static final String METHOD_SET_STATICROUTE = "setStaticRoute";

    @WebMethod
    public BasicResponse setStaticRoute(
            @WebParam(name = "destination")
            @XmlElement(required = true) String destination,
            @WebParam(name = "subnetMask")
            @XmlElement(required = true) String subnetMask,
            @WebParam(name = "interfacename")
            @XmlElement(required = true) String interfacename,
            @WebParam(name = "gateway")
            @XmlElement(required = true) String gateway,
            @WebParam(name = "connectionRequest")
            @XmlElement(required = true) String connectionRequest,
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber
    );
}
