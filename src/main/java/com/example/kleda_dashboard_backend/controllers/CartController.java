package com.example.kleda_dashboard_backend.controllers;

import com.example.kleda_dashboard_backend.model.Cart;
import com.example.kleda_dashboard_backend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<List<Cart>> getAllActiveCarts() {
        return ResponseEntity.ok(cartService.getAllActiveCarts());
    }
}
