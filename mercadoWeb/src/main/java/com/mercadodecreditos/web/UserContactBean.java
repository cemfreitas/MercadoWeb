package com.mercadodecreditos.web;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "userContactBean")
@RequestScoped
public class UserContactBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5534156803803226347L;
	private String firstName;
	private String lastName;
	private String message;
	private String emailMessage;
	private String emailNewsLetter;
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	public String getEmailNewsLetter() {
		return emailNewsLetter;
	}
	public void setEmailNewsLetter(String emailNewsLetter) {
		this.emailNewsLetter = emailNewsLetter;
	}
	
	public String saveNewsLetter() {
		System.out.println("emailNewsLetter :" +emailNewsLetter);
		
		return "/public/principal";
	}
	
	public String saveContact() {
		System.out.println("firstName :" +firstName);
		System.out.println("lastName :" +lastName);
		System.out.println("message :" +message);
		System.out.println("emailMessage :" +emailMessage);
		
		return "/public/principal";
	}

}
