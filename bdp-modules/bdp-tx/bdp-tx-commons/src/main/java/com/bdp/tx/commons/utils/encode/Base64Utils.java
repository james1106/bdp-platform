package com.bdp.tx.commons.utils.encode;

import org.apache.axis.encoding.Base64;


public class Base64Utils {

    public static String encode(byte[] bs){
        return Base64.encode(bs);
    }

    public static byte[] decode(String str){
        return Base64.decode(str);
    }


}
