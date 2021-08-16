package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.services.bulkImport.BulkImportService;
import com.cmc.moengagedataimport.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataImportService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${file.fieldName.loan_portfolio}")
    private List<String> loanPortfolioFieldName;

    @Value("${file.fieldName.sbf_cif}")
    private List<String> cifFieldName;

    @Autowired
    private BulkImportService bulkImportService;

    @Value("${file.fileName.loan_portfolio}")
    private String portfolioFileName;

    @Value("${file.fileName.sbf_cif}")
    private String cifFileName;

    public String importFileData(List<JSONObject> fileDataList, String fileName) {
        List<String> fieldNameList;
        if(fileName.toLowerCase().contains(cifFileName)){
            fieldNameList = cifFieldName;
        }
        else if(fileName.toLowerCase().contains(portfolioFileName)){
            fieldNameList = loanPortfolioFieldName;
        }
        else {
            return null;
        }
        List<DataImport> dataImports = new ArrayList<>();
        for (JSONObject fileData : fileDataList) {
            DataImport dataImport = new DataImport();
            dataImport.setRecord(fileData.toString());
            dataImport.setId(Long.parseLong(fileData.get(fieldNameList.get(0)).toString()));
            dataImport.setName(fileData.getString(fieldNameList.get(1)) + fileData.getString(fieldNameList.get(2)) + fileData.getString(fieldNameList.get(3)) );
            dataImport.setFirstName(fileData.getString(fieldNameList.get(1)));
            dataImport.setLastName(fileData.getString(fieldNameList.get(3)));
            dataImport.setGender(fileData.getString(fieldNameList.get(4)));
            dataImport.setMobile(fileData.getString(fieldNameList.get(5)));
            dataImport.setDataDate(fileData.getLong(fieldNameList.get(6)));
            String age = DateUtils.getAgeFromBirthday("yyyymmdd", fileData.getString(fieldNameList.get(7)));
            dataImport.setAge(age);
            dataImport.setSendDate(0L);
            dataImports.add(dataImport);
        }
        String response;
        try {
            response = bulkImportService.bulkImport(dataImports);
            return response;
        } catch(JsonProcessingException e) {
            log.error("Exception {}" , e);
        }
        return null;
    }
}
