package com.mercadodecreditos.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.mercadodecreditos.dao.UserDao;
import com.mercadodecreditos.dao.UserDaoImpl;
import com.mercadodecreditos.model.User;

public class AuthenticationSuccessHandlerImpl implements
		AuthenticationSuccessHandler {		

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {				
		User user;
		HttpSession session;		
		user = getLoggedUser(authentication.getName());		
		session = request.getSession(false);		 
		session.setAttribute("logged", authentication.isAuthenticated());
		session.setAttribute("loggedUser", user);		
		
		response.sendRedirect(request.getContextPath()+"/faces/public/principal.xhtml");		
	}
	
	private User getLoggedUser(String login) {
		UserDao userDao = new UserDaoImpl();
		
		return userDao.getUserByLogin(login);
	}

}
