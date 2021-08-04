package com.cmc.moengagedataimport.controller;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.services.MoengageFactoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
public class InputController {

    @Autowired
    private MoengageFactoryService moengageFactoryService;

    @PostMapping("/import-data")
    public ModelAndView importExcel(@RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        List<DataImport> dataImports = moengageFactoryService.GetTypeInput(file);
        String resp = "";
        if (dataImports.size() == 0) {
            resp = "Unable to import. Please check file content or database connectivity";
        } else {
            resp = "Import Successfully";
        }
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("response", resp);
        return modelAndView;
    }
}
