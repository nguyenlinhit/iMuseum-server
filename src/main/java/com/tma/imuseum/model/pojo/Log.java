package com.tma.imuseum.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

import java.io.Serializable;
import java.util.Date;


@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Logs", uniqueConstraints = {@UniqueConstraint(columnNames = "idLog")})
public class Log implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 6L;
    @Id
    @IgnoreTest
    @Column(name = "idLog", nullable = false)
    private int idLog;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "input", nullable = true)
    private String input;
    @IgnoreTest
    @Column(name = "status", columnDefinition = "BIT", nullable = true)
    private Boolean isActive;
    @IgnoreTest
    @Column(name = "timestamp", columnDefinition = "DATETIME", nullable = true)
    private Date timestamp;

    public Log() {
    }

    public Log(int idLog, String description, String input, Boolean isActive, Date timestamp) {
        this.idLog = idLog;
        this.description = description;
        this.input = input;
        this.isActive = isActive;
        this.timestamp = timestamp;
    }


    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }


    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
