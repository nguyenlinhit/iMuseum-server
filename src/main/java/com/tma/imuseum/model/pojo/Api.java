package com.tma.imuseum.model.pojo;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.imuseum.configuration.IgnoreTest;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "Api", uniqueConstraints = {@UniqueConstraint(columnNames = "idAPI")})
public class Api implements Serializable {
	@IgnoreTest
    private static final long serialVersionUID = 11L;
    @Id
	@IgnoreTest
    @Column(name = "idAPI", nullable = false)
    private int idApi;
    @Column(name = "group", nullable = true)
    private String group;
    @Column(name = "apiName", nullable = true)
    private String apiName;
    @Column(name = "apiLink", nullable = true)
    private String apiLink;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "apiExample", nullable = true)
    private String apiExample;
	@IgnoreTest
    @Column(name = "dateCreated", columnDefinition = "DATETIME", nullable = true)
    private Date dateCreated;
	@IgnoreTest
    @Column(name = "dateEdit", columnDefinition = "DATETIME", nullable = true)
    private Date dateEdit;
	@Column(name = "method", nullable = true)
    private String method;
	@IgnoreTest
    @Column(name = "status", columnDefinition = "BIT", nullable = false)
    private Boolean isActive;
	// @ManyToOne(fetch = FetchType.EAGER)
    // @JoinColumn(name = "idUser", nullable = false)
    // private User userApi;
	public Api(){};
	public Api(int idApi, String group, String apiName, String apiLink, String description, String apiExample,
			Date dateCreated, Date dateEdit, Boolean isActive) {
		this.idApi = idApi;
		this.group = group;
		this.apiName = apiName;
		this.apiLink = apiLink;
		this.description = description;
		this.apiExample = apiExample;
		this.dateCreated = dateCreated;
		this.dateEdit = dateEdit;
		this.isActive = isActive;
		// this.userApi = userApi;
	}

	public int getIdApi() {
		return idApi;
	}

	public void setIdApi(int idApi) {
		this.idApi = idApi;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiLink() {
		return apiLink;
	}

	public void setApiLink(String apiLink) {
		this.apiLink = apiLink;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApiExample() {
		return apiExample;
	}

	public void setApiExample(String apiExample) {
		this.apiExample = apiExample;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	// public User getUserApi() {
	// 	return userApi;
	// }

	// public void setUserApi(User userApi) {
	// 	this.userApi = userApi;
	// }
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
