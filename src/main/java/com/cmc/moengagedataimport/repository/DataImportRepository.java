package com.cmc.moengagedataimport.repository;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.enums.QueueStatusEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
@Repository
public interface DataImportRepository extends  MongoRepository<DataImport, Long>{
    Optional<DataImport> findFirstByTypeIsNotOrderByDataDate(ImportTypeEnum type);
    List<DataImport> findTop100ByStatusIs(QueueStatusEnum firstStatus);
}
