package com.backend.cafe.model;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;


@NamedQuery(name = "User.findByUsername", query = "select u from User u where u.username=:username ")

@NamedQuery(name = "User.getAllUsers", query = "select new com.backend.cafe.wrapper.UserWrapper(u.id, u.name, u.username, u.mobileNumber, u.status, u.role) from User u")

@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id")

@NamedQuery(name = "User.getAllAdmins", query = "select new com.backend.cafe.wrapper.UserWrapper(u.id, u.name, u.username, u.mobileNumber, u.status, u.role) from User u where u.role='admin'")


@Entity
@Data // The Data annotation takes care of boilerplate no arg constructor, getters, and setters
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "username")
    private String username;// we use this instead of 'email' for jwt set up...

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(role));
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