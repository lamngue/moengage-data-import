package com.cmc.moengagedataimport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
class DataImport {
    private Long id;
    private String record;
    private Long dataDate;
    private Long sendDate;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String gender;
    private String age;
}

public class MoengageMain {

    private static Dotenv dotenv = Dotenv.load();

    private static Logger log = LoggerFactory.getLogger(MoengageMain.class);
    private static String url = dotenv.get("BULK_API_URL");

    private static String userName = dotenv.get("MOENGAGE_DATAAPI_ID");

    private static String password = dotenv.get("MOENGAGE_DATAAPI_KEY");

    private static String portfolioFileName = "campaign";

    private static String cifFileName = "cif";

    private static List<String> loanPortfolioFieldName = Stream.of("customer_id_number","cust_first_name","cust_middle_name","cust_last_name","cust_gender","cust_mob_no","data_date","cust_birth_date")
            .collect(Collectors.toList());

    private static List<String> cifFieldName = Stream.of("cust_id1","first_name","middle_name","surname","gender_cd","ph_nbr1","data_date","dob")
            .collect(Collectors.toList());

    public static String getAgeFromBirthday(String strFormat, String birthday) {
        DateFormat dateFormat = new SimpleDateFormat(strFormat);
        Date birthDate = null;
        try {
            birthDate = dateFormat.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(birthDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        LocalDate l1 = LocalDate.of(year, month, date);
        LocalDate present = LocalDate.now();
        Period age = Period.between(l1, present);
        return String.valueOf(age.getYears());
    }

    public static String importFileData(List<JSONObject> fileDataList, String fileName) {
        List<String> fieldNameList;
        if(fileName.toLowerCase().contains(cifFileName)){
            fieldNameList = cifFieldName;
        }
        else if(fileName.toLowerCase().contains(portfolioFileName)){
            fieldNameList = loanPortfolioFieldName;
        }
        else {
            return null;
        }
        List<DataImport> dataImports = new ArrayList<>();
        for (JSONObject fileData : fileDataList) {
            DataImport dataImport = new DataImport();
            dataImport.setRecord(fileData.toString());
            dataImport.setId(Long.parseLong(fileData.get(fieldNameList.get(0)).toString()));
            dataImport.setName(fileData.getString(fieldNameList.get(1)) + fileData.getString(fieldNameList.get(2)) + fileData.getString(fieldNameList.get(3)) );
            dataImport.setFirstName(fileData.getString(fieldNameList.get(1)));
            dataImport.setLastName(fileData.getString(fieldNameList.get(3)));
            dataImport.setGender(fileData.getString(fieldNameList.get(4)));
            dataImport.setMobile(fileData.getString(fieldNameList.get(5)));
            dataImport.setDataDate(fileData.getLong(fieldNameList.get(6)));
            String age = getAgeFromBirthday("yyyymmdd", fileData.getString(fieldNameList.get(7)));
            dataImport.setAge(age);
            dataImport.setSendDate(0L);
            dataImports.add(dataImport);
        }
        String response;
        try {
            response = bulkImport(dataImports);
            return response;
        } catch(JsonProcessingException e) {
            log.error("Exception {}" , e);
        }
        return null;
    }

    public static String setDataImport(CSVParser csvParser, String fileName) throws IOException {
        List<JSONObject> records = readValueToJsonObject(csvParser);
        String response = importFileData(records, fileName);
        return response;
    }

    private static List<JSONObject> readValueToJsonObject(CSVParser csvParser) throws IOException {
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

    public static String getResources(Path path, String fileName) {
        CSVParser csvParser;
        String response = "";
        try {
            BufferedReader fileReader = Files.newBufferedReader(path);
            csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
            response = setDataImport(csvParser, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    private static List<JSONObject> populateBulkAttributes(List<DataImport> dataImports) {
        List<JSONObject> bulkAttributeList = new ArrayList<>();
        for (DataImport data : dataImports) {
            JSONObject customAttribute = new JSONObject(data.getRecord());
            JSONObject bulkAttribute = new JSONObject();
            customAttribute.put("name", data.getName());
            customAttribute.put("first_name", data.getFirstName());
            customAttribute.put("last_name", data.getLastName());
            customAttribute.put("email", data.getEmail());
            customAttribute.put("mobile", data.getMobile());
            customAttribute.put("gender", data.getGender());
            customAttribute.put("age", data.getAge());
            bulkAttribute.put("type", "customer");
            bulkAttribute.put("customer_id", data.getId());
            bulkAttribute.put("attributes", customAttribute);
            bulkAttributeList.add(bulkAttribute);
        }
        return bulkAttributeList;
    }

    public static JSONObject createMainBulkObject(List<DataImport> dataImports) {
        JSONObject mainBulkObj = new JSONObject();
        mainBulkObj.put("type", "transition");
        List<JSONObject> bulkAttribute = populateBulkAttributes(dataImports);
        mainBulkObj.put("elements", new JSONArray(bulkAttribute));
        return mainBulkObj;
    }

    public static String bulkImport(List<DataImport> dataImports) throws JsonProcessingException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject mainBulkObject = createMainBulkObject(dataImports);
        String requestJson = mapper.writeValueAsString(mainBulkObject.toMap());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(userName, password);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        String response;
        try {
            response = restTemplate.postForObject(url, entity, String.class);
            log.info("response" +response);
        } catch (HttpClientErrorException e) {
            response = e.toString();
        }
        return response;
    }

    public static void main(String[] args) {
        Path path = Paths.get("src/main/resources/csv/cif.csv");
        String response = getResources(path, "cif");
        log.info(response);
    }
}
