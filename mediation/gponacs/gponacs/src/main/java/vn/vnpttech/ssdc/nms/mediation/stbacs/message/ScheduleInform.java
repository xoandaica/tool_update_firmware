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

import vn.vnpttech.ssdc.nms.mediation.stbacs.Message;

import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

public class ScheduleInform extends Message {

    public ScheduleInform() {
        name = "ScheduleInform";
    }

    public ScheduleInform(int DelaySeconds, String CommandKey) {
        this();
        this.CommandKey = CommandKey;
        this.DelaySeconds = DelaySeconds;
    }

    protected void createBody(SOAPBodyElement body, SOAPFactory spf) throws SOAPException {
        body.addChildElement(COMMAND_KEY).setValue(CommandKey);
        body.addChildElement("DelaySeconds").setValue(((Integer) DelaySeconds).toString());
    }

    protected void parseBody(SOAPBodyElement body, SOAPFactory f) throws SOAPException {
    }
    public String CommandKey;
    public int DelaySeconds;
}