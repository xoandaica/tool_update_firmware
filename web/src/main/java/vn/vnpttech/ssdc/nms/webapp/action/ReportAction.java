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
package vn.vnpttech.ssdc.nms.webapp.action;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.definition.ReportParameters;

/**
 *
 * @author Dell
 */
public class ReportAction extends BaseAction {

    public ReportAction() {

    }
    static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private InputStream fileInputStream;

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(InputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    //Render value STATUS for policy report
    private class PolicyReport_StatusValueExpression extends AbstractValueFormatter<String, String> {

        @Override
        public String format(String value, ReportParameters reportParameters) {
            Object parameterValue = reportParameters.getValue("status").toString().equals("0") ? "Thất bại" : "Thành công";
            return parameterValue.toString();
        }
    }

    //Render value START_TIME for policy report
    private class PolicyReport_StartTimeValueExpression extends AbstractValueFormatter<String, String> {

        @Override
        public String format(String value, ReportParameters reportParameters) {
            String parameterValue = reportParameters.getValue("start_time").toString();
            String[] parameterValues = parameterValue.split(" ");
            return parameterValues[0] + "\n" + parameterValues[1].substring(0, parameterValues[1].length() - 2);
        }
    }
    //Render value END_TIME for policy report
    private class PolicyReport_EndTimeValueExpression extends AbstractValueFormatter<String, String> {

        @Override
        public String format(String value, ReportParameters reportParameters) {
            String parameterValue = reportParameters.getValue("end_time").toString();
            String[] parameterValues = parameterValue.split(" ");
            return parameterValues[0] + "\n" + parameterValues[1].substring(0, parameterValues[1].length() - 2);
        }
    }

    //Create report
    @Override
    public String execute() throws SQLException {
        Connection connection = null;
        try {
        
            return SUCCESS;
        } catch (Exception ex) {
            log.error("ERROR getCreatePolicyReport: ", ex);
            return ERROR;
        } finally {
            try {
                connection.close();
            } catch (Exception closeSQLConnectionEx) {
                log.error("ERROR getCreatePolicyReport - close connection problem ", closeSQLConnectionEx);
            }
        }
    }//End

    public void getCreatePolicyReport_Test() {
        try {
           

        } catch (Exception ex) {
            log.error("ERROR getCreatePolicyReport: ", ex);
        }
    }//End

    public static void main(String[] args) {
        new ReportAction().getCreatePolicyReport_Test();
        //new ReportAction().getDownloadPolicyReport_Test();
    }
}
