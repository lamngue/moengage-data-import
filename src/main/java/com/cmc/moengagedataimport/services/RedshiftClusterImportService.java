package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.repository.SbfLoanPortfolioRepository;
import dto.ResourceDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RedshiftClusterImportService {

    @Autowired
    private SbfLoanPortfolioRepository sbfLoanPortfolioRepository;

    public ResourceDto getResources(Object object) {
        ResourceDto resourceDTO = new ResourceDto();
        Map<String, List<SbfLoanPortfolio>> tablesFetching = new HashMap<>();
        List<SbfLoanPortfolio> sbfLoanPortfolioList = sbfLoanPortfolioRepository.findAll();
        tablesFetching.put(SbfLoanPortfolio.class.getSimpleName(), sbfLoanPortfolioList);
        resourceDTO.setDataImport(tablesFetching);
        return resourceDTO;
    }
}
