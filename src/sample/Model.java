package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import donnees.FileReader;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;

public class Model {
 
	LinkedHashMap<Integer,Annee> data;
	HashMap<Position,MeshView> meshs=new HashMap();
	HashMap<Position,Cylinder> histo=new HashMap();

	Position selecPos;
	
	int anneeSelectionnee=1880;
	
	float animationVitesse=1f;
	
	/** tests */
	public int sampleNumber;
	
	
	/** */
	
	public Model() {
		data =new LinkedHashMap();
		selecPos=null;
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
	
	public LinkedHashMap<Integer,Annee> getData(){
		return data;
	}
	
	public ArrayList<Float> getTempZone(float lat,float lon) {
		Position p=new Position(lat,lon);
		ArrayList<Float> anomalies=new ArrayList();
		for (int annee: data.keySet()) {
			anomalies.add(data.get(annee).get(p));
		}
		return anomalies;
	}
	
	public Annee getAnneeSelectionnee() {
		return data.get(anneeSelectionnee);
	}
	
	public void setAnneeSelectionnee(int annee) {
		anneeSelectionnee=annee;
	}
	
	public int getAnneeEnCours() {
		return anneeSelectionnee;
	}
	
	public HashMap<Position,MeshView> getMeshs(){
		return meshs;
	}
	public HashMap<Position,Cylinder> getHisto(){
		return histo;
	}
	
	public Position getSelecPos() {
		System.out.println(selecPos);
		return selecPos;
	}
	public void setSelecPos(Position selecPos) {
		this.selecPos = selecPos;
	}
	
	public void setAnimationVitesse(float animationVitesse) {
		this.animationVitesse = animationVitesse;
	}
	
	public float getAnimationVitesse() {
		return animationVitesse;
	}

	/** tests */
	public void readTemperatureFile(String path) {
		FileReader.getDataFromCSVFile("src/donnees/tempanomaly_4x4grid.csv", data);
	}
	
}
