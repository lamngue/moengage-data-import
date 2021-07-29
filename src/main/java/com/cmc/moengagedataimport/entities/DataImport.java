package com.cmc.moengagedataimport.entities;

import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.enums.QueueStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String record;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueueStatusEnum statusEnum = QueueStatusEnum.Waiting;
    private Long dataDate;
    private Long sendDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportTypeEnum type;
}
