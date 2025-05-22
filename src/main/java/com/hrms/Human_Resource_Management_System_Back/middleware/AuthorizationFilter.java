package com.hrms.Human_Resource_Management_System_Back.middleware;


import com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto;
import com.hrms.Human_Resource_Management_System_Back.model.tenant.UserRole;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.RolePermissionService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.UserRoleService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This filter is responsible for performing authorization checks based on roles and permissions.
 * <p>
 * It intercepts incoming HTTP requests, extracts the JWT token, and checks whether the user has the necessary
 * permissions to access the requested resource. Additionally, it handles path rewrites based on role-based access control (RBAC).
 * </p>
 */
@Component
@Order(2)
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    /**
     * Service for fetching role-based permissions.
     */
    private final RolePermissionService rolePermissionService;

    /**
     * Service for managing user roles.
     */
    private final UserRoleService userRoleService;

    /**
     * Service for handling JWT token extraction and validation.
     */
    private final JwtService jwtService;

    /**
     * Path matcher used for matching paths based on the configured permissions.
     */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * Determines whether the request should be filtered.
     * <p>
     * This method skips filtering for paths under "/api/v1/public", which are assumed to be public endpoints.
     * </p>
     *
     * @param request the incoming HTTP request
     * @return {@code true} if the request should be skipped, otherwise {@code false}
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        boolean skip = path.startsWith("/api/v1/public");
        System.out.println("[AuthorizationFilter] shouldNotFilter: {} for path={} " + skip + "    " + path);
        return skip;
    }

    /**
     * This method performs the authorization logic by checking user roles and permissions.
     * <p>
     * It extracts the JWT token from the authorization header, checks the global role (GENERAL_USER, TENANT_USER),
     * and then performs role-based access control by checking the permissions assigned to the user for the requested resource.
     * It also handles path rewrites based on the roles of the user.
     * </p>
     *
     * @param request the incoming HTTP request
     * @param response the HTTP response
     * @param chain the filter chain
     * @throws ServletException if the request processing fails
     * @throws IOException if there is an error during I/O operations
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {

        if (shouldNotFilter(request)) {
            chain.doFilter(request, response);
            return;
        }
        System.out.println("SHOULD NOT FILTER WAS NOT EXECUTED");

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("______________INSIDE AUTHORIZATION FILTER_______________");

            // 1) Extract the global role from the JWT token (e.g. SYSTEM_ADMIN vs TENANT_USER)
            String globalRole = (String) jwtService.extractClaim(token, c -> c.get("role"));

            if ("GENERAL_USER".equals(globalRole)) {
                // If the user is a general user, allow the request without RBAC checks
                chain.doFilter(request, response);
                return;
            }
            if (!"TENANT_USER".equals(globalRole)) {
                // If the user is not a tenant user, skip RBAC checks
                chain.doFilter(request, response);
                return;
            }

            // 2) Extract the user ID from the JWT token (tenant user ID)
            Integer userId = ((Number) jwtService.extractClaim(token, c -> c.get("user_id"))).intValue();

            // 3) Fetch the list of permissions for the user
            List<UserRolePermissionDto> allowed = rolePermissionService.getUserRolePermissions(userId);

            // Check if the user is an OWNER, as owners have all permissions
            boolean isOwner = false;
            List<UserRole> rolesOfUser = userRoleService.getUserRoles(userId);
            for (UserRole userRole : rolesOfUser) {
                if (userRole.getRole().getRoleName().equals("OWNER")) {
                    isOwner = true;
                    break;
                }
            }
            if (isOwner) {
                System.out.println(" IS OWNER: every request is ALLOWED!");
                chain.doFilter(request, response);
                return;
            }else{
                System.out.println(" IS NOT OWNER: every request is NOT ALLOWED!");
            }

            String verb = request.getMethod(); // GET / POST / PUT / DELETE ...
            String path = request.getRequestURI(); // full request path
            System.out.println("Requested verb: " + verb);
            System.out.println("Requested path: " + path);




            boolean permitted = false;
            List<String> targetRoles = new ArrayList<>();



            System.out.println("🟢 User Permissions:");
            System.out.printf("%-10s | %-50s%n", "VERB", "RESOURCE");
            System.out.println("-----------+----------------------------------------------------");

            // Iterate over each permission and check if the user has access to the requested verb and resource
            for (UserRolePermissionDto p : allowed) {
                System.out.printf("%-10s | %-50s%n", p.getVerb(), p.getResource());

                String permissionPath = p.getResource();
                if (permissionPath.endsWith("/") &&
                        path.startsWith(permissionPath) &&
                        path.substring(permissionPath.length()).matches("\\d+")&&
                        verb.equalsIgnoreCase(p.getVerb())) {
                    // This handles cases where the permission is "/some/path/" and request is "/some/path/123"
                    permitted = true;
                    if (p.getTarget_role() != null) {
                        targetRoles.add(p.getTarget_role());
                    }
                }
                // Keep the original check for other cases
                else if (verb.equalsIgnoreCase(p.getVerb()) && pathMatcher.match(p.getResource(), path)) {
                    permitted = true;
                    if (p.getTarget_role() != null) {
                        targetRoles.add(p.getTarget_role());
                    }
                }
            }

            if (!permitted) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                System.out.println("The request is FORBIDDEN.");
                return;
            }

            // Set the authentication context for the user
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // ────────────────────── PATH REWRITE ──────────────────────
            HttpServletRequest reqToUse = request; // default: unchanged

            if (!targetRoles.isEmpty() && request.getAttribute("rbRewriteDone") == null) {
                // Prepend "/role-based" to the path once, keeping the query string intact
                String original = request.getRequestURI(); // e.g., "/api/v1/tenant/user-tenant"
                String suffix = "/role-based";

                String rewritten = original.endsWith(suffix)
                        ? original // already suffixed → do nothing
                        : original + suffix; // append once

                reqToUse = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getRequestURI() {
                        return rewritten;
                    }

                    @Override
                    public String getServletPath() {
                        return rewritten;
                    }

                    @Override
                    public String getPathInfo() {
                        return rewritten;
                    }
                };
                reqToUse.setAttribute("rbRewriteDone", Boolean.TRUE);
                reqToUse.setAttribute("target_roles", targetRoles); // pass the list of target roles
            }
        }

        System.out.println("Authorization Filter OUTSIDE");
        chain.doFilter(request, response); // Continue processing the filter chain
    }
}