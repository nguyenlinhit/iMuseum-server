package com.tma.imuseum.model.pojo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tma.imuseum.configuration.IgnoreTest;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "Categorys", uniqueConstraints = { @UniqueConstraint(columnNames = "idCategory") })
public class Category implements Serializable {
    @IgnoreTest
    private static final long serialVersionUID = 3L;
    @Id
    @Column(name = "idCategory", nullable = false)
    private int idCategory;
    @Column(name = "name", nullable = true)
    private String nameCategory;
    @IgnoreTest
    @JsonIgnore
    @OneToMany(mappedBy = "categoryArtifact")
    private Set<Artifact> artifacts;

    public Category() {
    }

    public Category(int idCategory, String nameCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(Set<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

}
