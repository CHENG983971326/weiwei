package com.neuedu.utils;
import java.util.Date;

public class OrderNo {
    public static String toOrderNo(){
        String s="2019";
          Long l= new Date().getTime();
          return s+l;

    }
}
