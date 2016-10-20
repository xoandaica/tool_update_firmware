package vn.vnpttech.ssdc.nms.mediation.stbacs.tree;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang.StringUtils;

import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.PerformanceObject;
import vn.vnpttech.ssdc.nms.mediation.stbacs.myobject.SimpleObject;

/**
 * Parse datafile for TR069 module.
 *
 * @author Vunb
 */
public class DataFileUtils {

    private static String getPathFile(String shortPath) {
        String dir = System.getProperty("stbacs.datafile", "../datafile");
        return dir + File.separator + shortPath;
    }

    public static String getStringFromFile(String shortPath) throws IOException {
        String temp = "";

        DataInputStream in = new DataInputStream(new FileInputStream(getPathFile(shortPath)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;

        while ((strLine = br.readLine()) != null) {
            if (!strLine.equals("")) {
                temp = temp + strLine;
            }
        }

        return temp;
    }

    public static ArrayList<String> getLineFromFile(String shortPath) throws IOException {
        ArrayList<String> returnValue = new ArrayList<String>();

        DataInputStream in = new DataInputStream(new FileInputStream(getPathFile(shortPath)));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;

        while ((strLine = br.readLine()) != null) {
            if (!"".equals(strLine) && !strLine.startsWith("#")) {
                returnValue.add(strLine);
            }
        }

        return returnValue;
    }

    public static SimpleObject getDataModel(String line) {
        String[] elems = StringUtils.split(line, "-", 4);
        SimpleObject tmp = new SimpleObject();
        tmp.setId(Integer.parseInt(elems[0]));
        tmp.setName(elems[1]);
        tmp.setParameter(elems[2]);
        tmp.setType(elems[3]);
        return tmp;
    }

    public static ArrayList<String> get4ElementFromLine(String line) {
        ArrayList<String> returnValue = new ArrayList<String>();

        String bst = line;
        int start = 0, end = 0;
        for (int i = 0; i < bst.length(); i++) {
            if (bst.charAt(i) == '-' || i == (bst.length() - 1)) {
                end = i;
                if (i == (bst.length() - 1)) {
                    end++;
                }
                returnValue.add(bst.substring(start, end));
                start = end + 1;
            }
        }

        return returnValue;
    }

    public static ArrayList<String> getElementTreeNodeFromString(String data) {
        ArrayList<String> returnValue = new ArrayList<String>();
        String buf = data;

        int start = 0, end = 0;
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '.' || i == (buf.length() - 1)) {
                end = i;
                if (i == (buf.length() - 1)) {
                    end++;
                }
                returnValue.add(buf.substring(start, end));
                start = end + 1;
            }
        }
        return returnValue;
    }

    public static ArrayList<SimpleObject> getListSimpleObjectFromFile(String path) throws IOException {
        ArrayList<String> getLinesFile = getLineFromFile(path);
        
        return getListSimpleObjectFromArrayLine(getLinesFile);
    }

    public static ArrayList<SimpleObject> getListSimpleObjectFromArrayLine(ArrayList<String> data) {
        ArrayList<SimpleObject> returnValue = new ArrayList<SimpleObject>();
        for (String data1 : data) {
            SimpleObject tmp = getDataModel(data1);
            returnValue.add(tmp);
        }

        return returnValue;
    }

    public static ArrayList<SimpleObject> getListSimpleObjectFromList(ArrayList<String> data, int start, int end) {
        ArrayList<SimpleObject> returnValue = new ArrayList<SimpleObject>();
        for (int i = start; i < end; i++) {
            SimpleObject item = getDataModel(data.get(i));
            returnValue.add(item);
        }
        return returnValue;
    }

    public static String convertPerObjToXML(PerformanceObject obj) throws JAXBException {
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(PerformanceObject.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Write to System.out
//	    m.marshal(bookstore, System.out);
        m.marshal(obj, writer);

        return writer.toString();
    }

    public static PerformanceObject convertXMLToPerObj(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(PerformanceObject.class);
        Unmarshaller um = context.createUnmarshaller();
        PerformanceObject obj = (PerformanceObject) um.unmarshal(new StringReader(xml));

        return obj;
    }

    public static String getIPfromConnectionRequest(String connectionRequest) throws MalformedURLException {
        String hostName = new URL(connectionRequest).getHost();
        return hostName;
    }
}
