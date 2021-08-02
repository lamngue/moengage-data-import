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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataImportService {

    @Autowired
    private DataImportRepository dataImportRepository;

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

    public void importFileData(List<JSONObject> fileDataList){
        Gson gson = new Gson();
        List<DataImport> dataImports = new ArrayList<>();
        for (JSONObject fileData : fileDataList) {
            DataImport dataImport = new DataImport();
            dataImport.setRecord(fileData.toString());
            dataImport.setId(Long.parseLong(fileData.get("CUST_ID1").toString()));
//            dataImport.setEmail(x.());
            dataImport.setName(fileData.getString("FIRST_NAME") + fileData.getString("MIDDLE_NAME") + fileData.getString("SURNAME") );
            dataImport.setFirstName(fileData.getString("FIRST_NAME"));
            dataImport.setLastName(fileData.getString("SURNAME"));
            dataImport.setGender(fileData.getString("GENDER_CD"));
            dataImport.setMobile(fileData.getString("PH_NBR1"));
            dataImport.setDataDate(fileData.getLong("DATA_DATE"));
            String age = DateUtils.getAgeFromBirthday("yyyymmdd", fileData.getString("DOB"));
            dataImport.setAge(age);
            dataImport.setSendDate(0L);
            dataImport.setType(ImportTypeEnum.FIlE);
            dataImports.add(dataImport);
        }
        dataImportRepository.saveAll(dataImports);
    }
}
