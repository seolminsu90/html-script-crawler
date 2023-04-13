package com.sms.crawler.parser.service;

import com.sms.crawler.parser.model.CrawlerResponse;
import com.sms.crawler.parser.model.MergeResponse;
import com.sms.crawler.parser.util.ParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingService {

    private final AsyncService asyncService;

    /***
     * 대상목록을 크롤링한다.
     * 캐싱되어 동작하고, isCaching이 false일 경우에는 캐싱되지 않음
     * @param targetURLs 대상 목록
     * @param isCaching 캐싱 사용 여부
     * @return
     * @throws IOException
     */
    @Cacheable(
            value = "crawlResult",
            keyGenerator = "customKeyGenerator",
            condition = "#isCaching == true")
    public MergeResponse crawlingURLs(String[] targetURLs, boolean isCaching) throws IOException {

        CompletableFuture<CrawlerResponse>[] futures = new CompletableFuture[targetURLs.length];

        for (int i = 0; i < targetURLs.length; i++) {
            futures[i] = asyncService.getHtmlPageScript(targetURLs[i]);
        }

        List<CompletableFuture<CrawlerResponse>> futureList = Arrays.asList(futures);
        List<CrawlerResponse> combineResult = CompletableFuture
                .allOf(futures)
                .thenApply(result -> futureList
                        .stream()
                        .map(future -> future.join())
                        .collect(Collectors.toList()))
                .join();

        int successCount = 0;
        StringBuilder sb = new StringBuilder();
        for (CrawlerResponse result : combineResult) {
            if (result.isSuccess()) {
                sb.append(result.getScript());
                successCount++;
            }
        }

        // 전체 실패 : 500, 전체 성공 : 200, 일부 성공 206 처리
        int returnCode = (successCount == 0) ? 500 : (successCount == targetURLs.length) ? 200 : 206;
        return new MergeResponse(returnCode, ParseUtil.parse(sb.toString()));
    }
}
