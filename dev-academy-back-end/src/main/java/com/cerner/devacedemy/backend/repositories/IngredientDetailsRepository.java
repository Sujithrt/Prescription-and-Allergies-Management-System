package com.cerner.devacedemy.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cerner.devacedemy.backend.entities.Ingredients;

public interface IngredientDetailsRepository extends JpaRepository<Ingredients, Long> {
	
	/**
	 * Queries database to get list of ingredients stored in database based on searchInput
	 * @param searchInput
	 * @return
	 */
	@Query("SELECT i from Ingredients i WHERE i.name LIKE %?1%")
	public List<Ingredients> findAll(String searchInput);

}
