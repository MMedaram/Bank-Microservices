package com.bank.auth_service.repository;

import com.bank.auth_service.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials,Integer> {

 Optional<UserCredentials> findByName(String name);
}
