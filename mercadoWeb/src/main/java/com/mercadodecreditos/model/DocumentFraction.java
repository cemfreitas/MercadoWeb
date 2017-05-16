package com.mercadodecreditos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DocumentFraction")
public class DocumentFraction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5886277661202002495L;
	@Id
	@GeneratedValue
	@Column(name = "xidFractionDocument")
	private long xidFractionDocument;
	@ManyToOne
	@JoinColumn(name = "idDocument")
	private Document mainDocument;
	private BigDecimal price;
	@ManyToOne
	@JoinColumn(name = "idStatus")
	private DocumentStatus status;
	
	
	public long getXidFractionDocument() {
		return xidFractionDocument;
	}
	public void setXidFractionDocument(long xidFractionDocument) {
		this.xidFractionDocument = xidFractionDocument;
	}
	public Document getMainDocument() {
		return mainDocument;
	}
	public void setMainDocument(Document mainDocument) {
		this.mainDocument = mainDocument;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public DocumentStatus getStatus() {
		return status;
	}
	public void setStatus(DocumentStatus status) {
		this.status= status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((mainDocument == null) ? 0 : mainDocument.hashCode());
		result = prime * result
				+ ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result
				+ (int) (xidFractionDocument ^ (xidFractionDocument >>> 32));
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
		DocumentFraction other = (DocumentFraction) obj;
		if (mainDocument == null) {
			if (other.mainDocument != null)
				return false;
		} else if (!mainDocument.equals(other.mainDocument))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (xidFractionDocument != other.xidFractionDocument)
			return false;
		return true;
	}
}
