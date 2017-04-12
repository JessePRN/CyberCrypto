/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cybercrypto;


import java.util.ArrayList;

/**
 *
 * @author jesse
 */
public class FormatData {

    ArrayList<Integer> intArray = new ArrayList<>();
    ArrayList<String> stringArray = new ArrayList<>();

    public FormatData() {

       intArray = new ArrayList<>();
       stringArray = new ArrayList<>();

    }

    public ArrayList<String> formatDoubleToString(ArrayList<Double> scanArray) {
        //converts doubles to integer values
        for (int i = 0; i < scanArray.size(); i++) {
            double x = (scanArray.get(i) * 100);
            int y = (int) x + 128;
            intArray.add(y);
        }

        //converting from integer array to string arrray containing bits
        for (int i = 0; i < intArray.size(); i++) {
            int x = intArray.get(i);
            String num = Integer.toBinaryString(x);

            if (num.length() < 8) {
                while (num.length() < 8) {
                    num = "0" + num;
                }
            }                      
            stringArray.add(num);
        }
        return stringArray;
    }
    
    public ArrayList<String> formatIntToString(ArrayList<Integer> intArrayinput) {
        
        //converting from integer array to string arrray containing bits
        for (int i = 0; i < intArrayinput.size(); i++) {
            int x = intArrayinput.get(i);
            String num = Integer.toBinaryString(x);

            if (num.length() < 8) {
                while (num.length() < 8) {
                    num = "0" + num;
                }
            }                    
            stringArray.add(num);
        }
        return stringArray;
    }
    
    public ArrayList<Integer> formatIntToBit(ArrayList<Integer> intArrayinput) {
        stringArray.clear();
        intArray.clear();
        
        for (int i = 0; i < intArrayinput.size(); i++) {
            int x = intArrayinput.get(i);
            String num = Integer.toBinaryString(x);

            if (num.length() < 8) {
                while (num.length() < 8) {
                    num = "0" + num;
                }
            }                    
            stringArray.add(num);
        }
        
        for(int i = 0; i < stringArray.size(); i++){
            for(int j = 0; j < stringArray.get(j).length(); j++){
                
                if(stringArray.get(i).charAt(j) == '1'){
                    intArray.add(1);                    
                }
                else intArray.add(0);                
            }
        }        
        return intArray;        
    }

}
