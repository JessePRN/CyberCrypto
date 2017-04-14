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
            if (sum < 256) {
                safenumbers.add(i);
            }
            counter++;
        }//end for loop

        //System.out.print(arraylist);
    }

    public ArrayList<Integer> convert(ArrayList dataArray) {
        int arrayIndex = 0;
        long seed = System.nanoTime();
        Collections.shuffle(safenumbers, new Random(seed));

        for (int i = 0; i < dataArray.size(); i++) {
            if (!lookup.containsKey((dataArray.get(i)))) {
                lookup.put(dataArray.get(i), safenumbers.get(arrayIndex));
                arrayIndex++;
            }
        }

        //System.out.println(lookup);
        for (int i = 0; i < dataArray.size(); i++) {
            quantified.add((Integer) lookup.get(dataArray.get(i)));
        }

        //System.out.println(quantified);
        //System.out.println(quantified.size());
        return quantified;
    }

    public ArrayList<Double> deconvert(ArrayList inputArray) {

        ArrayList<Double> temp = new ArrayList<>();

        for (int i = 0; i < inputArray.size(); i++) {

            temp.add((Double) getKeyByValue(lookup, inputArray.get(i)));
        }
        return temp;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

}
