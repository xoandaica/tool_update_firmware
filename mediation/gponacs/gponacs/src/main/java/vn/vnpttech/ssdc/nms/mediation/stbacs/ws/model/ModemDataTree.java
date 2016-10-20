/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

/**
 *
 * @author Vunb
 * @date Aug 23, 2014
 * @update Aug 23, 2014
 */
public class ModemDataTree {

    // CONSTANT FIX
    public static final String WlIfcname = "WlIfcname";
    public static final String InterfaceDeviceName = "Name";
    public static final String X_InterfaceDeviceName = "X_BROADCOM_COM_IfName";

    // DataType
    public static class DataType {

        public static final String STRING = "xsd:string";
        public static final String BOOLEAN = "xsd:boolean";
        public static final String INT = "xsd:int";
        public static final String UINT = "xsd:unsignedInt";
    }

    public static class Layer2Bridging {

        public static final String Root = "InternetGatewayDevice.Layer2Bridging";
        public static final String BridgeNumberOfEntries = Root + "." + "BridgeNumberOfEntries";
        public static final String FilterNumberOfEntries = Root + "." + "FilterNumberOfEntries";

        public static class Bridge {

            public static final String Root = Layer2Bridging.Root + "." + "Bridge";
            public static final String BridgeEnable = "BridgeEnable";
            public static final String BridgeKey = "BridgeKey";
            public static final String BridgeName = "BridgeName";
            public static final String BridgeStatus = "BridgeStatus";
            public static final String VlanId = "VLANID";
        }

        public static class AvailableInterface {

            public static final String Root = Layer2Bridging.Root + "." + "AvailableInterface";
            public static final String AvailableInterfaceKey = "AvailableInterfaceKey";
            public static final String InterfaceReference = "InterfaceReference";
            public static final String InterfaceType = "InterfaceType";
        }

        public static class Filter {

            public static final String Root = Layer2Bridging.Root + "." + "Filter";
            public static final String FilterBridgeReference = "FilterBridgeReference";
            public static final String FilterEnable = "FilterEnable";
            public static final String FilterInterface = "FilterInterface";
            public static final String FilterKey = "FilterKey";
            public static final String FilterStatus = "FilterStatus";
            public static final String SourceMACFromVendorClassIDFilter = "SourceMACFromVendorClassIDFilter";
            public static final String SourceMACFromVendorClassIDFilterExclude = "SourceMACFromVendorClassIDFilterExclude";
            public static final String X_BROADCOM_COM_VLANIDFilter = "X_BROADCOM_COM_VLANIDFilter";
        }

    }
}
