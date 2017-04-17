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


/**************
 * this is the main class that streamlines all the encryption and auxiliary classes while establishing GUI interaction
 **************/

public class FXMLDocumentController implements Initializable {

    //a laundry list of declarations below. all these variables get used at some point the logic. some are initialized in 
    //the main initialize method (below), others when certain methods are called. declaring them here allows all methods in
    //the class to access the same variables
    ArrayList<Double> rawData;
    ArrayList<String> stringData;
    ArrayList<Integer> quantifiedData;
    ArrayList<String> encrypted;
    ArrayList<String> decrypted;
    String input;
    String filePath = "src\\cybercrypto\\ECG.xlsx";
    FormatData formatObject;
    Quantizer quantizerObject;
    Knapsack ksObject;
    A51 a51EncryptObject;
    A51 a51DecryptObject;

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
        //this method clears all charts, called by the "clear" button
        chart1.getData().clear();
        chart2.getData().clear();
        chart3.getData().clear();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        //this method tosses the EKG data onto the first chart and gridpane
        
        quantizerObject = new Quantizer();
        quantifiedData = quantizerObject.convert(rawData);
        
        chartData(rawData, chart1);

        rawGridpane.getChildren().clear();
        quantizedGridpane.getChildren().clear();

        populateGridpane(rawData, rawGridpane);
        populateGridpane(quantifiedData, quantizedGridpane);

    }

    @FXML
    private void ksEncryptButtonAction(ActionEvent event) {
        //this method quantizes raw data, encrypts it using knapsack, and populates charts/gridpanes. notice it creates a new Quantize object
        //each call so that everytime charts/grids that are populated will vary as the Quantizer.lookup Map is instantiated 
        //based on a different Random variable
        
        quantizerObject = new Quantizer();

        //maps raw values onto quantized values using knapsack safenumbers. key<->value map maintained in Quantizer.lookup Map object
        quantifiedData = quantizerObject.convert(rawData);

        //convers arraylist ints to arraylist (binary) strings
        stringData = formatObject.formatIntToString(quantifiedData);

        //encrypts
        encrypted = ksObject.encrypt(stringData);

        //formats encrypted (binary) string arraylist back into arraylist ints for charting
        ArrayList<Integer> tempArray = formatObject.formatStringToInt(encrypted);

        //charts data onto GUI     
        chartData(tempArray, chart2);

        //populates GUI gridpane with values
        populateGridpane(tempArray, encryptedGridpane);
    }

    @FXML
    private void ksDecryptButtonAction(ActionEvent event) {
        
        //retrieves last KS-encrypted arraylist (as new ones generated with each button click)
        encrypted = ksObject.getLastEncrypted();

        //decrypts it
        decrypted = ksObject.decrypt(encrypted);

        //formats decrypted (binary) String arraylist into integer arraylist for dequantizing 
        ArrayList<Integer> tempArray = formatObject.formatStringToInt(decrypted);

        //runs quantized values through lookup to retrieve raw values
        ArrayList<Double> tempArray2 = quantizerObject.deconvert(tempArray);

        chartData(tempArray2, chart3);
        populateGridpane(tempArray2, decryptedGridpane);

    }

    @FXML
    public void a51EncryptButtonAction(ActionEvent e) {
        
        //working on how to handle user input below (incase they type in chars that arent hex, or are out of bounds, etc)
        
        //String input;
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
        
        
        //simple input handling for now. if input field is empty, program defaults to value below, otherwise it attempts to 
        //parse user input as a variable
        if (keyField.getText().isEmpty()) {
            String defaultString = "A58F26C31337E42D";
            input = (new BigInteger(defaultString, 16).toString(2));
            while (input.length() < 64) {
                input = "0" + input;
            }

        } else {
            input = (new BigInteger(keyField.getText().replaceAll("\\s", ""), 16).toString(2));
            while (input.length() < 64) {
                input = "0" + input;
            }
        }

       
        //news Quantizer object made every time button clicked for varying data
        quantizerObject = new Quantizer();
        //converting raw to quantified data
        quantifiedData = quantizerObject.convert(rawData);
        //converting arraylist of integers to arraylist of bits for encryption
        ArrayList<Integer> intPlainText = formatObject.formatIntToBit(quantifiedData);
        //creating a51 object with user or default input (that's our initiation vector) and the prepared EKG data (in bits)
        a51EncryptObject = new A51(input, intPlainText);
        //this call consolidates all the a51 activity, encrypting data and returning it in an arraylist of bits
        ArrayList<Integer> ciphertextCopy = a51EncryptObject.encryptCycle();
        //format data as strings (takes 8 bits at a time and creates integer representations of them)
        ArrayList<String> ciphertextStringArray = formatObject.formatBytesToStringArray(ciphertextCopy);
        //converts String arraylist (representing bytes) into actualy integers to be charted
        ArrayList<Integer> cipherInt = formatObject.formatStringToInt(ciphertextStringArray);

        //toss to chart and gridpane
        chartData(cipherInt, chart2);
        populateGridpane(cipherInt, encryptedGridpane);

      
    }

    @FXML
    public void a51DecryptButtonAction(ActionEvent e) {

        //creates an a51 object intended to decrypt data. its created with the same IV as the encryption object, but takes
        //generated ciphertext as a parameter instead of plaintext
        a51DecryptObject = new A51(input, a51EncryptObject.ciphertext);
       
        //decrypts, see a51 class for logic
        ArrayList<Integer> ciphertextCopy = a51DecryptObject.decryptCycle();

        //bunch of formatting as before to prepare data for GUI
        ArrayList<String> ciphertextStringArray = formatObject.formatBytesToStringArray(ciphertextCopy); 
        ArrayList<Integer> tempArray = formatObject.formatStringToInt(ciphertextStringArray);       
        ArrayList<Double> tempArray2 = quantizerObject.deconvert(tempArray);
        
        chartData(tempArray2, chart3);
        populateGridpane(tempArray2, decryptedGridpane);

    }

  

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //here objects that are shared between different methods are instantiated upon application being launched.
        formatObject = new FormatData();
        rawData = scan(filePath);
        ksObject = new Knapsack();
        

        //text files that populate GUI with instructions/explanations
        Text knapsackText = new Text("A public key cryptosystem created in 1978, Knapsack uses a public key for encryption and private/general key for decryption. Once a"
                + " private key is selected (a set of 8 super-increasing numbers), the general key is generated using the formula n * PK[i] mod q");
        textflow1.getChildren().add(knapsackText);

        Text knapsackText2 = new Text("For demonstration, keys are hardcoded as follow: \n\n PK = [1,2,4,8,16,32,64,128] "
                + "\n GK = [17,34,68,136,16,32,64,128] \n n (multiplier) = 17, m (modulus) = 256");
        textflow2.getChildren().add(knapsackText2);

        Text a51text = new Text("A stream-cipher developed for cell-phone communications, A5/1 uses a 64 bit queue to populate 3 registers, the contexts"
                + "of which are XOR'ed and shifted to produce a pseudorandom keystream against which the plaintext is ciphered. Cipher-feedback chaining is also"
                + "used to further complicate the crypto-algorithm.");

        a51textflow.getChildren().add(a51text);

    }

    //consolidated charting method as its called many times for clean coding
    private void chartData(ArrayList arrayData, LineChart<?, ?> chart) {

        XYChart.Series series = new XYChart.Series();
        for (int i = 0; i < arrayData.size(); i++) {
            series.getData().add(new XYChart.Data(i, arrayData.get(i)));
        }
        chart.getData().addAll(series);
    }

    //consolidated gridpane populating method for easy re-use
    private void populateGridpane(ArrayList arrayData, GridPane gridpane) {

        gridpane.getChildren().clear();
        gridpane.add(new Label("Index"), 0, 0);
        gridpane.add(new Label("Value"), 1, 0);

        for (int i = 0; i != arrayData.size(); i++) {
            gridpane.add(new Label(Integer.toString(i + 1)), 0, i + 1);
            gridpane.add(new Label((String.valueOf(arrayData.get(i)))), 1, i + 1);
        }
    }
}
