package com.szleyi365.paymmtest;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * Created by shuaijiman on 12/25/15.
 */
public class Utils {
    public static String createSign(String charaEncoding, SortedMap<Object, Object> parameters) {
        StringBuilder stringBuilder = new StringBuilder();
        Set es = parameters.entrySet();
        Iterator iterator = es.iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            String k = (String)entry.getKey();
            Object o = entry.getValue();
            if (null != o && !"".equals(o) && !"sign".equals(k)
                    && !"key".equals(k)) {
                stringBuilder.append(k + "=" + o + "&");
            }
        }
        stringBuilder.append("key="+ MMPayConstants.APP_PAY);
        try {
            String sign = MD5.getMessageDigest(stringBuilder.toString().getBytes(charaEncoding));
            return sign.toUpperCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
