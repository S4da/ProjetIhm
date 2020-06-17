package sample;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.UnaryOperator;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import sample.CameraManager;

public class Controller {

	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    
    private Model model;
    @FXML
    Pane paneEarth;
    
    @FXML
    RadioButton btnRadioQuadri;
    
    @FXML
    RadioButton btnRadioHisto;
    
    @FXML
    Button btnGraphe;
    
    @FXML
    AnchorPane paneGraphe;
    
    @FXML
    BarChart<String,Number> graphe;
    
    @FXML
    TextField txtFieldAnnee;
    
    @FXML
    Slider slidAnnee;
    
    @FXML
    Button btnPlay;
    
    @FXML
    Button btnPause;
    
    @FXML
    Button btnStop;
    
    @FXML
    ComboBox cmbVitesse;
    
    @FXML
    Rectangle color10_8;
    
    @FXML
    Rectangle color8_6;
    
    @FXML
    Rectangle color6_4;
    
    @FXML
    Rectangle color4_2;
    
    @FXML
    Rectangle color2_0;
     
    @FXML
    Rectangle color_minus0_2;
    
    @FXML
    Rectangle color_minus2_4;
    
    @FXML
    Rectangle color_minus4_6;
    
    @FXML
    Rectangle color_minus6_8;
    
    @FXML
    Rectangle color_minus8_10;
    
    @FXML
    Label latLabel;
    
    @FXML
    Label lonLabel;
    
    Group quadri;
    Group histo;
    Group root3D;
    Group latLonLabel;
    UnaryOperator filter;
    boolean histoAffiche=false;
    boolean quadriAffiche=false;
    boolean grapheAffiche=false;
    
    public void initFormatter() {
  	  filter = new UnaryOperator<TextFormatter.Change>() {
  	  @Override
  	  public TextFormatter.Change apply(TextFormatter.Change t) {
  	    String text = t.getControlText() + t.getText();
  	      if (!t.isReplaced())
  	        if (!text.matches("^[-+]?[0-9]*[.]?[0-9]*([eE]?[-+]*[0-9]*)?$"))
  	          t.setText("");
  	          return t;
  	        }
  	      };
    }
    
	public void initialize() {
		   
			model=new Model();
			initFormatter();
			txtFieldAnnee.setText((int)slidAnnee.getValue()+"");
		    txtFieldAnnee.setTextFormatter(new TextFormatter(filter));
		    btnGraphe.setDisable(true);
		   graphe.setVisible(false);
		   
	          
			// tracer la terre de base
			root3D = new Group();
			latLonLabel=new Group();
	        ObjModelImporter objImporter =new ObjModelImporter();
	        try {
	        	URL modeUrl= this.getClass().getResource("Earth/earth.obj");
	        	objImporter.read(modeUrl);
	        }catch(ImportException e) {
	        	System.out.println(e.getMessage());
	        }
	        MeshView[] meshViews = objImporter.getImport();
	        Group earth = new Group(meshViews);
	        root3D.getChildren().add(earth);
	       
	        // Add a camera group
	        PerspectiveCamera camera = new PerspectiveCamera(true);

	        // Add point light
	        PointLight light = new PointLight(Color.WHITE);
	        light.setTranslateX(-180);
	        light.setTranslateY(-90);
	        light.setTranslateZ(-120);
	        light.getScope().addAll(root3D);
	        //root3D.getChildren().add(light);

	        // Add ambient light
	        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
	        ambientLight.getScope().addAll(root3D);
	        root3D.getChildren().add(ambientLight);
	        
	        setLatLonLabel();
	        
	        quadri=new Group();
	        histo=new Group();
	        tracerQuadri();
	        tracerHisto();
	        
	        //root3D.getChildren().add(quadri);
	        
	        SubScene subscene = new SubScene(root3D,600,600,true,SceneAntialiasing.BALANCED);
	        CameraManager camMan=new CameraManager(camera,paneEarth,root3D);
	        subscene.setCamera(camera);
	        paneEarth.getChildren().addAll(subscene);
	
		    
		    ToggleGroup toggle=btnRadioQuadri.getToggleGroup();
			
			// on ajoute un listener pour tous les radioButton li�s entre eux (tous)
			toggle.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
	        { 
	            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) 
	            { 
	                RadioButton rb = (RadioButton)toggle.getSelectedToggle(); 
	                if (rb != null) { 
	                    String s = rb.getText();
	                    if (s.equals("Quadrilatere")) {
	                    	afficherQuadri();
	                    }
	                    else if (s.equals("Histogramme")) {
	                    	afficherHisto();
	                    	
	                    }
	                } 
	            } 
	        });
		    
			
		    txtFieldAnnee.setOnKeyReleased(new EventHandler<KeyEvent>() {
	              @Override
	              public void handle(KeyEvent e) {
	                  //TODO
	            	  if (!e.getCode().equals(KeyCode.BACK_SPACE)) {
		            	  int annee=0;
		            	  try {
		            		  annee= Integer.parseInt(txtFieldAnnee.getText());
		            	  }catch(Exception er) {
		            		  annee=1880;
		            	  }
		            	  if (annee<1880) {
		            		  annee=1880;
		            	  }
		            	  else if(annee>2020) {
		            		  annee=2020;
		            	  }
		            	  model.setAnneeSelectionnee(annee);
		            	  slidAnnee.setValue(annee);
		            	  txtFieldAnnee.end();
		            	  afficherHistoQuadri();
	            	  }
	              }
	          });
		   
		    txtFieldAnnee.focusedProperty().addListener(new ChangeListener<Boolean>()
		    {
		        @Override
		        public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
		        {
		            if (!newPropertyValue)
		            {
		            	txtFieldAnnee.setText(model.getAnneeEnCours()+"");
		            }
		    
		        }
		    });
		    
		  
		    slidAnnee.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> ov,
	                Number old_val, Number new_val) {
	            	
	            	 model.setAnneeSelectionnee(new_val.intValue());
	            	 txtFieldAnnee.setText(new_val.intValue()+"");
	            	 afficherHistoQuadri();
	            }
	        });
		    
		    btnGraphe.setOnAction(new EventHandler<ActionEvent>() {
		        @Override public void handle(ActionEvent e) {

		        	afficherGraphe();
		        }
		    });
	          
	}
	
    public static Point3D geoCoordTo3dCoord(double lat, double lon,float radius) {
        double lat_cor = lat + TEXTURE_LAT_OFFSET;
        double lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius,
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor))*radius,
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor))*radius);
    }
    
    
    public MeshView addQuadrilateral(Group parent,Point3D topRight,Point3D bottomRight,Point3D topLeft,Point3D bottomLeft,PhongMaterial material,Position p) {
    	final TriangleMesh triangleMesh = new TriangleMesh();
    	
    	final float[] points = {
    			(float)topRight.getX(),(float)topRight.getY(),(float)topRight.getZ(),
    			(float)topLeft.getX(),(float)topLeft.getY(),(float)topLeft.getZ(),
    			(float)bottomLeft.getX(),(float)bottomLeft.getY(),(float)bottomLeft.getZ(),
    			(float)bottomRight.getX(),(float)bottomRight.getY(),(float)bottomRight.getZ()
    	};
    	
    	final float[] texCoords = {
    			1, 1,
    			1, 0,
    			0, 1,
    			0, 0
    	};
    	
    	final int[] faces = {
    			0, 1, 1, 0, 2, 2,
    			0, 1, 2, 2, 3, 3
    	};
    	
    	triangleMesh.getPoints().setAll(points);
    	triangleMesh.getTexCoords().setAll(texCoords);
    	triangleMesh.getFaces().setAll(faces);
    	
    	MeshView meshView = new MeshView(triangleMesh);
    	meshView.setMaterial(material);
    	
    	parent.getChildren().add(meshView);
    	return meshView;
    }
    
    public void tracerQuadri() {
    	Annee annee=model.getAnneeSelectionnee();
    	if (annee!=null) {
	        double pas=2;
	        for (int i=-88;i<=88;i+=4) {
	        	for (int j=-178;j<=178;j+=4) {
	        		Position p=new Position(i,j);       		
	        		Float temp=annee.get(p);
	        		if (temp!=null) {
		    		 	Point3D topLeft=geoCoordTo3dCoord(i+pas,j-pas,1.2f);
		    	        Point3D topRight=geoCoordTo3dCoord(i+pas,j+pas,1.2f);
		    	        Point3D botLeft=geoCoordTo3dCoord(i-pas,j-pas,1.2f);
		    	        Point3D botRight=geoCoordTo3dCoord(i-pas,j+pas,1.2f);
		    	        PhongMaterial pm=new PhongMaterial();
		    	      	pm.setDiffuseColor(Color.GREY);
			        	pm.setSpecularColor(Color.GREY);

		    	        //pm.setSpecularColor(null);
		    	        MeshView meshView=addQuadrilateral(quadri,topRight,botRight,topLeft,botLeft,pm,p);
		    	        meshView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    	    	    @Override
		    	    	    public void handle(MouseEvent mouseEvent) {
		    	    	        latLabel.setText(p.getLat()+"");
		    	    	        lonLabel.setText(p.getLon()+"");
		    	    	        if (grapheAffiche) {
		    	    	        	cacherGraphe();
		    	    	        }
		    	    	        zoneSelected();
		    	    	        model.setSelecPos(new Position(p.getLat(),p.getLon()));
		    	    	    }
		    	    	});
		    	        HashMap<Position,MeshView> meshs=model.getMeshs();
		    	    	meshs.put(p,meshView);
	        		}
	        	}
	        }
    	}
    }
    
    public void afficherQuadri() {
    	
    	if (histoAffiche) {
        	root3D.getChildren().remove(histo);
        	root3D.getChildren().add(quadri);
    	}else if(!quadriAffiche && !histoAffiche) {
    		root3D.getChildren().add(quadri);
    	}
    	histoAffiche=false;
    	quadriAffiche=true;
    	Annee annee=model.getAnneeSelectionnee();
    	float alpha=0.7f;
    	if (annee!=null) {
	        for (Position p: annee.keySet()) {
	        		if (annee.get(p)!=null) {
	        			
		        		MeshView mesh=model.getMeshs().get(p);
		        		PhongMaterial pm=new PhongMaterial();
		        		Float temp=annee.get(p);
		        		Color color=new Color(Color.GREY.getRed(),Color.GREY.getGreen(),Color.GREY.getBlue(),alpha);
		    	        if (temp==Float.NaN) {
			        	    pm.setDiffuseColor(new Color(Color.GREY.getRed(),Color.GREY.getGreen(),Color.GREY.getBlue(),alpha));
			        	    pm.setSpecularColor(new Color(Color.GREY.getRed(),Color.GREY.getGreen(),Color.GREY.getBlue(),alpha)); 
		    	        }
		    	        else if (temp>8){
		    	        	color=(Color)color10_8.getFill();
		    	        }
		    	        else if (temp>6){
		    	        	color=(Color)color8_6.getFill();
		    	        }
		    	        else if (temp>4){
		    	        	color=(Color)color6_4.getFill();
		    	        }
		    	        else if (temp>2){
		    	        	color=(Color)color4_2.getFill();
		    	        }
		    	        else if (temp>0){
		    	        	color=(Color)color2_0.getFill();
		    	        }
		    	        else if (temp>-2){
		    	        	color=(Color)color_minus0_2.getFill();
		    	        }
		    	        else if (temp>-4){
		    	        	color=(Color)color_minus2_4.getFill();
		    	        }
		    	        else if (temp>-6){
		    	        	color=(Color)color_minus4_6.getFill();
		    	        }
		    	        else if (temp>-8){
		    	        	color=(Color)color_minus6_8.getFill();
		    	        }
		    	        else if (temp>-10){
		    	        	color=(Color)color_minus8_10.getFill();
		    	        }
		    	    
		    	        pm.setDiffuseColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha));
		        	    pm.setSpecularColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha));
		        	    mesh.setMaterial(pm);
	        		}
	        	}
	        }
    	}
    
   
    
     // From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        int division=2;
        Cylinder line = new Cylinder(0.01f, height,division);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }
    
    public void tracerHisto() {
    	Annee annee=model.getAnneeSelectionnee();
    	if (annee!=null) {
	        for (int i=-88;i<=88;i+=4) {
	        	for (int j=-178;j<=178;j+=4) {
	        		Position p=new Position(i,j);       		
	        		Float temp=annee.get(p);
	        		if (temp!=null) {
	        			Point3D origin=geoCoordTo3dCoord(i,j,1);
	        			Point3D target=geoCoordTo3dCoord(i,j,1.1f);
	        			Cylinder cylinder=createLine(origin,target);
		    	        PhongMaterial pm=new PhongMaterial();
		    	      	pm.setDiffuseColor(Color.GREY);
			        	pm.setSpecularColor(Color.GREY);
			        	cylinder.setMaterial(pm);
		    	        //pm.setSpecularColor(null);
		    	        model.getHisto().put(p,cylinder);
		    	        histo.getChildren().add(cylinder);
	        		}
	        	}
	        }
    	}
    }
    
	 public void afficherHisto() {
		 
		 if (quadriAffiche) {
	        	root3D.getChildren().remove(quadri);
	        	root3D.getChildren().add(histo);
	    	}else if(!histoAffiche && !quadriAffiche) {
	    		root3D.getChildren().add(histo);
	    	}
		 histoAffiche=true;
     	 quadriAffiche=false;
	    	Annee annee=model.getAnneeSelectionnee();
	    	float alpha=0.7f;
	    	float minTemp=Math.abs(model.getMinTemp());
		    float taille=0;
			float pasTaille=0.5f;
	    	if (annee!=null) {
		        for (Position p: annee.keySet()) {
		        		if (annee.get(p)!=null) {	
			        		Cylinder cylinder=model.getHisto().get(p);
			        		PhongMaterial pm=new PhongMaterial();
			        		Float temp=annee.get(p);
			        		taille=((minTemp+temp)/minTemp)-pasTaille;
			        		cylinder.setHeight(taille);
			        		Color color=new Color(Color.GREY.getRed(),Color.GREY.getGreen(),Color.GREY.getBlue(),alpha);
			    	        if (temp==Float.NaN) {
				        	    pm.setDiffuseColor(new Color(Color.GREY.getRed(),Color.GREY.getGreen(),Color.GREY.getBlue(),alpha));
				        	    pm.setSpecularColor(new Color(Color.GREY.getRed(),Color.GREY.getGreen(),Color.GREY.getBlue(),alpha)); 
			    	        }
			    	        else if (temp>8){
			    	        	color=(Color)color10_8.getFill();
			    	        }
			    	        else if (temp>6){
			    	        	color=(Color)color8_6.getFill();
			    	        }
			    	        else if (temp>4){
			    	        	color=(Color)color6_4.getFill();
			    	        }
			    	        else if (temp>2){
			    	        	color=(Color)color4_2.getFill();
			    	        }
			    	        else if (temp>0){
			    	        	color=(Color)color2_0.getFill();
			    	        }
			    	        else if (temp>-2){
			    	        	color=(Color)color_minus0_2.getFill();
			    	        }
			    	        else if (temp>-4){
			    	        	color=(Color)color_minus2_4.getFill();
			    	        }
			    	        else if (temp>-6){
			    	        	color=(Color)color_minus4_6.getFill();
			    	        }
			    	        else if (temp>-8){
			    	        	color=(Color)color_minus6_8.getFill();
			    	        }
			    	        else if (temp>-10){
			    	        	color=(Color)color_minus8_10.getFill();
			    	        }
			    	    
			    	        pm.setDiffuseColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha));
			        	    pm.setSpecularColor(new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha));
			        	    cylinder.setMaterial(pm);
			    
		        		}
		        	}
		        }
	    	}
    
	 
	 public void afficherHistoQuadri() {
		 if (btnRadioQuadri.isSelected()) {
    		 afficherQuadri();
    	 }
    	  else if (btnRadioHisto.isSelected()) {
    		  	afficherHisto();
    	 }
	 }
	 
	 
	 public void setLatLonLabel() {
	    	Annee annee=model.getAnneeSelectionnee();
	    	if (annee!=null) {
		        double pas=2;
		        for (int i=-88;i<=88;i+=4) {
		        	for (int j=-178;j<=178;j+=4) {
		        		Position p=new Position(i,j);       		
		        		Float temp=annee.get(p);
		        		if (temp!=null) {
			    		 	Point3D topLeft=geoCoordTo3dCoord(i+pas,j-pas,1.01f);
			    	        Point3D topRight=geoCoordTo3dCoord(i+pas,j+pas,1.01f);
			    	        Point3D botLeft=geoCoordTo3dCoord(i-pas,j-pas,1.01f);
			    	        Point3D botRight=geoCoordTo3dCoord(i-pas,j+pas,1.01f);
			    	        PhongMaterial pm=new PhongMaterial();
			    	      	pm.setDiffuseColor(Color.TRANSPARENT);
				        	pm.setSpecularColor(Color.TRANSPARENT);

			    	        //pm.setSpecularColor(null);
			    	        MeshView meshView=addQuadrilateral(latLonLabel,topRight,botRight,topLeft,botLeft,pm,p);
			    	        meshView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			    	    	    @Override
			    	    	    public void handle(MouseEvent mouseEvent) {
			    	    	        latLabel.setText(p.getLat()+"");
			    	    	        lonLabel.setText(p.getLon()+"");
			    	    	        if (grapheAffiche) {
			    	    	        	cacherGraphe();
			    	    	        }
			    	    	        zoneSelected();
			    	    	        model.setSelecPos(new Position(p.getLat(),p.getLon()));
			    	    	    }
			    	    	});
		        		}
		        	}
		        }
	    	}
	    	root3D.getChildren().add(latLonLabel);
	    }
	 
	 public void afficherGraphe() {
		 graphe.getData().clear();
		 if (!grapheAffiche) {
			 grapheAffiche=true;
			 graphe.setVisible(true);
		 }
		 
		 Position pos=model.getSelecPos();
		 
		 XYChart.Series<String, Number> series = new Series<>();
		 series.setName(pos.toString());
		 LinkedHashMap<Integer, Annee> data = model.getData();
		 
		 if (pos!=null) {
			 Number temp=0.0f;
			 
			 for(Integer annee: data.keySet()) {
				 temp=data.get(annee).get(pos);
				 System.out.println(annee.toString()+" "+temp);
				 series.getData().add(new XYChart.Data<String,Number>(annee.toString(),temp)); 
			 }
			 graphe.layout();
			 graphe.getData().add(series);
			 
		 }
		 	
	 }
	 
	 public void zoneSelected() {
		 btnGraphe.setDisable(false);
	 }
	 
	 public void cacherGraphe() {
		 graphe.setVisible(false);
		 grapheAffiche=false;
		 btnGraphe.setDisable(true);
	 }
    /*
    public void displayTown(Group parent, String name, double lat, double lon) {
    	Sphere sphere=new Sphere(0.01);
    	sphere.setId(name);
    	Point3D p=geoCoordTo3dCoord(lat,lon,1);
    	PhongMaterial pm=new PhongMaterial();
    	pm.setDiffuseColor(Color.LIME);
    	pm.setSpecularColor(Color.LIME);
    	sphere.setMaterial(pm);
    	sphere.setTranslateX(p.getX());
    	sphere.setTranslateY(p.getY());
    	sphere.setTranslateZ(p.getZ());
    	parent.getChildren().add(sphere);
    		
    }*/
    
}
