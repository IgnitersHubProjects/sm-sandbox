package com.igniters.sm.sandbox.dao;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.igniters.sm.sandbox.model.User;


public interface UserRepository extends MongoRepository<User,String>{
   Optional<User> findByEmail(String email);
}
