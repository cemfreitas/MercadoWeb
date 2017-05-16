package com.mercadodecreditos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Subselect("select dq.userBid,dq.bid,dq.documentQuestionDate as dateMessage,dq.Document " +
		"from Document doc join doc.documentQuestion as dq")
@Synchronize( {"Document", "DocumentQuestion"} )
@Immutable
public class MessageSent implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9148857099760493129L;
	private User userBid;	
	private BigDecimal bid;
	private Date dateMessage;
	private Document document;
	
	public User getUserBid() {
		return userBid;
	}
	public void setUserBid(User userBid) {
		this.userBid = userBid;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}	
	public BigDecimal getBid() {
		return bid;
	}
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}
	public Date getDateMessage() {
		return dateMessage;
	}
	public void setDateMessage(Date dateMessage) {
		this.dateMessage = dateMessage;
	}

}
