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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

@NamedNativeQueries({
        @NamedNativeQuery(name = "spMapMedia", query = "CALL mapMedia(:idMedia, :idArtifact)", resultClass = Media.class),
        @NamedNativeQuery(name = "spListMediaAdvance", query = "CALL listMediaAdvance(:page, :numb, :normal, :beacon, :artifact)", resultClass = Media.class) })
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Medias", uniqueConstraints = {@UniqueConstraint(columnNames = "idMedia")})
public class Media implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 7L;
    @Id
    @IgnoreTest
    @Column(name = "idMedia", nullable = false)
    private int idMedia;
    @Column(name = "media", nullable = true)
    private String media;
    @Column(name = "title", nullable = true)
    private String title;

    // Map many-to-one
    @JsonIgnore
    @Fetch(FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "idArtifact", nullable = false)
    private Artifact artifactMedia;

    public Media() {
    }

    public Media(int idMedia, String media) {
        this.idMedia = idMedia;
        this.media = media;
    }


    public int getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(int idMedia) {
        this.idMedia = idMedia;
    }


    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }

    //Many-to-one need @JoinColumn with the name is one-side primary key

    public Artifact getArtifactMedia() {
        return artifactMedia;
    }

    public void setArtifactMedia(Artifact artifact) {
        this.artifactMedia = artifact;
    }
}
