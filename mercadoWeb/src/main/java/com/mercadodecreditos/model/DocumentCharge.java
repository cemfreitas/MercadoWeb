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
@Table(name = "DocumentCharge")
public class DocumentCharge implements Serializable {
	private static final long serialVersionUID = 1131937922734420350L;

	@Id
	@GeneratedValue
	@Column(name = "xidDocumentCharge")
	private int xidDocumentCharge;

	private String description;
	private BigDecimal value;
	@ManyToOne
	@JoinColumn(name = "idDocument")
	private Document document;

	public int getXidDocumentCharge() {
		return xidDocumentCharge;
	}

	public void setXidDocumentCharge(int xidDocumentCharge) {
		this.xidDocumentCharge = xidDocumentCharge;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		if (value != null) {
			this.value = value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		} else {
			this.value = null;
		}
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + xidDocumentCharge;
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
		DocumentCharge other = (DocumentCharge) obj;
		if (xidDocumentCharge != other.xidDocumentCharge)
			return false;
		return true;
	}
}
