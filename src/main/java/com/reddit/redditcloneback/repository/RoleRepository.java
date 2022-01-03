package com.reddit.redditcloneback.repository;

import com.reddit.redditcloneback.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
