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
@Table(name = "DocumentFractionQuestion")
public class DocumentFractionQuestion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7845579322788121052L;

	@Id
	@GeneratedValue
	@Column(name = "xidDocumentFractionQuestion")
	private long xidDocumentFractionQuestion;
	
	@ManyToOne
	@JoinColumn(name = "idDocumentQuestion")
	private DocumentQuestion documentQuestion;
	
	@ManyToOne
	@JoinColumn(name = "idDocumentFraction")
	private DocumentFraction documentFraction;	

	public long getXidDocumentFractionQuestion() {
		return xidDocumentFractionQuestion;
	}

	public void setXidDocumentFractionQuestion(long xidDocumentFractionQuestion) {
		this.xidDocumentFractionQuestion = xidDocumentFractionQuestion;
	}

	public DocumentQuestion getDocumentQuestion() {
		return documentQuestion;
	}

	public void setDocumentQuestion(DocumentQuestion documentQuestion) {
		this.documentQuestion = documentQuestion;
	}

	public DocumentFraction getDocumentFraction() {
		return documentFraction;
	}

	public void setDocumentFraction(DocumentFraction documentFraction) {
		this.documentFraction = documentFraction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((documentFraction == null) ? 0 : documentFraction.hashCode());
		result = prime
				* result
				+ ((documentQuestion == null) ? 0 : documentQuestion.hashCode());
		result = prime
				* result
				+ (int) (xidDocumentFractionQuestion ^ (xidDocumentFractionQuestion >>> 32));
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
		DocumentFractionQuestion other = (DocumentFractionQuestion) obj;
		if (documentFraction == null) {
			if (other.documentFraction != null)
				return false;
		} else if (!documentFraction.equals(other.documentFraction))
			return false;
		if (documentQuestion == null) {
			if (other.documentQuestion != null)
				return false;
		} else if (!documentQuestion.equals(other.documentQuestion))
			return false;
		if (xidDocumentFractionQuestion != other.xidDocumentFractionQuestion)
			return false;
		return true;
	}

}
