package com.bdp.common.data.biz.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.persistence.GeneratedValue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.data.biz.BaseBiz;
import com.bdp.common.data.dao.BaseDao;
import com.bdp.common.data.obj.PageResult;
import com.bdp.common.util.ReflectionUtils;
import com.bdp.common.util.UUIDUtils;

@Transactional
public class BaseBizImpl<T> implements BaseBiz<T> {

	@Autowired(required = true)
	BaseDao<T> baseDao;

	@Override
	public T createEntity(T entity) {
		validateId(entity);
		return baseDao.createEntity(entity);
	}

	/**
	 * 如果是手动设置ID的，这里判断没有ID就自动设置一个。<br/>
	 * 
	 * @param entity
	 */
	private void validateId(Object entity) {
		Class<T> clazz = ReflectionUtils.getSuperClassGenricType(getClass());
		try {
			Field field = clazz.getField("id");
			if (field != null) {
				GeneratedValue anno = field.getAnnotation(GeneratedValue.class);
				if (anno == null) {// 表明此时需要手动设置ID
					ReflectionUtils.makeAccessible(field);
					String id = (String) field.get(entity);
					if (StringUtils.isEmpty(id)) {
						field.set(entity, UUIDUtils.generateUuid());
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("实体类" + clazz.getName() + "的ID辅助生成失败，检查ID映射是否符合规范.");
		}
	}

	@Override
	public T readEntity(Serializable id) {
		Class<T> clazz = ReflectionUtils.getSuperClassGenricType(getClass());
		return baseDao.readEntity(id, clazz);
	}

	@Override
	public boolean deleteEntity(T entity) {
		return baseDao.deleteEntity(entity);
	}

	@Override
	public boolean updateEntity(T entity) {
		return baseDao.updateEntity(entity);
	}

	@Override
	public boolean deleteEntity(Serializable id) {
		T entity = readEntity(id);
		if (entity != null) {
			baseDao.deleteEntity(entity);
		}
		return true;
	}

	@Override
	public T getEntityByJpql(String jpql, Object... args) {
		return baseDao.getEntityByJpql(jpql, args);
	}

	@Override
	public List<T> getEntitiesByJpql(String jpql, Object... args) {
		return baseDao.getEntitiesByJpql(jpql, args);
	}

	@Override
	public List<T> getEntitiesBySql(String sql, Object... args) {
		return baseDao.getEntitiesBySql(sql, args);
	}

	@Override
	public T getEntityBySql(String sql, Object... args) {
		return baseDao.getEntityBySql(sql, args);
	}

	@Override
	public void doEntityByJpql(String jpql, Object... args) {
		baseDao.doEntityByJpql(jpql, args);
	}

	@Override
	public PageResult<T> getPageResultByJpql(String jpql, int pageNO, int pageSize, Object... args) {
		return baseDao.getPageResultByJpql(jpql, pageNO, pageSize, args);
	}

	@Override
	public PageResult<T> getPageResultBySql(String sql, int pageNO, int pageSize, Object... args) {
		return baseDao.getPageResultBySql(sql, pageNO, pageSize, args);
	}

	@Override
	public PageResult<Map<String, Object>> getMapPageResultBySql(String sql, int pageNO, int pageSize, Object... args) {
		return baseDao.getMapPageResultBySql(sql, pageNO, pageSize, args);
	}

	@Override
	public void doEntityBySql(String sql, Object... args) {
		baseDao.doEntityBySql(sql, args);
	}

	@Override
	public List<Map<String, Object>> getMapsBySql(String sql, Object... args) {
		return baseDao.getMapsBySql(sql, args);
	}
}
