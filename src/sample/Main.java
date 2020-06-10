package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
    	Parent root = FXMLLoader.load(getClass().getResource("view.fxml"));

        primaryStage.setTitle("Projet IHM 2020");
        primaryStage.setScene(new Scene(root, 695, 431));
        primaryStage.show();
        primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
    
}