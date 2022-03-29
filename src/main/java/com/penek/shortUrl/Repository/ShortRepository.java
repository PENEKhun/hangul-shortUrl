package com.penek.shortUrl.Repository;

import com.penek.shortUrl.Dto.OriginUrlDto;
import com.penek.shortUrl.Entity.ShortEntity;
import com.penek.shortUrl.Exception.CustomException;
import com.penek.shortUrl.Exception.ErrorCode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

@Repository
@Transactional
public class ShortRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(ShortEntity shortEntity){
        em.persist(shortEntity);
    }

    public OriginUrlDto getUrl(BigInteger idx){
        try {
            OriginUrlDto originUrlDto = new OriginUrlDto();
            ShortEntity shortEntity = em.find(ShortEntity.class, idx);
            originUrlDto.setUrl(shortEntity.getOriginUrl());
            originUrlDto.setIdx(shortEntity.getId());
            return originUrlDto;

        } catch(Exception e){
            throw new CustomException(ErrorCode.LINK_NOT_FOUND);
        }
    }


}
