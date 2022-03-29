package com.penek.shortUrl.Controller;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.penek.shortUrl.Dto.LogDto;
import com.penek.shortUrl.Exception.CustomException;
import com.penek.shortUrl.Exception.ErrorCode;
import com.penek.shortUrl.Service.RecaptchaService;
import com.penek.shortUrl.Service.ShortService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShortController {
    private final ShortService shortService;
    private final RecaptchaService recaptchaService;

    @PostMapping("/make")
    public ResponseEntity makeURL(HttpServletRequest request) {

        String token = request.getParameter("token");
        String originUrl = request.getParameter("originUrl");


        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = req.getRemoteAddr();

        recaptchaService.throwRobot(token, ip);

        try {
            log.warn("called make");
            if (originUrl.isEmpty()) throw new CustomException(ErrorCode.NO_INPUT);

            if (!(originUrl.contains("http://") || originUrl.contains("https://"))) //프로토콜이 없으면
                originUrl = "http://" + originUrl; //기본으로 http://를 추가해줍니다.

            log.info("/make {}", originUrl);
            return ResponseEntity.ok(shortService.make(originUrl));
        } catch(MalformedURLException e){
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }

    @GetMapping("/{hangul}")
    public ResponseEntity<String> tts(@PathVariable String hangul){
        log.warn("called go {}", hangul);
        if(hangul.length() > 5)
            return null;

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = req.getHeader("X-FORWARDED-FOR");
        if (ip == null)
            ip = req.getRemoteAddr();
        String userAgent = req.getHeader("User-Agent");
        String referer = req.getHeader("Referer");


        LogDto.dataCollection data = LogDto.dataCollection.builder().ip(ip)
                .userAgent(userAgent)
                .referer(referer)
                .hangul(hangul).build();

        try { //임시방편
            String originUrl = shortService.go(data);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setLocation(URI.create(originUrl));
            return ResponseEntity.status(HttpStatus.FOUND)
                    .headers(responseHeaders)
                    .body("");
        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Err");
    }
}
