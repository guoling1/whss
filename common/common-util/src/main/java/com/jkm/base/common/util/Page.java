package com.jkm.base.common.util;

import java.util.List;

public class Page<T> {
	private PageUtils page = new PageUtils();
	private List<T> list;
	private  T objectT;
	

	public T getObjectT() {
		return objectT;
	}
	public void setObjectT(T objectT) {
		this.objectT = objectT;
	}
	
	public PageUtils getPage() {
		return page;
	}
	public void setPage(PageUtils page) {
		this.page = page;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "Page [page=" + page + ", list=" + list + ", objectT=" + objectT
				+ "]";
	}
}
