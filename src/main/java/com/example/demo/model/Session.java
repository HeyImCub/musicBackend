package com.example.demo.model;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Sessions")
public class Session {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	
	
	@Column(name = "SessionCode")
    private long sessionCode;
	
	
	
	@Column(name="Queue")
	@ElementCollection(targetClass=String.class)
	private List<String> queue;
	
	
	
	@Column(name="ExpireNum")
	private int expireNum;
	
	
	
	@Column(name="AdminCode")
	private long adminCode;



	public List<String> getQueue() {
		return queue;
	}



	public void setQueue(List<String> queue) {
		this.queue = queue;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getSessionCode() {
		return sessionCode;
	}



	public void setSessionCode(long sessionCode) {
		this.sessionCode = sessionCode;
	}



	public int getExpireNum() {
		return expireNum;
	}



	public void setExpireNum(int expireNum) {
		this.expireNum = expireNum;
	}



	public long getAdminCode() {
		return adminCode;
	}



	public void setAdminCode(long adminCode) {
		this.adminCode = adminCode;
	}






	
}
