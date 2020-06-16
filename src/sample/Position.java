package sample;

import javafx.scene.shape.MeshView;

public class Position {

	float lat,lon;
	MeshView meshview;
	
	public Position(float lat,float lon) {
		this.lat=lat;
		this.lon=lon;
	}
	
	public float getLat() {
		return lat;
	}
	
	
	public float getLon() {
		return lon;
	}
	
	 @Override
    public int hashCode() {
        return Math.round(lat*lon+lon*6);
    }
	 
	@Override
	public boolean equals(Object p) {
		 if (p instanceof Position) {
			 Position pos=(Position)p;
			 return this.lat==pos.getLat() && this.lon==pos.getLon();
		 }
		 else return false;
	}
	
	public boolean estDansLaZone(float latR,float lonR) {
		return lat<=latR+2.0f && lat>=latR-2.0f && lon<=lonR+2.0f  && lon>=lonR-2.0f;
	}
	
	public String toString() {
		return "Position: lat: "+lat+", lon: "+lon;
	}
	
	public void setMeshView(MeshView m) {
		meshview=m;
	}
	
	public MeshView getMeshView() {
		return meshview;
	}
}
