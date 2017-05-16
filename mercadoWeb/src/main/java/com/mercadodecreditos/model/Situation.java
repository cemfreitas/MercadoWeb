package com.mercadodecreditos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Situation")
public class Situation implements Serializable {	
	private static final long serialVersionUID = -9058386224724330770L;
	
	@Id @GeneratedValue
	@Column(name = "xidSituation")
	private int xidSituation;
	
	private String description;

	public int getXidSituation() {
		return xidSituation;
	}

	public void setXidSituation(int xidSituation) {
		this.xidSituation = xidSituation;
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
		result = prime * result + xidSituation;
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
		Situation other = (Situation) obj;
		if (xidSituation != other.xidSituation)
			return false;
		return true;
	}

}
