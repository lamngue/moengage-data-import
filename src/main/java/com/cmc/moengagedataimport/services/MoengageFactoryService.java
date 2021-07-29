package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Map<String, List<SbfLoanPortfolio>> GetTypeInput(MultipartFile file) {
      if(file == null) {
         return redshiftClusterImportService.getResources().getDataImport();
      }
      else {
        String fileType = file.getContentType();
        if(fileType.equals("text/csv")){
           return csvFileImportService.getResources(file).getDataImport();
        }
        else if (fileType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || fileType.equals("application/vnd.ms-excel")) {
            return excelFileImportService.getResources(file).getDataImport();
        }
      }
      return  null;
   }
}
