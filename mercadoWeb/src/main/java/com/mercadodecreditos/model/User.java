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

import org.hibernate.annotations.Type;

@Entity
@Table(name="User")
public class User implements Serializable{	
	private static final long serialVersionUID = -3589539918401596147L;

	@Id @GeneratedValue
	@Column(name = "xidUser")
	private long xidUser;
	
	private String name;
	private String login;
	private String companyName;
	private String password;
	private String address;
	private String addressNumber;
	private String addressComp;
	private String neighborhood;
	@ManyToOne
	@JoinColumn(name="idState")
	private State state;
	@ManyToOne
	@JoinColumn(name="idCity")
	private City city;
	private String zipCode;
	private Date birthDate;
	private String resPhone;
	private String commPhone;
	private String celPhone;
	private String CPF;
	private String CNPJ;
	private String email;
	@ManyToOne
	@JoinColumn(name="idUserType")
	private UserType userType;
	@Type(type = "yes_no")
	private boolean isAdmin;
	@ManyToOne
	@JoinColumn(name = "lastSituation")
	private Situation lastSituation;
	
	
	public Situation getLastSituation() {
		return lastSituation;
	}
	public void setLastSituation(Situation lastSituation) {
		this.lastSituation = lastSituation;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public long getXidUser() {
		return xidUser;
	}
	public void setXidUser(long xidUser) {
		this.xidUser = xidUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getResPhone() {
		return resPhone;
	}
	public void setResPhone(String resPhone) {
		this.resPhone = resPhone;
	}
	public String getCommPhone() {
		return commPhone;
	}
	public void setCommPhone(String commPhone) {
		this.commPhone = commPhone;
	}
	public String getCelPhone() {
		return celPhone;
	}
	public void setCelPhone(String celPhone) {
		this.celPhone = celPhone;
	}
	public String getCPF() {
		return CPF;
	}
	public void setCPF(String cPF) {
		CPF = cPF;
	}
	public String getCNPJ() {
		return CNPJ;
	}
	public void setCNPJ(String cNPJ) {
		CNPJ = cNPJ;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserType getUserType() {
		return userType;
	}
	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	public City getCity() {
		return city;
	}
	public void setCity(City city) {
		this.city = city;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (xidUser ^ (xidUser >>> 32));
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
		User other = (User) obj;
		if (xidUser != other.xidUser)
			return false;
		return true;
	}
	public String getAddressNumber() {
		return addressNumber;
	}
	public void setAddressNumber(String addressNumber) {
		this.addressNumber = addressNumber;
	}
	public String getAddressComp() {
		return addressComp;
	}
	public void setAddressComp(String addressComp) {
		this.addressComp = addressComp;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	

}
