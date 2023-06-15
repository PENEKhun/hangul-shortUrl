package com.penek.shortUrl.Entity;

import com.penek.shortUrl.Dto.RequestInformation;
import com.penek.shortUrl.Util.IpAddressUtils;
import com.penek.shortUrl.config.Browser;
import com.penek.shortUrl.config.OperatingSystem;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Builder
@Table(name = "Log", schema = "shortlinkproject")
@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@EntityListeners(AuditingEntityListener.class)
public class LogEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public static LogEntity CreateLog(BigInteger pageId, RequestInformation data) {
        return LogEntity.builder()
                .country(IpAddressUtils.getCountry(data.getIp()))
                .pageId(pageId)
                .referer(data.getReferer())
                .platform(OperatingSystem.of(data.getUserAgent()).name())
                .userAgent(Browser.of(data.getUserAgent()).browserName())
                .build();
    }

}
