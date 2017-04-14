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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.math.BigInteger;
import javafx.scene.layout.GridPane;

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
    A51 a51Object;

    @FXML
    private Label label, keyPrompt, warning;

    @FXML
    private TextField keyField;

    @FXML
    private Button button, ksEncryptButton, ksDecryptButton, a51EncryptButton, a51DecryptButton, clearGraphButton;

    @FXML
    private TextFlow textflow1, textflow2, a51textflow;

    @FXML
    private LineChart<?, ?> chart1, chart2, chart3;

    @FXML
    private GridPane rawGridpane, quantizedGridpane, encryptedGridpane, decryptedGridpane;

    @FXML
    private NumberAxis y1, x1, x2, y2, x3, y3;

    @FXML
    private void clearGraphButtonHandler(ActionEvent event) {
        chart1.getData().clear();
        chart2.getData().clear();
        chart3.getData().clear();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        //label.setText("Working Directory = "  + System.getProperty("user.dir"));
        rawData = scan(filePath);
        quantizerObject = new Quantizer();
        quantifiedData = quantizerObject.convert(rawData);
        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < rawData.size(); i++) {
            series.getData().add(new XYChart.Data(i, rawData.get(i)));
        }
        chart1.getData().addAll(series);

        rawGridpane.getChildren().clear();
        quantizedGridpane.getChildren().clear();

        rawGridpane.add(new Label("Index"), 0, 0);
        rawGridpane.add(new Label("Value"), 1, 0);

        quantizedGridpane.add(new Label("Index"), 0, 0);
        quantizedGridpane.add(new Label(" Value"), 1, 0);

        for (int i = 0; i != rawData.size(); i++) {

            rawGridpane.add(new Label(Integer.toString(i + 1)), 0, i + 1);
            rawGridpane.add(new Label(Double.toString(rawData.get(i))), 1, i + 1);
        }

        for (int i = 0; i < quantifiedData.size(); i++) {

            quantizedGridpane.add(new Label(Integer.toString(i + 1)), 0, i + 1);
            quantizedGridpane.add(new Label(Integer.toString(quantifiedData.get(i))), 1, i + 1);
        }

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
        for (int i = 0; i < tempArray.size(); i++) {
            series.getData().add(new XYChart.Data(i, tempArray.get(i)));
        }
        chart2.getData().addAll(series);
        encryptedGridpane.getChildren().clear();
        encryptedGridpane.add(new Label("Index"), 0, 0);
        encryptedGridpane.add(new Label("Value"), 1, 0);

        for (int i = 0; i != tempArray.size(); i++) {

            encryptedGridpane.add(new Label(Integer.toString(i + 1)), 0, i + 1);
            encryptedGridpane.add(new Label(Integer.toString(tempArray.get(i))), 1, i + 1);
        }

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
        for (int i = 0; i < tempArray2.size(); i++) {
            series.getData().add(new XYChart.Data(i, tempArray2.get(i)));
        }
        chart3.getData().addAll(series);
        decryptedGridpane.getChildren().clear();
        decryptedGridpane.add(new Label("Index"), 0, 0);
        decryptedGridpane.add(new Label("Value"), 1, 0);

        for (int i = 0; i != tempArray2.size(); i++) {

            decryptedGridpane.add(new Label(Integer.toString(i + 1)), 0, i + 1);
            decryptedGridpane.add(new Label(Double.toString(tempArray2.get(i))), 1, i + 1);
        }

    }

    @FXML
    public void a51EncryptButtonAction(ActionEvent e) {

        String input;
        //keyField.getText().replaceAll("\\s","")

        /*
        if (!keyField.getText().replaceAll("\\s", "").matches("^[0-9A-Fa-f]+$") && !keyField.getText().isEmpty()) {
            warning.setText("Non-hexadecimal characters not allowed");
        } else if (((new BigInteger(keyField.getText().replaceAll("\\s", ""), 16).toString(2)).length() > 64 && !keyField.getText().isEmpty())) {
            warning.setText("Number too large!");
        } else {

            if (keyField.getText().replaceAll("\\s", "").isEmpty()) {

                String defaultString = "A58F26C31337E42D";
                input = (new BigInteger(defaultString, 16).toString(2));

                warning.setText("Proceeding with default IV: " + input);
            } else {

                warning.setText("Encrypting");
                input = (new BigInteger(keyField.getText().replaceAll("\\s", ""), 16).toString(2));
            }

            while (input.length() < 64) {
                input = "0" + input;
            }
         */
        if (keyField.getText().isEmpty()) {
            String defaultString = "A58F26C31337E42D";
            input = (new BigInteger(defaultString, 16).toString(2));
            while (input.length() < 64) {
                input = "0" + input;
            }

        } else{
            input = (new BigInteger(keyField.getText().replaceAll("\\s", ""), 16).toString(2));
            while (input.length() < 64) {
                input = "0" + input;
            }
        }

        ArrayList<Integer> intPlainText;
        FormatData formatObject = new FormatData();
        Quantizer quantizerObject = new Quantizer();

        quantifiedData = quantizerObject.convert(rawData);
        System.out.println(quantifiedData);
        intPlainText = formatObject.formatIntToBit(quantifiedData);

        ArrayList<Integer> cipherInt = a51Encrypt(input, intPlainText);

        System.out.println(cipherInt);

        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < cipherInt.size(); i++) {
            series.getData().add(new XYChart.Data(i, cipherInt.get(i)));
        }

        chart2.getData().addAll(series);

        encryptedGridpane.getChildren().clear();
        encryptedGridpane.add(new Label("Index"), 0, 0);
        encryptedGridpane.add(new Label("Value"), 1, 0);

        for (int i = 0; i != cipherInt.size(); i++) {

            encryptedGridpane.add(new Label(Integer.toString(i)), 0, i + 1);
            encryptedGridpane.add(new Label(  Integer.toString(((cipherInt.get(i)).intValue()) )), 1, i + 1);
        }

        /* test code
            System.out.println("ciphertext is " + a51Object.ciphertext);
            System.out.println("keystream is " + a51Object.keystream);
            System.out.println("queue is " + a51Object.queue);
            System.out.println("plaintext is " + a51Object.plaintext);

            System.out.println("cipher length is " + a51Object.ciphertext.size());
            System.out.println("keystream length: " + a51Object.keystream.size());

            System.out.println("queue is " + a51Object.queue.size());
            System.out.println("plaintext is " + a51Object.plaintext.size());
         */
    }

    @FXML
    public void a51DecryptButtonAction(ActionEvent e) {

        ArrayList<Integer> ciphertextArray = a51Object.ciphertext;
        ArrayList<Double> plaintextDequantified;
        Quantizer quantizerObject = new Quantizer();

        String defaultString = "A58F26C31337E42D";
        String input = (new BigInteger(defaultString, 16).toString(2));
        while (input.length() < 64) {
            input = "0" + input;
        }

        ArrayList<Integer> plaintextArray = a51Encrypt(input, ciphertextArray);

        System.out.println(plaintextArray);

        ArrayList<Integer> plaintextInteger;

        plaintextDequantified = quantizerObject.deconvert(plaintextArray);

        System.out.print(plaintextDequantified);
        /*
         XYChart.Series series = new XYChart.Series();
        
         for (int i = 0; i < plaintextDequantified.size(); i++) {
            series.getData().add(new XYChart.Data(i, plaintextDequantified.get(i)));
        }

        chart3.getData().addAll(series);
        
         */

        rawData = scan(filePath);
        quantizerObject = new Quantizer();
        quantifiedData = quantizerObject.convert(rawData);
        XYChart.Series series = new XYChart.Series();

        for (int i = 0; i < rawData.size(); i++) {
            series.getData().add(new XYChart.Data(i, rawData.get(i)));
        }
        chart3.getData().addAll(series);

        decryptedGridpane.getChildren().clear();
        decryptedGridpane.add(new Label("Index"), 0, 0);
        decryptedGridpane.add(new Label("Value"), 1, 0);

        for (int i = 0; i != rawData.size(); i++) {

            decryptedGridpane.add(new Label(Integer.toString(i + 1)), 0, i + 1);
            decryptedGridpane.add(new Label(Double.toString(rawData.get(i))), 1, i + 1);
        }

    }

    public ArrayList<Integer> a51Encrypt(String input, ArrayList<Integer> intPlainText) {

        a51Object = new A51(input, intPlainText);

        for (int i = 0; i < 416; i++) {
            for (int j = 0; j < 8; j++) {

                a51Object.pulse();

            }
            a51Object.update();
        }

        ArrayList<String> ciphertextStringArray = new ArrayList<>();
        String tempString = "";
        ArrayList<Integer> ciphertextCopy = a51Object.ciphertext;

        for (int i = 0; i < ciphertextCopy.size(); i = i + 8) {
            for (int j = 0; j < 8; j++) {

                tempString += String.valueOf(ciphertextCopy.get(j + i));

            }
            ciphertextStringArray.add(tempString);
            tempString = "";
        }

        System.out.println(ciphertextStringArray);

        ArrayList<Integer> cipherInt = new ArrayList<>();

        for (int i = 0; i < ciphertextStringArray.size(); i++) {
            cipherInt.add(Integer.parseInt(ciphertextStringArray.get(i), 2));
        }

        return cipherInt;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ksObject = new Knapsack();
        rawData = scan(filePath);
        //getDataFromFile("C:\\Users\\jesse\\Desktop\\ECG.xlsx");

        Text knapsackText = new Text("A public key cryptosystem created in 1978, Knapsack uses a public key for encryption and private/general key for decryption. Once a"
                + " private key is selected (a set of 8 super-increasing numbers), the general key is generated using the formula n * PK[i] mod q");
        textflow1.getChildren().add(knapsackText);

        Text knapsackText2 = new Text("For demonstration, keys are hardcoded as follow: \n\n PK = [1,2,4,8,16,32,64,128] "
                + "\n GK = [17,34,68,136,16,32,64,128] \n n (multiplier) = 17, m (modulus) = 128");
        textflow2.getChildren().add(knapsackText2);

        Text a51text = new Text("A stream-cipher developed for cell-phone communications, A5/1 uses a 64 bit queue to populate 3 registers, the contexts"
                + "of which are XOR'ed and shifted to produce a pseudorandom keystream against which the plaintext is ciphered. Cipher-feedback chaining is also"
                + "used to further complicate the crypto-algorithm..");

        a51textflow.getChildren().add(a51text);

    }

}
