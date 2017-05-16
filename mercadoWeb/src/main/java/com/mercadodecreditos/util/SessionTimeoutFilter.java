package com.mercadodecreditos.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionTimeoutFilter implements Filter {

	private final Log logger = LogFactory.getLog(SessionTimeoutFilter.class);

	private String timeoutPage = "faces/public/principal.xhtml";

	/*
	 * (non-Jsdoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/*
	 * (non-Jsdoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		if ((request instanceof HttpServletRequest)
				&& (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			// is session expire control required for this request?
			if (isSessionControlRequiredForThisResource(httpServletRequest)) {

				// is session invalid?
				if (isSessionInvalid(httpServletRequest)) {
					String timeoutUrl = httpServletRequest.getContextPath()
							+ "/" + getTimeoutPage();
					logger.info("session is invalid! redirecting to timeoutpage : "
							+ timeoutUrl);

					logger.info("Control on page : "
							+ httpServletRequest.getRequestURI());

					String facesRequest = httpServletRequest
							.getHeader("Faces-Request");
					if (facesRequest != null
							&& facesRequest.equals("partial/ajax")) {

						String url = MessageFormat.format(

						"{0}://{1}:{2,number,####0}{3}/",

						request.getScheme(), request.getServerName(),

						request.getServerPort(),
								httpServletRequest.getContextPath());

						PrintWriter pw = response.getWriter();

						pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

						pw.println("<partial-response><redirect url=\"" + url

						+ "\"></redirect></partial-response>");

						pw.flush();

					} else {
						httpServletResponse.sendRedirect(timeoutPage);
					}

					return;
				}
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * session shouldn't be checked for some pages. For example: for timeout
	 * page.. Since we're redirecting to timeout page from this filter, if we
	 * don't disable session control for it, filter will again redirect to it
	 * and this will be result with an infinite loop...
	 *
	 * @param httpServletRequest
	 *            the http servlet request
	 * @return true, if is session control required for this resource
	 */
	private boolean isSessionControlRequiredForThisResource(
			HttpServletRequest httpServletRequest) {
		String requestPath = httpServletRequest.getRequestURI();

		boolean controlRequiredLogin = !StringUtils.contains(requestPath,
				"principal");

		return !controlRequiredLogin ? false : true;

	}

	/**
	 * Checks if is session invalid.
	 *
	 * @param httpServletRequest
	 *            the http servlet request
	 * @return true, if is session invalid
	 */
	private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {
		boolean sessionInValid = (httpServletRequest.getRequestedSessionId() != null)
				&& !httpServletRequest.isRequestedSessionIdValid();
		return sessionInValid;
	}

	/*
	 * (non-Jsdoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * Gets the timeout page.
	 *
	 * @return the timeout page
	 */
	public String getTimeoutPage() {
		return timeoutPage;
	}

	/**
	 * Sets the timeout page.
	 *
	 * @param timeoutPage
	 *            the new timeout page
	 */
	public void setTimeoutPage(String timeoutPage) {
		this.timeoutPage = timeoutPage;
	}

}
