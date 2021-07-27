package com.cmc.moengagedataimport.services;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import dto.ResourceDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Service
public class MoengageFactoryService {

    @Autowired
    private CsvFileImportService csvFileImportService;

    @Autowired
    private ExcelFileImportService excelFileImportService;

    @Autowired
    private RedshiftClusterImportService redshiftClusterImportService;

    public Map<String, List<SbfLoanPortfolio>> GetTypeInput(MultipartFile file, Model model) {

      if(file == null && model == null){
         return null;
      }
      if(file == null) {
         return redshiftClusterImportService.getResources(file).getDataImport();
      }
      else {
        String fileType = file.getContentType();
        if(fileType.equals("text/csv")){
           return csvFileImportService.getResources(file).getDataImport();
        }
        else {
            return excelFileImportService.getResources(file).getDataImport();
        }
      }
   }
}
