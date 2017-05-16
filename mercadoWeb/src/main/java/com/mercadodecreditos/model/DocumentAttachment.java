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
@Table(name="DocumentAttachment")
public class DocumentAttachment implements Serializable {	
	private static final long serialVersionUID = 963594852926200397L;
	
	@Id @GeneratedValue
	@Column(name = "xidDocumentAttachment")
	private long xidDocumentAttachment;
	
	private String description;
	private String path;
	@ManyToOne
	@JoinColumn(name="idDocument")
	private Document document;
	
	public long getXidDocumentAttachment() {
		return xidDocumentAttachment;
	}
	public void setXidDocumentAttachment(long xidDocumentAttachment) {
		this.xidDocumentAttachment = xidDocumentAttachment;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
		result = prime
				* result
				+ (int) (xidDocumentAttachment ^ (xidDocumentAttachment >>> 32));
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
		DocumentAttachment other = (DocumentAttachment) obj;
		if (xidDocumentAttachment != other.xidDocumentAttachment)
			return false;
		return true;
	}

}
