package com.cmc.moengagedataimport.dto;

import lombok.Data;

@Data
public class MoengageRequestDto {
    private Long data_date;
    private Integer customer_id_number;
    private String name;
    private String first_name;
    private String last_name;
    private String email;
    private String gender;
    private Integer age;


}

