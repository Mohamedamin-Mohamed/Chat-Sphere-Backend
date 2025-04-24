package com.ChatSphere.Backend.Repositories;

import com.ChatSphere.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    User findByOauthId(String oauthId);
}
