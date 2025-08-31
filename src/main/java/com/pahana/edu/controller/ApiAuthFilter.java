package com.pahana.edu.controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebFilter("/api/*") 
public class ApiAuthFilter implements Filter {

    
    private static final String MASTER_API_KEY = "PAHANA-EDU-SECRET-KEY-12345";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        
        String apiKey = httpRequest.getHeader("X-API-Key");

        
        if (MASTER_API_KEY.equals(apiKey)) {
            
            chain.doFilter(request, response);
        } else {
            
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Unauthorized. A valid X-API-Key header is required.\"}");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // This method is called when the filter is initialized.
        // You can add initialization logic here if needed.
    }

    @Override
    public void destroy() {
        // This method is called when the filter is taken out of service.
    }
}
