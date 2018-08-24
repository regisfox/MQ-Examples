package br.gov.caixa.simtx.web.configuracao;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureException;

public class JWTFilter implements Filter{
	
	private static final Logger logger = Logger.getLogger(JWTFilter.class);

	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	//init null
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");

        if(request.getRequestURI().startsWith("/web/sessao/*")){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String token = request.getHeader(JWTUtil.TOKEN_HEADER);

        if(token == null || token.trim().isEmpty()){
            response.setStatus(401);
            return;
        }

        try {
            Jws<Claims> parser = JWTUtil.decodificaToken(token);
            if(parser!= null){
            	logger.debug("Requisicao usuario: "+ parser.getBody().getSubject());
            	filterChain.doFilter(servletRequest, servletResponse);
            } else {
            	logger.info("Sessao Finalizada usuario: ");
            	response.setStatus(401);
            }
        } catch (SignatureException e) {
            response.setStatus(401);
        }
    }

    @Override
    public void destroy() {
    	//destroy null
    }
}