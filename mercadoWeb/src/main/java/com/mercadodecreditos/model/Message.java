package com.mercadodecreditos.model;

import java.io.Serializable;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 860390824077974380L;
	private Document document;
	private User userBid;
	private String fakeUser;
	private String raiseBidRequest;
	
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public User getUserBid() {
		return userBid;
	}
	public void setUserBid(User userBid) {
		this.userBid = userBid;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		result = prime * result
				+ ((fakeUser == null) ? 0 : fakeUser.hashCode());
		result = prime * result
				+ ((raiseBidRequest == null) ? 0 : raiseBidRequest.hashCode());
		result = prime * result + ((userBid == null) ? 0 : userBid.hashCode());
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
		Message other = (Message) obj;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (fakeUser == null) {
			if (other.fakeUser != null)
				return false;
		} else if (!fakeUser.equals(other.fakeUser))
			return false;
		if (raiseBidRequest == null) {
			if (other.raiseBidRequest != null)
				return false;
		} else if (!raiseBidRequest.equals(other.raiseBidRequest))
			return false;
		if (userBid == null) {
			if (other.userBid != null)
				return false;
		} else if (!userBid.equals(other.userBid))
			return false;
		return true;
	}
	public String getFakeUser() {
		return fakeUser;
	}
	public void setFakeUser(String fakeUser) {
		this.fakeUser = fakeUser;
	}
	public String getRaiseBidRequest() {
		return raiseBidRequest;
	}
	public void setRaiseBidRequest(String raiseBidRequest) {
		this.raiseBidRequest = raiseBidRequest;
	}
	
	
}
