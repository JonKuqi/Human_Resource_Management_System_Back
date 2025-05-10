package com.hrms.Human_Resource_Management_System_Back.middleware;


import com.hrms.Human_Resource_Management_System_Back.model.dto.UserRolePermissionDto;
import com.hrms.Human_Resource_Management_System_Back.service.JwtService;
import com.hrms.Human_Resource_Management_System_Back.service.tenant.RolePermissionService;
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

@Component
@Order(2)
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final RolePermissionService rolePermissionService;
    private final JwtService jwtService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // use servletPath so you don’t accidentally include context-path
        String path = request.getServletPath();
        boolean skip = path.startsWith("/api/v1/public");
        System.out.println("[AuthorizationFilter] shouldNotFilter: {} for path={} " + skip + "    "+ path);
        return skip;
    }


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

            // 1) Global role inside the token (e.g. SYSTEM_ADMIN vs TENANT_USER)
            String globalRole = (String) jwtService.extractClaim(token, c -> c.get("role"));
            if (!"TENANT_USER".equals(globalRole)) {
                chain.doFilter(request, response);   // not a tenant user → skip RBAC
                return;
            }

            // 2) Tenant user id – JWT stores it as number, not String
            Integer userId = ((Number) jwtService.extractClaim(token, c -> c.get("user_id"))).intValue();

            // 3) Fetch every <verb,resource> the user is allowed to access
            List<UserRolePermissionDto> allowed = rolePermissionService.getUserRolePermissions(userId);

            String verb = request.getMethod();        // GET / POST / PUT / DELETE …
            String path = request.getRequestURI();    // full request path
            System.out.println("Requested verb: " + verb);
            System.out.println("Requested path: " + path);

            boolean permitted = false;
            List<String> targetRoles = new ArrayList<>();

            System.out.println("🟢 User Permissions:");
            System.out.printf("%-10s | %-50s%n", "VERB", "RESOURCE");
            System.out.println("-----------+----------------------------------------------------");


            for (UserRolePermissionDto p : allowed) {
                System.out.printf("%-10s | %-50s%n", p.getVerb(), p.getResource());
                if (verb.equalsIgnoreCase(p.getVerb())
                        && pathMatcher.match(p.getResource(), path)) {

                    permitted = true;

                    if (p.getTarget_role() != null) {          // ★ this triggers rewrite
                        targetRoles.add(p.getTarget_role());
                    }
                }
            }

            if (!permitted) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                System.out.println("The request is FORBIDEN.");
                return;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            /* ────────────────────── PATH REWRITE ────────────────────── */
            HttpServletRequest reqToUse = request;            // default: unchanged

            if (!targetRoles.isEmpty()
                    && request.getAttribute("rbRewriteDone") == null) {

                // prepend "/role-based" once, keep query‑string intact
                String original = request.getRequestURI();         // e.g. "/api/v1/tenant/user-tenant"
                String suffix   = "/role-based";

                String rewritten = original.endsWith(suffix)
                        ? original                                   // already suffixed → do nothing
                        : original + suffix;                         // append once


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
                reqToUse.setAttribute("target_roles", targetRoles); // still pass the list
            }
        }
        System.out.println("Authorization Filter OUTSIDE");
        chain.doFilter(request, response);   // continue

    }
}
