package com.green.acamatch.user.repository;

import com.green.acamatch.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role searchRoleByRoleId(Long roleId);
}
