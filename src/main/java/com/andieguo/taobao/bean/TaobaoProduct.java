package com.andieguo.taobao.bean;

public class TaobaoProduct {

	private String nid;
	private String detail_url;
	private String title;
	private String raw_title;
	private double reserve_price;
	private double view_price;
	private String nick;
	private String view_sales;
	private String user_id;
	public String getNid() {
		return nid;
	}
	public void setNid(String nid) {
		this.nid = nid;
	}
	public String getDetail_url() {
		return detail_url;
	}
	public void setDetail_url(String detail_url) {
		this.detail_url = detail_url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRaw_title() {
		return raw_title;
	}
	public void setRaw_title(String raw_title) {
		this.raw_title = raw_title;
	}
	public double getReserve_price() {
		return reserve_price;
	}
	public void setReserve_price(double reserve_price) {
		this.reserve_price = reserve_price;
	}
	public double getView_price() {
		return view_price;
	}
	public void setView_price(double view_price) {
		this.view_price = view_price;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getView_sales() {
		return view_sales;
	}
	public void setView_sales(String view_sales) {
		this.view_sales = view_sales;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public TaobaoProduct(String nid, String detail_url, String title, String raw_title, double reserve_price,
			double view_price, String nick, String view_sales, String user_id) {
		super();
		this.nid = nid;
		this.detail_url = detail_url;
		this.title = title;
		this.raw_title = raw_title;
		this.reserve_price = reserve_price;
		this.view_price = view_price;
		this.nick = nick;
		this.view_sales = view_sales;
		this.user_id = user_id;
	}
	public TaobaoProduct() {
		super();
	}
	
	@Override
	public String toString() {
		return "TaobaoProduct [nid=" + nid + ", detail_url=" + detail_url + ", title=" + title + ", raw_title="
				+ raw_title + ", reserve_price=" + reserve_price + ", view_price=" + view_price + ", nick=" + nick
				+ ", view_sales=" + view_sales + ", user_id=" + user_id + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		TaobaoProduct product = (TaobaoProduct)obj;
		return nid.equals(product.getNid()) && detail_url.equals(product.getDetail_url());
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		String input = nid+detail_url;
		return input.hashCode();
	}
	
}

