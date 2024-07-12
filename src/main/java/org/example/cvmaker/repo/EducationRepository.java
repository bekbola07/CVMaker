package org.example.cvmaker.repo;

import org.example.cvmaker.entity.Education;
import org.example.cvmaker.entity.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EducationRepository extends MongoRepository<Education, String> {
}
