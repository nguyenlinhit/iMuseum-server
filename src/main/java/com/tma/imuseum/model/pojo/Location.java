package com.tma.imuseum.model.pojo;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

import java.io.Serializable;


@NamedNativeQueries({
    @NamedNativeQuery(
        name = "spLocationFamily",
        query = "CALL getLocationFamily(:locationId)",
        resultClass = Location.class
    ),
    @NamedNativeQuery(
        name = "spLocationChild",
        query = "CALL getLocationChild(:locationId)",
        resultClass = Location.class
    ),
    @NamedNativeQuery(name = "insertLocation", query = "CALL insertLocation(:nameLocation, :type, :idParent)", resultClass = Location.class)
})
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Locations", uniqueConstraints = { @UniqueConstraint(columnNames = "idLocation") })
public class Location implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 5L;
    @Id
    @IgnoreTest
    @Column(name = "idLocation", nullable = false)
    private int idLocation;
    @Column(name = "name", nullable = true)
    private String nameLoc;
    @Column(name = "type", nullable = true)
    private String type;
    @Column(name = "idParent", nullable = true)
    private int idParent;
    @Column(name = "path", nullable = true)
    private String path;
    
    @IgnoreTest
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationArtifact")
    private Set<Artifact> artifacts;
    @IgnoreTest
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationBeacon")
    private Set<Beacon> beacons;

    public Location() {
    }

    public Location(int idLocation, String nameLoc, String type, int idParent) {
        this.idLocation = idLocation;
        this.nameLoc = nameLoc;
        this.type = type;
        this.idParent = idParent;
    }

    public int getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(int idLocation) {
        this.idLocation = idLocation;
    }

    public String getNameLoc() {
        return nameLoc;
    }

    public void setNameLoc(String nameLoc) {
        this.nameLoc = nameLoc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Set<Beacon> getBeacons() {
        return beacons;
    }

    public void setBeacons(Set<Beacon> beacons) {
        this.beacons = beacons;
    }

    public String getPath(){
        return this.path;
    }

}
