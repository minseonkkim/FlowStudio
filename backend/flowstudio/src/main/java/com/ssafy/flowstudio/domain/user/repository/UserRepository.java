package com.ssafy.flowstudio.domain.user.repository;

import com.ssafy.flowstudio.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(
            "SELECT u FROM User u"
                    + " LEFT JOIN FETCH u.apiKey a"
                    + " WHERE u.username = :username"
    )
    Optional<User> findByUsername(
            @Param("username") String username
    );

    boolean existsByNickname(String nickname);
}
