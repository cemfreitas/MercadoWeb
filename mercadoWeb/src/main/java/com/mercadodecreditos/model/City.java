package com.mercadodecreditos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="City")
public class City implements Serializable {	
	private static final long serialVersionUID = 3729284168355193500L;
	
	@Id @GeneratedValue
	@Column(name = "xidCity")	
	private int xidCity;
	
	private String description;

	@ManyToOne 
	@JoinColumn(name="idState")
	private State state;
	
	public int getXidCity() {
		return xidCity;
	}
	public void setXidCity(int xidCity) {
		this.xidCity = xidCity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	@Override
	public int hashCode() {
		/*final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + xidCity;
		return result;*/
		return xidCity;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (xidCity != other.xidCity)
			return false;
		return true;
	}
	

}
