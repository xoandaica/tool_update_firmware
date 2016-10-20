/*
 * 
 * Copyright 2007-2012 Audrius Valunas
 * 
 * This file is part of OpenACS.

 * OpenACS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenACS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OpenACS.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package vn.vnpttech.ssdc.nms.mediation.stbacs.message;

import java.util.List;
import java.util.Map;

import vn.vnpttech.ssdc.nms.mediation.stbacs.Message;

import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

public class GetParameterValues extends Message {

    /**
     * Creates a new instance of GetParameterValues
     */
    public GetParameterValues() {
        name = "GetParameterValues";
    }

    public GetParameterValues(String[] parameterNames) {
        name = "GetParameterValues";
        initParameterNames(parameterNames);
    }

    public GetParameterValues(Map<String, String> parameters) {
        name = "GetParameterValues";
        String[] paramNames = new String[parameters.size()];
        parameters.keySet().toArray(paramNames);
        initParameterNames(paramNames);
    }

    public GetParameterValues(List<String> parameters) {
        name = "GetParameterValues";
        String[] paramNames = new String[parameters.size()];
        parameters.toArray(paramNames);
        initParameterNames(paramNames);
    }

    public GetParameterValues(String paramName) {
        name = "GetParameterValues";
        String[] paramNames = new String[1];
        paramNames[0] = paramName;
        initParameterNames(paramNames);
    }

    private void initParameterNames(String[] parameterNames) {
        sb.setLength(0);
        sb.append(name).append(": PARAMETER_NAME=");

        if (parameterNames != null) {
            this.parameterNames = parameterNames;
            for (String paramName : parameterNames) {
                sb.append("[")
                        .append(paramName)
                        .append("]");
            }

        } else {
            sb.append("[null]");
        }
        logger.info(sb);
    }

    protected void createBody(SOAPBodyElement body, SOAPFactory spf) throws SOAPException {
        SOAPElement elm = body.addChildElement(spf.createName("ParameterNames"));
        elm.setAttribute(SOAP_ARRAY_TYPE, "xsd:string[" + String.valueOf(parameterNames.length) + "]");
        for (String parameterName : parameterNames) {
            SOAPElement s = elm.addChildElement("string");
            s.setValue(parameterName);
//            s.setAttribute("xsi:type","xsd:string");
        }
    }

    protected void parseBody(SOAPBodyElement body, SOAPFactory f) throws SOAPException {
    }
    public String[] parameterNames;
}
