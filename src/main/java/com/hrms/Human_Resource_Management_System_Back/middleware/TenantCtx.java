package com.hrms.Human_Resource_Management_System_Back.middleware;


/**
 * A utility class for managing the current tenant context in a multi-tenant application.
 * <p>
 * This class provides methods to set, get, and clear the current tenant for each thread.
 * It uses {@link ThreadLocal} to ensure that each request thread has its own tenant context,
 * providing tenant isolation in multi-tenant applications.
 * </p>
 */
public class TenantCtx {

    /**
     * Thread-local variable that holds the current tenant identifier for each thread.
     * <p>
     * This ensures that each thread (e.g., each request in a web application) can independently
     * store and retrieve the tenant information, avoiding cross-thread contamination of tenant data.
     * The default tenant is set to "public".
     * </p>
     */
    private static final ThreadLocal<String> CURRENT_TENANT = ThreadLocal.withInitial(() -> "public");

    /**
     * Static block to initialize the tenant to "public".
     * <p>
     * This ensures that the default tenant is set to "public" when the class is loaded,
     * before any tenant-specific data is set.
     * </p>
     */
    static {
        CURRENT_TENANT.set("public");
    }

    /**
     * Sets the current tenant for the current thread.
     * <p>
     * This method is used to set the tenant for the current request thread. It stores the tenant information
     * in the {@link ThreadLocal} variable, making it available to the thread for the duration of the request.
     * </p>
     *
     * @param tenant the tenant identifier (e.g., schema name)
     */
    public static void setTenant(String tenant) {
        System.out.println("SET THE TENANT: " + tenant);
        CURRENT_TENANT.set(tenant);
    }

    /**
     * Gets the current tenant for the current thread.
     * <p>
     * This method retrieves the tenant identifier from the {@link ThreadLocal} variable,
     * which provides the tenant context for the current thread.
     * </p>
     *
     * @return the current tenant identifier (e.g., schema name)
     */
    public static String getTenant() {
        return CURRENT_TENANT.get();
    }

    /**
     * Clears the current tenant for the current thread.
     * <p>
     * This method removes the tenant context for the current thread, ensuring that no tenant-specific
     * data persists beyond the scope of the request.
     * </p>
     */
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}