/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vnpt.tr069.handler;

import java.io.ByteArrayOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.vnpttech.collection.openacs.Message;

/**
 *
 * @author Zan
 */
public interface Tr069Handler {
    
    public String getHandlerName();

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception;

    public void processInform(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processEvent0Bootstrap(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processEvent1Boot(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processEvent2Periodic(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processEvent3ScheduleInform(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;
    
    public void processEvent4ValueChange(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processEvent6ConnectionRequest(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processEvent7TranferComplete(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processEvent8DiagnosticComplete(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processAddObjectResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processDelObjectResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processDownloadResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processFactoryResetResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processFault(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processGetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processRebootResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processSetParameterValuesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processTranferComplete(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processUploadResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processGetRPCMethods(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

    public void processGetParameterNamesResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;
    
    public void processScheduleInformResponse(HttpServletRequest request, HttpServletResponse response, Message msg, ByteArrayOutputStream out) throws Exception;

}
