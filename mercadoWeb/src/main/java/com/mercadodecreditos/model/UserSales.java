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
@Table(name = "UserSales")
public class UserSales implements Serializable {
	private static final long serialVersionUID = -1430436394080225029L;

	@Id
	@GeneratedValue
	@Column(name = "xidUserSales")
	private long xidUserSales;

	@ManyToOne
	@JoinColumn(name = "idUser")
	private User user;
	@ManyToOne
	@JoinColumn(name = "idDocument")
	private Document document;
	@ManyToOne
	@JoinColumn(name = "idUserBuyer")
	private User userBuyer;
	private BigDecimal salesValue;
	private Calendar salesDate;

	public long getXidUserSales() {
		return xidUserSales;
	}

	public void setXidUserSales(long xidUserSales) {
		this.xidUserSales = xidUserSales;
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

	public User getUserBuyer() {
		return userBuyer;
	}

	public void setUserBuyer(User userBuyer) {
		this.userBuyer = userBuyer;
	}

	public BigDecimal getSalesValue() {
		return salesValue;
	}

	public void setSalesValue(BigDecimal salesValue) {
		if (salesValue != null) {
			this.salesValue = salesValue
					.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		} else {
			salesValue = null;
		}
	}

	public Calendar getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Calendar salesDate) {
		this.salesDate = salesDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (xidUserSales ^ (xidUserSales >>> 32));
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
		UserSales other = (UserSales) obj;
		if (xidUserSales != other.xidUserSales)
			return false;
		return true;
	}

}
