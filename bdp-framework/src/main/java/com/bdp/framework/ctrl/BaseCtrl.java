package com.bdp.framework.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bdp.framework.data.biz.BaseBiz;

/**
 * 控制器基类
 * 
 * @param <T>
 * 
 * @param <T>
 *
 */
public class BaseCtrl<Biz extends BaseBiz<T>, T> {

	@Autowired
	protected Biz baseBiz;

	@PostMapping
	@ResponseBody
	public ResponseEntity<T> add(@RequestBody T entity) {
		baseBiz.createEntity(entity);
		return ResponseEntity.ok().body(entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<T> get(@PathVariable("id") String id) {
		T entity = baseBiz.readEntity(id);
		return ResponseEntity.ok(entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<T> update(@RequestBody T entity) {
		baseBiz.updateEntity(entity);
		return ResponseEntity.ok(entity);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Boolean> remove(@PathVariable("id") String id) {
		baseBiz.deleteEntity(id);
		return ResponseEntity.ok(true);
	}
}
