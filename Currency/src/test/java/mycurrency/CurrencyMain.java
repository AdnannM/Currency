package mycurrency;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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

public class CurrencyMain {

	// URL
	private static final String URL = "http://data.fixer.io/api/latest?access_key=a9768800ad38538c63d068cf5f06e769";
	private static final Logger LOG = LoggerFactory.getLogger(CurrencyMain.class);
	// DateTimeFormatter
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd. MM yyyy");
	private static DateTimeFormatter shorDateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	private static String title = "Kursna lista za: ";
	private static String country = "Country ";
	private static String rates = "Rates ";
	private static LocalDateTime now = LocalDateTime.now();
	// Create a new document
	private static Document doc = new Document();

	public static void main(String[] args) {

		try {

			// use httpClient (no need to close it explicitly)
			String apiResponse = getCurrencyRateAPIResponse();
			JSONObject jsonAPIResponseJsonObject = new JSONObject(apiResponse);
			JSONObject rateJsonObject = getJSONObjectForPropertyName(jsonAPIResponseJsonObject, "rates");
			Set<String> rateJsonObjectKeySet = rateJsonObject.keySet();
			HashMap<String, Double> reateMap = createAndReturnRateMapForRateKeyValues(rateJsonObject,
					rateJsonObjectKeySet);

			createNewPdfFileWithTable(reateMap);
			

			
		} catch (Exception e) {
		}
	}

	private static String getCurrencyRateAPIResponse() throws ClientProtocolException, IOException {

		StringBuilder sb = new StringBuilder();
		LOG.info("use httpClient for url > " + URL);
		try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

			// use httpClient (no need to close it explicitly)
			HttpGet request = new HttpGet(URL);
			HttpResponse response = client.execute(request);

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
		}

		return sb.toString();
	}

	private static JSONObject getJSONObjectForPropertyName(JSONObject jsonAPIResponseObject, String propertyName) {

		JSONObject jsonResultObject = null;

		jsonResultObject = jsonAPIResponseObject.getJSONObject(propertyName);
		return jsonResultObject;
	}

	/***
	 * This method creates map rates for countries base on provided rate key
	 * 
	 * @param rateJsonObject
	 * @param rateKeyValues
	 * @return map of rates and its values
	 */
	private static HashMap<String, Double> createAndReturnRateMapForRateKeyValues(JSONObject rateJsonObject,
			Set<String> rateKeyValues) {

		HashMap<String, Double> rateMap = new HashMap<String, Double>();

		for (String key : rateKeyValues) {
			Double rate = rateJsonObject.getDouble(key);
			rateMap.put(key, rate);

		}

		return rateMap;
	}

	private static void createNewPdfFileWithTable(HashMap<String, Double> rateMap) throws FileNotFoundException, DocumentException {
		
		try {

			FileOutputStream stream = new FileOutputStream(new File("TablePDF.pdf"));
			PdfWriter.getInstance(doc, stream);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		doc.open();

		// Create Table
		PdfPTable pdfPTable = new PdfPTable(2);
		
		takeAllItemFromJSON(rateMap, pdfPTable);
		
		configureCell(pdfPTable);
		
		
		
	}
	
	private static void takeAllItemFromJSON(HashMap<String, Double> reateMap, PdfPTable pdfPTable) {
		
		for (Map.Entry<String, Double> entry : reateMap.entrySet()) {

			String key = entry.getKey();
			Double values = entry.getValue();

			MDC.put(key, values);
			LOG.info("Key: " + key + " || " + "Value: " + values);

			pdfPTable.addCell("" + key);
			pdfPTable.addCell("" + values + " ");

		}
	}
	
	private static void configureCell(PdfPTable pdfPTable) throws DocumentException {
				// TableCell
				PdfPTable timeTable = new PdfPTable(1);
				PdfPTable headerTable = new PdfPTable(2);
				
				PdfPCell titleCell = new PdfPCell(new Paragraph("" + title));
				PdfPCell timeCell = new PdfPCell(new Paragraph(shorDateTimeFormatter.format(now)));
				PdfPCell dateCell = new PdfPCell(new Paragraph(dateTimeFormatter.format(now)));
				PdfPCell countryCell = new PdfPCell(new Paragraph(country));
				PdfPCell ratesCell = new PdfPCell(new Paragraph(rates));
				

				timeTable.addCell(timeCell);
				headerTable.addCell(titleCell);
				headerTable.addCell(dateCell);
				pdfPTable.addCell(countryCell);
				pdfPTable.addCell(ratesCell);
			

				doc.add(timeTable);
				doc.add(headerTable);
				doc.add(pdfPTable);
				doc.close();
	}
}
