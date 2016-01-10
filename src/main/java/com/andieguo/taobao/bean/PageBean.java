package com.andieguo.taobao.bean;

public class PageBean {

	private Integer pageSize;
	private Integer currentPage;
	private Integer totalPage;
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public PageBean(Integer pageSize, Integer currentPage, Integer totalPage) {
		super();
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalPage = totalPage;
	}
	public PageBean() {
		super();
	}
	@Override
	public String toString() {
		return "PageData [pageSize=" + pageSize + ", currentPage=" + currentPage + ", totalPage=" + totalPage + "]";
	}
	
}
