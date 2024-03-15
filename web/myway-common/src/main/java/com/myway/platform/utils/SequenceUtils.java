package com.myway.platform.utils;

public class SequenceUtils {

    public static String getSequence(int defaultLength, long seq) {
        String str = String.valueOf(seq);
        int len = str.length();
        if (len >= defaultLength) {// 取决于业务规模,应该不会到达3
            return str;
        }
        int rest = defaultLength - len;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rest; i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }
}
