package com.cmc.moengagedataimport.services;

import com.cmc.moengagedataimport.entities.DataImport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelFileImportService {
    @Autowired
    private DataImportService dataImportService;

    @Value("${file.fileName.loan_portfolio}")
    private String portfolioFileName;

    @Value("${file.fileName.sbf_cif}")
    private String cifFileName;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private List<String> getSheetNames(XSSFWorkbook workbook) {
        List<String> sheetNames = new ArrayList<>();
        for (int i=0; i<workbook.getNumberOfSheets(); i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }

    public List<DataImport> setDataImport(XSSFWorkbook workbook, List<String> sheetNames) {
        List<DataImport> dataImports = new ArrayList<>();
          for (String sheetName : sheetNames) {
            if (sheetName.toLowerCase().contains(portfolioFileName) || sheetName.toLowerCase().contains(cifFileName)) {
                XSSFSheet worksheet = workbook.getSheet(sheetName);
                List<JSONObject> listJsonObject = this.readValueToJsonObject(worksheet);
                dataImports = dataImportService.importFileData(listJsonObject, sheetName);
                break;
            }
          }
          return dataImports;
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
            if(cells == 0){
                break;
            }
            for (int j = 0; j < cells; j++) {
                // Numeric cell
                if (dataRow.getCell(j).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    objectJsonUser.put(headerRow.getCell(j).getStringCellValue().toLowerCase(),
                            dataRow.getCell(j).getNumericCellValue());
                    // Boolean Cell
                } else if (dataRow.getCell(j).getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                    objectJsonUser.put(headerRow.getCell(j).getStringCellValue().toLowerCase(),
                            dataRow.getCell(j).getBooleanCellValue());
                    // Formula Cell
                } else if (dataRow.getCell(j).getCellType() == Cell.CELL_TYPE_FORMULA) {
                    if (dataRow.getCell(j).getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC) {
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue().toLowerCase(),
                                dataRow.getCell(j).getNumericCellValue());
                    }
                    // String Cell
                }
                else {
                    String cellDataValue = dataRow.getCell(j).getStringCellValue();
                    // Array Object Json
                    if (cellDataValue.contains("[{")) {
                        JSONArray jsonArray = new JSONArray(cellDataValue);
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue().toLowerCase(), jsonArray);
                    } else if (cellDataValue.contains("{")) {
                        JSONObject jsonObject = new JSONObject(cellDataValue);
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue().toLowerCase(), jsonObject);
                    } else {
                        objectJsonUser.put(headerRow.getCell(j).getStringCellValue().toLowerCase(), cellDataValue);
                    }
                }
            }
            listJSONObject.add(objectJsonUser);
        }
        return listJSONObject;
    }

    public List<DataImport> getResources(Object object) {
        MultipartFile excelFile = (MultipartFile) object;
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(excelFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> sheetNames = getSheetNames(workbook);
        List<DataImport> dataImports = setDataImport(workbook, sheetNames);
        return dataImports;
    }
}
