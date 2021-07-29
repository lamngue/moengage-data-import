package com.cmc.moengagedataimport.rabbitMQ.publisher;

import com.cmc.moengagedataimport.entities.DataImport;
import com.cmc.moengagedataimport.rabbitMQ.config.MessagingConfig;
import com.cmc.moengagedataimport.repository.DataImportRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportDataPublisher {

    private final DataImportRepository dataImportRepository;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    public ImportDataPublisher(DataImportRepository dataImportRepository) {
        this.dataImportRepository = dataImportRepository;
    }

    public String sendDataToBulkImport() {
        List<DataImport> dataImportList = dataImportRepository.findAll();
        template.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, dataImportList);
        return "Success !!";
    }
}
