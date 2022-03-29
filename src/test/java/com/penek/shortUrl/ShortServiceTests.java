package com.penek.shortUrl;

import com.penek.shortUrl.Service.ShortService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

@SpringBootTest
public class ShortServiceTests {
    @Autowired
    ShortService shortService;

    @Test
    @DisplayName("idx to Short, Short to idx 상호 테스트")
    void Short와_idx_상호테스트(){
        // Assertions.assertEquals();

        //given
        BigInteger testIdx1 = new BigInteger("1");
        BigInteger testIdx2 = new BigInteger("23123");
        BigInteger testIdx3 = new BigInteger("231232321093");
        BigInteger testIdx4 = new BigInteger("10000000000000");
        BigInteger testIdx5 = new BigInteger("10000000000000000000");
        BigInteger testIdx6 = new BigInteger("88145719387088629856");

        //when
        String test1_hangul = shortService.idxToShortUrl(testIdx1);
        BigInteger test1_idx = shortService.shortUrlToIdx(test1_hangul);

        String test2_hangul = shortService.idxToShortUrl(testIdx2);
        BigInteger test2_idx = shortService.shortUrlToIdx(test2_hangul);


        String test3_hangul = shortService.idxToShortUrl(testIdx3);
        BigInteger test3_idx = shortService.shortUrlToIdx(test3_hangul);

        String test4_hangul = shortService.idxToShortUrl(testIdx4);
        BigInteger test4_idx = shortService.shortUrlToIdx(test4_hangul);


        String test5_hangul = shortService.idxToShortUrl(testIdx5);
        BigInteger test5_idx = shortService.shortUrlToIdx(test5_hangul);

        String test6_hangul = shortService.idxToShortUrl(testIdx6);
        BigInteger test6_idx = shortService.shortUrlToIdx(test6_hangul);

        //then
        Assertions.assertEquals(testIdx1, test1_idx);
        Assertions.assertEquals(testIdx2, test2_idx);
        Assertions.assertEquals(testIdx3, test3_idx);
        Assertions.assertEquals(testIdx4, test4_idx);
        Assertions.assertEquals(testIdx5, test5_idx);
        Assertions.assertEquals(testIdx6, test6_idx);
    }
}
