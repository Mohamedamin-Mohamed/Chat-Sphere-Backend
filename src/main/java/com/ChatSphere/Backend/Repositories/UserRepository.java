package com.ChatSphere.Backend.Repositories;

import com.ChatSphere.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByOauthId(String oauthId);

    /*here we are selecting users with pattern matching and case-insensitive, where the query appears anywhere in the email or name and is not the same profile as
    the requesters email */
    @Query("select user from User user where (lower(user.name) like lower(concat('%', :query, '%')) or lower(user.email) like lower(concat('%', :query, '%'))) and user.email <> :email")
    List<User> searchUsersByNameOrEmail(@Param("query") String query, @Param("email") String email);
}
