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
@Table(name="SalesFeedback")
public class SalesFeedback implements Serializable {	
	private static final long serialVersionUID = -8598846110829762605L;
	
	@Id @GeneratedValue
	@Column(name = "xidSalesFeedback")
	private long xidSalesFeedback;
	
	private String description;
	private Calendar salesFeedbackDate;
	@ManyToOne
	@JoinColumn(name="idUserSales")
	private UserSales userSales;
	@ManyToOne
	@JoinColumn(name="idFeedback")
	private Feedback feedback;
	
	public long getXidSalesFeedback() {
		return xidSalesFeedback;
	}
	public void setXidSalesFeedback(long xidSalesFeedback) {
		this.xidSalesFeedback = xidSalesFeedback;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Calendar getSalesFeedbackDate() {
		return salesFeedbackDate;
	}
	public void setSalesFeedbackDate(Calendar salesFeedbackDate) {
		this.salesFeedbackDate = salesFeedbackDate;
	}
	public UserSales getUserSales() {
		return userSales;
	}
	public void setUserSales(UserSales userSales) {
		this.userSales = userSales;
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
				+ (int) (xidSalesFeedback ^ (xidSalesFeedback >>> 32));
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
		SalesFeedback other = (SalesFeedback) obj;
		if (xidSalesFeedback != other.xidSalesFeedback)
			return false;
		return true;
	}
}
