package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cmc.moengagedataimport.dto.ResourceDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CsvFileImportService  {
    Logger log = LoggerFactory.getLogger(this.getClass());
    public ResourceDto setResourceDTO(CSVParser csvParser, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ResourceDto resourceDTO = new ResourceDto();
        List<JSONObject> records = readValueToJsonObject(csvParser);
        Map<String, List<SbfLoanPortfolio>> resource = new HashMap<>();
        List<SbfLoanPortfolio> sbfLoanPortfolioList = records.stream().map(x -> {
            SbfLoanPortfolio sbfLoanPortfolio = null;
            try {
                log.info(x.toString());
                sbfLoanPortfolio = mapper.readValue(x.toString(), SbfLoanPortfolio.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return sbfLoanPortfolio;
        }).collect(Collectors.toList());
        resource.put(fileName, sbfLoanPortfolioList);
        resourceDTO.setDataImport(resource);
        return resourceDTO;
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
                objectJsonUser.put(header.get(j), row.get(j));
            }
            csvData.add(objectJsonUser);
        }
        return csvData;
    }

    public ResourceDto getResources(Object data) {
        MultipartFile csvFile = (MultipartFile) data;
        String fileName = csvFile.getOriginalFilename();
        CSVParser csvParser;
        ResourceDto resourceDto = null;
        try {
            BufferedReader fileReader = new BufferedReader(new
                    InputStreamReader(csvFile.getInputStream(), "UTF-8"));
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            resourceDto = setResourceDTO(csvParser, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourceDto;
    }

}
