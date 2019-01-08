package com.tma.imuseum.model.pojo;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

import javax.persistence.CascadeType;

import java.io.Serializable;
import java.util.Date;

@NamedNativeQueries({
    @NamedNativeQuery(
        name = "spCheckExistBeacon",
        query = "CALL checkExistBeacon(:beaconId)",
        resultClass = Beacon.class
    ),
    @NamedNativeQuery(
        name = "spListBeaconAdvance",
        query = "CALL listBeaconAdvance(:numb, :page, :isActive, :normal, :location, :artifact, :user)",
        resultClass = Beacon.class
    ),
    @NamedNativeQuery(
        name = "spGetMostDuration",
        query = "CALL spGetMostDuration(:p, :n, :sortType)",
        resultClass = Beacon.class)
})
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Beacons", uniqueConstraints = { @UniqueConstraint(columnNames = "idBeacon") })
public class Beacon implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 2L;
    @Id
    @IgnoreTest
    @Column(name = "idBeacon", nullable = false)
    private int idBeacon;
    @IgnoreTest
    @Column(name = "dateCreated", columnDefinition = "DATETIME", nullable = true)
    private Date dateCreated;
    @IgnoreTest
    @Column(name = "dateEdit", columnDefinition = "DATETIME", nullable = true)
    private Date dateEdit;
    @Column(name = "status", columnDefinition = "BIT", nullable = false)
    private Boolean isActive;
    @IgnoreTest
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", nullable = false)
    private User userBeacon;
    // @JsonIgnore
    @Fetch(FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idLocation", nullable = false)
    private Location locationBeacon;

    @IgnoreTest
    @JsonIgnore
    @OneToMany(mappedBy = "beaconRequest", cascade = CascadeType.ALL)
    private Set<Request> request;
    @IgnoreTest
    @JsonIgnore
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "beaconArtifact", cascade = CascadeType.MERGE)
    private Set<Artifact> artifacts;

    public Beacon() {
    }

    public Beacon(int id, Location location, Date dateCreated, Date dateEdit, Boolean isActive,
            User user) {
        this.idBeacon = id;
        this.locationBeacon = location;
        this.dateCreated = dateCreated;
        this.dateEdit = dateEdit;
        this.isActive = isActive;
        this.userBeacon = user;
    }

    public void merge(Beacon other) {
        setActive(other.getActive());
        setArtifacts(other.getArtifacts());
        setDateCreated(other.getDateCreated());
        setDateEdit(other.getDateEdit());
        setLocationBeacon(other.getLocationBeacon());
        setRequest(other.getRequest());
        setUserBeacon(other.getUserBeacon());
    }

    public int getId() {
        return idBeacon;
    }

    public void setId(int id) {
        this.idBeacon = id;
    }

    public Location getLocationBeacon() {
        return locationBeacon;
    }

    public void setLocationBeacon(Location location) {
        this.locationBeacon = location;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public User getUserBeacon() {
        return userBeacon;
    }

    public void setUserBeacon(User user) {
        this.userBeacon = user;
    }

    public Set<Request> getRequest() {
        return request;
    }

    public void setRequest(Set<Request> request) {
        this.request = request;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Set<Artifact> getArtifacts() {
        Hibernate.initialize(artifacts);
        return artifacts;
    }

    @Override
    public String toString(){
        return "ID: " + this.idBeacon + " Location: " + (null != this.locationBeacon ? this.locationBeacon.getIdLocation() : "");
    }
}