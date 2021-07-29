package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.dto.ResourceDto;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.services.bulkImport.BulkImportService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private BulkImportService bulkImportService;

    @Autowired
    private RedshiftClusterImportService redshiftClusterImportService;

    public Map<String, List<SbfLoanPortfolio>> GetTypeInput(MultipartFile file) throws JsonProcessingException {
      if(file == null) {
         ResourceDto resourceDto = redshiftClusterImportService.getResources();
         return resourceDto.getDataImport();
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
