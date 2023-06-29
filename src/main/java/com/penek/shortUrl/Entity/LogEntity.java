package com.penek.shortUrl.Entity;

import com.penek.shortUrl.Service.ShortService.LogData;
import com.penek.shortUrl.Util.IpAddressUtils;
import com.penek.shortUrl.config.Browser;
import com.penek.shortUrl.config.OperatingSystem;
import java.math.BigInteger;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Table(name = "Log", schema = "shortlinkproject")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class LogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "idx")
  BigInteger idx;

  @Column(name = "pageId")
  BigInteger pageId;

  @Column(name = "country")
  String country;

  @Column(name = "referer")
  String referer;

  @Column(name = "platform")
  String platform;

  @Column(name = "userAgent")
  String userAgent;

  @Column(name = "enteredAt")
  @CreatedDate
  Timestamp enteredAt;

  public static LogEntity CreateLog(BigInteger pageId, LogData data) {
    return LogEntity.builder()
        .country(IpAddressUtils.getCountry(data.ip()))
        .pageId(pageId)
        .referer(data.referer())
        .platform(OperatingSystem.of(data.userAgent()).name())
        .userAgent(Browser.of(data.userAgent()).browserName())
        .build();
  }

}
