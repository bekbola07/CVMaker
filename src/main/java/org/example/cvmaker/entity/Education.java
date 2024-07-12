package org.example.cvmaker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "education")
public class Education {
    private String id;
    private String where;
    private LocalDate from;
    private LocalDate to;
}
