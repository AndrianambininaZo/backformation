package com.streaming.arosaina.repository;

import com.streaming.arosaina.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser,String> {
   ApplicationUser findByEmail(String username);
}
