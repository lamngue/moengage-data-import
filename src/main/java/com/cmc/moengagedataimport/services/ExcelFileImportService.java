package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cmc.moengagedataimport.dto.ResourceDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelFileImportService {
    private List<String> getSheetNames(XSSFWorkbook workbook) {
        List<String> sheetNames = new ArrayList<>();
        for (int i=0; i<workbook.getNumberOfSheets(); i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }

    public ResourceDto setResourceDTO(XSSFWorkbook workbook, List<String> sheetNames) {
        ResourceDto resourceDTO = new ResourceDto();
        Map<String, List<SbfLoanPortfolio>> sheetsInFile = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        for (String sheetName : sheetNames) {
            if (sheetName.equals("Sheet 1")) {
                XSSFSheet worksheet = workbook.getSheet(sheetName);
                List<JSONObject> listJsonObject = this.readValueToJsonObject(worksheet);
                List<SbfLoanPortfolio> sbfLoanPortfolioList = listJsonObject.stream().map(x -> {
                    SbfLoanPortfolio sbfLoanPortfolio = null;
                    try {
                        sbfLoanPortfolio = mapper.readValue(x.toString(), SbfLoanPortfolio.class );
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return  sbfLoanPortfolio;
                }).collect(Collectors.toList());
                sheetsInFile.put(sheetName, sbfLoanPortfolioList);
            }
        }
        resourceDTO.setDataImport(sheetsInFile);
        return resourceDTO;
    }

    private List<JSONObject> readValueToJsonObject(XSSFSheet worksheet) {
        List<JSONObject> listJSONObject = new ArrayList<>();
        Row headerRow = worksheet.getRow(0);
        int rows = worksheet.getPhysicalNumberOfRows();
        // Row
        for (int i = 1; i < rows; i++) {
            JSONObject objectJsonUser = new JSONObject();
            Row dataRow = worksheet.getRow(i);
            boolean skippedRow = false;
            int cells = dataRow.getPhysicalNumberOfCells();
            // Cell
            for (int j = 0; j < cells; j++) {
                if (headerRow.getCell(j).getStringCellValue().equals("Data Date") ) {
                    skippedRow = true;
                    break;
                }
                // Numeric cell
                if (dataRow.getCell(j).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    objectJsonUser.put(headerRow.getCell(j).getStringCellValue(),
                            dataRow.getCell(j).getNumericCellValue());
                    // Boolean Cell
                } else if (dataRow.getCell(j).getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    objectJsonUser.put(headerRow.getCell(j).getStringCellValue(),
                            dataRow.getCell(j).getBooleanCellValue());
                    // Formula Cell
                } else if (dataRow.getCell(j).getCellType() == Cell.CELL_TYPE_FORMULA) {
                    if (dataRow.getCell(j).getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue(),
                                dataRow.getCell(j).getNumericCellValue());
                    }
                    // String Cell
                }
                else {
                    String cellDataValue = dataRow.getCell(j).getStringCellValue();
                    // Array Object Json
                    if (cellDataValue.contains("[{")) {
                        JSONArray jsonArray = new JSONArray(cellDataValue);
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue(), jsonArray);
                    } else if (cellDataValue.contains("{")) {
                        JSONObject jsonObject = new JSONObject(cellDataValue);
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue(), jsonObject);
                    } else {
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue(), cellDataValue);
                    }
                }
            }
            if (skippedRow) {
                continue;
            }
            listJSONObject.add(objectJsonUser);
        }
        return listJSONObject;
    }

    public ResourceDto getResources(Object object) {
        MultipartFile excelFile = (MultipartFile) object;
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(excelFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> sheetNames = getSheetNames(workbook);
        ResourceDto resourceDTO = setResourceDTO(workbook, sheetNames);
        return resourceDTO;
    }
}
