package com.hrms.Human_Resource_Management_System_Back.middleware;

import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads the `tenant` claim from the JWT and stores it in TenantContext
 * so Hibernate can pick the right schema.
 */

@Component
@RequiredArgsConstructor
public class SchemaRoutingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        try {
            System.out.println("______________ INSIDE FILTER _______________");
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7);


                // read the "tenant" claim directly; no need to wait for authentication
                String schema = (String) jwtService.extractClaim(token, c -> c.get("tenant"));
                if (schema != null && !schema.isBlank()) {
                    System.out.println("FILTER IS HERE -> tenant = {}"+ schema);
                    TenantCtx.setTenant(schema);
                }

            }
            chain.doFilter(request, response);   // continue

        } finally {
            TenantCtx.clear();                // always clean up
        }
    }
}