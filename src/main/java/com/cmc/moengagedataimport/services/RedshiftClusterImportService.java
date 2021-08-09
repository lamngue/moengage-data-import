package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.cmc.moengagedataimport.repository.SbfLoanPortfolioRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedshiftClusterImportService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private SbfLoanPortfolioRepository sbfLoanPortfolioRepository;

    private DataImportRepository dataImportRepository;

    private DataImportService dataImportService;


    public RedshiftClusterImportService(SbfLoanPortfolioRepository sbfLoanPortfolioRepository, DataImportRepository dataImportRepository, DataImportService dataImportService) {
        this.sbfLoanPortfolioRepository = sbfLoanPortfolioRepository;
        this.dataImportRepository = dataImportRepository;
        this.dataImportService = dataImportService;
    }

    public List<DataImport> getResources() {
        Map<String, List<JSONObject>> tablesFetching = new HashMap<>();
        Optional<DataImport> latestDataImport = dataImportRepository.findFirstByTypeIsNotOrderByDataDate(ImportTypeEnum.FIlE);
        List<SbfLoanPortfolio> sbfLoanPortfolioList;
        if(latestDataImport.isPresent()){
            sbfLoanPortfolioList = sbfLoanPortfolioRepository.findAllByData_dateIsGreaterThan(latestDataImport.get().getDataDate());
        }
        else {
            sbfLoanPortfolioList = sbfLoanPortfolioRepository.findAll();
        }
        List<DataImport> dataImports = dataImportService.importRedshiftData(sbfLoanPortfolioList, ImportTypeEnum.REDSHIFT);
        List<JSONObject> jsonObjects =  sbfLoanPortfolioList.stream().map(x -> new JSONObject(x)).collect(Collectors.toList());

        tablesFetching.put(SbfLoanPortfolio.class.getSimpleName(), jsonObjects);
        return dataImports;
    }
}
