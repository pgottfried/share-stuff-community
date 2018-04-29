package application.service;

import org.springframework.data.elasticsearch.core.geo.GeoPoint;

/*
 * used to convert a String GeoPoint representation. See into mySql DB to se the form of the String.
 */

public class GeoConverterService {
	
	public GeoPoint convertToGeoPoint(String geoPointAsString){
		
		String latitudeString = geoPointAsString.split(",")[0];
		String longitudeString = geoPointAsString.split(",")[1];
		Double lon = Double.parseDouble(longitudeString);
		Double lat = Double.parseDouble(latitudeString);
		return new GeoPoint(lat,lon);
	}
}
