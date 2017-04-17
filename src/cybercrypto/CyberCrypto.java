/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cybercrypto;

import static cybercrypto.ScanData.scan;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * this is file that launches when program first runs. tells project to load the GUI from the fxml file, which then passes
 * off control to the DocumentController file that handles the GUI interface
 * @author jesse
 */
public class CyberCrypto extends Application {
    
    ArrayList<Double> rawData;
    ArrayList<Integer> dataQuantified;
    String fileName = "ECG.xlsx";
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));     
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }   
}
