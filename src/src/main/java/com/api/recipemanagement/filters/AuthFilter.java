package com.api.recipemanagement.filters;

import com.api.recipemanagement.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuthFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // Get Auth header from request
        String authHeader = httpServletRequest.getHeader("Authorization");
        // Get token from Auth header
        if(authHeader != null) {
            String[] authHeaderArr = authHeader.split("Bearer ");
            if(authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                try {
                    // validate token and extract claims in token
                    Claims claims = Jwts.parser().setSigningKey(Constants.TOKEN_SECRET_KEY)
                            .parseClaimsJws(token).getBody();

                    // extract userId claim and set as attribute of Http request for use in processing request
                    httpServletRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));

                } catch (Exception e) {
                    httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/Expired token");
                    return;
                }
            } else {
                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be in format 'Bearer [token]'");
                return;
            }
        } else {
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token is required");
            return;
        }

        /* If execution comes here it means token is valid
         * Invoke chain.doFilter method to continue processing the request */
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
