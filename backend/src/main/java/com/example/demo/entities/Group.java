package com.example.demo.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(
        name = "person_group",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> persons = new HashSet<>();

    // Constructors, getters, and setters
    public Group() {}

    public Group(String name, Set<Person> persons) {
        this.name = name;
        this.persons = persons;
    }

    public Long getId() {
        return id;
    }

    public String getName() { 
        return name; 
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }
}
