package com.mercadodecreditos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UserType")
public class UserType implements Serializable{	
	private static final long serialVersionUID = 1088842733266750934L;

	@Id @GeneratedValue
	@Column(name = "xidUserType")
	private int xidUserType;
	
	private String description;

	public int getXidUserType() {
		return xidUserType;
	}

	public void setXidUserType(int xidUserType) {
		this.xidUserType = xidUserType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
