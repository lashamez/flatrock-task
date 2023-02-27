package com.flatrock.common.jwt;

import com.flatrock.common.security.AuthoritiesConstants;
import com.flatrock.common.security.ServiceEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class MicroserviceAuthentication implements Authentication {

    private final ServiceEnum serviceEnum;
    private final List<GrantedAuthority> authorities;
    private boolean isAuthenticated;

    public MicroserviceAuthentication(ServiceEnum serviceEnum) {
        this.serviceEnum = serviceEnum;
        this.authorities = List.of(new SimpleGrantedAuthority(AuthoritiesConstants.INTERNAL));
        this.isAuthenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return serviceEnum.name();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return serviceEnum.name();
    }
}

