package com.penek.shortUrl.Service;

import static com.penek.shortUrl.Entity.LogEntity.CreateLog;
import static com.penek.shortUrl.Util.IpAddressUtils.getCountry;

import com.penek.shortUrl.Dto.RequestInformation;
import com.penek.shortUrl.Entity.ShortEntity;
import com.penek.shortUrl.Exception.CustomException;
import com.penek.shortUrl.Exception.ErrorCode;
import com.penek.shortUrl.Repository.LogRepository;
import com.penek.shortUrl.Repository.ShortRepository;
import com.penek.shortUrl.config.Browser;
import com.penek.shortUrl.config.OperatingSystem;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class ShortService {

  private final ShortRepository shortRepository;
  private final LogRepository logRepository;
  private final RecaptchaService recaptchaService;

  @Autowired
  ResourceLoader resourceLoader;

  public static URL getFinalURL(URL url) {
    try {
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setInstanceFollowRedirects(false);
      con.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
      con.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
      con.addRequestProperty("Referer", "https://www.google.com/");
      con.connect();
      //con.getInputStream();
      int resCode = con.getResponseCode();
      log.info("resCode {}", con.getResponseCode());
      if (resCode == HttpURLConnection.HTTP_SEE_OTHER
          || resCode == HttpURLConnection.HTTP_MOVED_PERM
          || resCode == HttpURLConnection.HTTP_MOVED_TEMP) {
        String Location = con.getHeaderField("Location");
        if (Location.startsWith("/")) {
          Location = url.getProtocol() + "://" + url.getHost() + Location;
        }
        return getFinalURL(new URL(Location));
      }

    } catch (Exception e) {
      throw new CustomException(ErrorCode.LINK_NOT_FOUND);
    }
    return url;
  }

  public void logging(LogData data) {
    displayLogTerminal(data);
    saveLogDatabase(data);
  }

  private void displayLogTerminal(LogData data) {
    log.info("########## user approached at {} #########", data.hangul);
    log.info("    connected header information");
    log.info("    ip Addr : {}", data.ip());
    log.info("    country : {}", getCountry(data.ip()));
    log.info("    userAgent : {}", data.userAgent());
    log.info("    operatingSystem : {}", OperatingSystem.of(data.userAgent).osName());
    log.info("    browser : {}", Browser.of(data.userAgent).browserName());
    log.info("    referer : {}", data.referer);
    log.info("################################################");
  }

  public ResponseEntity make(String originUrl) throws MalformedURLException {

    urlValidator(originUrl);

    ShortEntity shortEntity = new ShortEntity();
    shortEntity.setOriginUrl(originUrl);

    shortRepository.save(shortEntity);

    String shortUrl = idxToShortUrl(shortEntity.getId());
    log.info("shorten Url - index {}, origin {} ", shortEntity.getId(), shortUrl);
    return ResponseEntity.ok(shortUrl);
  }

  @Transactional
  public String restoreToOriginalUrl(RequestInformation request, String hangul) {
    // restore url
    String originalUrl = shortRepository.getUrl(shortUrl2Index(hangul)).getUrl();

    // collection Log DATA & logging
    LogData logData = new LogData(
        request.getIp(),
        getCountry(request.getIp()),
        request.getUserAgent(),
        OperatingSystem.of(request.getUserAgent()).osName(),
        Browser.of(request.getUserAgent()).browserName(),
        originalUrl, hangul);

    logging(logData);

    // return
    return originalUrl;
  }

  private void saveLogDatabase(LogData data) {
    logRepository.save(CreateLog(shortUrl2Index(data.hangul), data));
  }

  public BigInteger shortUrl2Index(String link) {
    try {
      link = link.substring(0, 5);

      log.info("shortUrlToIdx input : {}", link);
      String[] dataArray = link.split("");
      int[] fiveHangul = new int[3 * 5]; // 18 20 27
      int i = 0;
      BigInteger result = new BigInteger("0");

      for (String s : dataArray) {
        int chosung = (s.charAt(0) - 0xAC00) / 588;
        int jungsung = ((s.charAt(0) - 0xAC00) % 588) / 28;
        int jongsung = (s.charAt(0) - 0xAC00) % 28; //없을 수도 있음
        fiveHangul[i * 3] = chosung;
        fiveHangul[i * 3 + 1] = jungsung;
        fiveHangul[i * 3 + 2] = jongsung;
        i++;
      }

      result = result.add(BigInteger.valueOf(fiveHangul[0]));

      BigInteger[] arr = new BigInteger[3 * 5];
      arr[0] = BigInteger.ONE;

      for (int k = 1; k < 15; k++) {
        arr[k] = arr[k - 1].multiply(BigInteger.valueOf(getMax(k - 1)));
        result = result.add(arr[k].multiply(BigInteger.valueOf(fiveHangul[k])));
      }

      log.info("shortUrlToIdx output : {}", result);
      return result;
    } catch (StringIndexOutOfBoundsException e) {
      e.printStackTrace();
      throw new CustomException(ErrorCode.INVALID_INPUT);
    }
  }

  public String idxToShortUrl(BigInteger idx) {

    log.info("idxToShrtUrl input : {}", idx);
    int[] fiveHangul = new int[3 * 5]; // 18 20 27
    String result = "";

    int divisor = 18;

    fiveHangul[0] = idx.remainder(BigInteger.valueOf(divisor)).intValue();

    BigInteger[] arr = new BigInteger[3 * 5];
    arr[0] = BigInteger.ONE;

    for (int k = 1; k < 15; k++) {
      arr[k] = arr[k - 1].multiply(BigInteger.valueOf(getMax(k - 1)));
      if (arr[k].compareTo(idx) == 0) {
        fiveHangul[k - 1] = (int) getMax(k - 1);
        idx = BigInteger.ONE;
      }
    }

    for (int k = 14; k > 0; k--) {
      BigInteger div = idx.divide(arr[k]);
      if (!div.equals(BigInteger.ZERO)) {
        fiveHangul[k] = div.intValue();
        idx = idx.remainder(arr[k]);
      }
    }

    for (int i = 0; i < 5; i++) {
      //글자 만들기
      int chosung = fiveHangul[i * 3];
      int jungsung = fiveHangul[i * 3 + 1];
      int jongsung = fiveHangul[i * 3 + 2];

      char unicode = (char) (44032 + (chosung * 21 * 28) + (jungsung * 28) + jongsung);
      //유니코드 값 = 44032 + ( 초성번호(0~18) * 21 ) * 28 + ( 중성번호(0~20) * 28 ) + 종성번호(0~27)
      result = result + unicode;
    }
    log.info("idxToShrtUrl output : {}", result);
    return result;
  }

  public long getMax(long arr) {
    arr++;
    if (arr % 3 == 0) {
      return 27;
    }
    if ((arr + 1) % 3 == 0) {
      return 20;
    }
    return 18;
  }

  public void urlValidator(String url) throws MalformedURLException {

    URL startURL = new URL(url);

    if (startURL.getHost().equals("localhost")) {
      throw new CustomException(ErrorCode.BAD_SECURITY_URL);
    }
    if (startURL.getHost().equals("huni.kr")) {
      throw new CustomException(ErrorCode.BAD_SECURITY_URL);
    }
    if (startURL.getHost().equals("15.165.86.75")) {
      throw new CustomException(ErrorCode.BAD_SECURITY_URL);
    }

    URL finalURL = getFinalURL(startURL);
    if (finalURL.getHost().equals("localhost")) {
      throw new CustomException(ErrorCode.BAD_SECURITY_URL);
    }
    if (finalURL.getHost().equals("huni.kr")) {
      throw new CustomException(ErrorCode.BAD_SECURITY_URL);
    }
    if (finalURL.getHost().equals("15.165.86.75")) {
      throw new CustomException(ErrorCode.BAD_SECURITY_URL);
    }

    log.info("start url is {}", startURL.getHost());
    log.info("final url is {}", finalURL.getHost());

    if (!finalURL.getHost().contains(startURL.getHost())) {
      throw new CustomException(ErrorCode.REDIRECT_URL);
    }
  }

  public void getEnterable(URL url) {
    try {
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setInstanceFollowRedirects(false);
      con.setRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
      con.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
      con.addRequestProperty("Referer", "https://www.google.com/");
      con.connect();

      int resCode = con.getResponseCode();
      log.warn("resCode {}", con.getResponseCode());
      if (resCode != HttpURLConnection.HTTP_OK) {
        throw new CustomException(ErrorCode.INVALID_INPUT);
      }
    } catch (Exception e) {
      throw new CustomException(ErrorCode.LINK_NOT_FOUND);
    }
  }

  public record LogData(
      String ip,
      String country,
      String userAgent,
      String operatingSystem,
      String browser,
      String referer,
      String hangul
  ) {

  }

}