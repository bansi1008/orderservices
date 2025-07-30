package com.example.Servicelayer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;

import com.example.DTO.CartItemResponse;


@Service
public class Orderservice {
    private final RestTemplate restTemplate;

    public Orderservice(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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


    
}
