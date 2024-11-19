package com.ssafy.flowstudio.domain.user.repository;

import com.ssafy.flowstudio.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByNickname(String nickname);

    @Query("select u from User u join fetch u.apiKey where u.id = :id")
    Optional<User> findByIdWithApiKey(Long id);
}
