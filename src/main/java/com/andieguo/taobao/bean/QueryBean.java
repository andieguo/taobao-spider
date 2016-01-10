package com.andieguo.taobao.bean;

public class QueryBean {

	private String key;
	private double startprice;
	private double endprice;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public double getStartprice() {
		return startprice;
	}
	public void setStartprice(double startprice) {
		this.startprice = startprice;
	}
	public double getEndprice() {
		return endprice;
	}
	public void setEndprice(double endprice) {
		this.endprice = endprice;
	}
	public QueryBean(String key, double startprice, double endprice) {
		super();
		this.key = key;
		this.startprice = startprice;
		this.endprice = endprice;
	}
	public QueryBean() {
		super();
	}
	@Override
	public String toString() {
		return "QueryBean [key=" + key + ", startprice=" + startprice + ", endprice=" + endprice + "]";
	}
	
}
