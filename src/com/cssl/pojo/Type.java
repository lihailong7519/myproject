package com.cssl.pojo;

import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;

@NamedQuery(name="findType",query="from Type t where t.tid<5")
@NamedNativeQuery(name="findType",query="")
public class Type {

	private Integer tid;
	private String name;
		
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Type() {
		super();
	}
	public Type(Integer tid, String name) {
		super();
		this.tid = tid;
		this.name = name;
	}
	
	
	
	
}
