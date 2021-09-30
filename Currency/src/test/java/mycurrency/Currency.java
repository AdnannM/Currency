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
        	 Set<String> keys = rates.keySet();
        	 
        	 HashMap<String, Double> rateMap = new HashMap<String, Double>();   
        	 
        	 for(String key : keys) {
        		 Double rate = rates.getDouble(key);
        		 rateMap.put(key, rate);
        		
        	 }
        	 
        	 System.out.println(sb.toString());
        	 
        	 
        	 for (Map.Entry<String, Double> entry : rateMap.entrySet()) {
        		    System.out.println(entry.getKey() + " / " + entry.getValue());
        		   
        		    ArrayList<String> arList = new ArrayList<String>();
        		    
        		    for(Map.Entry<String, Double> map : rateMap.entrySet()){

        		        String key = map.getKey();
        		    	Double values = map.getValue();
        		    	//System.out.println(key + " / " + values);
        		  
//        		    	arList.add(key);
//        		    	arList.add(values);
        		    
        		    }
        		    
        		    
        		    // DateTimeFormatter
        		    // DateTimeFormatter
                    DateTimeFormatter todayDate = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
                    LocalDateTime now = LocalDateTime.now();  
                    System.out.println("Today Date: " + todayDate.format(now)); 
        		    
                    // Create a new document
                    Document doc = new Document();
                    FileOutputStream stream = new FileOutputStream(new File("TablePDF.pdf"));
                    
                    PdfWriter.getInstance(doc, stream);
                    
                    doc.open();
                  
                
                    // Create Table
                    PdfPTable pdfPTable = new PdfPTable(3);
                    
                    
                    // TableCell
                    PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Today-Date "));
                    PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("Country "));
                    PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Rates " ));
            
                                     
                    // Add TableCell
                    pdfPTable.addCell(pdfPCell1);
                    pdfPTable.addCell(pdfPCell2);
                    pdfPTable.addCell(pdfPCell3);
                    pdfPTable.addCell(todayDate.format(now));
                    pdfPTable.addCell(entry.getKey());
                    pdfPTable.addCell("" + entry.getValue());
                   
                    
                    
                    
                
                    doc.add(pdfPTable);
                    doc.close();
        		
        	 }
 

    	} catch (IOException e) {

    	    // handle

    		}
    	}
    }

class CurrencyClass{
  private String rates;
  private String base;
  private Date date;
  
  public String getBase() {
	  return base;
  }
  
  public String getRates() {
	  return rates;
  }
  
  public Date getDate() {
	  return date;
  }
}


class CurrencyWrapper {
	private String country;
	private Double rates;
	
	public String getCountry() {
		return country;
	}
	
	public Double getRates() {
		return rates;
	}
}


