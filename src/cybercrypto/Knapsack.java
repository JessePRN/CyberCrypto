/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cybercrypto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.ListIterator;


public class Knapsack {

    int[] PK = new int[8];
    int n = 256;
    int m = 17;
    int[] GK = new int[8];
    BigInteger[] bigPK = new BigInteger[8];

    public Knapsack() {

        //hard-coded private and general keys
        PK = new int[] {1, 2, 4, 8, 16, 32, 64, 128};
        GK = new int[] {17,34,68,136,16,32,64,128};
        
        
        //equivalent BigInteger array for PK, used for some Math methods
        for (int x = 0; x < 8; x++) {
            bigPK[x] = BigInteger.valueOf(PK[x]);
        }     
    }

    //encryption method. takes in an arraylist of bytes in string-binary format. based on which bits are true, 
    //corresponding number of that index in general key is added to sum. the sum is calculated and then converted
    //to a byte in string-binary notation
    ArrayList<String> encrypt(ArrayList<String> stringArray) {

        ArrayList<String> encryptedArray = new ArrayList<>();
        ArrayList<Integer> intArray = new ArrayList<>();

        //for each string of 8 bits in the arraylist
        for (int i = 0; i < stringArray.size(); i++) {

            int sum = 0;
            String byteString;

            byteString = stringArray.get(i);
            //checks every bit. if true (=='1'), its respective GK value added to sum
            for (int j = 0; j < 8; j++) {

                if (byteString.charAt(j) == '1') {
                    sum += GK[j];

                }
            }
            intArray.add(sum);
        }
        
        //this for loop converts the integer array calculated above into a string-binary
        //representation
        for (int i = 0; i < intArray.size(); i++) {
            int x = intArray.get(i);
            String num = Integer.toBinaryString(x);

            //padding incase the binary representation isn't 8 bits long
            if (num.length() < 8) {
                
                while (num.length() < 8) {
                    num = "0" + num;
                }
            }
            //padding incase binary representation is over 8 bits
            if (num.length() > 8) {
                num = num.substring(num.length() - 8, num.length());
            }
            
            encryptedArray.add(num);
        }       
        return encryptedArray;

    }//end encrypt method

    
    ArrayList<String> decrypt(ArrayList<String> encryptedArray) {

        BigInteger bigN = BigInteger.valueOf(n);
        BigInteger bigM = BigInteger.valueOf(m);
        ArrayList<String> decryptedArray = new ArrayList<>();
        BigInteger invMod;

        for (int i = 0; i < encryptedArray.size(); i++) {

            int intValue = Integer.parseInt(encryptedArray.get(i), 2);
            BigInteger cipher = BigInteger.valueOf(intValue);
            invMod = (cipher.multiply(bigM.modInverse(bigN))).mod(bigN);

            int startIndex = PK.length - 1;
            String plain = "";

            while (startIndex >= 0) {

                // In this case, DON'T add this number in the subset, so we get a
                // 0 for this bit of the plantext.
                if (invMod.compareTo(bigPK[startIndex]) < 0) {
                    plain = "0" + plain;
                } // Here we get a 1 for the plaintext bit
                else {

                    plain = "1" + plain;
                    invMod = invMod.subtract(bigPK[startIndex]);
                }

                // Go to figure out the next bit.
                startIndex--;
            }
            decryptedArray.add(plain);
        }
        return decryptedArray;
    }//end decrypt method  
}



/***

dynamic way to generate SIK
PK[0] = 1;
for (int b = 1; b < 8; b++) {
    PK[b] = PK[b - 1] * 2;
}

dynamic way to generate GK
for (int i = 0; i < 8; i++) {
    GK[i] = (PK[i] * m) % n;
    //GK[i] = m.multiply(SIK[i]).mod(n);
}
 

 ***/
