package com.green.acamatch.user.repository;

import com.green.acamatch.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleId(Long roleId);
}
