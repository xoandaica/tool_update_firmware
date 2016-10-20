package vn.vnpttech.ssdc.nms.mediation.stbacs.command;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

public class ConnectionCPE {
	public static void RequestConnectionHttp(String url, String user, String pass) throws Exception {
        HttpClient client = new HttpClient();
        if (user != null && pass != null) {
            client.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        }
        GetMethod get = new GetMethod(url);
        get.setDoAuthentication(true);

        try {
            int status = client.executeMethod(get);
            if (status != 200) {
                System.out.println(status + "\n" + get.getResponseBodyAsString());
                throw new Exception("Failed: status=" + status);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            get.releaseConnection();
        }
    }
    public static void requestCpeConnection(String cpeurl) {

        try {
            URL url = new URL(cpeurl);
            URLConnection httpconn = url.openConnection();
            httpconn.setReadTimeout(5000);
            httpconn.getContent();

        } catch (MalformedURLException ex) {
            //ex.printStackTrace();
            throw new RuntimeException(cpeurl + " is malformed.");
        } catch (UnknownServiceException e) {
            // ignore exceptions caused by missing content-type header.
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw new RuntimeException(cpeurl + " problem." + ex.getMessage() + " " + ex.getClass().getName());
        }
    }
	
	public static void initConnection(String cpeurl){

        try {
            URL url = new URL(cpeurl);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();          
            httpconn.connect();
            httpconn.getInputStream();            
        } catch (MalformedURLException ex) {
            //ex.printStackTrace();
            throw new RuntimeException(cpeurl + " is malformed.");
        } catch (UnknownServiceException e) {
            // ignore exceptions caused by missing content-type header.
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw new RuntimeException(cpeurl + " problem." + ex.getMessage() + " " + ex.getClass().getName());
        }
	}
}
