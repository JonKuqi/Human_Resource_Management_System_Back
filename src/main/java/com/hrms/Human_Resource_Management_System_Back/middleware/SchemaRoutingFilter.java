package com.hrms.Human_Resource_Management_System_Back.middleware;

import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Reads the `tenant` claim from the JWT and stores it in TenantContext
 * so Hibernate can pick the right schema.
 */

/**
 * A filter that routes requests to the appropriate tenant schema based on the JWT token.
 * <p>
 * This filter extracts the "tenant" claim from the JWT token and sets the tenant context for each request.
 * It ensures that subsequent parts of the application can access the tenant-specific schema.
 * </p>
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class SchemaRoutingFilter extends OncePerRequestFilter {

    /**
     * The service responsible for extracting claims from the JWT token.
     */
    private final JwtService jwtService;

    /**
     * This method performs the filtering logic, which is executed for every HTTP request.
     * <p>
     * It extracts the JWT token from the "Authorization" header, retrieves the "tenant" claim,
     * and sets the tenant context based on the claim value. The tenant context is then available
     * for subsequent parts of the application to determine the schema to use for database queries.
     * </p>
     *
     * @param request the incoming HTTP request
     * @param response the HTTP response
     * @param chain the filter chain to pass the request and response
     * @throws ServletException if the request processing fails
     * @throws IOException if there is an error during I/O operations
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        try {
            System.out.println("______________ INSIDE Schema Routing FILTER _______________");

            // Extract the Authorization header to retrieve the JWT token
            String auth = request.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                String token = auth.substring(7); // Remove "Bearer " prefix to get the actual token

                // Extract the "tenant" claim from the JWT token
                String schema = (String) jwtService.extractClaim(token, c -> c.get("tenant"));
                System.out.println("Schema is : " + schema);

                // If a valid tenant schema is found, set it in the tenant context
                if (schema != null && !schema.isBlank()) {
                    System.out.println("FILTER IS HERE -> tenant = {}" + schema);
                    TenantCtx.setTenant(schema);  // Set the tenant schema in the context
                }
            }

            // Continue with the filter chain to process the request
            chain.doFilter(request, response);

        } finally {
            // Always clean up the tenant context after the request is processed
            TenantCtx.clear();
        }
    }
}