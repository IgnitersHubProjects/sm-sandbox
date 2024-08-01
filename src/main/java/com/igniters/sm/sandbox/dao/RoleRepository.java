package com.igniters.sm.sandbox.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.igniters.sm.sandbox.model.Role;

public interface RoleRepository extends MongoRepository<Role,String>{

}
