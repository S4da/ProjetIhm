package sample;

public class Position {

	double lat,lon;
	public Position(double lat,double lon) {
		this.lat=lat;
		this.lon=lon;
	}
	
	public double getLat() {
		return lat;
	}
	
	
	public double getLon() {
		return lon;
	}
	
	public boolean equals(Position p) {
		return this.lat==p.getLat() && this.lon==p.getLon();
	}
}
