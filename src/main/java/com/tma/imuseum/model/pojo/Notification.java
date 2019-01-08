package com.tma.imuseum.model.pojo;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Notifications", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class Notification implements Serializable{
    @IgnoreTest
    private static final long serialVersionUID = 11L;

    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "string", nullable = false)
    private String string;

    public Notification() {
    }

    public Notification(String username, String string) {
        this.username = username;
        this.string = string;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}