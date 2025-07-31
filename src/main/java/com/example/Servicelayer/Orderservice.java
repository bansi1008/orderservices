package com.example.Servicelayer;
import com.example.model.Order;
import com.example.model.OrderItem;
import com.example.Repository.OrderRepository;

import com.example.Repository.OrderItemRepository;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.DTO.CartItemResponse;


@Service
public class Orderservice {
    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    

    public Orderservice(RestTemplate restTemplate, OrderRepository orderRepository,
                         OrderItemRepository orderItemRepository) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }
    

    public List<CartItemResponse> getCartItems(String jwtToken) {

         
    
        String url = "http://localhost:8082/cart/get";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
       ResponseEntity<CartItemResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                CartItemResponse[].class
        );
      

        return Arrays.asList(response.getBody());
    }

    public BigDecimal calculateOrderTotal(String jwtToken) {
    List<CartItemResponse> cartItems = getCartItems(jwtToken); // get from Cart Microservice

    BigDecimal orderTotal = BigDecimal.ZERO;

    for (CartItemResponse item : cartItems) {
        orderTotal = orderTotal.add(item.getTotalPrice()); // add each item's totalPrice
    }

    return orderTotal;
}

 public Order createOrder(String jwtToken, Long userId,String username) {
    List<CartItemResponse> cartItems = getCartItems(jwtToken);
    BigDecimal orderTotal = calculateOrderTotal(jwtToken);
    

    Order order = Order.builder()
                .userId(userId)
                .username(username)
                .status("PENDING")
                .totalAmount(orderTotal)
                .createdAt(LocalDateTime.now())
                .build();

                Order savedOrder = orderRepository.save(order);

       
      List<OrderItem> orderItems = cartItems.stream().map(item -> {
    OrderItem orderItem = new OrderItem();
    orderItem.setProductId(item.getProductId());
    orderItem.setUnitPrice(item.getPrice());
    orderItem.setQuantity(item.getQuantity());

   
    orderItem.setOrder(savedOrder);

    return orderItem;
}).collect(Collectors.toList());


        orderItemRepository.saveAll(orderItems);
        order.setItems(orderItems);

        return order;
   

    
 }

    
}
