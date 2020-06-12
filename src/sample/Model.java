package sample;

import java.util.HashMap;
import donnees.FileReader;

public class Model {
 
	HashMap<Integer,Annee> data;
	
	/** tests */
	public int sampleNumber;
	
	
	/** */
	
	public Model() {
		data =new HashMap();
    	//FileReader.getDataFromCSVFile("src/donnees/tempanomaly_4x4grid.csv", data);
		readTemperatureFile("src/donnees/tempanomaly_4x4grid.csv");
		sampleNumber=data.size();
	}
	
	public float getMinTemp() { // a voir mettre la temp du premier elements de la hashmap
		float min=Float.MAX_VALUE;
		for (int annee: data.keySet()) {
			for (Position pos: data.get(annee).keySet()) {
				if (data.get(annee).get(pos)<min) min=data.get(annee).get(pos);
			}
		}
		return min;
	}
	
	public float getMaxTemp() { // a voir mettre la temp du premier elements de la hashmap
		float max=Float.MIN_VALUE;
		for (int annee: data.keySet()) {
			for (Position pos: data.get(annee).keySet()) {
				if (data.get(annee).get(pos)>max) max=data.get(annee).get(pos);
			}
		}
		return max;
	}
	
	public float getTemp(float lat, float lon,int annee) {
		Annee anneeR=data.get(annee);
		if (anneeR!=null) {
			for (Position pos: anneeR.keySet()) {
				if (pos.estDansLaZone(lat,lon)) {
					return anneeR.get(pos);
				}
			}
		}
		return Float.NaN;
	}
	
	public HashMap<Integer,Annee> getData(){
		return data;
	}
	
	
	
	
	
	/** tests */
	public void readTemperatureFile(String path) {
		FileReader.getDataFromCSVFile("src/donnees/tempanomaly_4x4grid.csv", data);
	}
	
}
