package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.cmc.moengagedataimport.repository.SbfLoanPortfolioRepository;
import com.google.gson.Gson;
import com.cmc.moengagedataimport.dto.ResourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RedshiftClusterImportService {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SbfLoanPortfolioRepository sbfLoanPortfolioRepository;

    @Autowired
    private DataImportRepository dataImportRepository;

    public ResourceDto getResources(Object object) {
        ResourceDto resourceDTO = new ResourceDto();
        Map<String, List<SbfLoanPortfolio>> tablesFetching = new HashMap<>();
        List<SbfLoanPortfolio> sbfLoanPortfolioList = sbfLoanPortfolioRepository.findAll();
        Gson gson = new Gson();
        List<DataImport> dataImports = sbfLoanPortfolioList.stream().map(x -> {
            DataImport dataImport = new DataImport();
            dataImport.setRecord(gson.toJson(x));
            return dataImport;
        }).collect(Collectors.toList());
        dataImportRepository.saveAll(dataImports);
        tablesFetching.put(SbfLoanPortfolio.class.getSimpleName(), sbfLoanPortfolioList);
        resourceDTO.setDataImport(tablesFetching);
        return resourceDTO;
    }
}
