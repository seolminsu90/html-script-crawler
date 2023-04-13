package com.sms.crawler.parser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CrawlerResponse {
    private boolean isSuccess;
    private String script;

    public CrawlerResponse(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public CrawlerResponse(boolean isSuccess, String script) {
        this.isSuccess = isSuccess;
        this.script = script;
    }
}
