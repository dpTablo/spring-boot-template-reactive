package com.dptablo.template.springboot.reactive.repository.jpa;

import com.dptablo.template.springboot.reactive.model.entity.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping, Long> {
}
