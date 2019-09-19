package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.demo.domein.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByUsername(String username);

}