package com.cerner.devacedemy.backend.repositories;

import com.cerner.devacedemy.backend.entities.Encounter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EncountersRepository extends JpaRepository<Encounter, Long> {

}
