package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.enums.ImportTypeEnum;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.cmc.moengagedataimport.utils.DateUtils;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataImportService {

    @Autowired
    private DataImportRepository dataImportRepository;

    public void importData(List<SbfLoanPortfolio> sbfLoanPortfolioList, ImportTypeEnum type){

        List<DataImport> dataImports = sbfLoanPortfolioList.stream().map(x -> {
            JSONObject record = new JSONObject(x);
            DataImport dataImport = new DataImport();
            dataImport.setRecord(record);
            dataImport.setId(x.getCustomer_id_number());
            dataImport.setEmail(x.getCust_email_id());
            dataImport.setName(x.getCust_name());
            dataImport.setFirstName(x.getCust_first_name());
            dataImport.setLastName(x.getCust_last_name());
            dataImport.setGender(x.getCust_gender());
            dataImport.setMobile(x.getCust_mob_no());
            dataImport.setDataDate(x.getData_date());
            String age = DateUtils.getAgeFromBirthday("yyyymmdd", x.getCust_birth_date().toString());
            dataImport.setAge(age);
            dataImport.setSendDate(0L);
            dataImport.setType(type);
            return dataImport;
        }).collect(Collectors.toList());
        dataImportRepository.saveAll(dataImports);
    }
}
