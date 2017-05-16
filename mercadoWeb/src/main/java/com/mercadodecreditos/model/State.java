package com.mercadodecreditos.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="State")
public class State implements Serializable{
	private static final long serialVersionUID = -3119684668882297429L;
	
	@Id @GeneratedValue
	@Column(name = "xidState")
	private int xidState;
	
	private String UF;
	private String description;
	@OneToMany(mappedBy = "state")
	private List<City> city;
	
	public int getXidState() {
		return xidState;
	}
	public void setXidState(int xidState) {
		this.xidState = xidState;
	}
	public String getUF() {
		return UF;
	}
	public void setUF(String uF) {
		UF = uF;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((UF == null) ? 0 : UF.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + xidState;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;		
		if (xidState != other.xidState)
			return false;
		return true;
	}
	public List<City> getCity() {
		return city;
	}
	public void setCity(List<City> city) {
		this.city = city;
	}

}
