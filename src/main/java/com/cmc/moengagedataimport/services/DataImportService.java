package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataImportService {

    @Autowired
    private DataImportRepository dataImportRepository;

    public void importData(List<SbfLoanPortfolio> sbfLoanPortfolioList, ImportTypeEnum type){
        Gson gson = new Gson();
        List<DataImport> dataImports = sbfLoanPortfolioList.stream().map(x -> {
            DataImport dataImport = new DataImport();
            dataImport.setRecord(gson.toJson(x));
            dataImport.setId(x.getCustomer_id_number());
            dataImport.setDataDate(x.getData_date());
            dataImport.setSendDate(0L);
            dataImport.setType(type);
            return dataImport;
        }).collect(Collectors.toList());
        dataImportRepository.saveAll(dataImports);
    }
}
