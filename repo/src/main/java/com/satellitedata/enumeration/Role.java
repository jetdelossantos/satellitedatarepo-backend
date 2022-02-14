package com.satellitedata.enumeration;

import static com.satellitedata.constant.Authorities.*;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_UPLOADER(UPLOADER_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
