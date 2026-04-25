package org.example.notificationservicev2.repository;

import jakarta.validation.constraints.Email;
import org.example.notificationservicev2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByEmail(@Email String email);
}
