package com.penek.shortUrl.Repository;

import com.penek.shortUrl.Entity.LogEntity;
import com.penek.shortUrl.Exception.CustomException;
import com.penek.shortUrl.Exception.ErrorCode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional
public class LogRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(LogEntity logEntity){
        em.persist(logEntity);
    }

    public List<LogEntity> findByIdx(BigInteger idx){
        try {
            TypedQuery<LogEntity> query = em.createQuery("SELECT m FROM LogEntity m WHERE m.pageId = :pageId", LogEntity.class)
                    .setParameter("pageId", idx);
            return (List<LogEntity>) query.getResultList();
        } catch(Exception e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }



    public LogEntity findOne(long idx){
        try {
            return em.find(LogEntity.class, idx);
        } catch(Exception e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }


}
