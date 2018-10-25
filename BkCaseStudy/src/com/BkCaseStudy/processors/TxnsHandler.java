/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.BkCaseStudy.processors;

import com.BkCaseStudy.utils.Transaction;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author THEDEV
 */
public class TxnsHandler implements HttpHandler {

    static Logger log = Logger.getLogger(TxnsHandler.class);
    @Override
    public void handle(HttpExchange he) throws IOException {
        try {
            InputStream is = he.getRequestBody();
            String line, request;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
                request = sb.toString();
            }
            Processor poc = new Processor();
            String response = poc.transactionscsv();
            log.info("Outgoing  Response: " + response);
            if (response == null) {
                String errorResponse = "Oooops !!! Something went wrong";
                he.sendResponseHeaders(200, errorResponse.length());
                OutputStream os = he.getResponseBody();
                os.write(errorResponse.getBytes());
                os.close();
            } else {
                he.sendResponseHeaders(200, response.length());// response.size());
                OutputStream os = he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } catch (Exception e) {
            log.error("TxnsHandler exception");
            e.printStackTrace();
        }
    }
}
