package com.cmc.moengagedataimport.repository;

import com.cmc.moengagedataimport.entities.DataImport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface DataImportRepository extends MongoRepository<DataImport, Long>{

}
