package com.terrencewei.markdown.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.terrencewei.markdown.bean.OSSObject;

/**
 * Created by terrencewei on 3/13/17.
 * 
 * use spring data JPA interface to save entity to DB
 * http://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */
@Repository
public interface OSSDao extends CrudRepository<OSSObject, Long> {

    List<OSSObject> findByObjKey(String objKey);



    @Transactional
    Long countByObjKey(String objKey);



    @Transactional
    List<OSSObject> deleteByObjKey(String objKey);

}
