package cybercrypto;

import java.util.ArrayList;

/**
 * this entire class is just used to assist with formatting. Throughout a demonstration, data starts from being scanned in from the excel file and constantly
 * gets converted from an arraylist of Doubles (-.5, 2.3) to quantized integers (32, 123), to Strings bytes/binary ("10101110") to integer arraylists ( [23, 42, 16] )
 * to other integer arraylists representing bits ( [0,1,1,0,1,1,1,0] )
 * 
 * @author jesse
 */
public class FormatData {

    ArrayList<Integer> intArray = new ArrayList<>();
    ArrayList<String> stringArray = new ArrayList<>();

    //though not technically a static class, it basically behaves like one as methods are just used to 
    //manipulate data. nothing much is actually stored here
    public FormatData() {

    }

    //converts an arraylist of doubles into an arraylist of String bits. notice the padding so that converting integers, say
    //the number 3 to a byte is represented as 00000011 and not just 11
    public ArrayList<String> formatDoubleToString(ArrayList<Double> scanArray) {
        intArray = new ArrayList<>();
        stringArray = new ArrayList<>();

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
            //padding
            if (num.length() < 8) {
                while (num.length() < 8) {
                    num = "0" + num;
                }
            }
            stringArray.add(num);
        }
        return stringArray;
    }

    //converts arraylist<Integer> to arraylist<String> representing binary (e.g. "10010110"
    public ArrayList<String> formatIntToString(ArrayList<Integer> intArrayinput) {
        stringArray = new ArrayList<>();

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

    //converts arraylist of regular integers (32, 42) to much larger arraylist of integers representing
    //each previous integer as 8 bits. e.g. 3 will become [0,0,0,0,0,0,1,1]
    public ArrayList<Integer> formatIntToBit(ArrayList<Integer> intArrayinput) {
        intArray = new ArrayList<>();
        stringArray = new ArrayList<>();

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
        for (int i = 0; i < stringArray.size(); i++) {
            for (int j = 0; j < stringArray.get(j).length(); j++) {

                if (stringArray.get(i).charAt(j) == '1') {
                    intArray.add(1);
                } else {
                    intArray.add(0);
                }
            }
        }
        return intArray;
    }

    //converts string representation of bits into regular integers ( "00000011" -> 3 )
    public ArrayList<Integer> formatStringToInt(ArrayList<String> stringArray) {

        ArrayList<Integer> intArray = new ArrayList<>();

        for (int i = 0; i < stringArray.size(); i++) {
            intArray.add(Integer.parseInt(stringArray.get(i), 2));
        }

        return intArray;
    }

    //takes big arraylist of integers representing bits and converts to consolidated arraylist of string binaries
    //eg [0, 0, 1, 1, 0, 0, 1, 1] -> "00110011" 
    public ArrayList<String> formatBytesToStringArray(ArrayList<Integer> arrayBits) {

        ArrayList<String> stringArray = new ArrayList<>();
        String tempString = "";
        for (int i = 0; i < arrayBits.size(); i = i + 8) {
            for (int j = 0; j < 8; j++) {
                tempString += String.valueOf(arrayBits.get(j + i));
            }
            stringArray.add(tempString);
            tempString = "";
        }
        return stringArray;
    }
}
