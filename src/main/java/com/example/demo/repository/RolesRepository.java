package com.example.demo.repository;

import com.example.demo.Enum.Role;
import com.example.demo.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(Role name);
}