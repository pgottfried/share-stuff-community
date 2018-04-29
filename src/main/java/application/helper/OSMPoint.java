package application.helper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OSMPoint {

	private String lat;
	private String lon;
	private boolean pointFound;
	
	
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public boolean isPointFound() {
		return pointFound;
	}
	public void setPointFound(boolean pointFound) {
		this.pointFound = pointFound;
	}
	
	
	
}
