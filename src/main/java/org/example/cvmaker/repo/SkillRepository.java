package org.example.cvmaker.repo;

import org.example.cvmaker.entity.Skill;
import org.example.cvmaker.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SkillRepository extends MongoRepository<Skill, String> {
    Optional<Skill> findByName(String skillName);
}
