package com.tma.imuseum.model.pojo;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Surveys", uniqueConstraints = { @UniqueConstraint(columnNames = "idSurvey") })
public class Survey implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 9L;
    @Id
    @IgnoreTest
    @Column(name = "idSurvey", nullable = false)
    private int idSurvey;
    @Column(name = "comment", nullable = true)
    private String comment;
    @Column(name = "rank", nullable = true)
    private int rank;
    @Column(name = "idArtifact", nullable = false)
    private int idArtifact;

    //Map many to
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idArtifact", nullable = false, updatable = false, insertable = false)
    private Artifact artifactSurvey;
    
    public Survey() {
    }

    public Survey(int idSurvey, String comment, int rank, int idArtifact) {
        this.idSurvey = idSurvey;
        this.comment = comment;
        this.rank = rank;
        this.idArtifact = idArtifact;
    }
     public Survey(int idSurvey, String comment, int rank) {
        this.idSurvey = idSurvey;
        this.comment = comment;
        this.rank = rank;
    }

    public int getIdSurvey() {
        return idSurvey;
    }

    public void setIdSurvey(int idSurvey) {
        this.idSurvey = idSurvey;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Artifact getArtifactSurvey() {
        return artifactSurvey;
    }

    public void setArtifactSurvey(Artifact artifact) {
        this.artifactSurvey = artifact;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getIdArtifact() {
        return idArtifact;
    }

    public void setIdArtifact(int idArtifact) {
        this.idArtifact = idArtifact;
    }
}
