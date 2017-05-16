package com.mercadodecreditos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "DocumentQuestion")
public class DocumentQuestion implements Serializable {

	private static final long serialVersionUID = -7856563982023904074L;

	@Id
	@GeneratedValue
	@Column(name = "xidDocumentQuestion")
	private long xidDocumentQuestion;
	@ManyToOne
	@JoinColumn(name = "idDocument")
	private Document document;
	@ManyToOne
	@JoinColumn(name = "idUserBid")
	private User userBid;
	private String description;
	private Date documentQuestionDate;
	private BigDecimal bid;
	
	@OneToMany(mappedBy = "documentQuestion")
	private List<DocumentFractionQuestion> documentFractionQuestion;
	
	//@Type(type = "yes_no")
	private String raiseBidRequest;
	@Transient
	private transient String formatedDate;		

	public long getXidDocumentQuestion() {
		return xidDocumentQuestion;
	}

	public void setXidDocumentQuestion(long xidDocumentQuestion) {
		this.xidDocumentQuestion = xidDocumentQuestion;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public User getUserBid() {
		return userBid;
	}

	public void setUserBid(User user) {
		this.userBid = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDocumentQuestionDate() {
		return documentQuestionDate;
	}

	public void setDocumentQuestionDate(Date documentQuestionDate) {
		this.documentQuestionDate = documentQuestionDate;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		if (bid != null) {
			this.bid = bid.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		} else {
			this.bid = null;
		}
	}

	public String getRaiseBidRequest() {
		return raiseBidRequest;
	}

	public void setRaiseBidRequest(String raiseBidRequest) {
		this.raiseBidRequest = raiseBidRequest;
	}
	
	public List<DocumentFractionQuestion> getDocumentFractionQuestion() {
		return documentFractionQuestion;
	}

	public void setDocumentFractionQuestion(
			List<DocumentFractionQuestion> documentFractionQuestion) {
		this.documentFractionQuestion = documentFractionQuestion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (xidDocumentQuestion ^ (xidDocumentQuestion >>> 32));
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
		DocumentQuestion other = (DocumentQuestion) obj;
		if (xidDocumentQuestion != other.xidDocumentQuestion)
			return false;
		return true;
	}
	
	@Transient
	public String getFormatedDate() {
		if (this.documentQuestionDate == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		formatedDate = sdf.format(this.documentQuestionDate);		 		
		return formatedDate;
	}				
}
