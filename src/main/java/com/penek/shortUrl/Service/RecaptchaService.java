package com.penek.shortUrl.Service;

import com.penek.shortUrl.Dto.RecaptchaDto;
import com.penek.shortUrl.Exception.CustomException;
import com.penek.shortUrl.Exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class RecaptchaService {

    final double minScore = 0.02;
    //해당점수 이상이면 사람으로 인정할겁니다.
    //0.9점을 기본 점수로 구글은 권장

    public void throwRobot(String token,String remoteIp) {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("secret", "6LdjJO8eAAAAAOhKdoKh8dlR7Pslx2gxRu6VsbCa");
        map.add("response", token);
        map.add("remoteip", remoteIp);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        RecaptchaDto response = restTemplate.postForObject( url, request, RecaptchaDto.class );


        if (!(response.isSuccess() && response.getScore() >= minScore)) {
            log.warn("robot checked, score =  " + response.getScore());
            throw new CustomException(ErrorCode.CAPTCHA_ERROR);
        }

    }

}
