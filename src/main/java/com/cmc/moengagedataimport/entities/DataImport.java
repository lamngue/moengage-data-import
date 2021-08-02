package com.cmc.moengagedataimport.entities;

import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.enums.QueueStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "data_import")
public class DataImport {
    @Id
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private JSONObject record;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueueStatusEnum status = QueueStatusEnum.Waiting;
    private Long dataDate;
    private Long sendDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportTypeEnum type;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private Integer mobile;
    private String gender;
    private String age;
}
