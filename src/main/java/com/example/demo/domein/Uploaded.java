package com.example.demo.domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity 
public class Uploaded {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String deadline;
    public String isdone;
    public String owner;

    
    public Uploaded(){
        isdone = "No";
    }

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
    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDone() {
        return isdone;
    }

    public void setDone(){
        this.isdone = "Yes";
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /*
    @Override
    public String toString() {
        return "uploaded [id=" + id + ", name=" + name + ", deadline=" + deadline + ", isDone=" + isdone + "]";
    }*/
}

