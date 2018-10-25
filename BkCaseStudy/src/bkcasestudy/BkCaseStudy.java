/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bkcasestudy;

import com.BkCaseStudy.processors.Processor;
import com.BkCaseStudy.processors.SummaryHandler;
import com.BkCaseStudy.processors.TxnsHandler;
import com.BkCaseStudy.utils.GetConfigs;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author THEDEV
 */
public class BkCaseStudy {
    private static String TransactionEndPoint = GetConfigs.getConfig("TransactionEndPoint");
    private static String TransactionPort = GetConfigs.getConfig("TransactionPort");
    private static String TransactionThread = GetConfigs.getConfig("TransactionThreads");
    private static String SummaryEndPoint = GetConfigs.getConfig("SummaryEndPoint");
    private static String SummaryPort = GetConfigs.getConfig("SummaryPort");
    private static String EndPointBackLog = GetConfigs.getConfig("EndPointBackLog");
    static Logger LOG = Logger.getLogger(BkCaseStudy.class);
    public static void main(String[] args) {
        // TODO code application logic here
        try {   
                PropertyConfigurator.configure("log4j.properties");
                LOG.error("Starting Rest Server: .... ..... ....");
                HttpServer server = HttpServer.create(new InetSocketAddress(Integer.parseInt(TransactionPort)),Integer.parseInt(EndPointBackLog));
                server.createContext(TransactionEndPoint, new TxnsHandler());
                ExecutorService eng = Executors.newFixedThreadPool(Integer.parseInt(TransactionThread));
                server.setExecutor(eng);
                LOG.error("EndPoint : "+TransactionPort+""+TransactionEndPoint);
                server.start();
                
                HttpServer serverr = HttpServer.create(new InetSocketAddress(Integer.parseInt(SummaryPort)),Integer.parseInt(EndPointBackLog));
                serverr.createContext(SummaryEndPoint, new SummaryHandler());
                ExecutorService engg = Executors.newFixedThreadPool(Integer.parseInt(TransactionThread));
                serverr.setExecutor(engg);
                LOG.error("EndPoint : "+SummaryPort+""+SummaryEndPoint);
                serverr.start();
            } catch (Exception e) {
                LOG.error("Transaction handler exception: " + e.toString());
                e.printStackTrace();
            }
    }
}
