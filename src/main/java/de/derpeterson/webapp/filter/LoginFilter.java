package de.derpeterson.webapp.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.derpeterson.webapp.constants.KnownPages;
import de.derpeterson.webapp.controller.UserManager;


public class LoginFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        
        try {
	        // Managed bean name is exactly the session attribute name
        	UserManager userManager = null;
        	// false before testing with true
        	if(Objects.nonNull(httpServletRequest.getSession(true)) && httpServletRequest.getSession(true).getAttribute("userManager") instanceof UserManager) {
        		userManager = (UserManager)httpServletRequest.getSession(true).getAttribute("userManager");
        	}
	
	        if (Objects.nonNull(userManager)) {
	            if (userManager.isLoggedIn()) {
	                LOGGER.debug("User '{}({})' is logged in", userManager.getUser().getUsername(), userManager.getUser().getEmail());
	                // user is logged in, continue request
	                filterChain.doFilter(servletRequest, servletResponse);
	            } else {
	                LOGGER.debug("User '{}' is not logged in", userManager);
	                // user is not logged in, redirect to login page
	                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + KnownPages.LOGIN_PATTERN);
	            }
	        } else {
	            LOGGER.debug("UserManager not found");
	            // user is not logged in, redirect to login page
	            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + KnownPages.LOGIN_PATTERN);
	        }
        } catch(Exception e) {
        	LOGGER.error("LoginFilter Exception", e);
        }
        
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		LOGGER.debug("LoginFilter initialized");		
	}
	
	@Override
	public void destroy() {}

}
