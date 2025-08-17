package com.beat.gcp_order_service.order.service;

import com.beat.gcp_order_service.order.dto.NotificationPayload;
import com.beat.gcp_order_service.order.dto.OrderCreateRequest;
import com.beat.gcp_order_service.order.dto.OrderResponse;
import com.beat.gcp_order_service.order.entity.OrderEntity;
import com.beat.gcp_order_service.order.repo.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final PubSubTemplate pubSubTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gcp.pubsub.topic-name}")
    private String topicName;

    public OrderService(OrderRepository repo, PubSubTemplate pubSubTemplate) {
        this.repo = repo;
        this.pubSubTemplate = pubSubTemplate;
    }

    public OrderResponse create(OrderCreateRequest req) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomerEmail(req.customerEmail());
        orderEntity.setDescription(req.description());
        orderEntity.setTotalAmount(req.totalAmount());
        orderEntity.setCurrency(req.currency());
        // status, id, createdAt set in @PrePersist
        OrderEntity savedOrder = repo.save(orderEntity);

        // Publish payload to Pub/Sub
        try {
            NotificationPayload notificationPayload = new NotificationPayload();
            notificationPayload.setTo(orderEntity.getCustomerEmail());
            notificationPayload.setSubject("Welcome mail from gcp");
            notificationPayload.setBody("Hurrah notification works!");
            notificationPayload.setContentType("<h1>hello</h1>");
            notificationPayload.setDescription(orderEntity.getDescription());
            notificationPayload.setTotalAmount(orderEntity.getTotalAmount());
            notificationPayload.setCurrency(orderEntity.getCurrency());

            String message = objectMapper.writeValueAsString(notificationPayload);
            pubSubTemplate.publish(topicName, message);
            System.out.println("Published message to topic: " + topicName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish message", e);
        }

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCustomerEmail(),
                savedOrder.getDescription(),
                savedOrder.getTotalAmount(),
                savedOrder.getCurrency(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt()
        );
    }
}

