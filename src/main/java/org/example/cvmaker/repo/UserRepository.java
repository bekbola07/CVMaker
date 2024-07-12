package org.example.cvmaker.repo;

import org.example.cvmaker.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
