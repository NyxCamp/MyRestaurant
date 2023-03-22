package com.myRestaurant.terminal;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        String s1 = "0001";
        String s2 = "0011";
        String s3 = "0111";
        System.out.println(Integer.parseInt(s1));
        System.out.println(Integer.parseInt(s2));
        System.out.println(Integer.parseInt(s3));
    }


    public static void main01(String[] args) {
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String now = yyyyMMdd.format(date);
        System.out.println(now);
        int i = 1;
        int ii = 11;
        int iii = 111;
        System.out.println(now + String.format("%04d", i++));
        System.out.println(now + String.format("%04d", i++));
        System.out.println(now + String.format("%04d", i++));
        System.out.println(now + String.format("%04d", i++));
        System.out.println(now + String.format("%04d", i++));
        String test = "101 102 103 ";
        String[] s = test.split(" ");
        for (String s1 : s) {
            System.out.println("'" + s1 +"'");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < 10; j++) {
            stringBuilder.append(j);
            stringBuilder.append(" ");
        }
        System.out.println(stringBuilder);
    }
}
