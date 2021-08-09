package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class CsvFileImportService  {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DataImportService dataImportService;

    public List<DataImport> setDataImport(CSVParser csvParser, String fileName) throws IOException {
        List<JSONObject> records = readValueToJsonObject(csvParser);
        List<DataImport> dataImports = dataImportService.importFileData(records, fileName);
        return dataImports;
    }

    private List<JSONObject> readValueToJsonObject(CSVParser csvParser) throws IOException {
        List<JSONObject> csvData = new ArrayList<>();
        List<CSVRecord> csvRecords = csvParser.getRecords();
        CSVRecord header = csvRecords.get(0);
        int rows = csvRecords.size();
        int cells = header.size();
        // Row
        for (int i = 1; i < rows; i++) {
            JSONObject objectJsonUser = new JSONObject();
            CSVRecord row = csvRecords.get(i);
            // Cell
            for (int j = 0; j < cells; j++) {
                objectJsonUser.put(header.get(j).toLowerCase(), row.get(j));
            }
            csvData.add(objectJsonUser);
        }
        return csvData;
    }

    public List<DataImport> getResources(Object data) {
        MultipartFile csvFile = (MultipartFile) data;
        String fileName = csvFile.getOriginalFilename();
        CSVParser csvParser;
        List<DataImport> dataImports = null;
        try {
            BufferedReader fileReader = new BufferedReader(new
                    InputStreamReader(csvFile.getInputStream(), "UTF-8"));
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            dataImports = setDataImport(csvParser, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataImports;
    }
}
