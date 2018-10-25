/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.processors;

import com.BkCaseStudy.utils.GetConfigs;
import com.BkCaseStudy.utils.Transaction;
import com.BkCaseStudy.utils.TransactionSummary;
import java.util.Map;
import org.apache.log4j.Logger;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author THEDEV
 */
public class Processor {
    String FileLocation = GetConfigs.getConfig("TransactionFiles");
    static Logger LOG = Logger.getLogger(Processor.class);
    public String transactionscsv() {
        String transactioncode = UUID.randomUUID().toString();
        CallXml customers = new CallXml();
        Map<String, String> cust = customers.CallXml(transactioncode);
        if(cust == null || cust.isEmpty()){
            return null;
        }
        CallJson transactions = new CallJson();
        List<Transaction> txns = transactions.callJson(cust,transactioncode);
        if(txns == null || txns.isEmpty()){
            return null;
        }
        WriteFile wrtfile = new WriteFile();
        String csvdata=wrtfile.writeTxnCsv(transactioncode,txns);
        return csvdata;
    }
    public String summarysv() {
        List<TransactionSummary> summ = new ArrayList<>();
        String transactioncode = UUID.randomUUID().toString();
        CallXml customers = new CallXml();
        Map<String, String> cust = customers.CallXml(transactioncode);
        CallJson transactions = new CallJson();
        List<Transaction> txns = transactions.callJson(cust, transactioncode);
        Map<String, List<Transaction>> txnspercity = txns.stream().collect(Collectors.groupingBy(Transaction::getCityName));
        DecimalFormat formatter = new DecimalFormat(".##");
        formatter.setMaximumIntegerDigits(8);
        Double totalAmount = txns.stream().collect(Collectors.summingDouble(e -> e.getAmount()));
        String formattedAmnt = formatter.format(totalAmount);
        int unqCstt = txns.stream().collect(Collectors.groupingBy(e -> e.getCustomerId())).keySet().size();
        summ.add(new TransactionSummary(txnspercity.size(),Double.valueOf(formattedAmnt),unqCstt,txns.size()));
        WriteFile wrtfile = new WriteFile();
        String csvdata=wrtfile.writeSumCsv(transactioncode,summ);
        return csvdata;
    }
}
