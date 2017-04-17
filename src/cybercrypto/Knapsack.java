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
    int n = 256; //modulus
    int m = 17; //multiplier
    int[] GK = new int[8];
    //bigInteger form of PK used for inverse mod calculations
    BigInteger[] bigPK = new BigInteger[8];
    ArrayList<String> lastEncryptedData;

    public Knapsack() {
        //pk = private key, gk = public key. though hardcoded in this project, gk is generally calculated via the 
        //pk and n and m  ( GK[i] = m * PK[i] mod (n) 
        //dynamic way to generate GK ->  for(int i = 0; i < 8; i++) { GK[i] = (PK[i] * m) * % n; //GK[i] = m.multiply(SIK[i]).mod(n); }
        
        PK = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
        GK = new int[]{17, 34, 68, 136, 16, 32, 64, 128};

        //equivalent BigInteger array for PK, used for some Math methods
        for (int x = 0; x < 8; x++) {
            bigPK[x] = BigInteger.valueOf(PK[x]);
        }
    }

    //encryption method. takes in an arraylist of bytes in string-binary format (e.g. 01010101). based on which bits are true (1), 
    //corresponding number of that index in general key is added to a sum. once the sum is calculated it is then converted
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
            //padding incase binary representation is over 8 bits, should never happen, would cause data loss
            if (num.length() > 8) {
                num = num.substring(num.length() - 8, num.length());
            }
            encryptedArray.add(num);
        }
        //this random variable useful to make sure eventual decrypting occurs on the most recently generated encrypted data. In the 
        //GUI, users can generate multiple knapsack data sets, each with their own lookup value (see quantizer and controller classes)
        lastEncryptedData = encryptedArray;
        return encryptedArray;

    }//end encrypt method

    ArrayList<String> decrypt(ArrayList<String> encryptedArray) {

        BigInteger bigN = BigInteger.valueOf(n);
        BigInteger bigM = BigInteger.valueOf(m);
        ArrayList<String> decryptedArray = new ArrayList<>();
        BigInteger invMod;

        //some math to decrypt
        for (int i = 0; i < encryptedArray.size(); i++) {

            int intValue = Integer.parseInt(encryptedArray.get(i), 2);
            BigInteger cipher = BigInteger.valueOf(intValue);
            invMod = (cipher.multiply(bigM.modInverse(bigN))).mod(bigN);

            int startIndex = PK.length - 1;
            String plain = "";

            //decomposing value over private key to get plaintext data back
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

    public ArrayList<String> getLastEncrypted() {

        return lastEncryptedData;

    }

}
