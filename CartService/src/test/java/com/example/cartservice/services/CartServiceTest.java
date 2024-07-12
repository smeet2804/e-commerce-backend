package com.example.cartservice.services;

import com.example.cartservice.clients.OrderClient;
import com.example.cartservice.clients.ProductClient;
import com.example.cartservice.clients.UserClient;
import com.example.cartservice.dtos.*;
import com.example.cartservice.models.Cart;
import com.example.cartservice.models.CartItem;
import com.example.cartservice.models.Product;
import com.example.cartservice.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private UserClient userClient;

    @Mock
    private OrderClient orderClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addToCart_ShouldAddItem() {
        // Arrange
        AddToCartRequestDTO request = new AddToCartRequestDTO();
        request.setUserId(1L);
        request.setProductId("123");
        request.setQuantity(2);

        Cart cart = new Cart();
        cart.setUserId(1L);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setPrice(10.0);
        when(productClient.getProductPrice("123")).thenReturn(productPriceDTO);

        // Act
        CartResponseDTO response = cartService.addToCart(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("123", response.getItems().get(0).getProductId());
        assertEquals(2, response.getItems().get(0).getQuantity());
        assertEquals(20.0, response.getItems().get(0).getPrice());

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void reviewCart_ShouldReturnCartDetails() {
        // Arrange
        Long userId = 1L;
        Cart cart = new Cart();
        cart.setUserId(userId);
        CartItem cartItem = new CartItem();
        cartItem.setProductId("123");
        cartItem.setQuantity(2);
        cart.getItems().add(cartItem);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Product product = new Product();
        product.setName("Product 123");
        product.setDescription("Description for product 123");
        product.setPrice(10.0);
        when(productClient.getProductDetails("123")).thenReturn(product);

        // Act
        CartResponseDTO response = cartService.reviewCart(userId);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("Product 123", response.getItems().get(0).getProductName());
        assertEquals("Description for product 123", response.getItems().get(0).getProductDescription());
        assertEquals(20.0, response.getItems().get(0).getPrice());
    }

    @Test
    void checkout_ShouldCreateOrderAndDeleteCart() {
        // Arrange
        CheckoutRequestDTO request = new CheckoutRequestDTO();
        request.setUserId(1L);
        request.setPaymentMethod("Credit Card");
        request.setAddress("123 Main St");

        Cart cart = new Cart();
        cart.setUserId(1L);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        when(orderClient.createOrder(any(Cart.class))).thenReturn("orderId123");

        // Act
        String result = cartService.checkout(request);

        // Assert
        assertEquals("Order successfully created with orderId: orderId123", result);
        verify(cartRepository).delete(cart);
    }

    @Test
    void removeFromCart_ShouldRemoveItem() {
        // Arrange
        RemoveFromCartRequestDTO request = new RemoveFromCartRequestDTO();
        request.setUserId(1L);
        request.setProductId("123");

        Cart cart = new Cart();
        cart.setUserId(1L);
        CartItem cartItem = new CartItem();
        cartItem.setProductId("123");
        cartItem.setQuantity(2);
        cartItem.setPrice(20.0);
        cart.addItem(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        ProductPriceDTO productPriceDTO = new ProductPriceDTO();
        productPriceDTO.setPrice(10.0);
        when(productClient.getProductPrice("123")).thenReturn(productPriceDTO);

        // Act
        CartResponseDTO response = cartService.removeFromCart(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getItems().isEmpty());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void deleteCart_ShouldDeleteCart() {
        // Arrange
        Long userId = 1L;
        Cart cart = new Cart();
        cart.setUserId(userId);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        // Act
        cartService.deleteCart(userId);

        // Assert
        verify(cartRepository).delete(cart);
    }
}
