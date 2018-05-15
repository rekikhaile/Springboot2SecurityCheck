package com.rekik.springboot2securitycheck;

import org.springframework.data.repository.CrudRepository;

public interface AppRoleRepository extends CrudRepository<AppRole, Long>{
    AppRole findByRole(String role);
}
