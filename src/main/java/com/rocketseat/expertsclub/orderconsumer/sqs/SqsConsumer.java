package com.rocketseat.expertsclub.orderconsumer.sqs;

import com.rocketseat.expertsclub.orderconsumer.configuration.AwsConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;


@Slf4j
@RequiredArgsConstructor
@Service
public class SqsConsumer {

    private final AwsConfiguration awsConfiguration;
    private final SqsAsyncClient sqsAsyncClient;


    public Mono<Void> consumerOrdersToProcessMessage() {
        Mono<ReceiveMessageResponse> receiveMessageResponseMono = Mono.fromFuture(() ->
                sqsAsyncClient.receiveMessage(
                        ReceiveMessageRequest.builder()
                                .queueUrl(awsConfiguration.getProcessOrderQueueUrl())
                                .waitTimeSeconds(1)
                                .visibilityTimeout(30)
                                .build()
                )
        );

        receiveMessageResponseMono
                .repeat()
                .retry()
                .map(ReceiveMessageResponse::messages)
                .map(Flux::fromIterable)
                .flatMap(messageFlux -> messageFlux)
                .subscribe(messageToProcess -> {
                    log.info("Processando mensagem para ordem ser cobrada, infos da ordem:  " + messageToProcess.body());

                    sqsAsyncClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(awsConfiguration.getProcessOrderQueueUrl()).receiptHandle(messageToProcess.receiptHandle()).build())
                            .thenAccept(deletedMessage -> {
                                log.info("mensagem  com o id  processada e excluida com sucesso" + messageToProcess.messageId());
                            });
                });
        return null;
    }

}
