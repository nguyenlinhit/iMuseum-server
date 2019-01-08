package com.tma.imuseum.model.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.tma.imuseum.configuration.IgnoreTest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Requests", uniqueConstraints = { @UniqueConstraint(columnNames = "timestamp") })
public class Request implements Serializable {
    private static final long serialVersionUID = 8L;
    @Id
    @IgnoreTest
    @Column(name = "timestamp", columnDefinition = "DATETIME", nullable = true)
    private Date timestamp;
    @Column(name = "idDevice", nullable = true)
    private String idDevice;
    @Column(name = "duration", nullable = true)
    private Long duration;
    //Map many to one
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBeacon", nullable = false)
    private Beacon beaconRequest;

    public Request() {
    }

    public Request(String idDevice, Date timestamp, Long duration) {
        this.idDevice = idDevice;
        this.timestamp = timestamp;
        this.duration = duration;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    //Many-to-one need @JoinColumn with the name is one-side primary key

    public Beacon getBeaconRequest() {
        return beaconRequest;
    }

    public void setBeanconRequest(Beacon beaconRequest) {
        this.beaconRequest = beaconRequest;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDuration(){
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Request info: -Device: " + this.idDevice + " -Beacon: " + this.beaconRequest.getId() + " -Timestamp: " + this.timestamp + " -Duration: " + this.duration;
    }
}
