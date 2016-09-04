package wtsang01.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WTRandom {
    public static Random generator = new Random();
    private static Map<Integer, String> map;
    static {
        Map<Integer, String> aMap = new HashMap<Integer, String>();
        aMap.put(0, "-");
        aMap.put(1, "A");
        aMap.put(2, "B");
        aMap.put(3, "C");
        aMap.put(4, "D");
        aMap.put(5, "E");
        aMap.put(6, "F");
        aMap.put(7, "G");
        aMap.put(8, "H");
        aMap.put(9, "I");
        aMap.put(10, "J");
        aMap.put(11, "K");
        aMap.put(12, "L");
        aMap.put(13, "M");
        aMap.put(14, "N");
        aMap.put(15, "O");
        aMap.put(16, "P");
        aMap.put(17, "Q");
        aMap.put(18, "R");
        aMap.put(19, "S");
        aMap.put(20, "T");
        aMap.put(21, "U");
        aMap.put(22, "V");
        aMap.put(23, "W");
        aMap.put(24, "X");
        aMap.put(25, "Y");
        aMap.put(26, "Z");
        aMap.put(27, "0");
        aMap.put(28, "1");
        aMap.put(29, "2");
        aMap.put(30, "3");
        aMap.put(31, "4");
        aMap.put(32, "5");
        aMap.put(33, "6");
        aMap.put(34, "7");
        aMap.put(35, "8");
        aMap.put(36, "9");
        map = Collections.unmodifiableMap(aMap);
    }

    public static int getRandomIntWithRange(int low,int high){
        return  generator.nextInt(high-low) + low;
    }
    public static double getRandomDoubleWithRange(double low,double high){
        return  generator.nextDouble()*high + low;
    }

    public static int check(ArrayList<Integer> list, int num) {
        while (list.contains(num)) {
            num = generator.nextInt(4);
        }
        return num;
    }


    public static String genUCode() {
        StringBuilder sb = new StringBuilder();
        String firstLetter = map.get(generator.nextInt(27) + 1);
        sb.append(firstLetter);
        for (int i = 0; i < 5; i++) {
            sb.append(map.get(generator.nextInt(27)));
        }
        sb.append(map.get(0));
        for (int i = 0; i < 15; i++) {
            sb.append(generator.nextInt(10));
        }
        return sb.toString();
    }

    public static String getRanUpLetter() {
        String letter = map.get(generator.nextInt(26) + 1);
        return letter;

    }

    /**
     * @return a int number from 0-9
     */
    public static String getRanNumber() {
        return String.valueOf(generator.nextInt(10));

    }

    public static String getRandomChar() {
        String letter = map.get(generator.nextInt(36) + 1);
        return letter;
    }

    public static String getRandomChar(int length) {
        StringBuilder sb=new StringBuilder();
        for (int x = 0; x < length; x++) {
            sb.append((char) ((int) (Math.random() * 26) + 97));
        } // uses ISO/IEC 8859-1 coding.
        return sb.toString();
    }

    /**
     * @param length length of the number returned
     * @return a int number at a given length
     */
    public static int getRanNumber(int length){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<length;i++)
            sb.append(getRanNumber());
        int returnInt=Integer.parseInt(sb.toString());
        return returnInt;

    }
}