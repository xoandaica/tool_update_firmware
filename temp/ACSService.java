package net.vnpttech.collection.openacs.services;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlElement;
import net.vnpttech.collection.openacs.myobject.AAAConfigurationObject;

import net.vnpttech.collection.openacs.myobject.Layer2InterfaceObject;
import net.vnpttech.collection.openacs.myobject.SimpleObject;
import net.vnpttech.collection.openacs.myobject.StaticDNSObject;
import net.vnpttech.collection.openacs.myobject.StaticDynamicDNSObject;
import net.vnpttech.collection.openacs.myobject.UserAndPassObject;
import net.vnpttech.collection.openacs.myobject.WirelessObject_2;
import net.vnpttech.collection.openacs.myobject.WirelessObject_Advance;
import net.vnpttech.collection.openacs.ws.model.ConnectionRequest;
import net.vnpttech.collection.openacs.ws.model.DeviceInfo;
import net.vnpttech.collection.openacs.ws.model.IPPingDiagnostics;
import net.vnpttech.collection.openacs.ws.model.LANConfiguration;
import net.vnpttech.collection.openacs.ws.model.TracertDiagnostic;
import net.vnpttech.collection.openacs.ws.model.WirelessSecurity;
import net.vnpttech.collection.openacs.ws.response.AAAConfigurationResponse;
import net.vnpttech.collection.openacs.ws.response.AssociatedDeviceResponse;
import net.vnpttech.collection.openacs.ws.response.BasicResponse;
import net.vnpttech.collection.openacs.ws.response.DSLDeviceAdslResponse;
import net.vnpttech.collection.openacs.ws.response.GetParameterNamesResponse;
import net.vnpttech.collection.openacs.ws.response.GetParameterValueResposne;
import net.vnpttech.collection.openacs.ws.response.IPPingDiagnosticsResponse;
import net.vnpttech.collection.openacs.ws.response.LANConfigurationResponse;
import net.vnpttech.collection.openacs.ws.response.LanSetupObjectResponse;
import net.vnpttech.collection.openacs.ws.response.Layer2InterfaceObjectResponse;
import net.vnpttech.collection.openacs.ws.response.Layer3GUIObjectResponse;
import net.vnpttech.collection.openacs.ws.response.ModemInterfaceGroupResponse;
import net.vnpttech.collection.openacs.ws.response.NamedKeyValueResponse;
import net.vnpttech.collection.openacs.ws.response.ResponseFromDevice;
import net.vnpttech.collection.openacs.ws.response.StaticDNSObjectResponse;
import net.vnpttech.collection.openacs.ws.response.StaticDynamicDNSObjectResponse;
import net.vnpttech.collection.openacs.ws.response.StaticDynamicDNSResponse;
import net.vnpttech.collection.openacs.ws.response.TracertDiagnosticsResponse;
import net.vnpttech.collection.openacs.ws.response.UserAndPassObjectResponse;
import net.vnpttech.collection.openacs.ws.response.WANGUIObjectEthernetResponse;
import net.vnpttech.collection.openacs.ws.response.WANGUIObjectResponse;
import net.vnpttech.collection.openacs.ws.response.WanServiceObjectResponse;
import net.vnpttech.collection.openacs.ws.response.WirelessObjectResponse;
import net.vnpttech.collection.openacs.ws.response.WirelessObject_AdvanceResponse;
import net.vnpttech.collection.openacs.ws.response.WirelessSecurityResponse;

@WebService
@SOAPBinding(style = Style.RPC)
public interface ACSService {

    @WebMethod
    BasicResponse addObjectValue(@WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "dataTree") @XmlElement(required = true) String dataTree,
            @WebParam(name = "typeAddObj")
            @XmlElement(required = true) String typeAddObj,
            @WebParam(name = "simpleObject")
            @XmlElement(required = true) ArrayList<SimpleObject> data
    );

    @WebMethod
    BasicResponse getDataTree(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "dataTree") @XmlElement(required = true) String dataTree);

    //setvalue
    @WebMethod
    public BasicResponse setValue(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "simpleObject") ArrayList<SimpleObject> data);

    // TODO xoa
    @WebMethod
    public Layer2InterfaceObjectResponse addLayer2ATMObj(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "vpi") @XmlElement(required = true) int vpi,
            @WebParam(name = "vci") @XmlElement(required = true) int vci,
            @WebParam(name = "dslLatency") @XmlElement(required = true) int dslLatency);

    // TODO xoa
    @WebMethod
    public WanServiceObjectResponse addPPPoEWanService(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "layer2Interface") @XmlElement(required = true) Layer2InterfaceObject layer2,
            @WebParam(name = "description") @XmlElement(required = true) String description,
            @WebParam(name = "username") @XmlElement(required = true) String username,
            @WebParam(name = "password") @XmlElement(required = true) String password,
            @WebParam(name = "servicename") @XmlElement(required = true) String servicename);

    // TODO xoa
    @WebMethod
    public WanServiceObjectResponse addIpoEWanService(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "layer2Interface") @XmlElement(required = true) Layer2InterfaceObject layer2,
            @WebParam(name = "type") @XmlElement(required = true) String type,
            @WebParam(name = "description") @XmlElement(required = true) String description,
            @WebParam(name = "ipaddress") @XmlElement(required = true) String ipaddress,
            @WebParam(name = "subnetmask") @XmlElement(required = true) String subnetmask,
            @WebParam(name = "gateway") @XmlElement(required = true) String gateway,
            @WebParam(name = "dns1") @XmlElement(required = true) String dns1,
            @WebParam(name = "dns2") @XmlElement(required = true) String dns2,
            @WebParam(name = "nat") @XmlElement(required = true) boolean nat,
            @WebParam(name = "firewall") @XmlElement(required = true) boolean firewall);

    // TODO xoa
    @WebMethod
    public BasicResponse addLANStaticIPLease(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "macAddress") @XmlElement(required = true) String macAddress,
            @WebParam(name = "ipAddress") @XmlElement(required = true) String ipAddress);

    @WebMethod
    public BasicResponse addStaticRoute(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "destIP") @XmlElement(required = true) String destIP,
            @WebParam(name = "subnetMask") @XmlElement(required = true) String subnetMask,
            @WebParam(name = "type") @XmlElement(required = true) String type,
            @WebParam(name = "ifname") @XmlElement(required = true) String ifname,
            @WebParam(name = "gateway") @XmlElement(required = true) String gateway);

    // TODO xoa
    @WebMethod
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
            @XmlElement(required = true) int vci);

    //hoangtuan
    @WebMethod
    BasicResponse setAAAConfiguration(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "productClass")
            @XmlElement(required = true) String productClass,
            @WebParam(name = "AAAConfiguration")
            @XmlElement(required = true) AAAConfigurationObject AAAConfigObject
    );

    @WebMethod
    AAAConfigurationResponse getAAAConfiguration(
            @WebParam(name = "deviceInfo")
            @XmlElement(required = true) DeviceInfo deviceInfo
    );

    @WebMethod
    BasicResponse setWirelessConfiguration(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "productClass")
            @XmlElement(required = true) String productClass,
            @WebParam(name = "wirelessConfiguration")
            @XmlElement(required = true) ArrayList<WirelessObject_2> WirelessObject_2
    );

    @WebMethod
    BasicResponse setWirelessAdvance(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "productClass")
            @XmlElement(required = true) String productClass,
            @WebParam(name = "wirelessObjectAdvanceConfiguration")
            @XmlElement(required = true) WirelessObject_Advance wirelessObject_Advance
    );

    @WebMethod
    public WirelessObject_AdvanceResponse getWirelessAdvance(
            @WebParam(name = "deviceInfo")
            @XmlElement(required = true) DeviceInfo deviceInfo);

    @WebMethod
    BasicResponse setBackUp(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "url") @XmlElement(required = true) String url,
            @WebParam(name = "username") @XmlElement(required = true) String username,
            @WebParam(name = "password") @XmlElement(required = true) String password
    );

    @WebMethod
    BasicResponse setRestore(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "url") @XmlElement(required = true) String url,
            @WebParam(name = "username") @XmlElement(required = true) String username,
            @WebParam(name = "password") @XmlElement(required = true) String password
    );

    @WebMethod
    BasicResponse setDownload(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "commandKey") @XmlElement(required = true) String commandKey,
            @WebParam(name = "fileType") @XmlElement(required = true) String filetype,
            @WebParam(name = "url") @XmlElement(required = true) String url,
            @WebParam(name = "userName") @XmlElement(required = true) String username,
            @WebParam(name = "passWord") @XmlElement(required = true) String password,
            @WebParam(name = "fileSize") @XmlElement(required = true) long filesize,
            @WebParam(name = "targetFileName") @XmlElement(required = true) String targetFileName,
            @WebParam(name = "delaySeconds") @XmlElement(required = true) int delaySeconds,
            @WebParam(name = "successUrl") @XmlElement(required = true) String successURL,
            @WebParam(name = "failureUrl") @XmlElement(required = true) String failureURL
    );

    @WebMethod
    BasicResponse setUpload(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "commandKey") @XmlElement(required = true) String commandKey,
            @WebParam(name = "filetype") @XmlElement(required = true) String filetype,
            @WebParam(name = "url") @XmlElement(required = true) String url,
            @WebParam(name = "username") @XmlElement(required = true) String username,
            @WebParam(name = "password") @XmlElement(required = true) String password,
            @WebParam(name = "filesize") @XmlElement(required = true) long filesize,
            @WebParam(name = "targetFileName") @XmlElement(required = true) String targetFileName,
            @WebParam(name = "delaySeconds") @XmlElement(required = true) int delaySeconds,
            @WebParam(name = "successURL") @XmlElement(required = true) String successURL,
            @WebParam(name = "failureURL") @XmlElement(required = true) String failureURL
    );
    ////////////////////////////////////////////////////////////////////////////

    // TODO xoa
    @WebMethod
    public WanServiceObjectResponse addBridgeWanService_ethernet(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe);

    // TODO xoa
    @WebMethod
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
    );

    @WebMethod
    public ModemInterfaceGroupResponse addInterfaceGroup(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String requestUrl,
            @WebParam(name = "groupName")
            @XmlElement(required = true) String groupName,
            @WebParam(name = "wanInterface")
            @XmlElement(required = true) String wanInterface,
            @WebParam(name = "lanInterface")
            @XmlElement(required = true) String lanInterface);

    @WebMethod
    BasicResponse removeInterfaceGroup(
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
    );

    @WebMethod
    public ModemInterfaceGroupResponse getDeviceInterfaceGroups(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String ip);

    // TODO xoa
    @WebMethod
    public WanServiceObjectResponse addPPPoEWanService_ethernet(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "username")
            @XmlElement(required = true) String username,
            @WebParam(name = "password")
            @XmlElement(required = true) String password,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled
    );

    // TODO xoa
    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            //            @WebParam(name = "ipoeWanDHCP")
            //            @XmlElement(required = true) IPoEWanServiceDHCP ipoeWanDHCP
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            //            @WebParam(name = "username")
            //            @XmlElement(required = true) String username,
            //            @WebParam(name = "password")
            //            @XmlElement(required = true) String password,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled
    );

    // TODO xoa
    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            //            @WebParam(name = "ipoeWanStatic")
            //            @XmlElement(required = true) IPoEWanServiceStatic ipoeWanStatic
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            //                @WebParam(name = "username")
            //                @XmlElement(required = true) String username,
            //                @WebParam(name = "password")
            //                @XmlElement(required = true) String password,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "externalIPAdress")
            @XmlElement(required = true) String externalIPAdress,
            @WebParam(name = "subnetMask")
            @XmlElement(required = true) String subnetMask,
            @WebParam(name = "defaultGateway")
            @XmlElement(required = true) String defaultGateway,
            @WebParam(name = "dnsIfName")
            @XmlElement(required = true) String dnsIfName,
            @WebParam(name = "dnsServers")
            @XmlElement(required = true) String dnsServers
    );

    @WebMethod
    public BasicResponse getUptime(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    // TODO xoa
    @WebMethod
    public Layer2InterfaceObjectResponse getAtmInterface(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    // TODO xoa
    @WebMethod
    public WANGUIObjectResponse getWANGUIObject(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    // TODO xoa
    @WebMethod
    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    @WebMethod
    public LanSetupObjectResponse getLanSetup(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "bridgeIndex") @XmlElement(required = true) String bridgeIndex);

    @WebMethod
    public WirelessObjectResponse getWireLessInfor(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    @WebMethod
    public Layer3GUIObjectResponse getLayer3GUIObject(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    @WebMethod
    public BasicResponse deleteObject(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "path") @XmlElement(required = true) String path);

    @WebMethod
    public BasicResponse upgradeFirmware(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "url") @XmlElement(required = true) String url,
            @WebParam(name = "username") @XmlElement(required = true) String username,
            @WebParam(name = "password") @XmlElement(required = true) String password);

    @WebMethod
    public BasicResponse rebootDevice(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    @WebMethod
    public BasicResponse factoryReset(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    @WebMethod
    public WirelessSecurityResponse getAllWirelessSecurity(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    @WebMethod
    public BasicResponse setWirelessSecurity(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "wirelessSecurity") @XmlElement(required = true) WirelessSecurity wlSecurity);

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Add Features: Support GPON V2">
    @WebMethod
//    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet_V2(
//            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
//            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
//            @WebParam(name = "ontVersion") @XmlElement(required = true) int ontVersion);

    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet_V2(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName);

    @WebMethod
    public WanServiceObjectResponse addBridgeWanService_ethernet_V2(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceDescription")
            @XmlElement(required = true) String serviceDescription,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "connectionId")
            @XmlElement(required = true) String connectionId,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) int wanIndex
    );

    @WebMethod
    public WanServiceObjectResponse addPPPoEWanService_ethernet_V2(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "username")
            @XmlElement(required = true) String username,
            @WebParam(name = "password")
            @XmlElement(required = true) String password,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "connectionId")
            @XmlElement(required = true) String connectionId,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) int wanIndex
    );

    @WebMethod
    public WanServiceObjectResponse addPPPoEWanService_ethernet_ADSL_ADSLMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "username")
            @XmlElement(required = true) String username,
            @WebParam(name = "password")
            @XmlElement(required = true) String password,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "VPIValue")
            @XmlElement(required = true) int VPIValue,
            @WebParam(name = "VCIValue")
            @XmlElement(required = true) int VCIValue
    );

    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet_ADSL_ADSLMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) String wanIndex,
            @WebParam(name = "externalIPAdress")
            @XmlElement(required = true) String externalIPAdress,
            @WebParam(name = "subnetMask")
            @XmlElement(required = true) String subnetMask,
            @WebParam(name = "defaultGateway")
            @XmlElement(required = true) String defaultGateway,
            @WebParam(name = "dnsIfName")
            @XmlElement(required = true) String dnsIfName,
            @WebParam(name = "dnsServers")
            @XmlElement(required = true) String dnsServers,
            @WebParam(name = "VPIValue")
            @XmlElement(required = true) int VPIValue,
            @WebParam(name = "VCIValue")
            @XmlElement(required = true) int VCIValue
    );

    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet_ADSL_ADSLMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) String wanIndex,
            @WebParam(name = "VPIValue")
            @XmlElement(required = true) int VPIValue,
            @WebParam(name = "VCIValue")
            @XmlElement(required = true) int VCIValue
    );

    @WebMethod
    public WanServiceObjectResponse addBridgeWanService_ethernet_ADSL_ADSLMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceDescription")
            @XmlElement(required = true) String serviceDescription,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "VPIValue")
            @XmlElement(required = true) int VPIValue,
            @WebParam(name = "VCIValue")
            @XmlElement(required = true) int VCIValue
    );

    @WebMethod
    public WanServiceObjectResponse addPPPoEWanService_ethernet_ADSL_AccessMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "username")
            @XmlElement(required = true) String username,
            @WebParam(name = "password")
            @XmlElement(required = true) String password,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled
    );

    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet_ADSL_AccessMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) String wanIndex,
            @WebParam(name = "externalIPAdress")
            @XmlElement(required = true) String externalIPAdress,
            @WebParam(name = "subnetMask")
            @XmlElement(required = true) String subnetMask,
            @WebParam(name = "defaultGateway")
            @XmlElement(required = true) String defaultGateway,
            @WebParam(name = "dnsIfName")
            @XmlElement(required = true) String dnsIfName,
            @WebParam(name = "dnsServers")
            @XmlElement(required = true) String dnsServers
    );

    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet_AccessMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) String wanIndex
    );

    @WebMethod
    public WanServiceObjectResponse addBridgeWanService_ethernet_ADSL_AccessMode(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceDescription")
            @XmlElement(required = true) String serviceDescription,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID
    );

    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceDHCP_ethernet_V2(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) String wanIndex,
            @WebParam(name = "connectionId")
            @XmlElement(required = true) String connectionId
    );

    @WebMethod
    public WanServiceObjectResponse addIpoEWanServiceStatic_ethernet_V2(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName")
            @XmlElement(required = true) String modelName,
            @WebParam(name = "serviceName")
            @XmlElement(required = true) String serviceName,
            //                @WebParam(name = "username")
            //                @XmlElement(required = true) String username,
            //                @WebParam(name = "password")
            //                @XmlElement(required = true) String password,
            @WebParam(name = "vlanMuxPriority")
            @XmlElement(required = true) int vlanMux802_1Priority,
            @WebParam(name = "vlanMuxID")
            @XmlElement(required = true) int vlanMuxID,
            @WebParam(name = "ipV4Enabled")
            @XmlElement(required = true) boolean ipV4Enabled,
            @WebParam(name = "ipV6Enabled")
            @XmlElement(required = true) boolean ipV6Enabled,
            @WebParam(name = "natEnabled")
            @XmlElement(required = true) boolean natEnabled,
            @WebParam(name = "firewallEnabled")
            @XmlElement(required = true) boolean firewallEnabled,
            @WebParam(name = "wanIndex")
            @XmlElement(required = true) String wanIndex,
            @WebParam(name = "externalIPAdress")
            @XmlElement(required = true) String externalIPAdress,
            @WebParam(name = "subnetMask")
            @XmlElement(required = true) String subnetMask,
            @WebParam(name = "defaultGateway")
            @XmlElement(required = true) String defaultGateway,
            @WebParam(name = "dnsIfName")
            @XmlElement(required = true) String dnsIfName,
            @WebParam(name = "dnsServers")
            @XmlElement(required = true) String dnsServers,
            @WebParam(name = "wanConnectionId")
            @XmlElement(required = true) String wanConnectionId
    );

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Add features: Batch Update Firmware">
    @WebMethod
    public NamedKeyValueResponse batUpdateFirmware(
            @WebParam(name = "listDeviceRequest")
            @XmlElement(required = true, name = "connectionRequest") ArrayList<ConnectionRequest> listDeviceRequest,
            @WebParam(name = "fileUrl") @XmlElement(required = true) String fileUrl,
            @WebParam(name = "usernameFileServer") @XmlElement(required = true) String usernameFileServer,
            @WebParam(name = "passwordFileServer") @XmlElement(required = true) String passwordFileServer);
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="2015-07-17: Add getAssociatedDevice">
    // TODO xoa
    @WebMethod
    public AssociatedDeviceResponse getAssociatedDevice(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="2016-01-19: edit LAN configuration">
    @WebMethod
    public ModemInterfaceGroupResponse getInterfaceGroupNames(
            @WebParam(name = "deviceInfo")
            @XmlElement(required = true) DeviceInfo deviceInfo);

    @WebMethod
    public LANConfigurationResponse getLanConfiguration(
            @WebParam(name = "deviceInfo") @XmlElement(required = true) DeviceInfo deviceInfo,
            @WebParam(name = "bridgeIndex") @XmlElement(required = true) String bridgeIndex
    );

    @WebMethod
    public BasicResponse setLanConfiguration(
            @WebParam(name = "deviceInfo") @XmlElement(required = true) DeviceInfo deviceInfo,
            @WebParam(name = "lanConfiguration") @XmlElement(required = true) LANConfiguration lanConfiguration
    );
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="2016-02-18: add IPPingDiagnostics">
    @WebMethod
    public IPPingDiagnosticsResponse ipPingDiagnostics(
            @WebParam(name = "deviceInfo") @XmlElement(required = true) DeviceInfo deviceInfo,
            @WebParam(name = "ipPingDiagnostics") @XmlElement(required = true) IPPingDiagnostics ipPingDiagnostics);

    @WebMethod
    public TracertDiagnosticsResponse tracertDiagnostics(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "urlRequest") @XmlElement(required = true) String urlrequest,
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName,
            @WebParam(name = "tracertDiagnostics") @XmlElement(required = true) TracertDiagnostic ipPingDiagnostics);

    //</editor-fold>
    @WebMethod
    public ResponseFromDevice pingACSToDeviceMethod(String ipAddress);

    /*
     @WebMethod
     public BasicResponse1 deleteObject1(
     @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
     @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
     @WebParam(name = "path") @XmlElement(required = true) String path);
     */
    @WebMethod
    public GetParameterValueResposne getValue(@WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "listPath")
            @XmlElement(required = true) List<String> data);

    @WebMethod
    public BasicResponse scheduleInform(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "timeSchedule") @XmlElement(required = true) int time);

    @WebMethod
    public GetParameterNamesResponse getParameterNames(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "parameter") @XmlElement(required = true) String parameter,
            @WebParam(name = "nextLevel") @XmlElement(required = true) boolean nextLevel);

    @WebMethod
    public DSLDeviceAdslResponse getDslInformation(
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName,
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe);

    // TODO xoa
    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet_ADSL_ADSLMode(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName);

    // TODO xoa
    public WANGUIObjectEthernetResponse getWANGUIObject_ethernet_ADSL_AcessMode(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName);

    @WebMethod
    public StaticDNSObjectResponse getStaticDNSObject(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName);

    @WebMethod
    BasicResponse setStaticDNSObject(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "productClass")
            @XmlElement(required = true) String productClass,
            @WebParam(name = "staticDNSObject")
            @XmlElement(required = true) StaticDNSObject staticDNSObject
    );

    @WebMethod
    StaticDynamicDNSResponse getStaticDynamicDNSObject(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName);

    @WebMethod
    public StaticDynamicDNSObjectResponse setStaticDynamicDNSObject(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "productClass")
            @XmlElement(required = true) String productClass,
            @WebParam(name = "staticDynamicDNSObject")
            @XmlElement(required = true) StaticDynamicDNSObject staticDynamicDNSObject
    );

    @WebMethod
    public UserAndPassObjectResponse getUserAndPass(
            @WebParam(name = "serialNumber") @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl") @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "modelName") @XmlElement(required = true) String modelName);

    @WebMethod
    BasicResponse setUserAndPass(
            @WebParam(name = "serialNumber")
            @XmlElement(required = true) String serialNumber,
            @WebParam(name = "requestUrl")
            @XmlElement(required = true) String urlRequestCpe,
            @WebParam(name = "productClass")
            @XmlElement(required = true) String productClass,
            @WebParam(name = "userAndPassObject")
            @XmlElement(required = true) UserAndPassObject userAndPassObject
    );
}
