package mycurrency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.Base64.OutputStream;




public class Currency {
	
	// URL
	private static String url = "http://data.fixer.io/api/latest?access_key=a9768800ad38538c63d068cf5f06e769";
	
    public static void main(String[] args) throws UnsupportedOperationException, IOException, DocumentException {
          	         
    	
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
        	 
        	 System.out.println(sb.toString());
        	 
        	 
 		    // DateTimeFormatter
             DateTimeFormatter todayDate = DateTimeFormatter.ofPattern("dd. MM yyyy"); 
             LocalDateTime now = LocalDateTime.now();  
             System.out.println("Today Date: " + todayDate.format(now)); 
 		    
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
        		    System.out.println(entry.getKey() + " / " + entry.getValue());
     
        		    String key = entry.getKey();
        		    Double values = entry.getValue();

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
//class CurrencyClass{
//  private String rates;
//  private String base;
//  private Date date;
//  
//  public String getBase() {
//	  return base;
//  }
//  
//  public String getRates() {
//	  return rates;
//  }
//  
//  public Date getDate() {
//	  return date;
//  }
//}
//
//
//class CurrencyWrapper {
//	private String country;
//	private Double rates;
//	
//	public String getCountry() {
//		return country;
//	}
//	
//	public Double getRates() {
//		return rates;
//	}
//}


