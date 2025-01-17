package com.green.studybridge.user.repository;

import com.green.studybridge.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Role searchRoleByRoleId(Long roleId);
}
