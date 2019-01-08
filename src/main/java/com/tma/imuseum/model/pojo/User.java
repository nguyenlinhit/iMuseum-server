package com.tma.imuseum.model.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

@NamedNativeQueries({
        @NamedNativeQuery(name = "spListUserAdvance", query = "CALL spListUserAdvance(:numb, :page, :isActive, :normal)", resultClass = User.class) })
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Users", uniqueConstraints = {@UniqueConstraint(columnNames = "idUser")})
public class User implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 10L;
    @Id
    @IgnoreTest
    @Column(name = "idUser", nullable = false)
    private int idUser;
    @Column(name = "username", nullable = false)
    private String username;
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "name", nullable = false)
    private String name;
    @IgnoreTest
    @Column(name = "level", nullable = false)
    private int level;
    @IgnoreTest
    @Column(name = "status", columnDefinition = "BIT", nullable = false)
    private boolean isActive;
    @IgnoreTest
    @Column(name = "dateCreated", columnDefinition = "DATETIME", nullable = true)
    private Date dateCreated;
    @IgnoreTest
    @Column(name = "dateEdit", columnDefinition = "DATETIME", nullable = true)
    private Date dateEdit;

    @Column(name = "login", columnDefinition = "DATETIME", nullable = true)
    private Date login;

    @Column(name = "logout", columnDefinition = "DATETIME", nullable = true)
    private Date logout;

    @Column(name = "cookies", nullable = true)
    private String cookies;

    @IgnoreTest
    @JsonIgnore
    @OneToMany(mappedBy = "userBeacon")
    private Set<Beacon> beacons;
    @IgnoreTest
    @JsonIgnore
    @OneToMany(mappedBy = "userArtifact")
    private Set<Artifact> artifacts;

    public User() {
    }

    public User(int idUser, String username, String password, String email, String name, int level, boolean isActive,
                Date dateCreated, Date dateEdit, Date login, Date logout, String cookies) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.level = level;
        this.isActive = isActive;
        this.dateCreated = dateCreated;
        this.dateEdit = dateEdit;
        this.login = login;
        this.logout = logout;
        this.cookies = cookies;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }


    public Date getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(Date dateEdit) {
        this.dateEdit = dateEdit;
    }

    // mappedBy is key name of relationship, add in one-side. Many-side see the example above

    public Set<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(Set<Beacon> beacons) {
        this.beacons = beacons;
    }


    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getLogin() {
        return login;
    }

    public void setLogin(Date login) {
        this.login = login;
    }

    public Date getLogout() {
        return logout;
    }

    public void setLogout(Date logout) {
        this.logout = logout;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }
}
