package com.penek.shortUrl;

import com.penek.shortUrl.Service.ShortService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShortServiceTests {
    @Autowired
    ShortService shortService;

    @Test
    @DisplayName("idx to Short, Short to idx 상호 테스트")
    void 상호테스트(){
       // Assertions.assertEquals();

        //given

        //when
        long test1 = shortService.shortUrlToIdx("Avwixsux2");


        //then
        Assertions.assertEquals(shortService.shortUrlToIdx(shortService.idxToShortUrl(test1)), test1);
    }
}
