package com.terrencewei.markdown.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.terrencewei.markdown.model.OSSEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by terrencewei on 3/13/17.
 * 
 * use spring data JPA interface to save entity to DB
 * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
@Repository
public interface OSSDao extends CrudRepository<OSSEntity, Long> {

    List<OSSEntity> findByKey(String key);



    @Transactional
    Long countByKey(String key);



    @Transactional
    List<OSSEntity> deleteByKey(String key);

}
