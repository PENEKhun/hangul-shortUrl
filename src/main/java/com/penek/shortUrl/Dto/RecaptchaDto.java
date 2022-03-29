package com.penek.shortUrl.Dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RecaptchaDto {
    boolean success;
    double score;
    Timestamp challenge_ts;
    String hostname;

}
