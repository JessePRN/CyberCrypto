/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cybercrypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;

/**
 * this class takes the raw EKG data values and converts them into friendly
 * integers. aside from being required for this project, it could theoretically
 * incorporate another layer of randomness/security. the Knapsack algorithm
 * involves adding numbers in the GK when encrypting, and in so doing values can
 * exceed 256. a byte of data (8 bits) can only store 256 numbers/permutations,
 * so we can't have that. this class checks what numbers encrypted with our GK
 * fall within our acceptable range, and puts those in a list to be mapped onto
 * the raw data points.
 *
 * @author jesse
 */
public class Quantizer {

    Map lookup;
    ArrayList<Integer> safenumbers;
    ArrayList<Integer> quantified;
    ArrayList<Double> dequantified;

    public Quantizer() {

        lookup = new HashMap();
        safenumbers = new ArrayList<>();
        quantified = new ArrayList<>();
        dequantified = new ArrayList<>();
        //int[] sik = new int[] {1,2,4,8,16,32,64,128};

        int[] gk = new int[]{17, 34, 68, 136, 16, 32, 64, 128};
        int counter = 0;
        //lets test for safe numbers. we convert integers to binary strings, and pretend like
        //they're going through knapsack encryption
        for (int i = 0; i < 256; i++) {
            String temp = Integer.toBinaryString(i);
            int sum = 0;
            while (temp.length() < 8) {
                temp = "0" + temp;
            }

            for (int j = 0; j < 8; j++) {
                if (temp.charAt(j) == '1') {
                    sum += gk[j];
                }
            }
            //if sum is below 256, lets add it to our safelist
            if (sum < 256) {
                safenumbers.add(i);
            }
            counter++; //for testing
        }//end for loop

        //System.out.print(arraylist);
    }

    public ArrayList<Integer> convert(ArrayList dataArray) {
        int arrayIndex = 0;
        long seed = System.nanoTime(); //our random seed
        Collections.shuffle(safenumbers, new Random(seed)); //lets randomize that list of safenumbers so cracking them is less predictable
        
        //for each value in an array, lets see if its in our lookup map. if it isn't, map it onto a random safenumber
        //if its already mapped, just skip
        for (int i = 0; i < dataArray.size(); i++) {
            if (!lookup.containsKey((dataArray.get(i)))) {
                lookup.put(dataArray.get(i), safenumbers.get(arrayIndex));
                arrayIndex++;
            }
        }

        //now that every raw data point is mapped to a safenumber, lets run through that array one more time and dynamically create
        //a new one with the corresponding mapped values. this will be the array that gets encrypted/decrypted
        for (int i = 0; i < dataArray.size(); i++) {
            quantified.add((Integer) lookup.get(dataArray.get(i)));
        }      
        return quantified;
    }

    //the opposite of converting, this method takes the array of safenumber and remaps them to their original data points
    public ArrayList<Double> deconvert(ArrayList inputArray) {

        ArrayList<Double> temp = new ArrayList<>();

        for (int i = 0; i < inputArray.size(); i++) {
            temp.add((Double) getKeyByValue(lookup, inputArray.get(i)));
        }
        return temp;
    }

    //maps are meants to be queried via their key variables, not the value variables. this complex method allows
    //just that by searching the map for a value (quantized safe number version) and returning the corresponding key (raw ekg data point)
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
