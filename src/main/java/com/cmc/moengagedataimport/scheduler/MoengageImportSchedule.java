package com.cmc.moengagedataimport.scheduler;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.enums.QueueStatusEnum;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import com.cmc.moengagedataimport.services.bulkImport.BulkImportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@EnableScheduling
public class MoengageImportSchedule {
    Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DataImportRepository dataImportRepository;

    @Autowired
    private BulkImportService bulkImportService;

    @Scheduled(cron = "0 0/1 * * * *")
    public void dailyMoengageImport() throws JsonProcessingException {
        List<DataImport> dataImports = dataImportRepository.findTop100ByStatusIs(QueueStatusEnum.Waiting);
        if(dataImports.isEmpty()) return ;
        HttpStatus httpStatus = bulkImportService.bulkImport(dataImports);
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
