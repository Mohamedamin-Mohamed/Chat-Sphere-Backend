package com.ChatSphere.Backend.Repositories;

import com.ChatSphere.Backend.Model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowersRepository extends JpaRepository<Follower, Long> {

}
