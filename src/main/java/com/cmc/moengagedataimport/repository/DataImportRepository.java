package com.cmc.moengagedataimport.repository;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataImportRepository extends  MongoRepository<DataImport, Long>{
    Optional<DataImport> findFirstByTypeIsNotOrderByDataDate(ImportTypeEnum type);
}
