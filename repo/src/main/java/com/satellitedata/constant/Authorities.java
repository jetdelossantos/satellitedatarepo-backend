package com.satellitedata.constant;

public class Authorities {
    public static final String[] USER_AUTHORITIES = { "user:read", "satellitedata:read" };
    public static final String[] UPLOADER_AUTHORITIES = { "user:read", "satellitedata:read", "satellitedata:create"  };
    public static final String[] MANAGER_AUTHORITIES = { "user:read", "user:update", "satellitedata:read", "satellitedata:create" };
    public static final String[] ADMIN_AUTHORITIES = { "user:read", "user:create", "user:update","satellitedata:read", "satellitedata:create", "satellitedata:update"};
    public static final String[] SUPER_ADMIN_AUTHORITIES = { "user:read", "user:create", "user:update", "user:delete","satellitedata:read", "satellitedata:create", "satellitedata:update", "satellitedata:delete" };
}
