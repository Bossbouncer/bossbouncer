/*
package com.rating.bossbouncer.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.apiKey}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Check if the request URI matches the endpoint
        if (requestURI.equals("/api/report/bosses/ratings-summary")) {
            String headerApiKey = request.getHeader("X-API-KEY");

            // Validate the API Key
            if (headerApiKey == null || !headerApiKey.equals(apiKey)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Invalid API Key");
                return; // Stop the filter chain
            }

            // If API Key is valid, continue the filter chain
        }

        chain.doFilter(request, response);
    }
}

*/
