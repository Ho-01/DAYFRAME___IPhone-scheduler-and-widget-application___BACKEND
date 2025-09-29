package com.BUS.DayFrame.security.service;

import com.BUS.DayFrame.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final Long userId;
    private final String email;
    private final String password;

    public CustomUserDetails(User user){
        this.userId = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public Long getUserId(){
        return userId;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return null;}
    @Override
    public boolean isAccountNonExpired() {return UserDetails.super.isAccountNonExpired();}
    @Override
    public boolean isAccountNonLocked() {return UserDetails.super.isAccountNonLocked();}
    @Override
    public boolean isCredentialsNonExpired() {return UserDetails.super.isCredentialsNonExpired();}
    @Override
    public boolean isEnabled() {return UserDetails.super.isEnabled();}
}
