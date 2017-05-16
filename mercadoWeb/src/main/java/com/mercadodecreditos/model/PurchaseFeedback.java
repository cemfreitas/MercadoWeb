package com.mercadodecreditos.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="PurchaseFeedback")
public class PurchaseFeedback implements Serializable {	
	private static final long serialVersionUID = -6077855867339355340L;
	
	@Id @GeneratedValue
	@Column(name = "xidPurchaseFeedback")
	private long xidPurchaseFeedback;
	
	private String description;
	private Calendar purchaseFeedbackDate;
	@ManyToOne
	@JoinColumn(name="idUserPurchase")
	private UserPurchase userPurchase;
	@ManyToOne
	@JoinColumn(name="idFeedback")
	private Feedback feedback;
	
	public long getXidPurchaseFeedback() {
		return xidPurchaseFeedback;
	}
	public void setXidPurchaseFeedback(long xidPurchaseFeedback) {
		this.xidPurchaseFeedback = xidPurchaseFeedback;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getPurchaseFeedbackDate() {
		return purchaseFeedbackDate;
	}
	public void setPurchaseFeedbackDate(Calendar purchaseFeedbackDate) {
		this.purchaseFeedbackDate = purchaseFeedbackDate;
	}
	public UserPurchase getUserPurchase() {
		return userPurchase;
	}
	public void setUserPurchase(UserPurchase userPurchase) {
		this.userPurchase = userPurchase;
	}
	public Feedback getFeedback() {
		return feedback;
	}
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (xidPurchaseFeedback ^ (xidPurchaseFeedback >>> 32));
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
		PurchaseFeedback other = (PurchaseFeedback) obj;
		if (xidPurchaseFeedback != other.xidPurchaseFeedback)
			return false;
		return true;
	}
	

}
