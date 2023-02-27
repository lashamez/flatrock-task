package com.flatrock.common.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String CLIENT = "ROLE_CLIENT";

    public static final String SELLER = "ROLE_SELLER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String INTERNAL = "ROLE_INTERNAL";

    private AuthoritiesConstants() {}
}
