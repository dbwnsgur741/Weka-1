import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.bind.v2.schemagen.xmlschema.List;

public class ApiExplorer {
	
	AirplaneParkingArea airplaneData = new AirplaneParkingArea();
	
    ApiExplorer() throws IOException{
    	
    	String[] airplaneName = {"GMP", "PUS", "CJU","TAE","KWJ","RSU","USN","KUV","WJU","CJJ"};
    		
    	for(int i=0; i < airplaneName.length; i++) {
    		
    		StringBuilder urlBuilder = new StringBuilder("http://openapi.airport.co.kr/service/rest/AirportParkingCongestion/airportParkingCongestionRT");
    		urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=Tq%2FWrV66iXcaVKJEwcoFzeA6q7KVHNQwK6UyDbD37557YIyqBHyWXEOF0uFVcHrJHHMF6Sb8XDJ20cd2N6DVGg%3D%3D");
    		//urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=%2BDQd8pShYLPqi01nFPYY8nkzSSmY1ZdloIu2WEwO%2BnWnPWI1frIATtnp%2FB3hwvUJVvMimjhngiunQZRl7S%2BCyA%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); 
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); 
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); 
            urlBuilder.append("&" + URLEncoder.encode("schAirportCode","UTF-8") + "=" + URLEncoder.encode(airplaneName[i], "UTF-8")); 
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            
            rd.close();
            conn.disconnect();
            
            String xml = sb.toString();

            try {
            	
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentbuilder = factory.newDocumentBuilder(); 
                InputStream is = new ByteArrayInputStream(xml.getBytes());
                Document doc = documentbuilder.parse(is);
                Element element = doc.getDocumentElement();
     

                NodeList list = element.getElementsByTagName("item");
                for (int j = 0; j < list.getLength(); j++) {
                    Node nNode = list.item(j);
                    
                    if(nNode.getNodeType() == Node.ELEMENT_NODE) {
                    	
                    	Element eElemnt = (Element) nNode;
                    	System.out.println("##################");
                    	System.out.println(getTagValue("parkingOccupiedSpace",eElemnt));
                    	System.out.println(getTagValue("parkingTotalSpace",eElemnt));
                    	System.out.println(getTagValue("parkingCongestion",eElemnt));
                    	System.out.println(getTagValue("parkingCongestionDegree",eElemnt));
                    	
                    	String congestion = getTagValue("parkingCongestion",eElemnt);
                    	
                    	airplaneData.totalSpace.add(getTagValue("parkingOccupiedSpace",eElemnt));
                    	airplaneData.occupiedSpace.add(getTagValue("parkingTotalSpace",eElemnt));
                    	airplaneData.congestion.add(congestionToClass(congestion));
                    	airplaneData.degree.add(getTagValue("parkingCongestionDegree",eElemnt));
                    }
                }
     
            } catch (Exception e) {
                System.out.println("에러코드" + e.getMessage());
            }
            
    	}
    	
    	System.out.println("!!!!!!!!!");
    
    }
    
    public String congestionToClass(String con) {
    	
    	String _con = "";
    	
    	switch(con) {
    	case "만차":
    		_con = "full";
    		break;
    	case "혼잡":
    		_con = "congestion";
    		break;
    	case "원활":
    		_con = "smooth";
    		break;
    		
    	}
    	return _con;
    }
    
    public AirplaneParkingArea getAirPlaneData() {
    	
    	return this.airplaneData;
    }
    
    
 // tag값의 정보를 가져오는 메소드
	private static String getTagValue(String tag, Element eElement) {
	    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
	    Node nValue = (Node) nlList.item(0);
	    if(nValue == null) 
	        return null;
	    return nValue.getNodeValue();
	}
        
}
