package vn.vnpttech.ssdc.nms.mediation.stbacs.mycommand;

public class TR069StaticParameter {
	public final static String EVENT_0="0 BOOTSTRAP";
	public final static String EVENT_1 = "1 BOOT";
	public final static String EVENT_2 = "2 PERIODIC";
	public final static String EVENT_4 = "4 VALUE CHANGE";
	public final static String EVENT_6 = "6 CONNECTION REQUEST";
	public final static String EVENT_7 = "7 TRANSFERCOMPLETE";
	
	public final static String IPoE_TYPE = "IPoE";
	public final static String PPPoE_TYPE = "PPPoE";
	
	public final static String LinkType_EOA = "EoA";
	public final static String LinkType_PPPoA = "PPPoA";
	public final static String LinkType_IPoA = "IPoA";
	
	public final static int DSLLatency_Path0 = 0;
	public final static int DSLLatency_Path1 = 1;
	public final static int DSLLatency_Both = 4;
	
	public final static String AddressingType_Static = "Static";
	public final static String AddressingType_DHCP = "DHCP";
	
	public final static String ManagementURL = "InternetGatewayDevice.ManagementServer.URL";
	public final static String ManagementBoundIfName = "InternetGatewayDevice.ManagementServer.X_BROADCOM_COM_BoundIfName";
	public final static String PeriodicInformEnable = "InternetGatewayDevice.ManagementServer.PeriodicInformEnable";
	public final static String PeriodicInformInterval = "InternetGatewayDevice.ManagementServer.PeriodicInformInterval";
	//public final static String interval = "299";//second
	
	public final static String DeviceInfo = "InternetGatewayDevice.DeviceInfo.";
	public final static String DeviceInfoModelName = "InternetGatewayDevice.DeviceInfo.ModelName";
	public final static String OpticalInfo = "InternetGatewayDevice.OpticalInfo.";
	public final static String LanPerformancePath = "InternetGatewayDevice.LANDevice.1.LANEthernetInterfaceConfig.";
	public final static String WLanPerformancePath = "InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.";
	public final static String WanPerformancePath = "InternetGatewayDevice.WANDevice.%s.WANConnectionDevice.";
	public final static String DslPerformancePath = "InternetGatewayDevice.WANDevice.1.WANDSLInterfaceConfig.";
	
	public final static String WANPPPoE = "WANPPPConnection";
	public final static String WANIPoE = "WANIPConnection";
	
	public final static String Default_Gateway = "InternetGatewayDevice.Layer3Forwarding.X_BROADCOM_COM_DefaultConnectionServices";
	public final static String Default_DNS = "InternetGatewayDevice.X_BROADCOM_COM_NetworkConfig.DNSIfName";
	
//	public static boolean ENABLE_AUTO_UPDATE_FIRMWARE = false;
//	public static boolean ENABLE_ZERO_TOUCH = false;
	
	public final static String WlAuthMode_Open = "Open";
	public final static String WlAuthMode_WPA_PSK = "WPA_PSK";
	public final static String WlAuthMode_WPA2_PSK = "WPA2_PSK";
}
