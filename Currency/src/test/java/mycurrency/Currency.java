package mycurrency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.MDC;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Currency {
	
	// URL
	private static String url = "http://data.fixer.io/api/latest?access_key=a9768800ad38538c63d068cf5f06e769";
	private static Logger logger =  LoggerFactory.getLogger(Currency.class);
			
    public static void main(String[] args) throws UnsupportedOperationException, IOException, DocumentException {
          	         
    	logger.info("Hello World");
    	logger.warn("test");
    	logger.error("error");
    	logger.debug("Here is a debug message");
    	
    	try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

    	    // use httpClient (no need to close it explicitly)
        	HttpGet request = new HttpGet(url);
        	HttpResponse response = client.execute(request);

        	// Get the response
        	BufferedReader rd = new BufferedReader
        	    (new InputStreamReader(
        	    response.getEntity().getContent()));

        	String line = "";
        	StringBuilder sb = new StringBuilder();
        	while ((line = rd.readLine()) != null) {
        	    sb.append(line);
        	}

        	 JSONObject objtest = new JSONObject(sb.toString());
        
        	 JSONObject rates = objtest.getJSONObject("rates");
        	 
        	 String base = (String) objtest.getString("base");
        	 
        	 Set<String> keys = rates.keySet();
        	 
        	 HashMap<String, Double> rateMap = new HashMap<String, Double>();   
        	 
        	 for(String key : keys) {
        		 Double rate = rates.getDouble(key);
        		 rateMap.put(key, rate);
        		
        	 }
        	 
 		    // DateTimeFormatter
             DateTimeFormatter todayDate = DateTimeFormatter.ofPattern("dd. MM yyyy"); 
             LocalDateTime now = LocalDateTime.now();  
             
             logger.info("" + todayDate.format(now) + " | " + todayDate.format(now));
 		    
             // Create a new document
             Document doc = new Document();
             FileOutputStream stream = new FileOutputStream(new File("TablePDF.pdf"));
             
             PdfWriter.getInstance(doc, stream);
             
             String title = "Kursna lista za: ";
             DateTimeFormatter timeDate = DateTimeFormatter.ofPattern("HH:mm:ss");
            
             doc.open();
    
         
             // Create Table
             PdfPTable pdfPTable = new PdfPTable(2);
             PdfPTable header = new PdfPTable(2);
             PdfPTable tableTime = new PdfPTable(1);
             
             // TableCell
             PdfPCell timeCell = new PdfPCell(new Paragraph(timeDate.format(now)));
             PdfPCell headerCell1 = new PdfPCell(new Paragraph(title));
             PdfPCell headerCell2 = new PdfPCell(new Paragraph(todayDate.format(now)));
             PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Country "));
             PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Rates " ));
             
	    
               tableTime.addCell(timeCell);
               header.addCell(headerCell1);
               header.addCell(headerCell2);
	           pdfPTable.addCell(pdfPCell3);
	           pdfPTable.addCell(pdfPCell4);
	           
        	 
        	 for (Map.Entry<String, Double> entry : rateMap.entrySet()) {
     
        		    String key = entry.getKey();
        		    Double values = entry.getValue();
        		    
        		    MDC.put(key, values);
        		    logger.info("Key: " + key + " || " + "Value: " + values);

                    pdfPTable.addCell("" + key);
                    pdfPTable.addCell("" + values + " " + base);
 
        	 }
        	 
        	 	doc.add(tableTime);
        	 	doc.add(header);
        	    doc.add(pdfPTable);
                doc.close();


    	} catch (IOException e) {

   		}
    }
}



