package com.membership.membershipsample.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.membership.membershipsample.member.entity.Member;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class UserDetailsImpl implements UserDetails {
    private String userId;
    @JsonIgnore
    @ToString.Exclude
    private String password;
    private String userName;
    private String roleGroup;
    private Collection<? extends GrantedAuthority> authorities;

    @ConstructorProperties(
        {
            "id",
            "seqNo",
            "username",
            "email",
            "profileImage",
            "roleGroup",
            "authType",
            "status",
            "accountNonExpired",
            "accountNonLocked",
            "credentialsNonExpired",
            "enabled",
            "authorities",
        }
    )
    public UserDetailsImpl(
        String userId,
        String userName,
        List<Map<String, String>> authorities
    ) {
        this.userId = userId;
        this.userName = userName;
        authorities.forEach(
            e -> {
                this.authorities = Collections.singleton(new SimpleGrantedAuthority(e.get("authority").toString()));
            }
        );
    }

    public UserDetailsImpl(Member user) {
        this.userId = user.getEmail();
        this.userName = user.getName();
        this.password = "";
        this.roleGroup = user.getRoleGroup();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRoleGroup()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.userName;
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
