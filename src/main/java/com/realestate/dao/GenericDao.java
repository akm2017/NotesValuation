package com.realestate.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.realstate.entity.AbstractEntity;

public interface GenericDao {

	<E extends AbstractEntity> E create(final E model);
	
	<E extends AbstractEntity> E update(final E model);
	
	<E extends AbstractEntity> void delete(final E model);
	
	<E extends AbstractEntity> E getById(final Class<E> modelClass, final Serializable id);
	
	<E extends AbstractEntity> void deleteById(final Class<E> modelClass, final Serializable id);
	
	<E extends AbstractEntity> List<E>  getAll(final Class<E> model);
	
	/**
	 * Update using named query
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	int updateByNamedQuery(String queryName, Map<String,Object> parameters);
	
	<E extends AbstractEntity> List<E>  getResultByNamedQuery(String queryName, Map<String,Object> parameters);
	
	
}