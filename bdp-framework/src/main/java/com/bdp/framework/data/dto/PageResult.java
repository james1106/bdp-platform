package com.bdp.framework.data.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long totalSize;
	private long pageSize;
	private long pageNO;
	private List<T> list = new ArrayList<T>();

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getPageNO() {
		return pageNO;
	}

	public void setPageNO(long pageNO) {
		this.pageNO = pageNO;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public PageResult(int pageNO, int pageSize, int totalSize, List<T> list) {
		this.pageNO = pageNO;
		this.pageSize = pageSize;
		this.totalSize = totalSize;
		this.list = list;
	}

	public PageResult() {
		super();
	}

}
