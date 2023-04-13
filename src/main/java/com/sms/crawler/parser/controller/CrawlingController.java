package com.sms.crawler.parser.controller;

import com.sms.crawler.parser.model.MergeResponse;
import com.sms.crawler.parser.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class CrawlingController {

    private final CrawlingService crawlingService;

    /***
     *
     * @param isCaching     캐싱으로 동작할지 여부 : 기본 True
     * @param targetURLs    대상URL 정보 : 기본은 현대 사이트 주소
     * @return              { Status : 응답코드 (200|206|500), Merge : 머지 결과 텍스트 }
     * @throws IOException
     */
    @GetMapping("/crawler")
    public ResponseEntity<MergeResponse> crawling(
            @RequestParam(required = false, defaultValue = "true") boolean isCaching,
            @RequestParam(required = false,
                    defaultValue = "https://shop.hyundai.com,https://www.kia.com,https://www.genesis.com")
            String[] targetURLs) throws IOException {

        MergeResponse response = crawlingService.crawlingURLs(targetURLs, isCaching);

        return ResponseEntity
                .status(response.getHttpStatus())
                .body(response);
    }
}
