package com.cmc.moengagedataimport.services.bulkImport;

import com.cmc.moengagedataimport.dto.ResourceDto;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BulkImportService extends ApiService {

    @Value("${bulk.api.url}")
    private String url;

    @Value("${app.id}")
    private String userName;

    @Value("${secret.key}")
    private String password;

    private List<JSONObject> populateBulkAttributes(ResourceDto resourceDTO) {
        Map<String, List<SbfLoanPortfolio>> dataImport = resourceDTO.getDataImport();
        List<JSONObject> bulkAttribute = new ArrayList<>();
        boolean readingDone = false;
        for (Map.Entry<String, List<SbfLoanPortfolio>> entry : dataImport.entrySet()) {
            if (readingDone) {
                break;
            }
        List<JSONObject> jsonObjects = entry.getValue().stream().map(x -> {
            SbfLoanPortfolio a = x;
            return new JSONObject(x);
        }).collect(Collectors.toList());
        bulkAttribute.addAll(this.convertToLPDataBulk(jsonObjects));
        }
        return bulkAttribute;
    }

    public JSONObject createMainBulkObject(ResourceDto resourceDTO) {
        JSONObject mainBulkObj = new JSONObject();
        mainBulkObj.put("type", "transition");
        List<JSONObject> bulkAttribute = populateBulkAttributes(resourceDTO);
        mainBulkObj.put("elements", new JSONArray(bulkAttribute));
        return mainBulkObj;
    }

    public HttpStatus bulkImport(ResourceDto resourceDTO) throws JsonProcessingException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject mainBulkObject = createMainBulkObject(resourceDTO);
        String requestJson = mapper.writeValueAsString(mainBulkObject.toMap());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(this.userName, this.password);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);
        String response = null;
        HttpStatus status = HttpStatus.OK;
        try {
            response = restTemplate.postForObject(this.url, entity, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getStackTrace());
            status = e.getStatusCode();
        }
        return status;
    }
}
