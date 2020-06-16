package sample;

import java.net.URL;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import sample.CameraManager;

public class Controller {

	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    
    @FXML
    Pane paneEarth;
    
    @FXML
    RadioButton btnRadioQuadri;
    
    @FXML
    RadioButton btnRadioHisto;
    
    @FXML
    Button btnGraphe;
    
    @FXML
    Pane paneGraphe;
    
    @FXML
    Pane color10_8;
    
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
    Pane color8_6;
    
    @FXML
    Pane color6_4;
    
    @FXML
    Pane color4_2;
    
    @FXML
    Pane color2_0;
     
    @FXML
    Pane color_minus0_2;
    
    @FXML
    Pane color_minus2_4;
    
    @FXML
    Pane color_minus4_6;
    
    @FXML
    Pane color_minus6_8;
    
    @FXML
    Pane color_minus8_10;
    
	public void initialize() {
		   
			txtFieldAnnee.setText((int)slidAnnee.getValue()+"");
		       
			// tracer la terre de base
			Group root3D = new Group();
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
	        root3D.getChildren().add(light);

	        // Add ambient light
	        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
	        ambientLight.getScope().addAll(root3D);
	        root3D.getChildren().add(ambientLight);
	        
	        SubScene subscene = new SubScene(root3D,600,600,true,SceneAntialiasing.BALANCED);
	        CameraManager camMan=new CameraManager(camera,paneEarth,root3D);
	        subscene.setCamera(camera);
	        paneEarth.getChildren().addAll(subscene);
	        
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
    
    
    public void addQuadrilateral(Group parent,Point3D topRight,Point3D bottomRight,Point3D topLeft,Point3D bottomLeft,PhongMaterial material) {
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
    	
    	final MeshView meshView = new MeshView(triangleMesh);
    	meshView.setMaterial(material);
    	parent.getChildren().addAll(meshView);
    	
    }
    
    public void tracerQuadri(Group root) {
    	int base=0;
        double pas=4;
        
        for (int i=-90;i<90;i+=pas) {
        	for (int j=-180;j<180;j+=pas) {
        		 	Point3D topLeft=geoCoordTo3dCoord(i+pas,j,1.2f);
        	        Point3D topRight=geoCoordTo3dCoord(i+pas,j+pas,1.2f);
        	        Point3D botLeft=geoCoordTo3dCoord(i,j,1.2f);
        	        Point3D botRight=geoCoordTo3dCoord(i,j+pas,1.2f);
        	        PhongMaterial pm=new PhongMaterial();
        	    	pm.setDiffuseColor(Color.rgb(0, 255, 0, 0.1));
        	    	pm.setSpecularColor(Color.rgb(0, 255, 0, 0.1));
        	        addQuadrilateral(root,topRight,botRight,topLeft,botLeft,pm);
        	}
      
        }
    }
    /*
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

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }
    
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
