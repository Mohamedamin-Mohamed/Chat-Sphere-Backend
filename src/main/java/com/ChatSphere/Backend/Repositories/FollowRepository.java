package com.ChatSphere.Backend.Repositories;

import com.ChatSphere.Backend.Model.Follow;
import com.ChatSphere.Backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findFollowsByFollower(User user);

    List<Follow> findByFollower(User user);
}
