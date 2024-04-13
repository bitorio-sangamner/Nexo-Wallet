package com.authentication.config.jpaUserDetailsService;

import com.authentication.entities.AuthUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Arrays;
import java.util.Collection;

@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private final AuthUser authUser;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authUser
                        .getRoles()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
