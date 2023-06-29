package com.penek.shortUrl.Dto;

import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Getter
@Builder
@AllArgsConstructor
public class RequestInformation {

  String ip;
  String userAgent;
  String referer;

  public static RequestInformation parse() {
    HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String ip = req.getHeader("X-FORWARDED-FOR");
      if (ip == null) {
          ip = req.getRemoteAddr();
      }
    String userAgent = req.getHeader("User-Agent");
    String referer = req.getHeader("Referer");

    return RequestInformation.builder().ip(ip)
        .userAgent(userAgent)
        .referer(referer).build();
  }
}

