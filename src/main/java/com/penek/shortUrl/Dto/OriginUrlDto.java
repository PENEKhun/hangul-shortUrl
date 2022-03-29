package com.penek.shortUrl.Dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class OriginUrlDto {
    BigInteger idx;
    String url;
}
