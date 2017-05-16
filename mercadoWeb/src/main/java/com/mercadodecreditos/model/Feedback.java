package com.mercadodecreditos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Feedback")
public class Feedback implements Serializable {	
	private static final long serialVersionUID = -7712663107674010032L;
	
	@Id @GeneratedValue
	@Column(name = "xidFeedback")
	private int xidFeedback;
	
	private String description;

	public int getXidFeedback() {
		return xidFeedback;
	}

	public void setXidFeedback(int xidFeedback) {
		this.xidFeedback = xidFeedback;
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
		result = prime * result + xidFeedback;
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
		Feedback other = (Feedback) obj;
		if (xidFeedback != other.xidFeedback)
			return false;
		return true;
	}
}
