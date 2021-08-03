package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.cmc.moengagedataimport.utils.DateUtils;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataImportService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataImportRepository dataImportRepository;

    @Value("${file.fieldName.loan_portfolio}")
    private List<String> loanPortfolioFieldName;

    @Value("${file.fieldName.sbf_cif}")
    private List<String> cifFieldName;

    public void importRedshiftData(List<SbfLoanPortfolio> sbfLoanPortfolioList, ImportTypeEnum type){
        Gson gson = new Gson();
        List<DataImport> dataImports = sbfLoanPortfolioList.stream().map(x -> {
            DataImport dataImport = new DataImport();
            dataImport.setRecord(gson.toJson(x));
            dataImport.setId(x.getCustomer_id_number());
            dataImport.setEmail(x.getCust_email_id());
            dataImport.setName(x.getCust_name());
            dataImport.setFirstName(x.getCust_first_name());
            dataImport.setLastName(x.getCust_last_name());
            dataImport.setGender(x.getCust_gender());
            dataImport.setMobile(x.getCust_mob_no().toString());
            dataImport.setDataDate(x.getData_date());
            String age = DateUtils.getAgeFromBirthday("yyyymmdd", x.getCust_birth_date().toString());
            dataImport.setAge(age);
            dataImport.setSendDate(0L);
            dataImport.setType(type);
            return dataImport;
        }).collect(Collectors.toList());
        dataImportRepository.saveAll(dataImports);
    }

    public void importFileData(List<JSONObject> fileDataList, String fileName){
        List<String> fieldNameList = new ArrayList<>();
        if(fileName.toLowerCase().contains("cif")){
            fieldNameList = cifFieldName;
        }
        if(fileName.toLowerCase().contains("campaign")){
            fieldNameList = loanPortfolioFieldName;
        }
        log.info(fieldNameList.get(1));
        List<DataImport> dataImports = new ArrayList<>();
        for (JSONObject fileData : fileDataList) {
            DataImport dataImport = new DataImport();
            dataImport.setRecord(fileData.toString());
            dataImport.setId(Long.parseLong(fileData.get(fieldNameList.get(0)).toString()));
//            dataImport.setEmail(x.());
            dataImport.setName(fileData.getString(fieldNameList.get(1)) + fileData.getString(fieldNameList.get(2)) + fileData.getString(fieldNameList.get(3)) );
            dataImport.setFirstName(fileData.getString(fieldNameList.get(1)));
            dataImport.setLastName(fileData.getString(fieldNameList.get(3)));
            dataImport.setGender(fileData.getString(fieldNameList.get(4)));
            dataImport.setMobile(fileData.getString(fieldNameList.get(5)));
            dataImport.setDataDate(fileData.getLong(fieldNameList.get(6)));
            String age = DateUtils.getAgeFromBirthday("yyyymmdd", fileData.getString(fieldNameList.get(7)));
            dataImport.setAge(age);
            dataImport.setSendDate(0L);
            dataImport.setType(ImportTypeEnum.FIlE);
            dataImports.add(dataImport);
        }
        dataImportRepository.saveAll(dataImports);
    }
}
