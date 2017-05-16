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
@Table(name="UserSituation")
public class UserSituation implements Serializable {

	private static final long serialVersionUID = -8418117202664908967L;
	
	@Id @GeneratedValue
	@Column(name = "xidUserSituation")
	private long xidUserSituation;

	@ManyToOne
	@JoinColumn(name="idUser")
	private User user;
	@ManyToOne
	@JoinColumn(name="idSituation")
	private Situation situation;
	private String description;
	private Date userSituationDate;
	
	
	public long getXidUserSituation() {
		return xidUserSituation;
	}
	public void setXidUserSituation(long xidUserSituation) {
		this.xidUserSituation = xidUserSituation;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getUserSituationDate() {
		return userSituationDate;
	}
	public void setUserSituationDate(Date userSituationDate) {
		this.userSituationDate = userSituationDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (xidUserSituation ^ (xidUserSituation >>> 32));
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
		UserSituation other = (UserSituation) obj;
		if (xidUserSituation != other.xidUserSituation)
			return false;
		return true;
	}
	public Situation getSituation() {
		return situation;
	}
	public void setSituation(Situation situation) {
		this.situation = situation;
	} 
}
