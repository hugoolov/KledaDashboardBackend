package com.example.kleda_dashboard_backend.services;

import com.example.kleda_dashboard_backend.dtos.CartSummaryDTO;
import com.example.kleda_dashboard_backend.model.Cart;
import com.example.kleda_dashboard_backend.repos.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public List<Cart> getAllActiveCarts() {
        return cartRepository.findAll().stream()
                .filter(Cart::getIsActive)
                .collect(Collectors.toList());
    }

    public List<Cart> getCartsByBrandId(Long brandId) {
        return cartRepository.findByProductBrandIdAndIsActiveTrue(brandId);
    }

    public List<CartSummaryDTO> getCartSummaryByBrandId(Long brandId) {
        return cartRepository.findByProductBrandIdAndIsActiveTrue(brandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }

    private CartSummaryDTO convertToDTO(Cart cart) {
        return new CartSummaryDTO(
                cart.getId(),
                cart.getCustomerId(),
                cart.getProduct().getName(),
                cart.getProduct().getBrand().getName(),
                cart.getQuantity(),
                cart.getProduct().getPrice(),
                cart.getSubtotal(),
                cart.getAddedAt()
        );
    }
}
