package com.example.demo.provider;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class Connection {

	public static void consumeLogin() {
		try {

			System.out.println("Entro aquí");

			/* SSL */

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

			/* SSL */

		

			// Code to make a webservice HTTP request
			// Se realiza la construcción de la solicitud HTTP

			String responseString = "";
			String outputString = "";

			String wsEndPoint = "https://testsrv.credibanco.com/CheckOutWSWeb/CheckoutWSService";
			URL url = new URL(wsEndPoint);
			URLConnection connection = url.openConnection();

			HttpURLConnection httpConection = (HttpURLConnection) connection;
			ByteArrayOutputStream byteArrayOutputStram = new ByteArrayOutputStream();
			// Se arma el XML SOAP y los parametros
			// XML SOAP and parameters are built
			String xmlInput = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.checkout.credibanco.pharos.com/\">\n" + 
					"   <soapenv:Header/>\n" + 
					"   <soapenv:Body>\n" + 
					"      <ws:authenticateUser>\n" + 
					"         <!--Optional:-->\n" + 
					"         <email>pruebas.pago@credibanco.com</email>\n" + 
					"         <!--Optional:-->\n" + 
					"         <password>Credibanco16/</password>\n" + 
					"      </ws:authenticateUser>\n" + 
					"   </soapenv:Body>\n" + 
					"</soapenv:Envelope>";
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			byteArrayOutputStram.write(buffer);
			byte[] b = byteArrayOutputStram.toByteArray();
			String SOAPAction = "authenticateUser";

			httpConection.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConection.setRequestProperty("SOAPAction", SOAPAction);
			httpConection.setRequestMethod("POST");
			httpConection.setDoOutput(true);
			httpConection.setDoInput(true);

			OutputStream out = httpConection.getOutputStream();
			// Write the content of the request to the outputstream of the HTTP
			// Escriba el contenido de la solicitud en el flujo de salida de HTTP
			// Connection - Conexión.

			out.write(b);
			out.close();

			// Read the response.
			// Lee la respuesta.

			InputStreamReader isr = new InputStreamReader(httpConection.getInputStream(), Charset.forName("UTF-8"));
			BufferedReader in = new BufferedReader(isr);
			// Write the SOAP message response to a String.
			// Escriba la respuesta del mensaje SOAP en un String

			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
			// Print to console
			// Imprime en la consola
			String formattedSOAPResponse = formatXML(outputString);
			System.out.println(formattedSOAPResponse);

		} catch (Exception ex) {
			System.out.println("Excepcion ... " + ex.toString());
		}
	}
	
	
	public void getUser() {
		try {
			
		}catch(Exception ex) {
			
		}		
	}
	
	public void activateSession() {
		try {
			
		}catch(Exception ex) {
			
		}
	}

	// format the XML in String
	// Formato de XML a String
	private static String formatXML(String unformattedXml) {
		try {
			Document document = parseXmlFile(unformattedXml);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 3);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			transformer.transform(source, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	// parse XML
	private static Document parseXmlFile(String in) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(in));
			return db.parse(is);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new RuntimeException(e);
		}
	}

}
