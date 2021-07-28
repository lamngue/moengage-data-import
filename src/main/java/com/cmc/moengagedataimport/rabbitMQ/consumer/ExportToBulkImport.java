package com.cmc.moengagedataimport.rabbitMQ.consumer;

import com.cmc.moengagedataimport.dto.ResourceDto;
import com.cmc.moengagedataimport.rabbitMQ.config.MessagingConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;

public class ExportToBulkImport {

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(List<String> recordIds) {
        //get records from mongo
    }
}
