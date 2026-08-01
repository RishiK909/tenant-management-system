package com.project.tms.securityService.repository;

import com.project.tms.securityService.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByUserName(String userName);

    Optional<Users> findByPhoneNumber(String phoneNumber);
}

