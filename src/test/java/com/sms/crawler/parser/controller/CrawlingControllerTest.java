package com.sms.crawler.parser.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.spec.internal.HttpStatus;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 8090)
public class CrawlingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * 목 서버 테스트
     * 해당 경로 응답은 "<html><body><img src='Apple.png'/>Public Static void main 900103 Minsu</body></html>" 임 (mock_get.json)
     * @throws Exception
     */
    @Test
    @DisplayName("Mockserver 테스트")
    void crawlingMock() throws Exception {
        ResultActions actions =
                mockMvc.perform(
                        get("/api/crawler")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .param("targetURLs", "http://localhost:8090/sample-url"));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("Status").value(200))
                .andExpect(jsonPath("Merge").value("Aa0b1c3d9eghilMmnoPprSstuvy"));

    }

    @Test
    @DisplayName("크롤링 기본 테스트")
    void crawlingOK() throws Exception {
        ResultActions actions =
                mockMvc.perform(
                        get("/api/crawler")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8"));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("Status").value(200));
    }

    @Test
    @DisplayName("크롤링 일부 실패 테스트")
    void crawling206() throws Exception {
        ResultActions actions =
                mockMvc.perform(
                        get("/api/crawler")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .param("targetURLs", "https://www.kia.com,https://3474375657.com"));

        actions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("Status").value(206));
    }

    @Test
    @DisplayName("크롤링 전부 실패 테스트")
    void crawling500() throws Exception {
        ResultActions actions =
                mockMvc.perform(
                        get("/api/crawler")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .param("targetURLs", "https://www.34743756573.com,https://3474375657.com"));

        actions
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("Status").value(500));
    }
}