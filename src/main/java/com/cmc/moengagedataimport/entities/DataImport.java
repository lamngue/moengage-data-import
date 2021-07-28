package com.cmc.moengagedataimport.entities;

import com.cmc.moengagedataimport.enums.QueueStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Document(collection = "data_import")
public class DataImport {
    @Id
    @Column(nullable = false)
    private String record;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueueStatusEnum statusEnum = QueueStatusEnum.Waiting;

}
