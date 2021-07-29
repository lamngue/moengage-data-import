package com.cmc.moengagedataimport.services.bulkImport;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApiService {

    private static final Map<String, String> dataFields = Stream.of(new String[][] {
            { "cust_name", "name" },
            { "cust_first_name", "first_name" },
            { "cust_last_name", "last_name" },
            { "cust_email_id", "email" },
            { "cust_mob_no", "mobile" },
            { "cust_gender", "gender" },
            { "cust_birth_date", "age" }
    }).collect(Collectors.toConcurrentMap(data -> data[0], data -> data[1]));

    public JSONObject modifyAttributes(JSONObject dataObject) {
        dataObject.toMap().entrySet()
                .stream()
                .filter(entry -> dataFields.containsKey(entry.getKey().toLowerCase()))
                .forEach(entry -> {
                    String key = entry.getKey();
                    dataObject.put(dataFields.get(key.toLowerCase()).toLowerCase(), dataObject.get(key));
                    dataObject.remove(key);
                });
        return dataObject;
    }


    public List<JSONObject> convertToLPDataBulk(List<JSONObject> LPDataList) {
        List<JSONObject> LPJsonList = new ArrayList<>();
        for (JSONObject LPData : LPDataList) {
            JSONObject LPJson = new JSONObject();
            LPData = modifyAttributes(LPData);
            LPJson.put("type", "customer");
            Map<String, Object> a = LPData.toMap();
            String customerId = LPData.toMap().get("customer_id_number").toString();
            LPJson.put("customer_id", customerId);
            LPJson.put("attributes", LPData);
            LPJsonList.add(LPJson);
        }
        return LPJsonList;
    }

}
