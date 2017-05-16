package com.mercadodecreditos.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthenticationFailureHandlerImpl implements
		AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException error)
			throws IOException, ServletException {		

		response.sendRedirect(request.getContextPath()
				+ "/faces/public/login.xhtml?login_error=1&message="
				+ error.getMessage());
	}

}
