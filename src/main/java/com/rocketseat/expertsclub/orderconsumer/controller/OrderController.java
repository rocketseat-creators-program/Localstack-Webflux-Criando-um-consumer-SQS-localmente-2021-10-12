package com.rocketseat.expertsclub.orderconsumer.controller;


import com.rocketseat.expertsclub.orderconsumer.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> getAllOrders() {
        log.info("recebendo ordens para serem cobradas");
        return orderService.findAllOrders();
    }
}
