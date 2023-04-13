package com.sms.crawler.parser.util;

import java.util.Arrays;
import java.util.Comparator;

public class ParseUtil {
    /**
     * 예상 결과 정리
     * 1. AaBCDdefIi12456789 => Aa1B2C4Dd5e6f7Ii89 숫자가 남는 경우...
     * 2. AaBCDdefghIilmtv124 => Aa1B2C4DdefghIilmtv // 문자가 남는 경우...
     * 3. 2345678 => 12345678 // 숫자만 있다
     * 4. abcdefghkxz => abcdefghkxz // 문자만 있다.
     */

    /**
     * 결과값 파싱 프로세스
     *
     * @param mergeStr 크롤링 후 머지 된 문자열
     * @return
     */
    public static String parse(String mergeStr) {
        if (mergeStr.length() == 0) return "";
        String aZDigitStr = mergeStr.replaceAll("[^a-zA-Z0-9]", "");
        String distinctStr = distinct(aZDigitStr);
        String sortStr = sortString(distinctStr, new WeightComparator());
        return crossPrint(sortStr);
    }

    /**
     * 교차 출력
     *
     * @param s 정렬 및 중복제거 된 텍스트
     * @return
     */
    private static String crossPrint(String s) {
        if (s.length() == 0 ||
                !Character.isDigit(s.charAt(s.length() - 1)) ||  // 마지막이 숫자가 아닐 경우 모두 문자열
                Character.isDigit(s.charAt(0))) {  // 처음이 숫자일 경우 모두 숫자
            return s;
        }

        int numberEndIdx = -1;
        for (int i = s.length() - 1; i > 0; i--) {
            if (!Character.isDigit(s.charAt(i))) { // 숫자가 끝나는 인덱스 기록
                numberEndIdx = i + 1;
                break;
            }
        }

        String alphabetPart = s.substring(0, numberEndIdx);
        String numberPart = s.substring(numberEndIdx);

        int nIdx = 0, aIdx = 0, step = 0; // 숫자 인덱스, 알파벳 인덱스, 교차 단계
        StringBuilder sb = new StringBuilder();
        while (aIdx < alphabetPart.length() || nIdx < numberPart.length()) {
            // 두 파트 중 먼저 소모된 파트가 생길 시 나머지는 정렬된 상태 그대로 붙인다.
            if (nIdx >= numberPart.length()) {
                sb.append(alphabetPart.substring(aIdx));
                break;
            } else if (aIdx >= alphabetPart.length()) {
                sb.append(numberPart.substring(nIdx));
                break;
            }

            // 대문자 > 소문자 > [숫자] 순으로 넣어준다.
            char c = alphabetPart.charAt(aIdx);
            if ((step == 0 && Character.isUpperCase(c)) ||
                    (step == 1 && Character.isLowerCase(c))) {
                sb.append(c);
                aIdx++;
            } else if (step == 2 && nIdx < numberPart.length()) {
                sb.append(numberPart.charAt(nIdx++));
            }
            step = (step < 2) ? step + 1 : 0;
        }

        return sb.toString();
    }

    /**
     * Comparator 기준으로 정렬 처리
     *
     * @param s          대상텍스트
     * @param comparator comparator
     * @return
     */
    private static String sortString(String s, Comparator comparator) {
        String[] arr = s.split("");
        Arrays.sort(arr, comparator);
        return String.join("", arr);
    }

    /**
     * 중복 제거
     *
     * @param s 대상텍스트
     * @return
     */
    private static String distinct(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (sb.indexOf(String.valueOf(c)) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
