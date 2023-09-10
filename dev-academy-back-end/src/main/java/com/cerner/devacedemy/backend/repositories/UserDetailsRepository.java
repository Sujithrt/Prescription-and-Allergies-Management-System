package com.cerner.devacedemy.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.cerner.devacedemy.backend.entities.User;

public interface UserDetailsRepository extends JpaRepository<User, Long> {
	/**
	 * Queries database to get user details based on username
	 * @param username
	 * @return
	 */
	User findByUsername(String username);
}
