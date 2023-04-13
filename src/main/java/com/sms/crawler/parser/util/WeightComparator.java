package com.sms.crawler.parser.util;

import java.util.Comparator;

public class WeightComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        float a = setNewWeight(o1.charAt(0));
        float b = setNewWeight(o2.charAt(0));
        return (a == b) ? 0 : (a > b) ? 1 : -1;
    }

    /**
     * 아스키 코드값을 기준으로 대문자 -> 소문자 -> 숫자 순으로 정렬되게 한다.
     * @param c
     * @return
     */
    private float setNewWeight(char c) {
        if (c >= 65 && c <= 90) return c + 31.5F;       // 대문자
        else if (c >= 48 && c <= 57) return c + 75F;    // 숫자
        else return c; // 97-122                        // 소문자
    }
}