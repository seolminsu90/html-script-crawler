package com.sms.crawler.parser.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ParseUtilTest {

    @Test
    @DisplayName("예제 결과값 파싱 테스트")
    void parseTest() {
        String parseData = "html124divABCDefgtaBleImg1";
        String assertSortAndDistinct = "AaBCDdefghIilmtv124";
        String assertResult = "Aa1B2C4DdefghIilmtv";

        String distinct = ParseUtil.distinct(parseData);
        String sortAndDistinct = ParseUtil.sortString(distinct, new WeightComparator());
        String result = ParseUtil.parse(sortAndDistinct);

        assertThat(sortAndDistinct).isEqualTo(assertSortAndDistinct);
        assertThat(result).isEqualTo(assertResult);
    }
}