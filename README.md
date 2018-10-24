# BkCaseStudy
This project access data source via APIs online and output transactions list and aggregated transaction summary based on location (City). The solution exposes REST - GET protocol which when invoked interacts with two REST APIs (Json and Xml) to formulate the final output. The final output is text/csv and also generates csv files which are saved in a folder. It also interacts with geogle geo-location API to get city name using latitude and longitude values. To access google API a security key is generated in Geo-Location API platform. Since google geo-location API restricts number of request per hour, the program maintains a local copy of city name based on latitude and longitude in a json format. In case the geo-location API is unavailable or doesn't respond with desired results, city name is retrieved from this file. This local copy of cities is preloaded in memory to ensure efficiency is maintained.

#Implementation

The solution is coded using JAVA EE.
It uses Executor Service to expose an HttpServer where the GET API Endpoint is defined. Properties such as back log limit, multi-threading etc. are implemented here.
The service layer is used to orchestrate customer data and transaction data into an output lists based on user requirement.
The data source layer is used to access the three APIs namely; transactions, customer and geo-location API.
This application has leveraged Java 8 technology patterns and concepts such as Stream API and Lambda Expressions
There is a configuration file which maintains different properties such various API endpoints, generated file location etc.
It also generates log file which are stored in Logs folder.

#API Endpoints

The solution provides 2 url to achieve different operation.
Both APIs implements REST - GET method with no parameters required.
   1. http://localhost:9995/bkBCaseStudy/interview01/transactions
      This endpoint outputs the transactions list.
   2. http://localhost:9996/bkBCaseStudy/interview01/summary
      This endpoint outputs the aggregated summary.

#Requirements
1. JAVA 8 installed and JAVA HOME properly set to run the program.
2. Post man or Browser to test the solution

#How to run this solution
1. Git clone the application from Github by running git clone
2. Copy BkCaseStudy.jar and lib folder from Dist folder and copy to folder of your choice.
3. Copy folder Logs and files (cities.properties, config.properties, log4j.properties and start.sh) to same folder of your choice above.
4. Execute start.sh script in your Linux OS or type java -jar BkCaseStudy.jar& on your windows OS cmd to run the solution. Remember to execute this command while in the above folder of choice.

#Prerequisites
1. Working computer
2. Install Post man or browser software’s.
3. Java 8 installed.
