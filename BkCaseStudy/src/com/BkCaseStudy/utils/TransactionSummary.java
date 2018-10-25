/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.utils;

/**
 *
 * @author THEDEV
 */
public class TransactionSummary {
   private Integer cityName; 
   private Double totalAmount;
   private Integer uniqCusst;
   private Integer totalTxns;
    public TransactionSummary(Integer cityName, double totalAmount, Integer uniqCusst, Integer totalTxns) {
        this.totalAmount = totalAmount;
        this.uniqCusst = uniqCusst;
        this.totalTxns = totalTxns;
        this.cityName = cityName;
    }
    public Integer getCityName() {
        return cityName;
    }

    public void setCityName(Integer cityName) {
        this.cityName = cityName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getUniqCusst() {
        return uniqCusst;
    }

    public void setUniqCusst(Integer uniqCusst) {
        this.uniqCusst = uniqCusst;
    }

    public Integer getTotalTxns() {
        return totalTxns;
    }

    public void setTotalTxns(Integer totalTxns) {
        this.totalTxns = totalTxns;
    }
   
}
