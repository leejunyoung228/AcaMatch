package com.green.acamatch.user.repository;

import com.green.acamatch.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role searchRoleByRoleId(Long roleId);
}
