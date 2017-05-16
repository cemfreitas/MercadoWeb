package com.mercadodecreditos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DocumentType")
public class DocumentType implements Serializable {	
	private static final long serialVersionUID = -543550395731541335L;
	
	@Id @GeneratedValue
	@Column(name = "xidDocumentType")
	private int xidDocumentType;
	
	private String description;

	public int getXidDocumentType() {
		return xidDocumentType;
	}

	public void setXidDocumentType(int xidDocumentType) {
		this.xidDocumentType = xidDocumentType;
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
		result = prime * result + xidDocumentType;
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
		DocumentType other = (DocumentType) obj;
		if (xidDocumentType != other.xidDocumentType)
			return false;
		return true;
	}

}
