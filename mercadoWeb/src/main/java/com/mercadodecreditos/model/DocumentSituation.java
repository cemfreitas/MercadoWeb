package com.mercadodecreditos.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DocumentSituation")
public class DocumentSituation implements Serializable {
	private static final long serialVersionUID = 1706861821446540034L;

	@Id
	@GeneratedValue
	@Column(name = "xidDocumentSituation")
	private long xidDocumentSituation;

	@ManyToOne
	@JoinColumn(name = "idDocument")
	private Document document;
	@ManyToOne
	@JoinColumn(name = "idDocumentStatus")
	private DocumentStatus documentStatus;
	private String description;
	private Date documentSituationDate;

	public DocumentSituation() {

	}

	public DocumentSituation(DocumentSituation newDocumentSituation) {
		this.description = newDocumentSituation.description;
		this.document = newDocumentSituation.document;
		this.documentSituationDate = newDocumentSituation.documentSituationDate;
		this.documentStatus = newDocumentSituation.documentStatus;
	}

	public long getXidDocumentSituation() {
		return xidDocumentSituation;
	}

	public void setXidDocumentSituation(long xidDocumentSituation) {
		this.xidDocumentSituation = xidDocumentSituation;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDocumentSituationDate() {
		return documentSituationDate;
	}

	public void setDocumentSituationDate(Date documentSituationDate) {
		this.documentSituationDate = documentSituationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (xidDocumentSituation ^ (xidDocumentSituation >>> 32));
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
		DocumentSituation other = (DocumentSituation) obj;
		if (xidDocumentSituation != other.xidDocumentSituation)
			return false;
		return true;
	}

	public DocumentStatus getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(DocumentStatus documentStatus) {
		this.documentStatus = documentStatus;
	}

}
