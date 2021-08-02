package com.cmc.moengagedataimport.services.bulkImport;

import com.cmc.moengagedataimport.dto.ResourceDto;
import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BulkImportService {

    @Value("${bulk.api.url}")
    private String url;

    @Value("${app.id}")
    private String userName;

    @Value("${secret.key}")
    private String password;

    private List<JSONObject> populateBulkAttributes(List<DataImport> dataImports) {
        List<JSONObject> bulkAttributeList = new ArrayList<>();
        for (DataImport data : dataImports) {
        JSONObject customAttribute = data.getRecord();
        JSONObject bulkAttribute = new JSONObject();
        bulkAttribute.put("name", data.getName());
        bulkAttribute.put("first_name", data.getFirstName());
        bulkAttribute.put("lastName", data.getLastName());
        bulkAttribute.put("email", data.getEmail());
        bulkAttribute.put("mobile", data.getMobile());
        bulkAttribute.put("gender", data.getGender());
        bulkAttribute.put("age", data.getAge());
        bulkAttribute.put("type", "customer");
        bulkAttribute.put("customer_id", data.getId());
        bulkAttribute.put("attributes", customAttribute);
        bulkAttributeList.add(bulkAttribute);
        }
        return bulkAttributeList;
    }

    public JSONObject createMainBulkObject(List<DataImport> dataImports) {
        JSONObject mainBulkObj = new JSONObject();
        mainBulkObj.put("type", "transition");
        List<JSONObject> bulkAttribute = populateBulkAttributes(dataImports);
        mainBulkObj.put("elements", new JSONArray(bulkAttribute));
        return mainBulkObj;
    }

    public HttpStatus bulkImport(List<DataImport> dataImports) throws JsonProcessingException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject mainBulkObject = createMainBulkObject(dataImports);
        String requestJson = mapper.writeValueAsString(mainBulkObject.toMap());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(this.userName, this.password);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        HttpStatus status = HttpStatus.OK;
        try {
            String response = restTemplate.postForObject(this.url, entity, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStackTrace());
            status = e.getStatusCode();
        }
        return status;
    }
}
