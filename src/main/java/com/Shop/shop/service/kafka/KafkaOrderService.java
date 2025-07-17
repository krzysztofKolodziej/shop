package com.Shop.shop.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaOrderService {

    @KafkaListener(topics = "order-events", groupId = "order-group")
    public void handleUserOrder(String message) {
        System.out.println("Received Message: " + message);
    }
}
