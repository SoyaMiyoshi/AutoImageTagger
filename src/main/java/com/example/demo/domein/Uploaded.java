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
    public String owner;
    public String tag;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String owner) {
        this.tag = tag;
    }
    
    /*
    @Override
    public String toString() {
        return "uploaded [id=" + id + ", name=" + name + ", deadline=" + deadline + ", isDone=" + isdone + "]";
    }*/
}

