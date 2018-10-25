/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.processors;

import static com.BkCaseStudy.processors.Processor.LOG;
import com.BkCaseStudy.utils.GetConfigs;
import com.BkCaseStudy.utils.Transaction;
import com.BkCaseStudy.utils.TransactionSummary;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author THEDEV
 */
public class WriteFile {
 private static final char DEFAULT_SEPARATOR = ','; 
    private static final String FileLocation = GetConfigs.getConfig("TransactionFiles");
    private static final String SummayFiles = GetConfigs.getConfig("SummayFiles");
    private static final String CSV_SEPARATOR = ",";
    public String writeSumCsv(String transactioncode,List<TransactionSummary> txns){
        String ScvTxt="Oooops !!! Something went wrong";
        try {
            StringBuffer oneLine = new StringBuffer();
            String filename=GenerateCode("SUM-","");
            String fiile =SummayFiles+filename+".csv";
            File file = new File(fiile);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writeLine(writer, Arrays.asList("City_Name", "Total_Amount", "Unique_Customers", "Total_Transactions"));
            oneLine.append("City_Name,Total_Amount,Unique_Customers,Total_Transactions");
            oneLine.append("\n");
            for (TransactionSummary tx : txns) {
                oneLine.append(tx.getCityName().toString());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(String.valueOf(tx.getTotalAmount()));
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(tx.getUniqCusst().toString());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(tx.getTotalTxns().toString());
                oneLine.append("\n");
                List<String> list = new ArrayList<>();
                //list.add(GenerateCode("TX"));
                list.add(tx.getCityName().toString());
                list.add(String.valueOf(tx.getTotalAmount()));
                list.add(tx.getUniqCusst().toString());
                list.add(tx.getTotalTxns().toString());
                writeLine(writer, list);
            }
             ScvTxt= oneLine.toString();
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            LOG.error("TXN ID : " + transactioncode + " callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        }
        return ScvTxt;
    }
    public String writeTxnCsv(String transactioncode,List<Transaction> txns ){
        String ScvTxt="Oooops !!! Something went wrong";
        try {
            StringBuffer oneLine = new StringBuffer();
            String filename=GenerateCode("TXN-","");
            String fiile =FileLocation+filename+".csv";
            File file = new File(fiile);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writeLine(writer, Arrays.asList("Transaction_Id", "DateTime", "Customer_Id", "Customer_Name", "Amount", "City_Name"));
            oneLine.append("Transaction_Id,DateTime,Customer_Id,Customer_Name,Amount,City_Name");
            oneLine.append("\n");
            for (Transaction tx : txns) {
                oneLine.append(GenerateCode("TXN","FT"));
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(tx.getTimestamp());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(tx.getCustomerId().toString());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(tx.getCustomerName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(String.valueOf(tx.getAmount()));
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(tx.getCityName());
                oneLine.append("\n");
                List<String> list = new ArrayList<>();
                list.add(GenerateCode("TXN","FT"));
                list.add(tx.getTimestamp());
                list.add(tx.getCustomerId().toString());
                list.add(tx.getCustomerName());
                list.add(String.valueOf(tx.getAmount()));
                list.add(tx.getCityName());
                writeLine(writer, list);
            }
            writer.flush();
            writer.close();
            ScvTxt= oneLine.toString();
        } catch (Exception ex) {
            LOG.error("TXN ID : " + transactioncode + " callJson | Exception Error " + ex.getMessage());
            ex.printStackTrace();
        }
        return ScvTxt;
    }
    public static String GenerateCode(String idd,String end) {
        Random rnd = new Random();
        return idd + (100000 + rnd.nextInt(900000)+end);
    }
    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }
    public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }
    private static String followCVSformat(String value) {
        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;
    }
    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {
        boolean first = true;
        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }
        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }
            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }
}

