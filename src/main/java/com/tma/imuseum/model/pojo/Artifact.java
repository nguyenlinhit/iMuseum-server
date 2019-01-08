package com.tma.imuseum.model.pojo;

import java.util.Set;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

import org.hibernate.annotations.NamedNativeQueries;
import org.hibernate.annotations.NamedNativeQuery;

import javax.persistence.CascadeType;

import java.io.Serializable;

@NamedNativeQueries({
        @NamedNativeQuery(name = "spListArtifactAdvance", query = "CALL listArtifactAdvance(:numb, :page, :isActive, :normal, :location, :beacon, :user, :category)", resultClass = Artifact.class),
        @NamedNativeQuery(name = "spMap", query = "CALL map(:idArtifact, :idBeacon)", resultClass = Artifact.class),
        @NamedNativeQuery(name = "spAddArtifact", query = "CALL insertArtifact(:idBeacon, :idCategory, :name, :title, :description, :idLocation, :author, :dateCreated, :idUser, :status)", resultClass = Artifact.class),
        @NamedNativeQuery(name = "spGetMostView", query = "CALL spGetMostView(:p, :n, :sortType, :sortColumn)", resultClass = Artifact.class),
        @NamedNativeQuery(name = "spGetHighestRate", query = "CALL spGetHighestRate(:p, :n, :sortType, :sortColumn)", resultClass = Artifact.class),
        @NamedNativeQuery(name = "spEditArtifact", query = "CALL editArtifact(:idArtifact ,:idBeacon, :idCategory, :name, :title, :description, :idLocation,:author ,:editor, :dateEdit, :status)", resultClass = Artifact.class) })

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Artifacts", uniqueConstraints = { @UniqueConstraint(columnNames = "idArtifact") })
public class Artifact implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@IgnoreTest
    @Column(name = "idArtifact", nullable = false)
    private int idArtifact;
    @Column(name = "name", nullable = true)
    private String nameArtifact;
    @Column(name = "title", nullable = true)
    private String titleArtifact;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "author", nullable = true)
    private String author;

    @IgnoreTest
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateCreated", columnDefinition = "DATETIME", nullable = true)
    private Date dateCreated;
    @IgnoreTest
    @Column(name = "dateEdit", columnDefinition = "DATETIME", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEdit;
    //@IgnoreTest
    @Column(name = "status", columnDefinition = "BIT", nullable = false)
    private Boolean isActive;
    @IgnoreTest
    @Column(name = "view", nullable = false)
    private int view;

    //Many-to-one need @JoinColumn with the name is one-side primary key
    //@IgnoreTest
    // @JsonIgnore
    @Fetch(FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idBeacon", nullable = false)
    private Beacon beaconArtifact;
    // One-to-one in class contain another primary key we use @PrimaryKeyJoinColumn,
    // in another class use @OneToOne(fetch = FetchType.LAZY, mappedBy = "stock") after @OneToOne
    // fetch has 2 types is lazy and eager, lazy will load data when call getAttribute() but eager load data when when this object
    //@IgnoreTest
    @Fetch(FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idCategory", nullable = false)
    private Category categoryArtifact;
    //@IgnoreTest
    @Fetch(FetchMode.SELECT)
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "idLocation", nullable = false)
    private Location locationArtifact;
    //@IgnoreTest
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor", nullable = false)
    private User userArtifact;

    // mappedBy is key name of relationship, add in one-side. Many-side see the example above
    @IgnoreTest
    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "artifactMedia", cascade = CascadeType.MERGE)
    private Set<Media> medias;
    @IgnoreTest
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artifactSurvey", cascade = CascadeType.ALL)
    private Set<Survey> surveys;

    public Artifact() {
    }

    public Artifact(int idArtifact, String nameArtifact, String titleArtifact, String description, String author,
            Date dateCreated, Date dateEdit, Boolean isActive, Beacon beacon, Category category, Location location,
            User user, Set<Media> medias, int view) {
        this.idArtifact = idArtifact;
        this.nameArtifact = nameArtifact;
        this.titleArtifact = titleArtifact;
        this.description = description;
        this.author = author;
        this.dateCreated = dateCreated;
        this.dateEdit = dateEdit;
        this.isActive = isActive;
        this.beaconArtifact = beacon;
        this.categoryArtifact = category;
        this.locationArtifact = location;
        this.userArtifact = user;
        this.medias = medias;
        this.view = view;
    }

    // Avoid merge on another object/session
    public void merge(Artifact other) {
        setAuthor(other.getAuthor());
        setBeaconArtifact(other.getBeaconArtifact());
        setCategoryArtifact(other.getCategoryArtifact());
        setDateCreated(other.getDateCreated());
        setDateEdit(other.getDateEdit());
        setDescription(other.getDescription());
        setIsActive(other.getActive());
        setLocationArtifact(other.getLocationArtifact());
        setMedias(other.getMedias());
        setNameArtifact(other.getNameArtifact());
        setSurveys(other.getsurveys());
        setTitleArtifact(other.getTitleArtifact());
        setUserArtifact(other.getUserArtifact());
        setView(other.getView());
    }

    public int getIdArtifact() {
        return idArtifact;
    }

    public void setIdArtifact(int idArtifact) {
        this.idArtifact = idArtifact;
    }

    public Beacon getBeaconArtifact() {
        return beaconArtifact;
    }

    public void setBeaconArtifact(Beacon beacon) {
        this.beaconArtifact = beacon;
    }

    public Category getCategoryArtifact() {
        return categoryArtifact;
    }

    public void setCategoryArtifact(Category category) {
        this.categoryArtifact = category;
    }

    public String getNameArtifact() {
        return nameArtifact;
    }

    public void setNameArtifact(String name) {
        this.nameArtifact = name;
    }

    public String getTitleArtifact() {
        return titleArtifact;
    }

    public void setTitleArtifact(String name) {
        this.titleArtifact = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocationArtifact() {
        return locationArtifact;
    }

    public void setLocationArtifact(Location location) {
        this.locationArtifact = location;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Set<Media> getMedias() {
        return medias;
    }

    public void setMedias(Set<Media> medias) {
        this.medias = medias;
    }

    public Set<Survey> getsurveys() {
        return surveys;
    }

    public void setSurveys(Set<Survey> surveys) {
        this.surveys = surveys;
    }

    public User getUserArtifact() {
        return userArtifact;
    }

    public void setUserArtifact(User user) {
        this.userArtifact = user;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
