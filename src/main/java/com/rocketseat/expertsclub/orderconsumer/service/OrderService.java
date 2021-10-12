package com.rocketseat.expertsclub.orderconsumer.service;

import com.rocketseat.expertsclub.orderconsumer.sqs.SqsConsumer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class OrderService {

    private final SqsConsumer sqsConsumer;

    public Mono<Void> findAllOrders() {
        return sqsConsumer.consumerOrdersToProcessMessage();

    }

}
