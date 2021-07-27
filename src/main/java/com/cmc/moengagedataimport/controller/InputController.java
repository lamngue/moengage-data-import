package com.cmc.moengagedataimport.controller;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.services.MoengageFactoryService;
import dto.ResourceDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
public class InputController {

    @Autowired
    private MoengageFactoryService moengageFactoryService;

    @PostMapping("/import-data")
    public Map<String, List<SbfLoanPortfolio>> importExcel(@RequestParam(value = "file", required = false) MultipartFile file, Model model) {
        return moengageFactoryService.GetTypeInput(file, model);
    }
}
