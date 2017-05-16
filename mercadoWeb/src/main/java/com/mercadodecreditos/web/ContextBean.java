package com.mercadodecreditos.web;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.mercadodecreditos.model.User;

@ManagedBean(name = "contextBean")
@SessionScoped
public class ContextBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9119725528818830982L;
	private User loggedUser;
	private boolean logged, adminUser;
	private FacesContext context;

	public boolean isLogged() {
		context = FacesContext.getCurrentInstance();

		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		
		if (session == null || session.getAttribute("logged")==null) {
			return false;
		}

		logged = (boolean) session.getAttribute("logged");

		return logged;

	}

	public User getLoggedUser() {
		context = FacesContext.getCurrentInstance();

		HttpSession session = (HttpSession) context.getExternalContext()
				.getSession(false);
		
		if (session == null || session.getAttribute("loggedUser")==null) {
			return null;
		}

		loggedUser = (User) session.getAttribute("loggedUser");
		
		return loggedUser;
	}

	public boolean isAdminUser() {		
		if (loggedUser == null) {
			getLoggedUser();
			if (loggedUser == null) {
				return false;
			}
		}		
		adminUser = loggedUser.isAdmin();		
		return adminUser;
	}

}
