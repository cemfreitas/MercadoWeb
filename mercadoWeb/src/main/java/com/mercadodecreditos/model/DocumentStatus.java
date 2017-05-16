package com.mercadodecreditos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DocumentStatus")
public class DocumentStatus implements Serializable {	
	private static final long serialVersionUID = 1519889492122912783L;
	
	@Id @GeneratedValue
	@Column(name = "xidDocumentStatus")
	private int xidDocumentStatus;
	
	private String description;

	public int getXidDocumentStatus() {
		return xidDocumentStatus;
	}

	public void setXidDocumentStatus(int xidDocumentStatus) {
		this.xidDocumentStatus = xidDocumentStatus;
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
		result = prime * result + xidDocumentStatus;
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
		DocumentStatus other = (DocumentStatus) obj;
		if (xidDocumentStatus != other.xidDocumentStatus)
			return false;
		return true;
	}

}
