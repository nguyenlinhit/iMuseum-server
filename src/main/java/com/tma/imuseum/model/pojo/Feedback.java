package com.tma.imuseum.model.pojo;

import javax.persistence.*;

import com.tma.imuseum.configuration.IgnoreTest;

import java.io.Serializable;
import java.util.Date;

@NamedNativeQueries({
    @NamedNativeQuery(
        name = "spGetListFeedback",
        query = "CALL spGetListFeedback(:p,:n, :status, :category)",
        resultClass = Feedback.class
    ),
    @NamedNativeQuery(
        name = "spPagingError",
        query = "CALL spPagingError(:p,:n, :isActive)",
        resultClass = Feedback.class
    ),
    @NamedNativeQuery(
        name = "spPagingIdea",
        query = "CALL spPagingIdea(:p,:n, :isActive)",
        resultClass = Feedback.class
    )
})
@Entity
@Table(name = "Feedbacks", uniqueConstraints = {@UniqueConstraint(columnNames = "idFeedback")})
public class Feedback implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 4L;
    @Id
    @IgnoreTest
    @Column(name = "idFeedback", nullable = false)
    private int idFeedback;
    @Column(name = "error", nullable = true)
    private String error;
    @Column(name = "idea", nullable = true)
    private String idea;
    @IgnoreTest
    @Column(name = "status", columnDefinition = "BIT", nullable = false)
    private Boolean isActive;
    @IgnoreTest
    @Column(name = "timestamp", columnDefinition = "DATETIME", nullable = true)
    private Date timestamp;
    public Feedback() {
    }

    public int getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(int idFeedback) {
        this.idFeedback = idFeedback;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean status) {
        this.isActive = status;
    }
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
