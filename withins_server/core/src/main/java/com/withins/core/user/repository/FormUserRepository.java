package com.withins.core.user.repository;

import com.withins.core.user.entity.FormUser;
import com.withins.core.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormUserRepository extends JpaRepository<FormUser, Long> {

    Optional<User> findByEmail(String email);
}
