package com.mathologic.projects.crewlink.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * Created by vivek on 25/9/15.
 */
@Entity
@Table(name = "role")
public class Role implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;
    
    @ManyToMany(fetch = FetchType.LAZY)
    private List<SubRole> subRoles=new LinkedList<SubRole>();
    
    @OneToMany(mappedBy="role",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<User> users = new LinkedList<User>();
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public List<SubRole> getSubRoles() {
		return subRoles;
	}

	public void setSubRoles(List<SubRole> subRoles) {
		this.subRoles = subRoles;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
