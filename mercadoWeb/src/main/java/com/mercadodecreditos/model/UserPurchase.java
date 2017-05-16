package com.mercadodecreditos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "UserPurchase")
public class UserPurchase implements Serializable {
	private static final long serialVersionUID = 7611379607849395396L;

	@Id
	@GeneratedValue
	@Column(name = "xidUserPurchase")
	private long xidUserPurchase;

	@ManyToOne
	@JoinColumn(name = "idUser")
	private User user;
	@ManyToOne
	@JoinColumn(name = "idDocument")
	private Document document;
	@ManyToOne
	@JoinColumn(name = "idUserSeller")
	private User userSeller;
	private BigDecimal purchaseValue;
	private Calendar purchaseDate;

	public long getXidUserPurchase() {
		return xidUserPurchase;
	}

	public void setXidUserPurchase(long xidUserPurchase) {
		this.xidUserPurchase = xidUserPurchase;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public User getUserSeller() {
		return userSeller;
	}

	public void setUserSeller(User userSeller) {
		this.userSeller = userSeller;
	}

	public BigDecimal getPurchaseValue() {
		return purchaseValue;
	}

	public void setPurchaseValue(BigDecimal purchaseValue) {
		if (purchaseValue != null) {
			this.purchaseValue = purchaseValue.setScale(2,
					BigDecimal.ROUND_HALF_EVEN);
		} else {
			this.purchaseValue = null;
		}
	}

	public Calendar getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Calendar purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (xidUserPurchase ^ (xidUserPurchase >>> 32));
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
		UserPurchase other = (UserPurchase) obj;
		if (xidUserPurchase != other.xidUserPurchase)
			return false;
		return true;
	}

}
