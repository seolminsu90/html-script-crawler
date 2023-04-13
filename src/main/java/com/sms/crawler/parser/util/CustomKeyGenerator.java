package com.sms.crawler.parser.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class CustomKeyGenerator implements KeyGenerator {

    /**
     * TargetURLs 기준으로 동일한 경로 요청 시 캐싱되도록 한다.
     * @param target
     * @param method
     * @param params
     * @return
     */
    public Object generate(Object target, Method method, Object... params) {

        StringBuilder sb = new StringBuilder();
        for (Object param : params) {
            if (param instanceof String[]) {
                String[] targetURLs = (String[]) param;
                Arrays.sort(targetURLs);
                for (String s : targetURLs) {
                    sb.append(s).append("__");
                }
                break;
            }
        }

        return sb.toString();
    }
}
