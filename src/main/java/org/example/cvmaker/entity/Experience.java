package org.example.cvmaker.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;



@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Document(collection = "experience")
public class Experience  extends Education{
}
