package com.fundacred.userapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fundacred.userapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	
}
