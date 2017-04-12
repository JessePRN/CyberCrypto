/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cybercrypto;

import static cybercrypto.ScanData.scan;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author jesse
 */
public class FXMLDocumentController implements Initializable {

    ArrayList<Double> rawData;
    ArrayList<String> stringData;
    ArrayList<Integer> quantifiedData;
    ArrayList<String> encrypted;
    ArrayList<String> decrypted;

    String filePath = "src\\cybercrypto\\ECG.xlsx";
    FormatData formatObject;
    Quantizer quantizerObject;
    Knapsack ksObject;

    @FXML
    private Label label;

    @FXML
    private Button button, ksEncryptButton, ksDecryptButton;

    @FXML
    private TextFlow textflow1;

    @FXML
    private LineChart<?, ?> chart1, chart2, chart3;

    // @FXML
    //private CategoryAxis x;
    @FXML
    private NumberAxis y1, x1, x2, y2, x3, y3;

    @FXML
    private void handleButtonAction(ActionEvent event) {

        label.setText("Working Directory = "
                + System.getProperty("user.dir"));

        rawData = scan(filePath);

        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < rawData.size(); i++) {
            series.getData().add(new XYChart.Data(i, rawData.get(i)));
        }

        chart1.getData().addAll(series);
    }

    @FXML
    private void ksEncryptButtonAction(ActionEvent event) {

        formatObject = new FormatData();
        quantizerObject = new Quantizer();

        quantifiedData = quantizerObject.convert(rawData);
        stringData = formatObject.formatIntToString(quantifiedData);
        encrypted = ksObject.encrypt(stringData);
        ArrayList<Integer> tempArray = new ArrayList<>();

        for (int i = 0; i < encrypted.size(); i++) {
            tempArray.add(Integer.parseInt(encrypted.get(i), 2));
        }
        
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i < tempArray.size(); i++){
            series.getData().add(new XYChart.Data(i, tempArray.get(i)));
        }
        chart2.getData().addAll(series);

    }
    
    @FXML
    private void ksDecryptButtonAction(ActionEvent event) {
        formatObject = new FormatData();
        quantizerObject = new Quantizer();

        quantifiedData = quantizerObject.convert(rawData);
        stringData = formatObject.formatIntToString(quantifiedData);
        encrypted = ksObject.encrypt(stringData);
        decrypted = ksObject.decrypt(encrypted);
        
        ArrayList<Integer> tempArray = new ArrayList<>();

        for (int i = 0; i < decrypted.size(); i++) {
            tempArray.add(Integer.parseInt(decrypted.get(i), 2));
        }
        
        ArrayList<Double> tempArray2 = new ArrayList<>();
        tempArray2 = quantizerObject.deconvert(tempArray);
        
        XYChart.Series series = new XYChart.Series();
        for(int i = 0; i < tempArray2.size(); i++){
            series.getData().add(new XYChart.Data(i, tempArray2.get(i)));
        }
        chart3.getData().addAll(series);
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ksObject = new Knapsack();
        rawData = scan(filePath);
        //getDataFromFile("C:\\Users\\jesse\\Desktop\\ECG.xlsx");

        Text knapsackText = new Text("A public key cryptosystem created in 1978, uses a public key for encryption and private key for decryption.");
        textflow1.getChildren().add(knapsackText);

    }

}
