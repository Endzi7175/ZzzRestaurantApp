package com.ktsnwt.restaurant.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";


    public static final String MANAGER = "ROLE_MANAGER";

    public static final String WAITER = "ROLE_WAITER";
    public static final String BARMAN = "ROLE_BARMAN";
    public static final String COOK = "ROLE_COOK";



    private AuthoritiesConstants() {}
}
