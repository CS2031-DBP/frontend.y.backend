package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
