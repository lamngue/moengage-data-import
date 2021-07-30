package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.cmc.moengagedataimport.repository.SbfLoanPortfolioRepository;
import com.cmc.moengagedataimport.dto.ResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public ResourceDto getResources() {
        ResourceDto resourceDTO = new ResourceDto();
        Map<String, List<SbfLoanPortfolio>> tablesFetching = new HashMap<>();
        Optional<DataImport> latestDataImport = dataImportRepository.findFirstByTypeIsNotOrderByDataDate(ImportTypeEnum.FIlE);
        List<SbfLoanPortfolio> sbfLoanPortfolioList;
        if(latestDataImport.isPresent()){
            sbfLoanPortfolioList = sbfLoanPortfolioRepository.findAllByData_dateIsGreaterThan(latestDataImport.get().getDataDate());
        }
        else {
            sbfLoanPortfolioList = sbfLoanPortfolioRepository.findAll();
        }
        dataImportService.importData(sbfLoanPortfolioList, ImportTypeEnum.REDSHIFT);
        tablesFetching.put(SbfLoanPortfolio.class.getSimpleName(), sbfLoanPortfolioList);
        resourceDTO.setDataImport(tablesFetching);
        return resourceDTO;
    }
}
