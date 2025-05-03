package com.hrms.Human_Resource_Management_System_Back.middleware;


// WORKS like a SESSIOn
public class TenantCtx {

    private static final ThreadLocal<String> CURRENT_TENANT = ThreadLocal.withInitial(() -> "public");
    static {
        CURRENT_TENANT.set("public");
    }


    public static void setTenant(String tenant) {
        System.out.println("SET THE TENANT: "+ tenant);
        CURRENT_TENANT.set(tenant);
    }

    public static String getTenant() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}