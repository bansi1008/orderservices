package com.example.Controller;
import com.example.DTO.CartItemResponse;
import com.example.Servicelayer.Orderservice;
import com.example.utility.JWT;
import com.example.model.Order;

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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;



@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private Orderservice orderservice;

    @Autowired
    private JWT jwt;

   

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
     String username=jwt.getUsernameFromToken(token);
    System.out.println("Username: " + username);
   
        

        List<CartItemResponse> cartItems = orderservice.getCartItems(token);

        BigDecimal orderTotal = orderservice.calculateOrderTotal(token);

        
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("cartItems", cartItems);
        response.put("orderTotal", orderTotal);

        if (cartItems.isEmpty()) {
            response.put("message", "Cart is empty. Cannot place order.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Order order = orderservice.createOrder(token, userId,username);

        return ResponseEntity.ok(response);
    }




    
}
