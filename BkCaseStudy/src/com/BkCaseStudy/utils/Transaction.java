/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.utils;

import java.io.Serializable;

/**
 *
 * @author THEDEV
 */
public class Transaction implements Serializable{
    private Integer customerId;
    private String timestamp;
    private Double amount;
    private Double latitude;
    private Double longitude;
    private String customerName;
    private String cityName;
    public Transaction(Integer customerId,String timestamp,Double amount,Double latitude,Double longitude,String customerName,String cityName) {
        this.customerId = customerId;
        this.timestamp = timestamp;
        this.amount = amount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerName = customerName;
        this.cityName =cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    
    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    
    
}
