package donnees;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import sample.Annee;
import sample.Position;

public class FileReader
{
	public static void getDataFromCSVFile(String csvFilePath,HashMap<Integer,Annee> resultat)
	{

		String line = "";
        String[] data = null;
        String separator = ",";
        
        //Document data
        double lat;
        double lon;    
        
       
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(csvFilePath), StandardCharsets.ISO_8859_1)) 
        {
        	//Read the first line
        	line = bufferedReader.readLine();
        	
        	//Get data from the line
        	data = line.split(separator, -1);
      
        	//Read the file line by line
            while ((line = bufferedReader.readLine()) != null)
            {
            	try {
	            	data = line.split(separator, -1);
	            	
	            	lat = Double.parseDouble(data[0]);
	            	lon = Double.parseDouble(data[1]);
	            	int annee=1880;
	            	double temp=0;
	            	for (int j=2;j<data.length;j++) {
	            		annee=1880+j-2;
	            		Annee anneeR=resultat.get(annee);	      
	            		if (anneeR!=null) {
	            			if (!data[j].equals("NA")) {
	            				temp=Double.parseDouble(data[j]);
		            			anneeR.put(new Position(lat,lon), temp);
	            			}
	            		}else {
	            			anneeR=new Annee(annee);
	            			if (!data[j].equals("NA")) {
	            				temp=Double.parseDouble(data[j]);
		            			anneeR.put(new Position(lat,lon), temp);
	            			}
	            			resultat.put(annee,anneeR);
	            		}
	            	}
            	}
            	catch(Exception e) {
            		
            	}
            	
            }

        } 
        catch (IOException exception) 
        {
            System.err.println(exception);
        }
	}
}
