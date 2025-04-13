package br.com.priscyladepaula.desafioitau.security;

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

    private String apiKey;

    private static final String API_KEY_HEADER = "api-key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String apiKey = request.getHeader(API_KEY_HEADER);
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/h2-console") || requestURI.startsWith("/swagger") || requestURI.startsWith("/v3/api-docs")
                || requestURI.startsWith("/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (apiKey == null || !apiKey.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"codigo\":\"erro_autenticacao\", \"mensagem\":\"Api key incorreta ou ausente.\"}");

            return;
        }

        filterChain.doFilter(request, response);
    }
}
