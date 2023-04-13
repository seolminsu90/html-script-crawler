package com.sms.crawler.parser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class MergeResponse implements Serializable {
    @JsonProperty("Status")
    private int status;
    @JsonProperty("Merge")
    private String merge;

    @JsonIgnore
    public HttpStatus getHttpStatus() {
        switch (status) {
            case 200:
                return HttpStatus.OK;
            case 206:
                return HttpStatus.PARTIAL_CONTENT;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
