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
package vn.vnpttech.ssdc.nms.fileprocess;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import vn.vnpttech.ssdc.nms.model.Firmware;
import vn.vnpttech.ssdc.nms.webapp.util.Constant;
import vn.vnpttech.ssdc.nms.webapp.util.ResourceBundleUtils;

/**
 *
 * @author longdq
 */
public class ReadFile {

    public static List<Firmware> parseXml() {
        List<Firmware> fs = new ArrayList<Firmware>();
        try {

            String filePath = ResourceBundleUtils.getSaveDir() + File.separator + Constant.FILE_NAME;
           // System.out.println(filePath + "--------------------");
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("Version");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("Release Date : " + eElement.getAttribute("Updated"));
                    System.out.println("Model : " + eElement.getElementsByTagName("model").item(0).getTextContent());
                    System.out.println("Rom Version : " + eElement.getElementsByTagName("romVersion").item(0).getTextContent());
                    System.out.println("Release Note : " + eElement.getElementsByTagName("releaseNote").item(0).getTextContent());
                    System.out.println("Firmware Path : " + eElement.getElementsByTagName("firmwarePath").item(0).getTextContent());

                    Firmware f = new Firmware();
                    f.setVersion(eElement.getElementsByTagName("romVersion").item(0).getTextContent());
                    f.setReleaseNote(eElement.getElementsByTagName("releaseNote").item(0).getTextContent());
                    f.setFirmwarePath(eElement.getElementsByTagName("firmwarePath").item(0).getTextContent());
                    f.setModelName(eElement.getElementsByTagName("model").item(0).getTextContent());
                    String rd = eElement.getAttribute("Updated");
                    SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
                    Date parsedDate = dateFormat.parse(rd);
                    Timestamp timestamp = new Timestamp(parsedDate.getTime());
                    if (StringUtils.isNotBlank(rd)) {
                        f.setReleaseDate(timestamp);
                    }
                    fs.add(f);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return fs;
        }

    }

    public static void main(String argv[]) {
        List<Firmware> fs = parseXml();
        if (fs != null && fs.size() > 0) {
            System.out.println("Thank god, It's here !!! ");
        }
    }
}
