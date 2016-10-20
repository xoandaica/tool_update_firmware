/*
 * Copyright 2014 VNPT-Technology. All rights reserved.
 * VNPT-Technology PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.ws.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;

/**
 *
 * @author Vunb
 * @date Aug 23, 2014
 * @update Aug 23, 2014
 */
public class ModelUtils {

    private static Map<String, String> getMapDataTreeValue(Map<String, String> map, String dataTree) {
        HashMap<String, String> result = new HashMap<String, String>();
        for (String item : map.keySet()) {

            int fromIndex = dataTree.length();
            int toIndex = item.indexOf('.', fromIndex);
            if (toIndex >= 0) {
                result.put(item, map.get(item));
            }
        }
        return result;
    }

    public static HashMap<String, BrigdingGroup> parseBrigdingGroup(Map<String, String> map) {
        String dataTree = ModemDataTree.Layer2Bridging.Bridge.Root + ".";
        HashMap<String, BrigdingGroup> result = new HashMap<String, BrigdingGroup>();
        for (String key : map.keySet()) {

            int fromIndex = dataTree.length();
            int toIndex = key.indexOf('.', fromIndex);
            if (toIndex < 0) {
                continue;
            }
            String index = key.substring(fromIndex, toIndex);
            if (key.startsWith(dataTree)
                    && result.get(index) == null) {
                BrigdingGroup rule = new BrigdingGroup();
                rule.setIndex(index);
                result.put(index, rule);
            }

            BrigdingGroup item = result.get(index);
            String value = map.get(key);
            if (!StringUtils.isBlank(value)) {

                String token = key.substring((dataTree + index + ".").length());

                if (ModemDataTree.Layer2Bridging.Bridge.BridgeEnable.equals(token)) {
                    item.setBridgeEnable(value);
                } else if (ModemDataTree.Layer2Bridging.Bridge.BridgeKey.equals(token)) {
                    item.setBridgeKey(value);
                } else if (ModemDataTree.Layer2Bridging.Bridge.BridgeName.equals(token)) {
                    item.setBridgeName(value);
                } else if (ModemDataTree.Layer2Bridging.Bridge.BridgeStatus.equals(token)) {
                    item.setBridgeStatus(value);
                } else if (ModemDataTree.Layer2Bridging.Bridge.VlanId.equals(token)) {
                    item.setVlanId(value);
                }
            }
        }
        return result;
//        ArrayList<BrigdingGroup> listReturn = new ArrayList<BrigdingGroup>();
//        SortedSet<String> set = new TreeSet<String>(result.keySet());
//        for (String key : set) {
//            listReturn.add(result.get(key));
//        }
//        return listReturn;
    }

    public static HashMap<String, WirelessSecurity> parseWirelessSecurity(Map<String, String> map) {
        String dataTree = WirelessSecurity.DataTree.Root + ".";
        HashMap<String, WirelessSecurity> result = new HashMap<String, WirelessSecurity>();
        for (String key : map.keySet()) {

            int fromIndex = dataTree.length();
            int toIndex = key.indexOf('.', fromIndex);
            if (toIndex < 0) {
                continue;
            }
            String index = key.substring(fromIndex, toIndex);
            if (key.startsWith(dataTree)
                    && result.get(index) == null) {
                WirelessSecurity wlsec = new WirelessSecurity();
                wlsec.setInstance(index);
                result.put(index, wlsec);
            }

            WirelessSecurity item = result.get(index);
            String value = map.get(key);
            if (!StringUtils.isBlank(value)) {

                String token = key.substring((dataTree + index + ".").length());

                if (WirelessSecurity.DataTree.WlAuthMode.equals(token)) {
                    item.setWlAuthMode(value);
                } else if (WirelessSecurity.DataTree.WlNetReauth.equals(token)) {
                    item.setWlNetReauth(value);
                } else if (WirelessSecurity.DataTree.WlPreauth.equals(token)) {
                    item.setWlPreauth(value);
                } else if (WirelessSecurity.DataTree.WlRadiusKey.equals(token)) {
                    item.setWlRadiusKey(value);
                } else if (WirelessSecurity.DataTree.WlRadiusPort.equals(token)) {
                    item.setWlRadiusPort(value);
                } else if (WirelessSecurity.DataTree.WlRadiusServerIP.equals(token)) {
                    item.setWlRadiusServerIP(value);
                } else if (WirelessSecurity.DataTree.WlSsid.equals(token)) {
                    item.setWlSsid(value);
                } else if (WirelessSecurity.DataTree.WlWep.equals(token)) {
                    item.setWlWep(value);
                } else if (WirelessSecurity.DataTree.WlWpa.equals(token)) {
                    item.setWlWpa(value);
                } else if (WirelessSecurity.DataTree.WlWpaGTKRekey.equals(token)) {
                    item.setWlWpaGTKRekey(value);
                }
            }
        }
        return result;
    }

    public static HashMap<String, DeviceInterface> parseDeviceInterface(Map<String, String> map) {
        String dataTree = ModemDataTree.Layer2Bridging.AvailableInterface.Root + ".";
        HashMap<String, DeviceInterface> result = new HashMap<String, DeviceInterface>();
        // Get Available Interface
        for (String key : map.keySet()) {

            int fromIndex = dataTree.length();
            int toIndex = key.indexOf('.', fromIndex);
            if (toIndex < 0) {
                continue;
            }
            String index = key.substring(fromIndex, toIndex);
            if (key.startsWith(dataTree)
                    && result.get(index) == null) {
                DeviceInterface devif = new DeviceInterface();
                devif.setIfKey(index);
                result.put(index, devif);
            }

            DeviceInterface devif = result.get(index);
            String value = map.get(key);
            if (!StringUtils.isBlank(value)) {

                String token = key.substring((dataTree + index + ".").length());

                if (ModemDataTree.Layer2Bridging.AvailableInterface.AvailableInterfaceKey.equals(token)) {
                    devif.setIfKey(value);
                } else if (ModemDataTree.Layer2Bridging.AvailableInterface.InterfaceReference.equals(token)) {
                    devif.setIfReference(value);
                } else if (ModemDataTree.Layer2Bridging.AvailableInterface.InterfaceType.equals(token)) {
                    devif.setIfType(value);
                }
            }
        }

        // Update GroupInterface
        dataTree = ModemDataTree.Layer2Bridging.Filter.Root + ".";
        for (String key : map.keySet()) {

            int fromIndex = dataTree.length();
            int toIndex = key.indexOf('.', fromIndex);
            if (toIndex < 0) {
                continue;
            }
            String index = key.substring(fromIndex, toIndex);
            String token = key.substring((dataTree + index + ".").length());
            String iface = map.get(dataTree + index + "." + ModemDataTree.Layer2Bridging.Filter.FilterInterface);

            DeviceInterface devif = result.get(iface);
            String value = map.get(key);
            if (devif != null && !StringUtils.isBlank(value)) {

                if (ModemDataTree.Layer2Bridging.Filter.FilterBridgeReference.equals(token)) {
                    devif.setIfGroupKey(value);
                } else if (ModemDataTree.Layer2Bridging.Filter.FilterKey.equals(token)) {
                    devif.setFilterKey(value);
                }
            }
        }

        return result;
    }

    public static DeviceInfo parseDeviceInfo(Map<String, String> map, List<SimpleObject> dataModel) {

        if (map != null && !map.isEmpty() && dataModel != null && !dataModel.isEmpty()) {
            DeviceInfo devInfo = new DeviceInfo();
            for (SimpleObject model : dataModel) {
                String token = model.getName();
                String key = model.getFullParameterName();
                String value = map.get(key);
                if ("HardwareVersion".equalsIgnoreCase(token)) {
                    devInfo.setHardwareVersion(value);
                } else if ("SoftwareVersion".equalsIgnoreCase(token)) {
                    devInfo.setSoftwareVersion(value);
                } else if ("ProvisioningCode".equalsIgnoreCase(token)) {
                    devInfo.setProvisioningCode(value);
                } else if ("OID".equalsIgnoreCase(token)) {
                    devInfo.setOid(value);
                } else if ("Manufacturer".equalsIgnoreCase(token)) {
                    devInfo.setManufacturer(value);
                } else if ("SerialNumber".equalsIgnoreCase(token)) {
                    devInfo.setSerialNumber(value);
                } else if ("ModelName".equalsIgnoreCase(token)) {
                    devInfo.setModelName(value);
                } else if ("MAC".equalsIgnoreCase(token)) {
                    devInfo.setMac(value);
                } else if ("CPU".equalsIgnoreCase(token)) {
                    devInfo.setCpu(value);
                } else if ("RAM".equalsIgnoreCase(token)) {
                    devInfo.setRam(value);
                } else if ("ROM".equalsIgnoreCase(token)) {
                    devInfo.setRom(value);
                }
                
            }
            return devInfo;
        } else {
            return null;
        }
    }
}
