package sample;

import java.util.HashMap;
import donnees.FileReader;

public class Model {
 
	HashMap<Integer,Annee> data;
	
	public Model() {
		data =new HashMap();
    	FileReader.getDataFromCSVFile("src/donnees/tempanomaly_4x4grid.csv", data);
	}
}
