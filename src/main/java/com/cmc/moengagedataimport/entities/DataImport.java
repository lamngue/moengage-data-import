package com.cmc.moengagedataimport.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Document(collection = "data_import")
public class DataImport {
    @Id
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String record;
    private Long dataDate;
    private Long sendDate;
    @Enumerated(EnumType.STRING)
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String gender;
    private String age;
}
