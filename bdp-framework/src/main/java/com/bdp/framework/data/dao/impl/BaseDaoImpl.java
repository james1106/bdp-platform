package com.bdp.framework.data.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.bdp.framework.data.dao.BaseDao;
import com.bdp.framework.data.dto.PageResult;
import com.bdp.framework.util.ReflectionUtils;


@Repository
public class BaseDaoImpl<T> implements BaseDao<T> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public T createEntity(T entity) {
		entityManager.persist(entity);
		return entity;
	}

	@Override
	public T readEntity(Serializable id,Class<T> entityClass) {
		return (T) entityManager.find(entityClass, id);
	}

	@Override
	public boolean updateEntity(T entity) {
		entityManager.merge(entity);
		return true;
	}

	@Override
	public boolean deleteEntity(T entity) {
		entityManager.remove(entity);
		return true;
	}

	@Override
	public T getEntityByJpql(String jpql, Object... args) {
		List<T> result = getEntitiesByJpql(jpql, args);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public List<T> getEntitiesByJpql(String jpql, Object... args) {
		Class<T> clazz = ReflectionUtils.getSuperClassGenricType(getClass());
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getEntitiesBySql(String sql, Object... args) {
		Class<T> clazz = ReflectionUtils.getSuperClassGenricType(getClass());
		Query query = entityManager.createNativeQuery(sql, clazz);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query.getResultList();
	}

	@Override
	public T getEntityBySql(String sql, Object... args) {
		List<T> result = getEntitiesBySql(sql, args);
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public void doEntityByJpql(String jpql, Object... args) {
		Class<T> clazz = ReflectionUtils.getSuperClassGenricType(getClass());
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		query.executeUpdate();
	}

	@Override
	public void doEntityBySql(String sql, Object... args) {
		Query query = entityManager.createNativeQuery(sql);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMapsBySql(String sql, Object... args) {
		Query query = entityManager.createNativeQuery(sql, Map.class);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query.getResultList();
	}

	@Override
	public PageResult<T> getPageResultByJpql(String jpql, int pageNO, int pageSize, Object... args) {
		Class<T> clazz = ReflectionUtils.getSuperClassGenricType(getClass());
		jpql = jpql.trim();

		String countJpql = null;
		String jpqlUpper = jpql.toUpperCase();
		int fromIndex = jpqlUpper.indexOf("FROM");
		// 拼接总数查询Jpql语句
		if (fromIndex >= 0) {
			countJpql = "SELECT COUNT(1) " + jpql.substring(fromIndex, jpql.length());
		} else {
			throw new RuntimeException("分页查询的jpql语句不规范：" + jpql);
		}
		PageResult<T> pageResult = new PageResult<T>();
		TypedQuery<Long> countQuery = entityManager.createQuery(countJpql, Long.class);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				countQuery.setParameter(i, args[i]);
			}
		}
		pageResult.setTotalSize(countQuery.getSingleResult());

		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		query.setFirstResult((pageNO - 1) * pageSize);
		query.setMaxResults(pageSize);

		pageResult.setList(query.getResultList());
		pageResult.setPageNO(pageNO);
		pageResult.setPageSize(pageSize);
		return pageResult;
	}

	@Override
	public PageResult<T> getPageResultBySql(String sql, int pageNO, int pageSize, Object... args) {
		Class<T> clazz = ReflectionUtils.getSuperClassGenricType(getClass());
		PageResult<T> pageResult = getPageResultBySql(sql, clazz, pageNO, pageSize, args);
		return pageResult;
	}

	@SuppressWarnings("unchecked")
	private <B> PageResult<B> getPageResultBySql(String sql, Class<B> clazz, int pageNO, int pageSize, Object... args) {
		Query query = entityManager.createNativeQuery(sql, clazz);
		String countSql = "SELECT COUNT(1) AS CT FROM (" + sql + ") _T";
		Query countQuery = entityManager.createNativeQuery(countSql, Long.class);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
				countQuery.setParameter(i, args[i]);
			}
		}
		query.setFirstResult((pageNO - 1) * pageSize);
		query.setMaxResults(pageSize);
		PageResult<B> pageResult = new PageResult<B>();
		pageResult.setTotalSize((long) countQuery.getSingleResult());
		pageResult.setPageNO(pageNO);
		pageResult.setPageSize(pageSize);
		pageResult.setList(query.getResultList());
		return pageResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<Map<String, Object>> getMapPageResultBySql(String sql, int pageNO, int pageSize, Object... args) {
		@SuppressWarnings("rawtypes")
		PageResult<Map> pageResult = getPageResultBySql(sql, Map.class, pageNO, pageSize, args);
		PageResult<Map<String, Object>> result = new PageResult<>();
		List<Map<String, Object>> list = new ArrayList<>();
		if (pageResult.getList() != null) {
			for (Map<String, Object> map : pageResult.getList()) {
				list.add(map);
			}
		}
		result.setTotalSize(pageResult.getTotalSize());
		result.setPageNO(pageNO);
		result.setPageSize(pageSize);
		result.setList(list);
		return result;
	}
}