/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cybercrypto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/*****************
* class variables include 3 registers (shifting arrays) and array lists that contain different elements of
* a51 (the queue, the keystream, the plaintext, and ciphertext.

* when encrypting, the plaintext is passed into the class constructor, along with the initiation vector which
* populates the queue and registers. Every cycle/pulse generates a keystream bit. After 8 pulses, the resulting
* 8 bit keystream is XOR'ed with 8 bits of plaintext to create 8 bits of ciphertext. That ciphertext is then copied
* into the right-most 8 bits of the queue, and the rest of the queue is shifted left 8 times.
***************/

public class A51 {

    Register regX;
    Register regY;
    Register regZ;
    
    ArrayList<Integer> keystream;
    ArrayList<Integer> queue;
    ArrayList<Integer> plaintext;
    ArrayList<Integer> ciphertext;

    //these values used for testing. they generate a default a51 object if no params are passed
    int xSample = 349525;
    int ySample = 3355443;
    int zSample = 7401712;


    public A51() {

        regX = new Register(19, xSample);
        regY = new Register(22, ySample);
        regZ = new Register(23, zSample);

    }

    public A51(String stringInitVector, ArrayList<Integer> plain) {

        plaintext = plain;
        keystream = new ArrayList<Integer>();
        queue = new ArrayList<Integer>();
        ciphertext = new ArrayList<Integer>();
   
        //this method passes the IV into the queue
        queueStringInit(stringInitVector);

        int[] x = new int[19];
        int[] y = new int[22];
        int[] z = new int[23];

        //formatting to convert the passed String IV into 3 arrays to create the 3 registers
        String[] stringArray = stringInitVector.split("");
        for (int i = 0; i < 19; i++) {
            x[i] = Integer.parseInt(stringArray[i]);
        }
        for (int i = 0; i < 22; i++) {
            y[i] = Integer.parseInt(stringArray[i + 19]);
        }
        for (int i = 0; i < 23; i++) {
            z[i] = Integer.parseInt(stringArray[i + 41]);
        }

        //creating registers. see Register class below
        regX = new Register(19, x);
        regY = new Register(22, y);
        regZ = new Register(23, z);
    }   

    //the majority method, used to discern which registers get shifted each encryption pulse
    int majority(int x, int y, int z) {
        if ((x == 0 && y == 0) || (y == 0 && z == 0) || (x == 0 && z == 0)) {
            return 0;
        }
        return 1;
    }

    //based on majoriy values, approrpriate registers get shifted. the cipheroutput bit is also produced and
    //added to the keystream.
    int pulse() {
        
        int m = majority(regX.get(8), regY.get(10), regZ.get(10));
        //System.out.println("M: " + m);
        int cipherOutput = (regX.get(18) ^ regY.get(21) ^ regZ.get(22));

        if (m == regX.get(8)) {
            int t = regX.get(13) ^ regX.get(16) ^ regX.get(17) ^ regX.get(18);
            regX.push(t);
        }
        if (m == regY.get(10)) {
            int t = regY.get(20) ^ regY.get(21);
            regY.push(t);
        }

        if (m == regZ.get(10)) {
            int t = regZ.get(7) ^ regZ.get(20) ^ regZ.get(21) ^ regZ.get(22);
            regZ.push(t);
        }

        keystream.add(cipherOutput);
        return cipherOutput;
    }
    
    //XOR method. takes the last 8 bits of the keystream and XOR's with respective bits from plaintext
    ArrayList<Integer> xor(ArrayList<Integer> plaintext, ArrayList<Integer> keytext){
        
        ArrayList<Integer> solution = new ArrayList<>();     
        
        int keyIndex = keytext.size();        
        
        for (int i = 8; i > 0; i--){
            solution.add(plaintext.get(keyIndex - i) ^ keytext.get(keyIndex - i));
        }        
        
        return solution;        
    }

    //prints array contents
    static void intArrayPrint(Register reg) {

        int[] temp = reg.getArray();
        for (int i = 0; i < temp.length; i++) {
            System.out.print(temp[i]);
        }
        System.out.println("");
    }
   
    //used for testing, a method to simplify printing register contents
    void registerPrint() {
        intArrayPrint(regX);
        intArrayPrint(regY);
        intArrayPrint(regZ);
    }

    //initiates IV into queue
    void queueStringInit(String s) {

        String[] stringArray = s.split("");
        for (int i = 0; i < stringArray.length; i++) {
            queue.add(Integer.parseInt(stringArray[i]));
        }
    }
    
    //setter
    void setPlaintext(ArrayList<Integer> plaintext){
        this.plaintext = plaintext;
    }

    //this method reflects the cipher-feedback process in our algorithm. after pulsing 8 times, this method
    //is called to XOR the keystream with plaintext, and copy resulting ciphertext into the queue
    void update() {

        //shifting queue left 8 units
        for (int i = 0; i < 56; i++) {
            queue.set(i, queue.get(i + 8));
        }
                
        //adding results of plaintext XOR keystream to ciphertext
        ciphertext.addAll(xor(plaintext, keystream));
        
        //takes last 8 bits of ciphertext and copies to right 8 bits in queue               
        int ciphertextIndexOfLast = ciphertext.size();

        for (int i = ciphertextIndexOfLast - 8; i < ciphertextIndexOfLast; i++) {
            queue.set((i - ((ciphertextIndexOfLast - 8)) + 56), ciphertext.get(i));
        }
        
        //using the new queue, populates registers with new data
        resetRegisters((ArrayList<Integer>) queue);
    }

    //this method passes in new queue after an update and updates registers
    void resetRegisters(ArrayList<Integer> arraylist) {

        int[] x = new int[19];
        for (int i = 0; i < 19; i++) {
            x[i] = arraylist.get(i);
        }
        int[] y = new int[22];
        for (int i = 0; i < 22; i++) {
            y[i] = arraylist.get(i + 19);
        }
        int[] z = new int[23];
        for (int i = 0; i < 23; i++) {
            z[i] = arraylist.get(i + 41);
        }
        regX.set(x);
        regY.set(y);
        regZ.set(z);

    }

    //class that defines behavior of registers
    class Register {

        int[] registerArray;

        //register constructor for an array size and value in decimal
        public Register(int size, int decimalData) {

            registerArray = new int[size];
            String stringBinary = Integer.toBinaryString(decimalData);
            while(stringBinary.length() < size){
                stringBinary = "0" + stringBinary;
            }
            String[] stringArray = stringBinary.split("");

            for (int i = 0; i < stringArray.length; i++) {
                registerArray[i] = Integer.parseInt(stringArray[i]);
            }
        }
        
        //alternative constructor using size and integer array
        public Register(int size, int[] intArrData) {

            registerArray = new int[size];

            for (int i = 0; i < intArrData.length; i++) {
                registerArray[i] = intArrData[i];
            }
        }

        //when registers pulsing, this method pushes in new value and shifts array
        void push(int t) {

            for (int i = registerArray.length - 1; i > 0; i--) {
                registerArray[i] = registerArray[i - 1];
            }
            registerArray[0] = t;
        }

        int[] getArray() {
            return registerArray;
        }

        int get(int i) {
            return registerArray[i];
        }

        void set(int[] intArray) {
            registerArray = intArray;
        }
    }
}





/*


/*
    public static void main(String[] arg) {

        //a.registerPrint();
        //a.pulse();
        String xdata = "1010101010101010100";
        String ydata = "1100110011001100110010";
        String zdata = "11100011100011100011100";

        String iv = xdata + ydata + zdata;
        System.out.println("iv is : " + iv);
        //String iv = "1110101101000010011110111000111010001100010101011111110110101001";

        //System.out.println(stringArray.length);
        /*TestJavaStuff a = new TestJavaStuff(iv);
        a. registerPrint();
        for (int i = 0; i < 3; i++) {
            System.out.println(a.pulse());
            a.registerPrint();
        }
        */
    
        
        
        
        //System.out.println(iv.length());
        /* a.registerPrint();
        int pulseAmount = 8;

        for (int i = 0; i < pulseAmount; i++) {

            System.out.println("C : " + a.pulse());
            a.registerPrint();
        }

        //System.out.println(Arrays.toString(a.ciphertext.toArray()));
        //a.update();
        System.out.println(a.queue);
        System.out.println(a.ciphertext);
        a.update();
        System.out.println(a.queue);
        a.registerPrint();

        //intArrayPrint(a.regZ); 
    }  */

