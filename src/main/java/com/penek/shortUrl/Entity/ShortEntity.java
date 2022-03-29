package com.penek.shortUrl.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Short", schema = "shortlinkproject")
public class ShortEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx")
    BigInteger id;

    @NotBlank
    @Column(name = "orignUrl")
    String originUrl;

    @CreatedDate
    @Column(name = "createdAt")
    LocalDateTime createdAt;
}
