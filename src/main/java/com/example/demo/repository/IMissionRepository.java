package com.example.demo.repository;

import com.example.demo.entities.Mission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMissionRepository extends CrudRepository<Mission, Integer> {
}
