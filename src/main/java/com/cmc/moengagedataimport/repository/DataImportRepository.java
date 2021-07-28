package com.cmc.moengagedataimport.repository;

import com.cmc.moengagedataimport.entities.DataImport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface DataImportRepository extends  MongoRepository<DataImport, Long>{

}
