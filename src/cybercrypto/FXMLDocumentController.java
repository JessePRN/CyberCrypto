/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cybercrypto;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author jesse
 */
public class FXMLDocumentController implements Initializable {
    
   @FXML
    private Label label;
    
    @FXML
    private Button button;

    @FXML
    private LineChart<?, ?> chart1;

   // @FXML
    //private CategoryAxis x;

    @FXML
    private NumberAxis y1, x1;

  
    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        XYChart.Series series = new XYChart.Series();
        
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 25));
    
        
        chart1.getData().addAll(series);
    }    
    
}
