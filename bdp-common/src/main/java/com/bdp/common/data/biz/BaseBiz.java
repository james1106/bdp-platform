package com.bdp.common.data.biz;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.bdp.common.data.obj.PageResult;

public interface BaseBiz<T> {
	/**
	 * 保存数据对象
	 * 
	 * @param entity
	 * @return
	 */
	T createEntity(T entity);

	/**
	 * 根据id查询
	 * 
	 * @param id
	 * @param t
	 * @return
	 */
	T readEntity(Serializable id);

	/**
	 * 删除数据
	 * 
	 * @param entity
	 */
	boolean deleteEntity(T entity);
	
	
	/**
	 * 根据表的id删除数据
	 * 
	 * @param entity
	 */
	boolean deleteEntity(Serializable id);

	/**
	 * 更新对象
	 * 
	 * @param entity
	 * @return
	 */
	boolean updateEntity(T entity);

	public T getEntityByJpql(String jpql, Object... args);

	public T getEntityBySql(String sql, Object... args);

	public List<T> getEntitiesByJpql(String jpql, Object... args);

	public List<T> getEntitiesBySql(String sql, Object... args);

	public PageResult<T> getPageResultByJpql(String jpql, int pageNO, int pageSize, Object... args);

	public PageResult<T> getPageResultBySql(String sql, int pageNO, int pageSize, Object... args);

	public PageResult<Map<String, Object>> getMapPageResultBySql(String sql, int pageNO, int pageSize, Object... args);

	public void doEntityByJpql(String jpql, Object... args);

	public void doEntityBySql(String sql, Object... args);

	public List<Map<String, Object>> getMapsBySql(String sql, Object... args);

}
