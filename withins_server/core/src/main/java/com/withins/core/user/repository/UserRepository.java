package com.withins.core.user.repository;

import com.withins.core.user.entity.Provider;
import com.withins.core.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM SocialUser u WHERE u.provider = :provider AND u.providerId = :providerId")
    Optional<User> findBySocialUser(@Param("provider") Provider provider, @Param("providerId") String providerId);
}
