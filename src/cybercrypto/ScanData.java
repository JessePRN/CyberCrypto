package cybercrypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

/* imports apache library to scan excel file
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; */

/**
 * this class capable of reading excel files (in event project gets expanded to incorporate multiple files). the EKG data
 * currently hard-coded in so its easier to deploy
 * @author jesse
 */
public class ScanData {

    public static ArrayList scan(String filePath) {

        ArrayList<Double> dataArray = new ArrayList<>(Arrays.asList(-0.01, -0.02, -0.02, -0.01, -0.01, 0.0, 0.0, -0.01, -0.01, 0.0, 
                0.0, -0.02, -0.03, -0.02, -0.02, -0.05, -0.07, -0.05, -0.04, -0.04, -0.05, -0.07, -0.07, -0.05, -0.07, -0.08, -0.05, 
                -0.02, 0.01, 0.09, 0.16, 0.26, 0.42, 0.65, 0.91, 1.07, 0.98, 0.45, -0.19, -0.49, -0.53, -0.49, -0.41, -0.36, -0.34, 
                -0.34, -0.34, -0.33, -0.33, -0.32, -0.32, -0.33, -0.32, -0.32, -0.31, -0.31, -0.29, -0.29, -0.27, -0.27, -0.29, -0.3, 
                -0.3, -0.26, -0.24, -0.22, -0.21, -0.21, -0.2, -0.19, -0.18, -0.18, -0.16, -0.13, -0.11, -0.11, -0.11, -0.1, -0.08, 
                -0.07, -0.07, -0.1, -0.11, -0.11, -0.13, -0.14, -0.14, -0.13, -0.13, -0.12, -0.12, -0.11, -0.09, -0.09, -0.08, -0.07, 
                -0.08, -0.09, -0.08, -0.08, -0.01, -0.02, -0.02, -0.01, -0.01, -0.02, -0.02, -0.01, -0.01, 0.0, 0.0, -0.01, -0.01, 
                0.0, 0.0, -0.02, -0.03, -0.02, -0.02, -0.05, -0.07, -0.05, -0.04, -0.04, -0.05, -0.07, -0.07, -0.05, -0.07, -0.08, 
                -0.05, -0.02, 0.01, 0.09, 0.16, 0.26, 0.42, 0.65, 0.91, 1.07, 0.98, 0.45, -0.19, -0.49, -0.53, -0.49, -0.41, -0.36, 
                -0.34, -0.34, -0.34, -0.33, -0.33, -0.32, -0.32, -0.33, -0.32, -0.32, -0.31, -0.31, -0.29, -0.29, -0.27, -0.27, 
                -0.29, -0.3, -0.3, -0.26, -0.24, -0.22, -0.21, -0.21, -0.2, -0.19, -0.18, -0.18, -0.16, -0.13, -0.11, -0.11, -0.11, 
                -0.1, -0.08, -0.07, -0.07, -0.1, -0.11, -0.11, -0.13, -0.14, -0.14, -0.13, -0.13, -0.12, -0.12, -0.11, -0.09, -0.09, 
                -0.08, -0.07, -0.08, -0.09, -0.08, -0.08, -0.01, -0.02, -0.02, -0.01, -0.01, -0.02, -0.02, -0.01, -0.01, 0.0, 0.0, 
                -0.01, -0.01, 0.0, 0.0, -0.02, -0.03, -0.02, -0.02, -0.05, -0.07, -0.05, -0.04, -0.04, -0.05, -0.07, -0.07, -0.05, 
                -0.07, -0.08, -0.05, -0.02, 0.01, 0.09, 0.16, 0.26, 0.42, 0.65, 0.91, 1.07, 0.98, 0.45, -0.19, -0.49, -0.53, -0.49, 
                -0.41, -0.36, -0.34, -0.34, -0.34, -0.33, -0.33, -0.32, -0.32, -0.33, -0.32, -0.32, -0.31, -0.31, -0.29, -0.29, 
                -0.27, -0.27, -0.29, -0.3, -0.3, -0.26, -0.24, -0.22, -0.21, -0.21, -0.2, -0.19, -0.18, -0.18, -0.16, -0.13, -0.11, 
                -0.11, -0.11, -0.1, -0.08, -0.07, -0.07, -0.1, -0.11, -0.11, -0.13, -0.14, -0.14, -0.13, -0.13, -0.12, -0.12, -0.11, 
                -0.09, -0.09, -0.08, -0.07, -0.08, -0.09, -0.08, -0.08, -0.01, -0.02, -0.02, -0.01, -0.01, -0.02, -0.02, -0.01, 
                -0.01, 0.0, 0.0, -0.01, -0.01, 0.0, 0.0, -0.02, -0.03, -0.02, -0.02, -0.05, -0.07, -0.05, -0.04, -0.04, -0.05, -0.07, 
                -0.07, -0.05, -0.07, -0.08, -0.05, -0.02, 0.01, 0.09, 0.16, 0.26, 0.42, 0.65, 0.91, 1.07, 0.98, 0.45, -0.19, -0.49, 
                -0.53, -0.49, -0.41, -0.36, -0.34, -0.34, -0.34, -0.33, -0.33, -0.32, -0.32, -0.33, -0.32, -0.32, -0.31, -0.31, 
                -0.29, -0.29, -0.27, -0.27, -0.29, -0.3, -0.3, -0.26, -0.24, -0.22, -0.21, -0.21, -0.2, -0.19, -0.18, -0.18, -0.16, 
                -0.13, -0.11, -0.11, -0.11, -0.1, -0.08, -0.07, -0.07, -0.1, -0.11, -0.11, -0.13, -0.14, -0.14, -0.13, -0.13, -0.12, 
                -0.12, -0.11, -0.09, -0.09, -0.08, -0.07, -0.08, -0.09, -0.08, -0.08, -0.01, -0.02, -0.02, -0.01));
        
        
        //below commented code reads data from excel file (ECG.xlsx) into an arraylist. this code being substituted with 
        //hardcoded data for deployment compatibility purposes (requires dependencies to run)
        /*
        try {

            FileInputStream excelFile = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();

            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();                    
                    dataArray.add(cell.getNumericCellValue());                    
                }                
            }
            workbook.close();
            excelFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        return dataArray;
    }

}
