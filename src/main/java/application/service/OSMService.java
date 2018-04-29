package application.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import application.entity.Address;
import application.helper.OSMPoint;
import application.helper.OpenStreetMapPlace;

public class OSMService {
	
	RestTemplate restTemplate = new RestTemplate();
	String osmRestUrl = "http://nominatim.openstreetmap.org/search?";
	
	public OSMPoint getCoordFromAddress(Address address,String countryCode){
		
		OSMPoint osmPoint = new OSMPoint();
		
		String country = "country=" +countryCode;
		String city="&city=" + address.getCity();
		String street = "&street=" + address.getHouseNumber() + " " +address.getStreet();
		String postalCode = "&postalcode=" + address.getPostalCode();
		String format = "&format=json";
		String query = osmRestUrl+country +city + postalCode + street + format;
		ResponseEntity<List<OpenStreetMapPlace>> rateResponse =
		        restTemplate.exchange(query,
		            HttpMethod.GET, null, new ParameterizedTypeReference<List<OpenStreetMapPlace>>(){});
		List<OpenStreetMapPlace> placeList = rateResponse.getBody();
		
		// mehrere treffer vergleiche street und postalcode
		if (placeList.isEmpty()){
			osmPoint.setPointFound(false);
			return osmPoint;
		}
		String lon = placeList.get(0).getLon();
		String lat = placeList.get(0).getLat();
		if(lon.length() > 10)
			lon = lon.substring(0,9);
		if(lat.length() > 10)
			lat = lat.substring(0,9);
		osmPoint.setLon(lon);
		osmPoint.setLat(lat);
		osmPoint.setPointFound(true);
		
		return osmPoint;
	}
	
	public OSMPoint getCoordFromCityOrZip(String cityOrPostalCode,String countryCode){
		
		OSMPoint osmPoint = new OSMPoint();
		
		String country = "country=" +countryCode;
		String format = "&format=json";
		String query = null;
		List<OpenStreetMapPlace> placeList = null;
		//if all digits
		if (cityOrPostalCode.matches("\\d+")) {
			
			String postalCode = "&postalcode=" + cityOrPostalCode;
			
			query = osmRestUrl+country + postalCode + format;
			ResponseEntity<List<OpenStreetMapPlace>> rateResponse =
			        restTemplate.exchange(query,
			            HttpMethod.GET, null, new ParameterizedTypeReference<List<OpenStreetMapPlace>>(){});
			placeList = rateResponse.getBody();
		}
		else{
			String city = "&city=" + cityOrPostalCode;
			query = osmRestUrl+country + city + format;
			ResponseEntity<List<OpenStreetMapPlace>> rateResponse =
			        restTemplate.exchange(query,
			            HttpMethod.GET, null, new ParameterizedTypeReference<List<OpenStreetMapPlace>>(){});
			placeList = rateResponse.getBody();
		}
		
		if (placeList == null){
			osmPoint.setPointFound(false);
			return osmPoint;
		}
		
		String lon = placeList.get(0).getLon();
		String lat = placeList.get(0).getLat();
		if(lon.length() > 10)
			lon = lon.substring(0,9);
		if(lat.length() > 10)
			lat = lat.substring(0,9);
		osmPoint.setLon(lon);
		osmPoint.setLat(lat);
		osmPoint.setPointFound(true);
		
		
		
		
		return osmPoint;
	}
	
}

