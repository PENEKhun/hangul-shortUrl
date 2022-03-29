package com.penek.shortUrl.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class LogDto {

    @Getter @Setter
    @Builder
    @AllArgsConstructor
    public static class dataCollection{
        String hangul;
        String ip;
        String userAgent;
        String referer;
    }
}
