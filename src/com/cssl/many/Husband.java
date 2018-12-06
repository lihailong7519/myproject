package com.cssl.many;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@BatchSize(size=9)
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
public class Husband {
	
	@Id
	@GeneratedValue
	private Integer hid;
	private String hname;
	
	@OneToMany(mappedBy="husband")
	@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
	private Set<Wife> wifes = new HashSet<Wife>();
		
	public Set<Wife> getWifes() {
		return wifes;
	}
	
	public void setWifes(Set<Wife> wifes) {
		this.wifes = wifes;
	}
	public Integer getHid() {
		return hid;
	}
	public void setHid(Integer hid) {
		this.hid = hid;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
	}

	public Husband(Integer hid, String hname) {
		super();
		this.hid = hid;
		this.hname = hname;
	}

	public Husband() {
		super();
	}
	
	

}
