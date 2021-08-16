package com.cmc.moengagedataimport.controller;

import com.cmc.moengagedataimport.services.MoengageFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class InputController {

    @Autowired
    private MoengageFactoryService moengageFactoryService;


    @PostMapping("/import-data")
    public ModelAndView importExcel(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String resp = moengageFactoryService.GetTypeInput(file);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("response", resp);
        return modelAndView;
    }
}
