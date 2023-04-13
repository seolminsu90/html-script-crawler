package com.sms.crawler.parser.service;

import com.sms.crawler.parser.model.CrawlerResponse;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncService {

    /**
     * 크롤링 비동기 병렬 처리
     * 예외 발생시 1초 간격으로 3회 까지 재시도 한다, 실패 시 Recover 처리된다.
     * ConnectionTimeout 5초
     * @param url 대상 경로
     * @return
     * @throws IOException
     */
    @Async
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public CompletableFuture<CrawlerResponse> getHtmlPageScript(String url) throws IOException {
        int retry = RetrySynchronizationManager.getContext().getRetryCount();
        log.info("크롤링 대상 경로 : [{}], 재시도 횟수 : {}", url, retry);

        Document doc = Jsoup.connect(url).timeout(5000).get();
        String script = doc.toString();

        log.info("크롤링 완료 : [{}]", url);

        return CompletableFuture.completedFuture(new CrawlerResponse(true, script));
    }

    /**
     * 크롤링 실패시 Fallback 응답
     * @param e
     * @param url
     * @return
     */
    @Recover
    public CompletableFuture<CrawlerResponse> getHtmlPageScriptFallBack(Exception e, String url) {
        log.error("크롤링 실패. [{}] - {}", url, e.getMessage());
        return CompletableFuture.completedFuture(new CrawlerResponse(false));
    }
}
