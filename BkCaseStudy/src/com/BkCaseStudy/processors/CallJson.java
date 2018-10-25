/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.processors;

import com.BkCaseStudy.utils.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author THEDEV
 */
public class CallJson {
    //String thusURL = GetConfigs.getConfig("JSONURL");
     String thusURL ="https://df-dev.bk.rw/interview01/transactions";
    static Logger LOG = Logger.getLogger(CallJson.class);
    public List<Transaction> callJson(Map<String,String> Cusst,String transactioncode) {
        List<Transaction> txn = new ArrayList<>();
        int TimeoutSetting = 4000;
        String httpResponse = "";
        HttpURLConnection conn = null;
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            URL url = new URL(thusURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (conn.getResponseCode() != 200) {
                LOG.info("TXN ID : " + transactioncode + " : CallJSON | Invalid Response " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String outputString="";
            while ((httpResponse = br.readLine()) != null) {
                outputString = outputString + httpResponse;
            }
            conn.disconnect();
            LOG.info("TXN ID : " + transactioncode + " : CallJSON | RESPONSE : " + outputString);
           if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                txn = readJson(txn,outputString,Cusst,transactioncode);
           }
        } catch (ConnectException ex) {
            LOG.error("TXN ID : " + transactioncode + " callJson | IOException Error " + ex.getMessage());
            
        } catch (IOException ex) {            
            LOG.error("TXN ID : " + transactioncode + " callJson | IOException Error " + ex.getMessage());            
            ex.printStackTrace(); 
        } catch (Exception ex) {
            LOG.error("TXN ID : " + transactioncode + " callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return txn;
    }
    private List<Transaction> readJson(List<Transaction> txn, String rawResponse,Map<String,String> Cusst, String transactioncode) {
        try {
            if (!rawResponse.startsWith("[")) {
                rawResponse = "[" + rawResponse;
            }
            if (!rawResponse.endsWith("]")) {
                rawResponse = rawResponse + "]";
            }
            DecimalFormat formatter = new DecimalFormat(".##");
            formatter.setMaximumIntegerDigits(8);
            JSONArray itemsArray = new JSONArray(rawResponse);
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject e = itemsArray.getJSONObject(i);
                long myTimestamp = e.getLong("timestamp");
                String Timestamp = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(myTimestamp);
                GoogleGeoLocationApi loc = new GoogleGeoLocationApi();
                String CityName=loc.getCityName(e.getDouble("latitude"),e.getDouble("longitude"),transactioncode);
                String formattedAmnt = formatter.format(e.getDouble("amount"));
                txn.add(new Transaction(e.getInt("customerId"),Timestamp,Double.valueOf(formattedAmnt), e.getDouble("latitude"), e.getDouble("longitude"), Cusst.get(String.valueOf(e.getInt("customerId"))),CityName));
                LOG.error("TXN ID : " + transactioncode + " callJson | txn " + txn.toString());
            }
        } catch (Exception ex) {
            LOG.error("TXN ID : " + transactioncode + " callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        }
        return txn;
    }
    public static void main(String[] args) {
        try {
            Random rnd = new Random();
             int n = 100000 + rnd.nextInt(900000);
             System.out.println("TXN ID :  callJson | n "+n);
            Map<String,String> Cusst =  new HashMap();
            CallJson sds = new CallJson();
            List<Transaction> asasa = sds.callJson(Cusst,"sdsd");
            System.out.println("TXN ID :  callJson | s "+asasa);
           long myTimestamp = 1539767724559L;
                //String Timestamp="";
                String s = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").format(myTimestamp);
              System.out.println("TXN ID :  callJson | s "+s);
        } catch (Exception ex) {
            System.out.println("TXN ID :  callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
