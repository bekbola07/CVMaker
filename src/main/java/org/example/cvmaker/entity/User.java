package org.example.cvmaker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "users")
public class User {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    @DBRef
    private List<Education> educations;
    @DBRef
    private List<Experience> experience;
    @DBRef
    private List<Skill> skills;
    private byte[] photo;
    private String aboutMe;
}
