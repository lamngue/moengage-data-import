package com.cmc.moengagedataimport.scheduler;

import com.cmc.moengagedataimport.dto.ResourceDto;
import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import com.cmc.moengagedataimport.enums.QueueStatusEnum;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.cmc.moengagedataimport.services.bulkImport.BulkImportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@EnableScheduling
public class MoengageImportSchedule {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DataImportRepository dataImportRepository;

    @Autowired
    private BulkImportService bulkImportService;

    @Scheduled(cron = "0 34 16 * * *")
    public void dailyMoengageImport () throws JsonProcessingException {
        log.info("hello");
        List<DataImport> dataImports = dataImportRepository.findTop100ByStatusIsOrStatusIs(QueueStatusEnum.Waiting, QueueStatusEnum.Failed);
        ResourceDto resourceDto = new ResourceDto();
        Map<String, List<SbfLoanPortfolio>> resource = new HashMap<>();
        List<SbfLoanPortfolio> sbfLoanPortfolioList = dataImports.stream().map(x -> x.getRecord()).collect(Collectors.toList());
        resource.put("mongo", sbfLoanPortfolioList);
        resourceDto.setDataImport(resource);
        HttpStatus httpStatus =bulkImportService.bulkImport(resourceDto);
        QueueStatusEnum queueStatus;
        if(httpStatus.equals(HttpStatus.OK)){
            queueStatus= QueueStatusEnum.Success;
        } else {
            queueStatus = QueueStatusEnum.Failed;
        }
        for (DataImport data: dataImports) {
            data.setStatus(queueStatus);
        }
        dataImportRepository.saveAll(dataImports);
    }
}
