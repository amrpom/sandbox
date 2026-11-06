import org.example.Order;
import org.example.OrderRepository;
import org.example.OrderService;
import org.example.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    private final Order order = new Order(1L, 100.0);


    @Test
    void completeOrder_shouldReturnTrue_whenPaymentSucceeds() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(paymentService.processPayment(100.0))
                .thenReturn(true);

        // Act
        boolean result = orderService.completeOrder(1L);

        // Assert
        assertTrue(result);

        verify(orderRepository).findById(1L);
        verify(paymentService).processPayment(100.0);
        verify(orderRepository).save(order);
    }


    @Test
    void completeOrder_shouldReturnFalse_whenPaymentFails() {
        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(paymentService.processPayment(100.0))
                .thenReturn(false);

        // Act
        boolean result = orderService.completeOrder(1L);

        // Assert
        assertFalse(result);

        verify(orderRepository).findById(1L);
        verify(paymentService).processPayment(100.0);
        verify(orderRepository, never()).save(any());
    }


    @Test
    void completeOrder_shouldThrowException_whenOrderDoesNotExist() {
        // Arrange
        when(orderRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.completeOrder(1L)
        );

        verify(paymentService, never()).processPayment(anyDouble());
        verify(orderRepository, never()).save(any());
    }
}