/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.utils;

import com.BkCaseStudy.processors.CallJson;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author THEDEV
 */
public class GetConfigs {
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GetConfigs.class);
    public static String getConfig(String config) {
        String Desc = "GetConfigs.";
        FileInputStream fis=null;
        try {
            String configFilePath = "config.properties";
            Properties props = new Properties();
            fis = new FileInputStream(configFilePath);
            //  props.load(this.getClass().getResourceAsStream("BillerEngineConfigs.properties"));
            props.load(fis);
            Desc = props.getProperty(config);
            props.clear();
            fis.close();
        } catch (FileNotFoundException ex) {
            log.error("FileNotFoundException [getConfig - config.properties] : " + ex);
        } catch (IOException ex) {
            log.error("IOException [getConfig - config.properties] : " + ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ex) {
                log.error("IOException [getConfig - config.properties] : " + ex);
            }
        }
        return Desc;
    }
    public static String getCities(String config) {
        String Desc = "GetConfigs.";
        FileInputStream fis=null;
        try {
            String configFilePath = "cities.properties";
            Properties props = new Properties();
            fis = new FileInputStream(configFilePath);
            props.load(fis);
            Desc = props.getProperty(config);
            props.clear();
            fis.close();
        } catch (FileNotFoundException ex) {
            log.error("FileNotFoundException [getCities - cities.properties] : " + ex);
        } catch (IOException ex) {
            log.error("IOException [getCities - cities.properties] : " + ex);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception ex) {
                log.error("IOException [getCities - cities.properties] : " + ex);
            }
        }
        return Desc;
    }
}
