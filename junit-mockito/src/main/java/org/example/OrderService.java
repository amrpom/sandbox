package org.example;

public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public OrderService(
            OrderRepository orderRepository,
            PaymentService paymentService) {

        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    public boolean completeOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Order not found"));

        boolean paymentSuccessful =
                paymentService.processPayment(order.getAmount());

        if (!paymentSuccessful) {
            return false;
        }

        orderRepository.save(order);

        return true;
    }
}