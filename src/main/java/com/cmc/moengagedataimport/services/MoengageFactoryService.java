package com.cmc.moengagedataimport.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class MoengageFactoryService {

    @Autowired
    private CsvFileImportService csvFileImportService;

    @Autowired
    private ExcelFileImportService excelFileImportService;


    public String GetTypeInput(MultipartFile file) throws IOException {
        Path path = Paths.get("src/main/resources/log.txt"); //creates Path instance
        try
        {
            if(!Files.exists(path)){
                Files.createFile(path);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String response = "";
        if (file != null) {
            String fileType = StringUtils.getFilenameExtension(file.getOriginalFilename());
            if (fileType.equals("csv")) {
                response = csvFileImportService.getResources(file);
                response = file.getOriginalFilename() + response + LocalDateTime.now();
            } else if (fileType.equals("xlsx") || fileType.equals("xls")) {
                response = excelFileImportService.getResources(file);
                response = file.getOriginalFilename() + response + LocalDateTime.now();
            }
        }
        Files.write(path, response.getBytes(StandardCharsets.UTF_8));
        return response;
    }
}
