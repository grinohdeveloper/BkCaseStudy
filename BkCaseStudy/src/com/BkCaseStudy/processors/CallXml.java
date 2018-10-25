/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.processors;

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
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;

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
public class CallXml {
    // String thusURL = GetConfigs.getConfig("GetCustomer");

    String thusURL = "https://df-dev.bk.rw/interview01/customers";
    static Logger LOG = Logger.getLogger(CallJson.class);

    public Map<String, String> CallXml(String transactioncode) {
        Map<String, String> txn = new HashMap<>();
        int TimeoutSetting = 1000;
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
                LOG.info("TXN ID : " + transactioncode + " : CallXml | Invalid Response " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String outputString="";
            while ((httpResponse = br.readLine()) != null) {
                outputString = outputString + httpResponse;
            }
            conn.disconnect();
            LOG.info("TXN ID : " + transactioncode + " : CallXml | RESPONSE : " + outputString);
            if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                txn = readJson(txn, outputString, transactioncode);
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

    private Map<String, String> readJson(Map<String, String> txn, String rawResponse, String transactioncode) {
        try {
            Document doc = parseXmlFile(rawResponse);
            NodeList nList = doc.getElementsByTagName("customer");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    txn.put(eElement.getElementsByTagName("id").item(0).getTextContent(), eElement.getElementsByTagName("name").item(0).getTextContent());
                }
            }
        } catch (Exception ex) {
            LOG.error("TXN ID : " + transactioncode + " callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        }
        return txn;
    }

    public static void main(String[] args) {
        try {
            CallXml nn = new CallXml();
            Map<String, String> txn = nn.CallXml("hhshsh");
            System.out.println("e" + txn);
        } catch (Exception ex) {
            System.out.println("TXN ID :  callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Document parseXmlFile(String in) {
        try {
            in = SanitiseXml(in);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String SanitiseXml(String rawResponse) {
        if (rawResponse.contains("&gt;")) {
            rawResponse = rawResponse.replace("&gt;", ">");
        }
        if (rawResponse.contains("&lt;")) {
            rawResponse = rawResponse.replace("&lt;", "<");
        }
        if (rawResponse.contains("&#13;")) {
            rawResponse = rawResponse.replace("&#13;", "");
        }
        if (rawResponse.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
            rawResponse = rawResponse.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
        }
        if (rawResponse.contains("<?xml version=\"1.1\" encoding=\"UTF-8\"?>")) {
            rawResponse = rawResponse.replace("<?xml version=\"1.1\" encoding=\"UTF-8\"?>", "");
        }
        if (rawResponse.contains("<?xml version=\"2.0\" encoding=\"UTF-8\"?>")) {
            rawResponse = rawResponse.replace("<?xml version=\"2.0\" encoding=\"UTF-8\"?>", "");
        }
        if (rawResponse.contains("<?xml version=\"1.0\"?>")) {
            rawResponse = rawResponse.replace("<?xml version=\"1.0\"?>", "");
        }
        if (rawResponse.contains("<?xml version=\"1.1\"?>")) {
            rawResponse = rawResponse.replace("<?xml version=\"1.1\"?>", "");
        }
        if (rawResponse.contains("<?xml version=\"2.0\"?>")) {
            rawResponse = rawResponse.replace("<?xml version=\"2.0\"?>", "");
        }
        rawResponse = rawResponse.replaceAll("[^\\x20-\\x7e]", "");
        rawResponse = rawResponse.replaceAll("&", "");
        return rawResponse;
    }
}
