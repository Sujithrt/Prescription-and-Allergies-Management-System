package com.cerner.devacedemy.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cerner.devacedemy.backend.entities.PhysicianDetails;

public interface PhysicianDetailsRepository extends JpaRepository<PhysicianDetails, Long> {

}
