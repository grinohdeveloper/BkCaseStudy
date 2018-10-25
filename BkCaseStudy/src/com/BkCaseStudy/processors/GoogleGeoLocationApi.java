/* GoogleApi
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.processors;

import com.BkCaseStudy.utils.GetConfigs;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

class GoogleGeoLocationApi {

    static Logger LOG = Logger.getLogger(GoogleGeoLocationApi.class);
    String thusURL = GetConfigs.getConfig("GeoLocAPI");
    Map<String, String> Cities = new HashMap();
    //cities
    String cities = GetConfigs.getCities("GeoLoc");
    public GoogleGeoLocationApi(){
        Cities = (JSONObject)JSONValue.parse(cities);
    }
    public String getCityName(Double lat, Double lng, String transactioncode) {
        String inline = "";
        String CityName = "";
        try {
            LOG.info("TXN ID : " + transactioncode + " : GoogleGeoLocationApi | lat "+lat+ lat.toString() + " lng "+lng);
            LOG.info("TXN ID : " + transactioncode + " : GoogleGeoLocationApi | thusURL "+thusURL);
            if (thusURL.contains("hhhh")) {
                thusURL = thusURL.replaceAll("hhhh", lat.toString());
            }
            if (thusURL.contains("cccc")) {
                thusURL = thusURL.replaceAll("cccc", lng.toString());                
            }
            LOG.info("TXN ID : " + transactioncode + " : GoogleGeoLocationApi | thusURL "+thusURL);
            URL url = new URL(thusURL);
            //Parse URL into HttpURLConnection in order to open the connection in order to get the JSON data
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //Use the connect method to create the connection bridge
            conn.setConnectTimeout(1000);
            conn.connect();
            //Get the response status of the Rest API
            int responsecode = conn.getResponseCode();
            LOG.info("TXN ID : " + transactioncode + " : GoogleGeoLocationApi | Response code is: " + responsecode);
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                //read the JSON data from the stream
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                }
                LOG.info("TXN ID : " + transactioncode + " : GoogleGeoLocationApi | JSON Response " + inline);
                sc.close();
            }
            JSONParser parse = new JSONParser();
            JSONObject jobj = (JSONObject) parse.parse(inline);
            JSONArray jsonarr_1 = (JSONArray) jobj.get("results");
            for (int i = 0; i < jsonarr_1.size(); i++) {
                i = 0;
                JSONObject jsonobj_1 = (JSONObject) jsonarr_1.get(i);
                JSONArray jsonarr_2 = (JSONArray) jsonobj_1.get("address_components");
                /*(System.out.println("Elements under results array");
                System.out.println("Types: " + jsonobj_1.get("types"));*/
                //Get the index of the JSON objects 
                JSONObject jsonobj_2 = (JSONObject) jsonarr_2.get(0);
                String str_data1 = (String) jsonobj_2.get("long_name");
                System.out.println(str_data1);
                CityName = str_data1;
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //load data from previous saved list
        if (CityName == null || CityName.equalsIgnoreCase("")) {
            CityName = Cities.get(lat.toString()+lng.toString());
        }
        LOG.info("TXN ID : " + transactioncode + " : GoogleGeoLocationApi | CityName " + CityName);
        return CityName;
    }
    public static void main(String[] args) {
        try {
            
        } catch (Exception ex) {
            System.out.println("TXN ID :  callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
