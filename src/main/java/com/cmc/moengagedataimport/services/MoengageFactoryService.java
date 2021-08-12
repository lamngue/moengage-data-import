package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.services.bulkImport.BulkImportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MoengageFactoryService {

    @Autowired
    private CsvFileImportService csvFileImportService;

    @Autowired
    private ExcelFileImportService excelFileImportService;

    @Autowired
    private RedshiftClusterImportService redshiftClusterImportService;

    public List<DataImport> GetTypeInput(MultipartFile file) {
      if(file == null) {
          List<DataImport> dataImports = redshiftClusterImportService.getResources();
         return dataImports;
      }
      else {
        String fileType = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if(fileType.equals("csv")){
           return csvFileImportService.getResources(file);
        }
        else if (fileType.equals("xlsx") || fileType.equals("xls")) {
            return excelFileImportService.getResources(file);
        }
      }
      return null;
   }
}
