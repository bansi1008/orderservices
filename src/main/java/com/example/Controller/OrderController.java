package com.example.Controller;
import com.example.DTO.CartItemResponse;
import com.example.Servicelayer.Orderservice;
import com.example.utility.JWT;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.Arrays;
import java.util.List;



@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private Orderservice orderservice;

   

    @PostMapping("/create")
    public ResponseEntity<?> placeOrder() {

               Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
         if (authentication == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    Long userId = null;
    String token = null;

    if (authentication.getPrincipal() instanceof Long) {
        userId = (Long) authentication.getPrincipal();
    }

    if (authentication.getDetails() instanceof String) {
        token = (String) authentication.getDetails();
    }

        
    System.out.println("User ID: " + userId);
    System.out.println("Token: " + token);
   
        

        List<CartItemResponse> cartItems = orderservice.getCartItems(token);

      
        return ResponseEntity.ok(cartItems);
    }




    
}
