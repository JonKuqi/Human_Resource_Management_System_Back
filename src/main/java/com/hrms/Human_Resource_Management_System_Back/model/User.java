package com.hrms.Human_Resource_Management_System_Back.model;

import com.hrms.Human_Resource_Management_System_Back.model.types.UserRole;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

//E krijon vet tabelen ska nevoj [er
//Data is like class with constructor, getter Setter

@Data
@Entity
@Table(name = "`user`")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails { //makes it an Spring boot object

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false)
    private String username;

    private String email;
    private String salt;
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Timestamp createdOn;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name())); //Roles
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        //return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
       // return UserDetails.super.isEnabled();
        return true;
    }
}

